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
package com.aionemu.gameserver.model.gameobjects;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Comparator;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.configs.main.BrokerConfig;
import com.aionemu.gameserver.model.broker.BrokerRace;

/**
 * @author kosyachok
 * @author GiGatR00n
 */
public class BrokerItem implements Comparable<BrokerItem>
{
	private final Item item;
	private final int itemId;
	private int itemUniqueId;
	private long itemCount;
	private String itemCreator;
	private long price;
	private final String seller;
	private final int sellerId;
	private final BrokerRace itemBrokerRace;
	private boolean isSold, isCanceled;
	private boolean isSettled;
	private final Timestamp expireTime;
	private Timestamp settleTime;
	private final boolean isSplitSell;
	PersistentState state;
	private final int ExpireTimeinMillis = BrokerConfig.ITEMS_EXPIRE_TIME * 24 * 3600 * 1000;
	
	public BrokerItem(Item item, long price, String seller, int sellerId, BrokerRace itemBrokerRace, boolean isSplitSell)
	{
		this.item = item;
		itemId = item.getItemTemplate().getTemplateId();
		itemUniqueId = item.getObjectId();
		itemCount = item.getItemCount();
		itemCreator = item.getItemCreator();
		this.price = price;
		this.seller = seller;
		this.sellerId = sellerId;
		this.itemBrokerRace = itemBrokerRace;
		isSold = false;
		isSettled = false;
		expireTime = new Timestamp(Calendar.getInstance().getTimeInMillis() + ExpireTimeinMillis);
		settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		this.isSplitSell = isSplitSell;
		state = PersistentState.NEW;
	}
	
	public BrokerItem(Item item, int itemId, int itemUniqueId, long itemCount, String itemCreator, long price, String seller, int sellerId, BrokerRace itemBrokerRace, boolean isSold, boolean isSettled, Timestamp expireTime, Timestamp settleTime, boolean isSplitSell)
	{
		this.item = item;
		this.itemId = itemId;
		this.itemUniqueId = itemUniqueId;
		this.itemCount = itemCount;
		this.itemCreator = itemCreator;
		this.price = price;
		this.seller = seller;
		this.sellerId = sellerId;
		this.itemBrokerRace = itemBrokerRace;
		this.isSplitSell = isSplitSell;
		if (item == null)
		{
			this.isSold = true;
			this.isSettled = true;
			
		}
		else
		{
			this.isSold = isSold;
			this.isSettled = isSettled;
		}
		this.expireTime = expireTime;
		this.settleTime = settleTime;
		state = PersistentState.NOACTION;
	}
	
	public String getItemCreator()
	{
		if (itemCreator == null)
		{
			return StringUtils.EMPTY;
		}
		return itemCreator;
	}
	
	public void setItemCreator(String itemCreator)
	{
		this.itemCreator = itemCreator;
	}
	
	public Item getItem()
	{
		return item;
	}
	
	public boolean isCanceled()
	{
		return isCanceled;
	}
	
	public void setIsCanceled(boolean isCanceled)
	{
		this.isCanceled = isCanceled;
	}
	
	public void removeItem()
	{
		isSold = true;
		isSettled = true;
		settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public int getItemId()
	{
		return itemId;
	}
	
	public int getItemUniqueId()
	{
		return itemUniqueId;
	}
	
	public long getPrice()
	{
		return price;
	}
	
	public boolean isSplitSell()
	{
		return isSplitSell;
	}
	
	public String getSeller()
	{
		return seller;
	}
	
	public int getSellerId()
	{
		return sellerId;
	}
	
	public BrokerRace getItemBrokerRace()
	{
		return itemBrokerRace;
	}
	
	public boolean isSold()
	{
		return isSold;
	}
	
	public void setPersistentState(PersistentState persistentState)
	{
		switch (persistentState)
		{
			case DELETED:
			{
				if (state == PersistentState.NEW)
				{
					state = PersistentState.NOACTION;
				}
				else
				{
					state = PersistentState.DELETED;
				}
				break;
			}
			case UPDATE_REQUIRED:
			{
				if (state == PersistentState.NEW)
				{
					break;
				}
				break;
			}
			default:
			{
				state = persistentState;
			}
		}
	}
	
	public PersistentState getPersistentState()
	{
		return state;
	}
	
	public boolean isSettled()
	{
		return isSettled;
	}
	
	public void setSettled()
	{
		isSettled = true;
		settleTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
	}
	
	public Timestamp getExpireTime()
	{
		return expireTime;
	}
	
	public Timestamp getSettleTime()
	{
		return settleTime;
	}
	
	public long getItemCount()
	{
		return itemCount;
	}
	
	private int getItemLevel()
	{
		return item.getItemTemplate().getLevel();
	}
	
	public long getPiecePrice()
	{
		return getPrice() / getItemCount();
	}
	
	private String getItemName()
	{
		return item.getItemName();
	}
	
	public void setItemCount(long count)
	{
		itemCount = count;
	}
	
	public void setPrice(long ItemPrice)
	{
		price = ItemPrice;
	}
	
	public void setItemUniqueId(int newObjId)
	{
		itemUniqueId = newObjId;
	}
	
	@Override
	public int compareTo(BrokerItem o)
	{
		return itemUniqueId > o.getItemUniqueId() ? 1 : -1;
	}
	
	static Comparator<BrokerItem> NAME_SORT_ASC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		return o1.getItemName().compareTo(o2.getItemName());
	};
	
	static Comparator<BrokerItem> NAME_SORT_DESC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		return o1.getItemName().compareTo(o2.getItemName());
	};
	
	static Comparator<BrokerItem> PRICE_SORT_ASC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getPrice() == o2.getPrice())
		{
			return 0;
		}
		return o1.getPrice() > o2.getPrice() ? 1 : -1;
	};
	
	static Comparator<BrokerItem> PRICE_SORT_DESC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getPrice() == o2.getPrice())
		{
			return 0;
		}
		return o1.getPrice() > o2.getPrice() ? -1 : 1;
	};
	
	static Comparator<BrokerItem> PIECE_PRICE_SORT_ASC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getPiecePrice() == o2.getPiecePrice())
		{
			return 0;
		}
		return o1.getPiecePrice() > o2.getPiecePrice() ? 1 : -1;
	};
	
	static Comparator<BrokerItem> PIECE_PRICE_SORT_DESC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getPiecePrice() == o2.getPiecePrice())
		{
			return 0;
		}
		return o1.getPiecePrice() > o2.getPiecePrice() ? -1 : 1;
	};
	
	static Comparator<BrokerItem> LEVEL_SORT_ASC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getItemLevel() == o2.getItemLevel())
		{
			return 0;
		}
		return o1.getItemLevel() > o2.getItemLevel() ? 1 : -1;
	};
	
	static Comparator<BrokerItem> LEVEL_SORT_DESC = (o1, o2) ->
	{
		if ((o1 == null) || (o2 == null))
		{
			return comparePossiblyNull(o1, o2);
		}
		if (o1.getItemLevel() == o2.getItemLevel())
		{
			return 0;
		}
		return o1.getItemLevel() > o2.getItemLevel() ? -1 : 1;
	};
	
	static <T extends Comparable<T>> int comparePossiblyNull(T aThis, T aThat)
	{
		int result = 0;
		if ((aThis == null) && (aThat != null))
		{
			result = -1;
		}
		else if ((aThis != null) && (aThat == null))
		{
			result = 1;
		}
		return result;
	}
	
	public static Comparator<BrokerItem> getComparatoryByType(int sortType)
	{
		switch (sortType)
		{
			case 0:
			{
				return NAME_SORT_ASC;
			}
			case 1:
			{
				return NAME_SORT_DESC;
			}
			case 2:
			{
				return LEVEL_SORT_ASC;
			}
			case 3:
			{
				return LEVEL_SORT_DESC;
			}
			case 4:
			{
				return PRICE_SORT_ASC;
			}
			case 5:
			{
				return PRICE_SORT_DESC;
			}
			case 6:
			{
				return PIECE_PRICE_SORT_ASC;
			}
			case 7:
			{
				return PIECE_PRICE_SORT_DESC;
			}
			default:
			{
				throw new IllegalArgumentException("Illegal sort type for broker items");
			}
		}
	}
}