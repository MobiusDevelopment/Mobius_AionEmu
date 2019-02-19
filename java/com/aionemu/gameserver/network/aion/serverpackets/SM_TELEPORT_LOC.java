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
 * This packet is used to teleport player and port animation
 * @author Luno, orz, xTz
 */
public class SM_TELEPORT_LOC extends AionServerPacket
{
	private final int mapId;
	private final int instanceId;
	private final float x;
	private final float y;
	private final float z;
	private final byte heading;
	private final boolean isInstance;
	
	public SM_TELEPORT_LOC(boolean isInstance, int instanceId, int mapId, float x, float y, float z, byte heading)
	{
		this.isInstance = isInstance;
		this.instanceId = instanceId;
		this.mapId = mapId;
		this.x = x;
		this.y = y;
		this.z = z;
		this.heading = heading;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(0x03);
		writeD(mapId); // 4.3
		writeD(isInstance ? instanceId : mapId);
		writeF(x);
		writeF(y);
		writeF(z);
		writeC(heading);
	}
}