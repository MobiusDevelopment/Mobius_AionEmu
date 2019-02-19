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

import java.util.Collections;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class _18409Tiamat_Power_Unleashed extends QuestHandler
{
	private static final int questId = 18409;
	
	public _18409Tiamat_Power_Unleashed()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799553).addOnQuestStart(questId);
		qe.registerQuestNpc(799553).addOnTalkEvent(questId);
		qe.registerQuestNpc(799552).addOnTalkEvent(questId);
		qe.registerQuestNpc(730014).addOnTalkEvent(questId);
		qe.registerQuestNpc(215795).addOnKillEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 799553)
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
		else if (targetId == 799552)
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
			else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
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
		else if (targetId == 205232)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 1))
			{
				if (env.getDialogId() == 31)
				{
					return sendQuestDialog(env, 1352);
				}
				else if (env.getDialogId() == 10001)
				{
					if (!ItemService.addQuestItems(player, Collections.singletonList(new QuestItems(182215007, 1))))
					{
						return true;
					}
					player.getInventory().decreaseByItemId(182215006, 1);
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
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		final int targetId = env.getTargetId();
		switch (targetId)
		{
			case 215795:
			{
				if (qs.getQuestVarById(0) == 2)
				{
					ItemService.addQuestItems(player, Collections.singletonList(new QuestItems(182215008, 1)));
					player.getInventory().decreaseByItemId(182215007, 1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
				}
				break;
			}
		}
		return false;
	}
}