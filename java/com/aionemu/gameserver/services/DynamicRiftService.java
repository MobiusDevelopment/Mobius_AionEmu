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
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftLocation;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftStateType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.dynamicriftspawns.DynamicRiftSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.dynamicriftservice.DynamicRift;
import com.aionemu.gameserver.services.dynamicriftservice.Portal;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class DynamicRiftService
{
	private Map<Integer, DynamicRiftLocation> dynamicRift;
	private static final int duration = CustomConfig.DYNAMIC_RIFT_DURATION;
	private final Map<Integer, DynamicRift<?>> activeDynamicRift = new FastMap<Integer, DynamicRift<?>>().shared();
	
	public void initDynamicRiftLocations()
	{
		if (CustomConfig.DYNAMIC_RIFT_ENABLED)
		{
			dynamicRift = DataManager.DYNAMIC_RIFT_DATA.getDynamicRiftLocations();
			for (DynamicRiftLocation loc : getDynamicRiftLocations().values())
			{
				spawn(loc, DynamicRiftStateType.CLOSED);
			}
			CronService.getInstance().schedule(() ->
			{
				startDynamicRift(1);
				startDynamicRift(3);
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_PORTAL_OPEN_IDDF3_Dragon));
			}, CustomConfig.DYNAMIC_RIFT_DRAGON_SCHEDULE);
			CronService.getInstance().schedule(() ->
			{
				startDynamicRift(2);
				startDynamicRift(4);
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_PORTAL_OPEN_IDLF3_Castle_Indratoo));
			}, CustomConfig.DYNAMIC_RIFT_INDRATOO_SCHEDULE);
			// Shugo Merchant League
			CronService.getInstance().schedule(() ->
			{
				startDynamicRift(5);
				startDynamicRift(6);
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HF_ShugoCaravanAppear));
			}, CustomConfig.SHUGO_MERCHANT_LEAGUE_SCHEDULE);
			// Tower Of Eternity
			CronService.getInstance().schedule(() ->
			{
				startDynamicRift(7);
				startDynamicRift(8);
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendSys3Message(player, "\uE04C", "<Tower Of Eternity> open !!!"));
			}, CustomConfig.TOWER_OF_ETERNITY_SCHEDULE);
		}
		else
		{
			dynamicRift = Collections.emptyMap();
		}
	}
	
	public void startDynamicRift(int id)
	{
		final DynamicRift<?> portal;
		synchronized (this)
		{
			if (activeDynamicRift.containsKey(id))
			{
				return;
			}
			portal = new Portal(dynamicRift.get(id));
			activeDynamicRift.put(id, portal);
		}
		portal.start();
		ThreadPoolManager.getInstance().schedule(() -> stopDynamicRift(id), duration * 3600 * 1000);
	}
	
	public void stopDynamicRift(int id)
	{
		if (!isDynamicRiftInProgress(id))
		{
			return;
		}
		DynamicRift<?> portal;
		synchronized (this)
		{
			portal = activeDynamicRift.remove(id);
		}
		if ((portal == null) || portal.isClosed())
		{
			return;
		}
		portal.stop();
	}
	
	public void spawn(DynamicRiftLocation loc, DynamicRiftStateType dstate)
	{
		if (dstate.equals(DynamicRiftStateType.OPEN))
		{
		}
		final List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getDynamicRiftSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns)
		{
			for (SpawnTemplate st : group.getSpawnTemplates())
			{
				final DynamicRiftSpawnTemplate dynamicRifttemplate = (DynamicRiftSpawnTemplate) st;
				if (dynamicRifttemplate.getDStateType().equals(dstate))
				{
					loc.getSpawned().add(SpawnEngine.spawnObject(dynamicRifttemplate, 1));
				}
			}
		}
	}
	
	public void despawn(DynamicRiftLocation loc)
	{
		for (VisibleObject npc : loc.getSpawned())
		{
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}
	
	public boolean isDynamicRiftInProgress(int id)
	{
		return activeDynamicRift.containsKey(id);
	}
	
	public Map<Integer, DynamicRift<?>> getActiveDynamicRift()
	{
		return activeDynamicRift;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public DynamicRiftLocation getDynamicRiftLocation(int id)
	{
		return dynamicRift.get(id);
	}
	
	public Map<Integer, DynamicRiftLocation> getDynamicRiftLocations()
	{
		return dynamicRift;
	}
	
	public static DynamicRiftService getInstance()
	{
		return DynamicRiftServiceHolder.INSTANCE;
	}
	
	private static class DynamicRiftServiceHolder
	{
		static final DynamicRiftService INSTANCE = new DynamicRiftService();
	}
}