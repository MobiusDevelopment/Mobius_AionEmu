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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.team2.league.LeagueMember;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sarynth, xTz
 */
public class SM_ALLIANCE_INFO extends AionServerPacket
{
	
	private LootGroupRules lootRules;
	private final PlayerAlliance alliance;
	private final int leaderid;
	private final int groupid;
	private final int messageId;
	private final String message;
	// Alliance
	// %0 is now Vice Captain of the alliance.
	public static final int FORCE_PROMOTE_MANAGER = 1300984;
	// %0 has been demoted to member from Vice Captain.
	public static final int FORCE_DEMOTE_MANAGER = 1300985;
	// League
	// Your Alliance has joined %0's League.
	public static final int UNION_ENTER = 1400560;
	// %0's Alliance has left the League.
	public static final int UNION_LEAVE = 1400572;
	// You have expelled %0's alliance from the Alliance League.
	public static final int UNION_BAN_HIM = 1400574;
	// %0 has expelled your alliance from the Alliance League.
	public static final int UNION_BAN_ME = 1400576;
	
	public SM_ALLIANCE_INFO(PlayerAlliance alliance)
	{
		this(alliance, 0, StringUtils.EMPTY);
	}
	
	public SM_ALLIANCE_INFO(PlayerAlliance alliance, int messageId, String message)
	{
		this.alliance = alliance;
		groupid = alliance.getObjectId();
		leaderid = alliance.getLeader().getObjectId();
		lootRules = alliance.getLootGroupRules();
		this.messageId = messageId;
		this.message = message;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final Player player = con.getActivePlayer();
		writeH(alliance.groupSize());
		writeD(groupid);
		writeD(leaderid);
		writeD(player.getWorldId());
		final Collection<Integer> ids = alliance.getViceCaptainIds();
		for (Integer id : ids)
		{
			writeD(id);
		}
		for (int i = 0; i < (4 - ids.size()); i++)
		{
			writeD(0);
		}
		writeD(lootRules.getLootRule().getId());
		writeD(lootRules.getMisc());
		writeD(lootRules.getCommonItemAbove());
		writeD(lootRules.getSuperiorItemAbove());
		writeD(lootRules.getHeroicItemAbove());
		writeD(lootRules.getFabledItemAbove());
		writeD(lootRules.getEthernalItemAbove());
		writeD(lootRules.getAutodistribution().getId());
		writeD(0x02);
		writeC(0x00);
		writeD(alliance.getTeamType().getType());
		writeD(alliance.getTeamType().getSubType());
		writeD(alliance.isInLeague() ? alliance.getLeague().getTeamId() : 0);
		for (int a = 0; a < 4; a++)
		{
			writeD(a);
			writeD(1000 + a);
		}
		writeD(messageId);
		writeS(messageId != 0 ? message : StringUtils.EMPTY);
		if (alliance.isInLeague())
		{
			lootRules = alliance.getLeague().getLootGroupRules();
			writeH(alliance.getLeague().size());
			writeD(lootRules.getLootRule().getId());
			writeD(lootRules.getAutodistribution().getId());
			writeD(lootRules.getCommonItemAbove());
			writeD(lootRules.getSuperiorItemAbove());
			writeD(lootRules.getHeroicItemAbove());
			writeD(lootRules.getFabledItemAbove());
			writeD(lootRules.getEthernalItemAbove());
			writeD(0);
			writeD(0);
			for (LeagueMember leagueMember : alliance.getLeague().getSortedMembers())
			{
				writeD(leagueMember.getLeaguePosition());
				writeD(leagueMember.getObjectId());
				writeD(leagueMember.getObject().size());
				writeS(leagueMember.getObject().getLeaderObject().getName());
			}
		}
	}
}