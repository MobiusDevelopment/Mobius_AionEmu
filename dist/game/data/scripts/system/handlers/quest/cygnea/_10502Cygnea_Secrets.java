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
package system.handlers.quest.cygnea;

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
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _10502Cygnea_Secrets extends QuestHandler
{
	public static final int questId = 10502;
	
	public _10502Cygnea_Secrets()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			804701,
			804702,
			731537,
			702669
		};
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215601, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("LF5_ITEMUSEAREA_Q10502"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 10501, true);
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
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 804701)
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
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804702)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2)
						{
							return sendQuestDialog(env, 1693);
						}
						else if (var == 3)
						{
							return sendQuestDialog(env, 2034);
						}
						else if (var == 5)
						{
							return sendQuestDialog(env, 2716);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							changeQuestStep(env, 2, 3, false);
							return sendQuestDialog(env, 10000);
						}
						return sendQuestDialog(env, 10001);
					}
					case STEP_TO_4:
					{
						giveQuestItem(env, 182215601, 1);
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_6:
					{
						removeQuestItem(env, 182215602, 1);
						changeQuestStep(env, 5, 6, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 702669)
			{ // Shining Lump Of Soil.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						giveQuestItem(env, 182215600, 1);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 731537)
			{ // Ancient Fossil.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 6)
						{
							return sendQuestDialog(env, 3057);
						}
					}
					case SET_REWARD:
					{
						giveQuestItem(env, 182215635, 1);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804701)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
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
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			if (!player.isInsideZone(ZoneName.get("LF5_ITEMUSEAREA_Q10502")))
			{
				return HandlerResult.UNKNOWN;
			}
			final int var = qs.getQuestVarById(0);
			if (var == 4)
			{
				giveQuestItem(env, 182215602, 1);
				return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 5, false));
			}
		}
		return HandlerResult.FAILED;
	}
}