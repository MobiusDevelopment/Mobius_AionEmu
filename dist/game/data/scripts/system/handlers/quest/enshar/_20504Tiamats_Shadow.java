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
package system.handlers.quest.enshar;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _20504Tiamats_Shadow extends QuestHandler
{
	public static final int questId = 20504;
	private static final int[] mobs =
	{
		219943,
		219944,
		219945,
		219946,
		219947,
		219948
	};
	
	public _20504Tiamats_Shadow()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			804730,
			804742,
			804731
		};
		for (final int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (final int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(219949).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20503, true);
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
			if (targetId == 804730)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_3:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804742)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 5)
						{
							return sendQuestDialog(env, 2716);
						}
					}
					case STEP_TO_6:
					{
						changeQuestStep(env, 5, 6, false);
						final Npc npc = (Npc) env.getVisibleObject();
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 804731)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 6)
						{
							return sendQuestDialog(env, 3057);
						}
					}
					case SET_REWARD:
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804730)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
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
		final Npc npc = (Npc) env.getVisibleObject();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			final int var2 = qs.getQuestVarById(2);
			final int targetId = env.getTargetId();
			if (var == 1)
			{
				final int[] mobs1 =
				{
					219943,
					219944,
					219945
				};
				switch (targetId)
				{
					case 219943: // Beritra Defense Scaleblade.
					case 219944: // Beritra Defense Talonscout.
					case 219945:
					{ // Beritra Defense Wyrmtongue.
						if ((var1 >= 0) && (var1 < 4))
						{
							return defaultOnKillEvent(env, mobs1, var1, var1 + 1, 1);
						}
						else if (var1 == 4)
						{
							qs.setQuestVar(2);
							updateQuestStatus(env);
							return true;
						}
						break;
					}
				}
			}
			else if (var == 3)
			{
				final int[] mobs2 =
				{
					219946,
					219947,
					219948
				};
				switch (targetId)
				{
					case 219946: // Vengeful Aetheric Guard Dominator.
					case 219947: // Vengeful Aetheric Guard Swiftshank.
					case 219948:
					{ // Vengeful Aetheric Guard Seersage.
						if ((var2 >= 0) && (var2 < 4))
						{
							return defaultOnKillEvent(env, mobs2, var2, var2 + 1, 2);
						}
						else if (var2 == 4)
						{
							qs.setQuestVar(4);
							updateQuestStatus(env);
							return true;
						}
						break;
					}
				}
			}
			else if (var == 4)
			{
				switch (targetId)
				{
					case 219949:
					{ // Cursed Gilgamesh.
						QuestService.addNewSpawn(220080000, 1, 804742, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
						qs.setQuestVar(5);
						updateQuestStatus(env);
						return true;
					}
				}
			}
		}
		return false;
	}
}