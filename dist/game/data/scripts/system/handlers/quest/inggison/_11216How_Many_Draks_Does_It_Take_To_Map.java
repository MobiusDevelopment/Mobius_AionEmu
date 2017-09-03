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
package system.handlers.quest.inggison;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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

public class _11216How_Many_Draks_Does_It_Take_To_Map extends QuestHandler
{
	
	private static final int questId = 11216;
	
	public _11216How_Many_Draks_Does_It_Take_To_Map()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestItem(182206825, questId); // Balaur's Map.
		qe.registerQuestNpc(799017).addOnTalkEvent(questId); // Sulinia.
		qe.registerQuestNpc(700624).addOnTalkEvent(questId); // Eastern Star.
		qe.registerQuestNpc(700625).addOnTalkEvent(questId); // Western Star.
		qe.registerQuestNpc(700626).addOnTalkEvent(questId); // Southern Star.
		qe.registerQuestNpc(700627).addOnTalkEvent(questId); // Northern Star.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 0)
			{
				if (env.getDialog() == QuestDialog.ACCEPT_QUEST)
				{
					removeQuestItem(env, 182206825, 1); // Balaur's Map.
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
		{
			if (targetId == 799017)
			{ // Sulinia.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						return sendQuestDialog(env, 10002);
					}
					case SELECT_REWARD:
					{
						return sendQuestDialog(env, 5);
					}
					default:
						return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 700624:
				{ // Eastern Star.
					switch (env.getDialog())
					{
						case USE_OBJECT:
						{
							if (player.getInventory().getItemCountByItemId(182206827) == 0)
							{
								giveQuestItem(env, 182206827, 1); // Eastern Symbol.
							}
						}
					}
					break;
				}
				case 700625:
				{ // Western Star.
					switch (env.getDialog())
					{
						case USE_OBJECT:
						{
							if (player.getInventory().getItemCountByItemId(182206828) == 0)
							{
								giveQuestItem(env, 182206828, 1); // Western Symbol.
							}
						}
							break;
					}
				}
				case 700626:
				{ // Southern Star.
					switch (env.getDialog())
					{
						case USE_OBJECT:
						{
							if (player.getInventory().getItemCountByItemId(182206829) == 0)
							{
								giveQuestItem(env, 182206829, 1); // Southern Symbol.
							}
						}
							break;
					}
				}
				case 700627:
				{ // Northern Star.
					switch (env.getDialog())
					{
						case USE_OBJECT:
						{
							if (player.getInventory().getItemCountByItemId(182206830) == 0)
							{
								giveQuestItem(env, 182206830, 1); // Northern Symbol.
							}
						}
							break;
					}
				}
				case 799017:
				{ // Sulinia.
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						case SELECT_ACTION_1012:
						{
							return defaultCloseDialog(env, 0, 1);
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 1, 2, true, 5, 10001);
						}
					}
					break;
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
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
}