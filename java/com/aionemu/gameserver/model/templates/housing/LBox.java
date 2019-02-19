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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 * @author Rolandas
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class LBox implements Cloneable
{
	@XmlElement(required = true)
	protected int id;
	
	@XmlElement(required = true)
	protected String name;
	
	@XmlElement(required = true)
	protected String desc;
	
	@XmlElement(required = true)
	protected String script;
	
	@XmlElement(required = true)
	protected int icon;
	
	public int getId()
	{
		return id;
	}
	
	public void setId(int position)
	{
		id = 100 + position;
	}
	
	public String getName()
	{
		return name;
	}
	
	public String getDesc()
	{
		return desc;
	}
	
	public String getScript()
	{
		return script;
	}
	
	public int getIcon()
	{
		return icon;
	}
	
	public void setIcon(int id)
	{
		icon = id;
	}
	
	@Override
	public Object clone()
	{
		final LBox result = new LBox();
		result.id = id;
		result.name = name;
		result.desc = desc;
		result.script = script;
		result.icon = icon;
		
		return result;
	}
	
}
