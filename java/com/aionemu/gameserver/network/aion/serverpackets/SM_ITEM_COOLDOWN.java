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

import java.util.Map;

import com.aionemu.gameserver.model.items.ItemCooldown;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author ATracer
 */
public class SM_ITEM_COOLDOWN extends AionServerPacket
{
	
	private final Map<Integer, ItemCooldown> cooldowns;
	
	public SM_ITEM_COOLDOWN(Map<Integer, ItemCooldown> cooldowns)
	{
		this.cooldowns = cooldowns;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeH(cooldowns.size());
		final long currentTime = System.currentTimeMillis();
		for (Map.Entry<Integer, ItemCooldown> entry : cooldowns.entrySet())
		{
			writeH(entry.getKey());
			final int left = (int) ((entry.getValue().getReuseTime() - currentTime) / 1000);
			writeD(left > 0 ? left : 0);
			writeD(entry.getValue().getUseDelay());
		}
	}
}
