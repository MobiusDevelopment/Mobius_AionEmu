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
package com.aionemu.chatserver.network.aion.clientpackets;

import java.io.UnsupportedEncodingException;

import org.jboss.netty.buffer.ChannelBuffer;

import com.aionemu.chatserver.network.aion.AbstractClientPacket;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.chatserver.service.ChatService;

/**
 * @author ATracer
 */
public class CM_PLAYER_AUTH extends AbstractClientPacket
{
	
	private final ChatService chatService;
	private int playerId;
	private byte[] token;
	private byte[] identifier;
	@SuppressWarnings("unused")
	private byte[] accountName;
	private String realName;
	
	/**
	 * @param channelBuffer
	 * @param gameChannelHandler
	 * @param opCode
	 */
	public CM_PLAYER_AUTH(ChannelBuffer channelBuffer, ClientChannelHandler clientChannelHandler, ChatService chatService)
	{
		super(channelBuffer, clientChannelHandler, 0x05);
		this.chatService = chatService;
	}
	
	@Override
	protected void readImpl()
	{
		readB(29); // AION stuff
		playerId = readD();
		readD(); // 0x00
		readD(); // 0x00
		readD(); // 0x00
		final int length = readH() * 2;
		identifier = readB(length);
		final int accountLenght = readH() * 2;
		accountName = readB(accountLenght);
		final int tokenLength = readH();
		token = readB(tokenLength);
		
		try
		{
			final String realid = new String(identifier, "UTF-16le");
			
			realName = realid.split("@")[0];
			final String after = realid.split("@")[1];
			identifier = after.getBytes("UTF-16le");
		}
		catch (final UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
	
	@Override
	protected void runImpl()
	{
		try
		{
			chatService.registerPlayerConnection(playerId, token, identifier, clientChannelHandler, realName);
		}
		catch (final UnsupportedEncodingException e)
		{
			e.printStackTrace();
		}
	}
}
