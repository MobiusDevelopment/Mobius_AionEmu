/*
 * This file is part of the Aion-Emu project.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package com.aionemu.gameserver.services.craft;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.StaticObject;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.recipe.Component;
import com.aionemu.gameserver.model.templates.recipe.ComponentElement;
import com.aionemu.gameserver.model.templates.recipe.RecipeTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AETHERFORGING_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemService.ItemUpdatePredicate;
import com.aionemu.gameserver.skillengine.task.CraftingTask;
import com.aionemu.gameserver.skillengine.task.MorphingTask;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke, sphinx, synchro2
 */
public class CraftService
{
	public static void finishCrafting(Player player, RecipeTemplate recipetemplate, int critCount, int bonus)
	{
		if (recipetemplate.getMaxProductionCount() != null)
		{
			player.getRecipeList().deleteRecipe(player, recipetemplate.getId());
			if (critCount == 0)
			{
				QuestEngine.getInstance().onFailCraft(new QuestEnv(null, player, 0, 0), recipetemplate.getComboProduct(1) == null ? 0 : recipetemplate.getComboProduct(1));
			}
		}
		int xpReward = (int) (((0.008 * (recipetemplate.getSkillpoint() + 100) * (recipetemplate.getSkillpoint() + 100)) + 60));
		xpReward = xpReward + ((xpReward * bonus) / 100);
		final int productItemId = critCount > 0 ? recipetemplate.getComboProduct(critCount) : recipetemplate.getProductid();
		ItemService.addItem(player, productItemId, recipetemplate.getQuantity(), new ItemUpdatePredicate()
		{
			@Override
			public boolean changeItem(Item item)
			{
				if (item.getItemTemplate().isWeapon() || item.getItemTemplate().isArmor())
				{
					item.setItemCreator(player.getName());
				}
				/**
				 * High Daeva-only Items obtainable by Morphing Rune Tribe items. Items from the Rune Tribe Set that can be purchased with Ancient Coins, can be Morphed into stronger versions. Morph product will already be +5. http://www.aionpowerbook.com/powerbook/Rune_Hero%27s_Set
				 */
				// To do linked craft "Morphing Substance" = skillId: "40009"
				// Only this craft can enchant item archdaeva + 5
				if (item.isArchDaevaItem())
				{
					item.setEnchantLevel(item.getEnchantLevel() + 5);
					item.setPersistentState(PersistentState.UPDATE_REQUIRED);
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, item));
				}
				return true;
			}
		});
		final ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(productItemId);
		final int gainedCraftExp = (int) RewardType.CRAFTING.calcReward(player, xpReward);
		final int skillId = recipetemplate.getSkillid();
		if ((skillId == 40001) || (skillId == 40002) || (skillId == 40003) || (skillId == 40004) || (skillId == 40007) || (skillId == 40008) || (skillId == 40010))
		{
			if ((player.getSkillList().getSkillLevel(skillId) >= 500) && (recipetemplate.getSkillpoint() < 500))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DONT_GET_COMBINE_EXP_GRAND_MASTER);
			}
			else if ((player.getSkillList().getSkillLevel(skillId) >= 400) && (recipetemplate.getSkillpoint() < 400))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DONT_GET_COMBINE_EXP);
			}
			else
			{
				if (player.getSkillList().addSkillXp(player, recipetemplate.getSkillid(), gainedCraftExp, recipetemplate.getSkillpoint()))
				{
					player.getCommonData().addExp(xpReward, RewardType.CRAFTING);
				}
				else
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DONT_GET_PRODUCTION_EXP(new DescriptionId(DataManager.SKILL_DATA.getSkillTemplate(recipetemplate.getSkillid()).getNameId())));
				}
			}
		}
		if (recipetemplate.getCraftDelayId() != null)
		{
			player.getCraftCooldownList().addCraftCooldown(recipetemplate.getCraftDelayId(), recipetemplate.getCraftDelayTime());
		}
	}
	
	public static void startCrafting(Player player, int recipeId, int targetObjId, int craftType)
	{
		final RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);
		final int skillId = recipeTemplate.getSkillid();
		final VisibleObject target = player.getKnownList().getObject(targetObjId);
		final ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());
		if (recipeTemplate.getDp() != null)
		{
			player.getCommonData().addDp(-recipeTemplate.getDp());
		}
		if (skillId == 40009)
		{
			player.setCraftingTask(new MorphingTask(player, (StaticObject) target, recipeTemplate));
		}
		else
		{
			final int skillLvlDiff = player.getSkillList().getSkillLevel(skillId) - recipeTemplate.getSkillpoint();
			player.setCraftingTask(new CraftingTask(player, (StaticObject) target, recipeTemplate, skillLvlDiff, craftType == 1 ? 15 : 0));
		}
		player.getCraftingTask().start();
	}
	
	public static void stopAetherforging(Player player, int recipeId)
	{
		final RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);
		final ItemUseObserver Aetherforging = new ItemUseObserver()
		{
			@Override
			public void abort()
			{
			}
		};
		player.getObserveController().removeObserver(Aetherforging);
		player.getController().cancelTask(TaskId.ITEM_USE);
		PacketSendUtility.sendPacket(player, new SM_AETHERFORGING_ANIMATION(player, 0, 0, 1));
	}
	
	public static void startAetherforging(Player player, int recipeId, int craftType)
	{
		final RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);
		final int delayedTime = 4000;
		final int skillLvl = 0;
		final int skillId = recipeTemplate.getSkillid();
		final ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(recipeTemplate.getProductid());
		PacketSendUtility.broadcastPacket(player, new SM_AETHERFORGING_ANIMATION(player, recipeTemplate.getId(), delayedTime, 0), true);
		
		if (!checkMagicCraft(player, recipeTemplate, itemTemplate))
		{
			stopAetherforging(player, skillId);
			return;
		}
		
		final ItemUseObserver Aetherforging = new ItemUseObserver()
		{
			@Override
			public void abort()
			{
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.broadcastPacket(player, new SM_AETHERFORGING_ANIMATION(player, recipeTemplate.getId(), 0, 1), true);
			}
		};
		player.getObserveController().attach(Aetherforging);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				final int xpReward = (int) (((0.008 * (recipeTemplate.getSkillpoint() + 100) * (recipeTemplate.getSkillpoint() + 100)) + 80));
				final int gainedCraftExp = (int) RewardType.CRAFTING.calcReward(player, xpReward);
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(Aetherforging);
				ItemService.addItem(player, recipeTemplate.getProductid(), recipeTemplate.getQuantity(), new ItemUpdatePredicate(ItemAddType.AETHERFORGING, ItemUpdateType.INC_ITEM_COLLECT));
				if ((Rnd.get(1, 6) == 6) && (player.getSkillList().getSkillLevel(40011) != 300))
				{
					player.getSkillList().addSkill(player, 40011, player.getSkillList().getSkillLevel(40011) + 1);
				}
				PacketSendUtility.sendPacket(player, new SM_AETHERFORGING_ANIMATION(player, recipeTemplate.getId(), 0, 2));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_CRAFT_SUCCESS_GETEXP);
			}
		}, delayedTime));
	}
	
	private static boolean checkMagicCraft(Player player, RecipeTemplate recipeTemplate, ItemTemplate itemTemplate)
	{
		if (recipeTemplate == null)
		{
			return false;
		}
		if (itemTemplate == null)
		{
			return false;
		}
		if ((player.getCraftingTask() != null) && (player.getCraftingTask().isInProgress()))
		{
			return false;
		}
		return true;
	}
	
	@SuppressWarnings("rawtypes")
	public static void checkComponents(Player player, int recipeId, int itemId, int materialsCount)
	{
		final RecipeTemplate recipeTemplate = DataManager.RECIPE_DATA.getRecipeTemplateById(recipeId);
		if (recipeTemplate.getComponent() != null)
		{
			final HashMap<Integer, Integer> hm = new HashMap<>();
			for (Component a : recipeTemplate.getComponent())
			{
				for (ComponentElement b : a.getComponents())
				{
					if (b.getItemid().equals(itemId))
					{
						hm.put(itemId, b.getQuantity());
					}
				}
			}
			final Set<Entry<Integer, Integer>> set = hm.entrySet();
			final Iterator<Entry<Integer, Integer>> i = set.iterator();
			while (i.hasNext())
			{
				final Map.Entry me = i.next();
				if (!player.getInventory().decreaseByItemId((Integer) me.getKey(), (Integer) me.getValue()))
				{
					return;
				}
			}
		}
	}
	
	private static int getBonusReqItem(int skillId)
	{
		switch (skillId)
		{
			case 40001: // Cooking.
			{
				return 169401081;
			}
			case 40002: // Weaponsmithing.
			{
				return 169401076;
			}
			case 40003: // Armorsmithing.
			{
				return 169401077;
			}
			case 40004: // Tailoring.
			{
				return 169401078;
			}
			case 40007: // Alchemy.
			{
				return 169401080;
			}
			case 40008: // Handicrafting.
			{
				return 169401079;
			}
			case 40010: // Menuisier.
			{
				return 169401082;
			}
		}
		return 0;
	}
}