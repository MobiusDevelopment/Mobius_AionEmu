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
package com.aionemu.gameserver.network.aion.iteminfo;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob is sending info about the item that were fused with current item.
 * @author -Nemesiss-
 * @modified Rolandas
 */
public class CompositeItemBlobEntry extends ItemBlobEntry
{
	CompositeItemBlobEntry()
	{
		super(ItemBlobType.COMPOSITE_ITEM);
	}
	
	@Override
	public void writeThisBlob(ByteBuffer buf)
	{
		final Item item = ownerItem;
		writeD(buf, item.getFusionedItemId());
		writeFusionStones(buf);
		writeH(buf, 0);
	}
	
	private void writeFusionStones(ByteBuffer buf)
	{
		final Item item = ownerItem;
		int count = 0;
		if (item.hasFusionStones())
		{
			final Set<ManaStone> itemStones = item.getFusionStones();
			final ArrayList<ManaStone> basicStones = new ArrayList<>();
			final ArrayList<ManaStone> ancientStones = new ArrayList<>();
			for (ManaStone itemStone : itemStones)
			{
				if (itemStone.isBasic())
				{
					basicStones.add(itemStone);
				}
				else
				{
					ancientStones.add(itemStone);
				}
			}
			if (item.getFusionedItemTemplate().getSpecialSlots() > 0)
			{
				if (ancientStones.size() > 0)
				{
					for (ManaStone ancientStone : ancientStones)
					{
						if (count == 6)
						{
							break;
						}
						writeD(buf, ancientStone.getItemId());
						count++;
					}
				}
				for (int i = count; i < item.getFusionedItemTemplate().getSpecialSlots(); i++)
				{
					writeD(buf, 0);
					count++;
				}
			}
			for (ManaStone basicFusionStone : basicStones)
			{
				if (count == 6)
				{
					break;
				}
				writeD(buf, basicFusionStone.getItemId());
				count++;
			}
			skip(buf, (6 - count) * 4);
		}
		else
		{
			skip(buf, 24);
		}
	}
	
	@Override
	public int getSize()
	{
		return 30;
	}
}