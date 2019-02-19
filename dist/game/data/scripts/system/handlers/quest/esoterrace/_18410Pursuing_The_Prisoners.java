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
package system.handlers.quest.esoterrace;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _18410Pursuing_The_Prisoners extends QuestHandler
{
	private static final int questId = 18410;
	
	public _18410Pursuing_The_Prisoners()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799584).addOnQuestStart(questId);
		qe.registerQuestNpc(799585).addOnQuestStart(questId);
		qe.registerQuestNpc(799584).addOnTalkEvent(questId);
		qe.registerQuestNpc(799585).addOnTalkEvent(questId);
		qe.registerQuestNpc(799563).addOnTalkEvent(questId);
		qe.registerQuestNpc(799553).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("DRANA_PRODUCTION_LAB_300250000"), questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		if (player == null)
		{
			return false;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (zoneName != ZoneName.get("DRANA_PRODUCTION_LAB_300250000"))
		{
			return false;
		}
		if ((qs == null) || (qs.getQuestVars().getQuestVars() != 1))
		{
			return false;
		}
		if (qs.getStatus() != QuestStatus.START)
		{
			return false;
		}
		qs.setStatus(QuestStatus.REWARD);
		updateQuestStatus(env);
		return true;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((targetId == 799584) || (targetId == 799585))
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialogId() == 31)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (targetId == 799563)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
			{
				if (env.getDialogId() == 31)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (env.getDialogId() == 10000)
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
		else if (targetId == 799553)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
			{
				if (env.getDialogId() == -1)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == 1009)
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
}