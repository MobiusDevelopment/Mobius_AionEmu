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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24021Ghosts_In_The_Desert extends QuestHandler
{
	private static final int questId = 24021;
	private final int itemId = 182215363;
	
	public _24021Ghosts_In_The_Desert()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(itemId, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(204302).addOnTalkEvent(questId);
		qe.registerQuestNpc(204329).addOnTalkEvent(questId);
		qe.registerQuestNpc(802046).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24020, true);
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
				case 204302:
				{ // Bragi.
					switch (dialog)
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1011);
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				}
				case 204329:
				{ // Tofa.
					switch (dialog)
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
								playQuestMovie(env, 73);
								return sendQuestDialog(env, 1353);
							}
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2);
						}
						
					}
				}
				case 802046:
				{ // Tofynir.
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
								return sendQuestDialog(env, 10000);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 2, 3, false, 10000, 10001);
						}
						case STEP_TO_4:
						{
							if (!player.getInventory().isFullSpecialCube())
							{
								return defaultCloseDialog(env, 3, 4, 182215363, 1, 0, 0);
							}
							else
							{
								return sendQuestSelectionDialog(env);
							}
						}
						case FINISH_DIALOG:
							return sendQuestSelectionDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204329)
			{ // Tofa.
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
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			if ((item.getItemId() == 182215363) && player.isInsideZone(ZoneName.get("DF2_ITEMUSEAREA_Q2032")))
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true, 88));
			}
		}
		return HandlerResult.FAILED;
	}
}