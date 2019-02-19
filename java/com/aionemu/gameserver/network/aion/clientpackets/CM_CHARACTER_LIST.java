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

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ACCOUNT_PROPERTIES;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CHARACTER_LIST;

/**
 * In this packets aion client is requesting character list.
 * @author -Nemesiss-
 */
public class CM_CHARACTER_LIST extends AionClientPacket
{
	private int playOk2;
	
	public CM_CHARACTER_LIST(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		playOk2 = readD();
	}
	
	@Override
	protected void runImpl()
	{
		sendPacket(new SM_CHARACTER_LIST(0, playOk2));
		sendPacket(new SM_CHARACTER_LIST(2, playOk2));
		final boolean isGM = (getConnection()).getAccount().getAccessLevel() >= AdminConfig.GM_PANEL;
		sendPacket(new SM_ACCOUNT_PROPERTIES(isGM));
	}
}