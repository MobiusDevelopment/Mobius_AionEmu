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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.decomposable.DecomposableSelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItem;
import com.aionemu.gameserver.model.templates.decomposable.SelectItems;

@XmlRootElement(name = "decomposable_select_items")
@XmlAccessorType(XmlAccessType.FIELD)
public class DecomposableSelectItemsData
{
	@XmlElement(name = "decomposable_select_item", required = true)
	protected List<DecomposableSelectItem> selectItems;
	
	@SuppressWarnings(
	{
		"unchecked",
		"rawtypes"
	})
	private final HashMap<Integer, HashMap<PlayerClass, SelectItems>> selectItemData = new HashMap();
	
	@SuppressWarnings(
	{
		"unchecked",
		"rawtypes"
	})
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (Object element : selectItems)
		{
			DecomposableSelectItem item;
			item = (DecomposableSelectItem) element;
			if (item.getItems() != null)
			{
				if (!selectItemData.containsKey(Integer.valueOf(item.getItemId())))
				{
					selectItemData.put(Integer.valueOf(item.getItemId()), new HashMap());
				}
				for (SelectItems its : item.getItems())
				{
					((HashMap) selectItemData.get(Integer.valueOf(item.getItemId()))).put(its.getPlayerClass(), its);
				}
			}
		}
		selectItems.clear();
		selectItems = null;
	}
	
	@SuppressWarnings("rawtypes")
	public SelectItems getSelectItem(PlayerClass playerClass, Race race, int itemid)
	{
		if (selectItemData.containsKey(Integer.valueOf(itemid)))
		{
			if (((HashMap) selectItemData.get(Integer.valueOf(itemid))).containsKey(playerClass))
			{
				final SelectItems filtered = new SelectItems();
				for (SelectItem si : selectItemData.get(itemid).get(playerClass).getItems())
				{
					if ((si.getRace() == Race.PC_ALL) || (si.getRace() == race))
					{
						filtered.addItem(si);
					}
				}
				return filtered;
			}
			if (((HashMap) selectItemData.get(Integer.valueOf(itemid))).containsKey(PlayerClass.ALL))
			{
				final SelectItems filtered = new SelectItems();
				for (SelectItem si : selectItemData.get(itemid).get(PlayerClass.ALL).getItems())
				{
					if ((si.getRace() == Race.PC_ALL) || (si.getRace() == race))
					{
						filtered.addItem(si);
					}
				}
				return filtered;
			}
			return null;
		}
		return null;
	}
	
	public int size()
	{
		return selectItemData.size();
	}
}