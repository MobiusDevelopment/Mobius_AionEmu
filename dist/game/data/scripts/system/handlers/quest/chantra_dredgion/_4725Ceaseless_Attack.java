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
package system.handlers.quest.chantra_dredgion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _4725Ceaseless_Attack extends QuestHandler
{
	private static final int questId = 4725;
	
	public _4725Ceaseless_Attack()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnDredgionReward(questId);
		qe.registerQuestNpc(799403).addOnQuestStart(questId); // Yorgen.
		qe.registerQuestNpc(799403).addOnTalkEvent(questId); // Yorgen.
		qe.registerQuestNpc(799226).addOnTalkEvent(questId); // Valetta.
		qe.registerQuestNpc(216866).addOnKillEvent(questId); // Chantra Legatus.
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
			if (targetId == 799403) // Yorgen.
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
			if (targetId == 799226) // Valetta.
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					final int var1 = qs.getQuestVarById(1);
					final int var2 = qs.getQuestVarById(2);
					if ((var1 == 6) && (var2 == 15))
					{
						return sendQuestDialog(env, 10002);
					}
				}
				else if (dialog == QuestDialog.SELECT_REWARD)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestDialog(env, 5);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799226) // Valetta.
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final int[] mobs =
		{
			216866 // Chantra Legatus.
		};
		return defaultOnKillEvent(env, mobs, 0, 15, 2);
	}
	
	@Override
	public boolean onDredgionRewardEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var1 = qs.getQuestVarById(1);
			if (var1 < 6)
			{
				changeQuestStep(env, var1, var1 + 1, false, 1);
				return true;
			}
		}
		return false;
	}
}