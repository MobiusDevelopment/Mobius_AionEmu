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

/**
 * @author Rinzler (Encom)
 */
public class _14024A_Kralling_Suspicion extends QuestHandler
{
	private static final int questId = 14024;
	private static final int[] npc_ids =
	{
		203904,
		204045,
		204004,
		204020
	};
	
	public _14024A_Kralling_Suspicion()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		for (int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
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
		return defaultOnLvlUpEvent(env, 14023, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 203904)
			{
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
					case STEP_TO_1:
					{
						if (var == 0)
						{
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 204045)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case SELECT_ACTION_1353:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1353);
						}
					}
					case STEP_TO_2:
					{
						if (var == 1)
						{
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 204004)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case STEP_TO_4:
					{
						changeQuestStep(env, 3, 3, true);
						TeleportService2.teleportTo(player, 210020000, 1605.3418f, 1528.9393f, 318.0651f, (byte) 117, TeleportAnimation.BEAM_ANIMATION);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						return checkQuestItems(env, 2, 3, false, 2120, 2035);
					}
					case FINISH_DIALOG:
					{
						if (var == 3)
						{
							defaultCloseDialog(env, 3, 3);
						}
						else if (var == 2)
						{
							defaultCloseDialog(env, 2, 2);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204020)
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
}