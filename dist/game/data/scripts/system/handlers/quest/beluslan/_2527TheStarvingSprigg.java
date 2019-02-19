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
package system.handlers.quest.beluslan;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Collect Food Pouches and take them to Gark (204811).
 * @author vlog
 */
public class _2527TheStarvingSprigg extends QuestHandler
{
	
	private static final int questId = 2527;
	
	public _2527TheStarvingSprigg()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204811).addOnQuestStart(questId);
		qe.registerQuestNpc(204811).addOnTalkEvent(questId);
		qe.registerQuestNpc(700328).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if (qs == null)
		{
			if (targetId == 204811)
			{ // Gark
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 204811)
			{ // Gark
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 2375);
				}
				else if (dialog == QuestDialog.CHECK_COLLECTED_ITEMS)
				{
					return checkQuestItems(env, 0, 0, true, 5, 2716); // reward
				}
			}
			else if (targetId == 700328)
			{ // Food Pouche
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return true; // loot
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204811)
			{ // Gark
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}
