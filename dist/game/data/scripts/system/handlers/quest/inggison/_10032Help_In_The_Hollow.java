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

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rinzler (Encom)
 */
public class _10032Help_In_The_Hollow extends QuestHandler
{
	private static final int questId = 10032;
	
	public _10032Help_In_The_Hollow()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npc_ids =
		{
			798952,
			798954,
			799022,
			799503
		};
		for (int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerQuestItem(182215618, questId);
		qe.registerQuestItem(182215619, questId);
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
		return defaultOnLvlUpEvent(env, 10031, true);
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
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798952)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
			return false;
		}
		else if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (targetId == 798952)
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
					return defaultCloseDialog(env, 0, 1);
				}
			}
		}
		else if (targetId == 798954)
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
					return defaultCloseDialog(env, 1, 2);
				}
			}
		}
		else if (targetId == 799503)
		{
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 6)
					{
						removeQuestItem(env, 182215618, 1);
						removeQuestItem(env, 182215619, 1);
						TeleportService2.teleportTo(env.getPlayer(), 210050000, 2649, 340, 511, (byte) 92, TeleportAnimation.JUMP_ANIMATION);
						return checkQuestItems(env, 6, 7, false, 10000, 10001);
					}
				}
				case FINISH_DIALOG:
				{
					return sendQuestSelectionDialog(env);
				}
			}
		}
		else if (targetId == 799022)
		{
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 2)
					{
						return sendQuestDialog(env, 1693);
					}
				}
				case SELECT_ACTION_1694:
				{
					if (var == 2)
					{
						return sendQuestDialog(env, 1694);
					}
				}
				case STEP_TO_3:
				{
					if (player.isInGroup2())
					{
						PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You must leave your group or alliance to enter <Taloc Hollow>", ChatType.BRIGHT_YELLOW_CENTER), true);
						return true;
					}
					if (giveQuestItem(env, 182215618, 1) && giveQuestItem(env, 182215619, 1))
					{
						final WorldMapInstance talocHollow = InstanceService.getNextAvailableInstance(300190000);
						InstanceService.registerPlayerWithInstance(talocHollow, player);
						TeleportService2.teleportTo(player, 300190000, talocHollow.getInstanceId(), 202.26694f, 226.0532f, 1098.236f, (byte) 30);
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_WAREHOUSE_FULL_INVENTORY);
					return sendQuestSelectionDialog(env);
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
			if (player.getWorldId() == 300190000)
			{
				final int itemId = item.getItemId();
				final int var = qs.getQuestVarById(0);
				final int var1 = qs.getQuestVarById(1);
				if (itemId == 182215618)
				{
					changeQuestStep(env, 4, 5, false);
					return HandlerResult.SUCCESS;
				}
				else if (itemId == 182215619)
				{
					if (var == 5)
					{
						if ((var1 >= 0) && (var1 < 19))
						{
							changeQuestStep(env, var1, var1 + 1, false, 1);
							return HandlerResult.SUCCESS;
						}
						else if (var1 == 19)
						{
							qs.setQuestVar(6);
							updateQuestStatus(env);
							return HandlerResult.SUCCESS;
						}
					}
				}
			}
		}
		return HandlerResult.UNKNOWN;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() != 300190000)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if ((var >= 4) && (var < 6))
				{
					removeQuestItem(env, 182215618, 1);
					removeQuestItem(env, 182215619, 1);
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return true;
				}
				else if (var == 7)
				{
					qs.setQuestVar(8);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		else if (player.getWorldId() != 300190000)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if (var == 3)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if ((var >= 4) && (var < 6))
			{
				removeQuestItem(env, 182215618, 1);
				removeQuestItem(env, 182215619, 1);
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onLogOutEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if ((var >= 4) && (var < 6))
			{
				removeQuestItem(env, 182215618, 1);
				removeQuestItem(env, 182215619, 1);
				qs.setQuestVar(2);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}