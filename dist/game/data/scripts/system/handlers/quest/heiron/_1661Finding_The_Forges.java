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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
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

public class _1661Finding_The_Forges extends QuestHandler
{
	private static final int questId = 1661;
	
	public _1661Finding_The_Forges()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204600).addOnQuestStart(questId);
		qe.registerQuestNpc(204600).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1661A_206045_3_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1661B_206046_6_210040000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF3_SENSORYAREA_Q1661C_206047_10_210040000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204600)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (dialog == QuestDialog.ACCEPT_QUEST)
				{
					playQuestMovie(env, 200);
					return sendQuestStartDialog(env);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204600)
			{
				if (dialog == QuestDialog.START_DIALOG)
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
			if (var == 1)
			{
				if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1661A_206045_3_210040000"))
				{
					if (var1 == 0)
					{
						changeQuestStep(env, 0, 16, false);
						return true;
					}
				}
				else if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1661B_206046_6_210040000"))
				{
					if (var2 == 16)
					{
						changeQuestStep(env, 16, 48, false);
						return true;
					}
				}
				else if (zoneName == ZoneName.get("LF3_SENSORYAREA_Q1661C_206047_10_210040000"))
				{
					if (var3 == 48)
					{
						changeQuestStep(env, 48, 48, true);
						return true;
					}
				}
			}
		}
		return false;
	}
}