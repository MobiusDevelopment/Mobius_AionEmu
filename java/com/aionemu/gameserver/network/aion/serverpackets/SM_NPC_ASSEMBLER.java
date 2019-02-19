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

import com.aionemu.gameserver.model.assemblednpc.AssembledNpc;
import com.aionemu.gameserver.model.assemblednpc.AssembledNpcPart;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author xTz
 */
public class SM_NPC_ASSEMBLER extends AionServerPacket
{
	private final AssembledNpc assembledNpc;
	private final int routeId;
	private final long timeOnMap;
	
	public SM_NPC_ASSEMBLER(AssembledNpc assembledNpc)
	{
		this.assembledNpc = assembledNpc;
		routeId = assembledNpc.getRouteId();
		timeOnMap = assembledNpc.getTimeOnMap();
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(assembledNpc.getAssembledParts().size());
		for (AssembledNpcPart npc : assembledNpc.getAssembledParts())
		{
			writeD(routeId);
			writeD(npc.getObject());
			writeD(npc.getNpcId());
			writeD(npc.getEntityId());
			writeQ(timeOnMap);
		}
	}
}