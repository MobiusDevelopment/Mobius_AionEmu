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
package system.handlers.quest.norsvold;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _25552Pull_The_Lever extends QuestHandler
{
	private static final int questId = 25552;
	
	public _25552Pull_The_Lever()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(806101).addOnQuestStart(questId);
		qe.registerQuestNpc(806101).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25552_A_TO_D_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25552_D_TO_A_220110000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806101)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806101)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25552_A_TO_D_220110000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25552_D_TO_A_220110000"))
			{
				if (var == 1)
				{
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}