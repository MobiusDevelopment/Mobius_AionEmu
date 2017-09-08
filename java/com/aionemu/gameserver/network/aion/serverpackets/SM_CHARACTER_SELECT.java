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

import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author cura
 */
public class SM_CHARACTER_SELECT extends AionServerPacket
{
	
	private final int type; // 0: new passkey input window, 1: passkey input window, 2: message window
	private int messageType; // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
	private int wrongCount;
	
	public SM_CHARACTER_SELECT(int type)
	{
		this.type = type;
	}
	
	public SM_CHARACTER_SELECT(int type, int messageType, int wrongCount)
	{
		this.type = type;
		this.messageType = messageType;
		this.wrongCount = wrongCount;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(type);
		
		switch (type)
		{
			case 0:
			{
				break;
			}
			case 1:
			{
				break;
			}
			case 2:
			{
				writeH(messageType); // 0: newpasskey complete, 2: passkey edit complete, 3: passkey input
				writeC(wrongCount > 0 ? 1 : 0); // 0: right passkey, 1: wrong passkey
				writeD(wrongCount); // wrong passkey input count
				writeD(SecurityConfig.PASSKEY_WRONG_MAXCOUNT);
				// server default value: 5)
				break;
			}
		}
	}
}
