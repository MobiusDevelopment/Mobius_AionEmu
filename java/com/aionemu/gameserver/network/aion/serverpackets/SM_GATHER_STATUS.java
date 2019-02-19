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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author orz
 */
public class SM_GATHER_STATUS extends AionServerPacket
{
	
	private final int status;
	private final int playerobjid;
	private final int gatherableobjid;
	
	public SM_GATHER_STATUS(int playerobjid, int gatherableobjid, int status)
	{
		this.playerobjid = playerobjid;
		this.gatherableobjid = gatherableobjid;
		this.status = status;
	}
	
	/**
	 * {@inheritDoc}
	 */
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		
		writeD(playerobjid);
		writeD(gatherableobjid);
		writeH(0); // unk
		writeC(status);
		
	}
}
