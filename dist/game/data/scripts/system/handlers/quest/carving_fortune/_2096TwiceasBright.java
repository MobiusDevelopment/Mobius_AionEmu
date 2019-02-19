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
package system.handlers.quest.carving_fortune;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * Go to Pandaemonium and talk with Cavalorn (204206).<br>
 * Meet with Kasir (204207) in the Hidden Library.<br>
 * Go to Ishalgen and talk with Munin (203550).
 * @author Manu72
 * @reworked vlog
 */
public class _2096TwiceasBright extends QuestHandler
{
	
	private static final int questId = 2096;
	
	public _2096TwiceasBright()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204206).addOnTalkEvent(questId);
		qe.registerQuestNpc(204207).addOnTalkEvent(questId);
		qe.registerQuestNpc(203550).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204206:
				{ // Cavalorn
					if ((dialog == QuestDialog.START_DIALOG) && (var == 0))
					{
						return sendQuestDialog(env, 1011);
					}
					else if (dialog == QuestDialog.STEP_TO_1)
					{
						return defaultCloseDialog(env, 0, 1); // 1
					}
					break;
				}
				case 204207:
				{ // Kasir
					if ((dialog == QuestDialog.START_DIALOG) && (var == 1))
					{
						return sendQuestDialog(env, 1352);
					}
					else if (dialog == QuestDialog.STEP_TO_2)
					{
						return defaultCloseDialog(env, 1, 2); // 2
					}
					break;
				}
				case 203550:
				{ // Munin
					if ((dialog == QuestDialog.START_DIALOG) && (var == 2))
					{
						changeQuestStep(env, 2, 2, true); // reward
						return sendQuestDialog(env, 1693);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203550)
			{ // Munin
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
}
