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
package system.handlers.quest.pandaemonium;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _2938Secret_Library_Access extends QuestHandler
{
	private static final int questId = 2938;
	private static final int[] npc_ids =
	{
		204267,
		203557
	};
	
	public _2938Secret_Library_Access()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(204267).addOnQuestStart(questId);
		for (final int npc_id : npc_ids)
		{
			qe.registerQuestNpc(npc_id).addOnTalkEvent(questId);
		}
	}
	
	private boolean MissionsAltgardFinished(Player player)
	{
		final QuestState qs = player.getQuestStateList().getQuestState(2022);
		return ((qs == null) || ((qs.getStatus() != QuestStatus.COMPLETE) && (qs.getStatus() != QuestStatus.NONE))) ? false : true;
	}
	
	private boolean MissionsAethertechFinished(Player player)
	{
		final QuestState qs = player.getQuestStateList().getQuestState(24016);
		return ((qs == null) || ((qs.getStatus() != QuestStatus.COMPLETE) && (qs.getStatus() != QuestStatus.NONE))) ? false : true;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 204267)
		{
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
			else if (((qs.getStatus() == QuestStatus.REWARD) && (qs.getQuestVarById(0) == 0)) || (qs.getStatus() == QuestStatus.COMPLETE))
			{
				if ((env.getDialog() == QuestDialog.USE_OBJECT) && (qs.getStatus() == QuestStatus.REWARD))
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialogId() == 23)
				{
					removeQuestItem(env, 182207026, 1);
					qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
					updateQuestStatus(env);
					return sendQuestEndDialog(env);
				}
				else if (env.getDialogId() == 1009)
				{
					return sendQuestEndDialog(env);
				}
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						TeleportService2.teleportTo(player, 120010000, 1403.7645f, 1063.2236f, 206.02371f, (byte) 89, TeleportAnimation.BEAM_ANIMATION);
					}
				}, 1000);
			}
		}
		else if (targetId == 203557)
		{
			if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					if (MissionsAltgardFinished(player) || MissionsAethertechFinished(player))
					{
						return sendQuestDialog(env, 1011);
					}
					else
					{
						return sendQuestDialog(env, 1097);
					}
				}
				else if (env.getDialogId() == 10255)
				{
					if (giveQuestItem(env, 182207026, 1))
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 0));
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
}