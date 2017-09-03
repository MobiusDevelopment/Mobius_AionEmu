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
package system.handlers.quest.mission;

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

public class _14012Dukaki_Mischief extends QuestHandler
{
	private static final int questId = 14012;
	
	public _14012Dukaki_Mischief()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] mobs =
		{
			210145,
			210146,
			210157,
			210740
		};
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203129).addOnTalkEvent(questId);
		qe.registerQuestNpc(203098).addOnTalkEvent(questId);
		for (int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 14011, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			final int var2 = qs.getQuestVarById(2);
			switch (targetId)
			{
				case (203129):
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if ((var == 1) && (var1 == 5) && (var2 == 3))
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_1:
						{
							if (var == 0)
							{
								return defaultCloseDialog(env, 0, 1);
							}
						}
						case STEP_TO_3:
						{
							if (var == 1)
							{
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return closeDialogWindow(env);
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203098)
			{
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
		final int targetId = env.getTargetId();
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 1)
			{
				final int[] dukaki =
				{
					210145,
					210146
				};
				final int[] tursin =
				{
					210157,
					210740
				};
				switch (targetId)
				{
					case 210145:
					case 210146:
					{
						return defaultOnKillEvent(env, dukaki, 0, 5, 1);
					}
					case 210157:
					case 210740:
					{
						return defaultOnKillEvent(env, tursin, 0, 3, 2);
					}
				}
			}
		}
		return false;
	}
}