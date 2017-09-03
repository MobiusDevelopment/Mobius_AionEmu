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
package com.aionemu.gameserver.model.tasks;

import java.sql.Timestamp;

/**
 * @author Divinity
 */
public class TaskFromDB
{
	
	private final int id;
	private final String name;
	private final String type;
	private final Timestamp lastActivation;
	private final String startTime;
	private final int delay;
	private String params[];
	
	/**
	 * Constructor
	 * @param id : int
	 * @param name : String
	 * @param type : String
	 * @param lastActivation : Timestamp
	 * @param startTime : String
	 * @param delay : int
	 * @param param : String
	 */
	public TaskFromDB(int id, String name, String type, Timestamp lastActivation, String startTime, int delay, String param)
	{
		this.id = id;
		this.name = name;
		this.type = type;
		this.lastActivation = lastActivation;
		this.startTime = startTime;
		this.delay = delay;
		
		if (param != null)
		{
			params = param.split(" ");
		}
		else
		{
			params = new String[0];
		}
	}
	
	/**
	 * Task's id
	 * @return int
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Task's name
	 * @return String
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Task's type : - FIXED_IN_TIME (HH:MM:SS)
	 * @return String
	 */
	public String getType()
	{
		return type;
	}
	
	/**
	 * Task's last activation
	 * @return Timestamp
	 */
	public Timestamp getLastActivation()
	{
		return lastActivation;
	}
	
	/**
	 * Task's starting time (HH:MM:SS format)
	 * @return String
	 */
	public String getStartTime()
	{
		return startTime;
	}
	
	/**
	 * Task's delay
	 * @return int
	 */
	public int getDelay()
	{
		return delay;
	}
	
	/**
	 * Task's param(s)
	 * @return String[]
	 */
	public String[] getParams()
	{
		return params;
	}
}
