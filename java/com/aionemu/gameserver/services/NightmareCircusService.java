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
package com.aionemu.gameserver.services;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.shedule.CircusSchedule;
import com.aionemu.gameserver.configs.shedule.CircusSchedule.Circus;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusLocation;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.nightmarecircusspawns.NightmareCircusSpawnTemplate;
import com.aionemu.gameserver.services.nightmarecircusservice.CircusInstance;
import com.aionemu.gameserver.services.nightmarecircusservice.CircusStartRunnable;
import com.aionemu.gameserver.services.nightmarecircusservice.Nightmare;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class NightmareCircusService
{
	private CircusSchedule circusSchedule;
	private Map<Integer, NightmareCircusLocation> nightmareCircus;
	private static final int duration = CustomConfig.NIGHTMARE_CIRCUS_DURATION;
	private final Map<Integer, CircusInstance<?>> activeNightmareCircus = new FastMap<Integer, CircusInstance<?>>().shared();
	
	public void initCircusLocations()
	{
		if (CustomConfig.NIGHTMARE_CIRCUS_ENABLE)
		{
			nightmareCircus = DataManager.NIGHTMARE_CIRCUS_DATA.getNightmareCircusLocations();
			for (NightmareCircusLocation loc : getNightmareCircusLocations().values())
			{
				spawn(loc, NightmareCircusStateType.CLOSED);
			}
		}
		else
		{
			nightmareCircus = Collections.emptyMap();
		}
	}
	
	public void initCircus()
	{
		if (CustomConfig.NIGHTMARE_CIRCUS_ENABLE)
		{
			circusSchedule = CircusSchedule.load();
			for (Circus circus : circusSchedule.getCircussList())
			{
				for (String circusTime : circus.getCircusTimes())
				{
					CronService.getInstance().schedule(new CircusStartRunnable(circus.getId()), circusTime);
				}
			}
		}
	}
	
	public void startNightmareCircus(int id)
	{
		final CircusInstance<?> nightmare;
		synchronized (this)
		{
			if (activeNightmareCircus.containsKey(id))
			{
				return;
			}
			nightmare = new Nightmare(nightmareCircus.get(id));
			activeNightmareCircus.put(id, nightmare);
		}
		nightmare.start();
		dreamFaerieMsg(id);
		ThreadPoolManager.getInstance().schedule(() -> stopNightmareCircus(id), duration * 3600 * 1000);
	}
	
	public void stopNightmareCircus(int id)
	{
		if (!isNightmareCircusInProgress(id))
		{
			return;
		}
		CircusInstance<?> nightmare;
		synchronized (this)
		{
			nightmare = activeNightmareCircus.remove(id);
		}
		if ((nightmare == null) || nightmare.isClosed())
		{
			return;
		}
		nightmare.stop();
	}
	
	public void spawn(NightmareCircusLocation loc, NightmareCircusStateType nstate)
	{
		if (nstate.equals(NightmareCircusStateType.OPEN))
		{
		}
		final List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getNightmareCircusSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns)
		{
			for (SpawnTemplate st : group.getSpawnTemplates())
			{
				final NightmareCircusSpawnTemplate nightmareCircustemplate = (NightmareCircusSpawnTemplate) st;
				if (nightmareCircustemplate.getNStateType().equals(nstate))
				{
					loc.getSpawned().add(SpawnEngine.spawnObject(nightmareCircustemplate, 1));
				}
			}
		}
	}
	
	public boolean dreamFaerieMsg(int id)
	{
		switch (id)
		{
			case 1:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendSys3Message(player, "\uE09B", "<Nightmare Circus> is now open !!!"));
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	public void despawn(NightmareCircusLocation loc)
	{
		for (VisibleObject npc : loc.getSpawned())
		{
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}
	
	public boolean isNightmareCircusInProgress(int id)
	{
		return activeNightmareCircus.containsKey(id);
	}
	
	public Map<Integer, CircusInstance<?>> getActiveNightmareCircus()
	{
		return activeNightmareCircus;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public NightmareCircusLocation getNightmareCircusLocation(int id)
	{
		return nightmareCircus.get(id);
	}
	
	public Map<Integer, NightmareCircusLocation> getNightmareCircusLocations()
	{
		return nightmareCircus;
	}
	
	public static NightmareCircusService getInstance()
	{
		return NightmareCircusServiceHolder.INSTANCE;
	}
	
	private static class NightmareCircusServiceHolder
	{
		static final NightmareCircusService INSTANCE = new NightmareCircusService();
	}
}