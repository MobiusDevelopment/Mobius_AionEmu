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

/**
 * @author Rinzler (Encom)
 */
public class _15528Aetherspring_Tree extends QuestHandler
{
	private static final int questId = 15528;
	
	public _15528Aetherspring_Tree()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806098).addOnQuestStart(questId);
		qe.registerQuestNpc(806098).addOnTalkEvent(questId);
		qe.registerQuestNpc(241610).addOnKillEvent(questId);
		qe.registerQuestNpc(241611).addOnKillEvent(questId);
		qe.registerQuestNpc(241612).addOnKillEvent(questId);
		qe.registerQuestNpc(241613).addOnKillEvent(questId);
		qe.registerQuestNpc(241614).addOnKillEvent(questId);
		qe.registerQuestNpc(241615).addOnKillEvent(questId);
		qe.registerQuestNpc(241616).addOnKillEvent(questId);
		qe.registerQuestNpc(241617).addOnKillEvent(questId);
		qe.registerQuestNpc(241618).addOnKillEvent(questId);
		qe.registerQuestNpc(242287).addOnKillEvent(questId);
		qe.registerQuestNpc(242291).addOnKillEvent(questId);
		qe.registerQuestNpc(242295).addOnKillEvent(questId);
		qe.registerQuestNpc(242299).addOnKillEvent(questId);
		qe.registerQuestNpc(242303).addOnKillEvent(questId);
		qe.registerQuestNpc(242307).addOnKillEvent(questId);
		qe.registerQuestNpc(242311).addOnKillEvent(questId);
		qe.registerQuestNpc(242315).addOnKillEvent(questId);
		qe.registerQuestNpc(242319).addOnKillEvent(questId);
		qe.registerQuestNpc(242323).addOnKillEvent(questId);
		qe.registerQuestNpc(242327).addOnKillEvent(questId);
		qe.registerQuestNpc(242331).addOnKillEvent(questId);
		qe.registerQuestNpc(242335).addOnKillEvent(questId);
		qe.registerQuestNpc(242339).addOnKillEvent(questId);
		qe.registerQuestNpc(242343).addOnKillEvent(questId);
		qe.registerQuestNpc(243281).addOnKillEvent(questId);
		qe.registerQuestNpc(243282).addOnKillEvent(questId);
		qe.registerQuestNpc(243283).addOnKillEvent(questId);
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
			if (targetId == 806098)
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
			if (targetId == 806098)
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
			if (targetId == 806098)
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
				case 241610:
				case 241611:
				case 241612:
				case 241613:
				case 241614:
				case 241615:
				case 241616:
				case 241617:
				case 241618:
				case 242287:
				case 242291:
				case 242295:
				case 242299:
				case 242303:
				case 242307:
				case 242311:
				case 242315:
				case 242319:
				case 242323:
				case 242327:
				case 242331:
				case 242335:
				case 242339:
				case 242343:
				case 243281:
				case 243282:
				case 243283:
				{
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
		}
		return false;
	}
}