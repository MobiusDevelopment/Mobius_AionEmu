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

public class _25512Protect_The_Canyon_Or_Else extends QuestHandler
{
	private static final int questId = 25512;
	
	public _25512Protect_The_Canyon_Or_Else()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806105).addOnQuestStart(questId);
		qe.registerQuestNpc(806105).addOnTalkEvent(questId);
		qe.registerQuestNpc(241547).addOnTalkEvent(questId);
		qe.registerQuestNpc(241548).addOnTalkEvent(questId);
		qe.registerQuestNpc(241549).addOnKillEvent(questId);
		qe.registerQuestNpc(241550).addOnTalkEvent(questId);
		qe.registerQuestNpc(241551).addOnTalkEvent(questId);
		qe.registerQuestNpc(241552).addOnKillEvent(questId);
		qe.registerQuestNpc(243269).addOnKillEvent(questId);
		qe.registerQuestNpc(243270).addOnKillEvent(questId);
		qe.registerQuestNpc(243271).addOnKillEvent(questId);
		qe.registerQuestNpc(241947).addOnKillEvent(questId);
		qe.registerQuestNpc(241951).addOnKillEvent(questId);
		qe.registerQuestNpc(241955).addOnKillEvent(questId);
		qe.registerQuestNpc(241959).addOnKillEvent(questId);
		qe.registerQuestNpc(241963).addOnKillEvent(questId);
		qe.registerQuestNpc(241967).addOnKillEvent(questId);
		qe.registerQuestNpc(241971).addOnKillEvent(questId);
		qe.registerQuestNpc(241975).addOnKillEvent(questId);
		qe.registerQuestNpc(241979).addOnKillEvent(questId);
		qe.registerQuestNpc(241983).addOnKillEvent(questId);
		qe.registerQuestNpc(241987).addOnKillEvent(questId);
		qe.registerQuestNpc(241991).addOnKillEvent(questId);
		qe.registerQuestNpc(241995).addOnKillEvent(questId);
		qe.registerQuestNpc(241999).addOnKillEvent(questId);
		qe.registerQuestNpc(242003).addOnKillEvent(questId);
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
			if (targetId == 806105)
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
			if (targetId == 806105)
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
			if (targetId == 806105)
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
				case 241547:
				case 241548:
				case 241549:
				case 241550:
				case 241551:
				case 241552:
				case 243269:
				case 243270:
				case 243271:
				case 241947:
				case 241951:
				case 241955:
				case 241959:
				case 241963:
				case 241967:
				case 241971:
				case 241975:
				case 241979:
				case 241983:
				case 241987:
				case 241991:
				case 241995:
				case 241999:
				case 242003:
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