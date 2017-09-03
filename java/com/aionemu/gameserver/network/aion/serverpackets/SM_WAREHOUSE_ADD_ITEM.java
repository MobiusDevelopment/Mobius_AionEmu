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

import java.util.Collections;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;

/**
 * @author kosyachok
 * @author -Nemesiss-
 */
public class SM_WAREHOUSE_ADD_ITEM extends AionServerPacket
{
	
	private final int warehouseType;
	private final List<Item> items;
	private final Player player;
	private ItemAddType addType;
	
	public SM_WAREHOUSE_ADD_ITEM(Item item, int warehouseType, Player player)
	{
		this.player = player;
		this.warehouseType = warehouseType;
		items = Collections.singletonList(item);
		addType = ItemAddType.ALL_SLOT;
	}
	
	public SM_WAREHOUSE_ADD_ITEM(Item item, int warehouseType, Player player, ItemAddType addType)
	{
		this(item, warehouseType, player);
		this.addType = addType;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(warehouseType);
		writeH(addType.getMask());
		writeH(items.size());
		for (Item item : items)
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
		
		writeH((int) (item.getEquipmentSlot() & 0xFFFF));
	}
}
