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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Admin moveplayertoplayer command.
 * @author Tanelorn
 */
public class MovePlayerToPlayer extends AdminCommand
{
	public MovePlayerToPlayer()
	{
		super("moveplayertoplayer");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if ((params == null) || (params.length < 2))
		{
			PacketSendUtility.sendMessage(admin, "syntax //moveplayertoplayer <characterNameToMove> <characterNameDestination>");
			return;
		}
		
		final Player playerToMove = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (playerToMove == null)
		{
			PacketSendUtility.sendMessage(admin, "The specified player is not online.");
			return;
		}
		
		final Player playerDestination = World.getInstance().findPlayer(Util.convertName(params[1]));
		if (playerDestination == null)
		{
			PacketSendUtility.sendMessage(admin, "The destination player is not online.");
			return;
		}
		
		if (playerToMove.getObjectId() == playerDestination.getObjectId())
		{
			PacketSendUtility.sendMessage(admin, "Cannot move the specified player to their own position.");
			return;
		}
		
		TeleportService2.teleportTo(playerToMove, playerDestination.getWorldId(), playerDestination.getInstanceId(), playerDestination.getX(), playerDestination.getY(), playerDestination.getZ(), playerDestination.getHeading());
		
		PacketSendUtility.sendMessage(admin, "Teleported player " + playerToMove.getName() + " to the location of player " + playerDestination.getName() + ".");
		PacketSendUtility.sendMessage(playerToMove, "You have been teleported by an administrator.");
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax //moveplayertoplayer <characterNameToMove> <characterNameDestination>");
	}
}
