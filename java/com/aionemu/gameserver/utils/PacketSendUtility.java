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
package com.aionemu.gameserver.utils;

import com.aionemu.commons.objects.filter.ObjectFilter;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.Legion;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.zone.SiegeZoneInstance;

/**
 * This class contains static methods, which are utility methods, all of them are interacting only with objects passed as parameters.<br>
 * These methods could be placed directly into Player class, but we want to keep Player class as a pure data holder.<br>
 * @author Luno
 */
public class PacketSendUtility
{
	public static void sendMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.GOLDEN_YELLOW));
	}
	
	public static void sendWhiteMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}
	
	public static void sendWhiteMessageOnCenter(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE_CENTER));
	}
	
	public static void sendYellowMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW));
	}
	
	public static void sendYellowMessageOnCenter(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.YELLOW_CENTER));
	}
	
	public static void sendBrightYellowMessage(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW));
	}
	
	public static void sendBrightYellowMessageOnCenter(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.BRIGHT_YELLOW_CENTER));
	}
	
	public static void sendSys1Message(Player player, String sender, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.GROUP_LEADER));
	}
	
	public static void sendSys2Message(Player player, String sender, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.WHITE));
	}
	
	public static void sendSys3Message(Player player, String sender, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, sender, msg, ChatType.COMMAND));
	}
	
	public static void sendWarnMessageOnCenter(Player player, String msg)
	{
		sendPacket(player, new SM_MESSAGE(0, null, msg, ChatType.LEAGUE_ALERT));
	}
	
	public static void sendPacket(Player player, AionServerPacket packet)
	{
		if (player.getClientConnection() != null)
		{
			player.getClientConnection().sendPacket(packet);
		}
	}
	
	/**
	 * Player Send Packet
	 * @param player
	 * @param packet
	 * @param time
	 */
	public static void playerSendPacketTime(Player player, AionServerPacket packet, int time)
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			if (player.getClientConnection() != null)
			{
				player.getClientConnection().sendPacket(packet);
			}
		}, time);
	}
	
	/**
	 * Npc Send Packet
	 * @param npc
	 * @param packet
	 * @param time
	 */
	public static void npcSendPacketTime(Npc npc, AionServerPacket packet, int time)
	{
		ThreadPoolManager.getInstance().schedule(() -> npc.getKnownList().doOnAllPlayers(player ->
		{
			if (player.isOnline())
			{
				sendPacket(player, packet);
			}
		}), time);
	}
	
	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf)
	{
		if (toSelf)
		{
			sendPacket(player, packet);
		}
		broadcastPacket(player, packet);
	}
	
	public static void broadcastPacketAndReceive(VisibleObject visibleObject, AionServerPacket packet)
	{
		if (visibleObject instanceof Player)
		{
			sendPacket((Player) visibleObject, packet);
		}
		broadcastPacket(visibleObject, packet);
	}
	
	public static void broadcastPacket(VisibleObject visibleObject, AionServerPacket packet)
	{
		visibleObject.getKnownList().doOnAllPlayers(player ->
		{
			if (player.isOnline())
			{
				sendPacket(player, packet);
			}
		});
	}
	
	public static void broadcastPacket(Player player, AionServerPacket packet, boolean toSelf, ObjectFilter<Player> filter)
	{
		if (toSelf)
		{
			sendPacket(player, packet);
		}
		player.getKnownList().doOnAllPlayers(object ->
		{
			if (filter.acceptObject(object))
			{
				sendPacket(object, packet);
			}
		});
	}
	
	public static void broadcastPacket(VisibleObject visibleObject, AionServerPacket packet, int distance)
	{
		visibleObject.getKnownList().doOnAllPlayers(p ->
		{
			if (MathUtil.isIn3dRange(visibleObject, p, distance))
			{
				sendPacket(p, packet);
			}
		});
	}
	
	public static void broadcastFilteredPacket(AionServerPacket packet, ObjectFilter<Player> filter)
	{
		World.getInstance().doOnAllPlayers(object ->
		{
			if (filter.acceptObject(object))
			{
				sendPacket(object, packet);
			}
		});
	}
	
	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet)
	{
		for (Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			sendPacket(onlineLegionMember, packet);
		}
	}
	
	public static void broadcastPacketToLegion(Legion legion, AionServerPacket packet, int playerObjId)
	{
		for (Player onlineLegionMember : legion.getOnlineLegionMembers())
		{
			if (onlineLegionMember.getObjectId() != playerObjId)
			{
				sendPacket(onlineLegionMember, packet);
			}
		}
	}
	
	public static void broadcastPacketToZone(SiegeZoneInstance zone, AionServerPacket packet)
	{
		zone.doOnAllPlayers(player -> sendPacket(player, packet));
	}
}