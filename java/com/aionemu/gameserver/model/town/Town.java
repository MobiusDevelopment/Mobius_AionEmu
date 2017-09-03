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
package com.aionemu.gameserver.model.town;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TOWNS_LIST;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class Town
{
	private final int id;
	private int nameId;
	private int level;
	private int points;
	private final Timestamp levelUpDate;
	private final Race race;
	private PersistentState persistentState;
	private final List<Npc> spawnedNpcs;
	
	public Town(int id, int level, int points, Race race, Timestamp levelUpDate)
	{
		this.id = id;
		this.level = level;
		this.points = points;
		this.levelUpDate = levelUpDate;
		this.race = race;
		persistentState = PersistentState.UPDATED;
		spawnedNpcs = new ArrayList<>();
		spawnNewObjects();
	}
	
	public Town(int id, Race race)
	{
		this(id, 1, 0, race, new Timestamp(60000));
		persistentState = PersistentState.NEW;
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getNameId()
	{
		return nameId;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public synchronized void increasePoints(int amount)
	{
		switch (level)
		{
			case 1:
				if ((points + amount) >= 1000)
				{
					increaseLevel();
				}
				break;
			case 2:
				if ((points + amount) >= 2000)
				{
					increaseLevel();
				}
				break;
			case 3:
				if ((points + amount) >= 3000)
				{
					increaseLevel();
				}
				break;
			case 4:
				if ((points + amount) >= 4000)
				{
					increaseLevel();
				}
				break;
		}
		points += amount;
		setPersistentState(PersistentState.UPDATE_REQUIRED);
	}
	
	private void increaseLevel()
	{
		level++;
		levelUpDate.setTime(System.currentTimeMillis());
		broadcastUpdate();
		despawnOldObjects();
		spawnNewObjects();
	}
	
	private void broadcastUpdate()
	{
		final Map<Integer, Town> data = new HashMap<>(1);
		data.put(id, this);
		final SM_TOWNS_LIST packet = new SM_TOWNS_LIST(data);
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				if (player.getRace() == race)
				{
					PacketSendUtility.sendPacket(player, packet);
				}
			}
		});
	}
	
	private void spawnNewObjects()
	{
		final List<Spawn> newSpawns = DataManager.TOWN_SPAWNS_DATA.getSpawns(id, level);
		final int worldId = DataManager.TOWN_SPAWNS_DATA.getWorldIdForTown(id);
		for (Spawn spawn : newSpawns)
		{
			for (SpawnSpotTemplate sst : spawn.getSpawnSpotTemplates())
			{
				final SpawnTemplate spawnTemplate = SpawnEngine.addNewSpawn(worldId, spawn.getNpcId(), sst.getX(), sst.getY(), sst.getZ(), sst.getHeading(), spawn.getRespawnTime());
				spawnTemplate.setEntityId(sst.getEntityId());
				spawnTemplate.setRandomWalk(0);
				final VisibleObject object = SpawnEngine.spawnObject(spawnTemplate, 1);
				if (object instanceof Npc)
				{
					((Npc) object).setTownId(id);
					spawnedNpcs.add((Npc) object);
				}
			}
		}
	}
	
	private void despawnOldObjects()
	{
		for (Npc npc : spawnedNpcs)
		{
			npc.getController().delete();
		}
		spawnedNpcs.clear();
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public Timestamp getLevelUpDate()
	{
		return levelUpDate;
	}
	
	public PersistentState getPersistentState()
	{
		return persistentState;
	}
	
	public void setPersistentState(PersistentState state)
	{
		if ((persistentState == PersistentState.NEW) && (state == PersistentState.UPDATE_REQUIRED))
		{
			return;
		}
		else
		{
			persistentState = state;
		}
	}
}