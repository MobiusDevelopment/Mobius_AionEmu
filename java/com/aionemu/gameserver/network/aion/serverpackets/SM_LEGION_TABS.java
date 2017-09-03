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

import com.aionemu.gameserver.model.team.legion.LegionHistory;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Simple, KID, xTz
 */
public class SM_LEGION_TABS extends AionServerPacket
{
	
	private final int page;
	private final Collection<LegionHistory> legionHistory;
	private final int tabId;
	
	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory, int tabId)
	{
		this.legionHistory = legionHistory;
		page = 0;
		this.tabId = tabId;
	}
	
	public SM_LEGION_TABS(Collection<LegionHistory> legionHistory, int page, int tabId)
	{
		this.legionHistory = legionHistory;
		this.page = page;
		this.tabId = tabId;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final int size = legionHistory.size();
		/**
		 * If history size is less than page*8 return
		 */
		if (size < (page * 8))
		{
			return;
		}
		
		// TODO: Formula's could use a refactor
		int hisSize = size - (page * 8);
		if (size > ((page + 1) * 8))
		{
			hisSize = 8;
		}
		
		writeD(size);
		writeD(page); // current page
		writeD(hisSize);
		
		int i = 0;
		for (final LegionHistory history : legionHistory)
		{
			if ((i >= (page * 8)) && (i <= (8 + (page * 8))))
			{
				writeD((int) (history.getTime().getTime() / 1000));
				writeC(history.getLegionHistoryType().getHistoryId());
				writeC(0); // unk
				writeS(history.getName(), 64);
				writeH(0); // separator
				writeS(history.getDescription(), 64);
				writeD(0);
			}
			i++;
			if (i >= (8 + (page * 8)))
			{
				break;
			}
		}
		writeC(tabId);
		writeC(0);
	}
	
}