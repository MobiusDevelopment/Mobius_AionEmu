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

import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate;
import com.aionemu.gameserver.model.templates.tradelist.TradeListTemplate.TradeTab;
import com.aionemu.gameserver.model.templates.tradelist.TradeNpcType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SELL_ITEM extends AionServerPacket
{
	private final int targetObjectId;
	private final int sellPercentage;
	private byte action = 0x01;
	private TradeListTemplate tradeListTemplate = null;
	
	public SM_SELL_ITEM(int targetObjectId, int sellPercentage)
	{
		this.sellPercentage = sellPercentage;
		this.targetObjectId = targetObjectId;
	}
	
	public SM_SELL_ITEM(int targetObjectId, TradeListTemplate tradeListTemplate)
	{
		sellPercentage = tradeListTemplate.getBuyPriceRate();
		this.targetObjectId = targetObjectId;
		this.tradeListTemplate = tradeListTemplate;
		if (tradeListTemplate.getTradeNpcType() == TradeNpcType.ABYSS)
		{
			action = 0x02;
		}
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(targetObjectId);
		writeC(action);
		writeD(sellPercentage);
		writeC(tradeListTemplate == null ? 0x01 : 0x00);
		writeC(0x01);
		if (tradeListTemplate != null)
		{
			writeH(tradeListTemplate.getCount());
			for (final TradeTab tradeTabl : tradeListTemplate.getTradeTablist())
			{
				writeD(tradeTabl.getId());
			}
		}
		else
		{
			writeH(0x00);
		}
	}
}