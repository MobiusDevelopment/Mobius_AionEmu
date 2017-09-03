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
package system.handlers.quest.fenris_fang;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _29064Fang_Of_Construction extends QuestHandler
{
	private static final int questId = 29064;
	
	public _29064Fang_Of_Construction()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 4939, true);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		final int[] npcs =
		{
			798452,
			204075,
			204053
		};
		qe.registerQuestNpc(204053).addOnQuestStart(questId);
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
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204053)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 798452:
					switch (dialog)
					{
						case START_DIALOG:
							return sendQuestDialog(env, 1352);
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 204075:
					switch (dialog)
					{
						case START_DIALOG:
						{
							if ((var == 1) && (player.getInventory().getItemCountByItemId(182213239) >= 1) && (player.getInventory().getItemCountByItemId(186000085) >= 1))
							{
								return sendQuestDialog(env, 2375);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							if ((player.getInventory().getItemCountByItemId(182213239) >= 1) && (player.getInventory().getItemCountByItemId(186000085) >= 1))
							{
								removeQuestItem(env, 182213239, 1);
								removeQuestItem(env, 186000085, 1);
								return defaultCloseDialog(env, 1, 1, true, false);
							}
						}
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204075)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 5);
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