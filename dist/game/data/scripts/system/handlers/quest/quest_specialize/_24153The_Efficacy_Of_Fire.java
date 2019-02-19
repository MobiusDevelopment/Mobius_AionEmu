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
package system.handlers.quest.quest_specialize;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rinzler (Encom)
 */
public class _24153The_Efficacy_Of_Fire extends QuestHandler
{
	private static final int questId = 24153;
	
	private static final int[] mob_ids =
	{
		213730,
		213788,
		213789,
		213790,
		213791
	};
	
	public _24153The_Efficacy_Of_Fire()
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
		qe.registerQuestNpc(204787).addOnQuestStart(questId); // Delris
		qe.registerQuestNpc(204787).addOnTalkEvent(questId); // Chieftain Akagitan
		qe.registerQuestNpc(204784).addOnTalkEvent(questId); // Delris
		qe.registerQuestItem(182215462, questId);
		for (int mob_id : mob_ids)
		{
			qe.registerQuestNpc(mob_id).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204787) // Chieftain Akagitan
			{
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
						QuestService.startQuest(env);
						if (qs != null)
						{
							qs.setQuestVarById(5, 1);
						}
						updateQuestStatus(env);
						return closeDialogWindow(env);
					}
					case REFUSE_QUEST:
					{
						return sendQuestDialog(env, 1004);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204784: // Delris
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1352);
						}
						case STEP_TO_2:
						{
							giveQuestItem(env, 182215462, 1);
							qs.setQuestVar(0);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
					}
				}
				case 204787: // Chieftain Akagitan
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 2375);
						}
						case SELECT_REWARD:
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204787) // Chieftain Akagitan
			{
				if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final int targetId = env.getTargetId();
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int var1 = qs.getQuestVarById(1);
		final int var2 = qs.getQuestVarById(2);
		final int var3 = qs.getQuestVarById(3);
		final int var4 = qs.getQuestVarById(4);
		if ((targetId == 213730) && (var == 0) && (var < 1)) // Glaciont The Hardy
		{
			qs.setQuestVarById(0, 1);
			updateQuestStatus(env);
		}
		else if ((targetId == 213788) && (var1 == 0) && (var1 < 1)) // Frostfist
		{
			qs.setQuestVarById(1, 1);
			updateQuestStatus(env);
		}
		else if ((targetId == 213789) && (var2 == 0) && (var2 < 1)) // Iceback
		{
			qs.setQuestVarById(2, 1);
			updateQuestStatus(env);
		}
		else if ((targetId == 213790) && (var3 == 0) && (var3 < 1)) // Chillblow
		{
			qs.setQuestVarById(3, 1);
			updateQuestStatus(env);
		}
		else if ((targetId == 213791) && (var4 == 0) && (var4 < 1)) // Snowfury
		{
			qs.setQuestVarById(4, 1);
			updateQuestStatus(env);
		}
		return false;
	}
}