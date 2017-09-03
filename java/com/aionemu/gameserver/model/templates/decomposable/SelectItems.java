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
package com.aionemu.gameserver.model.templates.decomposable;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.PlayerClass;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SelectItems")
public class SelectItems
{
	@XmlAttribute(name = "player_class")
	private final PlayerClass playerClass = PlayerClass.ALL;
	
	@XmlElement(name = "item")
	private List<SelectItem> items;
	
	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
	
	public List<SelectItem> getItems()
	{
		return items;
	}
	
	public void addItem(SelectItem newItem)
	{
		if (items == null)
		{
			items = new ArrayList<>();
		}
		items.add(newItem);
	}
}