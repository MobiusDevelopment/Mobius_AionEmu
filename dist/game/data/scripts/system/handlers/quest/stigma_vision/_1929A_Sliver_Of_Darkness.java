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
package system.handlers.quest.stigma_vision;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
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
import com.aionemu.gameserver.world.WorldMapInstance;

public class _1929A_Sliver_Of_Darkness extends QuestHandler
{
	private static final int questId = 1929;
	
	public _1929A_Sliver_Of_Darkness()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			203752,
			203852,
			203164,
			205110,
			700240,
			205111,
			203701,
			203711
		};
		final int[] stigmas =
		{
			140000001,
			140000002,
			140000003,
			140000004
		};
		qe.registerOnLevelUp(questId);
		qe.registerOnMovieEndQuest(155, questId);
		qe.registerQuestNpc(212992).addOnKillEvent(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnDie(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int stigma : stigmas)
		{
			qe.registerOnEquipItem(stigma, questId);
		}
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
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		final int var = qs.getQuestVars().getQuestVars();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203752:
				{ // Jucleas.
					switch (dialog)
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
				case 203852:
				{ // Ludina.
					switch (dialog)
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
							TeleportService2.teleportTo(player, 210030000, 2315.0986f, 1798.2798f, 195.26416f, (byte) 25, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
					}
					break;
				}
				case 203164:
				{ // Morai.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
							else if (var == 8)
							{
								return sendQuestDialog(env, 3057);
							}
						}
						case STEP_TO_3:
						{
							if (var == 2)
							{
								changeQuestStep(env, 2, 93, false);
								final WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310070000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService2.teleportTo(player, 310070000, newInstance.getInstanceId(), 338, 101, 1191);
								return closeDialogWindow(env);
							}
						}
						case STEP_TO_7:
						{
							TeleportService2.teleportTo(player, 110010000, 1878.27f, 1513.74f, 812.675f, (byte) 25, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 8, 9);
						}
					}
					break;
				}
				case 205110:
				{ // Icaronix.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 93)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_4:
						{
							if (var == 93)
							{
								changeQuestStep(env, 93, 94, false);
								player.setState(CreatureState.FLIGHT_TELEPORT);
								player.unsetState(CreatureState.ACTIVE);
								player.setFlightTeleportId(31001);
								PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 31001, 0));
								return true;
							}
						}
					}
					break;
				}
				case 700240:
				{ // Icaronix's Box.
					if (dialog == QuestDialog.USE_OBJECT)
					{
						if (var == 94)
						{
							return playQuestMovie(env, 155);
						}
					}
					break;
				}
				case 205111:
				{ // Ecus.
					switch (dialog)
					{
						case USE_OBJECT:
						{
							if (var == 96)
							{
								if (isStigmaEquipped(env))
								{
									return sendQuestDialog(env, 2716);
								}
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 1));
								return closeDialogWindow(env);
							}
						}
						case START_DIALOG:
						{
							if (var == 98)
							{
								return sendQuestDialog(env, 2375);
							}
						}
						case SELECT_ACTION_2546:
						{
							if (var == 98)
							{
								if (giveQuestItem(env, getStoneId(player), 1))
								{
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 1));
									return true;
								}
							}
						}
						case SELECT_ACTION_2720:
						{
							if (var == 96)
							{
								final Npc npc = (Npc) env.getVisibleObject();
								npc.getController().delete();
								QuestService.addNewSpawn(310070000, player.getInstanceId(), 212992, (float) 195.3323, (float) 265.31827, (float) 1374.1426, (byte) 8);
								changeQuestStep(env, 96, 97, false);
								return closeDialogWindow(env);
							}
						}
					}
					break;
				}
				case 203701:
				{ // Lavirintos.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 9)
							{
								return sendQuestDialog(env, 3398);
							}
						}
						case STEP_TO_8:
						{
							return defaultCloseDialog(env, 9, 9, true, false);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203711)
			{ // Miriya.
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		final Player player = env.getPlayer();
		if (movieId == 155)
		{
			QuestService.addNewSpawn(310070000, player.getInstanceId(), 205111, (float) 197.6, (float) 265.9, (float) 1374.0, (byte) 0);
			changeQuestStep(env, 94, 98, false);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onEquipItemEvent(QuestEnv env, int itemId)
	{
		changeQuestStep(env, 98, 96, false);
		return closeDialogWindow(env);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 97)
			{
				changeQuestStep(env, 97, 8, false);
				TeleportService2.teleportTo(player, 210030000, 2315.9f, 1800.0f, 195.2f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
				return true;
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
			final int var = qs.getQuestVars().getQuestVars();
			if ((var >= 93) && (var <= 98))
			{
				removeStigma(env);
				qs.setQuestVar(2);
				updateQuestStatus(env);
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
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVars().getQuestVars();
			if (player.getWorldId() != 310070000)
			{
				if ((var >= 93) && (var <= 98))
				{
					removeStigma(env);
					qs.setQuestVar(2);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
				else if (var == 8)
				{
					removeStigma(env);
					return true;
				}
			}
		}
		return false;
	}
	
	private int getStoneId(Player player)
	{
		switch (player.getCommonData().getPlayerClass())
		{
			case GLADIATOR:
			{
				return 140000003; // Ferocious Strike III
			}
			case TEMPLAR:
			{
				return 140000003; // Ferocious Strike III
			}
			case ASSASSIN:
			{
				return 140000003; // Ferocious Strike III
			}
			case RANGER:
			{
				return 140000003; // Ferocious Strike III
			}
			case SORCERER:
			{
				return 140000002; // Flame Cage I
			}
			case SPIRIT_MASTER:
			{
				return 140000002; // Flame Cage I
			}
			case CLERIC:
			{
				return 140000002; // Flame Cage I
			}
			case CHANTER:
			{
				return 140000003; // Ferocious Strike III
			}
			case GUNSLINGER:
			{
				return 140000004; // Hydro Eruption II
			}
			case SONGWEAVER:
			{
				return 140000004; // Hydro Eruption II
			}
			case AETHERTECH:
			{
				return 140000004; // Hydro Eruption II
			}
			default:
			{
				return 0;
			}
		}
	}
	
	private boolean isStigmaEquipped(QuestEnv env)
	{
		final Player player = env.getPlayer();
		for (Item i : player.getEquipment().getEquippedItemsAllStigma())
		{
			if (i.getItemId() == getStoneId(player))
			{
				return true;
			}
		}
		return false;
	}
	
	private void removeStigma(QuestEnv env)
	{
		final Player player = env.getPlayer();
		for (Item item : player.getEquipment().getEquippedItemsByItemId(getStoneId(player)))
		{
			player.getEquipment().unEquipItem(item.getObjectId(), 0);
		}
		removeQuestItem(env, getStoneId(player), 1);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
}