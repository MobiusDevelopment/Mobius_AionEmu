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
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24014Stomp_Out_The_Plot extends QuestHandler
{
	private static final int questId = 24014;
	
	public _24014Stomp_Out_The_Plot()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(203665).addOnTalkEvent(questId);
		qe.registerQuestNpc(203668).addOnTalkEvent(questId);
		qe.registerQuestNpc(210562).addOnKillEvent(questId);
		qe.registerQuestNpc(210563).addOnKillEvent(questId);
		qe.registerQuestNpc(210583).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24013, true);
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
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203665:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							break;
						case STEP_TO_1:
							return defaultCloseDialog(env, 0, 1);
					}
					break;
				case 203668:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
							else if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
							else if (var == 6)
							{
								return sendQuestDialog(env, 2034);
							}
							break;
						case STEP_TO_2:
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						case CHECK_COLLECTED_ITEMS:
							return checkQuestItems(env, 5, 5, true, 5, 2120);
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203668)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (((targetId == 210562) || (targetId == 210563) || (targetId == 210583)) && (var >= 2) && (var < 5))
		{
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}