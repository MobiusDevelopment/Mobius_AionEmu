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

import java.util.Set;

import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemCategory;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

public class SM_LOOT_ITEMLIST extends AionServerPacket
{
	private int targetObjectId;
	private FastList<DropItem> dropItems;
	
	public SM_LOOT_ITEMLIST(int targetObjectId, Set<DropItem> setItems, Player player)
	{
		this.targetObjectId = targetObjectId;
		dropItems = new FastList<>();
		if (setItems == null)
		{
			LoggerFactory.getLogger(SM_LOOT_ITEMLIST.class).warn("null Set<DropItem>, skip");
			return;
		}
		for (final DropItem item : setItems)
		{
			if ((item.getPlayerObjId() == 0) || (player.getObjectId() == item.getPlayerObjId()))
			{
				dropItems.add(item);
			}
		}
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(targetObjectId);
		writeC(dropItems.size());
		for (final DropItem dropItem : dropItems)
		{
			final Drop drop = dropItem.getDropTemplate();
			writeC(dropItem.getIndex());
			writeD(drop.getItemId());
			writeD((int) dropItem.getCount());
			writeC(dropItem.getOptionalSocket());
			writeC(0);
			writeC(0);
			final ItemTemplate template = drop.getItemTemplate();
			writeC(!template.getCategory().equals(ItemCategory.QUEST) && !template.isTradeable() ? 1 : 0);
		}
		FastList.recycle(dropItems);
	}
}