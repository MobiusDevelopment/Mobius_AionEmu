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
package system.handlers.quest.prelude_to_chaos;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _20101Unraveling_The_Mystery extends QuestHandler
{
	private static final int questId = 20101;
	private static final int[] npcs =
	{
		802463,
		731531,
		804556,
		731532,
		802361,
		802433
	};
	
	public _20101Unraveling_The_Mystery()
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
		qe.registerQuestNpc(234680).addOnKillEvent(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("LINKGATE_FOUNDRY_SENSORYAREA_Q20101_301340000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20100, true);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		return defaultOnKillEvent(env, 234680, 2, 4);
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
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 802463:
				{ // Kahrun.
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
							return defaultCloseDialog(env, 0, 1, 182215523, 1, 0, 0);
						}
					}
					break;
				}
				case 731531:
				{ // Stonepike Invasion Corridor.
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
							if ((var == 1) && (player.getInventory().getItemCountByItemId(182215523) == 1))
							{
								final WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(301340000);
								InstanceService.registerPlayerWithInstance(newInstance, player);
								TeleportService2.teleportTo(player, 301340000, newInstance.getInstanceId(), 243, 333, 392, (byte) 91, TeleportAnimation.BEAM_ANIMATION);
								changeQuestStep(env, 1, 2, false, 0);
								return sendQuestDialog(env, 10000);
							}
							return sendQuestDialog(env, 10001);
						}
					}
					break;
				}
				case 804556:
				{ // Spy Ditono.
					switch (dialog)
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
							removeQuestItem(env, 182215523, 1);
							return defaultCloseDialog(env, 5, 6, 182215522, 1, 0, 0);
						}
					}
					break;
				}
				case 731532:
				{ // Secret Zone Invasion Corridor.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 6)
							{
								return sendQuestDialog(env, 3057);
							}
						}
						case STEP_TO_7:
						{
							if ((var == 6) && (player.getInventory().getItemCountByItemId(182215522) == 1))
							{
								TeleportService2.teleportTo(player, 600090000, 364.02426f, 926.74615f, 90.12348f, (byte) 60, TeleportAnimation.BEAM_ANIMATION);
								changeQuestStep(env, 6, 7, false, 0);
								return closeDialogWindow(env);
							}
						}
					}
					break;
				}
				case 802361:
				{ // Kisian.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 7)
							{
								return sendQuestDialog(env, 3398);
							}
							else if (var == 8)
							{
								return sendQuestDialog(env, 3739);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							removeQuestItem(env, 182215522, 1);
							return checkQuestItems(env, 7, 8, false, 10000, 10001);
						}
						case SET_REWARD:
						{
							giveQuestItem(env, 182215578, 1);
							TeleportService2.teleportTo(player, 600090000, 407.16f, 1371.84f, 164.7f, (byte) 84, TeleportAnimation.BEAM_ANIMATION);
							return defaultCloseDialog(env, 8, 9, true, false);
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 802433)
			{ // Keroz.
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					removeQuestItem(env, 182215578, 1);
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("LINKGATE_FOUNDRY_SENSORYAREA_Q20101_301340000"))
		{
			final Player player = env.getPlayer();
			if (player == null)
			{
				return false;
			}
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if (var == 4)
				{
					playQuestMovie(env, 903);
					changeQuestStep(env, 4, 5, false);
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
			if (var >= 1)
			{
				qs.setQuestVar(0);
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
			if (var >= 1)
			{
				qs.setQuestVar(0);
				updateQuestStatus(env);
				return true;
			}
		}
		return false;
	}
}