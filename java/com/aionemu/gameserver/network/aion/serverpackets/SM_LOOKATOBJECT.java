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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author alexa026
 */
public class SM_LOOKATOBJECT extends AionServerPacket
{
	
	private final VisibleObject visibleObject;
	private int targetObjectId;
	private int heading;
	
	public SM_LOOKATOBJECT(VisibleObject visibleObject)
	{
		this.visibleObject = visibleObject;
		if (visibleObject.getTarget() != null)
		{
			targetObjectId = visibleObject.getTarget().getObjectId();
			heading = Math.abs(128 - visibleObject.getTarget().getHeading());
		}
		else
		{
			targetObjectId = 0;
			heading = visibleObject.getHeading();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(visibleObject.getObjectId());
		writeD(targetObjectId);
		writeC(heading);
	}
}
