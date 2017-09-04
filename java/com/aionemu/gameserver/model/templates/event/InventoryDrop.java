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
package com.aionemu.gameserver.model.templates.event;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlValue;

/**
 * @author Rolandas
 */
@XmlType(name = "InventoryDrop")
@XmlAccessorType(XmlAccessType.FIELD)
public class InventoryDrop
{
	@XmlValue
	private int dropItem;
	
	@XmlAttribute(name = "startlevel", required = false)
	private int startLevel;
	
	@XmlAttribute(name = "interval", required = true)
	private int interval;
	
	/**
	 * @return the dropItem
	 */
	public int getDropItem()
	{
		return dropItem;
	}
	
	/**
	 * @return the startLevel
	 */
	public int getStartLevel()
	{
		return startLevel;
	}
	
	/**
	 * @return the interval in minutes
	 */
	public int getInterval()
	{
		return interval;
	}
}
