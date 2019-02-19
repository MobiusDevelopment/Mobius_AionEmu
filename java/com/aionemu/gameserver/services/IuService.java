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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.iu.IuStateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.iuspawns.IuSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.iuservice.CircusBound;
import com.aionemu.gameserver.services.iuservice.Iu;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class IuService
{
	private Map<Integer, IuLocation> iu;
	private static final int duration = CustomConfig.IU_DURATION;
	private final Map<Integer, Iu<?>> activeConcert = new FastMap<Integer, Iu<?>>().shared();
	
	public void initConcertLocations()
	{
		if (CustomConfig.IU_ENABLED)
		{
			iu = DataManager.IU_DATA.getIuLocations();
			for (IuLocation loc : getIuLocations().values())
			{
				spawn(loc, IuStateType.CLOSED);
			}
			CronService.getInstance().schedule(() ->
			{
				for (IuLocation loc : getIuLocations().values())
				{
					startConcert(loc.getId());
				}
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_OPEN));
			}, CustomConfig.IU_SCHEDULE);
		}
		else
		{
			iu = Collections.emptyMap();
		}
	}
	
	public void startConcert(int id)
	{
		final Iu<?> circusBound;
		synchronized (this)
		{
			if (activeConcert.containsKey(id))
			{
				return;
			}
			circusBound = new CircusBound(iu.get(id));
			activeConcert.put(id, circusBound);
		}
		circusBound.start();
		lPCHCountdownMsg(id);
		ThreadPoolManager.getInstance().schedule(() -> stopConcert(id), duration * 3600 * 1000);
	}
	
	public void stopConcert(int id)
	{
		if (!isConcertInProgress(id))
		{
			return;
		}
		Iu<?> circusBound;
		synchronized (this)
		{
			circusBound = activeConcert.remove(id);
		}
		if ((circusBound == null) || circusBound.isFinished())
		{
			return;
		}
		circusBound.stop();
	}
	
	public void spawn(IuLocation loc, IuStateType iustate)
	{
		if (iustate.equals(IuStateType.OPEN))
		{
		}
		final List<SpawnGroup2> locSpawns = DataManager.SPAWNS_DATA2.getIuSpawnsByLocId(loc.getId());
		for (SpawnGroup2 group : locSpawns)
		{
			for (SpawnTemplate st : group.getSpawnTemplates())
			{
				final IuSpawnTemplate iutemplate = (IuSpawnTemplate) st;
				if (iutemplate.getIUStateType().equals(iustate))
				{
					loc.getSpawned().add(SpawnEngine.spawnObject(iutemplate, 1));
				}
			}
		}
	}
	
	/**
	 * Live Party Concert Hall Countdown.
	 * @param id
	 * @return
	 */
	public boolean lPCHCountdownMsg(int id)
	{
		switch (id)
		{
			case 1:
			{
				World.getInstance().doOnAllPlayers(player ->
				{
					// The entrance to the Live Party Concert Hall appeared.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_OPEN, 0);
					// The entrance to the Live Party Concert Hall closes in 90 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_90M, 1800000);
					// The entrance to the Live Party Concert Hall closes in 60 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_60M, 3600000);
					// The entrance to the Live Party Concert Hall closes in 30 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_30M, 5400000);
					// The entrance to the Live Party Concert Hall closes in 15 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_15M, 6300000);
					// The entrance to the Live Party Concert Hall closes in 10 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_10M, 6600000);
					// The entrance to the Live Party Concert Hall closes in 5 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_5M, 6900000);
					// The entrance to the Live Party Concert Hall closes in 3 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_3M, 7020000);
					// The entrance to the Live Party Concert Hall closes in 2 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_2M, 7080000);
					// The entrance to the Live Party Concert Hall closes in 1 minutes. Escape will engage.
					PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_EVENT_DIRECT_PORTAL_CLOSE_TIMER_1M, 7140000);
				});
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	public void despawn(IuLocation loc)
	{
		for (VisibleObject npc : loc.getSpawned())
		{
			((Npc) npc).getController().cancelTask(TaskId.RESPAWN);
			npc.getController().onDelete();
		}
		loc.getSpawned().clear();
	}
	
	public boolean isConcertInProgress(int id)
	{
		return activeConcert.containsKey(id);
	}
	
	public Map<Integer, Iu<?>> getActiveIu()
	{
		return activeConcert;
	}
	
	public int getDuration()
	{
		return duration;
	}
	
	public IuLocation getIuLocation(int id)
	{
		return iu.get(id);
	}
	
	public Map<Integer, IuLocation> getIuLocations()
	{
		return iu;
	}
	
	public static IuService getInstance()
	{
		return IuServiceHolder.INSTANCE;
	}
	
	private static class IuServiceHolder
	{
		static final IuService INSTANCE = new IuService();
	}
}