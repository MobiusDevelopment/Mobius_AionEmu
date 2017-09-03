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

import com.aionemu.loginserver.network.aion.AionAuthResponse;
import com.aionemu.loginserver.network.aion.AionServerPacket;
import com.aionemu.loginserver.network.aion.LoginConnection;

/**
 * @author -Nemesiss-
 */
public class SM_PLAY_FAIL extends AionServerPacket
{
	
	/**
	 * response - why play fail
	 */
	private final AionAuthResponse response;
	
	/**
	 * Constructs new instance of <tt>SM_PLAY_FAIL</tt> packet.
	 * @param response auth response
	 */
	public SM_PLAY_FAIL(AionAuthResponse response)
	{
		super(0x06);
		this.response = response;
	}
	
	@Override
	protected void writeImpl(LoginConnection con)
	{
		writeD(response.getMessageId());
	}
}
