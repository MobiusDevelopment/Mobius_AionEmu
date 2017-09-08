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
package system.handlers.quest.clash_of_destiny;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _24031Enemy_At_The_Doorstep extends QuestHandler
{
	private static final int questId = 24031;
	private static final int[] npc_ids =
	{
		204052,
		801224,
		203550,
		203654,
		204369,
		730888,
		730898
	};
	
	public _24031Enemy_At_The_Doorstep()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestItem(182215394, questId); // Munin's Dimension Investigation Device.
		qe.registerQuestItem(182215395, questId); // Aurtri's Dimension Investigation Device.
		qe.registerQuestItem(182215396, questId); // Tyr's Dimension Investigation Device.
		qe.registerQuestNpc(233879).addOnKillEvent(questId); // Captain Hagarkan.
		for (int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("NIDALBER_320040000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24030, true);
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
			if (targetId == 204052)
			{ // Vidar.
				return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (targetId == 204052)
		{ // Vidar.
			switch (env.getDialog())
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
		}
		else if (targetId == 801224)
		{ // Rapidefire Rita.
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 1)
					{
						return sendQuestDialog(env, 1352);
					}
				}
				case STEP_TO_2:
				{
					return defaultCloseDialog(env, 1, 2);
				}
			}
		}
		else if (targetId == 203550)
		{ // Munin.
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 2)
					{
						return sendQuestDialog(env, 1693);
					}
				}
				case STEP_TO_3:
				{
					giveQuestItem(env, 182215394, 1);
					return defaultCloseDialog(env, 2, 3);
				}
			}
		}
		else if (targetId == 203654)
		{ // Aurtri.
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 4)
					{
						return sendQuestDialog(env, 2376);
					}
				}
				case STEP_TO_5:
				{
					giveQuestItem(env, 182215395, 1);
					return defaultCloseDialog(env, 4, 5);
				}
			}
		}
		else if (targetId == 204369)
		{ // Tyr.
			switch (env.getDialog())
			{
				case START_DIALOG:
				{
					if (var == 6)
					{
						return sendQuestDialog(env, 3057);
					}
					else if (var == 8)
					{
						return sendQuestDialog(env, 3740);
					}
				}
				case STEP_TO_7:
				{
					giveQuestItem(env, 182215396, 1);
					return defaultCloseDialog(env, 6, 7);
				}
				case STEP_TO_9:
				{
					return defaultCloseDialog(env, 8, 9);
				}
				case FINISH_DIALOG:
				{
					return closeDialogWindow(env);
				}
			}
		}
		else if (targetId == 730888)
		{
			switch (env.getDialog())
			{
				case USE_OBJECT:
				{
					if (var == 10)
					{
						playQuestMovie(env, 898);
						final Npc npc = (Npc) env.getVisibleObject();
						QuestService.addNewSpawn(npc.getWorldId(), npc.getInstanceId(), 730898, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						npc.getController().onDelete();
						changeQuestStep(env, 10, 11, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (targetId == 730898)
		{
			if ((env.getDialog() == QuestDialog.USE_OBJECT) && (var == 11))
			{
				final Npc npc = (Npc) env.getVisibleObject();
				npc.getController().onDelete();
				TeleportService2.teleportTo(env.getPlayer(), 120010000, 1275.116f, 1173.6276f, 215.21492f, (byte) 91, TeleportAnimation.BEAM_ANIMATION);
				qs.setStatus(QuestStatus.REWARD);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		if (targetId == 233879)
		{ // Captain Hagarkan.
			if (var == 9)
			{
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return HandlerResult.UNKNOWN;
		}
		final int var = qs.getQuestVarById(0);
		if ((var == 3) && (id == 182215394))
		{ // Munin's Dimension Investigation Device.
			if (!player.isInsideZone(ZoneName.get("DF1_USE_ITEM_AREA_Q24031")))
			{
				return HandlerResult.UNKNOWN;
			}
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					removeQuestItem(env, 182215394, 1);
					final int var = qs.getQuestVarById(0);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		else if ((var == 5) && (id == 182215395))
		{ // Aurtri's Dimension Investigation Device.
			if (!player.isInsideZone(ZoneName.get("DF1A_USE_ITEM_AREA_Q24031")))
			{
				return HandlerResult.UNKNOWN;
			}
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					removeQuestItem(env, 182215395, 1);
					final int var = qs.getQuestVarById(0);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		else if ((var == 7) && (id == 182215396))
		{ // Tyr's Dimension Investigation Device.
			if (!player.isInsideZone(ZoneName.get("DF2_USE_ITEM_AREA_Q24031")))
			{
				return HandlerResult.UNKNOWN;
			}
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					removeQuestItem(env, 182215396, 1);
					PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
					qs.setQuestVarById(0, var + 1);
					updateQuestStatus(env);
				}
			}, 3000);
			return HandlerResult.SUCCESS;
		}
		return HandlerResult.UNKNOWN;
	}
	
	@Override
	public boolean onDieEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if ((var >= 9) && (var < 11))
			{
				qs.setQuestVar(8);
				updateQuestStatus(env);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
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
			if ((var >= 9) && (var < 11))
			{
				qs.setQuestVar(8);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("NIDALBER_320040000"))
		{
			final Player player = env.getPlayer();
			if (player == null)
			{
				return false;
			}
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null)
			{
				return false;
			}
			if (qs.getQuestVars().getQuestVars() == 8)
			{
				changeQuestStep(env, 8, 9, false);
				return true;
			}
		}
		return false;
	}
}