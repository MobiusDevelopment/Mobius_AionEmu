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

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */
public class SM_COALESCENCE_RESULT extends AionServerPacket
{
	private final int itemTemplateId;
	private final int itemObjId;
	
	public SM_COALESCENCE_RESULT(int itemTemplateId, int itemObjId)
	{
		this.itemTemplateId = itemTemplateId;
		this.itemObjId = itemObjId;
	}
	
	@Override
	protected void writeImpl(AionConnection client)
	{
		writeD(itemTemplateId);
		writeD(itemObjId);
		writeQ(0x00);// unk
		writeQ(0x00);// unk
		writeD(0x01);// unk
	}
}
