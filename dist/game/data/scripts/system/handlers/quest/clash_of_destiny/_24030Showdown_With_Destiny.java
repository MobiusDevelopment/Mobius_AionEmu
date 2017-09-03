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
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24030Showdown_With_Destiny extends QuestHandler
{
	private static final int questId = 24030;
	private static final int[] npcs =
	{
		204206,
		204207,
		203550,
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
	
	public _24030Showdown_With_Destiny()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnDie(questId);
		qe.registerOnEnterWorld(questId);
		for (final int npc_id : npcs)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
		for (final int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
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
				case 204206:
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
							return defaultCloseDialog(env, 0, 1);
						}
					}
					break;
				}
				case 204207:
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
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				}
				case 203550:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
							else if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
							else if (var == 4)
							{
								return sendQuestDialog(env, 2375);
							}
							else if (var == 8)
							{
								return sendQuestDialog(env, 3740);
							}
						}
						case STEP_TO_3:
						{
							return defaultCloseDialog(env, 2, 3);
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 3, 4, false, 10000, 10001, 182215392, 1);
						}
						case STEP_TO_5:
						{
							// Teleport To Fissure Of Destiny.
							TeleportService2.teleportTo(env.getPlayer(), 400010000, 3253.8171f, 3040.3752f, 1434.3372f, (byte) 57, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 4, 5);
						}
						case SET_REWARD:
						{
							return defaultCloseDialog(env, 8, 8, true, false);
						}
					}
					break;
				}
				case 700551:
				{
					if ((env.getDialog() == QuestDialog.USE_OBJECT) && (var == 5))
					{
						final WorldMapInstance AtaxiarD = InstanceService.getNextAvailableInstance(320140000);
						InstanceService.registerPlayerWithInstance(AtaxiarD, player);
						TeleportService2.teleportTo(player, 320140000, AtaxiarD.getInstanceId(), 52, 174, 229);
						return true;
					}
					break;
				}
				case 205020:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
						}
						case STEP_TO_6:
						{
							if (var == 5)
							{
								removeQuestItem(env, 182215392, 1);
								SkillEngine.getInstance().applyEffectDirectly(281, player, player, 0);
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
										changeQuestStep(qe, 5, 6, false);
									}
								}, 43000);
								return true;
							}
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204052)
			{
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
			if (var == 6)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 49))
				{
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 49)
				{
					qs.setQuestVar(7);
					updateQuestStatus(env);
					QuestService.addNewSpawn(320140000, player.getInstanceId(), 798346, 299.4378f, 289.15744f, 206.48138f, (byte) 75);
					return true;
				}
			}
			else if (var == 7)
			{
				return defaultOnKillEvent(env, 798346, 7, 8);
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
			if ((var > 5) && (var < 8))
			{
				changeQuestStep(env, var, 5, false);
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
				if ((var > 5) && (var < 8))
				{
					changeQuestStep(env, var, 5, false);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
			}
		}
		return false;
	}
}