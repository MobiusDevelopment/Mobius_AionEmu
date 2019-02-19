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
package system.handlers.quest.reshanta;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _1851Dispatched_Shugo_Craftsmen extends QuestHandler
{
	private static final int questId = 1851;
	
	public _1851Dispatched_Shugo_Craftsmen()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(278533).addOnQuestStart(questId); // Rentia.
		qe.registerQuestNpc(278533).addOnTalkEvent(questId); // Rentia.
		qe.registerQuestNpc(279025).addOnTalkEvent(questId); // Guuminerk.
		qe.registerQuestNpc(279036).addOnTalkEvent(questId); // Muirunerk.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 278533)
			{ // Rentia
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					case ASK_ACCEPTION:
					{
						return sendQuestDialog(env, 4);
					}
					case ACCEPT_QUEST:
					{
						return sendQuestStartDialog(env);
					}
					case REFUSE_QUEST:
					{
						return sendQuestDialog(env, 1004);
					}
				}
			}
		}
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (targetId)
			{
				case 279025:
				{ // Guuminerk
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1352);
						}
						case STEP_TO_1:
						{
							qs.setQuestVar(1);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
					}
				}
				case 279036:
				{ // Muirunerk
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_2:
						{
							qs.setQuestVar(2);
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
		{
			if (targetId == 278533)
			{ // Rentia
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 2375);
					}
					case SELECT_REWARD:
					{
						return sendQuestDialog(env, 5);
					}
					default:
					{
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
}