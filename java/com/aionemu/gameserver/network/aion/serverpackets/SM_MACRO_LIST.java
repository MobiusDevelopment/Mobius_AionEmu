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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * Packet with macro list.
 * @author -Nemesiss-
 */
public class SM_MACRO_LIST extends AionServerPacket
{
	
	private final Player player;
	private final boolean secondPart;
	
	/**
	 * Constructs new <tt>SM_MACRO_LIST </tt> packet
	 * @param player
	 * @param secondPart
	 */
	public SM_MACRO_LIST(Player player, boolean secondPart)
	{
		this.player = player;
		this.secondPart = secondPart;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(player.getObjectId());// player id
		
		final Map<Integer, String> macrosToSend = player.getMacroList().getMarcosPart(secondPart);
		final int size = macrosToSend.size();
		
		if (secondPart)
		{
			writeC(0x00);
			writeH(size);
		}
		else
		{
			writeC(0x01);
			writeH(-size);
		}
		
		if (size != 0)
		{
			for (Map.Entry<Integer, String> entry : macrosToSend.entrySet())
			{
				writeC(entry.getKey());// order
				writeS(entry.getValue());// xml
			}
		}
	}
}
