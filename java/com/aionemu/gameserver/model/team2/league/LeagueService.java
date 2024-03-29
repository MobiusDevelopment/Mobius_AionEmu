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
package com.aionemu.gameserver.model.team2.league;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.alliance.callback.PlayerAllianceDisbandCallback;
import com.aionemu.gameserver.model.team2.league.events.LeagueDisbandEvent;
import com.aionemu.gameserver.model.team2.league.events.LeagueEnteredEvent;
import com.aionemu.gameserver.model.team2.league.events.LeagueInvite;
import com.aionemu.gameserver.model.team2.league.events.LeagueLeftEvent;
import com.aionemu.gameserver.model.team2.league.events.LeagueLeftEvent.LeaveReson;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.restrictions.RestrictionsManager;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.google.common.base.Preconditions;

/**
 * @author ATracer
 */
public class LeagueService
{
	static final Map<Integer, League> leagues = new ConcurrentHashMap<>();
	
	static
	{
		GlobalCallbackHelper.addCallback(new AllianceDisbandListener());
	}
	
	public static void inviteToLeague(Player inviter, Player invited)
	{
		if (canInvite(inviter, invited))
		{
			final LeagueInvite invite = new LeagueInvite(inviter, invited);
			if (invited.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_MSGBOX_UNION_INVITE_ME, invite))
			{
				if (invited.isInAlliance2())
				{
					PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_UNION_INVITE_HIM(invited.getName(), inviter.getName()));
				}
				PacketSendUtility.sendPacket(invited, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_MSGBOX_UNION_INVITE_ME, 0, 0, inviter.getName()));
			}
		}
	}
	
	public static boolean canInvite(Player inviter, Player invited)
	{
		if (inviter.isInInstance())
		{
			if (AutoGroupService.getInstance().isAutoInstance(inviter.getInstanceId()))
			{
				// You cannot use invite, leave or kick commands related to your group or alliance in this region.
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
				return false;
			}
		}
		if (invited.isInInstance())
		{
			if (AutoGroupService.getInstance().isAutoInstance(invited.getInstanceId()))
			{
				// You cannot use invite, leave or kick commands related to your group or alliance in this region.
				PacketSendUtility.sendPacket(inviter, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_CANT_OPERATE_PARTY_COMMAND);
				return false;
			}
		}
		return RestrictionsManager.canInviteToLeague(inviter, invited);
	}
	
	public static League createLeague(Player inviter, Player invited)
	{
		final PlayerAlliance alliance = inviter.getPlayerAlliance2();
		Preconditions.checkNotNull(alliance, "League can not be null");
		final League newLeague = new League(new LeagueMember(alliance, 0));
		leagues.put(newLeague.getTeamId(), newLeague);
		addAlliance(newLeague, alliance);
		return newLeague;
	}
	
	public static void addAlliance(League league, PlayerAlliance alliance)
	{
		Preconditions.checkNotNull(league, "League should not be null");
		league.onEvent(new LeagueEnteredEvent(league, alliance));
	}
	
	public static void addAllianceToLeague(League league, PlayerAlliance alliance)
	{
		league.addMember(new LeagueMember(alliance, league.size()));
	}
	
	public static void removeAlliance(PlayerAlliance alliance)
	{
		if (alliance != null)
		{
			final League league = alliance.getLeague();
			Preconditions.checkNotNull(league, "League should not be null");
			league.onEvent(new LeagueLeftEvent(league, alliance));
		}
	}
	
	public static void expelAlliance(Player expelledPlayer, Player expelGiver)
	{
		Preconditions.checkNotNull(expelledPlayer, "Expelled player should not be null");
		Preconditions.checkNotNull(expelGiver, "ExpelGiver player should not be null");
		Preconditions.checkArgument(expelGiver.isInLeague(), "Expelled player should be in league");
		Preconditions.checkArgument(expelledPlayer.isInLeague(), "ExpelGiver should be in league");
		Preconditions.checkArgument(expelGiver.getPlayerAlliance2().getLeague().isLeader(expelGiver.getPlayerAlliance2()), "ExpelGiver alliance should be the leader of league");
		Preconditions.checkArgument(expelGiver.getPlayerAlliance2().isLeader(expelGiver), "ExpelGiver should be the leader of alliance");
		final PlayerAlliance alliance = expelGiver.getPlayerAlliance2();
		final League league = alliance.getLeague();
		league.onEvent(new LeagueLeftEvent(league, expelledPlayer.getPlayerAlliance2(), LeaveReson.EXPEL));
	}
	
	public static void disband(League league)
	{
		Preconditions.checkState(league.onlineMembers() <= 1, "Can't disband league with more than one online member");
		leagues.remove(league.getTeamId());
		league.onEvent(new LeagueDisbandEvent(league));
	}
	
	static class AllianceDisbandListener extends PlayerAllianceDisbandCallback
	{
		@Override
		public void onBeforeAllianceDisband(PlayerAlliance alliance)
		{
		}
		
		@Override
		public void onAfterAllianceDisband(PlayerAlliance alliance)
		{
			try
			{
				for (League league : leagues.values())
				{
					if (league.hasMember(alliance.getTeamId()))
					{
						league.onEvent(new LeagueLeftEvent(league, alliance));
					}
				}
			}
			catch (Throwable t)
			{
				// log.error("Error during alliance disband listen", t);
			}
		}
	}
}
