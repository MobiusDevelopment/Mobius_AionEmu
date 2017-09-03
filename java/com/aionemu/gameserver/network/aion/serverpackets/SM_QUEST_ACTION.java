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
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class SM_QUEST_ACTION extends AionServerPacket
{
	protected int questId;
	private int status;
	private int step;
	protected int action;
	private int timer;
	private int sharerId;
	@SuppressWarnings("unused")
	private boolean unk;
	
	SM_QUEST_ACTION()
	{
		
	}
	
	public SM_QUEST_ACTION(int questId, int status, int step)
	{
		action = 1;
		this.questId = questId;
		this.status = status;
		this.step = step;
	}
	
	public SM_QUEST_ACTION(int questId, QuestStatus status, int step)
	{
		action = 2;
		this.questId = questId;
		this.status = status.value();
		this.step = step;
	}
	
	public SM_QUEST_ACTION(int questId)
	{
		action = 3;
		this.questId = questId;
	}
	
	public SM_QUEST_ACTION(int questId, int timer)
	{
		action = 4;
		this.questId = questId;
		this.timer = timer;
		step = 0;
	}
	
	public SM_QUEST_ACTION(int questId, int sharerId, boolean unk)
	{
		action = 5;
		this.questId = questId;
		this.sharerId = sharerId;
		this.unk = unk;
	}
	
	public SM_QUEST_ACTION(int questId, boolean fake)
	{
		action = 6;
		this.questId = questId;
		timer = 0;
		step = 0;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(action);
		writeD(questId);
		switch (action)
		{
			case 1:
				writeC(status);
				writeC(0x0);
				writeD(step);
				writeH(0);
				writeC(0);
				break;
			case 2:
				writeC(status);
				writeC(0x0);
				writeD(step);
				writeH(0);
				break;
			case 3:
				writeD(0);
				break;
			case 4:
				writeD(timer);
				writeC(0x01);
				writeH(0x0);
				writeC(0x01);
				break;
			case 5:
				writeD(sharerId);
				writeD(0);
				break;
			case 6:
				writeH(0x01);
				writeH(0x0);
				break;
		}
	}
}