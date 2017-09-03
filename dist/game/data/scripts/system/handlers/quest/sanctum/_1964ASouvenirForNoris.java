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
package system.handlers.quest.sanctum;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _1964ASouvenirForNoris extends QuestHandler
{
	
	private static final int questId = 1964;
	
	public _1964ASouvenirForNoris()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			203726,
			203776
		};
		qe.registerQuestNpc(203726).addOnQuestStart(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		if (sendQuestNoneDialog(env, 203726, 182206033, 1))
		{
			return true;
		}
		
		final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			if (env.getTargetId() == 203776)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
						if (var == 0)
						{
							return sendQuestDialog(env, 1352);
						}
					case STEP_TO_1:
						return defaultCloseDialog(env, 0, 1, true, false, 0, 0, 0, 182206033, 1);
				}
			}
		}
		return sendQuestRewardDialog(env, 203726, 2375);
	}
}
