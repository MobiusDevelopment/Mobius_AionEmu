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
package system.handlers.quest.inggison;

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

public class _10035Soar_To_The_Corridor extends QuestHandler
{
	private static final int questId = 10035;
	private static final int[] basrasaTrapper =
	{
		216775,
		220021,
		220022
	};
	
	public _10035Soar_To_The_Corridor()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			798928,
			799025,
			798958,
			798996,
			702663,
			798926
		};
		for (final int mob : basrasaTrapper)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("ANGRIEF_GATE_210050000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		final int[] inggisonQuests =
		{
			10031,
			10032,
			10033,
			10034
		};
		return defaultOnZoneMissionEndEvent(env, inggisonQuests);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		final int[] inggisonQuests =
		{
			10031,
			10032,
			10033,
			10034
		};
		return defaultOnLvlUpEvent(env, inggisonQuests, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 798928:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 7)
							{
								return sendQuestDialog(env, 3399);
							}
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
						case SET_REWARD:
						{
							qs.setQuestVar(8);
							updateQuestStatus(env);
							return defaultCloseDialog(env, 8, 8, true, false);
						}
					}
					break;
				}
				case 799025:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				}
				case 798958:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_3:
						{
							return defaultCloseDialog(env, 2, 3);
						}
					}
					break;
				}
				case 798996:
				{
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_4:
						{
							giveQuestItem(env, 182215629, 1);
							playQuestMovie(env, 517);
							return defaultCloseDialog(env, 3, 4);
						}
					}
					break;
				}
				case 702663:
				{
					switch (dialog)
					{
						case USE_OBJECT:
						{
							if (var == 6)
							{
								removeQuestItem(env, 182215629, 1);
								final Npc npc = (Npc) env.getVisibleObject();
								npc.getController().scheduleRespawn();
								npc.getController().onDelete();
								qs.setQuestVar(7);
								updateQuestStatus(env);
								return closeDialogWindow(env);
							}
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798926)
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
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
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 5)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 9))
				{
					return defaultOnKillEvent(env, basrasaTrapper, var1, var1 + 1, 1);
				}
				else if (var1 == 9)
				{
					qs.setQuestVar(6);
					updateQuestStatus(env);
					return true;
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
			if (zoneName.equals(ZoneName.get("ANGRIEF_GATE_210050000")))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}
}