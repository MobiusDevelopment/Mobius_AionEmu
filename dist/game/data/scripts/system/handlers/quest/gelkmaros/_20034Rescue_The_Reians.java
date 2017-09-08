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
package system.handlers.quest.gelkmaros;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rinzler (Encom)
 */
public class _20034Rescue_The_Reians extends QuestHandler
{
	private static final int questId = 20034;
	private static final int[] npc_ids =
	{
		799295,
		799297,
		730243,
		799513,
		799514,
		799515,
		799516,
		799341,
		700706
	};
	
	public _20034Rescue_The_Reians()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnDie(questId);
		qe.registerOnLogOut(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnMovieEndQuest(442, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(216592).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVars().getQuestVars();
			if (var == 2)
			{
				if (player.getWorldId() == 300150000)
				{
					qs.setQuestVar(3);
					updateQuestStatus(env);
				}
			}
			if (var == 6)
			{
				if (player.getWorldId() == 220070000)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20033, true);
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
		final int var1 = qs.getQuestVarById(1);
		int targetId = 0;
		final QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final Npc npc = (Npc) player.getTarget();
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799295)
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
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
			return false;
		}
		else if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		if (targetId == 799297)
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
					giveQuestItem(env, 182215630, 1);
					return defaultCloseDialog(env, 0, 1);
				}
			}
		}
		else if (targetId == 799295)
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
				case STEP_TO_2:
				{
					removeQuestItem(env, 182215630, 1);
					giveQuestItem(env, 182215595, 1);
					return defaultCloseDialog(env, 1, 2);
				}
			}
		}
		else if (targetId == 730243)
		{
			switch (env.getDialog())
			{
				case USE_OBJECT:
				{
					if (var == 2)
					{
						return sendQuestDialog(env, 1693);
					}
				}
				case STEP_TO_3:
				{
					if (var == 2)
					{
						final WorldMapInstance IDTempleUp = InstanceService.getNextAvailableInstance(300150000);
						InstanceService.registerPlayerWithInstance(IDTempleUp, player);
						TeleportService2.teleportTo(player, 300150000, IDTempleUp.getInstanceId(), 561.8651f, 221.91483f, 134.53333f, (byte) 90);
						return true;
					}
				}
			}
		}
		else if ((targetId == 799513) || (targetId == 799514) || (targetId == 799515) || (targetId == 799516))
		{
			switch (env.getDialog())
			{
				case USE_OBJECT:
				{
					if ((var == 3) && (var1 < 3))
					{
						qs.setQuestVarById(1, var1 + 1);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDelete();
						return true;
					}
					else if ((var == 3) && (var1 == 3))
					{
						playQuestMovie(env, 442);
						qs.setQuestVar(4);
						updateQuestStatus(env);
						PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
						npc.getController().onDelete();
						return true;
					}
				}
			}
		}
		else if (targetId == 799341)
		{
			switch (env.getDialog())
			{
				case USE_OBJECT:
				{
					if (var == 5)
					{
						changeQuestStep(env, 5, 6, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (targetId == 700706)
		{
			switch (env.getDialog())
			{
				case USE_OBJECT:
				{
					if (var == 6)
					{
						removeQuestItem(env, 182215595, 1);
						TeleportService2.teleportTo(env.getPlayer(), 220070000, 2511.7263f, 2140.7117f, 464.84372f, (byte) 101, TeleportAnimation.JUMP_ANIMATION);
					}
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
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int var = qs.getQuestVars().getQuestVars();
		if ((var >= 3) && (var < 6))
		{
			qs.setQuestVar(2);
			updateQuestStatus(env);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
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
			if ((var >= 3) && (var < 6))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int instanceId = player.getInstanceId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final int var = qs.getQuestVarById(0);
		if (targetId == 216592)
		{
			if (var == 4)
			{
				QuestService.addNewSpawn(300150000, instanceId, 799341, 561.8763f, 192.25128f, 135.88919f, (byte) 30);
				qs.setQuestVarById(0, var + 1);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		if (movieId != 442)
		{
			return false;
		}
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START) || (qs.getQuestVars().getQuestVars() != 4))
		{
			return false;
		}
		return true;
	}
}