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
package system.handlers.quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _25050Treasure_In_The_Deep_Sea extends QuestHandler
{
	private static final int questId = 25050;
	
	public _25050Treasure_In_The_Deep_Sea()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804915).addOnQuestStart(questId);
		qe.registerQuestNpc(804915).addOnTalkEvent(questId);
		qe.registerQuestNpc(731553).addOnTalkEvent(questId);
		qe.registerQuestNpc(805160).addOnTalkEvent(questId);
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
			if (targetId == 804915)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 804915)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 10000);
						}
						else
						{
							return sendQuestDialog(env, 10001);
						}
					}
					case STEP_TO_2:
					{
						if (var == 1)
						{
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							giveQuestItem(env, 182215719, 1);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 731553)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					if ((var == 2) && (player.getInventory().getItemCountByItemId(182215719) == 1))
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_3)
				{
					removeQuestItem(env, 182215719, 1);
					QuestService.addNewSpawn(220080000, player.getInstanceId(), 805160, 2046.8f, 1588.8f, 348.4f, (byte) 90);
					changeQuestStep(env, 2, 3, false);
					return closeDialogWindow(env);
				}
			}
			if (targetId == 805160)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					if (var == 3)
					{
						return sendQuestDialog(env, 2034);
					}
				}
				else if (env.getDialog() == QuestDialog.SET_REWARD)
				{
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().onDelete();
					changeQuestStep(env, 3, 3, true);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804915)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2376);
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