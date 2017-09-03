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

public class Letter extends AionObject
{
	private final int recipientId;
	private Item attachedItem;
	private long attachedKinahCount;
	private final String senderName;
	private final String title;
	private final String message;
	private boolean unread;
	private boolean express;
	private final Timestamp timeStamp;
	private PersistentState persistentState;
	private LetterType letterType;
	
	public Letter(int objId, int recipientId, Item attachedItem, long attachedKinahCount, String title, String message, String senderName, Timestamp timeStamp, boolean unread, LetterType letterType)
	{
		super(objId);
		if ((letterType == LetterType.EXPRESS) || (letterType == LetterType.BLACKCLOUD))
		{
			express = true;
		}
		else
		{
			express = false;
		}
		this.recipientId = recipientId;
		this.attachedItem = attachedItem;
		this.attachedKinahCount = attachedKinahCount;
		this.title = title;
		this.message = message;
		this.senderName = senderName;
		this.timeStamp = timeStamp;
		this.unread = unread;
		persistentState = PersistentState.NEW;
		this.letterType = letterType;
	}
	
	@Override
	public String getName()
	{
		return String.valueOf(attachedItem.getItemTemplate().getNameId());
	}
	
	public int getRecipientId()
	{
		return recipientId;
	}
	
	public Item getAttachedItem()
	{
		return attachedItem;
	}
	
	public long getAttachedKinah()
	{
		return attachedKinahCount;
	}
	
	public String getTitle()
	{
		return title;
	}
	
	public String getMessage()
	{
		return message;
	}
	
	public String getSenderName()
	{
		return senderName;
	}
	
	public LetterType getLetterType()
	{
		return letterType;
	}
	
	public boolean isUnread()
	{
		return unread;
	}
	
	public void setReadLetter()
	{
		unread = false;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public boolean isExpress()
	{
		return express;
	}
	
	public void setExpress(boolean express)
	{
		this.express = express;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public void setLetterType(LetterType letterType)
	{
		this.letterType = letterType;
		if ((letterType == LetterType.EXPRESS) || (letterType == LetterType.BLACKCLOUD))
		{
			express = true;
		}
		else
		{
			express = false;
		}
	}
	
	public PersistentState getLetterPersistentState()
	{
		return persistentState;
	}
	
	public void removeAttachedItem()
	{
		attachedItem = null;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public void removeAttachedKinah()
	{
		attachedKinahCount = 0;
		persistentState = PersistentState.UPDATE_REQUIRED;
	}
	
	public void delete()
	{
		persistentState = PersistentState.DELETED;
	}
	
	public void setPersistState(PersistentState state)
	{
		persistentState = state;
	}
	
	public Timestamp getTimeStamp()
	{
		return timeStamp;
	}
}