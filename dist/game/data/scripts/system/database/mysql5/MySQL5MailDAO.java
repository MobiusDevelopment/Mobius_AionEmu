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
package system.database.mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.ItemStoneListDAO;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Letter;
import com.aionemu.gameserver.model.gameobjects.LetterType;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Mailbox;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.items.storage.StorageType;

/**
 * @author kosyachok
 */
public class MySQL5MailDAO extends MailDAO
{
	
	private static final Logger log = LoggerFactory.getLogger(MySQL5MailDAO.class);
	
	@Override
	public Mailbox loadPlayerMailbox(Player player)
	{
		final Mailbox mailbox = new Mailbox(player);
		final int playerId = player.getObjectId();
		DB.select("SELECT * FROM mail WHERE mail_recipient_id = ? ORDER BY recieved_time LIMIT 100", new ParamReadStH()
		{
			
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerId);
			}
			
			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				final List<Item> mailboxItems = loadMailboxItems(playerId);
				while (rset.next())
				{
					final int mailUniqueId = rset.getInt("mail_unique_id");
					final int recipientId = rset.getInt("mail_recipient_id");
					final String senderName = rset.getString("sender_name");
					final String mailTitle = rset.getString("mail_title");
					final String mailMessage = rset.getString("mail_message");
					final int unread = rset.getInt("unread");
					final int attachedItemId = rset.getInt("attached_item_id");
					final long attachedKinahCount = rset.getLong("attached_kinah_count");
					final LetterType letterType = LetterType.getLetterTypeById(rset.getInt("express"));
					final Timestamp recievedTime = rset.getTimestamp("recieved_time");
					Item attachedItem = null;
					if (attachedItemId != 0)
					{
						for (Item item : mailboxItems)
						{
							if (item.getObjectId() == attachedItemId)
							{
								if (item.getItemTemplate().isArmor() || item.getItemTemplate().isWeapon())
								{
									DAOManager.getDAO(ItemStoneListDAO.class).load(Collections.singletonList(item));
								}
								
								attachedItem = item;
							}
						}
					}
					
					final Letter letter = new Letter(mailUniqueId, recipientId, attachedItem, attachedKinahCount, mailTitle, mailMessage, senderName, recievedTime, unread == 1, letterType);
					letter.setPersistState(PersistentState.UPDATED);
					mailbox.putLetterToMailbox(letter);
				}
			}
		});
		
		return mailbox;
	}
	
	@Override
	public boolean haveUnread(int playerId)
	{
		
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT * FROM mail WHERE mail_recipient_id = ? ORDER BY recieved_time LIMIT 100");
			stmt.setInt(1, playerId);
			final ResultSet rset = stmt.executeQuery();
			while (rset.next())
			{
				final int unread = rset.getInt("unread");
				if (unread == 1)
				{
					return true;
				}
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Could not read mail for player: " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return false;
	}
	
	private List<Item> loadMailboxItems(int playerId)
	{
		final List<Item> mailboxItems = new ArrayList<>();
		
		DB.select("SELECT * FROM inventory WHERE `item_owner` = ? AND `item_location` = 127", new ParamReadStH()
		{
			
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerId);
			}
			
			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while (rset.next())
				{
					final int itemUniqueId = rset.getInt("item_unique_id");
					final int itemId = rset.getInt("item_id");
					final long itemCount = rset.getLong("item_count");
					final int itemColor = rset.getInt("item_color");
					final int colorExpireTime = rset.getInt("color_expires");
					final String itemCreator = rset.getString("item_creator");
					final int expireTime = rset.getInt("expire_time");
					final int activationCount = rset.getInt("activation_count");
					final int isEquiped = rset.getInt("is_equiped");
					final int isSoulBound = rset.getInt("is_soul_bound");
					final int slot = rset.getInt("slot");
					final int enchant = rset.getInt("enchant");
					final int enchantBonus = rset.getInt("enchant_bonus");
					final int itemSkin = rset.getInt("item_skin");
					final int fusionedItem = rset.getInt("fusioned_item");
					final int optionalSocket = rset.getInt("optional_socket");
					final int optionalFusionSocket = rset.getInt("optional_fusion_socket");
					final int charge = rset.getInt("charge");
					final Integer randomNumber = rset.getInt("rnd_bonus");
					final int rndCount = rset.getInt("rnd_count");
					final int wrappingCount = rset.getInt("wrappable_count");
					final int isPacked = rset.getInt("is_packed");
					final int temperingLevel = rset.getInt("tempering_level");
					final int isTopped = rset.getInt("is_topped");
					final int strengthenSkill = rset.getInt("strengthen_skill");
					final int skinSkill = rset.getInt("skin_skill");
					final int isLunaReskin = rset.getInt("luna_reskin");
					final int reductionLevel = rset.getInt("reduction_level");
					final int unSeal = rset.getInt("is_seal");
					final Item item = new Item(itemUniqueId, itemId, itemCount, itemColor, colorExpireTime, itemCreator, expireTime, activationCount, isEquiped == 1, isSoulBound == 1, slot, StorageType.MAILBOX.getId(), enchant, enchantBonus, itemSkin, fusionedItem, optionalSocket, optionalFusionSocket, charge, randomNumber, rndCount, wrappingCount, isPacked == 1, temperingLevel, isTopped == 1, strengthenSkill, skinSkill, isLunaReskin == 1, reductionLevel, unSeal);
					item.setPersistentState(PersistentState.UPDATED);
					mailboxItems.add(item);
				}
			}
		});
		return mailboxItems;
	}
	
	@Override
	public void storeMailbox(Player player)
	{
		final Mailbox mailbox = player.getMailbox();
		if (mailbox == null)
		{
			return;
		}
		final Collection<Letter> letters = mailbox.getLetters();
		for (Letter letter : letters)
		{
			storeLetter(letter.getTimeStamp(), letter);
		}
	}
	
	@Override
	public boolean storeLetter(Timestamp time, Letter letter)
	{
		boolean result = false;
		switch (letter.getLetterPersistentState())
		{
			case NEW:
			{
				result = saveLetter(time, letter);
				break;
			}
			case UPDATE_REQUIRED:
			{
				result = updateLetter(time, letter);
				break;
			}
			/*
			 * case DELETED: return deleteLetter(letter);
			 */
		}
		letter.setPersistState(PersistentState.UPDATED);
		
		return result;
	}
	
	private boolean saveLetter(Timestamp time, Letter letter)
	{
		int attachedItemId = 0;
		if (letter.getAttachedItem() != null)
		{
			attachedItemId = letter.getAttachedItem().getObjectId();
		}
		
		final int fAttachedItemId = attachedItemId;
		
		return DB.insertUpdate("INSERT INTO `mail` (`mail_unique_id`, `mail_recipient_id`, `sender_name`, `mail_title`, `mail_message`, `unread`, `attached_item_id`, `attached_kinah_count`, `express`, `recieved_time`) VALUES(?,?,?,?,?,?,?,?,?,?)", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, letter.getObjectId());
				stmt.setInt(2, letter.getRecipientId());
				stmt.setString(3, letter.getSenderName());
				stmt.setString(4, letter.getTitle());
				stmt.setString(5, letter.getMessage());
				stmt.setBoolean(6, letter.isUnread());
				stmt.setInt(7, fAttachedItemId);
				stmt.setLong(8, letter.getAttachedKinah());
				stmt.setInt(9, letter.getLetterType().getId());
				stmt.setTimestamp(10, time);
				stmt.execute();
			}
		});
	}
	
	private boolean updateLetter(Timestamp time, Letter letter)
	{
		int attachedItemId = 0;
		if (letter.getAttachedItem() != null)
		{
			attachedItemId = letter.getAttachedItem().getObjectId();
		}
		
		final int fAttachedItemId = attachedItemId;
		
		return DB.insertUpdate("UPDATE mail SET  unread=?, attached_item_id=?, attached_kinah_count=?, `express`=?, recieved_time=? WHERE mail_unique_id=?", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setBoolean(1, letter.isUnread());
				stmt.setInt(2, fAttachedItemId);
				stmt.setLong(3, letter.getAttachedKinah());
				stmt.setInt(4, letter.getLetterType().getId());
				stmt.setTimestamp(5, time);
				stmt.setInt(6, letter.getObjectId());
				stmt.execute();
			}
		});
	}
	
	@Override
	public boolean deleteLetter(int letterId)
	{
		return DB.insertUpdate("DELETE FROM mail WHERE mail_unique_id=?", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, letterId);
				stmt.execute();
			}
		});
	}
	
	@Override
	public void updateOfflineMailCounter(PlayerCommonData recipientCommonData)
	{
		DB.insertUpdate("UPDATE players SET mailbox_letters=? WHERE name=?", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, recipientCommonData.getMailboxLetters());
				stmt.setString(2, recipientCommonData.getName());
				stmt.execute();
			}
		});
	}
	
	@Override
	public int[] getUsedIDs()
	{
		final PreparedStatement statement = DB.prepareStatement("SELECT mail_unique_id FROM mail", ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
		
		try
		{
			final ResultSet rs = statement.executeQuery();
			rs.last();
			final int count = rs.getRow();
			rs.beforeFirst();
			final int[] ids = new int[count];
			for (int i = 0; i < count; i++)
			{
				rs.next();
				ids[i] = rs.getInt("mail_unique_id");
			}
			return ids;
		}
		catch (SQLException e)
		{
			log.error("Can't get list of id's from mail table", e);
		}
		finally
		{
			DB.close(statement);
		}
		
		return new int[0];
	}
	
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
