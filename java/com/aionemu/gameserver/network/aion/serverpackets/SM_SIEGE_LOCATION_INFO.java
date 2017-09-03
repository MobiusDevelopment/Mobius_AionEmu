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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.team.legion.LegionEmblem;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.SiegeService;

import javolution.util.FastMap;

public class SM_SIEGE_LOCATION_INFO extends AionServerPacket
{
	private final int infoType;
	private final Map<Integer, SiegeLocation> locations;
	private static final Logger log = LoggerFactory.getLogger(SM_SIEGE_LOCATION_INFO.class);
	
	public SM_SIEGE_LOCATION_INFO()
	{
		infoType = 0;
		locations = SiegeService.getInstance().getSiegeLocations();
	}
	
	public SM_SIEGE_LOCATION_INFO(SiegeLocation loc)
	{
		infoType = 1;
		locations = new FastMap<>();
		locations.put(loc.getLocationId(), loc);
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final Player player = con.getActivePlayer();
		if (!SiegeConfig.SIEGE_ENABLED)
		{
			writeC(0);
			writeH(0);
			return;
		}
		writeC(infoType);
		writeH(locations.size());
		for (final SiegeLocation loc : locations.values())
		{
			LegionEmblem emblem = new LegionEmblem();
			writeD(loc.getLocationId());
			final int legionId = loc.getLegionId();
			writeD(legionId);
			if (legionId != 0)
			{
				if (LegionService.getInstance().getLegion(legionId) == null)
				{
					log.error("Can't find or load legion with id " + legionId);
				}
				else
				{
					emblem = LegionService.getInstance().getLegion(legionId).getLegionEmblem();
				}
			}
			if (emblem.getEmblemType() == LegionEmblemType.DEFAULT)
			{
				writeD(emblem.getEmblemId());
				writeC(255);
				writeC(emblem.getColor_r());
				writeC(emblem.getColor_g());
				writeC(emblem.getColor_b());
			}
			else
			{
				writeD(emblem.getCustomEmblemData().length);
				writeC(255);
				writeC(emblem.getColor_r());
				writeC(emblem.getColor_g());
				writeC(emblem.getColor_b());
			}
			writeC(loc.getRace().getRaceId());
			writeC(loc.isVulnerable() ? 2 : 0);
			writeC(loc.isCanTeleport(player) ? 1 : 0);
			writeC(loc.getNextState());
			// writeC(loc.getOccupyCount());
			writeH(0);
			writeH(1);
			writeD(0x00);// unk
			writeD(0x9);// unk dynamic value
			switch (loc.getLocationId())
			{
				case 2111: // Veille.
				case 3111: // Mastarius.
					writeD(SiegeService.getInstance().getRemainingSiegeTimeInSeconds(loc.getLocationId()));
					writeD(0x00);// unk
					break;
				default:
					writeD(10000);
					writeD(0x00);// unk
					break;
			}
		}
	}
}