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
package system.handlers.quest.cygnea;

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

public class _15203Dangerous_Wildlife extends QuestHandler
{
	private static final int questId = 15203;
	
	public _15203Dangerous_Wildlife()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804704).addOnQuestStart(questId);
		qe.registerQuestNpc(804704).addOnTalkEvent(questId);
		qe.registerQuestNpc(235829).addOnKillEvent(questId);
		qe.registerQuestNpc(235831).addOnKillEvent(questId);
		qe.registerQuestNpc(235851).addOnKillEvent(questId);
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
			if (targetId == 804704)
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
			if (targetId == 804704)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 5)
					{
						return sendQuestDialog(env, 2375);
					}
				}
				if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 5, 6, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804704)
			{
				if (env.getDialogId() == 1352)
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
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{
				final int targetId = env.getTargetId();
				final int var1 = qs.getQuestVarById(1);
				final int var2 = qs.getQuestVarById(2);
				final int var3 = qs.getQuestVarById(3);
				switch (targetId)
				{
					case 235829:
					{
						if (var1 < 4)
						{
							return defaultOnKillEvent(env, 235829, 0, 4, 1);
						}
						else if (var1 == 4)
						{
							if ((var2 == 3) && (var3 == 2))
							{
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else
							{
								return defaultOnKillEvent(env, 235829, 4, 5, 1);
							}
						}
						break;
					}
					case 235831:
					{
						if (var2 < 2)
						{
							return defaultOnKillEvent(env, 235831, 0, 2, 2);
						}
						else if (var2 == 2)
						{
							if ((var1 == 5) && (var3 == 2))
							{
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else
							{
								return defaultOnKillEvent(env, 235831, 2, 3, 2);
							}
						}
						break;
					}
					case 235851:
					{
						if (var3 < 1)
						{
							return defaultOnKillEvent(env, 235851, 0, 1, 3);
						}
						else if (var3 == 1)
						{
							if ((var1 == 5) && (var2 == 3))
							{
								qs.setQuestVar(1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return true;
							}
							else
							{
								return defaultOnKillEvent(env, 235851, 1, 2, 3);
							}
						}
						break;
					}
				}
			}
		}
		return false;
	}
}