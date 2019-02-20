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
package system.handlers.quest.panesterra;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _41062No_Re_Doubts_No_Regrets extends QuestHandler
{
	private static final int questId = 41062;
	
	public _41062No_Re_Doubts_No_Regrets()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnKillInWorld(400060000, questId);
		qe.registerQuestNpc(802533).addOnQuestStart(questId);
		qe.registerQuestNpc(802533).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onKillInWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		if ((env.getVisibleObject() instanceof Player) && (player != null))
		{
			if ((env.getPlayer().getLevel() >= (((Player) env.getVisibleObject()).getLevel() - 5)) && (env.getPlayer().getLevel() <= (((Player) env.getVisibleObject()).getLevel() + 9)))
			{
				return defaultOnKillRankedEvent(env, 0, 1, true);
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getTargetId() == 802533)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
			else if (qs.getStatus() == QuestStatus.REWARD)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}