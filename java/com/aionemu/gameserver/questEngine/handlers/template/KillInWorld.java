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
package com.aionemu.gameserver.questEngine.handlers.template;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.services.VortexService;

public class KillInWorld extends QuestHandler
{
	private final int questId;
	private final Set<Integer> startNpcs = new HashSet<>();
	private final Set<Integer> endNpcs = new HashSet<>();
	private final Set<Integer> worldIds = new HashSet<>();
	private final int killAmount;
	private final int invasionWorldId;
	
	public KillInWorld(int questId, List<Integer> endNpcIds, List<Integer> startNpcIds, List<Integer> worldIds, int killAmount, int invasionWorld)
	{
		super(questId);
		if (startNpcIds != null)
		{
			startNpcs.addAll(startNpcIds);
			startNpcs.remove(0);
		}
		if (endNpcIds == null)
		{
			endNpcs.addAll(startNpcs);
		}
		else
		{
			endNpcs.addAll(endNpcIds);
			endNpcs.remove(0);
		}
		this.questId = questId;
		this.worldIds.addAll(worldIds);
		this.worldIds.remove(0);
		this.killAmount = killAmount;
		invasionWorldId = invasionWorld;
	}
	
	@Override
	public void register()
	{
		Iterator<Integer> iterator = startNpcs.iterator();
		while (iterator.hasNext())
		{
			final int startNpc = iterator.next();
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}
		iterator = endNpcs.iterator();
		while (iterator.hasNext())
		{
			final int endNpc = iterator.next();
			qe.registerQuestNpc(endNpc).addOnTalkEvent(getQuestId());
		}
		iterator = worldIds.iterator();
		while (iterator.hasNext())
		{
			final int worldId = iterator.next();
			qe.registerOnKillInWorld(worldId, questId);
		}
		if (invasionWorldId != 0)
		{
			qe.registerOnEnterWorld(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (startNpcs.isEmpty() || startNpcs.contains(targetId))
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					{
						return sendQuestStartDialog(env);
					}
					default:
					{
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
		{
			if (endNpcs.contains(targetId))
			{
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
		final VortexLocation vortexLoc = VortexService.getInstance().getLocationByWorld(invasionWorldId);
		if (player.getWorldId() == invasionWorldId)
		{
			if (((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat()))
			{
				if (((vortexLoc != null) && vortexLoc.isActive()) || (searchOpenRift()))
				{
					return QuestService.startQuest(env);
				}
			}
		}
		return false;
	}
	
	private boolean searchOpenRift()
	{
		for (final RiftLocation loc : RiftService.getInstance().getRiftLocations().values())
		{
			if ((loc.getWorldId() == invasionWorldId) && loc.isOpened())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillInWorldEvent(QuestEnv env)
	{
		return defaultOnKillRankedEvent(env, 0, killAmount, true);
	}
}