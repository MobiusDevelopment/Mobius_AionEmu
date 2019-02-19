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
package system.handlers.admincommands;

import java.util.Iterator;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Admin notice command
 * @author Jenose Updated By Darkwolf
 */
public class Notice extends AdminCommand
{
	
	public Notice()
	{
		super("notice");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		
		String message = "";
		
		try
		{
			for (String param : params)
			{
				message += " " + param;
			}
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(player, "Parameters should be text or number !");
			return;
		}
		final Iterator<Player> iter = World.getInstance().getPlayersIterator();
		
		while (iter.hasNext())
		{
			PacketSendUtility.sendBrightYellowMessageOnCenter(iter.next(), "Information: " + message);
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax: //notice <message>");
	}
}
