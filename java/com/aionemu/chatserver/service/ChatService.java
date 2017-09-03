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
package com.aionemu.chatserver.service;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.model.ChannelType;
import com.aionemu.chatserver.model.ChatClient;
import com.aionemu.chatserver.model.channel.Channel;
import com.aionemu.chatserver.model.channel.ChatChannels;
import com.aionemu.chatserver.network.aion.serverpackets.SM_PLAYER_AUTH_RESPONSE;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler;
import com.aionemu.chatserver.network.netty.handler.ClientChannelHandler.State;
import com.aionemu.commons.utils.internal.chmv8.PlatformDependent;

/**
 * @author ATracer
 */
public class ChatService
{
	
	private static ChatService instance = new ChatService();
	
	public static ChatService getInstance()
	{
		return instance;
	}
	
	private static final Logger log = LoggerFactory.getLogger(ChatService.class);
	private final Map<Integer, ChatClient> players = PlatformDependent.newConcurrentHashMap();
	private final BroadcastService broadcastService;
	
	public ChatService()
	{
		broadcastService = BroadcastService.getInstance();
	}
	
	/**
	 * Player registered from server side
	 * @param playerId
	 * @param nick
	 * @param token
	 * @param identifier
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public ChatClient registerPlayer(int playerId, String playerLogin, String nick) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
		final MessageDigest md = MessageDigest.getInstance("SHA-256");
		md.reset();
		md.update(playerLogin.getBytes("UTF-8"), 0, playerLogin.length());
		final byte[] accountToken = md.digest();
		final byte[] token = generateToken(accountToken);
		final ChatClient chatClient = new ChatClient(playerId, token, nick);
		players.put(playerId, chatClient);
		return chatClient;
	}
	
	/**
	 * @param playerId
	 * @return
	 */
	private byte[] generateToken(byte[] accountToken)
	{
		final byte[] dynamicToken = new byte[16];
		new Random().nextBytes(dynamicToken);
		final byte[] token = new byte[48];
		for (int i = 0; i < token.length; i++)
		{
			if (i < 16)
			{
				token[i] = dynamicToken[i];
			}
			else
			{
				token[i] = accountToken[i - 16];
			}
		}
		return token;
	}
	
	/**
	 * Player registered from client request
	 * @param playerId
	 * @param token
	 * @param identifier
	 * @param realAccount
	 * @param realName
	 * @param clientChannelHandler
	 * @throws UnsupportedEncodingException
	 */
	public void registerPlayerConnection(int playerId, byte[] token, byte[] identifier, ClientChannelHandler channelHandler, String realName) throws UnsupportedEncodingException
	{
		final ChatClient chatClient = players.get(playerId);
		if (chatClient != null)
		{
			final byte[] regToken = chatClient.getToken();
			chatClient.same(realName);
			
			if (Arrays.equals(regToken, token))
			{
				final String sreal = chatClient.getRealName() + "@" + new String(identifier);
				chatClient.setIdentifier(sreal.getBytes("utf-16le"));
				chatClient.setChannelHandler(channelHandler);
				channelHandler.sendPacket(new SM_PLAYER_AUTH_RESPONSE());
				channelHandler.setState(State.AUTHED);
				channelHandler.setChatClient(chatClient);
				broadcastService.addClient(chatClient);
			}
		}
	}
	
	/**
	 * @param chatClient
	 * @param channelIndex
	 * @param channelIdentifier
	 * @return
	 */
	public Channel registerPlayerWithChannel(ChatClient chatClient, int channelIndex, byte[] channelIdentifier)
	{
		final Channel channel = ChatChannels.getChannelByIdentifier(channelIdentifier);
		if (channel != null)
		{
			final ChannelType channelType = channel.getChannelType();
			if (channelType == ChannelType.GROUP /* || channelType == ChannelType.JOB */)
			{
				if (chatClient.isInChannel(channel))
				{
					return null;
				}
			}
			chatClient.addChannel(channel);
		}
		return channel;
	}
	
	/**
	 * @param playerId
	 */
	public void playerLogout(int playerId)
	{
		final ChatClient chatClient = players.get(playerId);
		if (chatClient != null)
		{
			players.remove(playerId);
			broadcastService.removeClient(chatClient);
			if (chatClient.getChannelHandler() != null)
			{
				chatClient.getChannelHandler().close();
			}
			else
			{
				log.warn("Received logout event without client authentication for player " + playerId);
			}
		}
	}
	
	/**
	 * @param playerId
	 * @param gagTime
	 */
	public void gagPlayer(int playerId, long gagTime)
	{
		if (players.containsKey(playerId))
		{
			final ChatClient client = players.get(playerId);
			client.setGagTime(gagTime);
		}
	}
}
