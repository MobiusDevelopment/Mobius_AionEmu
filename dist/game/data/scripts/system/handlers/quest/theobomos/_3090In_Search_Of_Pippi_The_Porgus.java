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
package system.handlers.quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _3090In_Search_Of_Pippi_The_Porgus extends QuestHandler
{
	private static final int questId = 3090;
	
	public _3090In_Search_Of_Pippi_The_Porgus()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798182).addOnQuestStart(questId);
		qe.registerQuestNpc(798182).addOnTalkEvent(questId);
		qe.registerQuestNpc(798193).addOnTalkEvent(questId);
		qe.registerQuestNpc(700420).addOnTalkEvent(questId);
		qe.registerQuestNpc(700421).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("LF2A_SENSORYAREA_Q3090_206085_3_210060000"), questId);
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
			if (targetId == 798182)
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
			if (targetId == 798193)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					if (qs.getQuestVarById(0) == 0)
					{
						return sendQuestDialog(env, 1011);
					}
					else if (qs.getQuestVarById(0) == 2)
					{
						return sendQuestDialog(env, 1693);
					}
				}
				else if (dialog == QuestDialog.STEP_TO_1)
				{
					return defaultCloseDialog(env, 0, 1);
				}
				else if (dialog == QuestDialog.STEP_TO_3)
				{
					if (player.getInventory().getKinah() >= 10000)
					{
						giveQuestItem(env, 182208050, 1);
						player.getInventory().decreaseKinah(10000);
						return defaultCloseDialog(env, 2, 3);
					}
					return sendQuestDialog(env, 1779);
				}
				else if (dialog == QuestDialog.SELECT_ACTION_1779)
				{
					return sendQuestDialog(env, 1779);
				}
			}
			if (targetId == 700420)
			{
				final int var = qs.getQuestVarById(0);
				final int var2 = qs.getQuestVarById(2);
				if ((var == 1) && (var2 == 0))
				{
					changeQuestStep(env, 0, 1, false, 2);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(true, 1111007, player.getObjectId(), 0));
					changeStep(qs, env);
					return true;
				}
			}
			if (targetId == 700421)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					if (qs.getQuestVarById(0) == 3)
					{
						return sendQuestDialog(env, 2034);
					}
				}
				else if (dialog == QuestDialog.SET_REWARD)
				{
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().scheduleRespawn();
					npc.getController().onDelete();
					removeQuestItem(env, 182208050, 1);
					giveQuestItem(env, 182208051, 1);
					return defaultCloseDialog(env, 3, 3, true, false);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798182)
			{
				switch (dialog)
				{
					case USE_OBJECT:
					{
						return sendQuestDialog(env, 10002);
					}
					default:
					{
						removeQuestItem(env, 182208051, 1);
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("LF2A_SENSORYAREA_Q3090_206085_3_210060000"))
		{
			final Player player = env.getPlayer();
			if (player == null)
			{
				return false;
			}
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				final int var1 = qs.getQuestVarById(1);
				if ((var == 1) && (var1 == 0))
				{
					changeQuestStep(env, 0, 1, false, 1);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(true, 1111006, player.getObjectId(), 0));
					changeStep(qs, env);
					return true;
				}
			}
		}
		return false;
	}
	
	private void changeStep(QuestState qs, QuestEnv env)
	{
		if ((qs.getQuestVarById(1) == 1) && (qs.getQuestVarById(2) == 1))
		{
			qs.setQuestVarById(1, 0);
			qs.setQuestVarById(2, 0);
			qs.setQuestVar(2);
			updateQuestStatus(env);
		}
	}
}