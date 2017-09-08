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

import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;

public class CM_TUNE_RESULT extends AionClientPacket
{
	private int itemObjectId;
	private int unk;
	private int accept;
	
	public CM_TUNE_RESULT(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		itemObjectId = readD();
		unk = readC();
		switch (unk)
		{
			case 0:
			{
				accept = 0;
				break;
			}
			case 1:
			{
				accept = 1;
				break;
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		if (accept > 0)
		{
			// STR_MSG_ITEM_REIDENTIFY_APPLY_YES
			// PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401910));
		}
		else
		{
			// STR_MSG_ITEM_REIDENTIFY_APPLY_NO
			// PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401911));
		}
	}
}