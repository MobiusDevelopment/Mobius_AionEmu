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
package system.handlers.quest.mission;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _4542The_Secret_Of_The_Seiren_Treasure extends QuestHandler
{
	private static final int questId = 4542;
	
	public _4542The_Secret_Of_The_Seiren_Treasure()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204768).addOnQuestStart(questId);
		qe.registerQuestNpc(204768).addOnTalkEvent(questId);
		qe.registerQuestNpc(204743).addOnTalkEvent(questId);
		qe.registerQuestNpc(204808).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204768)
			{
				switch (dialog)
				{
					case START_DIALOG:
						return sendQuestDialog(env, 4762);
					case ACCEPT_QUEST_SIMPLE:
						giveQuestItem(env, 182215327, 1);
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 204743:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							if ((var == 6) && (player.getInventory().getItemCountByItemId(182215330) >= 1))
							{
								return sendQuestDialog(env, 3057);
							}
						case STEP_TO_1:
							if (var == 0)
							{
								giveQuestItem(env, 182215328, 1);
								removeQuestItem(env, 182215327, 1);
								return defaultCloseDialog(env, 0, 1);
							}
						case STEP_TO_3:
							return defaultCloseDialog(env, 2, 3);
						case SELECT_REWARD:
						{
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 6, 6, true, true);
						}
						case SELECT_ACTION_3143:
							return sendQuestDialog(env, 3143);
						case STEP_TO_7:
						{
							playQuestMovie(env, 239);
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 6, 6, true, true);
						}
					}
					break;
				case 204768:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
							else if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
						case STEP_TO_2:
							removeQuestItem(env, 182215328, 1);
							playQuestMovie(env, 239);
							return defaultCloseDialog(env, 1, 2);
						case SELECT_REWARD:
							removeQuestItem(env, 182215330, 1);
							return defaultCloseDialog(env, 5, 5, true, false);
					}
					break;
				case 204808:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
							if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
							if (var == 4)
							{
								return sendQuestDialog(env, 2376);
							}
						case STEP_TO_3:
							if (var == 2)
							{
								playQuestMovie(env, 240);
								return defaultCloseDialog(env, 2, 3);
							}
						case CHECK_COLLECTED_ITEMS:
							return checkQuestItems(env, 3, 4, false, 10000, 10001);
						case STEP_TO_5:
							return defaultCloseDialog(env, 4, 5, false, false, 182215330, 1, 0, 0);
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204768)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}