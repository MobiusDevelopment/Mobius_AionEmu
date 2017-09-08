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
package system.handlers.quest.morheim;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _2345Orashunerk_Special_Order extends QuestHandler
{
	private static final int questId = 2345;
	
	int rewardIndex;
	
	public _2345Orashunerk_Special_Order()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798084).addOnQuestStart(questId);
		qe.registerQuestNpc(798084).addOnTalkEvent(questId);
		qe.registerQuestNpc(700238).addOnTalkEvent(questId);
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
			if (targetId == 798084)
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
			final int var = qs.getQuestVarById(0);
			if (targetId == 798084)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (var == 0)
					{
						return sendQuestDialog(env, 1011);
					}
					else if (var == 1)
					{
						return sendQuestDialog(env, 1352);
					}
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS)
				{
					return checkQuestItems(env, 0, 1, false, 10000, 10001);
				}
				else if (dialog == QuestDialog.SELECT_ACTION_1353)
				{
					return sendQuestDialog(env, 1353);
				}
				else if (dialog == QuestDialog.SELECT_ACTION_1438)
				{
					return sendQuestDialog(env, 1438);
				}
				else if (dialog == QuestDialog.STEP_TO_10)
				{
					giveQuestItem(env, 182204137, 1);
					changeQuestStep(env, 1, 10, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
				else if (dialog == QuestDialog.STEP_TO_20)
				{
					giveQuestItem(env, 182204138, 1);
					changeQuestStep(env, 1, 20, false);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}
			else if ((targetId == 700238) && (player.getInventory().getItemCountByItemId(182204136) < 3))
			{
				return true;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798084)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					if (qs.getQuestVarById(0) == 10)
					{
						removeQuestItem(env, 182204137, 1);
						return sendQuestDialog(env, 1693);
					}
					else if (qs.getQuestVarById(0) == 20)
					{
						rewardIndex = 1;
						removeQuestItem(env, 182204138, 1);
						return sendQuestDialog(env, 2034);
					}
				}
				return sendQuestEndDialog(env, rewardIndex);
			}
		}
		return false;
	}
}