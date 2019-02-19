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
package com.aionemu.gameserver.model.gameobjects.siege;

import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;

/**
 * @author ViAl
 */
public class SiegeNpc extends Npc
{
	private final int siegeId;
	private final SiegeRace siegeRace;
	
	/**
	 * @param objId
	 * @param controller
	 * @param spawnTemplate
	 * @param objectTemplate SiegeNpc constructor
	 */
	public SiegeNpc(int objId, NpcController controller, SiegeSpawnTemplate spawnTemplate, NpcTemplate objectTemplate)
	{
		super(objId, controller, spawnTemplate, objectTemplate);
		siegeId = spawnTemplate.getSiegeId();
		siegeRace = spawnTemplate.getSiegeRace();
	}
	
	public SiegeRace getSiegeRace()
	{
		return siegeRace;
	}
	
	public int getSiegeId()
	{
		return siegeId;
	}
	
	@Override
	public SiegeSpawnTemplate getSpawn()
	{
		return (SiegeSpawnTemplate) super.getSpawn();
	}
	
	@Override
	public boolean isAggressiveTo(Creature creature)
	{
		if ((creature instanceof SiegeNpc) && (getSiegeRace() != ((SiegeNpc) creature).getSiegeRace()))
		{
			return true;
		}
		
		return super.isAggressiveTo(creature);
	}
}
