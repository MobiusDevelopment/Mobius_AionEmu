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
package system.handlers.quest.iluma;

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

public class _15534Scout_Nightbloom_Forest extends QuestHandler
{
	private static final int questId = 15534;
	
	public _15534Scout_Nightbloom_Forest()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806100).addOnQuestStart(questId);
		qe.registerQuestNpc(806100).addOnTalkEvent(questId);
		qe.registerQuestNpc(241646).addOnKillEvent(questId);
		qe.registerQuestNpc(241647).addOnKillEvent(questId);
		qe.registerQuestNpc(241648).addOnKillEvent(questId);
		qe.registerQuestNpc(241649).addOnKillEvent(questId);
		qe.registerQuestNpc(241650).addOnKillEvent(questId);
		qe.registerQuestNpc(241651).addOnKillEvent(questId);
		qe.registerQuestNpc(242487).addOnKillEvent(questId);
		qe.registerQuestNpc(242491).addOnKillEvent(questId);
		qe.registerQuestNpc(242495).addOnKillEvent(questId);
		qe.registerQuestNpc(242499).addOnKillEvent(questId);
		qe.registerQuestNpc(242503).addOnKillEvent(questId);
		qe.registerQuestNpc(242507).addOnKillEvent(questId);
		qe.registerQuestNpc(242511).addOnKillEvent(questId);
		qe.registerQuestNpc(242515).addOnKillEvent(questId);
		qe.registerQuestNpc(242519).addOnKillEvent(questId);
		qe.registerQuestNpc(242523).addOnKillEvent(questId);
		qe.registerQuestNpc(242527).addOnKillEvent(questId);
		qe.registerQuestNpc(242531).addOnKillEvent(questId);
		qe.registerQuestNpc(242535).addOnKillEvent(questId);
		qe.registerQuestNpc(242539).addOnKillEvent(questId);
		qe.registerQuestNpc(242543).addOnKillEvent(questId);
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
			if (targetId == 806100)
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
			if (targetId == 806100)
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
			if (targetId == 806100)
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
				case 241646:
				case 241647:
				case 241648:
				case 241649:
				case 241650:
				case 241651:
				case 242487:
				case 242491:
				case 242495:
				case 242499:
				case 242503:
				case 242507:
				case 242511:
				case 242515:
				case 242519:
				case 242523:
				case 242527:
				case 242531:
				case 242535:
				case 242539:
				case 242543:
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