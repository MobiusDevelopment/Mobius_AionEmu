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
package system.handlers.quest.crafting;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */
public class _2979ExpertCooksFinalExam extends QuestHandler
{
	
	private static final int questId = 2979;
	
	public _2979ExpertCooksFinalExam()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204100).addOnQuestStart(questId);
		qe.registerQuestNpc(204100).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204100)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204100:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							final long itemCount1 = player.getInventory().getItemCountByItemId(182207955);
							final long itemCount2 = player.getInventory().getItemCountByItemId(182207956);
							final long itemCount3 = player.getInventory().getItemCountByItemId(182207957);
							final long itemCount4 = player.getInventory().getItemCountByItemId(182207958);
							if ((itemCount1 > 0) && (itemCount2 > 0) && (itemCount3 > 0) && (itemCount4 > 0))
							{
								removeQuestItem(env, 182207955, 1);
								removeQuestItem(env, 182207956, 1);
								removeQuestItem(env, 182207957, 1);
								removeQuestItem(env, 182207958, 1);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 2375);
							}
							return sendQuestDialog(env, 2716);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204100)
			{
				if (env.getDialogId() == 39)
				{
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
