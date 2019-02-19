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

import java.util.Calendar;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Created by wanke on 01/02/2017.
 */

public class SM_BOOST_EVENTS extends AionServerPacket
{
	private final int eventStartTime = (int) (Calendar.getInstance().getTimeInMillis() / 1000);
	private final int eventEndTime;
	
	public SM_BOOST_EVENTS()
	{
		eventEndTime = eventStartTime + 6048;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeH(2);
		writeC(0);
		writeC(3);
		
		writeD(eventStartTime);
		writeD(0);
		writeD(eventEndTime);
		writeD(0);
		writeD(150);
		writeQ(-1);
		writeD(0);
		writeD(0);
		
		writeD(eventStartTime);
		writeD(0);
		writeD(eventEndTime);
		writeD(0);
		writeD(200);
		writeQ(-1);
		writeD(0);
		writeD(0);
		
		writeD(eventStartTime);
		writeD(0);
		writeD(eventEndTime);
		writeD(0);
		writeD(200);
		writeQ(-1);
		writeD(0);
		writeD(0);
		writeC(1);
		writeC(2);
		
		writeD(eventStartTime);
		writeD(0);
		writeD(eventEndTime);
		writeD(0);
		writeD(150);
		writeQ(-1);
		writeD(0);
		writeD(0);
		
		writeD(eventStartTime);
		writeD(0);
		writeD(eventEndTime);
		writeD(0);
		writeD(200);
		writeQ(-1);
		writeD(0);
		writeD(0);
	}
}