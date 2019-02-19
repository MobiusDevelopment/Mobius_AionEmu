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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _14120The_Bucket_List extends QuestHandler
{
	private static final int questId = 14120;
	
	public _14120The_Bucket_List()
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
		qe.registerQuestItem(182215478, questId);
		qe.registerQuestNpc(203932).addOnQuestStart(questId); // Phomona
		qe.registerQuestNpc(203932).addOnTalkEvent(questId); // Phomona
		qe.registerQuestNpc(730020).addOnTalkEvent(questId); // Demro
		qe.registerQuestNpc(730019).addOnTalkEvent(questId); // Lodas
		qe.registerQuestNpc(700157).addOnTalkEvent(questId); // Kerubian Bucket
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203932) // Phomona.
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
			if (targetId == 730019) // Lodas
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (qs.getQuestVarById(0) == 1)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case CHECK_COLLECTED_ITEMS_SIMPLE:
					{
						return checkQuestItems(env, 1, 1, true, 5, 2716);
					}
					case FINISH_DIALOG:
					{
						return sendQuestSelectionDialog(env);
					}
				}
			}
			else if (targetId == 700157) // Kerubian Bucket
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return true;
				}
			}
			else if (targetId == 730020) // Demro.
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 0)
					{
						return sendQuestDialog(env, 1352);
					}
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					return defaultCloseDialog(env, 0, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 730019) // Lodas.
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}