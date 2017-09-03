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

public class _15510Behind_Enemy_Lines extends QuestHandler
{
	private static final int questId = 15510;
	
	public _15510Behind_Enemy_Lines()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806092).addOnQuestStart(questId);
		qe.registerQuestNpc(806092).addOnTalkEvent(questId);
		qe.registerQuestNpc(241535).addOnKillEvent(questId);
		qe.registerQuestNpc(241536).addOnKillEvent(questId);
		qe.registerQuestNpc(241537).addOnKillEvent(questId);
		qe.registerQuestNpc(241538).addOnKillEvent(questId);
		qe.registerQuestNpc(241539).addOnKillEvent(questId);
		qe.registerQuestNpc(241540).addOnKillEvent(questId);
		qe.registerQuestNpc(241541).addOnKillEvent(questId);
		qe.registerQuestNpc(241542).addOnKillEvent(questId);
		qe.registerQuestNpc(241543).addOnKillEvent(questId);
		qe.registerQuestNpc(241887).addOnKillEvent(questId);
		qe.registerQuestNpc(241891).addOnKillEvent(questId);
		qe.registerQuestNpc(241895).addOnKillEvent(questId);
		qe.registerQuestNpc(241899).addOnKillEvent(questId);
		qe.registerQuestNpc(241903).addOnKillEvent(questId);
		qe.registerQuestNpc(241907).addOnKillEvent(questId);
		qe.registerQuestNpc(241911).addOnKillEvent(questId);
		qe.registerQuestNpc(241915).addOnKillEvent(questId);
		qe.registerQuestNpc(241919).addOnKillEvent(questId);
		qe.registerQuestNpc(241923).addOnKillEvent(questId);
		qe.registerQuestNpc(241927).addOnKillEvent(questId);
		qe.registerQuestNpc(241931).addOnKillEvent(questId);
		qe.registerQuestNpc(241935).addOnKillEvent(questId);
		qe.registerQuestNpc(241939).addOnKillEvent(questId);
		qe.registerQuestNpc(241943).addOnKillEvent(questId);
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
			if (targetId == 806092)
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
			if (targetId == 806092)
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
			if (targetId == 806092)
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
				case 241535:
				case 241536:
				case 241537:
				case 241538:
				case 241539:
				case 241540:
				case 241541:
				case 241542:
				case 241543:
				case 241887:
				case 241891:
				case 241895:
				case 241899:
				case 241903:
				case 241907:
				case 241911:
				case 241915:
				case 241919:
				case 241923:
				case 241927:
				case 241931:
				case 241935:
				case 241939:
				case 241943:
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