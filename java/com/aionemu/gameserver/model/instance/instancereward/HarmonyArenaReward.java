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
package com.aionemu.gameserver.model.instance.instancereward;

import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.sort;
import static ch.lambdaj.Lambda.sum;

import java.util.Comparator;
import java.util.List;

import com.aionemu.gameserver.model.autogroup.AGPlayer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * @author xTz
 */
public class HarmonyArenaReward extends PvPArenaReward
{
	private final FastList<HarmonyGroupReward> groups = new FastList<>();
	
	public HarmonyArenaReward(Integer mapId, int instanceId, WorldMapInstance instance)
	{
		super(mapId, instanceId, instance);
	}
	
	public HarmonyGroupReward getHarmonyGroupReward(Integer object)
	{
		for (InstancePlayerReward reward : groups)
		{
			final HarmonyGroupReward harmonyReward = (HarmonyGroupReward) reward;
			if (harmonyReward.containPlayer(object))
			{
				return harmonyReward;
			}
		}
		return null;
	}
	
	public FastList<HarmonyGroupReward> getHarmonyGroupInside()
	{
		final FastList<HarmonyGroupReward> harmonyGroups = new FastList<>();
		for (HarmonyGroupReward group : groups)
		{
			for (AGPlayer agp : group.getAGPlayers())
			{
				if (agp.isInInstance())
				{
					harmonyGroups.add(group);
					break;
				}
			}
		}
		return harmonyGroups;
	}
	
	public FastList<Player> getPlayersInside(HarmonyGroupReward group)
	{
		final FastList<Player> players = new FastList<>();
		for (Player playerInside : instance.getPlayersInside())
		{
			if (group.containPlayer(playerInside.getObjectId()))
			{
				players.add(playerInside);
			}
		}
		return players;
	}
	
	public void addHarmonyGroup(HarmonyGroupReward reward)
	{
		groups.add(reward);
	}
	
	public FastList<HarmonyGroupReward> getGroups()
	{
		return groups;
	}
	
	public void sendPacket(int type, Integer object)
	{
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(type, getTime(), getInstanceReward(), object));
			}
		});
	}
	
	@Override
	public int getRank(int points)
	{
		int rank = -1;
		for (HarmonyGroupReward reward : sortGroupPoints())
		{
			if (reward.getPoints() >= points)
			{
				rank++;
			}
		}
		return rank;
	}
	
	public List<HarmonyGroupReward> sortGroupPoints()
	{
		return sort(groups, on(HarmonyGroupReward.class).getPoints(), new Comparator<Integer>()
		{
			@Override
			public int compare(Integer o1, Integer o2)
			{
				return o2 != null ? o2.compareTo(o1) : -o1.compareTo(o2);
			}
		});
	}
	
	@Override
	public int getTotalPoints()
	{
		return sum(groups, on(HarmonyGroupReward.class).getPoints());
	}
	
	@Override
	public void clear()
	{
		groups.clear();
		super.clear();
	}
}