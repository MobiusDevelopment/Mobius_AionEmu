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
package com.aionemu.gameserver.model.autogroup;

import static ch.lambdaj.Lambda.having;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.select;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.TeamType;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.portal.PortalLoc;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/**
 * @author xTz
 */
public class AutoGeneralInstance extends AutoInstance
{
	@Override
	public AGQuestion addPlayer(Player player, SearchInstance searchInstance)
	{
		super.writeLock();
		try
		{
			if (!satisfyTime(searchInstance) || (players.size() >= agt.getPlayerSize()))
			{
				return AGQuestion.FAILED;
			}
			final PlayerClass playerClass = player.getPlayerClass();
			final int clericSize = getPlayersByClass(PlayerClass.CLERIC).size();
			final int chanterSize = getPlayersByClass(PlayerClass.CHANTER).size();
			final int songweaverSize = getPlayersByClass(PlayerClass.SONGWEAVER).size();
			final int templarSize = getPlayersByClass(PlayerClass.TEMPLAR).size();
			final int aethertechSize = getPlayersByClass(PlayerClass.AETHERTECH).size();
			if (playerClass.equals(PlayerClass.CLERIC))
			{
				if (clericSize > 0)
				{
					return AGQuestion.FAILED;
				}
			}
			else if (playerClass.equals(PlayerClass.CHANTER))
			{
				if (chanterSize > 0)
				{
					return AGQuestion.FAILED;
				}
			}
			else if (playerClass.equals(PlayerClass.SONGWEAVER))
			{
				if (songweaverSize > 0)
				{
					return AGQuestion.FAILED;
				}
			}
			else if (playerClass.equals(PlayerClass.TEMPLAR))
			{
				if (templarSize > 0)
				{
					return AGQuestion.FAILED;
				}
			}
			else if (playerClass.equals(PlayerClass.AETHERTECH))
			{
				if (aethertechSize > 0)
				{
					return AGQuestion.FAILED;
				}
			}
			else
			{
				int size = players.size();
				size -= clericSize;
				size -= chanterSize;
				size -= templarSize;
				size -= songweaverSize;
				size -= aethertechSize;
				if (size >= 2)
				{
					return AGQuestion.FAILED;
				}
			}
			players.put(player.getObjectId(), new AGPlayer(player));
			return instance != null ? AGQuestion.ADDED : (players.size() == agt.getPlayerSize() ? AGQuestion.READY : AGQuestion.ADDED);
		}
		finally
		{
			super.writeUnlock();
		}
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onEnterInstance(player);
		final List<Player> playersByRace = instance.getPlayersInside();
		if ((playersByRace.size() == 1) && !playersByRace.get(0).isInGroup2())
		{
			final PlayerGroup newGroup = PlayerGroupService.createGroup(playersByRace.get(0), player, TeamType.AUTO_GROUP);
			final int groupId = newGroup.getObjectId();
			if (!instance.isRegistered(groupId))
			{
				instance.register(groupId);
			}
		}
		else if (!playersByRace.isEmpty() && playersByRace.get(0).isInGroup2())
		{
			PlayerGroupService.addPlayer(playersByRace.get(0).getPlayerGroup2(), player);
		}
		final Integer object = player.getObjectId();
		if (!instance.isRegistered(object))
		{
			instance.register(object);
		}
	}
	
	@Override
	public void onPressEnter(Player player)
	{
		super.onPressEnter(player);
		final int worldId = instance.getMapId();
		final PortalPath portal = DataManager.PORTAL2_DATA.getPortalDialog(worldId, 10000, player.getRace());
		if (portal == null)
		{
			return;
		}
		final PortalLoc loc = DataManager.PORTAL_LOC_DATA.getPortalLoc(portal.getLocId());
		if (loc == null)
		{
			return;
		}
		TeleportService2.teleportTo(player, worldId, instance.getInstanceId(), loc.getX(), loc.getY(), loc.getZ(), loc.getH());
		if (player.getPortalCooldownList().getPortalCooldownItem(loc.getWorldId()) != null)
		{
			player.getPortalCooldownList().addPortalCooldown(loc.getWorldId(), 1, DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCooltime(player, worldId));
		}
		else
		{
			player.getPortalCooldownList().addEntry(worldId);
		}
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		super.unregister(player);
		PlayerGroupService.removePlayer(player);
	}
	
	private List<AGPlayer> getPlayersByClass(PlayerClass playerClass)
	{
		return select(players, having(on(AGPlayer.class).getPlayerClass(), equalTo(playerClass)));
	}
}