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
package com.aionemu.gameserver.model.stats.listeners;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.model.items.RandomStats;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.item.WeaponStats;
import com.aionemu.gameserver.model.templates.item.WeaponType;
import com.aionemu.gameserver.model.templates.itemset.FullBonus;
import com.aionemu.gameserver.model.templates.itemset.ItemSetTemplate;
import com.aionemu.gameserver.model.templates.itemset.PartBonus;
import com.aionemu.gameserver.services.EnchantService;
import com.aionemu.gameserver.services.SkillLearnService;

/**
 * @author xavier modified by Wakizashi
 */
public class ItemEquipmentListener
{
	
	static Logger log = LoggerFactory.getLogger(ItemEquipmentListener.class);
	
	/**
	 * @param item
	 * @param cgs
	 */
	public static void onItemEquipment(Item item, Player owner)
	{
		owner.getController().cancelUseItem();
		final ItemTemplate itemTemplate = item.getItemTemplate();
		
		onItemEquipment(item, owner.getGameStats(), owner);
		
		// Check if belongs to ItemSet
		if (itemTemplate.isItemSet())
		{
			recalculateItemSet(itemTemplate.getItemSet(), owner, item.getItemTemplate().isWeapon());
		}
		if (item.hasManaStones())
		{
			addStonesStats(item, item.getItemStones(), owner.getGameStats());
		}
		
		if (item.hasFusionStones())
		{
			addStonesStats(item, item.getFusionStones(), owner.getGameStats());
		}
		
		final IdianStone idianStone = item.getIdianStone();
		if (idianStone != null)
		{
			idianStone.onEquip(owner);
		}
		addGodstoneEffect(owner, item);
		final RandomStats randomStats = item.getRandomStats();
		if (randomStats != null)
		{
			randomStats.onEquip(owner);
		}
		if (item.getConditioningInfo() != null)
		{
			owner.getObserveController().addObserver(item.getConditioningInfo());
			item.getConditioningInfo().setPlayer(owner);
		}
		if (item.getAmplificationSkill() > 0)
		{
			owner.getSkillList().addSkill(owner, item.getAmplificationSkill(), 1);
		}
		if (item.getItemSkinSkill() > 0)
		{
			owner.getSkillList().addSkill(owner, item.getItemSkinSkill(), 1);
		}
		EnchantService.GloryShieldSkill(owner);
		EnchantService.onItemEquip(owner, item);
	}
	
	/**
	 * @param item
	 * @param owner
	 */
	public static void onItemUnequipment(Item item, Player owner)
	{
		owner.getController().cancelUseItem();
		
		final ItemTemplate itemTemplate = item.getItemTemplate();
		// Check if belongs to ItemSet
		if (itemTemplate.isItemSet())
		{
			recalculateItemSet(itemTemplate.getItemSet(), owner, item.getItemTemplate().isWeapon());
		}
		
		owner.getGameStats().endEffect(item);
		
		if (item.hasManaStones())
		{
			removeStoneStats(item.getItemStones(), owner.getGameStats());
		}
		
		if (item.hasFusionStones())
		{
			removeStoneStats(item.getFusionStones(), owner.getGameStats());
		}
		
		if (item.getConditioningInfo() != null)
		{
			owner.getObserveController().removeObserver(item.getConditioningInfo());
			item.getConditioningInfo().setPlayer(null);
		}
		final IdianStone idianStone = item.getIdianStone();
		if (idianStone != null)
		{
			idianStone.onUnEquip(owner);
		}
		removeGodstoneEffect(owner, item);
		final RandomStats randomStats = item.getRandomStats();
		if (randomStats != null)
		{
			randomStats.onUnEquip(owner);
		}
		if (item.getAmplificationSkill() > 0)
		{
			if (owner.getSkillList().isSkillPresent(item.getAmplificationSkill()))
			{
				SkillLearnService.removeSkill(owner, item.getAmplificationSkill());
			}
		}
		if (item.getItemSkinSkill() > 0)
		{
			if (owner.getSkillList().isSkillPresent(item.getItemSkinSkill()))
			{
				SkillLearnService.removeSkill(owner, item.getItemSkinSkill());
			}
		}
		EnchantService.GloryShieldSkill(owner);
	}
	
	/**
	 * @param itemTemplate
	 * @param slot
	 * @param cgs
	 */
	private static void onItemEquipment(Item item, CreatureGameStats<?> cgs, Player player)
	{
		final ItemTemplate itemTemplate = item.getItemTemplate();
		final long slot = item.getEquipmentSlot();
		final List<StatFunction> modifiers = itemTemplate.getModifiers();
		if (modifiers == null)
		{
			return;
		}
		
		List<StatFunction> allModifiers = null;
		final List<StatFunction> decreaseAllModifiers = null;
		
		if ((slot & ItemSlot.MAIN_OR_SUB.getSlotIdMask()) != 0)
		{
			allModifiers = wrapModifiers(item, modifiers);
			if (item.hasFusionedItem())
			{
				// add all bonus modifiers according to rules
				final ItemTemplate fusionedItemTemplate = item.getFusionedItemTemplate();
				final WeaponType weaponType = fusionedItemTemplate.getWeaponType();
				final List<StatFunction> fusionedItemModifiers = fusionedItemTemplate.getModifiers();
				if (fusionedItemModifiers != null)
				{
					allModifiers.addAll(wrapModifiers(item, fusionedItemModifiers));
				}
				// add 10% of Magic Boost and Attack
				final WeaponStats weaponStats = fusionedItemTemplate.getWeaponStats();
				if (weaponStats != null)
				{
					final int boostMagicalSkill = Math.round(0.1f * weaponStats.getBoostMagicalSkill());
					final int attack = Math.round(0.1f * weaponStats.getMeanDamage());
					if ((weaponType == WeaponType.ORB_2H) || (weaponType == WeaponType.BOOK_2H) || (weaponType == WeaponType.GUN_1H) || // 4.3
						(weaponType == WeaponType.CANNON_2H) || // 4.3
						(weaponType == WeaponType.HARP_2H) || // 4.3
						(weaponType == WeaponType.KEYBLADE_2H))
					{ // 4.5
						allModifiers.add(new StatAddFunction(StatEnum.MAGICAL_ATTACK, attack, false));
						allModifiers.add(new StatAddFunction(StatEnum.BOOST_MAGICAL_SKILL, boostMagicalSkill, false));
					}
					else
					{
						allModifiers.add(new StatAddFunction(StatEnum.MAIN_HAND_POWER, attack, false));
					}
				}
			}
			if (CustomConfig.ITEM_NOT_FOR_ARCHDAEVA_ENABLE)
			{
				if ((player.getLevel() >= 65) && !itemTemplate.isArchdaeva())
				{
					for (StatFunction a : modifiers)
					{
						final int value = a.getValue();
						final int formula = (int) (value * (20.0f / 100.0f));
						allModifiers.add(new StatAddFunction(a.getName(), -formula, false));
					}
				}
			}
			// ArchDaeva item level limitations
			if ((player.getLevel() >= 65) && itemTemplate.isArchdaeva())
			{
				final int pLevel = player.getLevel();
				final int iLevel = itemTemplate.getLevel();
				float percentageDecrease = 0;
				if ((iLevel - pLevel) == 1)
				{
					percentageDecrease = 2.0f;
				}
				else if ((iLevel - pLevel) == 2)
				{
					percentageDecrease = 4.0f;
				}
				else if ((iLevel - pLevel) == 3)
				{
					percentageDecrease = 6.0f;
				}
				else if ((iLevel - pLevel) == 4)
				{
					percentageDecrease = 8.0f;
				}
				else if ((iLevel - pLevel) == 5)
				{
					percentageDecrease = 10.0f;
				}
				else if ((iLevel - pLevel) == 6)
				{
					percentageDecrease = 12.0f;
				}
				else if ((iLevel - pLevel) == 7)
				{
					percentageDecrease = 14.0f;
				}
				else if ((iLevel - pLevel) == 8)
				{
					percentageDecrease = 16.0f;
				}
				else if ((iLevel - pLevel) == 9)
				{
					percentageDecrease = 18.0f;
				}
				else if ((iLevel - pLevel) == 10)
				{
					percentageDecrease = 20.0f;
				}
				for (StatFunction a : modifiers)
				{
					final int value = a.getValue();
					final int formula = (int) (value * (percentageDecrease / 100.0f));
					allModifiers.add(new StatAddFunction(a.getName(), -formula, false));
				}
			}
		}
		else
		{
			allModifiers = modifiers;
		}
		item.setCurrentModifiers(allModifiers);
		cgs.addEffect(item, allModifiers);
	}
	
	/**
	 * Filter stats based on the following rules:<br>
	 * 1) don't include fusioned stats which will be taken only from 1 weapon <br>
	 * 2) wrap stats which are different for MAIN and OFF hands<br>
	 * 3) add the rest<br>
	 * @param item
	 * @param modifiers
	 * @return
	 */
	private static List<StatFunction> wrapModifiers(Item item, List<StatFunction> modifiers)
	{
		final List<StatFunction> allModifiers = new ArrayList<>();
		for (StatFunction modifier : modifiers)
		{
			switch (modifier.getName())
			{
				// why they are removed look at DuplicateStatFunction
				case ATTACK_SPEED:
				case PVP_ATTACK_RATIO:
				case PVP_DEFEND_RATIO:
				case BOOST_CASTING_TIME:
				{
					continue;
				}
				default:
				{
					allModifiers.add(modifier);
				}
			}
		}
		return allModifiers;
	}
	
	/**
	 * @param itemSetTemplate
	 * @param player
	 * @param isWeapon
	 */
	private static void recalculateItemSet(ItemSetTemplate itemSetTemplate, Player player, boolean isWeapon)
	{
		if (itemSetTemplate == null)
		{
			return;
		}
		
		// TODO quite
		player.getGameStats().endEffect(itemSetTemplate);
		// 1.- Check equipment for items already equip with this itemSetTemplate id
		final int itemSetPartsEquipped = player.getEquipment().itemSetPartsEquipped(itemSetTemplate.getId());
		
		// If main hand and off hand is same , no bonus
		int mainHandItemId = 0;
		int offHandItemId = 0;
		if (player.getEquipment().getMainHandWeapon() != null)
		{
			mainHandItemId = player.getEquipment().getMainHandWeapon().getItemId();
		}
		if (player.getEquipment().getOffHandWeapon() != null)
		{
			offHandItemId = player.getEquipment().getOffHandWeapon().getItemId();
		}
		final boolean mainAndOffNotSame = mainHandItemId != offHandItemId;
		
		// 2.- Check Item Set Parts and add effects one by one if not done already
		for (PartBonus itempartbonus : itemSetTemplate.getPartbonus())
		{
			if (mainAndOffNotSame && isWeapon)
			{
				// If the partbonus was not applied before, do it now
				if (itempartbonus.getCount() <= itemSetPartsEquipped)
				{
					if (itempartbonus.getModifiers() != null)
					{
						player.getGameStats().addEffect(itemSetTemplate, itempartbonus.getModifiers());
					}
				}
			}
			else if (!isWeapon)
			{
				// If the partbonus was not applied before, do it now
				if (itempartbonus.getCount() <= itemSetPartsEquipped)
				{
					player.getGameStats().addEffect(itemSetTemplate, itempartbonus.getModifiers());
				}
			}
		}
		
		// 3.- Finally check if all items are applied and set the full bonus if not already applied
		final FullBonus fullbonus = itemSetTemplate.getFullbonus();
		if ((fullbonus != null) && (itemSetPartsEquipped == fullbonus.getCount()))
		{
			// Add the full bonus with index = total parts + 1 to avoid confusion with part bonus equal to number of
			// objects
			player.getGameStats().addEffect(itemSetTemplate, fullbonus.getModifiers());
		}
	}
	
	/**
	 * All modifiers of stones will be applied to character
	 * @param item
	 * @param cgs
	 */
	private static void addStonesStats(Item item, Set<? extends ManaStone> itemStones, CreatureGameStats<?> cgs)
	{
		if ((itemStones == null) || (itemStones.size() == 0))
		{
			return;
		}
		
		for (ManaStone stone : itemStones)
		{
			addStoneStats(item, stone, cgs);
		}
	}
	
	/**
	 * Used when socketing of equipped item
	 * @param item
	 * @param stone
	 * @param cgs
	 */
	public static void addStoneStats(Item item, ManaStone stone, CreatureGameStats<?> cgs)
	{
		final List<StatFunction> modifiers = stone.getModifiers();
		if (modifiers == null)
		{
			return;
		}
		cgs.addEffect(stone, modifiers);
	}
	
	/**
	 * All modifiers of stones will be removed
	 * @param itemStones
	 * @param cgs
	 */
	public static void removeStoneStats(Set<? extends ManaStone> itemStones, CreatureGameStats<?> cgs)
	{
		if ((itemStones == null) || (itemStones.size() == 0))
		{
			return;
		}
		
		for (ManaStone stone : itemStones)
		{
			final List<StatFunction> modifiers = stone.getModifiers();
			if (modifiers != null)
			{
				cgs.endEffect(stone);
			}
		}
	}
	
	/**
	 * @param item
	 */
	private static void addGodstoneEffect(Player player, Item item)
	{
		if (item.getGodStone() != null)
		{
			item.getGodStone().onEquip(player);
		}
	}
	
	/**
	 * @param item
	 */
	private static void removeGodstoneEffect(Player player, Item item)
	{
		if (item.getGodStone() != null)
		{
			item.getGodStone().onUnEquip(player);
		}
	}
	
	public static void addIdianBonusStats(Item item, List<StatFunction> modifiers, CreatureGameStats<?> cgs)
	{
		cgs.addEffect(item, modifiers);
	}
	
	public static void removeIdianBonusStats(Item item, CreatureGameStats<?> cgs)
	{
		cgs.endEffect(item);
	}
}
