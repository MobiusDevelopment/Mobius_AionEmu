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

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob;

/**
 * @author Avol, xTz
 */
public class SM_VIEW_PLAYER_DETAILS extends AionServerPacket
{
	
	private final List<Item> items;
	private final int itemSize;
	private final int targetObjId;
	private final Player player;
	
	public SM_VIEW_PLAYER_DETAILS(List<Item> items, Player player)
	{
		this.player = player;
		targetObjId = player.getObjectId();
		this.items = items;
		itemSize = items.size();
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		
		writeD(targetObjId);
		writeC(11);
		writeH(itemSize);
		for (final Item item : items)
		{
			writeItemInfo(item);
		}
	}
	
	private void writeItemInfo(Item item)
	{
		final ItemTemplate template = item.getItemTemplate();
		
		writeD(0);
		writeD(template.getTemplateId());
		writeNameId(template.getNameId());
		
		final ItemInfoBlob itemInfoBlob = ItemInfoBlob.getFullBlob(player, item);
		itemInfoBlob.writeMe(getBuf());
	}
}
