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
package system.handlers.quest.arena;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _18213The_Broken_Insignia extends QuestHandler
{
	private static final int questId = 18213;
	
	public _18213The_Broken_Insignia()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(205985).addOnQuestStart(questId);
		qe.registerQuestNpc(205316).addOnTalkEvent(questId);
		qe.registerQuestNpc(798604).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			if (targetId == 205985)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 205316:
				{
					if (var == 0)
					{
						switch (env.getDialog())
						{
							case START_DIALOG:
							{
								return sendQuestDialog(env, 1352);
							}
							case STEP_TO_1:
							{
								return defaultCloseDialog(env, 0, 1);
							}
						}
					}
					break;
				}
				case 798604:
				{
					if (var == 1)
					{
						switch (env.getDialog())
						{
							case START_DIALOG:
							{
								return sendQuestDialog(env, 1693);
							}
							case STEP_TO_2:
							{
								return defaultCloseDialog(env, 1, 3);
							}
						}
					}
					else if (var == 3)
					{
						switch (env.getDialog())
						{
							case START_DIALOG:
							{
								return sendQuestDialog(env, 2375);
							}
							case SELECT_REWARD:
							{
								return defaultCloseDialog(env, 3, 3, true, false);
							}
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798604)
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}