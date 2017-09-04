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

import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.mail.MailMessage;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.MailServicePacket;
import com.aionemu.gameserver.utils.collections.ListSplitter;

public class SM_MAIL_SERVICE extends MailServicePacket
{
	private final int serviceId;
	private Collection<Letter> letters;
	private int totalCount;
	private int unreadCount;
	private int unreadExpressCount;
	private int unreadBlackCloudCount;
	private int mailMessage;
	private Letter letter;
	private long time;
	private int letterId;
	private int[] letterIds;
	private int attachmentType;
	private boolean isExpress;
	
	public SM_MAIL_SERVICE(Mailbox mailbox)
	{
		super(null);
		serviceId = 0;
	}
	
	public SM_MAIL_SERVICE(MailMessage mailMessage)
	{
		super(null);
		serviceId = 1;
		this.mailMessage = mailMessage.getId();
	}
	
	public SM_MAIL_SERVICE(Player player, Collection<Letter> letters)
	{
		super(player);
		serviceId = 2;
		this.letters = letters;
	}
	
	public SM_MAIL_SERVICE(Player player, Collection<Letter> letters, boolean isExpress)
	{
		super(player);
		serviceId = 2;
		this.letters = letters;
		this.isExpress = isExpress;
	}
	
	public SM_MAIL_SERVICE(Player player, Letter letter, long time)
	{
		super(player);
		serviceId = 3;
		this.letter = letter;
		this.time = time;
	}
	
	public SM_MAIL_SERVICE(int letterId, int attachmentType)
	{
		super(null);
		serviceId = 5;
		this.letterId = letterId;
		this.attachmentType = attachmentType;
	}
	
	public SM_MAIL_SERVICE(int[] letterIds)
	{
		super(null);
		serviceId = 6;
		this.letterIds = letterIds;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final Mailbox mailbox = con.getActivePlayer().getMailbox();
		totalCount = mailbox.size();
		unreadCount = mailbox.getUnreadCount();
		unreadExpressCount = mailbox.getUnreadCountByType(LetterType.EXPRESS);
		unreadBlackCloudCount = mailbox.getUnreadCountByType(LetterType.BLACKCLOUD);
		writeC(serviceId);
		switch (serviceId)
		{
			case 0:
			{
				mailbox.isMailListUpdateRequired = true;
				writeMailboxState(totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount);
				break;
			}
			case 1:
			{
				writeMailMessage(mailMessage);
				break;
			}
			case 2:
			{
				Collection<Letter> _letters;
				if (!letters.isEmpty())
				{
					final ListSplitter<Letter> splittedLetters = new ListSplitter<>(letters, 100);
					_letters = splittedLetters.getNext();
				}
				else
				{
					_letters = letters;
				}
				writeLettersList(_letters, player, isExpress, unreadExpressCount + unreadBlackCloudCount);
				break;
			}
			case 3:
			{
				writeLetterRead(letter, time, totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount);
				break;
			}
			case 5:
			{
				writeLetterState(letterId, attachmentType);
				break;
			}
			case 6:
			{
				mailbox.isMailListUpdateRequired = true;
				writeLetterDelete(totalCount, unreadCount, unreadExpressCount, unreadBlackCloudCount, letterIds);
				break;
			}
		}
	}
}