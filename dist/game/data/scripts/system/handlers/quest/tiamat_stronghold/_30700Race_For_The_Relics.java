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
package system.handlers.quest.tiamat_stronghold;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.SystemMessageId;
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
public class _30700Race_For_The_Relics extends QuestHandler
{
	private static final int questId = 30700;
	private static final int npcs[] =
	{
		804868,
		800368,
		800338
	};
	private static final int mobs[] =
	{
		219352,
		219358
	};
	
	public _30700Race_For_The_Relics()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804868).addOnQuestStart(questId);
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("DREDGION_CONTROL_CENTER_2_300510000"), questId);
		qe.registerOnEnterWorld(questId);
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
			if (targetId == 804868)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 800368)
			{
				switch (dialog)
				{
					case START_DIALOG:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2034);
						}
					}
					case STEP_TO_4:
					{
						return defaultCloseDialog(env, 3, 4);
					}
				}
			}
			else if (targetId == 800338)
			{
				switch (dialog)
				{
					case USE_OBJECT:
					{
						if (var == 5)
						{
							return sendQuestDialog(env, 2716);
						}
					}
					case SET_REWARD:
					{
						return defaultCloseDialog(env, 5, 6, true, false);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804868)
			{
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 1)
			{
				return defaultOnKillEvent(env, 219352, 1, 2);
			}
			else if (var == 4)
			{
				return defaultOnKillEvent(env, 219358, 4, 5);
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
			if (zoneName.equals(ZoneName.get("DREDGION_CONTROL_CENTER_2_300510000")))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onEnterWorldEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (player.getWorldId() != 300510000)
			{
				if (var >= 1)
				{
					qs.setQuestVar(1);
					updateQuestStatus(env);
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(SystemMessageId.QUEST_FAILED_$1, DataManager.QUEST_DATA.getQuestById(questId).getName()));
					return true;
				}
			}
			else
			{
				if (var == 0)
				{
					qs.setQuestVar(1);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}