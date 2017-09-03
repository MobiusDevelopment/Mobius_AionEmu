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

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _3502Nereus_Need_You extends QuestHandler
{
	private static final int questId = 3502;
	
	private static final int[] npcs =
	{
		204656,
		730192
	};
	private static final int[] mobs =
	{
		214894,
		214895,
		214896,
		214897,
		214904
	};
	
	public _3502Nereus_Need_You()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204656).addOnQuestStart(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		final QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 730192:
				{ // Balaur Operation Orders.
					if ((dialog == QuestDialog.USE_OBJECT) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					if (dialog == QuestDialog.STEP_TO_1)
					{
						return defaultCloseDialog(env, 0, 1);
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204656)
			{ // Maloren.
				if (dialog == QuestDialog.START_DIALOG)
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
		final Npc npc = (Npc) env.getVisibleObject();
		final int targetId = npc.getNpcId();
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			final int var2 = qs.getQuestVarById(2);
			final int var3 = qs.getQuestVarById(3);
			switch (targetId)
			{
				case 214894: // Telepathy Controller.
					if (var == 1)
					{
						return defaultOnKillEvent(env, 214894, 1, 2, 0);
					}
					break;
				case 214895: // Main Power Generator.
					if ((var == 2) && (var1 != 1))
					{
						defaultOnKillEvent(env, 214895, 0, 1, 1);
						if ((var2 == 1) && (var3 == 1))
						{
							return true;
						}
						return true;
					}
					break;
				case 214896: // Auxiliary Power Generator.
					if ((var == 2) && (var2 != 1))
					{
						defaultOnKillEvent(env, 214896, 0, 1, 2);
						if ((var1 == 1) && (var3 == 1))
						{
							return true;
						}
						return true;
					}
					break;
				case 214897: // Emergency Generator.
					if ((var == 2) && (var3 != 1))
					{
						defaultOnKillEvent(env, 214897, 0, 1, 3);
						if ((var1 == 1) && (var2 == 1))
						{
							return true;
						}
						return true;
					}
					break;
				case 214904: // Brigade General Anuhart.
					if ((var == 2) && (var1 == 1) && (var2 == 1) && (var3 == 1))
					{
						return defaultOnKillEvent(env, 214904, 2, true);
					}
					break;
			}
		}
		return false;
	}
}