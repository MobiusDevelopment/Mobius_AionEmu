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

import java.util.List;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */
public class SM_LUNA_SHOP_LIST extends AionServerPacket
{
	private final int actionId;
	@SuppressWarnings("unused")
	private long points;
	@SuppressWarnings("unused")
	private int keys;
	private int costId;
	@SuppressWarnings("unused")
	private int entryCount;
	private int tableId;
	private List<Integer> idList;
	private List<Integer> randomDailyCraft;
	
	public SM_LUNA_SHOP_LIST(int actionId)
	{
		this.actionId = actionId;
	}
	
	public SM_LUNA_SHOP_LIST(int actionId, long points)
	{
		this.actionId = actionId;
		this.points = points;
	}
	
	public SM_LUNA_SHOP_LIST(int actionId, int keys)
	{
		this.actionId = actionId;
		this.keys = keys;
	}
	
	public SM_LUNA_SHOP_LIST(int actionId, int tableId, List<Integer> idList)
	{
		this.actionId = 2;
		this.tableId = 0;
		this.idList = idList;
	}
	
	public SM_LUNA_SHOP_LIST(List<Integer> randomDailyCraft)
	{
		actionId = 2;
		tableId = 1;
		this.randomDailyCraft = randomDailyCraft;
	}
	
	public SM_LUNA_SHOP_LIST(int actionId, int tableId, int costId)
	{
		this.actionId = actionId;
		this.tableId = tableId;
		this.costId = costId;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(actionId);// actionid
		switch (actionId)
		{
			case 0:// luna point handler id
			{
				writeQ(con.getAccount().getLuna());
				break;
			}
			case 1:// taki advanture update
			{
				writeH(tableId);// size?
				writeD(costId);
				writeD(1);
				break;
			}
			case 2:
			{
				writeC(tableId);// tabId
				switch (tableId)
				{
					case 0:
					{
						writeD(1474466400);// Start time
						writeD(0);
						writeD(1476280799);// End time
						writeD(0);
						writeH(idList.size());// size
						for (int i = 0; i < idList.size(); i++)
						{
							writeD(idList.get(i));// luna recipe id
						}
						break;
					}
					case 1:
					{
						writeD(1482393600);
						writeD(0); // test
						writeD(1482480000);
						writeD(0);
						writeH(randomDailyCraft.size());// size
						for (int i = 0; i < randomDailyCraft.size(); i++)
						{
							writeD(randomDailyCraft.get(i));// luna recipe id
						}
						break;
					}
				}
				break;
			}
			case 4:// munirunerk's keys
			{
				writeD(con.getActivePlayer().getMuniKeys());
				break;
			}
			case 5:// luna consume point spent
			{
				writeD(con.getActivePlayer().getLunaConsumePoint());
				break;
			}
			case 6:// update taki's mission?
			{
				break;
			}
			case 7:
			{
				writeC(0);
				writeH(100);
				break;
			}
		}
	}
}