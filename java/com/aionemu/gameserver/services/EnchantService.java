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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatEnchantFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.stats.listeners.ItemEquipmentListener;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.EnchantType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemEnchantTemplate;
import com.aionemu.gameserver.model.templates.item.ItemQuality;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.actions.EnchantItemAction;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemSocketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.RndArray;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author Ranastic (Encom)
 */

public class EnchantService
{
	private static final Logger log = LoggerFactory.getLogger(EnchantService.class);
	
	public static boolean breakItem(Player player, Item targetItem)
	{
		final Storage inventory = player.getInventory();
		final int kinah = 20000;
		final int stone = 188100335; // Enchantment Stone Dust.
		if (inventory.getItemByObjId(targetItem.getObjectId()) == null)
		{
			return false;
		}
		final ItemTemplate itemTemplate = targetItem.getItemTemplate();
		int quality = itemTemplate.getItemQuality().getQualityId();
		if (!itemTemplate.isArmor() && !itemTemplate.isWeapon())
		{
			AuditLogger.info(player, "Player try break dont compatible item type.");
			return false;
		}
		if (!itemTemplate.isArmor() && !itemTemplate.isWeapon())
		{
			AuditLogger.info(player, "Break item hack, armor/weapon iD changed.");
			return false;
		}
		if (player.getInventory().getKinah() < kinah)
		{
			return false;
		}
		if (player.getInventory().getKinah() >= kinah)
		{
			player.getInventory().decreaseKinah(kinah);
		}
		if (itemTemplate.isSoulBound() && !itemTemplate.isArmor())
		{
			quality += 1;
		}
		else if (!itemTemplate.isSoulBound() && itemTemplate.isArmor())
		{
			quality -= 1;
		}
		int number = 0;
		switch (quality)
		{
			case 0: // JUNK.
			case 1: // COMMON.
				number = Rnd.get(50, 200);
				break;
			case 2: // RARE.
				number = Rnd.get(200, 400);
				break;
			case 3: // LEGEND.
				number = Rnd.get(400, 600);
				break;
			case 4: // UNIQUE.
				number = Rnd.get(600, 800);
				break;
			case 5: // EPIC.
				number = Rnd.get(800, 1000);
				break;
			case 6: // MYTHIC.
			case 7:
				number = Rnd.get(1000, 2000);
				break;
		}
		// Extracting Archdaeva equipment will give Enchantment Stone Dust and Archdaeva crafting materials.
		if (targetItem.isArchDaevaItem())
		{
			ItemService.addItem(player, RndArray.get(archDaevaStoneItems), 1);
		}
		final int enchantItemId = stone;
		if (inventory.delete(targetItem) != null)
		{
			ItemService.addItem(player, enchantItemId, number);
		}
		else
		{
			AuditLogger.info(player, "Possible break item hack, do not remove item.");
		}
		return true;
	}
	
	public static int BreakKinah(Item item)
	{
		return 20000;
	}
	
	// TODO apply additional stat for skill on each stigma @@!
	public static void stigmaEnchant(Player player, Item parentItem, Item targetItem)
	{
		if (targetItem.getEnchantLevel() >= 5)
		{
			// You cannot enchant %0 any further.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_IT_CAN_NOT_BE_ENCHANTED_MORE_TIME(targetItem.getNameId()));
			return;
		}
		final int chance = 100 - targetItem.getEnchantLevel();
		final boolean isStigmaSuccess;
		if (chance >= 80)
		{
			isStigmaSuccess = true;
		}
		else
		{
			isStigmaSuccess = false;
		}
		if (player.getAccessLevel() > 0)
		{
			PacketSendUtility.sendMessage(player, "Success! Stigma: " + chance + " Lucky" + chance);
		}
		final int parentItemId = parentItem.getItemId();
		final int parntObjectId = parentItem.getObjectId();
		final int parentNameId = parentItem.getNameId();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItemId, 5000, 0, 0), true);
		final ItemUseObserver observer = new ItemUseObserver()
		{
			@Override
			public void abort()
			{
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.removeItemCoolDown(parentItem.getItemTemplate().getUseLimits().getDelayId());
				// Stigma enchantment of %0 has been cancelled.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_CANCEL(new DescriptionId(parentNameId)));
				PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 2, 0), true);
				player.getObserveController().removeObserver(this);
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (isStigmaSuccess)
				{
					player.getObserveController().removeObserver(observer);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parntObjectId, parentItemId, 0, 1, 1), true);
					player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
					targetItem.setEnchantLevel(targetItem.getEnchantLevel() + 1);
					targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
					// You have successfully enchanted %0 and the Stigma's enchantment level has increased by 1 level.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_SUCCESS(new DescriptionId(parentNameId)));
				}
				else
				{
					player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
					targetItem.setEnchantLevel(0);
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
					// You have failed to enchant %0 and the Stigma has been destroyed.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_STIGMA_ENCHANT_FAIL(new DescriptionId(parentNameId)));
				}
				PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 0, isStigmaSuccess ? 1 : 2, 0));
			}
		}, 5000));
	}
	
	public static boolean enchantItem(Player player, Item parentItem, Item targetItem, Item supplementItem)
	{
		final ItemTemplate enchantStone = parentItem.getItemTemplate();
		int enchantStoneLevel = enchantStone.getLevel();
		final int targetItemLevel = targetItem.getItemTemplate().getLevel();
		final int enchantItemLevel = targetItem.getEnchantLevel() + 1;
		int qualityCap = 0;
		final ItemQuality quality = targetItem.getItemTemplate().getItemQuality();
		switch (quality)
		{
			case JUNK:
			case COMMON:
				qualityCap = 5;
				break;
			case RARE:
				qualityCap = 10;
				break;
			case LEGEND:
				qualityCap = 15;
				break;
			case UNIQUE:
				qualityCap = 20;
				break;
			case EPIC:
				qualityCap = 25;
				break;
			case MYTHIC:
				qualityCap = 30;
				break;
		}
		float success = EnchantsConfig.ENCHANT_ITEM;
		switch (parentItem.getItemId())
		{
			// Enhances the basic attributes of armor or weapons.
			// Activate by double-clicking and selecting an item to enchant.
			case 166000196: // Enchantment Stone.
				enchantStoneLevel = Rnd.get(105, 190);
				break;
		}
		final int levelDiff = enchantStoneLevel - targetItemLevel;
		success += levelDiff > 0 ? (levelDiff * 3f) / qualityCap : 0;
		success += levelDiff - qualityCap;
		success -= (targetItem.getEnchantLevel() * qualityCap) / (enchantItemLevel > 10 ? 5f : 6f);
		if (supplementItem != null)
		{
			int supplementUseCount = 1;
			final ItemTemplate supplementTemplate = supplementItem.getItemTemplate();
			float addSuccessRate = 0f;
			EnchantItemAction action = supplementTemplate.getActions().getEnchantAction();
			if (action != null)
			{
				if (action.isManastoneOnly())
				{
					return false;
				}
				addSuccessRate = action.getChance() * 2;
			}
			action = enchantStone.getActions().getEnchantAction();
			if (action != null)
			{
				supplementUseCount = action.getCount();
			}
			if (enchantItemLevel > 10)
			{
				supplementUseCount = supplementUseCount * 2;
			}
			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount)
			{
				return false;
			}
			switch (parentItem.getItemTemplate().getItemQuality())
			{
				case RARE:
					addSuccessRate *= EnchantsConfig.LESSER_SUP;
					break;
				case LEGEND:
				case UNIQUE:
					addSuccessRate *= EnchantsConfig.REGULAR_SUP;
					break;
				case EPIC:
				case MYTHIC:
					addSuccessRate *= EnchantsConfig.GREATER_SUP;
					break;
				default:
					break;
			}
			success += addSuccessRate;
			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}
		if (targetItem.isAmplified() && enchantStone.isAmplificationStone())
		{
			success += 180 - (targetItem.getEnchantLevel() * 1.0f);
		}
		if (success >= 95)
		{
			success = 85;
		}
		if (targetItem.isArchDaevaItem())
		{
			success = 75;
		}
		
		boolean result = false;
		final float random = Rnd.get(1, 1000) / 10f;
		if (random <= success)
		{
			result = true;
		}
		if (player.getAccessLevel() > 0)
		{
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}
		return result;
	}
	
	public static void enchantItemAct(Player player, Item parentItem, Item targetItem, Item supplementItem, int currentEnchant, boolean result)
	{
		int addLevel = 1;
		int critLevel = 1;
		final int EnchantKinah = EnchantService.EnchantKinah(targetItem);
		final int rnd = Rnd.get(100);
		switch (parentItem.getItemId())
		{
			case 166020000: // Omega Enchantment Stone.
			case 166020001: // [Event] Omega Enchantment Stone (10 Min)
			case 166020002: // [Event] Omega Enchantment Stone (3 Days)
			case 166020003: // [Event] Omega Enchantment Stone.
			case 166020004: // [Event] Empyrean Lord's Enchantment Stone (7 Days)
			case 166020005: // [Event] Enchantment Stone Of The Empyrean Lord.
			case 166020006: // Omega Enchantment Stone.
				if (rnd < 10)
				{
					addLevel = 3;
				}
				else if (rnd < 35)
				{
					addLevel = 2;
				}
				break;
			case 166022000: // Irridescent Omega Enchantment Stone.
			case 166022001: // [Event] Irridescent Omega Enchantment Stone (7 Days)
			case 166022002: // [Event] Irridescent Omega Enchantment Stone.
			case 166022003: // Omega Enchantment Stone.
			case 166022007: // Omega Enchantment Stone.
				if (rnd < 7)
				{
					addLevel = 3;
					critLevel = 2;
				}
				else if (rnd < 25)
				{
					addLevel = 3;
				}
				else if (rnd <= 100)
				{
					addLevel = 2;
				}
				break;
			default:
				if (rnd < 2)
				{
					addLevel = 3;
				}
				else if (rnd < 7)
				{
					addLevel = 2;
					critLevel = 2;
				}
		}
		final ItemQuality targetQuality = targetItem.getItemTemplate().getItemQuality();
		if (!player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1))
		{
			AuditLogger.info(player, "Possible enchant hack, do not remove enchant stone.");
			return;
		}
		if (targetItem.isAmplified() && (player.getInventory().getKinah() >= EnchantKinah))
		{
			player.getInventory().decreaseKinah(EnchantKinah);
		}
		else if (targetItem.isAmplified() && (player.getInventory().getKinah() < EnchantKinah))
		{
			AuditLogger.info(player, "Possible enchant hack, Not depleted");
			return;
		}
		player.updateSupplements();
		if (result || player.isGM())
		{
			switch (targetQuality)
			{
				case JUNK:
				case COMMON:
				case RARE:
				case LEGEND:
					if ((currentEnchant > 10) && !targetItem.isAmplified())
					{
						currentEnchant = 10;
					}
					else if (targetItem.isAmplified() && (parentItem.getItemId() >= 166020000) && (parentItem.getItemId() <= 166020006) && (parentItem.getItemId() == 166000196))
					{
						currentEnchant += 1;
					}
					else if (targetItem.isAmplified() && (parentItem.getItemId() >= 166022000) && (parentItem.getItemId() <= 166022007))
					{
						currentEnchant += critLevel;
					}
					else if ((currentEnchant == 10) && !targetItem.isAmplified())
					{
						return;
					}
					else if ((currentEnchant + addLevel) <= 10)
					{
						currentEnchant += addLevel;
					}
					else if (((addLevel - 1) > 1) && (((currentEnchant + addLevel) - 1) <= 10))
					{
						currentEnchant += (addLevel - 1);
					}
					else if (currentEnchant > 17)
					{
						currentEnchant += 1;
					}
					else
					{
						currentEnchant += 1;
					}
					break;
				case UNIQUE:
				case EPIC:
				case MYTHIC:
					if ((currentEnchant > 15) && !targetItem.isAmplified())
					{
						currentEnchant = 15;
					}
					else if (targetItem.isAmplified() && (parentItem.getItemId() >= 166020000) && (parentItem.getItemId() <= 166020006) && (parentItem.getItemId() == 166000196))
					{
						currentEnchant += 1;
					}
					else if (targetItem.isAmplified() && (parentItem.getItemId() >= 166022000) && (parentItem.getItemId() <= 166022007))
					{
						currentEnchant += critLevel;
					}
					else if ((currentEnchant == 15) && !targetItem.isAmplified())
					{
						return;
					}
					else if ((currentEnchant + addLevel) <= 15)
					{
						currentEnchant += addLevel;
					}
					else if (((addLevel - 1) > 1) && (((currentEnchant + addLevel) - 1) <= 15))
					{
						currentEnchant += (addLevel - 1);
					}
					else if (currentEnchant > 17)
					{
						currentEnchant += 1;
					}
					else
					{
						currentEnchant += 1;
					}
					break;
			}
		}
		else
		{
			if (targetItem.isAmplified())
			{
				final int skillId = targetItem.getAmplificationSkill();
				final int maxEnchant = targetItem.getItemTemplate().getMaxEnchantLevel();
				currentEnchant = maxEnchant;
				if (targetItem.getEnchantLevel() != maxEnchant)
				{
					targetItem.setAmplification(false);
				}
				targetItem.setAmplificationSkill(0);
				if (player.getSkillList().isSkillPresent(skillId))
				{
					SkillLearnService.removeSkill(player, skillId);
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_SKILL_DELETE(new DescriptionId(targetItem.getNameId())));
				}
			}
			else if ((currentEnchant >= 10) && (currentEnchant <= 25) && !targetItem.isAmplified())
			{
				currentEnchant = 10;
			}
			else if ((currentEnchant > 0) && !targetItem.isAmplified())
			{
				currentEnchant -= 1;
			}
		}
		targetItem.setEnchantLevel(currentEnchant);
		
		/**
		 * New Amplified Start MaxEnchant Auto Amplified True
		 */
		if (!targetItem.isAmplified() && (targetItem.getEnchantLevel() == targetItem.getItemTemplate().getMaxEnchantLevel()) && targetItem.canAmplification())
		{
			targetItem.setAmplification(true);
		}
		if (targetItem.isEquipped())
		{
			player.getGameStats().updateStatsVisually();
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem, ItemUpdateType.STATS_CHANGE);
		if (targetItem.isEquipped())
		{
			player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		else
		{
			player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		// Strengthen Topped Item 4.7.5
		if (targetItem.isAmplified() && (targetItem.getEnchantLevel() == 20))
		{
			targetItem.setAmplificationSkill(getRndSkills(targetItem));
			targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
			PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EXCEED_SKILL_ENCHANT(new DescriptionId(targetItem.getNameId()), targetItem.getEnchantLevel(), getRndSkills(targetItem)));
		}
		else if ((targetItem.isAmplified() || !targetItem.isAmplified()) && (targetItem.getEnchantLevel() < 20))
		{
			targetItem.setAmplificationSkill(0);
			if (player.getSkillList().isSkillPresent(targetItem.getAmplificationSkill()) && targetItem.isEquipped())
			{
				SkillLearnService.removeSkill(player, targetItem.getAmplificationSkill());
				targetItem.setPersistentState(PersistentState.UPDATE_REQUIRED);
				PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
			}
		}
		if (result)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_ITEM_SUCCEED_NEW(new DescriptionId(targetItem.getNameId()), addLevel));
			if (targetItem.isAmplified() && (targetItem.getEnchantLevel() == /* maxEnchant */targetItem.getItemTemplate().getMaxEnchantLevel()) && targetItem.canAmplification())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CAN_EXCEED_ENCHANT_LEVEL(new DescriptionId(targetItem.getNameId())));
			}
		}
		else
		{
			if (targetItem.isArchDaevaItem() && targetItem.isEquipped() && !isArchdaevaRemodeledDanuar(targetItem))
			{
				if (EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
				{
					player.getEquipment().unEquipItem(targetItem.getObjectId(), targetItem.getEquipmentSlot());
				}
				if (targetItem.hasGodStone() && EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
				{
					// If the enchant fails, there is a chance that the player will receive their "Manastones & Godstones" back.
					ItemService.addItem(player, targetItem.getGodStone().getItemId(), 1);
					for (ManaStone manaStone : targetItem.getItemStones())
					{
						ItemService.addItem(player, manaStone.getItemId(), 1);
					}
					ItemService.addItem(player, 188100335, 500); // Enchantment Stone Dust.
					ItemService.addItem(player, RndArray.get(archDaevaStoneItems), 1);
				}
				// If the enchant fails, the player will receive "Enchantment Stone Dust" & "Archdaeva Crafting Materials"
				else
				{
					if (EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
					{
						ItemService.addItem(player, 188100335, 500); // Enchantment Stone Dust.
						ItemService.addItem(player, RndArray.get(archDaevaStoneItems), 1);
					}
				}
				if (EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_TYPE1_ENCHANT_FAIL(new DescriptionId(targetItem.getNameId())));
				}
				
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(targetItem.getNameId())));
				
				if (EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
				{
					if (!player.getInventory().decreaseByObjectId(targetItem.getObjectId(), 1))
					{
					}
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
					player.getGameStats().updateStatsVisually();
				}
			}
			else if ((!EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM && targetItem.isArchDaevaItem() && targetItem.isEquipped() && !isArchdaevaRemodeledDanuar(targetItem)) || player.isGM())
			{
				if ((currentEnchant >= 15) && !targetItem.isAmplified())
				{
					currentEnchant = 15;
				}
				else if ((currentEnchant >= 10) && (currentEnchant <= 25) && !targetItem.isAmplified())
				{
					currentEnchant = 10;
				}
				else if ((currentEnchant > 0) && !targetItem.isAmplified())
				{
					currentEnchant -= 1;
				}
			}
			if (EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
			{
				return;
			}
			else if (targetItem.isArchDaevaItem() && EnchantsConfig.ENABLE_ARCHDAEVA_DESTROY_ITEM)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ENCHANT_TYPE1_ENCHANT_FAIL(new DescriptionId(targetItem.getNameId())));
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(targetItem.getNameId())));
				if (!player.getInventory().decreaseByObjectId(targetItem.getObjectId(), 1))
				{
				}
			}
			else
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ENCHANT_ITEM_FAILED(new DescriptionId(targetItem.getNameId())));
			}
		}
	}
	
	private static final int[] archDaevaStoneItems =
	{
		169405421,
		169405422,
		169405423
	};
	
	private static final int[] skills4WeaponShield =
	{
		13001,
		13002,
		13003,
		13004,
		13005,
		13006,
		13007,
		13008,
		13009,
		13010,
		13011,
		13012,
		13013,
		13014,
		13015,
		13016,
		13017,
		13018,
		13019,
		13020,
		13021,
		13022,
		13023,
		13024,
		13025,
		13026,
		13027,
		13028,
		13029,
		13030,
		13031,
		13032,
		13033,
		13034,
		13035,
		13036,
		13037,
		13228,
		13229,
		13230,
		13231,
		13232,
		13233,
		13234
	};
	
	private static final int[] skills4Glove =
	{
		13038,
		13039,
		13040,
		13041,
		13042,
		13043,
		13044,
		13045,
		13046,
		13047,
		13048,
		13049,
		13050,
		13051,
		13052,
		13053,
		13054,
		13055,
		13056,
		13057,
		13058,
		13059,
		13060,
		13247,
		13248,
		13249,
		13250,
		13251,
		13252,
		13253,
		13254
	};
	
	private static final int[] skills4Pant =
	{
		13061,
		13062,
		13063,
		13064,
		13065,
		13066,
		13067,
		13068,
		13069,
		13070,
		13071,
		13072,
		13073,
		13074,
		13075,
		13076,
		13077,
		13078,
		13079,
		13080,
		13081
	};
	
	private static final int[] skills4Shoulder =
	{
		13082,
		13083,
		13084,
		13085,
		13086,
		13087,
		13088,
		13089,
		13090,
		13091,
		13092,
		13093,
		13094,
		13095,
		13096,
		13097,
		13098,
		13099,
		13100,
		13101,
		13102,
		13103,
		13104,
		13105,
		13106,
		13107,
		13266,
		13267,
		13268,
		13269,
		13270,
		13271
	};
	
	private static final int[] skills4Shoes =
	{
		13108,
		13109,
		13110,
		13111,
		13112,
		13113,
		13114,
		13115,
		13116,
		13117,
		13118,
		13119,
		13120,
		13121,
		13122,
		13123,
		13124,
		13125,
		13126,
		13127
	};
	
	private static final int[] skills4Jacket =
	{
		13128,
		13129,
		13130,
		13131,
		13132,
		13133,
		13134,
		13135,
		13136,
		13137,
		13138,
		13139,
		13140,
		13141,
		13142,
		13143,
		13144,
		13145,
		13146,
		13147,
		13235,
		13236,
		13237,
		13238,
		13239,
		13240,
		13241,
		13242,
		13243,
		13244,
		13245,
		13246
	};
	
	private static final int[] skills4Wing =
	{
		13001,
		13002,
		13003,
		13004,
		13005,
		13006,
		13007,
		13008,
		13009,
		13010,
		13011,
		13012,
		13013,
		13014,
		13015,
		13016,
		13017,
		13018,
		13019,
		13020,
		13021,
		13022,
		13023,
		13024,
		13025,
		13026,
		13027,
		13028,
		13029,
		13030,
		13031,
		13032,
		13033,
		13034,
		13035,
		13036,
		13037,
		13228,
		13229,
		13230,
		13231,
		13232,
		13233,
		13234
	};
	
	public static int getRndSkills(Item item)
	{
		if (item.getItemTemplate().getArmorType() == ArmorType.WING)
		{
			return RndArray.get(skills4Wing);
		}
		switch (item.getItemTemplate().getCategory())
		{
			case SWORD:
			case DAGGER:
			case MACE:
			case ORB:
			case SPELLBOOK:
			case GREATSWORD:
			case POLEARM:
			case STAFF:
			case BOW:
			case GUN:
			case CANNON:
			case HARP:
			case KEYBLADE:
			case SHIELD:
				return RndArray.get(skills4WeaponShield);
			case JACKET:
				return RndArray.get(skills4Jacket);
			case PANTS:
				return RndArray.get(skills4Pant);
			case SHOULDERS:
				return RndArray.get(skills4Shoulder);
			case GLOVES:
				return RndArray.get(skills4Glove);
			case SHOES:
				return RndArray.get(skills4Shoes);
			default:
				return 0;
		}
	}
	
	public static int EnchantKinah(Item item)
	{
		if (!item.isAmplified())
		{
			return 0;
		}
		if (item.getItemTemplate().getItemQuality() == ItemQuality.RARE)
		{
			switch (item.getEnchantLevel())
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
				case 16:
					return 2500000;
				case 17:
					return 5000000;
				case 18:
					return 10000000;
				case 19:
					return 12500000;
				case 20:
					return 15000000;
				case 21:
					return 20000000;
				case 22:
					return 25000000;
				case 23:
					return 30000000;
				case 24:
					return 35000000;
				case 25:
					return 40000000;
				default:
					return 40000000;
			}
		}
		if (item.getItemTemplate().getItemQuality() == ItemQuality.LEGEND)
		{
			switch (item.getEnchantLevel())
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					return 2500000;
				case 16:
					return 5000000;
				case 17:
					return 10000000;
				case 18:
					return 12500000;
				case 19:
					return 15000000;
				case 20:
					return 20000000;
				case 21:
					return 25000000;
				case 22:
					return 30000000;
				case 23:
					return 35000000;
				case 24:
					return 40000000;
				case 25:
					return 45000000;
				default:
					return 40000000;
			}
		}
		if (item.getItemTemplate().getItemQuality() == ItemQuality.EPIC)
		{
			switch (item.getEnchantLevel())
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					return 5000000;
				case 16:
					return 10000000;
				case 17:
					return 20000000;
				case 18:
					return 25000000;
				case 19:
					return 30000000;
				case 20:
					return 40000000;
				case 21:
					return 50000000;
				case 22:
					return 60000000;
				case 23:
					return 70000000;
				case 24:
					return 80000000;
				case 25:
					return 90000000;
				default:
					return 90000000;
			}
		}
		else if (item.getItemTemplate().getItemQuality() == ItemQuality.MYTHIC)
		{
			switch (item.getEnchantLevel())
			{
				case 0:
				case 1:
				case 2:
				case 3:
				case 4:
				case 5:
				case 6:
				case 7:
				case 8:
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					return 10000000;
				case 16:
					return 20000000;
				case 17:
					return 40000000;
				case 18:
					return 50000000;
				case 19:
					return 60000000;
				case 20:
					return 80000000;
				case 21:
					return 100000000;
				case 22:
					return 120000000;
				case 23:
					return 140000000;
				case 24:
					return 160000000;
				case 25:
					return 180000000;
				default:
					return 180000000;
			}
		}
		else
		{
			return 0;
		}
	}
	
	public static boolean socketManastone(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon)
	{
		int targetItemLevel = 1;
		if (targetWeapon == 1)
		{
			targetItemLevel = targetItem.getItemTemplate().getLevel();
		}
		else
		{
			targetItemLevel = targetItem.getFusionedItemTemplate().getLevel();
		}
		final int stoneLevel = parentItem.getItemTemplate().getLevel();
		final int slotLevel = (int) (10 * Math.ceil((targetItemLevel + 10) / 10d));
		boolean result = false;
		float success = EnchantsConfig.SOCKET_MANASTONE;
		int stoneCount;
		if (stoneLevel > slotLevel)
		{
			return false;
		}
		if (targetWeapon == 1)
		{
			stoneCount = targetItem.getItemStones().size();
		}
		else
		{
			stoneCount = targetItem.getFusionStones().size();
		}
		if (targetWeapon == 1)
		{
			if (stoneCount >= targetItem.getSockets(false))
			{
				AuditLogger.info(player, "Manastone socket overload");
				return false;
			}
		}
		else if (!targetItem.hasFusionedItem() || (stoneCount >= targetItem.getSockets(true)))
		{
			AuditLogger.info(player, "Manastone socket overload");
			return false;
		}
		success += parentItem.getItemTemplate().getItemQuality() == ItemQuality.COMMON ? 25f : 15f;
		final float socketDiff = (stoneCount * 1.25f) + 1.75f;
		success += (slotLevel - stoneLevel) / socketDiff;
		if (supplementItem != null)
		{
			int supplementUseCount = 0;
			final ItemTemplate manastoneTemplate = parentItem.getItemTemplate();
			int manastoneCount;
			if (targetWeapon == 1)
			{
				manastoneCount = targetItem.getItemStones().size() + 1;
			}
			else
			{
				manastoneCount = targetItem.getFusionStones().size() + 1;
			}
			final ItemTemplate supplementTemplate = supplementItem.getItemTemplate();
			float addSuccessRate = 0f;
			boolean isManastoneOnly = false;
			EnchantItemAction action = manastoneTemplate.getActions().getEnchantAction();
			if (action != null)
			{
				supplementUseCount = action.getCount();
			}
			action = supplementTemplate.getActions().getEnchantAction();
			if (action != null)
			{
				addSuccessRate = action.getChance();
				isManastoneOnly = action.isManastoneOnly();
			}
			switch (parentItem.getItemTemplate().getItemQuality())
			{
				case RARE:
					addSuccessRate *= EnchantsConfig.LESSER_SUP;
					break;
				case LEGEND:
				case UNIQUE:
					addSuccessRate *= EnchantsConfig.REGULAR_SUP;
					break;
				case EPIC:
				case MYTHIC:
					addSuccessRate *= EnchantsConfig.GREATER_SUP;
					break;
				default:
					break;
			}
			if (isManastoneOnly)
			{
				supplementUseCount = 1;
			}
			else if (stoneCount > 0)
			{
				supplementUseCount = supplementUseCount * manastoneCount;
			}
			if (player.getInventory().getItemCountByItemId(supplementTemplate.getTemplateId()) < supplementUseCount)
			{
				return false;
			}
			success += addSuccessRate;
			player.subtractSupplements(supplementUseCount, supplementTemplate.getTemplateId());
		}
		final float random = Rnd.get(1, 1000) / 10f;
		if (random <= success)
		{
			result = true;
		}
		if (player.getAccessLevel() > 0)
		{
			PacketSendUtility.sendMessage(player, (result ? "Success" : "Fail") + " Rnd:" + random + " Luck:" + success);
		}
		return result;
	}
	
	public static void socketManastoneAct(Player player, Item parentItem, Item targetItem, Item supplementItem, int targetWeapon, boolean result)
	{
		player.updateSupplements();
		if (player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1) && result)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_SUCCEED(new DescriptionId(targetItem.getNameId())));
			if (targetWeapon == 1)
			{
				final ManaStone manaStone = ItemSocketService.addManaStone(targetItem, parentItem.getItemTemplate().getTemplateId());
				if (targetItem.isEquipped())
				{
					ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
			}
			else
			{
				final ManaStone manaStone = ItemSocketService.addFusionStone(targetItem, parentItem.getItemTemplate().getTemplateId());
				if (targetItem.isEquipped())
				{
					ItemEquipmentListener.addStoneStats(targetItem, manaStone, player.getGameStats());
					player.getGameStats().updateStatsAndSpeedVisually();
				}
			}
		}
		else
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_GIVE_ITEM_OPTION_FAILED(new DescriptionId(targetItem.getNameId())));
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
	}
	
	public static void onItemEquip(Player player, Item item)
	{
		final List<IStatFunction> modifiers = new ArrayList<>();
		try
		{
			if (item.getItemTemplate().isWeapon())
			{
				switch (item.getItemTemplate().getWeaponType())
				{
					case ORB_2H:
					case BOOK_2H:
					case GUN_1H: // 4.3
					case HARP_2H: // 4.3
					case CANNON_2H: // 4.3
					case KEYBLADE_2H: // 4.5
						modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ATTACK, 0));
						break;
					case MACE_1H:
					case STAFF_2H:
						modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ATTACK, 0));
						modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
						modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
						break;
					case SWORD_1H:
					case DAGGER_1H:
						if (item.getEquipmentSlot() == ItemSlot.MAIN_HAND.getSlotIdMask())
						{
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAIN_HAND_POWER, 0));
						}
						else
						{
							modifiers.add(new StatEnchantFunction(item, StatEnum.OFF_HAND_POWER, 0));
						}
						break;
					case SWORD_2H:
					case BOW:
					case POLEARM_2H:
						modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
						break;
				}
			}
			else if (item.getItemTemplate().isArmor())
			{
				if (item.getItemTemplate().getArmorType() == ArmorType.SHIELD)
				{
					modifiers.add(new StatEnchantFunction(item, StatEnum.DAMAGE_REDUCE, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.BLOCK, 0));
				}
				/**
				 * 5.0 Wings Enchant
				 */
				else if (item.getItemTemplate().getItemSlot() == 32768)
				{
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL_RESIST, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.FLY_TIME, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL_RESIST, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.SOAR_SPEED, 0));
				}
				else if (item.getItemTemplate().getCategory() == ItemCategory.PLUME)
				{
					final int plumeId = item.getItemTemplate().getTemperingTableId();
					switch (plumeId)
					{
						case 10051:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10052:
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						// Plume 4.9
						case 10056:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10057:
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10063:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10064:
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10065:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						case 10066:
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							break;
						// Plume 5.1
						case 10103:
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY, 0));
							break;
						case 10104:
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL, 0));
							break;
						case 10105:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ACCURACY, 0));
							break;
						// Pure Plume 5.1
						case 10106:
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_CRITICAL, 0));
							break;
						case 10107:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
							break;
						case 10108:
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
							break;
						case 10109:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL, 0));
							break;
						case 10110:
							modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
							modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_ACCURACY, 0));
							break;
					}
				}
				else
				{
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_ATTACK, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.BOOST_MAGICAL_SKILL, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_DEFENSE, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAGICAL_DEFEND, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.MAXHP, 0));
					modifiers.add(new StatEnchantFunction(item, StatEnum.PHYSICAL_CRITICAL_RESIST, 0));
				}
			}
			else if (item.getItemTemplate().isAccessory() || item.getItemTemplate().isArmor() || item.getItemTemplate().isWeapon())
			{
				if (item.getItemTemplate().getTemperingTableId() > 0)
				{
					final ItemEnchantTemplate ie = DataManager.ITEM_ENCHANT_DATA.getEnchantTemplate(EnchantType.AUTHORIZE, item.getItemTemplate().getTemperingTableId());
					if (item.getAuthorize() > 0)
					{
						try
						{
							modifiers.addAll(ie.getStats(item.getAuthorize()));
						}
						catch (Exception localException2)
						{
							log.error("Cant add tempering modifiers for item: " + item.getItemId() + " , " + ie.getStats(item.getAuthorize()));
						}
					}
				}
				else
				{
					switch (item.getItemTemplate().getCategory())
					{
						case HELMET:
						case EARRINGS:
						case NECKLACE:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_ATTACK_RATIO, 0));
							break;
						case RINGS:
						case BELT:
							modifiers.add(new StatEnchantFunction(item, StatEnum.PVP_DEFEND_RATIO, 0));
							break;
					}
				}
			}
			if (!modifiers.isEmpty())
			{
				player.getGameStats().addEffect(item, modifiers);
			}
		}
		catch (Exception ex)
		{
			log.error("Error on item equip.", ex);
		}
	}
	
	public static int EnchantLevel(Item item)
	{
		if (item.getItemTemplate().isWeapon() || (item.getItemTemplate().getArmorType() == ArmorType.SHIELD))
		{
			if (((item.getEnchantLevel() >= item.getItemTemplate().getMaxEnchantLevel()) && (item.getEnchantLevel() < 20)) || (item.getItemTemplate().getMaxEnchantLevel() == 0))
			{
				return 1;
			}
			else if (item.getEnchantLevel() >= 20)
			{
				return 4;
			}
			else
			{
				return 0;
			}
		}
		else if (item.getItemTemplate().getArmorType() == ArmorType.PLUME)
		{
			if ((item.getAuthorize() >= 5) && (item.getAuthorize() < 10))
			{
				return 8;
			}
			else if (item.getAuthorize() >= 10)
			{
				return 16;
			}
			else
			{
				return 0;
			}
		}
		return 0;
	}
	
	/**
	 * Archdaeva's Remodeled Danuar Destroy Enchant: NEVER!!!
	 */
	public static boolean isArchdaevaRemodeledDanuar(Item targetItem)
	{
		switch (targetItem.getItemId())
		{
			case 110101990:
			case 110101991:
			case 110301966:
			case 110301967:
			case 110551314:
			case 110551315:
			case 110551316:
			case 110601754:
			case 110601755:
			case 111101784:
			case 111101785:
			case 111301905:
			case 111301906:
			case 111501874:
			case 111501875:
			case 111501876:
			case 111601718:
			case 111601719:
			case 112101729:
			case 112101730:
			case 112301842:
			case 112301843:
			case 112501810:
			case 112501811:
			case 112501812:
			case 112601699:
			case 112601700:
			case 113101795:
			case 113101796:
			case 113301936:
			case 113301937:
			case 113501893:
			case 113501894:
			case 113501895:
			case 113601701:
			case 113601702:
			case 114101829:
			case 114101830:
			case 114301973:
			case 114301974:
			case 114501901:
			case 114501902:
			case 114501903:
			case 114601707:
			case 114601708:
			case 115001961:
			case 115001962:
			case 101701506:
			case 101701505:
			case 100901525:
			case 101501510:
			case 101501511:
			case 101901245:
			case 101901246:
			case 102101183:
			case 102101184:
			case 100201670:
			case 100901524:
			case 100601566:
			case 101301408:
			case 102001369:
			case 102001368:
			case 100501447:
			case 101301409:
			case 100601565:
			case 100101490:
			case 100101489:
			case 100501448:
			case 100201671:
			case 101801340:
			case 101801341:
			case 100002007:
			case 100002008:
				return true;
		}
		return false;
	}
	
	/**
	 * http://aionpowerbook.com/powerbook/Glory:_Shield
	 * @param player
	 */
	public static void GloryShieldSkill(Player player)
	{
		int Enchant = 0;
		final Equipment equip = player.getEquipment();
		for (Item item : equip.getEquippedItemsWithoutStigmaOld())
		{
			if (item.getItemTemplate().isWeapon() || item.getItemTemplate().isArmor() || (item.getItemTemplate().getItemSlot() == 32768))
			{
				if (item.getEnchantLevel() >= 20)
				{
					Enchant++;
				}
			}
		}
		if (Enchant >= 6)
		{
			if (player.getSkillList().isSkillPresent(4694) || player.getSkillList().isSkillPresent(4695))
			{
				return;
			}
			if (player.getRace() == Race.ELYOS)
			{
				player.getSkillList().addSkill(player, 4694, 1);
			}
			else if (player.getRace() == Race.ASMODIANS)
			{
				player.getSkillList().addSkill(player, 4695, 1);
			}
			PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
		}
		else
		{
			if (player.getSkillList().isSkillPresent(4694))
			{
				SkillLearnService.removeSkill(player, 4694);
			}
			else if (player.getSkillList().isSkillPresent(4695))
			{
				SkillLearnService.removeSkill(player, 4695);
			}
			PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getBasicSkills()));
		}
	}
	
	public static void reductItemAct(Player player, Item parentItem, Item targetItem, int currentReduction, boolean result, int count)
	{
		if (!result)
		{
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 2, 0));
			// The reduction of %0 recommended level failed.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_FAIL(targetItem.getNameId()));
		}
		else
		{
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 1, 0));
			if ((currentReduction + count) > 5)
			{
				targetItem.setReductionLevel(5);
			}
			else
			{
				targetItem.setReductionLevel(currentReduction + count);
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_SUCCEED(targetItem.getNameId(), count));
			}
			if (targetItem.getReductionLevel() == 5)
			{
				// The max. recommended level reduction for %0 has been reached.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EQUIPLEVEL_ADJ_SUCCEED_MAX(targetItem.getNameId()));
			}
		}
		PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
		if (targetItem.isEquipped())
		{
			player.getGameStats().updateStatsVisually();
		}
		ItemPacketService.updateItemAfterInfoChange(player, targetItem);
		if (targetItem.isEquipped())
		{
			player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		else
		{
			player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
	}
}