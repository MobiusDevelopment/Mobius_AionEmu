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
package com.aionemu.loginserver.network;

import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import com.aionemu.loginserver.configs.Config;
import com.aionemu.loginserver.network.aion.AionConnectionFactoryImpl;
import com.aionemu.loginserver.network.gs.GsConnectionFactoryImpl;

/**
 * @author KID
 */
public class NetConnector
{
	
	/**
	 * NioServer instance that will handle io.
	 */
	private static final NioServer instance;
	
	static
	{
		final ServerCfg aion = new ServerCfg(Config.LOGIN_BIND_ADDRESS, Config.LOGIN_PORT, "Aion Connections", new AionConnectionFactoryImpl());
		
		final ServerCfg gs = new ServerCfg(Config.GAME_BIND_ADDRESS, Config.GAME_PORT, "Gs Connections", new GsConnectionFactoryImpl());
		
		instance = new NioServer(Config.NIO_READ_THREADS, gs, aion);
	}
	
	/**
	 * @return NioServer instance.
	 */
	public static NioServer getInstance()
	{
		return instance;
	}
}
