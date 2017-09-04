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

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Ghostfur
 */
public class cmd_help extends PlayerCommand
{
	public cmd_help()
	{
		super("help");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (params.length != 0)
		{
			onFail(player, null);
			return;
		}
		
		if ((player.getRace() == Race.ASMODIANS) || (player.getRace() == Race.ELYOS))
		{
			PacketSendUtility.sendMessage(player, "===========================");
			PacketSendUtility.sendMessage(player, " Available .[dot] commands");
			PacketSendUtility.sendMessage(player, "===========================");
			PacketSendUtility.sendMessage(player, ".skills : Refresh or get new skills.");
			// PacketSendUtility.sendMessage(player, ".ffa : Join or leave free for all.");
			// PacketSendUtility.sendMessage(player, ".vs : Join or leave 1v1 battles.");
			// PacketSendUtility.sendMessage(player, ".pk : Make pk xform.");
			PacketSendUtility.sendMessage(player, ".clean <item id/link> : Delete an item.");
			PacketSendUtility.sendMessage(player, ".luna <ap | kinah> <value> : kinah/ap to luna coins.");
			PacketSendUtility.sendMessage(player, ".toll : Shows current toll you have in you're account.");
			// PacketSendUtility.sendMessage(player, ".uniquepack info : Informs you on how to obtain gear!");
			PacketSendUtility.sendMessage(player, ".dye <color> : Dye yourself.");
			PacketSendUtility.sendMessage(player, ".unstuck : Go to obelisk location.");
			PacketSendUtility.sendMessage(player, ".skin : Will remove your candy look.");
			PacketSendUtility.sendMessage(player, ".reskinvip : Reskin two handed weapons with use of tiamat bloody tear [VIP].");
			// PacketSendUtility.sendMessage(player, ".faction : Asmodian/Elyos world chat.");
			PacketSendUtility.sendMessage(player, ".world : open world chat.");
			// PacketSendUtility.sendMessage(player, ".enchant 16 : will enchant your equipment to 16.");
			PacketSendUtility.sendMessage(player, ".gmlist : shows available gms.");
			PacketSendUtility.sendMessage(player, ".marry : marry another player.");
			PacketSendUtility.sendMessage(player, ".divorce : divorces from a player.");
			// PacketSendUtility.sendMessage(player, ".pet add : adds you a scroll Buffer Pet.");
			// PacketSendUtility.sendMessage(player, ".job : Makes all craft available.");
			PacketSendUtility.sendMessage(player, ".queue : Register in an on-going event hosted by a gm.");
			PacketSendUtility.sendMessage(player, ".remodel : Cross remodel with use of tiamat bloody tear.");
		}
	}
	
	@Override
	public void onFail(Player player, String msg)
	{
		PacketSendUtility.sendMessage(player, "Syntax : .help");
	}
}
