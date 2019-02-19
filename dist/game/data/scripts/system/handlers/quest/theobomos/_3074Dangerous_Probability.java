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
package system.handlers.quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.item.ItemService;

public class _3074Dangerous_Probability extends QuestHandler
{
	private static final int questId = 3074;
	private int reward = -1;
	
	public _3074Dangerous_Probability()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798193).addOnQuestStart(questId);
		qe.registerQuestNpc(798193).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 798193)
			{ // Nagrunerk.
				if (dialog == QuestDialog.EXCHANGE_COIN)
				{
					if (QuestService.startQuest(env))
					{
						return sendQuestDialog(env, 1011);
					}
					return sendQuestSelectionDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 798193)
			{ // Nagrunerk.
				final long kinahAmount = player.getInventory().getKinah();
				final long angelsEye = player.getInventory().getItemCountByItemId(186000037);
				switch (dialog)
				{
					case EXCHANGE_COIN:
					{
						return sendQuestDialog(env, 1011);
					}
					case SELECT_ACTION_1011:
					{
						if ((kinahAmount >= 1000) && (angelsEye >= 1))
						{
							changeQuestStep(env, 0, 0, true);
							reward = 0;
							return sendQuestDialog(env, 5);
						}
						return sendQuestDialog(env, 1009);
					}
					case SELECT_ACTION_1352:
					{
						if ((kinahAmount >= 5000) && (angelsEye >= 1))
						{
							changeQuestStep(env, 0, 0, true);
							reward = 1;
							return sendQuestDialog(env, 6);
						}
						return sendQuestDialog(env, 1009);
					}
					case SELECT_ACTION_1693:
					{
						if ((kinahAmount >= 25000) && (angelsEye >= 1))
						{
							changeQuestStep(env, 0, 0, true);
							reward = 2;
							return sendQuestDialog(env, 7);
						}
						return sendQuestDialog(env, 1009);
					}
					case FINISH_DIALOG:
					{
						return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798193)
			{ // Nagrunerk.
				if (dialog == QuestDialog.SELECT_NO_REWARD)
				{
					switch (reward)
					{
						case 0:
						{
							if (QuestService.finishQuest(env, 0))
							{
								player.getInventory().decreaseKinah(1000);
								removeQuestItem(env, 186000037, 1);
								ItemService.addItem(player, 186000005, 3);
								reward = -1;
								break;
							}
						}
						case 1:
						{
							if (QuestService.finishQuest(env, 1))
							{
								player.getInventory().decreaseKinah(5000);
								removeQuestItem(env, 186000037, 1);
								ItemService.addItem(player, 186000005, 5);
								reward = -1;
								break;
							}
						}
						case 2:
						{
							if (QuestService.finishQuest(env, 2))
							{
								player.getInventory().decreaseKinah(25000);
								removeQuestItem(env, 186000037, 1);
								ItemService.addItem(player, 186000005, 7);
								reward = -1;
								break;
							}
						}
					}
					return closeDialogWindow(env);
				}
				QuestService.abandonQuest(player, questId);
			}
		}
		return false;
	}
}