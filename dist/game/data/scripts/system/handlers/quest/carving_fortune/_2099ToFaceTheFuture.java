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
package system.handlers.quest.carving_fortune;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
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
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * Talk with Munin (790001). Find Fissure of Destiny (700551) that connects to Ataxiar (320140000) and talk with Hagen (205020) (spawn). Proceed to Ataxiar aand annihilate the Guardian Legionarys (50): Legionary (798342, 798343, 798344, 798345), Vanquish Brigade General Hellion (1), Talk with Lephar
 * (205118) (spawn). Report the result to Vidar (204052).
 * @author Bobobear
 */
public class _2099ToFaceTheFuture extends QuestHandler
{
	private static final int questId = 2099;
	private static final int[] npcs =
	{
		203550,
		205020,
		205118,
		700551,
		204052
	};
	private static final int[] mobs =
	{
		798342,
		798343,
		798344,
		798345,
		798346
	};
	
	public _2099ToFaceTheFuture()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnDie(questId);
		qe.registerOnEnterWorld(questId);
		for (int npc_id : npcs)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		for (int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 2098);
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
			switch (targetId)
			{
				case 203550:
				{ // Munin.
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
							if ((!giveQuestItem(env, 182206066, 1)) || (!giveQuestItem(env, 182206067, 1)))
							{
								return false;
							}
							// Teleport To Fissure Of Destiny.
							TeleportService2.teleportTo(env.getPlayer(), 400010000, 3253.8171f, 3040.3752f, 1434.3372f, (byte) 57, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				}
				case 700551:
				{ // Fissure Of Destiny.
					if ((env.getDialog() == QuestDialog.USE_OBJECT) && (var == 1))
					{
						final WorldMapInstance AtaxiarD = InstanceService.getNextAvailableInstance(320140000);
						InstanceService.registerPlayerWithInstance(AtaxiarD, player);
						TeleportService2.teleportTo(player, 320140000, AtaxiarD.getInstanceId(), 52, 174, 229);
						return true;
					}
					break;
				}
				case 205020:
				{ // Hagen.
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
							if (var == 1)
							{
								player.setState(CreatureState.FLIGHT_TELEPORT);
								player.unsetState(CreatureState.ACTIVE);
								player.setFlightTeleportId(1001);
								PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
								final QuestEnv qe = env;
								ThreadPoolManager.getInstance().schedule(new Runnable()
								{
									@Override
									public void run()
									{
										changeQuestStep(qe, 1, 2, false);
									}
								}, 43000);
								return true;
							}
						}
					}
					break;
				}
				case 205118:
				{ // Lephar.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 53)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2:
						case STEP_TO_3:
						{
							return defaultCloseDialog(env, 53, 53, true, false);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204052)
			{ // Vidar.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if ((var >= 2) && (var < 52))
			{
				final int[] npcIds =
				{
					798342,
					798343,
					798344,
					798345
				};
				if (var == 51)
				{
					QuestService.addNewSpawn(310120000, player.getInstanceId(), 798346, 299.4378f, 289.15744f, 206.48138f, (byte) 75);
				}
				return defaultOnKillEvent(env, npcIds, 2, 52);
			}
			else if (var == 52)
			{
				return defaultOnKillEvent(env, 798346, 52, 53);
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
			if (var > 1)
			{
				changeQuestStep(env, var, 1, false);
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() != 320140000)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if (var > 1)
				{
					changeQuestStep(env, var, 1, false);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
			}
		}
		return false;
	}
}