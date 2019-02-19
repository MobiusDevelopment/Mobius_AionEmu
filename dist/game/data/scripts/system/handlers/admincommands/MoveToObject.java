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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Rolandas
 */
public class MoveToObject extends AdminCommand
{
	
	public MoveToObject()
	{
		super("movetoobj");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if ((params == null) || (params.length != 1))
		{
			PacketSendUtility.sendMessage(admin, "Syntax : //movetoobj <object id>");
			return;
		}
		
		int objectId = 0;
		
		try
		{
			objectId = Integer.valueOf(params[0]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "Only numbers please!!!");
		}
		
		final VisibleObject object = World.getInstance().findVisibleObject(objectId);
		if (object == null)
		{
			PacketSendUtility.sendMessage(admin, "Cannot find object for spawn #" + objectId);
			return;
		}
		
		final VisibleObject spawn = object;
		
		TeleportService2.teleportTo(admin, spawn.getWorldId(), spawn.getSpawn().getX(), spawn.getSpawn().getY(), spawn.getSpawn().getZ());
		admin.getController().stopProtectionActiveTask();
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax : //movetoobj <object id>");
	}
	
}
