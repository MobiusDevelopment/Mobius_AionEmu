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
public class _26822Traveling_With_The_Earth_Jotun extends QuestHandler
{
	private static final int questId = 26822;
	private static final int[] npcs =
	{
		220588,
		220590,
		806283
	};
	private static final int[] IDEternity02NepilimBoss75Ah =
	{
		220534
	}; // 타락한 물의 느빌림.
	
	public _26822Traveling_With_The_Earth_Jotun()
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
		for (int mob : IDEternity02NepilimBoss75Ah)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterWorld(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16822_A_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16822_B_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16822_C_301550000"), questId);
		qe.registerOnEnterZone(ZoneName.get("IDETERNITY_02_Q16822_D_301550000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 26821, true);
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
			if (targetId == 220588) // 전투중인 대지의 느빌림.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case SELECT_ACTION_1353:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1353);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 220590) // 대지의 느빌림.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case SELECT_ACTION_2035:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2035);
						}
					}
					case STEP_TO_4:
					{
						changeQuestStep(env, 3, 4, false);
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
		if (player.getWorldId() == 301550000)
		{ // 지식의 정원.
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
			if (var == 5)
			{
				final int[] IDEternity02NepilimBoss75Ah =
				{
					220534
				}; // 타락한 물의 느빌림.
				switch (targetId)
				{
					case 220534:
					{ // 타락한 물의 느빌림.
						qs.setQuestVar(6);
						updateQuestStatus(env);
						return defaultOnKillEvent(env, IDEternity02NepilimBoss75Ah, 0, 1, 1);
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
			if (zoneName == ZoneName.get("IDETERNITY_02_Q16822_A_301550000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("IDETERNITY_02_Q16822_B_301550000"))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("IDETERNITY_02_Q16822_C_301550000"))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("IDETERNITY_02_Q16822_D_301550000"))
			{
				if (var == 6)
				{
					qs.setQuestVar(7);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}