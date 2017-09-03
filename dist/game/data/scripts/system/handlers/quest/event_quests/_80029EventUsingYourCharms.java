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
package system.handlers.quest.event_quests;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rolandas
 */

public class _80029EventUsingYourCharms extends QuestHandler
{
	
	private static final int questId = 80029;
	
	public _80029EventUsingYourCharms()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799766).addOnQuestStart(questId);
		qe.registerQuestNpc(799766).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			return false;
		}
		
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 799766)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialog() == QuestDialog.ACCEPT_QUEST)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					defaultCloseDialog(env, 0, 0, true, true);
					return sendQuestDialog(env, 5);
				}
				else if (env.getDialog() == QuestDialog.SELECT_NO_REWARD)
				{
					return sendQuestRewardDialog(env, 799766, 5);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		return sendQuestRewardDialog(env, 799766, 0);
	}
	
}
