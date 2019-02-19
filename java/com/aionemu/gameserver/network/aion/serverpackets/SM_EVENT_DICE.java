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

public class SM_EVENT_DICE extends AionServerPacket
{
	private int tableId = 3;
	private final int currentStep;
	private final int diceLeft;
	private final int diceGolden;
	private final int unkButton;
	private final int moveStep;
	
	public SM_EVENT_DICE(int tableId, int currentStep, int diceLeft, int diceGolden, int unkButton, int moveStep)
	{
		this.tableId = tableId;
		this.currentStep = currentStep;
		this.diceLeft = diceLeft;
		this.diceGolden = diceGolden;
		this.unkButton = unkButton;
		this.moveStep = moveStep;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(tableId);// table id
		writeD(currentStep);// current step
		writeD(0);
		writeD(0);
		writeD(diceLeft);// dice left
		writeD(diceGolden);// dice golden
		writeD(unkButton);// button near dice left
		writeD(379322);
		writeD(0);
		writeD(379322);
		writeD(0);
		writeD(moveStep);// move step
	}
	
}
