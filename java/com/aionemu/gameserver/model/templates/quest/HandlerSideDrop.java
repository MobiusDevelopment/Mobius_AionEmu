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
package com.aionemu.gameserver.model.templates.quest;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.templates.QuestTemplate;

public class HandlerSideDrop extends QuestDrop
{
	private final int neededAmount;
	
	public HandlerSideDrop(int questId, int npcId, int itemId, int amount, int chance)
	{
		this.questId = questId;
		this.npcId = npcId;
		this.itemId = itemId;
		this.chance = chance;
		
		final QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
		for (final QuestDrop drop : template.getQuestDrop())
		{
			if ((drop.npcId == npcId) && (drop.itemId == itemId))
			{
				dropEachMember = drop.dropEachMember;
				break;
			}
		}
		neededAmount = amount;
	}
	
	public HandlerSideDrop(int questId, int npcId, int itemId, int amount, int chance, int step)
	{
		this(questId, npcId, itemId, amount, chance);
		collecting_step = step;
	}
	
	public int getNeededAmount()
	{
		return neededAmount;
	}
}