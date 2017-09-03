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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.BrokerService;

public class CM_REGISTER_BROKER_ITEM extends AionClientPacket
{
	@SuppressWarnings("unused")
	private int brokerId;
	private int itemUniqueId;
	private long PricePerItem;
	private long itemCount;
	private boolean isSplitSell;
	
	public CM_REGISTER_BROKER_ITEM(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		brokerId = readD();
		itemUniqueId = readD();
		PricePerItem = readQ();
		itemCount = readQ();
		isSplitSell = (readC() == 1);
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (player.isTrading() || (PricePerItem < 1) || (itemCount < 1))
		{
			return;
		}
		BrokerService.getInstance().registerItem(player, itemUniqueId, itemCount, PricePerItem, isSplitSell);
	}
}