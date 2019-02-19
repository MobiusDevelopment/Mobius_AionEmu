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
package system.handlers.quest.haramel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _28511Souper_Passion extends QuestHandler
{
	private static final int questId = 28511;
	
	public _28511Souper_Passion()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			799522,
			700954,
			700952,
			798031
		};
		qe.registerQuestNpc(799522).addOnQuestStart(questId);
		qe.registerGetingItem(182212023, questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
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
			if (targetId == 799522)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			switch (targetId)
			{
				case 700954:
				{
					return true;
				}
				case 700952:
				{
					switch (dialog)
					{
						case USE_OBJECT:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							return checkQuestItems(env, 0, 0, false, 1352, 10001);
						}
						case STEP_TO_2:
						{
							giveQuestItem(env, 182212023, 1);
							return closeDialogWindow(env);
						}
						case FINISH_DIALOG:
						{
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798031)
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
	
	@Override
	public boolean onGetItemEvent(QuestEnv env)
	{
		defaultOnGetItemEvent(env, 0, 1, false);
		changeQuestStep(env, 1, 1, true);
		return true;
	}
}