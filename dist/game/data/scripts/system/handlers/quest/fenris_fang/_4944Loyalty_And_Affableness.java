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
package system.handlers.quest.fenris_fang;

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

public class _4944Loyalty_And_Affableness extends QuestHandler
{
	private static final int questId = 4944;
	private static final int[] npcs =
	{
		204053,
		204075
	};
	private static final int[] mobs =
	{
		251002,
		251021,
		251018,
		251039,
		251033,
		251036,
		214823,
		216850
	};
	
	public _4944Loyalty_And_Affableness()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 4943, true);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (final int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			if (targetId == 204053)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVars().getQuestVars();
			switch (targetId)
			{
				case 204053:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 306)
							{
								return sendQuestDialog(env, 1693);
							}
							else if (var == 4)
							{
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 0, 6, false, 10000, 10001);
						}
						case FINISH_DIALOG:
						{
							return defaultCloseDialog(env, 0, 0);
						}
						case STEP_TO_3:
						{
							qs.setQuestVar(3);
							updateQuestStatus(env);
							return sendQuestSelectionDialog(env);
						}
						case STEP_TO_5:
						{
							return defaultCloseDialog(env, 4, 5);
						}
					}
					break;
				}
				case 204075:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
						}
						case SELECT_ACTION_2718:
						{
							if (player.getCommonData().getDp() >= 4000)
							{
								return checkItemExistence(env, 5, 5, false, 186000087, 1, true, 2718, 2887, 0, 0);
							}
							else
							{
								return sendQuestDialog(env, 2802);
							}
						}
						case SET_REWARD:
						{
							player.getCommonData().setDp(0);
							return defaultCloseDialog(env, 5, 5, true, false);
						}
						case FINISH_DIALOG:
						{
							return defaultCloseDialog(env, 5, 5);
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204053)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVars().getQuestVars();
			if ((var >= 6) && (var < 306))
			{
				final int[] npcids =
				{
					251002,
					251021,
					251018,
					251039,
					251033,
					251036
				};
				for (final int id : npcids)
				{
					if (targetId == id)
					{
						qs.setQuestVar(var + 1);
						updateQuestStatus(env);
						return true;
					}
				}
			}
			else if (var == 3)
			{
				final int[] npcids =
				{
					214823,
					216850
				};
				return defaultOnKillEvent(env, npcids, 3, 4);
			}
		}
		return false;
	}
}