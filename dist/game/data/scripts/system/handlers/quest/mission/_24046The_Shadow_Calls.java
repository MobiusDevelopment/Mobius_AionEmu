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
package system.handlers.quest.mission;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24046The_Shadow_Calls extends QuestHandler
{
	private static final int questId = 24046;
	private static final int[] npcs =
	{
		798300,
		204253,
		700369,
		204089,
		203550
	};
	
	public _24046The_Shadow_Calls()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnDie(questId);
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLeaveZone(ZoneName.get("BALTASAR_HILL_VILLAGE_220050000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24045, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if (qs == null)
		{
			return false;
		}
		final Npc target = (Npc) env.getVisibleObject();
		final int targetId = target.getNpcId();
		final int var = qs.getQuestVarById(0);
		final QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 798300:
				{ // Phyper.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					if (dialog == QuestDialog.STEP_TO_1)
					{
						return defaultCloseDialog(env, 0, 1);
					}
					break;
				}
				case 204253:
				{ // Khrudgelmir.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 2))
					{
						return sendQuestDialog(env, 1693);
					}
					if ((dialog == QuestDialog.START_DIALOG) && (var == 6))
					{
						return sendQuestDialog(env, 3057);
					}
					if (dialog == QuestDialog.STEP_TO_3)
					{
						removeQuestItem(env, 182205502, 1);
						return defaultCloseDialog(env, 2, 3);
					}
					if (dialog == QuestDialog.SET_REWARD)
					{
						return defaultCloseDialog(env, 6, 6, true, false);
					}
					break;
				}
				case 700369:
				{ // Shadow Court Dungeon Exit.
					if ((dialog == QuestDialog.USE_OBJECT) && (var == 5))
					{
						TeleportService2.teleportTo(player, 120010000, 1003.02637f, 1531.2028f, 222.19403f, (byte) 105, TeleportAnimation.BEAM_ANIMATION);
						changeQuestStep(env, 5, 6, false);
						return true;
					}
					break;
				}
				case 204089:
				{ // Garm.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 3))
					{
						return sendQuestDialog(env, 2034);
					}
					if (dialog == QuestDialog.STEP_TO_4)
					{
						final WorldMapInstance newInstance = InstanceService.getNextAvailableInstance(320120000); // Shadow Court Dungeon.
						InstanceService.registerPlayerWithInstance(newInstance, player);
						TeleportService2.teleportTo(player, 320120000, newInstance.getInstanceId(), 591.47894f, 420.20865f, 202.97754f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
						changeQuestStep(env, 3, 5, false);
						return closeDialogWindow(env);
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203550)
			{ // Munin.
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				else
				{
					final int[] questItems =
					{
						182205502
					};
					return sendQuestEndDialog(env, questItems);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onLeaveZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if ((zoneName == ZoneName.get("BALTASAR_HILL_VILLAGE_220050000")) && (var == 1))
			{
				giveQuestItem(env, 182205502, 1);
				changeQuestStep(env, 1, 2, false);
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
		if ((player.getWorldId() == 320120000) || (qs == null) || (qs.getStatus() != QuestStatus.START))
		{ // Shadow Court Dungeon.
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (var == 5)
		{
			qs.setQuestVarById(0, 3);
			updateQuestStatus(env);
			return true;
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
		final int var = qs.getQuestVarById(0);
		if (var == 5)
		{
			qs.setQuestVarById(0, 3);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}