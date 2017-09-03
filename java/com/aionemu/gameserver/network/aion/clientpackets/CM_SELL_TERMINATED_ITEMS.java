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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemDeleteType;

/**
 * @author Ranastic
 */

public class CM_SELL_TERMINATED_ITEMS extends AionClientPacket
{
	private int size;
	private int itemObjId;
	private static final Logger log = LoggerFactory.getLogger(CM_SELL_TERMINATED_ITEMS.class);
	
	public CM_SELL_TERMINATED_ITEMS(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (player == null)
		{
			return;
		}
		final Storage inventory = player.getInventory();
		size = readH();
		for (int i = 0; i < size; i++)
		{
			itemObjId = readD();
			final Item item = inventory.getItemByObjId(itemObjId);
			inventory.delete(item, ItemDeleteType.DISCARD);
		}
		inventory.increaseKinah(size * 5000);
	}
	
	@Override
	protected void runImpl()
	{
	}
}