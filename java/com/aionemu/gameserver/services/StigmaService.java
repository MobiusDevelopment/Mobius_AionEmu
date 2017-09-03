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

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Equipment;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.model.templates.item.RequireSkill;
import com.aionemu.gameserver.model.templates.item.Stigma;
import com.aionemu.gameserver.model.templates.item.Stigma.StigmaSkill;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUBE_UPDATE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SKILL_REMOVE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.skillengine.model.SkillLearnTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;

/**
 * @author KorLightning (Encom)
 */

public class StigmaService
{
	private static final Logger log = LoggerFactory.getLogger(StigmaService.class);
	
	private static int getPriceByQuality(Item item)
	{
		int price = 0;
		switch (item.getItemTemplate().getItemQuality())
		{
			case RARE:
				price = 35312;
				break;
			case LEGEND:
				price = 70625;
				break;
			case UNIQUE:
				price = 141250;
				break;
			default:
				break;
		}
		return price;
	}
	
	public static boolean notifyEquipAction(Player player, Item resultItem, long slot)
	{
		if (resultItem.getItemTemplate().isStigma())
		{
			if (ItemSlot.isRegularStigma(slot))
			{
				if (getPossibleStigmaCount(player) <= player.getEquipment().getEquippedItemsRegularStigma().size())
				{
					AuditLogger.info(player, "Possible client hack stigma count big :O");
					return false;
				}
			}
			if (!resultItem.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass()))
			{
				AuditLogger.info(player, "Possible client hack not valid for class.");
				return false;
			}
			final Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
			if (stigmaInfo == null)
			{
				log.warn("Stigma info missing for item: " + resultItem.getItemTemplate().getTemplateId());
				return false;
			}
			if (player.getInventory().getKinah() < getPriceByQuality(resultItem))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_NOT_ENOUGH_MONEY);
				return false;
			}
			else
			{
				player.getInventory().decreaseKinah(getPriceByQuality(resultItem));
			}
			for (int i = 1; i <= player.getLevel(); i++)
			{
				final SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
				for (SkillLearnTemplate skillTree : skillTemplates)
				{
					if (resultItem.getSkillGroup().equals(skillTree.getSkillGroup()))
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300401, new DescriptionId(resultItem.getNameId()), resultItem.getEnchantLevel() + 1));
						player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel() + resultItem.getEnchantLevel() + 1);
						PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
					}
				}
			}
			final List<Integer> Stigma = player.getEquipment().getEquippedItemsAllStigmaIds();
			Stigma.add(resultItem.getItemId());
			StigmaLinkedService.checkEquipConditions(player, Stigma);
		}
		return true;
	}
	
	public static boolean notifyUnequipAction(Player player, Item resultItem)
	{
		if (resultItem.getItemTemplate().isStigma())
		{
			final Stigma stigmaInfo = resultItem.getItemTemplate().getStigma();
			final int itemId = resultItem.getItemId();
			final Equipment equipment = player.getEquipment();
			if ((itemId == 140000007) || (itemId == 140000005))
			{
				if (equipment.hasDualWeaponEquipped(ItemSlot.LEFT_HAND))
				{
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_STIGMA_CANNT_UNEQUIP_STONE_FIRST_UNEQUIP_CURRENT_EQUIPPED_ITEM);
					return false;
				}
			}
			for (Item item : player.getEquipment().getEquippedItemsAllStigma())
			{
				final Stigma si = item.getItemTemplate().getStigma();
				if ((resultItem == item) || (si == null))
				{
					continue;
				}
				for (StigmaSkill skill : stigmaInfo.getSkills())
				{
					for (RequireSkill rs : si.getRequireSkill())
					{
						if (rs.getSkillIds().contains(skill.getSkillId()))
						{
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300410, new DescriptionId(resultItem.getItemTemplate().getNameId()), new DescriptionId(item.getItemTemplate().getNameId())));
							return false;
						}
					}
				}
			}
			PacketSendUtility.sendPacket(player, SM_CUBE_UPDATE.stigmaSlots(player.getCommonData().getAdvencedStigmaSlotSize()));
			PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, resultItem));
			for (StigmaSkill skill : stigmaInfo.getSkills())
			{
				final String skillStack = DataManager.SKILL_DATA.getSkillTemplate(skill.getSkillId()).getStack();
				final PlayerSkillEntry[] skillTemplates = player.getSkillList().getStigmaSkills();
				for (PlayerSkillEntry skillTree : skillTemplates)
				{
					if (skillTree.getSkillTemplate().getStack().equals(skillStack))
					{
						// Remove "Basic Stigma" Skills.
						PacketSendUtility.sendPacket(player, new SM_SKILL_REMOVE(skillTree.getSkillId(), skillTree.getSkillLevel(), skillTree.isStigma(), false));
						
						SkillLearnService.removeSkill(player, skillTree.getSkillId());
						player.getSkillList().removeSkill(skillTree.getSkillId());
						player.getEffectController().removeEffect(skillTree.getSkillId());
						
						// Remove "Enchanted Stigma" Skills.
						// SkillLearnService.removeSkill(player, skillTree.getSkillId() + resultItem.getEnchantLevel() + 1);
						
					}
					
				}
				
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300403, new DescriptionId(resultItem.getNameId())));
				player.getEquipedStigmaList().remove(player, itemId);
				StigmaLinkedService.DeleteLinkedSkills(player);
			}
		}
		return true;
	}
	
	public static void onPlayerLogin(Player player)
	{
		final List<Item> equippedItems = player.getEquipment().getEquippedItemsAllStigma();
		for (Item item : equippedItems)
		{
			for (int i = 1; i <= player.getLevel(); i++)
			{
				final SkillLearnTemplate[] skillTemplates = DataManager.SKILL_TREE_DATA.getTemplatesFor(player.getPlayerClass(), i, player.getRace());
				for (SkillLearnTemplate skillTree : skillTemplates)
				{
					if (item.getItemTemplate().isStigma() && item.getSkillGroup().equals(skillTree.getSkillGroup()))
					{
						player.getSkillList().addStigmaSkill(player, skillTree.getSkillId(), skillTree.getSkillLevel() + item.getEnchantLevel() + 1);
						PacketSendUtility.sendPacket(player, new SM_SKILL_LIST(player, player.getSkillList().getStigmaSkills()));
					}
				}
			}
		}
		for (Item item : equippedItems)
		{
			if (item.getItemTemplate().isStigma())
			{
				if (!isPossibleEquippedStigma(player, item))
				{
					AuditLogger.info(player, "Possible client hack stigma count big :O");
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				final Stigma stigmaInfo = item.getItemTemplate().getStigma();
				if (stigmaInfo == null)
				{
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				int needSkill = stigmaInfo.getRequireSkill().size();
				for (RequireSkill rs : stigmaInfo.getRequireSkill())
				{
					for (int id : rs.getSkillIds())
					{
						if (player.getSkillList().isSkillPresent(id))
						{
							needSkill--;
							break;
						}
					}
				}
				if (needSkill != 0)
				{
					AuditLogger.info(player, "Possible client hack advenced stigma skill.");
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
				if (!item.getItemTemplate().isClassSpecific(player.getCommonData().getPlayerClass()))
				{
					AuditLogger.info(player, "Possible client hack not valid for class.");
					player.getEquipment().unEquipItem(item.getObjectId(), 0);
					continue;
				}
			}
		}
		/** Stigma Linked Skills **/
		final List<Integer> Stigma = player.getEquipment().getEquippedItemsAllStigmaIds();
		StigmaLinkedService.checkEquipConditions(player, Stigma);
	}
	
	private static int getPossibleStigmaCount(Player player)
	{
		if ((player == null) || (player.getLevel() < 20))
		{
			return 0;
		}
		if (player.havePermission(MembershipConfig.STIGMA_SLOT_QUEST))
		{
			return 6;
		}
		boolean isCompleteQuest = false;
		if (player.getRace() == Race.ELYOS)
		{
			isCompleteQuest = player.isCompleteQuest(1929) || ((player.getQuestStateList().getQuestState(1929).getStatus() == QuestStatus.START) && (player.getQuestStateList().getQuestState(1929).getQuestVars().getQuestVars() == 98));
		}
		else
		{
			isCompleteQuest = player.isCompleteQuest(2900) || ((player.getQuestStateList().getQuestState(2900).getStatus() == QuestStatus.START) && (player.getQuestStateList().getQuestState(2900).getQuestVars().getQuestVars() == 99));
		}
		final int playerLevel = player.getLevel();
		if (isCompleteQuest)
		{
			if (playerLevel < 30)
			{
				return 2;
			}
			else if (playerLevel < 40)
			{
				return 3;
			}
			else if (playerLevel < 50)
			{
				return 4;
			}
			else if (playerLevel < 55)
			{
				return 5;
			}
			else
			{
				return 6;
			}
		}
		return 0;
	}
	
	private static boolean isPossibleEquippedStigma(Player player, Item item)
	{
		if ((player == null) || (item == null) || !item.getItemTemplate().isStigma())
		{
			return false;
		}
		final long itemSlotToEquip = item.getEquipmentSlot();
		if (ItemSlot.isRegularStigma(itemSlotToEquip))
		{
			final int stigmaCount = getPossibleStigmaCount(player);
			if (stigmaCount > 0)
			{
				if (stigmaCount == 1)
				{
					if (itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask())
					{
						return true;
					}
				}
				else if (stigmaCount == 2)
				{
					if ((itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()))
					{
						return true;
					}
				}
				else if (stigmaCount == 3)
				{
					if ((itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()))
					{
						return true;
					}
				}
				else if (stigmaCount == 4)
				{
					if ((itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()))
					{
						return true;
					}
				}
				else if (stigmaCount == 5)
				{
					if ((itemSlotToEquip == ItemSlot.STIGMA1.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA2.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA3.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA4.getSlotIdMask()) || (itemSlotToEquip == ItemSlot.STIGMA5.getSlotIdMask()))
					{
						return true;
					}
				}
				else if (stigmaCount == 6)
				{
					return true;
				}
			}
		}
		return false;
	}
}