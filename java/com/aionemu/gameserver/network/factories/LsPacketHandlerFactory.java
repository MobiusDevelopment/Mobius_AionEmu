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
package com.aionemu.gameserver.network.factories;

import com.aionemu.gameserver.network.ls.LoginServerConnection.State;
import com.aionemu.gameserver.network.ls.LsClientPacket;
import com.aionemu.gameserver.network.ls.LsPacketHandler;
import com.aionemu.gameserver.network.ls.clientpackets.CM_ACCOUNT_RECONNECT_KEY;
import com.aionemu.gameserver.network.ls.clientpackets.CM_ACOUNT_AUTH_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_BAN_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_GS_AUTH_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_GS_CHARACTER_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_LS_CONTROL_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_LS_PING;
import com.aionemu.gameserver.network.ls.clientpackets.CM_MACBAN_LIST;
import com.aionemu.gameserver.network.ls.clientpackets.CM_PREMIUM_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_PTRANSFER_RESPONSE;
import com.aionemu.gameserver.network.ls.clientpackets.CM_REQUEST_KICK_ACCOUNT;

/**
 * @author Luno
 */
public class LsPacketHandlerFactory
{
	
	private final LsPacketHandler handler = new LsPacketHandler();
	
	public static LsPacketHandlerFactory getInstance()
	{
		return SingletonHolder.instance;
	}
	
	/**
	 */
	private LsPacketHandlerFactory()
	{
		addPacket(new CM_ACCOUNT_RECONNECT_KEY(0x03), State.AUTHED);
		addPacket(new CM_ACOUNT_AUTH_RESPONSE(0x01), State.AUTHED);
		addPacket(new CM_GS_AUTH_RESPONSE(0x00), State.CONNECTED);
		addPacket(new CM_REQUEST_KICK_ACCOUNT(0x02), State.AUTHED);
		addPacket(new CM_LS_CONTROL_RESPONSE(0x04), State.AUTHED);
		addPacket(new CM_BAN_RESPONSE(0x05), State.AUTHED);
		addPacket(new CM_GS_CHARACTER_RESPONSE(0x08), State.AUTHED);
		addPacket(new CM_MACBAN_LIST(9), State.AUTHED);
		addPacket(new CM_PREMIUM_RESPONSE(10), State.AUTHED);
		addPacket(new CM_LS_PING(11), State.AUTHED);
		addPacket(new CM_PTRANSFER_RESPONSE(12), State.AUTHED);
	}
	
	private void addPacket(LsClientPacket prototype, State... states)
	{
		handler.addPacketPrototype(prototype, states);
	}
	
	public LsPacketHandler getPacketHandler()
	{
		return handler;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		
		protected static final LsPacketHandlerFactory instance = new LsPacketHandlerFactory();
	}
}
