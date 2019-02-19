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
package com.aionemu.gameserver.spawnengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.walker.WalkerTemplate;

/**
 * Forms the walker groups on initial spawn<br>
 * Brings NPCs back to their positions if they die<br>
 * Cleanup and rework will be made after tests and error handling<br>
 * To use only with patch!
 * @author vlog
 * @based on Imaginary's imagination
 * @modified Rolandas
 */
public class WalkerFormator
{
	
	private static final Logger log = LoggerFactory.getLogger(WalkerFormator.class);
	
	/**
	 * @param npc
	 * @param worldId
	 * @param instanceId
	 * @return <tt>true</tt> if npc was brought into world by the method call.
	 */
	public static boolean processClusteredNpc(Npc npc, int worldId, int instanceId)
	{
		final SpawnTemplate spawn = npc.getSpawn();
		if (spawn.getWalkerId() != null)
		{
			final InstanceWalkerFormations formations = WalkerFormationsCache.getInstanceFormations(worldId, instanceId);
			final WalkerGroup wg = formations.getSpawnWalkerGroup(spawn.getWalkerId());
			
			if (wg != null)
			{
				npc.setWalkerGroup(wg);
				wg.respawn(npc);
				return false;
			}
			
			final WalkerTemplate template = DataManager.WALKER_DATA.getWalkerTemplate(spawn.getWalkerId());
			if (template == null)
			{
				log.warn("Missing walker ID: " + spawn.getWalkerId());
				return false;
			}
			if (template.getPool() < 2)
			{
				return false;
			}
			return formations.cacheWalkerCandidate(new ClusteredNpc(npc, instanceId, template));
		}
		return false;
	}
	
	/**
	 * Organizes spawns in all processed walker groups. Must be called only when spawning all npcs for the instance of world.
	 * @param worldId
	 * @param instanceId
	 */
	public static void organizeAndSpawn(int worldId, int instanceId)
	{
		final InstanceWalkerFormations formations = WalkerFormationsCache.getInstanceFormations(worldId, instanceId);
		formations.organizeAndSpawn();
	}
	
	/**
	 * @param worldId
	 * @param instanceId
	 */
	public static void onInstanceDestroy(int worldId, int instanceId)
	{
		WalkerFormationsCache.onInstanceDestroy(worldId, instanceId);
	}
	
}
