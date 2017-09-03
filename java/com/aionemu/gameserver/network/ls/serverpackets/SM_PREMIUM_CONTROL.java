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
import com.aionemu.gameserver.model.ingameshop.IGRequest;
import com.aionemu.gameserver.network.ls.LoginServerConnection;
import com.aionemu.gameserver.network.ls.LsServerPacket;

/**
 * @author KID
 */
public class SM_PREMIUM_CONTROL extends LsServerPacket
{
	private final IGRequest request;
	private final long cost;
	
	public SM_PREMIUM_CONTROL(IGRequest request, long cost)
	{
		super(0x0B);
		this.request = request;
		this.cost = cost;
	}
	
	@Override
	protected void writeImpl(LoginServerConnection con)
	{
		writeD(request.accountId);
		writeD(request.requestId);
		writeQ(cost);
		writeC(NetworkConfig.GAMESERVER_ID);
	}
}
