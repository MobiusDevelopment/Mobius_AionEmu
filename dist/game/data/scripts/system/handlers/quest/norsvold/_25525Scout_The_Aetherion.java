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

/**
 * @author Rinzler (Encom)
 */
public class _25525Scout_The_Aetherion extends QuestHandler
{
	private static final int questId = 25525;
	
	public _25525Scout_The_Aetherion()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806109).addOnQuestStart(questId);
		qe.registerQuestNpc(806109).addOnTalkEvent(questId);
		qe.registerQuestNpc(241767).addOnKillEvent(questId);
		qe.registerQuestNpc(241768).addOnKillEvent(questId);
		qe.registerQuestNpc(241769).addOnKillEvent(questId);
		qe.registerQuestNpc(241770).addOnKillEvent(questId);
		qe.registerQuestNpc(241771).addOnKillEvent(questId);
		qe.registerQuestNpc(241772).addOnKillEvent(questId);
		qe.registerQuestNpc(242927).addOnKillEvent(questId);
		qe.registerQuestNpc(242931).addOnKillEvent(questId);
		qe.registerQuestNpc(242935).addOnKillEvent(questId);
		qe.registerQuestNpc(242939).addOnKillEvent(questId);
		qe.registerQuestNpc(242943).addOnKillEvent(questId);
		qe.registerQuestNpc(242947).addOnKillEvent(questId);
		qe.registerQuestNpc(242951).addOnKillEvent(questId);
		qe.registerQuestNpc(242955).addOnKillEvent(questId);
		qe.registerQuestNpc(242959).addOnKillEvent(questId);
		qe.registerQuestNpc(242963).addOnKillEvent(questId);
		qe.registerQuestNpc(242967).addOnKillEvent(questId);
		qe.registerQuestNpc(242971).addOnKillEvent(questId);
		qe.registerQuestNpc(242975).addOnKillEvent(questId);
		qe.registerQuestNpc(242979).addOnKillEvent(questId);
		qe.registerQuestNpc(242983).addOnKillEvent(questId);
		qe.registerQuestNpc(242987).addOnKillEvent(questId);
		qe.registerQuestNpc(242991).addOnKillEvent(questId);
		qe.registerQuestNpc(242995).addOnKillEvent(questId);
		qe.registerQuestNpc(242999).addOnKillEvent(questId);
		qe.registerQuestNpc(243003).addOnKillEvent(questId);
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
			if (targetId == 806109)
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
			if (targetId == 806109)
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
			if (targetId == 806109)
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
				case 241767:
				case 241768:
				case 241769:
				case 241770:
				case 241771:
				case 241772:
				case 242927:
				case 242931:
				case 242935:
				case 242939:
				case 242943:
				case 242947:
				case 242951:
				case 242955:
				case 242959:
				case 242963:
				case 242967:
				case 242971:
				case 242975:
				case 242979:
				case 242983:
				case 242987:
				case 242991:
				case 242995:
				case 242999:
				case 243003:
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