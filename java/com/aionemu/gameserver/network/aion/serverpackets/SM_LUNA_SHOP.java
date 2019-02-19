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

import java.util.HashMap;
import java.util.Map;

import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeEntry;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */
public class SM_LUNA_SHOP extends AionServerPacket
{
	private final int actionId;
	private int unk1;
	private int slotSize;
	private int fail;
	private ItemTemplate item;
	// Karunerk's Workshop
	@SuppressWarnings("unused")
	private int unkCraft1;
	@SuppressWarnings("unused")
	private int unkCraft2;
	private int craftItemID;
	private int craftItemCount;
	// Taki's Adventure
	private int indun_id;
	// Munirunerk's Treasure
	private HashMap<Integer, Long> munirunerk_treasure;
	
	private int isApply;
	private int applySlot;
	private int itemId;
	private int itemSize;
	
	public SM_LUNA_SHOP(int actionId)
	{
		this.actionId = actionId;
	}
	
	// Karunerk's Workshop
	public SM_LUNA_SHOP(int unkCraft1, int unkCraft2, int craftItemID, int craftItemCount)
	{
		actionId = 3;
		this.unkCraft1 = unkCraft1;
		this.unkCraft2 = unkCraft2;
		this.craftItemID = craftItemID;
		this.craftItemCount = craftItemCount;
	}
	
	// Taki's Adventure
	public SM_LUNA_SHOP(int actionId, int indun_id)
	{
		this.actionId = actionId;
		this.indun_id = indun_id;
	}
	
	// Munirunerk's Treasure
	public SM_LUNA_SHOP(HashMap<Integer, Long> munirunerk_treasure)
	{
		actionId = 12;
		this.munirunerk_treasure = munirunerk_treasure;
	}
	
	// Dorinerk's Wardrobe
	public SM_LUNA_SHOP(int actionId, int isApply, int applySlot, int itemId, int unk1)
	{
		this.actionId = actionId;
		this.isApply = isApply;
		this.applySlot = applySlot;
		this.itemId = itemId;
		this.unk1 = unk1;
	}
	
	public SM_LUNA_SHOP(int actionId, int slotSize, int itemSize)
	{
		this.actionId = actionId;
		this.slotSize = slotSize;
		this.itemSize = itemSize;
	}
	
	public SM_LUNA_SHOP(int actionId, ItemTemplate item, int fail)
	{
		this.actionId = actionId;
		this.item = item;
		this.fail = fail;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final Player player = con.getActivePlayer();
		writeC(actionId);
		switch (actionId)
		{
			case 0:
			{
				writeC(0);
				writeD(indun_id);
				break;
			}
			case 2:
			{
				writeC(fail);
				switch (fail)
				{
					case 0:
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330059, new Object[0]));
						break;
					}
					case 1:
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1330050, new Object[]
						{
							item.getName()
						}));
						break;
					}
				}
				break;
			}
			case 3:
			{
				writeC(1);// success ? 0x00 : 0x01
				writeH(1);// unk 0x01
				writeD(craftItemID);// productid
				writeQ(craftItemCount);// quantity
				break;
			}
			case 4:
			{
				break;
			}
			case 5:
			{
				writeC(0);
				writeC(0);
				writeC(0);
				break;
			}
			case 6:
			{
				writeD(53);
				break;
			}
			case 7:
			{
				writeD(55);
				break;
			}
			case 8:// dorinerk's wardrobe
			{
				writeC(0x00);
				writeC(slotSize);
				writeH(itemSize);
				for (int i = 0; i < itemSize; i++)
				{
					for (PlayerWardrobeEntry ce : player.getWardrobe().getAllWardrobe())
					{
						writeC(ce.getSlot());
						writeD(ce.getItemId());
						writeD(0x00);
						writeD(0x01);
					}
				}
				break;
			}
			case 10:
			{
				writeC(isApply);
				writeC(applySlot);
				writeD(itemId);
				writeD(unk1);
				break;
			}
			case 11:
			{
				writeC(0x00);
				writeC(indun_id);
				writeD(0x01);
				break;
			}
			case 12:// open chest
			{
				writeC(0);// unk
				writeH(3);// size always 3
				for (Map.Entry<Integer, Long> e : munirunerk_treasure.entrySet())
				{
					writeD(e.getKey());
					writeQ(e.getValue());
				}
				break;
			}
			case 14:
			{
				writeC(1); // free enter = 1
				writeD(indun_id);
				break;
			}
		}
	}
}