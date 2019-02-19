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
package system.handlers.quest.inggison_armor;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

public class _11052Gloves_And_Glossy_Beads extends QuestHandler
{
	private static final int questId = 11052;
	
	public _11052Gloves_And_Glossy_Beads()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798984).addOnQuestStart(questId); // Meininerk
		qe.registerQuestNpc(798984).addOnTalkEvent(questId); // Meininerk
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || qs.canRepeat())
		{
			if (targetId == 798984)
			{ // Meininerk
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 798984)
			{ // Meininerk
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS)
				{
					// Collect Glittering Bead (30)
					// Collect Kinah (50000)
					final long itemCount = player.getInventory().getItemCountByItemId(182206837);
					if (player.getInventory().tryDecreaseKinah(50000) && (itemCount > 29))
					{
						player.getInventory().decreaseByItemId(182206837, 30);
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
					return sendQuestDialog(env, 2716);
				}
				else if (dialog == QuestDialog.FINISH_DIALOG)
				{
					return defaultCloseDialog(env, 0, 0);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798984)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}