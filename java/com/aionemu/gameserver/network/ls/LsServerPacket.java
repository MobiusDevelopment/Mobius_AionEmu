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
package com.aionemu.gameserver.network.ls;

import java.nio.ByteBuffer;

import com.aionemu.commons.network.packet.BaseServerPacket;

/**
 * Base class for every GameServer -> Login Server Packet.
 * @author -Nemesiss-
 */
public abstract class LsServerPacket extends BaseServerPacket
{
	
	/**
	 * constructs new server packet with specified opcode.
	 * @param opcode packet id
	 */
	protected LsServerPacket(int opcode)
	{
		super(opcode);
	}
	
	/**
	 * Write this packet data for given connection, to given buffer.
	 * @param con
	 * @param buffer
	 * @param buf
	 */
	public final void write(LoginServerConnection con, ByteBuffer buffer)
	{
		setBuf(buffer);
		buf.putShort((short) 0);
		buf.put((byte) getOpcode());
		writeImpl(con);
		buf.flip();
		buf.putShort((short) buf.limit());
		buf.position(0);
	}
	
	/**
	 * Write data that this packet represents to given byte buffer.
	 * @param con
	 * @param buf
	 */
	protected abstract void writeImpl(LoginServerConnection con);
}
