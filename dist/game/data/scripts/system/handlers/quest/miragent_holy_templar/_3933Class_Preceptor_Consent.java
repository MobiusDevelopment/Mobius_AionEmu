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

public class _3933Class_Preceptor_Consent extends QuestHandler
{
	private static final int questId = 3933;
	
	public _3933Class_Preceptor_Consent()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		final int[] npcs =
		{
			203704,
			203705,
			203706,
			203707,
			203752,
			203701,
			801214,
			801215
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
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
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
				case 203704:
					switch (dialog)
					{
						case START_DIALOG:
							return sendQuestDialog(env, 1011);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
				case 203705:
					if (var == 1)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 1352);
							case STEP_TO_2:
								return defaultCloseDialog(env, 1, 2);
						}
					}
				case 203706:
					if (var == 2)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 1693);
							case STEP_TO_3:
								return defaultCloseDialog(env, 2, 3);
						}
					}
				case 203707:
					if (var == 3)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2034);
							case STEP_TO_4:
								return defaultCloseDialog(env, 3, 4);
						}
					}
				case 801214:
					if (var == 4)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2375);
							case STEP_TO_5:
								return defaultCloseDialog(env, 4, 5);
						}
					}
				case 801215:
					if (var == 5)
					{
						switch (dialog)
						{
							case START_DIALOG:
								return sendQuestDialog(env, 2716);
							case STEP_TO_6:
								return defaultCloseDialog(env, 5, 6);
						}
					}
				case 203752:
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 6)
							{
								return sendQuestDialog(env, 3057);
							}
						}
						case SET_REWARD:
						{
							if (player.getInventory().getItemCountByItemId(186000080) >= 1)
							{
								removeQuestItem(env, 186000080, 1);
								return defaultCloseDialog(env, 6, 6, true, false, 0);
							}
							else
							{
								return sendQuestDialog(env, 3143);
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