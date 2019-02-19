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
package system.handlers.quest.baranath_dredgion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _3718Dredging_The_Dredgion extends QuestHandler
{
	private static final int questId = 3718;
	
	public _3718Dredging_The_Dredgion()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnDredgionReward(questId);
		qe.registerQuestNpc(279045).addOnQuestStart(questId);
		qe.registerQuestNpc(279045).addOnTalkEvent(questId);
		qe.registerQuestNpc(214814).addOnKillEvent(questId);
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
			if (targetId == 279045)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 279045)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					final int var1 = qs.getQuestVarById(1);
					final int var2 = qs.getQuestVarById(2);
					if (qs.getQuestVarById(0) == 0)
					{
						return sendQuestDialog(env, 1011);
					}
					else if ((var1 == 3) && (var2 == 8))
					{
						return sendQuestDialog(env, 10002);
					}
				}
				else if (dialog == QuestDialog.STEP_TO_1)
				{
					return defaultCloseDialog(env, 0, 1);
				}
				else if (dialog == QuestDialog.SELECT_REWARD)
				{
					return defaultCloseDialog(env, 1, 1, true, true);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 279045)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		return defaultOnKillEvent(env, 214814, 0, 8, 2);
	}
	
	@Override
	public boolean onDredgionRewardEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var1 = qs.getQuestVarById(1);
			if (var1 < 3)
			{
				changeQuestStep(env, var1, var1 + 1, false, 1);
				return true;
			}
		}
		return false;
	}
}