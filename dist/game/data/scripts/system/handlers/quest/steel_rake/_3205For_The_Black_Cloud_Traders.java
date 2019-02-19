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
package system.handlers.quest.steel_rake;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _3205For_The_Black_Cloud_Traders extends QuestHandler
{
	private static final int questId = 3205;
	private static final int[] Petrahulk_Sentinel =
	{
		215049,
		219024
	};
	
	public _3205For_The_Black_Cloud_Traders()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(278651).addOnTalkEvent(questId);
		qe.registerQuestNpc(204535).addOnTalkEvent(questId);
		qe.registerQuestNpc(805835).addOnTalkEvent(questId);
		for (int mob : Petrahulk_Sentinel)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 278651)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 15)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 15, 16, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 204535)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 16)
						{
							return sendQuestDialog(env, 1694);
						}
					}
					case SET_REWARD:
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805835)
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
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 14))
				{
					return defaultOnKillEvent(env, Petrahulk_Sentinel, var1, var1 + 1, 1);
				}
				else if (var1 == 14)
				{
					qs.setQuestVar(15);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}