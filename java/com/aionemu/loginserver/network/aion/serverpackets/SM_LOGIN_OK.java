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
public class SM_LOGIN_OK extends AionServerPacket
{
	
	/**
	 * accountId is part of session key - its used for security purposes
	 */
	private final int accountId;
	/**
	 * loginOk is part of session key - its used for security purposes
	 */
	private final int loginOk;
	
	/**
	 * Constructs new instance of <tt>SM_LOGIN_OK</tt> packet.
	 * @param key session key
	 */
	public SM_LOGIN_OK(SessionKey key)
	{
		super(3);
		accountId = key.accountId;
		loginOk = key.loginOk;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(LoginConnection con)
	{
		writeD(accountId);
		writeD(loginOk);
		writeD(0);
		writeD(0);
		writeD(1002);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeD(0);
		writeB(new byte[19]);
	}
}
