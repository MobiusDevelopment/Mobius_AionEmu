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
package system.handlers.playercommands;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Goong Adm
 */
public class cmd_enchant extends PlayerCommand
{
	public cmd_enchant()
	{
		super("enchant");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		int enchant = 0;
		
		try
		{
			enchant = params[0] == null ? enchant : Integer.parseInt(params[0]);
		}
		catch (Exception ex)
		{
			onFail(player, "Fail");
			return;
		}
		if (enchant <= 16)
		{
			enchant(player, enchant);
		}
		else
		{
			PacketSendUtility.sendMessage(player, "You cannot enchant higher than 16 using the command!");
		}
	}
	
	private void enchant(Player player, int enchant)
	{
		
		for (Item targetItem : player.getEquipment().getEquippedItemsWithoutStigma())
		{
			if (isUpgradeble(targetItem))
			{
				if (targetItem.getEnchantLevel() < enchant)
				{
					targetItem.setEnchantLevel(enchant);
					if (targetItem.isEquipped())
					{
						player.getGameStats().updateStatsVisually();
					}
					ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				}
			}
		}
	}
	
	/**
	 * Verify if the item is enchantble and/or socketble
	 * @param item
	 * @return
	 */
	public static boolean isUpgradeble(Item item)
	{
		if (item.getItemTemplate().isNoEnchant())
		{
			return false;
		}
		if (item.getEnchantLevel() >= 16)
		{
			return false;
		}
		if (item.getItemTemplate().isWeapon())
		{
			return true;
		}
		if ((item.getEnchantLevel() > item.getItemTemplate().getMaxEnchantLevel()) && item.isAmplified())
		{
			return false;
		}
		if (item.getItemTemplate().isArmor())
		{
			final int at = item.getItemTemplate().getItemSlot();
			if ((at == 1) || (/* Main Hand */ at == 2) || (/* Sub Hand */ at == 8) || (/* Jacket */ at == 16) || (/* Gloves */ at == 32) || (/* Boots */ at == 2048) || (/* Shoulder */ at == 4096) || (/* Pants */ at == 131072) || (/* Wings */ at == 32768) || (/* Main Off Hand */ at == 262144))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, " " + "Syntax .enchant : " + " <value>.");
		// PacketSendUtility.sendMessage(player, "Syntax .enchant : \n" + " Syntax .enchant <value>.\n" + LanguageHandler.translate(CustomMessageId.ENCHANT_INFO) + "\n"
		// + LanguageHandler.translate(CustomMessageId.ENCHANT_SAMPLE));
	}
}