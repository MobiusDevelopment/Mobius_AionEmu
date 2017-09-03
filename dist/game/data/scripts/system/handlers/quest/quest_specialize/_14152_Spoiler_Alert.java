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
package system.handlers.quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _14152_Spoiler_Alert extends QuestHandler
{
	private static final int questId = 14152;
	
	public _14152_Spoiler_Alert()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204504).addOnQuestStart(questId); // Sofne.
		qe.registerQuestNpc(204504).addOnTalkEvent(questId); // Sofne.
		qe.registerQuestNpc(204574).addOnTalkEvent(questId); // Finn.
		qe.registerQuestNpc(203705).addOnTalkEvent(questId); // Jumentis.
		qe.registerQuestNpc(212151).addOnKillEvent(questId); // Chairman Garnis.
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
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204504)
			{ // Sofne.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					{
						return sendQuestDialog(env, 4);
					}
					case REFUSE_QUEST:
					{
						QuestService.startQuest(env);
						qs.setQuestVarById(5, 1);
						updateQuestStatus(env);
						return sendQuestDialog(env, 1003);
					}
					case REFUSE_QUEST_2:
					{
						return sendQuestDialog(env, 1004);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 204574)
			{ // Finn.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1352);
					}
					case STEP_TO_1:
					{
						qs.setQuestVarById(5, 2);
						updateQuestStatus(env);
						giveQuestItem(env, 182215481, 1);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 203705)
			{ // Jumentis.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1693);
					}
					case STEP_TO_2:
					{
						removeQuestItem(env, 182215481, 1);
						qs.setQuestVarById(5, 0);
						qs.setQuestVarById(0, 0);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204504)
			{ // Sofne.
				if ((env.getDialog() == QuestDialog.USE_OBJECT) && (var == 1))
				{
					return sendQuestDialog(env, 2375);
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
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((targetId == 212151) && (var == 0))
		{ // Chairman Garnis.
			qs.setQuestVarById(0, 1);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}