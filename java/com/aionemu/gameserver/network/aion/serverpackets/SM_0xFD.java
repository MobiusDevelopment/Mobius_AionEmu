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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_0xFD extends AionServerPacket
{
	private final int unk;
	private static final Logger log = LoggerFactory.getLogger(SM_0xFD.class);
	
	public SM_0xFD(int unk)
	{
		this.unk = unk;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		// This H 1~2 Random what Packet?
		writeH(unk);
		log.info("SM_0xFD : " + unk);
		writeC(0);
	}
}
