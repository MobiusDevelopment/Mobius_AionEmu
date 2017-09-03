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
import com.aionemu.loginserver.network.aion.SessionKey;

/**
 * @author -Nemesiss-
 */
public class SM_PLAY_OK extends AionServerPacket
{
	
	/**
	 * playOk1 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int playOk1;
	/**
	 * playOk2 is part of session key - its used for security purposes [checked at game server side]
	 */
	private final int playOk2;
	private final int serverId;
	
	/**
	 * Constructs new instance of <tt>SM_PLAY_OK </tt> packet.
	 * @param key session key
	 */
	public SM_PLAY_OK(SessionKey key, byte serverId)
	{
		super(0x07);
		playOk1 = key.playOk1;
		playOk2 = key.playOk2;
		this.serverId = serverId;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginConnection con)
	{
		writeD(playOk1);
		writeD(playOk2);
		writeC(serverId);
		writeB(new byte[14]);
	}
}
