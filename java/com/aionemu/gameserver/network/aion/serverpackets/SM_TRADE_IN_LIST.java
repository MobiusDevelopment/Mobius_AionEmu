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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate.TradeTab;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Rinzler (30.03.2014)
 */
public class SM_TRADE_IN_LIST extends AionServerPacket
{
	private final Npc npc;
	private final TradeListTemplate tlist;
	private final int buyPriceModifier;
	
	public SM_TRADE_IN_LIST(Npc npc, TradeListTemplate tlist, int buyPriceModifier)
	{
		this.npc = npc;
		this.tlist = tlist;
		this.buyPriceModifier = buyPriceModifier;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		if ((tlist != null) && (tlist.getNpcId() != 0) && (tlist.getCount() != 0))
		{
			writeD(npc.getObjectId());
			writeC(tlist.getTradeNpcType().index());
			writeD(buyPriceModifier);
			writeD(0x64); // 4.7
			writeH(tlist.getCount());
			for (TradeTab tradeTabl : tlist.getTradeTablist())
			{
				writeD(tradeTabl.getId());
			}
		}
	}
}