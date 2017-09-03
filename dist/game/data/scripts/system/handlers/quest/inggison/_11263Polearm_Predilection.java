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
package system.handlers.quest.inggison;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _11263Polearm_Predilection extends QuestHandler
{
	private static final int questId = 11263;
	
	public _11263Polearm_Predilection()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799038).addOnQuestStart(questId); // Laestrygos.
		qe.registerQuestNpc(799038).addOnTalkEvent(questId); // Laestrygos.
		qe.registerQuestNpc(798316).addOnTalkEvent(questId); // Anusis.
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
			if (targetId == 799038)
			{ // Laestrygos.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 1011);
					}
					default:
						return sendQuestStartDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 798316)
			{ // Anusis.
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS)
				{
					if (player.getInventory().tryDecreaseKinah(8000000))
					{
						player.getInventory().decreaseByItemId(101300687, 1);
						player.getInventory().decreaseByItemId(182206884, 1);
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
					else
					{
						return sendQuestDialog(env, 2716);
					}
				}
				else if (dialog == QuestDialog.FINISH_DIALOG)
				{
					return defaultCloseDialog(env, 0, 0);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798316)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}