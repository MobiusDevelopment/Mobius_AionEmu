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
package system.handlers.quest.panesterra;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24220Welcome_To_Panesterra extends QuestHandler
{
	private static final int questId = 24220;
	private static final Set<Integer> GAB1PangaeaScout;
	
	public _24220Welcome_To_Panesterra()
	{
		super(questId);
	}
	
	static
	{
		GAB1PangaeaScout = new HashSet<>();
		GAB1PangaeaScout.add(802542);
		GAB1PangaeaScout.add(802543);
		GAB1PangaeaScout.add(802544);
		GAB1PangaeaScout.add(802545);
		GAB1PangaeaScout.add(802546);
		GAB1PangaeaScout.add(802547);
		GAB1PangaeaScout.add(804080);
		GAB1PangaeaScout.add(804081);
		GAB1PangaeaScout.add(804082);
		GAB1PangaeaScout.add(804083);
		GAB1PangaeaScout.add(804084);
		GAB1PangaeaScout.add(804085);
		GAB1PangaeaScout.add(804086);
		GAB1PangaeaScout.add(804087);
		GAB1PangaeaScout.add(804088);
		GAB1PangaeaScout.add(804089);
		GAB1PangaeaScout.add(804090);
		GAB1PangaeaScout.add(804091);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public void register()
	{
		final Iterator<Integer> iter = GAB1PangaeaScout.iterator();
		while (iter.hasNext())
		{
			final int GAB1Id = iter.next();
			qe.registerOnLevelUp(questId);
			qe.registerQuestNpc(GAB1Id).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (!GAB1PangaeaScout.contains(targetId))
		{
			return false;
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 802543)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case SELECT_ACTION_1012:
					{
						return sendQuestDialog(env, 1012);
					}
					case STEP_TO_1:
					{
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
				}
			}
			if ((targetId == 802544) || (targetId == 804080) || (targetId == 804081) || (targetId == 804082))
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
						return sendQuestDialog(env, 1353);
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 904);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
			if ((targetId == 802545) || (targetId == 804083) || (targetId == 804084) || (targetId == 804085))
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1693);
						}
					}
					case SELECT_ACTION_1694:
					{
						return sendQuestDialog(env, 1694);
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 905);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
			if ((targetId == 802546) || (targetId == 804086) || (targetId == 804087) || (targetId == 804088))
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case SELECT_ACTION_2035:
					{
						return sendQuestDialog(env, 2035);
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 906);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
			if ((targetId == 802547) || (targetId == 804089) || (targetId == 804090) || (targetId == 804091))
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case SELECT_ACTION_2376:
					{
						return sendQuestDialog(env, 2376);
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 907);
						changeQuestStep(env, 1, 1, true);
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 802542)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}