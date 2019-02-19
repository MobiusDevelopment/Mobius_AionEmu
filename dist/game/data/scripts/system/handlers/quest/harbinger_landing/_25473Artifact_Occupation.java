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
package system.handlers.quest.harbinger_landing;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _25473Artifact_Occupation extends QuestHandler
{
	private static final int questId = 25473;
	
	public _25473Artifact_Occupation()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805815).addOnQuestStart(questId);
		qe.registerQuestNpc(805815).addOnTalkEvent(questId);
		qe.registerQuestNpc(883052).addOnKillEvent(questId);
		qe.registerQuestNpc(883058).addOnKillEvent(questId);
		qe.registerQuestNpc(883064).addOnKillEvent(questId);
		qe.registerQuestNpc(883070).addOnKillEvent(questId);
		qe.registerQuestNpc(883220).addOnKillEvent(questId);
		qe.registerQuestNpc(883232).addOnKillEvent(questId);
		qe.registerQuestNpc(883244).addOnKillEvent(questId);
		qe.registerQuestNpc(883256).addOnKillEvent(questId);
		qe.registerQuestNpc(883222).addOnKillEvent(questId);
		qe.registerQuestNpc(883234).addOnKillEvent(questId);
		qe.registerQuestNpc(883246).addOnKillEvent(questId);
		qe.registerQuestNpc(883258).addOnKillEvent(questId);
		qe.registerQuestNpc(883054).addOnKillEvent(questId);
		qe.registerQuestNpc(883060).addOnKillEvent(questId);
		qe.registerQuestNpc(883066).addOnKillEvent(questId);
		qe.registerQuestNpc(883072).addOnKillEvent(questId);
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
			if (targetId == 805815)
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
			if (targetId == 805815)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 1)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 1, 2, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805815)
			{
				if (env.getDialogId() == 1352)
				{
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
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
				case 883052:
				case 883058:
				case 883064:
				case 883070:
				case 883220:
				case 883232:
				case 883244:
				case 883256:
				case 883222:
				case 883234:
				case 883246:
				case 883258:
				case 883054:
				case 883060:
				case 883066:
				case 883072:
				{
					if (qs.getQuestVarById(1) < 1)
					{
						qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
						updateQuestStatus(env);
					}
					if (qs.getQuestVarById(1) >= 1)
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