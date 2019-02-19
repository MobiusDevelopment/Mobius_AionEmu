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
package system.handlers.quest.cradle_of_eternity;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _26823Reunion_With_Agent_Peregrine extends QuestHandler
{
	private static final int questId = 26823;
	private static final int[] npcs =
	{
		806289,
		806290
	};
	private static final int[] IDEternity02EventGuardFiLi =
	{
		220613,
		220614,
		220616,
		220617
	};
	
	public _26823Reunion_With_Agent_Peregrine()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : IDEternity02EventGuardFiLi)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(220540).addOnKillEvent(questId); // 피톤.
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16823_A_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16823_B_301550000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 26822, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs == null ? 0 : qs.getQuestVarById(0);
			if (targetId == 806289) // 페레그란.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1694);
						}
					}
					case SELECT_ACTION_1695:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1695);
						}
					}
					case STEP_TO_3:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			QuestService.finishQuest(env);
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (player.getWorldId() == 301550000) // 지식의 정원.
		{
			if (qs == null)
			{
				env.setQuestId(questId);
				if (QuestService.startQuest(env))
				{
					return true;
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
		final int targetId = env.getTargetId();
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 4))
				{
					return defaultOnKillEvent(env, IDEternity02EventGuardFiLi, var1, var1 + 1, 1);
				}
				else if (var1 == 4)
				{
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 4)
			{
				switch (targetId)
				{
					case 220540: // 피톤.
					{
						playQuestMovie(env, 942);
						qs.setQuestVar(5);
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return true;
					}
				}
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
			if (zoneName == ZoneName.get("IDETERNITY_02_Q16823_A_301550000"))
			{
				if (var == 1)
				{
					playQuestMovie(env, 941);
					changeQuestStep(env, 1, 2, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("IDETERNITY_02_Q16823_B_301550000"))
			{
				if (var == 3)
				{
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			}
		}
		return false;
	}
}