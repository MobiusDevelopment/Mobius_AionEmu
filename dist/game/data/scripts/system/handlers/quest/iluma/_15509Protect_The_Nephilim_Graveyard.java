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

public class _15509Protect_The_Nephilim_Graveyard extends QuestHandler
{
	private static final int questId = 15509;
	
	public _15509Protect_The_Nephilim_Graveyard()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806092).addOnQuestStart(questId);
		qe.registerQuestNpc(806092).addOnTalkEvent(questId);
		qe.registerQuestNpc(241695).addOnKillEvent(questId);
		qe.registerQuestNpc(241696).addOnKillEvent(questId);
		qe.registerQuestNpc(241697).addOnKillEvent(questId);
		qe.registerQuestNpc(241698).addOnKillEvent(questId);
		qe.registerQuestNpc(241699).addOnKillEvent(questId);
		qe.registerQuestNpc(241700).addOnKillEvent(questId);
		qe.registerQuestNpc(241701).addOnKillEvent(questId);
		qe.registerQuestNpc(241702).addOnKillEvent(questId);
		qe.registerQuestNpc(241703).addOnKillEvent(questId);
		qe.registerQuestNpc(242607).addOnKillEvent(questId);
		qe.registerQuestNpc(242611).addOnKillEvent(questId);
		qe.registerQuestNpc(242615).addOnKillEvent(questId);
		qe.registerQuestNpc(242619).addOnKillEvent(questId);
		qe.registerQuestNpc(242623).addOnKillEvent(questId);
		qe.registerQuestNpc(242627).addOnKillEvent(questId);
		qe.registerQuestNpc(242631).addOnKillEvent(questId);
		qe.registerQuestNpc(242635).addOnKillEvent(questId);
		qe.registerQuestNpc(242639).addOnKillEvent(questId);
		qe.registerQuestNpc(242643).addOnKillEvent(questId);
		qe.registerQuestNpc(242647).addOnKillEvent(questId);
		qe.registerQuestNpc(242651).addOnKillEvent(questId);
		qe.registerQuestNpc(242655).addOnKillEvent(questId);
		qe.registerQuestNpc(242659).addOnKillEvent(questId);
		qe.registerQuestNpc(242663).addOnKillEvent(questId);
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
				case 241695:
				case 241696:
				case 241697:
				case 241698:
				case 241699:
				case 241700:
				case 241701:
				case 241702:
				case 241703:
				case 242607:
				case 242611:
				case 242615:
				case 242619:
				case 242623:
				case 242627:
				case 242631:
				case 242635:
				case 242639:
				case 242643:
				case 242647:
				case 242651:
				case 242655:
				case 242659:
				case 242663:
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