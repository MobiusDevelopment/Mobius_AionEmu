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
package system.handlers.quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _4502Essence_Of_Fate extends QuestHandler
{
	private static final int questId = 4502;
	
	private static final int[] npcs =
	{
		204837,
		730192,
		204182
	};
	private static final int[] mobs =
	{
		214894,
		214895,
		214896,
		214897,
		214904
	};
	
	public _4502Essence_Of_Fate()
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
		qe.registerQuestNpc(204837).addOnQuestStart(questId);
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
				case 730192: // Balaur Operation Orders.
					if (var == 0)
					{
						if (dialog == QuestDialog.USE_OBJECT)
						{
							return sendQuestDialog(env, 1011);
						}
						else
						{
							changeQuestStep(env, 0, 1, false);
							return sendQuestDialog(env, 0);
						}
					}
					break;
				case 204182: // Heimdall.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 2, 2, true, 5, 10001);
						}
						case FINISH_DIALOG:
						{
							return sendQuestSelectionDialog(env);
						}
					}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204837)
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
						return defaultOnKillEvent(env, 214904, 2, false);
					}
					break;
			}
		}
		return false;
	}
}