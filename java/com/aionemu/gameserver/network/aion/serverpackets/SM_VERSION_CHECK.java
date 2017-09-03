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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.network.NetworkController;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_VERSION_CHECK extends AionServerPacket
{
	private static final Logger log = LoggerFactory.getLogger(SM_VERSION_CHECK.class);
	private final int version;
	private int characterLimitCount;
	private final int characterCreateMode;
	private final int characterFactionsMode;
	private static int port = 10241;
	
	public SM_VERSION_CHECK(int version)
	{
		this.version = version;
		if ((MembershipConfig.CHARACTER_ADDITIONAL_ENABLE != 10) && (MembershipConfig.CHARACTER_ADDITIONAL_COUNT > GSConfig.CHARACTER_LIMIT_COUNT))
		{
			characterLimitCount = MembershipConfig.CHARACTER_ADDITIONAL_COUNT;
		}
		else
		{
			characterLimitCount = GSConfig.CHARACTER_LIMIT_COUNT;
		}
		characterLimitCount *= NetworkController.getInstance().getServerCount();
		if ((GSConfig.CHARACTER_CREATION_MODE < 0) || (GSConfig.CHARACTER_CREATION_MODE > 2))
		{
			characterFactionsMode = 0;
		}
		else
		{
			characterFactionsMode = GSConfig.CHARACTER_CREATION_MODE;
		}
		if ((GSConfig.CHARACTER_FACTION_LIMITATION_MODE < 0) || (GSConfig.CHARACTER_FACTION_LIMITATION_MODE > 3))
		{
			characterCreateMode = 0;
		}
		else
		{
			characterCreateMode = GSConfig.CHARACTER_FACTION_LIMITATION_MODE * 0x04;
		}
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		// Aion 3.0 = 194
		// Aion 3.5 = 196
		// Aion 4.0 = 201
		// Aion 4.5 = 203
		// Aion 4.7 = 204
		// Aion 4.7.5 = 206
		// Aion 4.8 = 207
		// Aion 4.9 = 208
		// Aion 5.0 = 211
		// Aion 5.1 = 212
		if (version < 212)
		{
			// Send wrong client version
			writeC(0x02);
			return;
		}
		if (version == 212)
		{
			log.info("Authentication with client version 5.1");
		}
		else if (version < 212)
		{
			log.info("Authentication with client version lower than 5.0");
		}
		writeC(0x00);
		writeC(NetworkConfig.GAMESERVER_ID);
		writeB("417202003E720200000000003E720200A9E7F15600"); // 21 bytes
		writeC(GSConfig.SERVER_COUNTRY_CODE);
		writeC(0x00);
		final int serverMode = (characterLimitCount * 0x10) | characterFactionsMode;
		if (GSConfig.ENABLE_RATIO_LIMITATION)
		{
			if ((GameServer.getCountFor(Race.ELYOS) + GameServer.getCountFor(Race.ASMODIANS)) > GSConfig.RATIO_HIGH_PLAYER_COUNT_DISABLING)
			{
				writeC(serverMode | 0x0C);
			}
			else if (GameServer.getRatiosFor(Race.ELYOS) > GSConfig.RATIO_MIN_VALUE)
			{
				writeC(serverMode | 0x04);
			}
			else if (GameServer.getRatiosFor(Race.ASMODIANS) > GSConfig.RATIO_MIN_VALUE)
			{
				writeC(serverMode | 0x08);
			}
			else
			{
				writeC(serverMode);
			}
		}
		else
		{
			writeC(serverMode | characterCreateMode);
		}
		writeD((int) (System.currentTimeMillis() / 1000));
		writeB("5E010101010A053301010200"); // 12 bytes
		writeC(GSConfig.CHARACTER_REENTRY_TIME);
		switch (EventsConfig.ENABLE_DECOR)
		{
			case 1:
				writeC(0x01); // Christmast.
				break;
			case 2:
				writeC(0x02); // Halloween.
				break;
			case 3:
				writeC(0x08); // Brax Cafe.
				break;
			case 4:
				writeC(0x04); // Valentine.
				break;
			default:
				writeC(EventsConfig.ENABLE_DECOR);
				break;
		}
		if (GSConfig.SERVER_COUNTRY_CODE == 1)
		{
			writeB("00000000808FFFFF0478916202010000000000000000B80B010001010000000001010000000000000000000000000000000000E8030000E8030000E8030000E8030000E8030000E8030000E8030000E8030000E8030000E8030000E80300000064000000E80300000000803F01130000000108"); // 115 bytes
		}
		writeH(0x01);
		writeC(0x00);
		writeB(IPConfig.getDefaultAddress());
		writeH(port);
	}
}