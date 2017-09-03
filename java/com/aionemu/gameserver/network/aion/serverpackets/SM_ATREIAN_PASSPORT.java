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

import java.sql.Timestamp;
import java.util.Map;

import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.model.templates.event.AttendType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_ATREIAN_PASSPORT extends AionServerPacket
{
	private final int stamps;
	private final int currentPassportId;
	private final int arrival;
	private final Timestamp time;
	private final Map<Integer, AtreianPassport> passports;
	private final int year;
	private final int check;
	
	public SM_ATREIAN_PASSPORT(Map<Integer, AtreianPassport> passports, int stamps, int currentPassportId, Timestamp time, int year, int arrival, int check)
	{
		this.passports = passports;
		this.stamps = stamps;
		this.currentPassportId = currentPassportId;
		this.time = time;
		this.arrival = arrival;
		this.check = check;
		this.year = year;
	}
	
	@Override
	protected void writeImpl(AionConnection paramAionConnection)
	{
		writeH(year);
		writeH(arrival);
		writeH(0);
		writeH(passports.size());
		writeD(currentPassportId);
		if (check != 0)
		{
			writeD(0);
		}
		else
		{
			writeD(stamps);
		}
		for (AtreianPassport atp : passports.values())
		{
			writeH(atp.getRewardId());
			if (atp.isFinish() && (atp.getAttendType() == AttendType.CUMULATIVE))
			{
				writeH(1);
			}
			else
			{
				writeH(0);
			}
			writeD((int) time.getTime() / 1000);
			writeD(atp.getId());
			writeD(stamps);
			atp.setRewardId(0);
			atp.setFinish(false);
		}
		writeH(0);
		writeH(0);
		writeD((int) time.getTime() / 1000);
	}
}