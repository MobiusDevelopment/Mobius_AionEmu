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
package system.handlers.quest.archdaeva;

import com.aionemu.gameserver.model.TeleportAnimation;
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
import com.aionemu.gameserver.services.teleport.TeleportService2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _20526Special_Secret_Mission extends QuestHandler
{
	public static final int questId = 20526;
	private static final int[] npcs =
	{
		806079,
		806394,
		806296,
		806297,
		703318,
		703319,
		703320
	};
	
	public _20526Special_Secret_Mission()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182216086, questId); // 잠든 위자보보.
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
		return defaultOnLvlUpEvent(env, 20525, true);
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 220120000)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVars().getQuestVars();
				if (var == 1)
				{
					changeQuestStep(env, 1, 2, false);
				}
			}
		}
		else if (player.getWorldId() == 220110000)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVars().getQuestVars();
				if (var == 4)
				{
					playQuestMovie(env, 876);
					changeQuestStep(env, 4, 5, false);
				}
			}
		}
		return false;
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
			if (targetId == 806079)
			{ // Feregran.
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
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 806394)
			{ // Varotta.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1694);
						}
					}
					case SELECT_REWARD:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 806296)
			{ // 데자보보.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2291);
						}
					}
					case STEP_TO_4:
					{
						// 잠든 위자보보.
						giveQuestItem(env, 182216086, 1);
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 703318)
			{ // 수상한 오드 파편.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 6)
						{
							return sendQuestDialog(env, 2717);
						}
					}
					case STEP_TO_6:
					{
						// 잠든 위자보보.
						giveQuestItem(env, 182216086, 1);
						changeQuestStep(env, 6, 7, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 703319)
			{ // 불가사의한 오드 파편.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 8)
						{
							return sendQuestDialog(env, 3400);
						}
					}
					case STEP_TO_8:
					{
						// 잠든 위자보보.
						giveQuestItem(env, 182216086, 1);
						changeQuestStep(env, 8, 9, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 703320)
			{ // 기묘한 오드 파편.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 10)
						{
							return sendQuestDialog(env, 4081);
						}
					}
					case STEP_TO_10:
					{
						// 잠든 위자보보.
						giveQuestItem(env, 182216086, 1);
						// 잠이 든 위자보보.
						giveQuestItem(env, 164002348, 1);
						changeQuestStep(env, 10, 11, false);
						QuestService.addNewSpawn(220110000, 1, 806297, player.getX(), player.getY(), player.getZ(), (byte) 0); // 잠에서 깬 위자보보.
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 806297)
			{ // 잠에서 깬 위자보보.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 11)
						{
							return sendQuestDialog(env, 6843);
						}
					}
					case SET_REWARD:
					{
						final Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						changeQuestStep(env, 11, 12, true);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806079)
			{ // Feregran.
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
			final int var = qs.getQuestVarById(0);
			if (var == 5)
			{
				TeleportService2.teleportTo(player, 220110000, 1393.7161f, 2895.9714f, 253.41635f, (byte) 13, TeleportAnimation.BEAM_ANIMATION);
				return HandlerResult.fromBoolean(useQuestItem(env, item, 5, 6, false));
			}
			if (var == 7)
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 7, 8, false));
			}
			if (var == 9)
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 9, 10, false));
			}
		}
		return HandlerResult.FAILED;
	}
}