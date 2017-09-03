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
package system.handlers.quest.miragent_holy_templar;

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

public class _3938Well_Rounded extends QuestHandler
{
	private static final int questId = 3938;
	
	public _3938Well_Rounded()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 3937, true);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		final int[] npcs =
		{
			203788,
			203792,
			203790,
			203793,
			203784,
			203786,
			798316,
			203752,
			203701
		};
		qe.registerQuestNpc(203701).addOnQuestStart(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
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
			if (targetId == 203701)
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
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203701:
					if (var == 0)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 1011);
							case STEP_TO_1:
								return defaultCloseDialog(env, 0, 1);
							case STEP_TO_2:
								return defaultCloseDialog(env, 0, 2);
							case STEP_TO_3:
								return defaultCloseDialog(env, 0, 3);
							case STEP_TO_4:
								return defaultCloseDialog(env, 0, 4);
							case STEP_TO_5:
								return defaultCloseDialog(env, 0, 5);
							case STEP_TO_6:
								return defaultCloseDialog(env, 0, 6);
						}
					}
					break;
				case 203788:
					if (var == 1)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 1352);
							case STEP_TO_7:
								return defaultCloseDialog(env, 1, 7, 152201596, 1, 0, 0);
						}
					}
					break;
				case 203792:
					if (var == 2)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 1693);
							case STEP_TO_7:
								return defaultCloseDialog(env, 2, 7, 152201639, 1, 0, 0);
						}
					}
					break;
				case 203790:
					if (var == 3)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2034);
							case STEP_TO_7:
								return defaultCloseDialog(env, 3, 7, 152201615, 1, 0, 0);
						}
					}
					break;
				case 203793:
					if (var == 4)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2375);
							case STEP_TO_7:
								return defaultCloseDialog(env, 4, 7, 152201632, 1, 0, 0);
						}
					}
					break;
				case 203784:
					if (var == 5)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2716);
							case STEP_TO_7:
								return defaultCloseDialog(env, 5, 7, 152201644, 1, 0, 0);
						}
					}
					break;
				case 203786:
					if (var == 6)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 3057);
							case STEP_TO_7:
								return defaultCloseDialog(env, 6, 7, 152201643, 1, 0, 0);
						}
					}
					break;
				case 798316:
					if (var == 7)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 3398);
							case CHECK_COLLECTED_ITEMS:
								return checkItemExistence(env, 7, 8, false, 186000077, 1, true, 10000, 10001, 0, 0);
						}
					}
					break;
				case 203752:
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 8)
							{
								return sendQuestDialog(env, 3739);
							}
						}
						case SET_REWARD:
						{
							if (player.getInventory().getItemCountByItemId(186000081) >= 1)
							{
								removeQuestItem(env, 186000081, 1);
								return defaultCloseDialog(env, 8, 8, true, false, 0);
							}
							else
							{
								return sendQuestDialog(env, 3825);
							}
						}
						case FINISH_DIALOG:
						{
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				default:
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203701)
			{
				if (dialog == QuestDialog.USE_OBJECT)
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
}