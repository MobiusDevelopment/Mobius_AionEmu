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
package system.handlers.quest.beritra_grand_invasion;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _13403Portal_Predictions extends QuestHandler
{
	public static final int questId = 13403;
	
	public _13403Portal_Predictions()
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
		qe.registerQuestNpc(203098).addOnQuestStart(questId); // Spatalos.
		qe.registerQuestNpc(203098).addOnTalkEvent(questId); // Spatalos.
		qe.registerQuestNpc(203096).addOnTalkEvent(questId); // William.
		qe.registerQuestNpc(203106).addOnTalkEvent(questId); // Alisdair.
		qe.registerQuestNpc(203133).addOnTalkEvent(questId); // Beris.
		qe.registerQuestNpc(203170).addOnTalkEvent(questId); // Jenel.
		qe.registerQuestNpc(731643).addOnTalkEvent(questId); // Damaged Invasion Generator.
		qe.registerQuestNpc(731644).addOnTalkEvent(questId); // Inoperable Invasion Generator.
		qe.registerQuestItem(182215794, questId); // Infiltration Sensor.
		qe.registerQuestItem(182215795, questId); // Infiltration Detector.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203098)
			{ // Spatalos.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
					{
						return sendQuestStartDialog(env);
					}
					case REFUSE_QUEST_SIMPLE:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 203096)
			{ // William.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
					}
					case SELECT_ACTION_1012:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1012);
						}
					}
					case SELECT_ACTION_1013:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1013);
						}
					}
					case STEP_TO_1:
					{
						giveQuestItem(env, 182215794, 1); // Infiltration Sensor.
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 203133)
			{ // Beris.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case SELECT_ACTION_1694:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1694);
						}
					}
					case SELECT_ACTION_1695:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1695);
						}
					}
					case STEP_TO_3:
					{
						giveQuestItem(env, 182215794, 1); // Infiltration Sensor.
						giveQuestItem(env, 182215795, 1); // Infiltration Detector.
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 203170)
			{ // Jenel.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 4)
						{
							return sendQuestDialog(env, 2376);
						}
					}
					case SELECT_ACTION_2375:
					{
						if (var == 4)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case STEP_TO_5:
					{
						giveQuestItem(env, 182215795, 1); // Infiltration Detector.
						changeQuestStep(env, 4, 5, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 731643)
			{ // Damaged Invasion Generator.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 5)
						{
							return sendQuestDialog(env, 2716);
						}
					}
					case STEP_TO_6:
					{
						giveQuestItem(env, 182215796, 1); // Preserved Circuit.
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 731644)
			{ // Inoperable Invasion Generator.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 6)
						{
							return sendQuestDialog(env, 3057);
						}
					}
					case SET_REWARD:
					{
						giveQuestItem(env, 182215797, 1); // Intact Circuit.
						changeQuestStep(env, 6, 7, true);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203106)
			{ // Alisdair.
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
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 1)
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 1, 2, false));
			}
			if (var == 3)
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
			}
		}
		return HandlerResult.FAILED;
	}
}