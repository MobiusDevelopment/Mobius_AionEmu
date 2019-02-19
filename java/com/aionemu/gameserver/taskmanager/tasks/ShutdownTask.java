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
package com.aionemu.gameserver.taskmanager.tasks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.ShutdownHook;
import com.aionemu.gameserver.ShutdownHook.ShutdownMode;
import com.aionemu.gameserver.model.templates.tasks.TaskFromDBHandler;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Divinity
 */
public class ShutdownTask extends TaskFromDBHandler
{
	
	private static final Logger log = LoggerFactory.getLogger(ShutdownTask.class);
	
	private int countDown;
	int announceInterval;
	private int warnCountDown;
	
	@Override
	public String getTaskName()
	{
		return "shutdown";
	}
	
	@Override
	public boolean isValid()
	{
		return params.length == 3;
		
	}
	
	@Override
	public void run()
	{
		log.info("Task[" + id + "] launched : shuting down the server !");
		setLastActivation();
		
		countDown = Integer.parseInt(params[0]);
		announceInterval = Integer.parseInt(params[1]);
		warnCountDown = Integer.parseInt(params[2]);
		
		World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendBrightYellowMessageOnCenter(player, "Automatic Task: The server will shutdown in " + warnCountDown + " seconds ! Please find a peace place and disconnect your character."));
		
		ThreadPoolManager.getInstance().schedule(() -> ShutdownHook.getInstance().doShutdown(countDown, announceInterval, ShutdownMode.SHUTDOWN), warnCountDown * 1000);
	}
}
