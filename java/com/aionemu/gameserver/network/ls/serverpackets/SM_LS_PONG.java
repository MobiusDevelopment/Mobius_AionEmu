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

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.ls.LoginServerConnection;
import com.aionemu.gameserver.network.ls.LsServerPacket;

/**
 * @author KID
 */
public class SM_LS_PONG extends LsServerPacket
{
	private final int pid;
	
	public SM_LS_PONG(int pid)
	{
		super(12);
		this.pid = pid;
	}
	
	@Override
	protected void writeImpl(LoginServerConnection con)
	{
		writeC(NetworkConfig.GAMESERVER_ID);
		writeD(pid);
	}
}
