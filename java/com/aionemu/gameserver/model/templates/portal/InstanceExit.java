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
package com.aionemu.gameserver.model.templates.portal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Race;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceExit")
public class InstanceExit
{
	@XmlAttribute(name = "instance_id")
	protected int instanceId;
	
	@XmlAttribute(name = "exit_world")
	protected int exitWorld;
	
	@XmlAttribute(name = "race")
	protected Race race = Race.PC_ALL;
	
	@XmlAttribute(name = "x")
	protected float x;
	
	@XmlAttribute(name = "y")
	protected float y;
	
	@XmlAttribute(name = "z")
	protected float z;
	
	@XmlAttribute(name = "h")
	protected byte h;
	
	public Integer getInstanceId()
	{
		return instanceId;
	}
	
	public void setInstanceId(int value)
	{
		instanceId = value;
	}
	
	public int getExitWorld()
	{
		return exitWorld;
	}
	
	public void setExitWorld(int value)
	{
		exitWorld = value;
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public void setRace(Race value)
	{
		race = value;
	}
	
	public float getX()
	{
		return x;
	}
	
	public void setX(float value)
	{
		x = value;
	}
	
	public float getY()
	{
		return y;
	}
	
	public void setY(float value)
	{
		y = value;
	}
	
	public float getZ()
	{
		return z;
	}
	
	public void setZ(float value)
	{
		z = value;
	}
	
	public byte getH()
	{
		return h;
	}
	
	public void setH(byte value)
	{
		h = value;
	}
}
