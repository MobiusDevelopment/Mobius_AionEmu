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

import java.util.concurrent.Future;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Watson
 */
public class Gag extends AdminCommand
{
	
	public Gag()
	{
		super("gag");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if ((params == null) || (params.length < 1))
		{
			PacketSendUtility.sendMessage(admin, "Syntax: //gag <player> [time in minutes]");
			return;
		}
		
		final String name = Util.convertName(params[0]);
		final Player player = World.getInstance().findPlayer(name);
		if (player == null)
		{
			PacketSendUtility.sendMessage(admin, "Player " + name + " was not found!");
			PacketSendUtility.sendMessage(admin, "Syntax: //gag <player> [time in minutes]");
			return;
		}
		
		int time = 0;
		if (params.length > 1)
		{
			try
			{
				time = Integer.parseInt(params[1]);
			}
			catch (NumberFormatException e)
			{
				PacketSendUtility.sendMessage(admin, "Syntax: //gag <player> [time in minutes]");
				return;
			}
		}
		
		player.setGagged(true);
		if (time != 0)
		{
			final Future<?> task = player.getController().getTask(TaskId.GAG);
			if (task != null)
			{
				player.getController().cancelTask(TaskId.GAG);
			}
			player.getController().addTask(TaskId.GAG, ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				
				@Override
				public void run()
				{
					player.setGagged(false);
					PacketSendUtility.sendMessage(player, "You have been ungagged");
				}
			}, time * 60000L));
		}
		PacketSendUtility.sendMessage(player, "You have been gagged" + (time != 0 ? " for " + time + " minutes" : ""));
		
		PacketSendUtility.sendMessage(admin, "Player " + name + " gagged" + (time != 0 ? " for " + time + " minutes" : ""));
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax: //gag <player> [time in minutes]");
	}
}
