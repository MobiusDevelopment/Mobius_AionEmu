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

import com.aionemu.gameserver.model.gameobjects.player.PlayerScripts;
import com.aionemu.gameserver.model.house.PlayerScript;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_HOUSE_SCRIPTS extends AionServerPacket
{
	
	private final int address;
	private final PlayerScripts scripts;
	int from;
	int to;
	
	public SM_HOUSE_SCRIPTS(int address, PlayerScripts scripts, int from, int to)
	{
		this.address = address;
		this.scripts = scripts;
		this.from = from;
		this.to = to;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(address);
		writeH((to - from) + 1);
		final Map<Integer, PlayerScript> scriptMap = scripts.getScripts();
		for (int position = from; position <= to; position++)
		{
			writeC(position);
			final PlayerScript script = scriptMap.get(position);
			final byte[] bytes = script.getCompressedBytes();
			if (bytes == null)
			{
				writeH(-1);
			}
			else if (bytes.length == 0)
			{
				writeH(0);
			}
			else
			{
				writeH(bytes.length + 8);
				writeD(bytes.length);
				writeD(script.getUncompressedSize());
				writeB(bytes);
			}
		}
	}
}
