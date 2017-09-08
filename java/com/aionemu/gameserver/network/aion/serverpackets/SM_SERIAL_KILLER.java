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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Source & xTz
 */
public class SM_SERIAL_KILLER extends AionServerPacket
{
	private final int type;
	private int debuffLvl;
	private Collection<Player> players;
	
	public SM_SERIAL_KILLER(boolean showMsg, int debuffLvl)
	{
		type = showMsg ? 1 : 0;
		this.debuffLvl = debuffLvl;
	}
	
	public SM_SERIAL_KILLER(Collection<Player> players)
	{
		type = 4;
		this.players = players;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		switch (type)
		{
			case 0:
			case 1:
			{
				writeD(type);
				writeD(0x01);
				writeD(0x01);
				writeH(0x01);
				writeD(debuffLvl);
				break;
			}
			case 4:
			{
				writeD(type);
				writeD(0x01);
				writeD(0x01);
				writeH(players.size());
				for (Player player : players)
				{
					writeD(player.getProtectorInfo().getRank());
					writeD(player.getProtectorInfo().getType());
					writeD(player.getConquerorInfo().getRank());
					writeD(player.getObjectId());
					writeD(0x01);
					writeD(player.getAbyssRank().getRank().getId());
					writeH(player.getLevel());
					writeF(player.getX());
					writeF(player.getY());
					writeS(player.getName(), 134);
					writeH(4);
				}
				break;
			}
			case 5:
			{
				writeH(players.size());
				for (Player player : players)
				{
					writeD(player.getProtectorInfo().getRank());
					writeD(player.getProtectorInfo().getType());
					writeD(player.getConquerorInfo().getRank());
					writeD(player.getObjectId());
					writeD(0x01);
					writeD(player.getAbyssRank().getRank().getId());
					writeH(player.getLevel());
					writeF(player.getX());
					writeF(player.getY());
					writeS(player.getName(), 134);
					writeH(0);
				}
				break;
			}
		}
	}
}