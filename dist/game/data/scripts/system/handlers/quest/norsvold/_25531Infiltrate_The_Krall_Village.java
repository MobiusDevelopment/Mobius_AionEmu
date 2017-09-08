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
public class _25531Infiltrate_The_Krall_Village extends QuestHandler
{
	private static final int questId = 25531;
	
	public _25531Infiltrate_The_Krall_Village()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806111).addOnQuestStart(questId);
		qe.registerQuestNpc(806111).addOnTalkEvent(questId);
		qe.registerQuestNpc(241254).addOnKillEvent(questId);
		qe.registerQuestNpc(241255).addOnKillEvent(questId);
		qe.registerQuestNpc(241764).addOnKillEvent(questId);
		qe.registerQuestNpc(241765).addOnKillEvent(questId);
		qe.registerQuestNpc(241766).addOnKillEvent(questId);
		qe.registerQuestNpc(241800).addOnKillEvent(questId);
		qe.registerQuestNpc(241801).addOnKillEvent(questId);
		qe.registerQuestNpc(241802).addOnKillEvent(questId);
		qe.registerQuestNpc(243127).addOnKillEvent(questId);
		qe.registerQuestNpc(243131).addOnKillEvent(questId);
		qe.registerQuestNpc(243135).addOnKillEvent(questId);
		qe.registerQuestNpc(243139).addOnKillEvent(questId);
		qe.registerQuestNpc(243143).addOnKillEvent(questId);
		qe.registerQuestNpc(243147).addOnKillEvent(questId);
		qe.registerQuestNpc(243151).addOnKillEvent(questId);
		qe.registerQuestNpc(243155).addOnKillEvent(questId);
		qe.registerQuestNpc(243159).addOnKillEvent(questId);
		qe.registerQuestNpc(243163).addOnKillEvent(questId);
		qe.registerQuestNpc(243167).addOnKillEvent(questId);
		qe.registerQuestNpc(243171).addOnKillEvent(questId);
		qe.registerQuestNpc(243175).addOnKillEvent(questId);
		qe.registerQuestNpc(243179).addOnKillEvent(questId);
		qe.registerQuestNpc(243183).addOnKillEvent(questId);
		qe.registerQuestNpc(243187).addOnKillEvent(questId);
		qe.registerQuestNpc(243191).addOnKillEvent(questId);
		qe.registerQuestNpc(243195).addOnKillEvent(questId);
		qe.registerQuestNpc(243199).addOnKillEvent(questId);
		qe.registerQuestNpc(243203).addOnKillEvent(questId);
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
			if (targetId == 806111)
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
			if (targetId == 806111)
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
			if (targetId == 806111)
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
				case 241254:
				case 241255:
				case 241764:
				case 241765:
				case 241766:
				case 241800:
				case 241801:
				case 241802:
				case 243127:
				case 243131:
				case 243135:
				case 243139:
				case 243143:
				case 243147:
				case 243151:
				case 243155:
				case 243159:
				case 243163:
				case 243167:
				case 243171:
				case 243175:
				case 243179:
				case 243183:
				case 243187:
				case 243191:
				case 243195:
				case 243199:
				case 243203:
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