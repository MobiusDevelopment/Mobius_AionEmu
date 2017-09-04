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

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.IPv4;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.world.World;

/**
 * In this packet client is sending Mac Address.
 * @author -Nemesiss-, KID
 */
public class CM_MAC_ADDRESS extends AionClientPacket
{
	private static final Logger log = LoggerFactory.getLogger(CM_MAC_ADDRESS.class);
	
	/**
	 * Mac Address send by client in the same format as: ipconfig /all [ie: xx-xx-xx-xx-xx-xx]
	 */
	private String macAddress;
	private final List<String> IPv4list = new ArrayList<>();
	private final List<String> IPv4listLocal = new ArrayList<>();
	private String hddSerial;
	private String localIp;
	
	/**
	 * Constructs new instance of <tt>CM_MAC_ADDRESS </tt> packet
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_MAC_ADDRESS(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		readC();
		final short counter = (short) readH();
		for (short i = 0; i < counter; i++)
		{
			readD();
		}
		macAddress = readS();
		hddSerial = readS();
		IPv4listLocal.add(IPv4.getIP(readD()));
		localIp = IPv4listLocal.toString(); // local ip address
	}
	
	@Override
	protected void runImpl()
	{
		if (BannedMacManager.getInstance().isBanned(macAddress))
		{
			getConnection().closeNow();
			LoggerFactory.getLogger(CM_MAC_ADDRESS.class).info("[MAC_AUDIT] " + macAddress + " (" + getConnection().getIP() + ") was kicked due to mac ban.");
			log.info("[MAC_AUDIT] " + macAddress + " (" + getConnection().getIP() + ") was kicked due to MAC ban.");
			return;
		}
		
		final String macReg = "^([A-F|0-9]{2}-){5}[A-F|0-9]{2}$";
		final Pattern pattern = Pattern.compile(macReg);
		final Matcher matcher = pattern.matcher(macAddress);
		if (!matcher.matches() || macAddress.isEmpty() || macAddress.contains("00-00-00-00"))
		{
			getConnection().closeNow();
			log.info("No valid Mac Address : " + macAddress);
			return;
		}
		
		if (AdminConfig.NO_OPEN_NEW_WINDOW && !getConnection().getIP().equals("127.0.0.1"))
		{
			for (Player player : World.getInstance().getAllPlayers())
			{
				if (player.getClientConnection().getIP().equals(getConnection().getIP()) && player.getClientConnection().getMacAddress().equals(macAddress) && player.getClientConnection().getHddSerial().equals(hddSerial))
				{
					log.info("Logon attempt with two windows.\nhdd_serial: " + hddSerial + "\nIP: " + getConnection().getIP());
					getConnection().closeNow();
					break;
				}
			}
		}
		
		getConnection().setMacAddress(macAddress);
		getConnection().setHDDSerial(hddSerial);
		getConnection().setIPv4List(IPv4list.toString());
		getConnection().setLocalIP(localIp);
		getConnection().setTracerouteIP(IPv4list.toString());
	}
}
