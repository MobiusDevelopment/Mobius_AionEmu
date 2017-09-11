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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team.legion.LegionEmblemType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.LegionService;

/**
 * @author Simple modified cura
 */
public class CM_LEGION_MODIFY_EMBLEM extends AionClientPacket
{
	
	/** Emblem related information **/
	private int legionId;
	private int emblemId;
	private int red;
	private int green;
	private int blue;
	private LegionEmblemType emblemType;
	
	/**
	 * @param opcode
	 * @param state
	 * @param restStates
	 */
	public CM_LEGION_MODIFY_EMBLEM(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		legionId = readD();
		emblemId = readC();
		emblemType = (readC() == LegionEmblemType.DEFAULT.getValue()) ? LegionEmblemType.DEFAULT : LegionEmblemType.CUSTOM;
		readC(); // 0xFF (Fixed)
		red = readC();
		green = readC();
		blue = readC();
	}
	
	@Override
	protected void runImpl()
	{
		final Player activePlayer = getConnection().getActivePlayer();
		
		if (activePlayer.isLegionMember())
		{
			LegionService.getInstance().storeLegionEmblem(activePlayer, legionId, emblemId, red, green, blue, emblemType);
		}
	}
}
