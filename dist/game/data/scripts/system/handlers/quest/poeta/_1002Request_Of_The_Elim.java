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
package system.handlers.quest.poeta;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ASCENSION_MORPH;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rinzler (Encom)
 */
public class _1002Request_Of_The_Elim extends QuestHandler
{
	private static final int questId = 1002;
	
	public _1002Request_Of_The_Elim()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			203076,
			730007,
			730010,
			730008,
			205000,
			203067
		};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 1001, true);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203076: // Ampeis.
				{
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
				case 730007: // Forest Protector Noah.
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
							else if (var == 5)
							{
								return sendQuestDialog(env, 1693);
							}
							else if (var == 6)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case SELECT_ACTION_1353:
						{
							if (var == 1)
							{
								playQuestMovie(env, 20);
								return sendQuestDialog(env, 1353);
							}
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2, 182200002, 1, 0, 0);
						}
						case STEP_TO_3:
						{
							return defaultCloseDialog(env, 5, 6, 0, 0, 182200002, 1);
						}
						case CHECK_COLLECTED_ITEMS:
						{
							if (var == 6)
							{
								return checkQuestItems(env, 6, 12, false, 2120, 2205);
							}
							else if (var == 12)
							{
								return sendQuestDialog(env, 2120);
							}
						}
						case STEP_TO_4:
						{
							return defaultCloseDialog(env, 12, 13);
						}
						case FINISH_DIALOG:
						{
							return sendQuestSelectionDialog(env);
						}
					}
					break;
				}
				case 730010: // Sleeping Elder.
				{
					if (dialog == QuestDialog.USE_OBJECT)
					{
						if (player.getInventory().getItemCountByItemId(182200002) == 1)
						{
							if (var == 2)
							{
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								useQuestObject(env, 2, 4, false, false);
							}
							else if (var == 4)
							{
								((Npc) env.getVisibleObject()).getController().scheduleRespawn();
								((Npc) env.getVisibleObject()).getController().onDelete();
								return useQuestObject(env, 4, 5, false, false);
							}
						}
					}
					break;
				}
				case 730008: // Daminu.
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 13)
							{
								return sendQuestDialog(env, 2375);
							}
							else if (var == 14)
							{
								return sendQuestDialog(env, 2461);
							}
						}
						case STEP_TO_5:
						{
							final WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(310010000);
							TeleportService2.teleportTo(player, 310010000, newInstance.getInstanceId(), 52, 174, 229);
							changeQuestStep(env, 13, 20, false);
							return closeDialogWindow(env);
						}
						case STEP_TO_6:
						{
							return defaultCloseDialog(env, 14, 14, true, false);
						}
					}
					break;
				}
				case 205000: // Belpartan.
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 20)
							{
								player.setState(CreatureState.FLIGHT_TELEPORT);
								player.unsetState(CreatureState.ACTIVE);
								player.setFlightTeleportId(1001);
								PacketSendUtility.sendPacket(player, new SM_EMOTION(player, EmotionType.START_FLYTELEPORT, 1001, 0));
								ThreadPoolManager.getInstance().schedule(() ->
								{
									changeQuestStep(env, 20, 14, false);
									TeleportService2.teleportTo(player, 210010000, 1, 603, 1537, 116, (byte) 20);
								}, 43000);
								return true;
							}
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203067) // Kalio.
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 2716);
				}
				return sendQuestEndDialog(env);
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
			if (player.getWorldId() == 310010000)
			{
				PacketSendUtility.sendPacket(player, new SM_ASCENSION_MORPH(1));
				return true;
			}
			final int var = qs.getQuestVarById(0);
			if (var == 20)
			{
				changeQuestStep(env, 20, 13, false);
			}
		}
		return false;
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		final int targetId = env.getTargetId();
		if (targetId == 730010)
		{
			if ((qs == null) || (qs.getStatus() != QuestStatus.START))
			{
				return false;
			}
			final int var = qs.getQuestVarById(0);
			if (((var != 2) && (var != 4)))
			{
				return false;
			}
		}
		return true;
	}
}