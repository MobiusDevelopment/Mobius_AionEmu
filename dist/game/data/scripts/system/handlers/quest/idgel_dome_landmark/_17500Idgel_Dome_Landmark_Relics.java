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
package system.handlers.quest.idgel_dome_landmark;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _17500Idgel_Dome_Landmark_Relics extends QuestHandler
{
	private static final int questId = 17500;
	
	public _17500Idgel_Dome_Landmark_Relics()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806262).addOnQuestStart(questId); // 모리세.
		qe.registerQuestNpc(806262).addOnTalkEvent(questId); // 모리세.
		qe.registerQuestNpc(806263).addOnTalkEvent(questId); // 넬레우스.
		qe.registerOnEnterZone(ZoneName.get("IDLDF5_FORTRESS_WAR_Q17500_A_301680000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDLDF5_FORTRESS_WAR_Q17500_B_301680000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806262)
			{ // 모리세.
				switch (env.getDialog())
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
					case STEP_TO_1:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 806263)
			{ // 넬레우스.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1353);
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806262)
			{ // 모리세.
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("IDLDF5_FORTRESS_WAR_Q17500_A_301680000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("IDLDF5_FORTRESS_WAR_Q17500_B_301680000"))
			{
				if (var == 2)
				{
					qs.setQuestVar(3);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}