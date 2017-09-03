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

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _14045Rumors_On_Wings extends QuestHandler
{
	private static final int questId = 14045;
	
	public _14045Rumors_On_Wings()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			278506,
			279023,
			279006
		};
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 14044, true);
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
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 278506:
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
						}
						case SELECT_ACTION_1013:
						{
							playQuestMovie(env, 272);
							break;
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				case 279023:
					switch (dialog)
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
							giveQuestItem(env, 182215918, 1);
							TeleportService2.teleportTo(player, 400010000, 3392.2627f, 2445.2544f, 2766.6846f, (byte) 61, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				case 279006:
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
							else if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case SELECT_ACTION_1694:
						{
							removeQuestItem(env, 182215918, 1);
							return sendQuestDialog(env, 1694);
						}
						case STEP_TO_3:
						{
							if (var == 2)
							{
								qs.setQuestVarById(0, 12);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								TeleportService2.teleportTo(player, 400010000, 588.8393f, 1160.0896f, 2836.5127f, (byte) 41, TeleportAnimation.BEAM_ANIMATION);
								return sendQuestSelectionDialog(env);
							}
						}
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 279023)
			{
				if (dialog == QuestDialog.USE_OBJECT)
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
}