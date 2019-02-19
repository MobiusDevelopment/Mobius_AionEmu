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
package system.handlers.quest.altgard;

import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author HellBoy
 */
public class _2230AFriendlyWager extends QuestHandler
{
	
	private static final int questId = 2230;
	
	public _2230AFriendlyWager()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(203621).addOnQuestStart(questId);
		qe.registerQuestNpc(203621).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final QuestState qs = env.getPlayer().getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (env.getTargetId() == 203621)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case ASK_ACCEPTION:
					{
						return sendQuestDialog(env, 4);
					}
					case ACCEPT_QUEST:
					{
						return sendQuestDialog(env, 1003);
					}
					case REFUSE_QUEST:
					{
						return sendQuestDialog(env, 1004);
					}
					case STEP_TO_1:
					{
						if (QuestService.startQuest(env))
						{
							QuestService.questTimerStart(env, 1800);
							return true;
						}
						return false;
					}
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			if (env.getTargetId() == 203621)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (var == 0)
						{
							if (QuestService.collectItemCheck(env, true))
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								QuestService.questTimerEnd(env);
								return sendQuestDialog(env, 5);
							}
							return sendQuestDialog(env, 2716);
						}
					}
				}
			}
		}
		return sendQuestRewardDialog(env, 203621, 0);
	}
}
