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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * So far I've found only one usage for this packet - to stop character blinking ( just after login into game, player's character is blinking )
 * @author Luno, Sweetkr states: 0 - normal char 1- crounched invisible char 64 - standing blinking char 128- char is invisible
 */
public class SM_PLAYER_STATE extends AionServerPacket
{
	private final int playerObjId;
	private final int visualState;
	private final int seeState;
	
	public SM_PLAYER_STATE(Creature creature)
	{
		playerObjId = creature.getObjectId();
		visualState = creature.getVisualState();
		seeState = creature.getSeeState();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(playerObjId);
		writeC(visualState);
		writeC(seeState);
		writeC(visualState == 64 ? 0x01 : 0x00);
	}
}
