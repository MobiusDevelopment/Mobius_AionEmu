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
package com.aionemu.gameserver.network.ls.serverpackets;

import com.aionemu.gameserver.network.ls.LoginServerConnection;
import com.aionemu.gameserver.network.ls.LsServerPacket;

/**
 * @author cura
 */
public class SM_GS_CHARACTER extends LsServerPacket
{
	
	private final int accountId;
	private final int characterCount;
	
	/**
	 * @param accountId
	 * @param characterCount
	 */
	public SM_GS_CHARACTER(int accountId, int characterCount)
	{
		super(0x08);
		this.accountId = accountId;
		this.characterCount = characterCount;
	}
	
	@Override
	protected void writeImpl(LoginServerConnection con)
	{
		writeD(accountId);
		writeC(characterCount);
	}
}
