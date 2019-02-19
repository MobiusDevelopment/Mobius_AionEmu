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
package system.handlers.quest.esoterrace;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _28407The_Gathering extends QuestHandler
{
	private static final int questId = 28407;
	
	public _28407The_Gathering()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799557).addOnQuestStart(questId);
		qe.registerQuestNpc(799557).addOnTalkEvent(questId);
		qe.registerQuestNpc(730380).addOnTalkEvent(questId);
		qe.registerQuestNpc(799558).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 799557)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
			else if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 1))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_2)
				{
					return defaultCloseDialog(env, 1, 2);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 730380)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					return defaultCloseDialog(env, 0, 1, 182215016, 1, 0, 0);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 799558)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 2))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
			else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}