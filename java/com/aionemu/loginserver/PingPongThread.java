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
package com.aionemu.loginserver;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.configs.SvStatsConfig;
import com.aionemu.loginserver.dao.SvStatsDAO;
import com.aionemu.loginserver.network.gs.GsConnection;
import com.aionemu.loginserver.network.gs.serverpackets.SM_PING;

/**
 * @author KID
 */
public class PingPongThread implements Runnable
{
	
	private final Logger log = LoggerFactory.getLogger(PingPongThread.class);
	private final GsConnection connection;
	public volatile boolean uptime = true;
	private final SM_PING ping;
	private byte requests = 0;
	private int serverPID = -1;
	private final boolean killProcess = false;
	
	public PingPongThread(GsConnection connection)
	{
		uptime = true;
		this.connection = connection;
		ping = new SM_PING();
	}
	
	@Override
	public void run()
	{
		log.info("PingPong for gameserver #" + connection.getGameServerInfo().getId() + " has started.");
		while (uptime)
		{
			try
			{
				Thread.sleep(Config.PINGPONG_DELAY);
			}
			catch (final InterruptedException e)
			{
				e.printStackTrace();
			}
			
			if (!uptime || validateResponse())
			{
				return;
			}
			
			try
			{
				connection.sendPacket(ping);
				requests++;
				if (SvStatsConfig.SVSTATS_ENABLE)
				{
					final int currentID = connection.getGameServerInfo().getId();
					final int currentPlayer = connection.getGameServerInfo().getCurrentPlayers();
					final int currentMax = connection.getGameServerInfo().getMaxPlayers();
					DAOManager.getDAO(SvStatsDAO.class).update_SvStats_Online(currentID, 1, currentPlayer, currentMax);
				}
			}
			catch (final Exception ex)
			{
				log.error("PingThread#" + connection.getGameServerInfo().getId(), ex);
			}
		}
	}
	
	public void onResponse(int pid)
	{
		requests--;
		serverPID = pid;
	}
	
	public boolean validateResponse()
	{
		if (requests >= 2)
		{
			uptime = false;
			log.info("Gameserver #" + connection.getGameServerInfo().getId() + " [PID=" + serverPID + "] died, closing.");
			
			if (SvStatsConfig.SVSTATS_ENABLE)
			{
				final int currentID = connection.getGameServerInfo().getId();
				DAOManager.getDAO(SvStatsDAO.class).update_SvStats_Offline(currentID, 0, 0);
			}
			connection.close(false);
			if (killProcess && (serverPID != -1))
			{
				if (System.getProperty("os.name").toLowerCase().indexOf("windows") != -1)
				{
					try
					{
						Runtime.getRuntime().exec("taskkill /pid " + serverPID + " /f");
					}
					catch (final IOException e)
					{
						e.printStackTrace();
					}
				}
			}
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void closeMe()
	{
		uptime = false;
		
		if (SvStatsConfig.SVSTATS_ENABLE)
		{
			final int currentID = connection.getGameServerInfo().getId();
			DAOManager.getDAO(SvStatsDAO.class).update_SvStats_Offline(currentID, 0, 0);
		}
	}
}