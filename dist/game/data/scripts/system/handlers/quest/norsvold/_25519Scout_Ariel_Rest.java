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

public class _25519Scout_Ariel_Rest extends QuestHandler
{
	private static final int questId = 25519;
	
	public _25519Scout_Ariel_Rest()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806107).addOnQuestStart(questId);
		qe.registerQuestNpc(806107).addOnTalkEvent(questId);
		qe.registerQuestNpc(241743).addOnKillEvent(questId);
		qe.registerQuestNpc(241744).addOnKillEvent(questId);
		qe.registerQuestNpc(241745).addOnKillEvent(questId);
		qe.registerQuestNpc(241746).addOnKillEvent(questId);
		qe.registerQuestNpc(241747).addOnKillEvent(questId);
		qe.registerQuestNpc(241748).addOnKillEvent(questId);
		qe.registerQuestNpc(241749).addOnKillEvent(questId);
		qe.registerQuestNpc(241750).addOnKillEvent(questId);
		qe.registerQuestNpc(241751).addOnKillEvent(questId);
		qe.registerQuestNpc(242807).addOnKillEvent(questId);
		qe.registerQuestNpc(242811).addOnKillEvent(questId);
		qe.registerQuestNpc(242815).addOnKillEvent(questId);
		qe.registerQuestNpc(242819).addOnKillEvent(questId);
		qe.registerQuestNpc(242823).addOnKillEvent(questId);
		qe.registerQuestNpc(242827).addOnKillEvent(questId);
		qe.registerQuestNpc(242831).addOnKillEvent(questId);
		qe.registerQuestNpc(242835).addOnKillEvent(questId);
		qe.registerQuestNpc(242839).addOnKillEvent(questId);
		qe.registerQuestNpc(242843).addOnKillEvent(questId);
		qe.registerQuestNpc(242847).addOnKillEvent(questId);
		qe.registerQuestNpc(242851).addOnKillEvent(questId);
		qe.registerQuestNpc(242855).addOnKillEvent(questId);
		qe.registerQuestNpc(242859).addOnKillEvent(questId);
		qe.registerQuestNpc(242863).addOnKillEvent(questId);
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
			if (targetId == 806107)
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
			if (targetId == 806107)
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
			if (targetId == 806107)
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
				case 241743:
				case 241744:
				case 241745:
				case 241746:
				case 241747:
				case 241748:
				case 241749:
				case 241750:
				case 241751:
				case 242807:
				case 242811:
				case 242815:
				case 242819:
				case 242823:
				case 242827:
				case 242831:
				case 242835:
				case 242839:
				case 242843:
				case 242847:
				case 242851:
				case 242855:
				case 242859:
				case 242863:
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