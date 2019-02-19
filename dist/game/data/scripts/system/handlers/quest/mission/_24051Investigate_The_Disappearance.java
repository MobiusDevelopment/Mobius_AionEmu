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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _24051Investigate_The_Disappearance extends QuestHandler
{
	private static final int questId = 24051;
	private static final int[] npcs =
	{
		204707,
		204749,
		204800,
		700359
	};
	
	public _24051Investigate_The_Disappearance()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215375, questId);
		qe.registerOnMovieEndQuest(236, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("MINE_PORT_220040000"), questId);
		qe.registerOnEnterWorld(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24050, true);
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
				case 204707:
				{ // Mani.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					if ((dialog == QuestDialog.START_DIALOG) && (var == 3))
					{
						return sendQuestDialog(env, 2034);
					}
					if (dialog == QuestDialog.STEP_TO_1)
					{
						return defaultCloseDialog(env, 0, 1);
					}
					if (dialog == QuestDialog.STEP_TO_4)
					{
						return defaultCloseDialog(env, 3, 4);
					}
					break;
				}
				case 204749:
				{ // Paeru.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 1))
					{
						return sendQuestDialog(env, 1352);
					}
					if (dialog == QuestDialog.STEP_TO_2)
					{
						return defaultCloseDialog(env, 1, 2, 182215375, 1, 0, 0);
					}
					break;
				}
				case 204800:
				{ // Hammel.
					if ((dialog == QuestDialog.START_DIALOG) && (var == 4))
					{
						return sendQuestDialog(env, 2375);
					}
					if (dialog == QuestDialog.STEP_TO_5)
					{
						giveQuestItem(env, 182215376, 1);
						return defaultCloseDialog(env, 4, 5);
					}
					break;
				}
				case 700359:
				{ // Port.
					if ((dialog == QuestDialog.USE_OBJECT) && (var == 5) && (player.getInventory().getItemCountByItemId(182215377) >= 1))
					{
						TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), 1757.82f, 1392.94f, 401.75f, (byte) 94);
						return true;
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204707)
			{ // Mani.
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					removeQuestItem(env, 182215376, 1);
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
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
			final int var = qs.getQuestVarById(0);
			if (var == 2)
			{
				return HandlerResult.fromBoolean(useQuestItem(env, item, 2, 3, false));
			}
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName name)
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
			if (name == ZoneName.get("MINE_PORT_220040000"))
			{
				if (var == 5)
				{
					ThreadPoolManager.getInstance().schedule(() -> playQuestMovie(env, 236), 10000);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		if (movieId != 236)
		{
			return false;
		}
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			changeQuestStep(env, 5, 5, true);
			removeQuestItem(env, 182215377, 1);
			return true;
		}
		return false;
	}
}