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
package com.aionemu.chatserver.common.netty;

import org.jboss.netty.buffer.ChannelBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class BaseClientPacket extends AbstractPacket
{
	
	private static final Logger log = LoggerFactory.getLogger(BaseClientPacket.class);
	private final ChannelBuffer buf;
	
	/**
	 * @param channelBuffer
	 * @param opCode
	 */
	public BaseClientPacket(ChannelBuffer channelBuffer, int opCode)
	{
		super(opCode);
		buf = channelBuffer;
	}
	
	public int getRemainingBytes()
	{
		return buf.readableBytes();
	}
	
	/**
	 * Perform packet read
	 * @return boolean
	 */
	public boolean read()
	{
		try
		{
			readImpl();
			if (getRemainingBytes() > 0)
			{
				log.debug("Packet " + this + " not fully readed!");
			}
			return true;
		}
		catch (final Exception ex)
		{
			log.error("Reading failed for packet " + this, ex);
			return false;
		}
		
	}
	
	/**
	 * Perform packet action
	 */
	public void run()
	{
		try
		{
			runImpl();
		}
		catch (final Exception ex)
		{
			log.error("Running failed for packet " + this, ex);
		}
	}
	
	protected abstract void readImpl();
	
	protected abstract void runImpl();
	
	/**
	 * Read int from this packet buffer.
	 * @return int
	 */
	protected final int readD()
	{
		try
		{
			return buf.readInt();
		}
		catch (final Exception e)
		{
			log.error("Missing D for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read byte from this packet buffer.
	 * @return int
	 */
	protected final int readC()
	{
		try
		{
			return buf.readByte() & 0xFF;
		}
		catch (final Exception e)
		{
			log.error("Missing C for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read short from this packet buffer.
	 * @return int
	 */
	protected final int readH()
	{
		try
		{
			return buf.readShort() & 0xFFFF;
		}
		catch (final Exception e)
		{
			log.error("Missing H for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read double from this packet buffer.
	 * @return double
	 */
	protected final double readDF()
	{
		try
		{
			return buf.readDouble();
		}
		catch (final Exception e)
		{
			log.error("Missing DF for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read double from this packet buffer.
	 * @return double
	 */
	protected final float readF()
	{
		try
		{
			return buf.readFloat();
		}
		catch (final Exception e)
		{
			log.error("Missing F for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read long from this packet buffer.
	 * @return long
	 */
	protected final long readQ()
	{
		try
		{
			return buf.readLong();
		}
		catch (final Exception e)
		{
			log.error("Missing Q for: " + this);
		}
		return 0;
	}
	
	/**
	 * Read String from this packet buffer.
	 * @return String
	 */
	protected final String readS()
	{
		final StringBuffer sb = new StringBuffer();
		char ch;
		try
		{
			while ((ch = buf.readChar()) != 0)
			{
				sb.append(ch);
			}
		}
		catch (final Exception e)
		{
			log.error("Missing S for: " + this);
		}
		return sb.toString();
		
	}
	
	/**
	 * Read n bytes from this packet buffer, n = length.
	 * @param length
	 * @return byte[]
	 */
	protected final byte[] readB(int length)
	{
		final byte[] result = new byte[length];
		try
		{
			buf.readBytes(result);
		}
		catch (final Exception e)
		{
			log.error("Missing byte[] for: " + this);
		}
		return result;
	}
}
