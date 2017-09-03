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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.Collection;
import java.util.Collections;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;

/**
 * @author kosyachok
 */
public class SM_WAREHOUSE_INFO extends AionServerPacket
{
	
	private final int warehouseType;
	private Collection<Item> itemList;
	private final boolean firstPacket;
	private final int expandLvl;
	private final Player player;
	
	public SM_WAREHOUSE_INFO(Collection<Item> items, int warehouseType, int expandLvl, boolean firstPacket, Player player)
	{
		this.warehouseType = warehouseType;
		this.expandLvl = expandLvl;
		this.firstPacket = firstPacket;
		if (items == null)
		{
			itemList = Collections.emptyList();
		}
		else
		{
			itemList = items;
		}
		this.player = player;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(warehouseType);
		writeC(firstPacket ? 1 : 0);
		writeC(expandLvl); // warehouse expand (0 - 9)
		writeH(0);
		writeH(itemList.size());
		for (final Item item : itemList)
		{
			writeItemInfo(item);
		}
	}
	
	private void writeItemInfo(Item item)
	{
		final ItemTemplate itemTemplate = item.getItemTemplate();
		
		writeD(item.getObjectId());
		writeD(itemTemplate.getTemplateId());
		writeC(0); // some item info (4 - weapon, 7 - armor, 8 - rings, 17 - bottles)
		writeNameId(itemTemplate.getNameId());
		
		final ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
		itemInfoBlob.writeMe(getBuf());
		
		writeH((int) (item.getEquipmentSlot() & 0xFFFF)); // FF FF equipment
	}
}
