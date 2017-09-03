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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author IlBuono
 */
public class SM_PLASTIC_SURGERY extends AionServerPacket
{
	
	private final int playerObjId;
	private final byte check_ticket;
	private final byte change_sex;
	
	public SM_PLASTIC_SURGERY(Player player, byte check_ticket, byte change_sex)
	{
		playerObjId = player.getObjectId();
		this.check_ticket = check_ticket;
		this.change_sex = change_sex;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(playerObjId);
		writeC(check_ticket);
		writeC(change_sex);
	}
}
