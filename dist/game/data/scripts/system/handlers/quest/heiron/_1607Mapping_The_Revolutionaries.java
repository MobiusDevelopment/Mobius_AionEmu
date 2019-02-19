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
package system.handlers.quest.heiron;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _1607Mapping_The_Revolutionaries extends QuestHandler
{
	private static final int questId = 1607;
	
	public _1607Mapping_The_Revolutionaries()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestItem(182201744, questId);
		qe.registerQuestNpc(204578).addOnTalkEvent(questId);
		qe.registerQuestNpc(204574).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1607A_206074_12_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1607B_206075_5_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1607C_206076_4_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1607D_206077_1_210040000"), questId);
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (QuestService.startQuest(env))
			{
				return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
			}
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if (qs == null)
		{
			return false;
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			final int var2 = qs.getQuestVarById(2);
			final int var3 = qs.getQuestVarById(3);
			final int var4 = qs.getQuestVarById(4);
			if (targetId == 204578)
			{ // Kuobe.
				switch (dialog)
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case STEP_TO_1:
					{
						return defaultCloseDialog(env, 0, 1);
					}
				}
			}
			else if (targetId == 204574)
			{ // Finn.
				if (dialog == QuestDialog.START_DIALOG)
				{
					if ((var == 1) && (var1 == 1) && (var2 == 1) && (var3 == 1) && (var4 == 1))
					{
						return sendQuestDialog(env, 10002);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD)
				{
					changeQuestStep(env, 1, 1, true);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204574)
			{ // Finn.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
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
			final int var1 = qs.getQuestVarById(1);
			final int var2 = qs.getQuestVarById(2);
			final int var3 = qs.getQuestVarById(3);
			final int var4 = qs.getQuestVarById(4);
			if (var == 1)
			{
				if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1607A_206074_12_210040000"))
				{
					if (var1 == 0)
					{
						changeQuestStep(env, 0, 1, false, 1);
						return true;
					}
				}
				else if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1607B_206075_5_210040000"))
				{
					if (var2 == 0)
					{
						changeQuestStep(env, 0, 1, false, 2);
						return true;
					}
				}
				else if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1607C_206076_4_210040000"))
				{
					if (var3 == 0)
					{
						changeQuestStep(env, 0, 1, false, 3);
						return true;
					}
				}
				else if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1607D_206077_1_210040000"))
				{
					if (var4 == 0)
					{
						changeQuestStep(env, 0, 1, false, 4);
						return true;
					}
				}
			}
		}
		return false;
	}
}