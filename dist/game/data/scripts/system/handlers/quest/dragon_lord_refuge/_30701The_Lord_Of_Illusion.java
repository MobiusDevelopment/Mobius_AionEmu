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
package system.handlers.quest.dragon_lord_refuge;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _30701The_Lord_Of_Illusion extends QuestHandler
{
	private static final int questId = 30701;
	private static final int npcs[] =
	{
		804868,
		800430,
		800350
	};
	
	public _30701The_Lord_Of_Illusion()
	{
		super(questId);
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		return defaultOnKillEvent(env, 219362, 0, 1);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804868).addOnQuestStart(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		qe.registerQuestNpc(219362).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 804868)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 800430)
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
			}
			else if (targetId == 800350)
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
					case SET_REWARD:
					{
						return defaultCloseDialog(env, 2, 3, true, false);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804868)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}