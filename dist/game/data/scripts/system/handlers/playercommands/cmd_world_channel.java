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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Maestross
 * @Reworked Kill3r
 */
public class cmd_world_channel extends PlayerCommand
{
	
	public cmd_world_channel()
	{
		super("world");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		int i = 1;
		// int ap = CustomConfig.WORLD_CHANNEL_AP_COSTS;
		final int ap = 0;
		final boolean check = true;
		String adminTag = "";
		String factionTag = "";
		
		if (params.length < 1)
		{
			PacketSendUtility.sendMessage(player, "syntax : .world <message>");
			return;
		}
		
		if (!CustomConfig.FACTION_CMD_CHANNEL && (player.getAccessLevel() <= 1))
		{
			PacketSendUtility.sendMessage(player, "The command is closed for now!!");
			return;
		}
		
		if (player.isGagged())
		{
			PacketSendUtility.sendMessage(player, "You cannot talk while Gagged!");
			return;
		}
		
		if (AdminConfig.ADMIN_TAG_ENABLE)
		{
			if (player.getAccessLevel() == 1)
			{
				adminTag = "[\uE01F]";
			}
			else if (player.getAccessLevel() == 2)
			{
				adminTag = "[\uE01F]";
			}
			else if (player.getAccessLevel() == 3)
			{
				adminTag = "[\uE01F]";
			}
			else if (player.getAccessLevel() == 4)
			{
				adminTag = "[\uE01F]";
			}
			else if (player.getAccessLevel() == 5)
			{
				adminTag = "[\uE022]";
			}
			else if (player.getAccessLevel() == 6)
			{
				adminTag = "[\uE020]";
			}
			else if (player.getAccessLevel() == 7)
			{
				adminTag = "[\uE022]";
			}
			else if (player.getAccessLevel() == 8)
			{
				adminTag = "[\uE020]";
			}
			else if (player.getAccessLevel() == 9)
			{
				adminTag = "[\uE022]";
			}
			else if (player.getAccessLevel() == 10)
			{
				adminTag = "[\uE022]";
			}
		}
		
		adminTag += player.getName() + " : ";
		
		StringBuilder sbMessage;
		if (player.isGM())
		{
			sbMessage = new StringBuilder("[World-Chat]" + " " + adminTag);
		}
		else if (player.getRace() == Race.ASMODIANS)
		{
			factionTag = "[\uE070]";
			sbMessage = new StringBuilder("[World-Chat]" + " " + factionTag + player.getName() + " : ");
		}
		else if (player.getRace() == Race.ELYOS)
		{
			factionTag = "[\uE042]";
			sbMessage = new StringBuilder("[World-Chat]" + " " + factionTag + player.getName() + " : ");
		}
		else
		{
			sbMessage = new StringBuilder("[World-Chat]" + " " + player.getName() + " : ");
		}
		
		for (String s : params)
		{
			if ((i++ != 0) && (check))
			{
				sbMessage.append(s).append(" ");
			}
		}
		
		final String message = sbMessage.toString().trim();
		final int messageLenght = message.length();
		
		final String sMessage = message.substring(0, CustomConfig.MAX_CHAT_TEXT_LENGHT > messageLenght ? messageLenght : CustomConfig.MAX_CHAT_TEXT_LENGHT);
		if (player.isGM())
		{
			
			World.getInstance().doOnAllPlayers(player1 -> PacketSendUtility.sendMessage(player1, sMessage));
		}
		else if (!player.isGM() && !player.isInPrison())
		{
			if (player.getAbyssRank().getAp() < ap)
			{
				PacketSendUtility.sendMessage(player, "Not Anough AP " + player.getAbyssRank().getAp());
			}
			else
			{
				AbyssPointsService.addAp(player, -ap);
				World.getInstance().doOnAllPlayers(player1 -> PacketSendUtility.sendMessage(player1, sMessage));
			}
		}
		else
		{
			PacketSendUtility.sendMessage(player, "Not Anough AP " + player.getAbyssRank().getAp());
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax : .world <message>");
	}
}
