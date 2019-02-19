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
package system.handlers.quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _20505Ancient_Crystal extends QuestHandler
{
	public static final int questId = 20505;
	
	public _20505Ancient_Crystal()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			804732,
			804733,
			804734,
			804735
		};
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(219953).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20504, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 804732)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
					}
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804733)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804734)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case STEP_TO_3:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804735)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case STEP_TO_4:
					{
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804732)
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
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (targetId == 219953)
		{ // Beritra Research Corps Warmage.
			if (var == 4)
			{
				playQuestMovie(env, 863);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}