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

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerChatService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Shepper, modified: bobobear
 */
public class cmd_faction extends PlayerCommand
{
	
	public cmd_faction()
	{
		super("faction");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		final Storage sender = player.getInventory();
		
		if (!CustomConfig.FACTION_CMD_CHANNEL)
		{
			PacketSendUtility.sendMessage(player, "The command is disabled.");
			return;
		}
		
		if (player.isInPrison() && !player.isGM())
		{
			PacketSendUtility.sendMessage(player, "You can't talk in Prison.");
			return;
		}
		else if (player.isGagged())
		{
			PacketSendUtility.sendMessage(player, "You are gaged, you can't talk.");
			return;
		}
		
		if (!CustomConfig.FACTION_FREE_USE)
		{
			if (sender.getKinah() > CustomConfig.FACTION_USE_PRICE)
			{
				sender.decreaseKinah(CustomConfig.FACTION_USE_PRICE);
			}
			else
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_NOT_ENOUGH_MONEY);
				return;
			}
		}
		
		if (!PlayerChatService.isFlooding(player))
		{
			if (player.getCommonData().getRace() == Race.ASMODIANS)
			{
				final StringBuilder sbMessage = new StringBuilder("[Asmos] " + player.getCustomTag(true) + player.getName() + " : ");
				
				for (String p : params)
				{
					sbMessage.append(p + " ");
				}
				final String sMessage = sbMessage.toString().trim();
				// Logging
				if (LoggingConfig.LOG_FACTION)
				{
					PlayerChatService.chatLogging(player, ChatType.NORMAL, "[Faction] " + sMessage);
				}
				for (Player a : World.getInstance().getAllPlayers())
				{
					if (((a.getCommonData().getRace() == Race.ASMODIANS) || a.isGM()) && !player.isInPrison())
					{
						PacketSendUtility.sendMessage(a, sMessage);
					}
				}
			}
			if (player.getCommonData().getRace() == Race.ELYOS)
			{
				final StringBuilder sbMessage = new StringBuilder("[Elyos] " + player.getCustomTag(true) + player.getName() + " : ");
				
				for (String p : params)
				{
					sbMessage.append(p + " ");
				}
				final String sMessage = sbMessage.toString().trim();
				// Logging
				if (LoggingConfig.LOG_FACTION)
				{
					PlayerChatService.chatLogging(player, ChatType.NORMAL, "[Faction] " + sMessage);
				}
				for (Player e : World.getInstance().getAllPlayers())
				{
					if (((e.getCommonData().getRace() == Race.ELYOS) || e.isGM()) && !player.isInPrison())
					{
						PacketSendUtility.sendMessage(e, sMessage);
					}
				}
			}
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax: .faction <message>");
	}
	
}