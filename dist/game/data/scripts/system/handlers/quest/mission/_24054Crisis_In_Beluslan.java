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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) [702041 Baranath Silencer] /
 ****/

public class _24054Crisis_In_Beluslan extends QuestHandler
{
	private static final int questId = 24054;
	private static final int[] npcs =
	{
		204701,
		204702,
		802053
	};
	
	public _24054Crisis_In_Beluslan()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(700290).addOnKillEvent(questId); // Field Suppressor.
		qe.registerQuestNpc(233865).addOnKillEvent(questId); // Officer Bakuram.
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24053, true);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int targetId = env.getTargetId();
			switch (targetId)
			{
				case 700290:
				{ // Field Suppressor.
					return defaultOnKillEvent(env, 700290, 2, 5);
				}
				case 233865:
				{ // Officer Bakuram.
					return defaultOnKillEvent(env, 233865, 5, 6);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null)
		{
			return false;
		}
		final Npc target = (Npc) env.getVisibleObject();
		final int targetId = target.getNpcId();
		final int var = qs.getQuestVarById(0);
		final QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204702)
			{ // Nerita.
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204702:
				{ // Nerita.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					if (dialog == QuestDialog.STEP_TO_1)
					{
						return defaultCloseDialog(env, 0, 1);
					}
					break;
				}
				case 802053:
				{ // Fafner.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 1))
					{
						return sendQuestDialog(env, 1352);
					}
					if (dialog == QuestDialog.STEP_TO_2)
					{
						return defaultCloseDialog(env, 1, 2);
					}
					break;
				}
				case 204701:
				{ // Hod.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 6))
					{
						return sendQuestDialog(env, 2375);
					}
					else if (dialog == QuestDialog.SET_REWARD)
					{
						return defaultCloseDialog(env, 6, 6, true, false);
					}
					break;
				}
			}
		}
		return false;
	}
}