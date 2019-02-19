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
package system.handlers.quest.udas_temple;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Cheatkiller
 */
public class _30111CenterOfTheWeb extends QuestHandler
{
	
	private static final int questId = 30111;
	
	public _30111CenterOfTheWeb()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799335).addOnQuestStart(questId);
		qe.registerQuestNpc(799335).addOnTalkEvent(questId);
		qe.registerQuestNpc(206105).addOnAtDistanceEvent(questId);
		qe.registerQuestNpc(215792).addOnKillEvent(questId);
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
			if (targetId == 799335)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 799335)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (dialog == QuestDialog.SELECT_REWARD)
				{
					return defaultCloseDialog(env, 2, 2, true, true);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799335)
			{
				if (dialog == QuestDialog.USE_OBJECT)
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
		return defaultOnKillEvent(env, 215792, 1, 2);
	}
	
	@Override
	public boolean onAtDistanceEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{
				changeQuestStep(env, 0, 1, false);
				return true;
			}
		}
		return false;
	}
}
