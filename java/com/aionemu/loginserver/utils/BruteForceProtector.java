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

import com.aionemu.loginserver.configs.Config;

import javolution.util.FastMap;

/**
 * @author Mr. Poke
 */
public class BruteForceProtector
{
	
	private final FastMap<String, FailedLoginInfo> failedConnections = new FastMap<>();
	
	class FailedLoginInfo
	{
		
		private int count;
		private final long time;
		
		/**
		 * @param count
		 * @param time
		 */
		public FailedLoginInfo(int count, long time)
		{
			super();
			this.count = count;
			this.time = time;
		}
		
		public void increseCount()
		{
			count++;
		}
		
		/**
		 * @return the count
		 */
		public int getCount()
		{
			return count;
		}
		
		/**
		 * @return the time
		 */
		public long getTime()
		{
			return time;
		}
	}
	
	public static BruteForceProtector getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public boolean addFailedConnect(String ip)
	{
		final FailedLoginInfo failed = failedConnections.get(ip);
		if ((failed == null) || ((System.currentTimeMillis() - failed.getTime()) > (Config.WRONG_LOGIN_BAN_TIME * 1000 * 60)))
		{
			failedConnections.put(ip, new FailedLoginInfo(1, System.currentTimeMillis()));
		}
		else
		{
			if (failed.getCount() >= Config.LOGIN_TRY_BEFORE_BAN)
			{
				failedConnections.remove(ip);
				return true;
			}
			else
			{
				failed.increseCount();
			}
		}
		return false;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		
		protected static final BruteForceProtector instance = new BruteForceProtector();
	}
}
