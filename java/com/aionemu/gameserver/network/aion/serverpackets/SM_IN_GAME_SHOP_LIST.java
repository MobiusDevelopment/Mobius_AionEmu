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
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.ingameshop.IGItem;
import com.aionemu.gameserver.model.ingameshop.InGameShopEn;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;

public class SM_IN_GAME_SHOP_LIST extends AionServerPacket
{
	private final Player player;
	private final int nrList;
	private final int salesRanking;
	private final TIntObjectHashMap<FastList<IGItem>> allItems = new TIntObjectHashMap<>();
	
	public SM_IN_GAME_SHOP_LIST(Player player, int nrList, int salesRanking)
	{
		this.player = player;
		this.nrList = nrList;
		this.salesRanking = salesRanking;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final byte category = player.inGameShop.getCategory();
		final byte subCategory = player.inGameShop.getSubCategory();
		if (salesRanking == 1)
		{
			final Collection<IGItem> items = InGameShopEn.getInstance().getItems(category);
			int size = 0;
			int tabSize = 9;
			int f = 0;
			for (final IGItem a : items)
			{
				if ((subCategory == 2) || (a.getSubCategory() == subCategory))
				{
					if (size == tabSize)
					{
						tabSize += 9;
						f++;
					}
					FastList<IGItem> template = allItems.get(f);
					if (template == null)
					{
						template = FastList.newInstance();
						allItems.put(f, template);
					}
					template.add(a);
					size++;
				}
			}
			final List<IGItem> inAllItems = allItems.get(nrList);
			writeD(salesRanking);
			writeD(nrList);
			writeD(size > 0 ? tabSize : 0);
			writeH(inAllItems == null ? 0 : inAllItems.size());
			if (inAllItems != null)
			{
				for (final IGItem item : inAllItems)
				{
					writeD(item.getObjectId());
				}
			}
		}
		else
		{
			final FastList<Integer> salesRankingItems = InGameShopEn.getInstance().getTopSales(subCategory, category);
			writeD(salesRanking);
			writeD(nrList);
			writeD((InGameShopEn.getInstance().getMaxList(subCategory, category) + 1) * 9);
			writeH(salesRankingItems.size());
			for (final int id : salesRankingItems)
			{
				writeD(id);
			}
			FastList.recycle(salesRankingItems);
		}
	}
}