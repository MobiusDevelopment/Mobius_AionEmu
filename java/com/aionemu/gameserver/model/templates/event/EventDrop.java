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
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventDrop")
public class EventDrop
{
	@XmlAttribute(name = "item_id", required = true)
	protected int itemId;
	@XmlAttribute(name = "count", required = true)
	protected long count;
	@XmlAttribute(name = "chance", required = true)
	protected float chance;
	@XmlAttribute(name = "minDiff")
	protected int minDiff;
	@XmlAttribute(name = "maxDiff")
	protected int maxDiff;
	@XmlTransient
	private ItemTemplate template;
	
	public int getItemId()
	{
		return itemId;
	}
	
	public long getCount()
	{
		return count;
	}
	
	public float getChance()
	{
		return chance;
	}
	
	public int getMinDiff()
	{
		return minDiff;
	}
	
	public int getMaxDiff()
	{
		return maxDiff;
	}
	
	public ItemTemplate getItemTemplate()
	{
		if (template == null)
		{
			template = DataManager.ITEM_DATA.getItemTemplate(itemId);
		}
		return template;
	}
}
