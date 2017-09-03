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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _14040Orders_From_Reshanta extends QuestHandler
{
	private static final int questId = 14040;
	
	public _14040Orders_From_Reshanta()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(278501).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("TEMINON_FORTRESS_400010000"), questId);
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
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (targetId != 278501)
		{
			return false;
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (env.getDialog() == QuestDialog.START_DIALOG)
			{
				return sendQuestDialog(env, 10002);
			}
			else if (env.getDialogId() == 1009)
			{
				qs.setStatus(QuestStatus.REWARD);
				qs.setQuestVarById(0, 1);
				updateQuestStatus(env);
				return sendQuestDialog(env, 5);
			}
			return false;
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (env.getDialogId() == 23)
			{
				final int[] ids =
				{
					14041,
					14042,
					14043,
					14044,
					14045,
					14046,
					14047
				};
				for (final int id : ids)
				{
					QuestEngine.getInstance().onEnterZoneMissionEnd(new QuestEnv(env.getVisibleObject(), env.getPlayer(), id, env.getDialogId()));
				}
			}
			return sendQuestEndDialog(env);
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("TEMINON_FORTRESS_400010000"));
	}
}