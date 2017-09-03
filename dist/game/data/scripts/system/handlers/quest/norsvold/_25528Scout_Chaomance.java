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
package system.handlers.quest.norsvold;

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

public class _25528Scout_Chaomance extends QuestHandler
{
	private static final int questId = 25528;
	
	public _25528Scout_Chaomance()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806110).addOnQuestStart(questId);
		qe.registerQuestNpc(806110).addOnTalkEvent(questId);
		qe.registerQuestNpc(241776).addOnKillEvent(questId);
		qe.registerQuestNpc(241777).addOnKillEvent(questId);
		qe.registerQuestNpc(241778).addOnKillEvent(questId);
		qe.registerQuestNpc(241779).addOnKillEvent(questId);
		qe.registerQuestNpc(241780).addOnKillEvent(questId);
		qe.registerQuestNpc(241781).addOnKillEvent(questId);
		qe.registerQuestNpc(241782).addOnKillEvent(questId);
		qe.registerQuestNpc(241783).addOnKillEvent(questId);
		qe.registerQuestNpc(241784).addOnKillEvent(questId);
		qe.registerQuestNpc(241785).addOnKillEvent(questId);
		qe.registerQuestNpc(241786).addOnKillEvent(questId);
		qe.registerQuestNpc(241787).addOnKillEvent(questId);
		qe.registerQuestNpc(243007).addOnKillEvent(questId);
		qe.registerQuestNpc(243011).addOnKillEvent(questId);
		qe.registerQuestNpc(243015).addOnKillEvent(questId);
		qe.registerQuestNpc(243019).addOnKillEvent(questId);
		qe.registerQuestNpc(243023).addOnKillEvent(questId);
		qe.registerQuestNpc(243027).addOnKillEvent(questId);
		qe.registerQuestNpc(243031).addOnKillEvent(questId);
		qe.registerQuestNpc(243035).addOnKillEvent(questId);
		qe.registerQuestNpc(243039).addOnKillEvent(questId);
		qe.registerQuestNpc(243043).addOnKillEvent(questId);
		qe.registerQuestNpc(243047).addOnKillEvent(questId);
		qe.registerQuestNpc(243051).addOnKillEvent(questId);
		qe.registerQuestNpc(243055).addOnKillEvent(questId);
		qe.registerQuestNpc(243059).addOnKillEvent(questId);
		qe.registerQuestNpc(243063).addOnKillEvent(questId);
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
			if (targetId == 806110)
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
			if (targetId == 806110)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 60)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 60, 61, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806110)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
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
				case 241776:
				case 241777:
				case 241778:
				case 241779:
				case 241780:
				case 241781:
				case 241782:
				case 241783:
				case 241784:
				case 241785:
				case 241786:
				case 241787:
				case 243007:
				case 243011:
				case 243015:
				case 243019:
				case 243023:
				case 243027:
				case 243031:
				case 243035:
				case 243039:
				case 243043:
				case 243047:
				case 243051:
				case 243055:
				case 243059:
				case 243063:
					if (qs.getQuestVarById(1) < 60)
					{
						qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
						updateQuestStatus(env);
					}
					if (qs.getQuestVarById(1) >= 60)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
			}
		}
		return false;
	}
}