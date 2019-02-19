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
 * @author Rinzler (Encom)
 */
public class _50009Tis_The_Season extends QuestHandler
{
	private static final int questId = 50009;
	
	public _50009Tis_The_Season()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(831032).addOnQuestStart(questId);
		qe.registerQuestNpc(831038).addOnQuestStart(questId);
		qe.registerQuestNpc(831032).addOnTalkEvent(questId);
		qe.registerQuestNpc(831038).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if ((targetId == 831032) || (targetId == 831038))
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
					{
						return sendQuestStartDialog(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if ((targetId == 831032) || (targetId == 831038))
			{
				changeQuestStep(env, 0, 0, true);
				return sendQuestDialog(env, 2375);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if ((targetId == 831032) || (targetId == 831038))
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}