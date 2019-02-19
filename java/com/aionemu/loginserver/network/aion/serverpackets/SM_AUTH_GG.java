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
package com.aionemu.loginserver.network.aion.serverpackets;

import com.aionemu.loginserver.network.aion.AionServerPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;

/**
 * @author -Nemesiss-
 */
public class SM_AUTH_GG extends AionServerPacket
{
	
	/**
	 * Session Id of this connection
	 */
	private final int sessionId;
	
	/**
	 * Constructs new instance of <tt>SM_AUTH_GG</tt> packet
	 * @param sessionId
	 */
	public SM_AUTH_GG(int sessionId)
	{
		super(0x0b);
		
		this.sessionId = sessionId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginConnection con)
	{
		writeD(sessionId);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeD(0x00);
		writeB(new byte[0x19]);
	}
}
