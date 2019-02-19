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
package com.aionemu.loginserver.utils;

import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.controller.BannedIpController;
import com.aionemu.loginserver.network.aion.clientpackets.CM_LOGIN;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 */
public class FloodProtector
{
	private static final Logger log = LoggerFactory.getLogger(CM_LOGIN.class);
	private final FastMap<String, Long> flood = new FastMap<>();
	private final FastMap<String, Long> ban = new FastMap<>();
	
	public static FloodProtector getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@Deprecated
	public boolean addIp_nn(String ip)
	{
		final Long time = flood.get(ip);
		if ((time == null) || ((System.currentTimeMillis() - time) > Config.FAST_RECONNECTION_TIME))
		{
			flood.put(ip, System.currentTimeMillis());
			return false;
		}
		final Timestamp newTime = new Timestamp(System.currentTimeMillis() + (Config.WRONG_LOGIN_BAN_TIME * 60000));
		if (!BannedIpController.isBanned(ip))
		{
			log.info("[AUDIT]FloodProtector:" + ip + " IP banned for " + Config.WRONG_LOGIN_BAN_TIME + " min");
			return BannedIpController.banIp(ip, newTime);
		}
		// in this case this ip is already banned
		
		return true;
	}
	
	public boolean tooFast(String ip)
	{
		final String[] exclIps = Config.EXCLUDED_IP.split(",");
		for (String exclIp : exclIps)
		{
			if (ip.equals(exclIp))
			{
				return false;
			}
		}
		final Long banned = ban.get(ip);
		if (banned != null)
		{
			if (System.currentTimeMillis() < banned)
			{
				return true;
			}
			ban.remove(ip);
			return false;
		}
		final Long time = flood.get(ip);
		if (time == null)
		{
			flood.put(ip, System.currentTimeMillis() + (Config.FAST_RECONNECTION_TIME * 1000));
			return false;
		}
		if (time > System.currentTimeMillis())
		{
			log.info("[AUDIT]FloodProtector:" + ip + " IP too fast connection attemp. blocked for " + Config.WRONG_LOGIN_BAN_TIME + " min");
			ban.put(ip, System.currentTimeMillis() + (Config.WRONG_LOGIN_BAN_TIME * 60000));
			return true;
		}
		return false;
	}
	
	private static class SingletonHolder
	{
		protected static final FloodProtector instance = new FloodProtector();
	}
}
