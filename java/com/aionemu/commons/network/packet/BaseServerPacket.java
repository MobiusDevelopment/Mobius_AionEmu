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
package com.aionemu.commons.network.packet;

import java.nio.ByteBuffer;

import com.aionemu.commons.utils.PrintUtils;

/**
 * Base class for every Server Packet
 * @author -Nemesiss-
 */
public abstract class BaseServerPacket extends BasePacket
{
	
	/**
	 * ByteBuffer that contains this packet data
	 */
	public ByteBuffer buf;
	
	/**
	 * Constructs a new server packet with specified id.
	 * @param opcode packet opcode.
	 */
	protected BaseServerPacket(int opcode)
	{
		super(PacketType.SERVER, opcode);
	}
	
	/**
	 * Constructs a new server packet.<br>
	 * If this constructor was used, then {@link #setOpcode(int)} must be called
	 */
	protected BaseServerPacket()
	{
		super(PacketType.SERVER);
	}
	
	/**
	 * @param buf the buf to set
	 */
	public void setBuf(ByteBuffer buf)
	{
		this.buf = buf;
	}
	
	/**
	 * Write int to buffer.
	 * @param value
	 */
	public final void writeD(int value)
	{
		buf.putInt(value);
	}
	
	/**
	 * Write short to buffer.
	 * @param value
	 */
	public final void writeH(int value)
	{
		buf.putShort((short) value);
	}
	
	/**
	 * Write byte to buffer.
	 * @param value
	 */
	public final void writeC(int value)
	{
		buf.put((byte) value);
	}
	
	/**
	 * Write double to buffer.
	 * @param value
	 */
	public final void writeDF(double value)
	{
		buf.putDouble(value);
	}
	
	/**
	 * Write float to buffer.
	 * @param value
	 */
	public final void writeF(float value)
	{
		buf.putFloat(value);
	}
	
	/**
	 * Write long to buffer.
	 * @param value
	 */
	public final void writeQ(long value)
	{
		buf.putLong(value);
	}
	
	/**
	 * Write String to buffer
	 * @param text
	 */
	public final void writeS(String text)
	{
		if (text == null)
		{
			buf.putChar('\000');
		}
		else
		{
			final int len = text.length();
			for (int i = 0; i < len; i++)
			{
				buf.putChar(text.charAt(i));
			}
			buf.putChar('\000');
		}
	}
	
	/**
	 * Write byte array to buffer.
	 * @param data
	 */
	public final void writeB(byte[] data)
	{
		buf.put(data);
	}
	
	public final void writeB(String bytes)
	{
		writeB(PrintUtils.hex2bytes(bytes));
	}
}
