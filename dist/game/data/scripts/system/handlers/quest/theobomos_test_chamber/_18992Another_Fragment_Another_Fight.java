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
package system.handlers.quest.theobomos_test_chamber;

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

public class _18992Another_Fragment_Another_Fight extends QuestHandler
{
	private static final int questId = 18992;
	private static final int[] IDF6LapShelukSN67Ae =
	{
		220424
	}; // 피의 계약을 맺은 아라크네.
	private static final int[] IDF6LapPrincessSN67Ae =
	{
		220425
	}; // 피의 계약을 맺은 갈라테이아.
	private static final int[] IDF6LapGodElemental67Ah =
	{
		220426
	}; // 아티팩트를 지배하는 원소 군주.
	
	public _18992Another_Fragment_Another_Fight()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806075).addOnQuestStart(questId); // Weatha.
		qe.registerQuestNpc(806075).addOnTalkEvent(questId); // Weatha.
		qe.registerQuestNpc(806215).addOnTalkEvent(questId); // Stochio.
		for (final int mob : IDF6LapShelukSN67Ae)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (final int mob : IDF6LapPrincessSN67Ae)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (final int mob : IDF6LapGodElemental67Ah)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
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
			if (targetId == 806075)
			{ // Weatha.
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
			if (targetId == 806215)
			{ // Stochio.
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				if (dialog == QuestDialog.STEP_TO_1)
				{
					qs.setQuestVarById(0, 1);
					updateQuestStatus(env);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 806075)
			{ // Weatha.
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 3)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 2, 3, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806075)
			{ // Weatha.
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
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (var == 1)
		{
			if (targetId == 220424)
			{ // 피의 계약을 맺은 아라크네.
				qs.setQuestVarById(1, 1);
			}
			else if (targetId == 220425)
			{ // 피의 계약을 맺은 갈라테이아.
				qs.setQuestVarById(2, 1);
			}
			updateQuestStatus(env);
			if ((qs.getQuestVarById(1) == 1) && (qs.getQuestVarById(2) == 1))
			{
				changeQuestStep(env, 1, 2, false);
			}
		}
		else if (var == 2)
		{
			if (targetId == 220426)
			{ // 아티팩트를 지배하는 원소 군주.
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
			}
		}
		return false;
	}
}