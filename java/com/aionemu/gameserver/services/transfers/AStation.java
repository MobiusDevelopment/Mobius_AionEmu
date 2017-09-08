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
package com.aionemu.gameserver.services.transfers;

/**
 * @author Ranastic
 */
public class AStation
{
	private final int serverId;
	private final int iconSet;
	private final int minlevel, maxlevel;
	
	public AStation(int serverId, boolean sendIcon, int minLevel, int maxLevel)
	{
		this.serverId = serverId;
		iconSet = sendIcon ? 257 : 513; // 257 Master Server / 513 Live Server
		minlevel = minLevel;
		maxlevel = maxLevel;
	}
	
	public int getServerId()
	{
		return serverId;
	}
	
	public int getIconSet()
	{
		return iconSet;
	}
	
	public int getMinLevel()
	{
		return minlevel;
	}
	
	public int getMaxLevel()
	{
		return maxlevel;
	}
}