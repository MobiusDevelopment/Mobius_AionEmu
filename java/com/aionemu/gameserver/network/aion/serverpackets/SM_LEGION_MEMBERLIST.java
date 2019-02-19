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

import java.util.List;

import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.team.legion.LegionMemberEx;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.HousingService;

/**
 * @author Simple
 */
public class SM_LEGION_MEMBERLIST extends AionServerPacket
{
	private static final int OFFLINE = 0x00;
	private static final int ONLINE = 0x01;
	private final boolean isFirst;
	private final List<LegionMemberEx> legionMembers;
	
	public SM_LEGION_MEMBERLIST(List<LegionMemberEx> legionMembers, boolean isFirst)
	{
		this.legionMembers = legionMembers;
		this.isFirst = isFirst;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final int size = legionMembers.size();
		int x = 1;
		writeC(isFirst ? 1 : 0);
		writeH((65536 - size));
		for (LegionMemberEx legionMember : legionMembers)
		{
			if (x > size)
			{
				break;
			}
			writeD(legionMember.getObjectId());
			writeS(legionMember.getName());
			writeC(legionMember.getPlayerClass().getClassId());
			writeD(legionMember.getLevel());
			writeC(legionMember.getRank().getRankId());
			writeD(legionMember.getWorldId());
			writeC(legionMember.isOnline() ? ONLINE : OFFLINE);
			writeS(legionMember.getSelfIntro());
			writeS(legionMember.getNickname());
			writeD(legionMember.getLastOnline());
			final int address = HousingService.getInstance().getPlayerAddress(legionMember.getObjectId());
			if (address > 0)
			{
				House house = HousingService.getInstance().getPlayerStudio(legionMember.getObjectId());
				if (house == null)
				{
					house = HousingService.getInstance().getHouseByAddress(address);
				}
				writeD(address);
				writeD(house.getDoorState().getPacketValue());
			}
			else
			{
				writeD(0);
				writeD(0);
			}
			writeC(1);
			writeC(0);
			writeC(0);
			writeC(0);
			x++;
		}
	}
}