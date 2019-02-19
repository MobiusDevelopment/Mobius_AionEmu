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

import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastList;

public class SM_ABYSS_ARTIFACT_INFO2 extends AionServerPacket
{
	private final Collection<SiegeLocation> locations;
	
	public SM_ABYSS_ARTIFACT_INFO2(Collection<SiegeLocation> collection)
	{
		locations = collection;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final FastList<SiegeLocation> validLocations = new FastList<>();
		for (SiegeLocation loc : locations)
		{
			if (((loc.getType() == SiegeType.ARTIFACT) || (loc.getType() == SiegeType.FORTRESS)) && (loc.getLocationId() >= 1011) && (loc.getLocationId() < 2000))
			{
				validLocations.add(loc);
			}
		}
		writeH(validLocations.size());
		for (SiegeLocation loc : validLocations)
		{
			writeD(loc.getLocationId());
			writeC(0);
		}
	}
}
