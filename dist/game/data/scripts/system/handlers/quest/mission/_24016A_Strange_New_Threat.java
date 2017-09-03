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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24016A_Strange_New_Threat extends QuestHandler
{
	private static final int questId = 24016;
	private static final int[] npcs =
	{
		203557,
		700140,
		700184
	};
	
	public _24016A_Strange_New_Threat()
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
		qe.registerOnMovieEndQuest(154, questId);
		qe.registerQuestNpc(233876).addOnKillEvent(questId);
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("BREGIRUN_320030000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		final int[] altgardQuests =
		{
			24010,
			24011,
			24012,
			24013,
			24014,
			24015
		};
		return defaultOnZoneMissionEndEvent(env, altgardQuests);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final int[] altgardQuests =
		{
			24010,
			24011,
			24012,
			24013,
			24014,
			24015
		};
		return defaultOnLvlUpEvent(env, altgardQuests, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203557: // Suthran.
					if ((env.getDialog() == QuestDialog.START_DIALOG) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					else if (env.getDialog() == QuestDialog.STEP_TO_1)
					{
						TeleportService2.teleportTo(player, 220030000, 2453.1934f, 2555.148f, 316.267f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
					else if (env.getDialogId() == 1013)
					{
						playQuestMovie(env, 66);
						return sendQuestDialog(env, 1013);
					}
					break;
				case 700184: // Abyss Gate.
					playQuestMovie(env, 154);
					return useQuestObject(env, 4, 4, true, false);
				case 700140: // Abyss Gate Guardian Stone.
					if (dialog == QuestDialog.USE_OBJECT)
					{
						QuestService.addNewSpawn(320030000, player.getInstanceId(), 233876, (float) 251.91177, (float) 262.239, (float) 228.30093, (byte) 89);
						return useQuestObject(env, 2, 3, false, false);
					}
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().onDelete();
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203557)
			{ // Suthran.
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 1352);
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
	public boolean onDieEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVars().getQuestVars();
			if (var == 2)
			{
				changeQuestStep(env, 2, 1, false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("BREGIRUN_320030000"))
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
			if (qs.getQuestVars().getQuestVars() == 1)
			{
				changeQuestStep(env, 1, 2, false);
				return true;
			}
			else if (qs.getQuestVars().getQuestVars() == 2)
			{
				changeQuestStep(env, 2, 1, false);
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		return defaultOnKillEvent(env, 233876, 3, 4);
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		final Player player = env.getPlayer();
		if (movieId == 154)
		{
			TeleportService2.teleportTo(player, 220030000, 1683.0532f, 1758.4905f, 259.49335f, (byte) 68, TeleportAnimation.BEAM_ANIMATION);
			return true;
		}
		return false;
	}
}