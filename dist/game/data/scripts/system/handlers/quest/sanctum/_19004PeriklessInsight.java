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
package system.handlers.quest.sanctum;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Starts Perikles (203757). Talk with Jucleas (203752). Talk with Lavirintos (203701). Talk with Mysteris (798500).
 * @author Rolandas
 * @reworked vlog
 */
public class _19004PeriklessInsight extends QuestHandler
{
	private static final int questId = 19004;
	private static final int[] npcs =
	{
		203757,
		203752,
		203701,
		798500
	};
	
	public _19004PeriklessInsight()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(203757).addOnQuestStart(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203757) // Perikles
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			if (targetId == 203752) // Jucleas
			{
				switch (dialog)
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
						return defaultCloseDialog(env, 0, 1); // 1
					}
				}
			}
			else if (targetId == 203701) // Lavirintos
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case STEP_TO_2:
					{
						return defaultCloseDialog(env, 1, 2); // 2
					}
				}
			}
			else if (targetId == 798500) // Mysteris
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case SELECT_REWARD:
					{
						changeQuestStep(env, 2, 2, true); // reward
						return sendQuestDialog(env, 5);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798500) // Mysteris
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
