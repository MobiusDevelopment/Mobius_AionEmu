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
package com.aionemu.loginserver.taskmanager.handler.implementations;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.loginserver.Shutdown;
import com.aionemu.loginserver.taskmanager.handler.TaskFromDBHandler;

/**
 * @author Divinity, nrg
 */
public class ShutdownHandler extends TaskFromDBHandler
{
	private static final Logger log = LoggerFactory.getLogger(ShutdownHandler.class);
	
	@Override
	public boolean isValid()
	{
		return true;
		
	}
	
	@Override
	public void trigger()
	{
		log.info("Task[" + taskId + "] launched : shutting down the server !");
		
		final Shutdown shutdown = Shutdown.getInstance();
		shutdown.setRestartOnly(false);
		shutdown.start();
	}
}
