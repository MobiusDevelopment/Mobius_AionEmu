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
package com.aionemu.gameserver.model.trade;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.Acquisition;
import com.aionemu.gameserver.model.templates.item.AcquisitionType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.trade.PricesService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author ATracer modified by Wakizashi
 */
public class TradeList
{
	
	private int sellerObjId;
	
	private final List<TradeItem> tradeItems = new ArrayList<>();
	
	private long requiredKinah;
	
	private int requiredAp;
	
	private final Map<Integer, Long> requiredItems = new HashMap<>();
	
	public TradeList()
	{
		
	}
	
	public TradeList(int sellerObjId)
	{
		this.sellerObjId = sellerObjId;
	}
	
	/**
	 * @param itemId
	 * @param count
	 */
	public void addBuyItem(int itemId, long count)
	{
		
		final ItemTemplate itemTemplate = DataManager.ITEM_DATA.getItemTemplate(itemId);
		if (itemTemplate != null)
		{
			final TradeItem tradeItem = new TradeItem(itemId, count);
			tradeItem.setItemTemplate(itemTemplate);
			tradeItems.add(tradeItem);
		}
	}
	
	/**
	 * @param itemId
	 * @param count
	 */
	public void addPSItem(int itemId, long count)
	{
		final TradeItem tradeItem = new TradeItem(itemId, count);
		tradeItems.add(tradeItem);
	}
	
	/**
	 * @param itemObjId
	 * @param count
	 */
	public void addSellItem(int itemObjId, long count)
	{
		final TradeItem tradeItem = new TradeItem(itemObjId, count);
		tradeItems.add(tradeItem);
	}
	
	/**
	 * @param player
	 * @param modifier
	 * @return price TradeList sum price
	 */
	public boolean calculateBuyListPrice(Player player, int modifier)
	{
		final long availableKinah = player.getInventory().getKinah();
		requiredKinah = 0;
		
		for (TradeItem tradeItem : tradeItems)
		{
			requiredKinah += (PricesService.getKinahForBuy(tradeItem.getItemTemplate().getPrice(), player.getRace()) * tradeItem.getCount() * modifier) / 100;
		}
		
		return availableKinah >= requiredKinah;
	}
	
	/**
	 * @param player
	 * @return true or false
	 */
	public boolean calculateAbyssBuyListPrice(Player player)
	{
		final int ap = player.getAbyssRank().getAp();
		
		requiredAp = 0;
		requiredItems.clear();
		
		for (TradeItem tradeItem : tradeItems)
		{
			final Acquisition aquisition = tradeItem.getItemTemplate().getAcquisition();
			if ((aquisition == null) || ((aquisition.getType() != AcquisitionType.ABYSS) && (aquisition.getType() != AcquisitionType.AP)))
			{
				continue;
			}
			
			requiredAp += aquisition.getRequiredAp() * tradeItem.getCount();
			
			final int abysItemId = aquisition.getItemId();
			if (abysItemId == 0)
			{
				continue;
			}
			
			long alreadyAddedCount = 0;
			if (requiredItems.containsKey(abysItemId))
			{
				alreadyAddedCount = requiredItems.get(abysItemId);
			}
			if (alreadyAddedCount == 0)
			{
				requiredItems.put(abysItemId, (long) aquisition.getItemCount());
			}
			else
			{
				requiredItems.put(abysItemId, alreadyAddedCount + (aquisition.getItemCount() * tradeItem.getCount()));
			}
		}
		
		if (ap < requiredAp)
		{
			// You do not have enough Abyss Points.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300927));
			return false;
		}
		
		for (Integer itemId : requiredItems.keySet())
		{
			final long count = player.getInventory().getItemCountByItemId(itemId);
			if ((requiredItems.get(itemId) < 1) || (count < requiredItems.get(itemId)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @param player
	 * @return true or false
	 */
	public boolean calculateRewardBuyListPrice(Player player)
	{
		requiredItems.clear();
		
		for (TradeItem tradeItem : tradeItems)
		{
			final Acquisition aquisition = tradeItem.getItemTemplate().getAcquisition();
			if ((aquisition == null) || ((aquisition.getType() != AcquisitionType.REWARD) && (aquisition.getType() != AcquisitionType.COUPON)))
			{
				continue;
			}
			
			final int itemId = aquisition.getItemId();
			long alreadyAddedCount = 0;
			if (requiredItems.containsKey(itemId))
			{
				alreadyAddedCount = requiredItems.get(itemId);
			}
			if (alreadyAddedCount == 0)
			{
				requiredItems.put(itemId, aquisition.getItemCount() * tradeItem.getCount());
			}
			else
			{
				requiredItems.put(itemId, alreadyAddedCount + (aquisition.getItemCount() * tradeItem.getCount()));
			}
		}
		
		for (Integer itemId : requiredItems.keySet())
		{
			final long count = player.getInventory().getItemCountByItemId(itemId);
			if ((requiredItems.get(itemId) < 1) || (count < requiredItems.get(itemId)))
			{
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * @return the tradeItems
	 */
	public List<TradeItem> getTradeItems()
	{
		return tradeItems;
	}
	
	public int size()
	{
		return tradeItems.size();
	}
	
	/**
	 * @return the npcId
	 */
	public int getSellerObjId()
	{
		return sellerObjId;
	}
	
	/**
	 * @return the requiredAp
	 */
	public int getRequiredAp()
	{
		return requiredAp;
	}
	
	/**
	 * @return the requiredKinah
	 */
	public long getRequiredKinah()
	{
		return requiredKinah;
	}
	
	/**
	 * @return the requiredItems
	 */
	public Map<Integer, Long> getRequiredItems()
	{
		return requiredItems;
	}
}
