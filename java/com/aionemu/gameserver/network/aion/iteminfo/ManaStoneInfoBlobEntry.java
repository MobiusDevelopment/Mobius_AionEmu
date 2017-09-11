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
import com.aionemu.gameserver.model.items.IdianStone;
import com.aionemu.gameserver.model.items.ItemStone;
import com.aionemu.gameserver.model.items.ManaStone;
import com.aionemu.gameserver.network.aion.iteminfo.ItemInfoBlob.ItemBlobType;

/**
 * This blob sends info about mana stones.
 * @author -Nemesiss-
 * @author Rolandas
 * @author GiGatR00n v4.7.5.x
 */
public class ManaStoneInfoBlobEntry extends ItemBlobEntry
{
	ManaStoneInfoBlobEntry()
	{
		super(ItemBlobType.MANA_SOCKETS);
	}
	
	@Override
	public void writeThisBlob(ByteBuffer buf)
	{
		final Item item = ownerItem;
		writeC(buf, item.isSoulBound() ? 1 : 0);
		writeC(buf, item.getEnchantLevel());
		writeD(buf, item.getItemSkinTemplate().getTemplateId());
		writeC(buf, item.getOptionalSocket());
		writeC(buf, item.hasEnchantBonus() ? item.getEnchantBonus() : 0);
		writeItemStones(buf);// 24-bytes
		final ItemStone god = item.getGodStone();
		writeD(buf, god == null ? 0 : god.getItemId());
		final int itemColor = item.getItemColor();
		final int dyeExpiration = item.getColorTimeLeft();
		if ((((dyeExpiration > 0) && (item.getColorExpireTime() > 0)) || ((dyeExpiration == 0) && (item.getColorExpireTime() == 0))) && item.getItemTemplate().isItemDyePermitted())
		{
			writeC(buf, itemColor == 0 ? 0 : 1);
			writeD(buf, itemColor);
			writeD(buf, 0);
			writeD(buf, dyeExpiration);
		}
		else
		{
			writeC(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
			writeD(buf, 0);
		}
		final IdianStone idianStone = item.getIdianStone();
		if ((idianStone != null) && (idianStone.getPolishNumber() > 0))
		{
			writeD(buf, idianStone.getItemId());
			writeC(buf, idianStone.getPolishNumber());
		}
		else
		{
			writeD(buf, 0);
			writeC(buf, 0);
		}
		writeC(buf, item.getAuthorize());// tempering 4.5
		writeH(buf, 0);
		writePlumeStats(buf);// 64-bytes
		writeB(buf, new byte[36]);
		writeAmplification(buf);
		writeB(buf, new byte[20]);
		writeD(buf, item.isLunaReskin() ? 1 : 0);// isLunaReskin
		writeC(buf, item.getReductionLevel()); // Level Reduction
		
	}
	
	private void writeAmplification(ByteBuffer buf)
	{
		final Item item = ownerItem;
		writeC(buf, item.isAmplified() ? 1 : 0);
		writeD(buf, item.getAmplificationSkill());
	}
	
	/**
	 * Writes plume stats
	 * @param buf
	 */
	private void writePlumeStats(ByteBuffer buf)
	{
		final Item item = ownerItem;
		if (item.getItemTemplate().isPlume())
		{
			writeD(buf, 0);// unk plume stat
			writeD(buf, 0);// value
			writeD(buf, 0);// unk plume stat
			writeD(buf, 0);// value
			writeD(buf, 42);
			writeD(buf, item.getAuthorize() * 150); // HP Boost for Tempering Solution
			if ((item.getItemTemplate().getTemperingTableId() == 10051) || (item.getItemTemplate().getTemperingTableId() == 10063))
			{
				writeD(buf, 30);
				writeD(buf, item.getAuthorize() * 4); // Physical Attack
				writeD(buf, 0);// unk plume stat
				writeD(buf, 0);// value
			}
			else if ((item.getItemTemplate().getTemperingTableId() == 10052) || (item.getItemTemplate().getTemperingTableId() == 10064))
			{
				writeD(buf, 35);
				writeD(buf, item.getAuthorize() * 20); // Magic Boost
				writeD(buf, 0);// unk plume stat
				writeD(buf, 0);// value
			}
			else if ((item.getItemTemplate().getTemperingTableId() == 10056) || (item.getItemTemplate().getTemperingTableId() == 10065))
			{
				writeD(buf, 33);
				writeD(buf, item.getAuthorize() * 12); // Physical Critical
				writeD(buf, 0);// unk plume stat
				writeD(buf, 0);// value
			}
			else if ((item.getItemTemplate().getTemperingTableId() == 10057) || (item.getItemTemplate().getTemperingTableId() == 10066))
			{
				writeD(buf, 36);
				writeD(buf, item.getAuthorize() * 8); // Magical Accuracy
				writeD(buf, 0);// unk plume stat
				writeD(buf, 0);// value
			}
			else if (item.getItemTemplate().getTemperingTableId() == 10105)
			{
				writeD(buf, 30);
				writeD(buf, item.getAuthorize() * 4); // Physical Attack
				writeD(buf, 32);
				writeD(buf, item.getAuthorize() * 16); // Physical Accuracy
			}
			else if (item.getItemTemplate().getTemperingTableId() == 10106)
			{
				writeD(buf, 35);
				writeD(buf, item.getAuthorize() * 20); // Magic Boost
				writeD(buf, 34);
				writeD(buf, item.getAuthorize() * 8); // Magic Critical
			}
			else if ((item.getItemTemplate().getTemperingTableId() == 10107) || (item.getItemTemplate().getTemperingTableId() == 10109))
			{
				writeD(buf, 30);
				writeD(buf, item.getAuthorize() * 4); // Magical Accuracy
				writeD(buf, 33);
				writeD(buf, item.getAuthorize() * 12); // Physical Critical
			}
			else if ((item.getItemTemplate().getTemperingTableId() == 10108) || (item.getItemTemplate().getTemperingTableId() == 10110))
			{
				writeD(buf, 35);
				writeD(buf, item.getAuthorize() * 20); // Magic Boost
				writeD(buf, 36);
				writeD(buf, item.getAuthorize() * 8); // Magical Accuracy
			}
			// Some Padding for future.
			writeD(buf, 0);// unk plume stat
			writeD(buf, 0);// value
			writeD(buf, 0);// unk plume stat
			writeD(buf, 0);// value
			writeD(buf, 0);// unk plume stat
			writeD(buf, 0);// value
		}
		else
		{
			writeB(buf, new byte[64]);
		}
	}
	
	private void writeItemStones(ByteBuffer buf)
	{
		final Item item = ownerItem;
		int count = 0;
		if (item.hasManaStones())
		{
			final Set<ManaStone> itemStones = item.getItemStones();
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
			if (item.getItemTemplate().getSpecialSlots() > 0)
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
				for (int i = count; i < item.getItemTemplate().getSpecialSlots(); i++)
				{
					writeD(buf, 0);
					count++;
				}
			}
			for (ManaStone basicStone : basicStones)
			{
				if (count == 6)
				{
					break;
				}
				writeD(buf, basicStone.getItemId());
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
		return 187;
		
	}
}