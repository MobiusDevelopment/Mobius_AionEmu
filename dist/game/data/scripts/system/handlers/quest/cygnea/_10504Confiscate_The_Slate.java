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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rinzler (Encom)
 */
public class _10504Confiscate_The_Slate extends QuestHandler
{
	public static final int questId = 10504;
	
	public _10504Confiscate_The_Slate()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			804706,
			702671
		};
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(236253).addOnKillEvent(questId); // Beritra Trooper.
		qe.registerQuestNpc(236254).addOnKillEvent(questId); // Beritra Sentinel.
		qe.registerQuestNpc(236255).addOnKillEvent(questId); // Commander Nazracht.
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 10503, true);
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
			if (targetId == 804706)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 3)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case STEP_TO_1:
					{
						giveQuestItem(env, 182215606, 1);
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							removeQuestItem(env, 182215606, 1);
							changeQuestStep(env, 3, 4, true);
							return sendQuestDialog(env, 10000);
						}
						return sendQuestDialog(env, 10001);
					}
				}
			}
			if (targetId == 702671)
			{ // Cracked Oath Slate.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						giveQuestItem(env, 182215607, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804706)
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
			final int var = qs.getQuestVarById(0);
			if (var == 1)
			{
				final int[] mobs =
				{
					236253,
					236254
				}; // Beritra Trooper-Sentinel.
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 4))
				{
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 4)
				{
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return true;
				}
			}
			if (var == 2)
			{
				return defaultOnKillEvent(env, 236255, 2, 3); // Commander Nazracht.
			}
		}
		return false;
	}
}