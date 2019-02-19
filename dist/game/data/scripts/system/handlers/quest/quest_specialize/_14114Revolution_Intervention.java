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
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _14114Revolution_Intervention extends QuestHandler
{
	private static final int questId = 14114;
	
	public _14114Revolution_Intervention()
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
		qe.registerOnMovieEndQuest(23, questId);
		qe.registerQuestNpc(203098).addOnQuestStart(questId); // Spatalos
		qe.registerQuestNpc(203098).addOnTalkEvent(questId); // Spatalos
		qe.registerQuestNpc(203183).addOnTalkEvent(questId); // Khidia
		qe.registerOnEnterZone(ZoneName.get("LF1A_SENSORYAREA_Q1023_SPG_206008_2_210030000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203098) // Spatalos
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 203183) // Khidia
			{
				final int var = qs.getQuestVarById(0);
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2)
						{
							return sendQuestDialog(env, 1352);
						}
						else if (var == 3)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case STEP_TO_1:
					{
						playQuestMovie(env, 30);
						SkillEngine.getInstance().getSkill(player, 8197, 1, player).useSkill(); // Transforming Plumis.
						updateQuestStatus(env);
						return defaultCloseDialog(env, 0, 1);
					}
					case STEP_TO_2:
					{
						player.getEffectController().removeEffect(8197); // Transforming Plumis.
						return defaultCloseDialog(env, 2, 3);
					}
					case STEP_TO_3:
					{
						removeQuestItem(env, 182215457, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						return checkQuestItems(env, 3, 4, false, 10000, 10001);
					}
					case FINISH_DIALOG:
					{
						if (var == 4)
						{
							defaultCloseDialog(env, 4, 4);
						}
						else if (var == 3)
						{
							defaultCloseDialog(env, 3, 3);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203098)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2375);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (movieId == 23))
		{
			qs.setQuestVar(2);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("LF1A_SENSORYAREA_Q1023_SPG_206008_2_210030000"))
			{
				if (var == 1)
				{
					playQuestMovie(env, 23);
					return true;
				}
			}
		}
		return false;
	}
}