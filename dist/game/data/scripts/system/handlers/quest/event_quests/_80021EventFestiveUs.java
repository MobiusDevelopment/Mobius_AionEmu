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
package system.handlers.quest.event_quests;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rolandas
 */
public class _80021EventFestiveUs extends QuestHandler
{
	
	private static final int questId = 80021;
	private static final int[] npcs =
	{
		799784,
		799783,
		203618,
		203650
	};
	
	public _80021EventFestiveUs()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(799784).addOnQuestStart(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (((qs == null) || (qs.getStatus() == QuestStatus.NONE)) && !onLvlUpEvent(env))
		{
			return false;
		}
		
		final QuestTemplate template = DataManager.QUEST_DATA.getQuestById(env.getQuestId());
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || ((qs.getStatus() == QuestStatus.COMPLETE) && (qs.getCompleteCount() < template.getMaxRepeatCount())))
		{
			if (env.getTargetId() == 799784)
			{
				if ((env.getDialog() == QuestDialog.USE_OBJECT) || (env.getDialog() == QuestDialog.START_DIALOG))
				{
					return sendQuestDialog(env, 1011);
				}
				else
				{
					return sendQuestNoneDialog(env, 799784, 182214014, 1);
				}
			}
			return false;
		}
		
		final int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if (env.getTargetId() == 799783)
			{
				if ((env.getDialog() == QuestDialog.USE_OBJECT) || (env.getDialog() == QuestDialog.START_DIALOG))
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					defaultCloseDialog(env, 0, 1, 182214015, 2, 182214014, 1);
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
			else if ((env.getTargetId() == 203618) && (var == 1))
			{
				if ((env.getDialog() == QuestDialog.USE_OBJECT) || (env.getDialog() == QuestDialog.START_DIALOG))
				{
					return sendQuestDialog(env, 1693);
				}
				else if (env.getDialog() == QuestDialog.SELECT_ACTION_1694)
				{
					sendEmotion(env, (Creature) env.getVisibleObject(), EmotionId.NO, true);
					return sendQuestDialog(env, 1694);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_2)
				{
					defaultCloseDialog(env, 1, 2, 0, 0, 182214015, 1);
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
			else if ((env.getTargetId() == 203650) && (var == 2))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2034);
				}
				else if (env.getDialog() == QuestDialog.SELECT_ACTION_2035)
				{
					sendEmotion(env, (Creature) env.getVisibleObject(), EmotionId.PANIC, true);
					return sendQuestDialog(env, 2035);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_3)
				{
					return defaultCloseDialog(env, 2, 3, true, false, 0, 0, 0, 182214015, 1);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		
		return sendQuestRewardDialog(env, 799784, 2375);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if (EventService.getInstance().checkQuestIsActive(questId))
		{
			return QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel());
		}
		else if (qs != null)
		{
			// Set as expired
			QuestService.abandonQuest(player, questId);
		}
		return false;
	}
	
}
