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

import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * This packet is informing client that some AionObject is no longer visible.
 * @author -Nemesiss-
 */
public class SM_DELETE extends AionServerPacket
{
	/**
	 * Object that is no longer visible.
	 */
	private final int objectId;
	private final int time;
	private final int id;
	
	/**
	 * Constructor.
	 * @param object
	 * @param time
	 * @param id
	 */
	
	public SM_DELETE(AionObject object, int time, int id)
	{
		objectId = object.getObjectId();
		this.time = time;
		this.id = id;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		final int action = 0;
		if (action != 1)
		{
			writeD(objectId);
			writeC(time); // removal animation speed
			writeC(id);
		}
	}
}
