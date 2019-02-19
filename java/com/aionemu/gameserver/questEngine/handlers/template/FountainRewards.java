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
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Wakizashi, vlog, Bobobear
 * @reworked Luzien
 */
public class FountainRewards extends QuestHandler
{
	private final int questId;
	private final Set<Integer> startNpcs = new HashSet<>();
	
	public FountainRewards(int questId, List<Integer> startNpcIds)
	{
		super(questId);
		this.questId = questId;
		startNpcs.addAll(startNpcIds);
		startNpcs.remove(0);
	}
	
	@Override
	public void register()
	{
		final Iterator<Integer> iterator = startNpcs.iterator();
		while (iterator.hasNext())
		{
			final int startNpc = iterator.next();
			qe.registerQuestNpc(startNpc).addOnQuestStart(getQuestId());
			qe.registerQuestNpc(startNpc).addOnTalkEvent(getQuestId());
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (startNpcs.contains(targetId))
			{
				switch (dialog)
				{
					case USE_OBJECT:
					{
						if (!QuestService.inventoryItemCheck(env, true))
						{
							return true;
						}
						if ((targetId == 730142) || // Teminon Coin Fountain.
							(targetId == 730143) || // Primum Coin Fountain.
							(targetId == 730241) || // Inggison Coin Fountain.
							(targetId == 730242) || // Gelkmaros Coin Fountain.
							(targetId == 701429) || // Oriel Coin Fountain.
							(targetId == 701430) || // Pernon Coin Fountain.
							(targetId == 804759) || // Enshar Coin Fountain.
							(targetId == 804788) || // Cygnea Coin Fountain.
							(targetId == 805778) || // Iluma Coin Fountain.
							(targetId == 805753))
						{ // Norsvold Coin Fountain.
							return sendQuestDialog(env, 1011);
						}
						return sendQuestSelectionDialog(env);
					}
					case STEP_TO_1:
					{
						if (QuestService.collectItemCheck(env, false))
						{
							if (!player.getInventory().isFullSpecialCube())
							{
								if (QuestService.startQuest(env))
								{
									changeQuestStep(env, 0, 0, true);
									return sendQuestDialog(env, 5);
								}
							}
							else
							{
								PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_FULL_INVENTORY);
								return sendQuestSelectionDialog(env);
							}
						}
						else
						{
							return sendQuestSelectionDialog(env);
						}
					}
				}
			}
		}
		else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
		{
			if (startNpcs.contains(targetId))
			{
				if (dialog == QuestDialog.SELECT_NO_REWARD)
				{
					if (QuestService.collectItemCheck(env, true))
					{
						return sendQuestEndDialog(env);
					}
				}
				else
				{
					return QuestService.abandonQuest(player, questId);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects)
	{
		if (startNpcs.contains(env.getTargetId()))
		{
			return true;
		}
		return false;
	}
}