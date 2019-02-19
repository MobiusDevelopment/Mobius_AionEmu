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
package com.aionemu.loginserver.network.gs.serverpackets;

import com.aionemu.loginserver.network.gs.GsConnection;
import com.aionemu.loginserver.network.gs.GsServerPacket;

/**
 * @author cura
 */
public class SM_GS_CHARACTER_RESPONSE extends GsServerPacket
{
	
	private final int accountId;
	
	/**
	 * @param accountId
	 */
	public SM_GS_CHARACTER_RESPONSE(int accountId)
	{
		this.accountId = accountId;
	}
	
	@Override
	protected void writeImpl(GsConnection con)
	{
		writeC(8);
		writeD(accountId);
	}
}
