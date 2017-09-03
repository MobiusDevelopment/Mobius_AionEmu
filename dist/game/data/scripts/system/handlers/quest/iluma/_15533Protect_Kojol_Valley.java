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

public class _15533Protect_Kojol_Valley extends QuestHandler
{
	private static final int questId = 15533;
	
	public _15533Protect_Kojol_Valley()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806100).addOnQuestStart(questId);
		qe.registerQuestNpc(806100).addOnTalkEvent(questId);
		qe.registerQuestNpc(241815).addOnKillEvent(questId);
		qe.registerQuestNpc(241816).addOnKillEvent(questId);
		qe.registerQuestNpc(241817).addOnKillEvent(questId);
		qe.registerQuestNpc(241818).addOnKillEvent(questId);
		qe.registerQuestNpc(241819).addOnKillEvent(questId);
		qe.registerQuestNpc(241820).addOnKillEvent(questId);
		qe.registerQuestNpc(241821).addOnKillEvent(questId);
		qe.registerQuestNpc(241822).addOnKillEvent(questId);
		qe.registerQuestNpc(241823).addOnKillEvent(questId);
		qe.registerQuestNpc(243207).addOnKillEvent(questId);
		qe.registerQuestNpc(243211).addOnKillEvent(questId);
		qe.registerQuestNpc(243215).addOnKillEvent(questId);
		qe.registerQuestNpc(243219).addOnKillEvent(questId);
		qe.registerQuestNpc(243223).addOnKillEvent(questId);
		qe.registerQuestNpc(243227).addOnKillEvent(questId);
		qe.registerQuestNpc(243231).addOnKillEvent(questId);
		qe.registerQuestNpc(243235).addOnKillEvent(questId);
		qe.registerQuestNpc(243239).addOnKillEvent(questId);
		qe.registerQuestNpc(243243).addOnKillEvent(questId);
		qe.registerQuestNpc(243247).addOnKillEvent(questId);
		qe.registerQuestNpc(243251).addOnKillEvent(questId);
		qe.registerQuestNpc(243255).addOnKillEvent(questId);
		qe.registerQuestNpc(243259).addOnKillEvent(questId);
		qe.registerQuestNpc(243263).addOnKillEvent(questId);
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
				case 241815:
				case 241816:
				case 241817:
				case 241818:
				case 241819:
				case 241820:
				case 241821:
				case 241822:
				case 241823:
				case 243207:
				case 243211:
				case 243215:
				case 243219:
				case 243223:
				case 243227:
				case 243231:
				case 243235:
				case 243239:
				case 243243:
				case 243247:
				case 243251:
				case 243255:
				case 243259:
				case 243263:
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