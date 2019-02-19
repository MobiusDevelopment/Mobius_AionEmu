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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUNA_SHOP;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Falke34
 */
public class cmd_luna extends PlayerCommand
{
	
	public cmd_luna()
	{
		super("luna");
	}
	
	// AP to Luna Coin = 10.000 AP for 1 Luna Coin
	// Kinah to Luna Coin = 10.000.000 Kinah for 1 Luna Coin
	
	@Override
	public void execute(Player player, String... params)
	{
		if ((params == null) || (params.length < 2))
		{
			PacketSendUtility.sendMessage(player, ".luna <ap | kinah> <value>" + "\nAp 10,000:1 : Kinah 10,000,000:1");
			return;
		}
		int luna;
		try
		{
			luna = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
			return;
		}
		if (luna > 500)
		{ // max amount of Luna Coins
			// PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.LUNATOBIG));
			PacketSendUtility.sendMessage(player, "Luna To Big.");
			return;
		}
		if (params[0].equals("ap") && (luna > 0))
		{
			final int PlayerAbyssPoints = player.getAbyssRank().getAp();
			final int pointsLost = (luna * 10000);
			if (PlayerAbyssPoints < pointsLost)
			{
				// PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.TOLOWAP));
				PacketSendUtility.sendMessage(player, "To low AP.");
				return;
			}
			AbyssPointsService.addAp(player, -pointsLost);
			addluna(player, luna);
		}
		else if (params[0].equals("kinah") && (luna > 0))
		{
			final int kinahLost = (luna * 10000000);
			if (player.getInventory().getKinah() < kinahLost)
			{
				// PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.TOLOWLUNA));
				PacketSendUtility.sendMessage(player, "To Low Luna.");
				return;
			}
			player.getInventory().decreaseKinah(kinahLost);
			addluna(player, luna);
		}
		else
		{
			// PacketSendUtility.sendMessage(player, LanguageHandler.translate(CustomMessageId.WRONGLUNANUM));
			PacketSendUtility.sendMessage(player, "Wrong Luna Number.");
			return;
		}
	}
	
	private void addluna(Player player, int luna)
	{
		player.getCommonData().setLunaConsumeCount(player.getCommonData().getLunaConsumePoint() + luna);
		PacketSendUtility.sendPacket(player, new SM_LUNA_SHOP(0));
		
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		// TODO Auto-generated method stub
	}
}
