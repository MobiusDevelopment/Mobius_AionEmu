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

import java.util.Collection;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Created by Kill3r
 */
public class Recharger extends AdminCommand
{
	
	public Recharger()
	{
		super("recharger");
	}
	
	private static boolean isOpened = false;
	
	@Override
	public void execute(Player player, String... params)
	{
		final int RechargerID = 730397;
		if (params[0].equals("off"))
		{
			if (isOpened)
			{
				final Collection<Npc> recharger = World.getInstance().getNpcs();
				for (Npc n : recharger)
				{
					if (n.getNpcId() == RechargerID)
					{
						n.getController().delete();
					}
				}
				PacketSendUtility.sendMessage(player, "Recharger Closing!");
				isOpened = false;
			}
		}
		else if (params[0].equals("on"))
		{
			final float x = player.getX();
			final float y = player.getY();
			final float z = player.getZ();
			final byte heading = player.getHeading();
			final int worldId = player.getWorldId();
			if (!isOpened)
			{
				final SpawnTemplate spawn = SpawnEngine.addNewSpawn(worldId, RechargerID, x, y, z, heading, 0);
				final VisibleObject visibleObject = SpawnEngine.spawnObject(spawn, player.getInstanceId());
				PacketSendUtility.sendMessage(player, visibleObject.getName() + " has been Summoned!");
				isOpened = true;
			}
			else
			{
				PacketSendUtility.sendMessage(player, "Already Open");
			}
		}
	}
}
