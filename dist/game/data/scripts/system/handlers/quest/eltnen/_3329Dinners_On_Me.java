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
package system.handlers.quest.eltnen;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _3329Dinners_On_Me extends QuestHandler
{
	private static final int questId = 3329;
	private static final int[] mob_ids =
	{
		210887,
		210912,
		210914,
		210932
	};
	
	public _3329Dinners_On_Me()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(203909).addOnQuestStart(questId);
		qe.registerQuestNpc(203909).addOnTalkEvent(questId);
		qe.registerQuestNpc(203956).addOnTalkEvent(questId);
		for (int mob_id : mob_ids)
		{
			qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final int[] mobs1 =
		{
			210887,
			210912
		};
		final int[] mobs2 =
		{
			210914,
			210932
		};
		if (defaultOnKillEvent(env, mobs1, 0, 4, 0) || defaultOnKillEvent(env, mobs2, 0, 6, 1))
		{
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		final QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203909)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 203956)
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return sendQuestDialog(env, 2375);
					}
					case SELECT_REWARD:
					{
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203956)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}