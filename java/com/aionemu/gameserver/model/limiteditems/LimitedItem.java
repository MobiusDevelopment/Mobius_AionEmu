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
package com.aionemu.gameserver.model.limiteditems;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author xTz
 */
public class LimitedItem
{
	
	private int itemId;
	private int sellLimit;
	private int buyLimit;
	private int defaultSellLimit;
	private String salesTime;
	
	private final TIntObjectHashMap<Integer> buyCounts = new TIntObjectHashMap<>();
	
	public LimitedItem()
	{
	}
	
	public LimitedItem(int itemId, int sellLimit, int buyLimit, String salesTime)
	{
		this.itemId = itemId;
		this.sellLimit = sellLimit;
		this.buyLimit = buyLimit;
		defaultSellLimit = sellLimit;
		this.salesTime = salesTime;
	}
	
	/**
	 * return itemId.
	 * @return
	 */
	public int getItemId()
	{
		return itemId;
	}
	
	/**
	 * @param playerObjectId
	 * @param count
	 */
	public void setBuyCount(int playerObjectId, int count)
	{
		buyCounts.putIfAbsent(playerObjectId, count);
	}
	
	/**
	 * return playerListByObject.
	 * @return
	 */
	public TIntObjectHashMap<Integer> getBuyCount()
	{
		return buyCounts;
	}
	
	/**
	 * @param itemId
	 */
	public void setItem(int itemId)
	{
		this.itemId = itemId;
	}
	
	/**
	 * return sellLimit.
	 * @return
	 */
	public int getSellLimit()
	{
		return sellLimit;
	}
	
	/**
	 * return buyLimit.
	 * @return
	 */
	public int getBuyLimit()
	{
		return buyLimit;
	}
	
	public void setToDefault()
	{
		sellLimit = defaultSellLimit;
		buyCounts.clear();
	}
	
	/**
	 * @param sellLimit
	 */
	public void setSellLimit(int sellLimit)
	{
		this.sellLimit = sellLimit;
	}
	
	/**
	 * return defaultSellLimit.
	 * @return
	 */
	public int getDefaultSellLimit()
	{
		return defaultSellLimit;
	}
	
	public String getSalesTime()
	{
		return salesTime;
	}
}
