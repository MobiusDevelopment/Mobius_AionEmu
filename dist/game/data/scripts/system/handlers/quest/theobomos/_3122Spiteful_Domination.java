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
package system.handlers.quest.theobomos;

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

public class _3122Spiteful_Domination extends QuestHandler
{
	private static final int questId = 3122;
	
	public _3122Spiteful_Domination()
	{
		super(questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		return defaultOnEnterZoneEvent(env, zoneName, ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"));
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798155).addOnTalkEvent(questId);
		qe.registerQuestNpc(237245).addOnKillEvent(questId); // Suspicious Pot.
		qe.registerQuestNpc(237240).addOnKillEvent(questId); // Enthralled Gutorum.
		qe.registerQuestNpc(237241).addOnKillEvent(questId); // Enthralled Karemiwen.
		qe.registerQuestNpc(237243).addOnKillEvent(questId); // Enthralled Zeeturun.
		qe.registerQuestNpc(237244).addOnKillEvent(questId); // Enthralled Lannok.
		qe.registerQuestNpc(237239).addOnKillEvent(questId); // Death Reaper.
		qe.registerOnEnterZone(ZoneName.get("ADMA_STRONGHOLD_INTERIOR_320130000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 0)
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
					{
						return sendQuestStartDialog(env);
					}
					case REFUSE_QUEST_SIMPLE:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 798155:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 10002);
						}
						case SELECT_REWARD:
						{
							return sendQuestEndDialog(env);
						}
						default:
						{
							return sendQuestEndDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798155)
			{
				switch (dialog)
				{
					case SELECT_REWARD:
					{
						return sendQuestDialog(env, 5);
					}
					default:
					{
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{ // Suspicious Pot.
				return defaultOnKillEvent(env, 237245, 0, 1);
			}
			else if (var == 1)
			{ // Enthralled Gutorum.
				return defaultOnKillEvent(env, 237240, 1, 2);
			}
			else if (var == 2)
			{ // Enthralled Karemiwen.
				return defaultOnKillEvent(env, 237241, 2, 3);
			}
			else if (var == 3)
			{ // Enthralled Zeeturun.
				return defaultOnKillEvent(env, 237243, 3, 4);
			}
			else if (var == 4)
			{ // Enthralled Lannok.
				return defaultOnKillEvent(env, 237244, 4, 5);
			}
			else if (var == 5)
			{ // Death Reaper.
				return defaultOnKillEvent(env, 237239, 5, true);
			}
		}
		return false;
	}
}