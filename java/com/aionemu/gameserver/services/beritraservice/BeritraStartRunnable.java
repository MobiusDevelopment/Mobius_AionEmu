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
package com.aionemu.gameserver.services.beritraservice;

import java.util.Map;

import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.BeritraService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler (Encom)
 */

public class BeritraStartRunnable implements Runnable
{
	private final int id;
	
	public BeritraStartRunnable(int id)
	{
		this.id = id;
	}
	
	@Override
	public void run()
	{
		// Beritra Invasion Portal.
		BeritraService.getInstance().adventPortalSP(id);
		// Ereshkigal Invasion Portal.
		BeritraService.getInstance().adventPortalEreshSP(id);
		// The Beritra Legion's Invasion Corridor has appeared.
		BeritraService.getInstance().invasionCorridorMsg(id);
		// The Ereshkigal Legion's Invasion Corridor has been created.
		BeritraService.getInstance().ereshkigalCorridorMsg(id);
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// Beritra Invasion Lazer.
				BeritraService.getInstance().adventDirectingSP(id);
				// Ereshkigal Invasion Lazer.
				BeritraService.getInstance().adventDirectingEreshSP(id);
				// The Devil Unit has infiltrated through the Invasion Corridor.
				BeritraService.getInstance().devilUnitThroughMsg(id);
				// The Ereshkigal Legion's Magic weapon has infiltrated through the Invasion Corridor.
				BeritraService.getInstance().ereshkigalLegionThroughMsg(id);
			}
		}, 180000);
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// Beritra Invasion Black Sky.
				BeritraService.getInstance().adventControlSP(id);
				// Ereshkigal Invasion Black Sky.
				BeritraService.getInstance().adventControlEreshSP(id);
			}
		}, 300000);
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				final Map<Integer, BeritraLocation> locations = BeritraService.getInstance().getBeritraLocations();
				for (BeritraLocation loc : locations.values())
				{
					if (loc.getId() == id)
					{
						// Beritra Invasion Light Blue.
						BeritraService.getInstance().adventEffectSP(id);
						// Ereshkigal Invasion Light Blue.
						BeritraService.getInstance().adventEffectEreshSP(id);
						BeritraService.getInstance().startBeritraInvasion(loc.getId());
					}
				}
				World.getInstance().doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_WORLDRAID_INVADE_VRITRA_SPECIAL);
					}
				});
			}
		}, 600000);
	}
}