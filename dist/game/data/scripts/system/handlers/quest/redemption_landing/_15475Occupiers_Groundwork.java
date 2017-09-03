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
package system.handlers.quest.redemption_landing;

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

public class _15475Occupiers_Groundwork extends QuestHandler
{
	private static final int questId = 15475;
	
	public _15475Occupiers_Groundwork()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805798).addOnQuestStart(questId);
		qe.registerQuestNpc(805798).addOnTalkEvent(questId);
		qe.registerQuestNpc(883190).addOnKillEvent(questId);
		qe.registerQuestNpc(883202).addOnKillEvent(questId);
		qe.registerQuestNpc(883214).addOnKillEvent(questId);
		qe.registerQuestNpc(883034).addOnKillEvent(questId);
		qe.registerQuestNpc(883040).addOnKillEvent(questId);
		qe.registerQuestNpc(883046).addOnKillEvent(questId);
		qe.registerQuestNpc(883192).addOnKillEvent(questId);
		qe.registerQuestNpc(883204).addOnKillEvent(questId);
		qe.registerQuestNpc(883216).addOnKillEvent(questId);
		qe.registerQuestNpc(883036).addOnKillEvent(questId);
		qe.registerQuestNpc(883042).addOnKillEvent(questId);
		qe.registerQuestNpc(883048).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 805798)
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
			if (targetId == 805798)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 1)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 1, 2, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805798)
			{
				if (env.getDialogId() == 1352)
				{
					return sendQuestDialog(env, 5);
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
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (env.getTargetId())
			{
				case 883190:
				case 883202:
				case 883214:
				case 883034:
				case 883040:
				case 883046:
				case 883192:
				case 883204:
				case 883216:
				case 883036:
				case 883042:
				case 883048:
					if (qs.getQuestVarById(1) < 1)
					{
						qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
						updateQuestStatus(env);
					}
					if (qs.getQuestVarById(1) >= 1)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
			}
		}
		return false;
	}
}