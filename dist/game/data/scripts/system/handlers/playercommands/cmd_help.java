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
			PacketSendUtility.sendMessage(player, "" + "\n" + "==============================\n" + "Available .[dot] Commands for Players!" + "\n==============================\n" + " .skills : refresh or get new skills.\n" + " .ffa : to join or leave free for all\n" + " .vs : to join or leave 1v1 battles\n" + " .pk : to make pk xform\n" + " .clean <item id/link> : to delete an item\n" + " .luna <ap | kinah> <value> : kinah/ap to luna coins\n" + " .toll : shows current toll you have in you're account.\n" + " .uniquepack info : informs you with some important information about how to get gears!\n" + " .dye <color> : to dye yourself.\n" + " .unstuck : go to obelisk location\n" + " .skin : will remove your candy look,\n" + " .reskinvip : reskin two handed weapons with use of tiamat bloody tear [VIP]");
			PacketSendUtility.sendMessage(player, ".faction : asmodian/elyos world chat\n" + " .world : open world chat\n" + " .enchant 16 : will enchant your equiptment to 16.\n" + " .gmlist : shows available gm's \n" + " .marry : marry another player \n" + " .divorce : divorces from a player\n" + " .pet add : adds You a scroll Buffer Pet.\n" + " .job : Makes all craft available\n" + " .queue : registers you in an on-going event hosted by a gm.\n" + " .remodel : cross remodel with use of tiamat bloody tear\n");
			
		}
		
	}
	
	@Override
	public void onFail(Player player, String msg)
	{
		PacketSendUtility.sendMessage(player, "Syntax : .help");
	}
}
