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
package system.handlers.quest.heiron;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class _1527Rotten_Rotrons extends QuestHandler
{
	private static final int questId = 1527;
	
	public _1527Rotten_Rotrons()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204555).addOnQuestStart(questId); // Mirborante.
		qe.registerQuestNpc(204555).addOnTalkEvent(questId); // Mirborante.
		qe.registerQuestNpc(204562).addOnTalkEvent(questId); // Sirilis.
		qe.registerQuestNpc(205229).addOnTalkEvent(questId); // Raninia.
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
			if (targetId == 204555)
			{ // Mirborante.
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 204562)
		{ // Sirilis.
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialog() == QuestDialog.STEP_TO_1)
				{
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
					return true;
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		else if (targetId == 205229)
		{ // Trajanus.
			if (qs != null)
			{
				if ((env.getDialog() == QuestDialog.START_DIALOG) && (qs.getStatus() == QuestStatus.START))
				{
					return sendQuestDialog(env, 2375);
				}
				else if ((env.getDialogId() == 1009) && (qs.getStatus() != QuestStatus.COMPLETE) && (qs.getStatus() != QuestStatus.NONE))
				{
					qs.setQuestVar(2);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}