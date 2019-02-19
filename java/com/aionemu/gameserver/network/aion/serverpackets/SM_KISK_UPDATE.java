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

import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Sarynth 0xB0 for 1.5.1.10 and 1.5.1.15
 */
public class SM_KISK_UPDATE extends AionServerPacket
{
	private final int objId;
	private final int creatorid;
	private final int useMask;
	private final int currentMembers;
	private final int maxMembers;
	private final int remainingRessurects;
	private final int maxRessurects;
	private final int remainingLifetime;
	
	public SM_KISK_UPDATE(Kisk kisk)
	{
		objId = kisk.getObjectId();
		creatorid = kisk.getCreatorId();
		useMask = kisk.getUseMask();
		currentMembers = kisk.getCurrentMemberCount();
		maxMembers = kisk.getMaxMembers();
		remainingRessurects = kisk.getRemainingResurrects();
		maxRessurects = kisk.getMaxRessurects();
		remainingLifetime = kisk.getRemainingLifetime();
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(objId);
		writeD(creatorid);
		writeD(useMask);
		writeD(currentMembers);
		writeD(maxMembers);
		writeD(remainingRessurects);
		writeD(maxRessurects);
		writeD(remainingLifetime);
	}
}