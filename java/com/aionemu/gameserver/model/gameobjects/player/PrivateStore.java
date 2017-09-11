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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.LinkedHashMap;

import com.aionemu.gameserver.model.trade.TradePSItem;

/**
 * @author Xav Modified by Simple
 */
public class PrivateStore
{
	
	private final Player owner;
	private LinkedHashMap<Integer, TradePSItem> items;
	private String storeMessage;
	
	/**
	 * This method binds a player to the store and creates a list of items
	 * @param owner
	 */
	public PrivateStore(Player owner)
	{
		this.owner = owner;
		items = new LinkedHashMap<>();
	}
	
	/**
	 * This method will return the owner of the store
	 * @return Player
	 */
	public Player getOwner()
	{
		return owner;
	}
	
	/**
	 * This method will return the items being sold
	 * @return LinkedHashMap<Integer, TradePSItem>
	 */
	public LinkedHashMap<Integer, TradePSItem> getSoldItems()
	{
		return items;
	}
	
	/**
	 * This method will add an item to the list and price
	 * @param itemObjId
	 * @param tradeItem
	 * @param tradeList
	 * @param price
	 */
	public void addItemToSell(int itemObjId, TradePSItem tradeItem)
	{
		items.put(itemObjId, tradeItem);
	}
	
	/**
	 * This method will remove an item from the list
	 * @param itemObjId
	 * @param item
	 */
	public void removeItem(int itemObjId)
	{
		if (items.containsKey(itemObjId))
		{
			final LinkedHashMap<Integer, TradePSItem> newItems = new LinkedHashMap<>();
			for (int itemObjIds : items.keySet())
			{
				if (itemObjId != itemObjIds)
				{
					newItems.put(itemObjIds, items.get(itemObjIds));
				}
			}
			items = newItems;
		}
	}
	
	/**
	 * @param itemObjId
	 * @param itemId return tradeItem
	 * @return
	 */
	public TradePSItem getTradeItemByObjId(int itemObjId)
	{
		return items.get(itemObjId);
	}
	
	/**
	 * @param storeMessage the storeMessage to set
	 */
	public void setStoreMessage(String storeMessage)
	{
		this.storeMessage = storeMessage;
	}
	
	/**
	 * @return the storeMessage
	 */
	public String getStoreMessage()
	{
		return storeMessage;
	}
}
