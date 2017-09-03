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

public class _15515Scouting_A_War_Zone extends QuestHandler
{
	private static final int questId = 15515;
	
	public _15515Scouting_A_War_Zone()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806094).addOnQuestStart(questId);
		qe.registerQuestNpc(806094).addOnTalkEvent(questId);
		qe.registerQuestNpc(241725).addOnKillEvent(questId);
		qe.registerQuestNpc(241726).addOnKillEvent(questId);
		qe.registerQuestNpc(241727).addOnKillEvent(questId);
		qe.registerQuestNpc(241728).addOnKillEvent(questId);
		qe.registerQuestNpc(241729).addOnKillEvent(questId);
		qe.registerQuestNpc(241730).addOnKillEvent(questId);
		qe.registerQuestNpc(241731).addOnKillEvent(questId);
		qe.registerQuestNpc(241732).addOnKillEvent(questId);
		qe.registerQuestNpc(241733).addOnKillEvent(questId);
		qe.registerQuestNpc(241734).addOnKillEvent(questId);
		qe.registerQuestNpc(241735).addOnKillEvent(questId);
		qe.registerQuestNpc(241736).addOnKillEvent(questId);
		qe.registerQuestNpc(242743).addOnKillEvent(questId);
		qe.registerQuestNpc(241744).addOnKillEvent(questId);
		qe.registerQuestNpc(241745).addOnKillEvent(questId);
		qe.registerQuestNpc(241746).addOnKillEvent(questId);
		qe.registerQuestNpc(242747).addOnKillEvent(questId);
		qe.registerQuestNpc(242751).addOnKillEvent(questId);
		qe.registerQuestNpc(242755).addOnKillEvent(questId);
		qe.registerQuestNpc(242759).addOnKillEvent(questId);
		qe.registerQuestNpc(242763).addOnKillEvent(questId);
		qe.registerQuestNpc(242767).addOnKillEvent(questId);
		qe.registerQuestNpc(242771).addOnKillEvent(questId);
		qe.registerQuestNpc(242775).addOnKillEvent(questId);
		qe.registerQuestNpc(242779).addOnKillEvent(questId);
		qe.registerQuestNpc(242783).addOnKillEvent(questId);
		qe.registerQuestNpc(242787).addOnKillEvent(questId);
		qe.registerQuestNpc(242791).addOnKillEvent(questId);
		qe.registerQuestNpc(242795).addOnKillEvent(questId);
		qe.registerQuestNpc(242799).addOnKillEvent(questId);
		qe.registerQuestNpc(242803).addOnKillEvent(questId);
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
			if (targetId == 806094)
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
			if (targetId == 806094)
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
			if (targetId == 806094)
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
				case 241725:
				case 241726:
				case 241727:
				case 241728:
				case 241729:
				case 241730:
				case 241731:
				case 241732:
				case 241733:
				case 241734:
				case 241735:
				case 241736:
				case 242743:
				case 241744:
				case 241745:
				case 242746:
				case 242747:
				case 242751:
				case 242755:
				case 242759:
				case 242763:
				case 242767:
				case 242771:
				case 242775:
				case 242779:
				case 242783:
				case 242787:
				case 242791:
				case 242795:
				case 242799:
				case 242803:
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