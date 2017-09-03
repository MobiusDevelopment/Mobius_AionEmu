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
package com.aionemu.loginserver.network.gs.serverpackets;

import java.util.Map;

import com.aionemu.loginserver.controller.BannedMacManager;
import com.aionemu.loginserver.model.base.BannedMacEntry;
import com.aionemu.loginserver.network.gs.GsConnection;
import com.aionemu.loginserver.network.gs.GsServerPacket;

/**
 * @author KID
 */
public class SM_MACBAN_LIST extends GsServerPacket
{
	
	private final Map<String, BannedMacEntry> bannedList;
	
	public SM_MACBAN_LIST()
	{
		bannedList = BannedMacManager.getInstance().getMap();
	}
	
	@Override
	protected void writeImpl(GsConnection con)
	{
		writeC(9);
		writeD(bannedList.size());
		
		for (BannedMacEntry entry : bannedList.values())
		{
			writeS(entry.getMac());
			writeQ(entry.getTime().getTime());
			writeS(entry.getDetails());
		}
	}
}
