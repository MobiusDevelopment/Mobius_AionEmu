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

/**
 * @author Rinzler (Encom)
 */
public class _14150Secrets_Of_The_Superus extends QuestHandler
{
	private static final int questId = 14150;
	
	public _14150Secrets_Of_The_Superus()
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
		qe.registerQuestItem(182215458, questId);
		qe.registerQuestNpc(204501).addOnQuestStart(questId);// Sarantus
		qe.registerQuestNpc(204501).addOnTalkEvent(questId); // Sarantus
		qe.registerQuestNpc(204582).addOnTalkEvent(questId); // Ibelia
		qe.registerQuestNpc(700217).addOnTalkEvent(questId); // Engraved Stone Tablet
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.NONE)
		{
			if (targetId == 204501) // Sarantus
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 204501) // Sarantus
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case CHECK_COLLECTED_ITEMS_SIMPLE:
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						removeQuestItem(env, 182215458, 1);
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 204582) // Ibelia
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case STEP_TO_1:
					{
						if (var == 0)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
					}
				}
			}
			else if ((targetId == 700217) && (qs.getQuestVarById(0) == 1)) // Engraved Stone Tablet
			{
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						giveQuestItem(env, 182215458, 1);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204501) // Sarantus
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}