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

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.skill.PlayerSkillEntry;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

public class SM_SKILL_LIST extends AionServerPacket
{
	private final PlayerSkillEntry[] skillList;
	private final int messageId;
	private int skillNameId;
	private String skillLvl;
	public static final int YOU_LEARNED_SKILL = 1300050;
	boolean isNew = false;
	private Player player;
	private int state;
	
	public SM_SKILL_LIST(Player player, PlayerSkillEntry[] basicSkills)
	{
		this.player = player;
		skillList = player.getSkillList().getBasicSkills();
		messageId = 0;
	}
	
	public SM_SKILL_LIST(Player player, PlayerSkillEntry[] linkedSkills, int state)
	{
		this.player = player;
		skillList = player.getSkillList().getLinkedSkills();
		this.state = state;
		messageId = 0;
		isNew = true;
	}
	
	public SM_SKILL_LIST(Player player, PlayerSkillEntry stigmaSkill)
	{
		skillList = new PlayerSkillEntry[]
		{
			stigmaSkill
		};
		messageId = 0;
	}
	
	public SM_SKILL_LIST(PlayerSkillEntry skillListEntry, int messageId, boolean isNew)
	{
		skillList = new PlayerSkillEntry[]
		{
			skillListEntry
		};
		this.messageId = messageId;
		skillNameId = DataManager.SKILL_DATA.getSkillTemplate(skillListEntry.getSkillId()).getNameId();
		skillLvl = String.valueOf(skillListEntry.getSkillLevel());
		this.isNew = isNew;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final int size = skillList.length;
		writeH(size);
		if (isNew)
		{
			writeC(0);
		}
		else
		{
			writeC(1);
		}
		if (size > 0)
		{
			for (final PlayerSkillEntry entry : skillList)
			{
				writeH(entry.getSkillId());
				writeH(entry.getSkillLevel());
				writeC(0x00);
				final int extraLevel = entry.getExtraLvl();
				writeC(extraLevel);
				if (isNew && (extraLevel == 0) && !entry.isStigma())
				{
					writeD((int) (System.currentTimeMillis() / 1000));
				}
				else
				{
					writeD(0);
				}
				if (entry.isStigma())
				{
					writeC(1);
				}
				else if (entry.isLinked())
				{
					writeC(3);
				}
				else
				{
					writeC(0);
				}
			}
		}
		writeD(messageId);
		if (messageId != 0)
		{
			writeH(0x24);
			writeD(skillNameId);
			writeH(0x00);
			writeS(skillLvl);
		}
	}
}