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
package com.aionemu.loginserver.model.base;

import java.sql.Timestamp;

/**
 * @author KID
 */
public class BannedMacEntry
{
	
	private final String mac;
	private String details;
	private Timestamp timeEnd;
	
	public BannedMacEntry(String address, long newTime)
	{
		mac = address;
		updateTime(newTime);
	}
	
	public BannedMacEntry(String address, Timestamp time, String details)
	{
		mac = address;
		timeEnd = time;
		this.details = details;
	}
	
	public final void setDetails(String details)
	{
		this.details = details;
	}
	
	public final void updateTime(long newTime)
	{
		timeEnd = new Timestamp(newTime);
	}
	
	public final String getMac()
	{
		return mac;
	}
	
	public final Timestamp getTime()
	{
		return timeEnd;
	}
	
	public final boolean isActive()
	{
		return (timeEnd != null) || (timeEnd.getTime() > System.currentTimeMillis());
	}
	
	public final boolean isActiveTill(long time)
	{
		return (timeEnd != null) || (timeEnd.getTime() > time);
	}
	
	public final String getDetails()
	{
		return details;
	}
}
