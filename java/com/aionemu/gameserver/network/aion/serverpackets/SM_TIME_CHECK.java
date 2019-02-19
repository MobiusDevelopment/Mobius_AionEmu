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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * I have no idea wtf is this
 * @author -Nemesiss-
 */
public class SM_TIME_CHECK extends AionServerPacket
{
	
	// Don't be fooled with empty class :D
	// This packet is just sending opcode, without any content
	
	// 1.5.x sending 8 bytes
	
	private final int nanoTime;
	private final int time;
	private final Timestamp dateTime;
	
	public SM_TIME_CHECK(int nanoTime)
	{
		dateTime = new Timestamp((new java.util.Date()).getTime());
		this.nanoTime = nanoTime;
		time = (int) dateTime.getTime();
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(time);
		writeD(nanoTime);
		
	}
}
