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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.ls.LoginServer;

/**
 * In this packets aion client is asking for fast reconnection to LoginServer.
 * @author -Nemesiss-
 */
public class CM_RECONNECT_AUTH extends AionClientPacket
{
	/**
	 * Constructs new instance of <tt>CM_RECONNECT_AUTH </tt> packet
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_RECONNECT_AUTH(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		// empty
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final AionConnection client = getConnection();
		// TODO! check if may reconnect
		LoginServer.getInstance().requestAuthReconnection(client);
	}
}
