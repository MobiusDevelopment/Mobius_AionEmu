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
package com.aionemu.chatserver.network.gs.serverpackets;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.gs.GsAuthResponse;
import com.aionemu.chatserver.network.gs.GsConnection;
import com.aionemu.chatserver.network.gs.GsServerPacket;

/**
 * @author ATracer
 */
public class SM_GS_AUTH_RESPONSE extends GsServerPacket
{
	
	private final GsAuthResponse response;
	
	public SM_GS_AUTH_RESPONSE(GsAuthResponse resp)
	{
		response = resp;
	}
	
	@Override
	protected void writeImpl(GsConnection con)
	{
		writeC(0);
		writeC(response.getResponseId());
		writeB(Config.CHAT_ADDRESS.getAddress().getAddress());
		writeH(Config.CHAT_ADDRESS.getPort());
	}
}
