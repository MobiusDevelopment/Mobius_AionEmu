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

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.ItemMask;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.model.templates.restriction.ItemCleanupTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Luno
 */
@XmlRootElement(name = "item_templates")
@XmlAccessorType(XmlAccessType.FIELD)
public class ItemData extends ReloadableData
{
	
	@XmlElement(name = "item_template")
	private List<ItemTemplate> its;
	
	@XmlTransient
	private TIntObjectHashMap<ItemTemplate> items;
	
	@XmlTransient
	private final TIntObjectHashMap<ItemTemplate> petEggs = new TIntObjectHashMap<>();
	
	@XmlTransient
	Map<Integer, List<ItemTemplate>> manastones = new HashMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		items = new TIntObjectHashMap<>();
		for (ItemTemplate it : its)
		{
			items.put(it.getTemplateId(), it);
			// if (it.getCategory().equals(ItemCategory.MANASTONE)) {
			// int level = it.getLevel();
			// if (!manastones.containsKey(level)) {
			// manastones.put(level, new ArrayList<ItemTemplate>());
			// }
			// manastones.get(level).add(it);
			// }
			if (it.getActions() == null)
			{
				continue;
			}
		}
		its = null;
	}
	
	public void cleanup()
	{
		for (ItemCleanupTemplate ict : DataManager.ITEM_CLEAN_UP.getList())
		{
			final ItemTemplate template = items.get(ict.getId());
			applyCleanup(template, ict.resultTrade(), ItemMask.TRADEABLE);
			applyCleanup(template, ict.resultSell(), ItemMask.SELLABLE);
			applyCleanup(template, ict.resultWH(), ItemMask.STORABLE_IN_WH);
			applyCleanup(template, ict.resultAccountWH(), ItemMask.STORABLE_IN_AWH);
			applyCleanup(template, ict.resultLegionWH(), ItemMask.STORABLE_IN_LWH);
		}
	}
	
	private void applyCleanup(ItemTemplate item, byte result, int mask)
	{
		if (result != -1)
		{
			switch (result)
			{
				case 1:
				{
					item.modifyMask(true, mask);
					break;
				}
				case 0:
				{
					item.modifyMask(false, mask);
					break;
				}
			}
		}
	}
	
	public ItemTemplate getItemTemplate(int itemId)
	{
		return items.get(itemId);
	}
	
	/**
	 * @return items.size()
	 */
	public int size()
	{
		return items.size();
	}
	
	public Map<Integer, List<ItemTemplate>> getManastones()
	{
		return manastones;
	}
	
	public ItemTemplate getPetEggTemplate(int petId)
	{
		return petEggs.get(petId);
	}
	
	@Override
	public void reload(Player admin)
	{
		try
		{
			final JAXBContext jc = JAXBContext.newInstance(StaticData.class);
			final Unmarshaller un = jc.createUnmarshaller();
			un.setSchema(getSchema("./data/static_data/static_data.xsd"));
			final List<ItemTemplate> newTemplates = new ArrayList<>();
			final ItemData data = (ItemData) un.unmarshal(new File("./data/static_data/items/item_templates.xml"));
			if ((data != null) && (data.getData() != null))
			{
				newTemplates.addAll(data.getData());
			}
			DataManager.ITEM_DATA.setData(newTemplates);
		}
		catch (Exception e)
		{
			PacketSendUtility.sendMessage(admin, "Item templates reload failed!");
			log.error("Item templates reload failed!", e);
		}
		finally
		{
			PacketSendUtility.sendMessage(admin, "Item templates reload Success! Total loaded: " + DataManager.ITEM_DATA.size());
		}
	}
	
	@Override
	protected List<ItemTemplate> getData()
	{
		return its;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void setData(List<?> data)
	{
		its = (List<ItemTemplate>) data;
		afterUnmarshal(null, null);
	}
	
	public TIntObjectHashMap<ItemTemplate> getItemData()
	{
		return items;
	}
}
