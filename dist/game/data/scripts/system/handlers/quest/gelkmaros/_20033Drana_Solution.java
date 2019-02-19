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
package system.handlers.quest.gelkmaros;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _20033Drana_Solution extends QuestHandler
{
	private static final int questId = 20033;
	private static final int[] npcs =
	{
		799270,
		700701
	};
	private static final int[] mobs =
	{
		216444,
		216445,
		216446,
		216447
	};
	
	public _20033Drana_Solution()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : mobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215614, questId);
		qe.registerQuestItem(182215594, questId);
		qe.registerOnEnterZoneMissionEnd(questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20032, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 799270)
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
					}
					case STEP_TO_1:
					{
						giveQuestItem(env, 182215614, 1);
						giveQuestItem(env, 182215594, 1);
						return defaultCloseDialog(env, 0, 1);
					}
				}
			}
			else if (targetId == 700701)
			{
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return useQuestObject(env, 2, 3, false, 0, 0, 0, 182215614, 1);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799270)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
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
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 4))
				{
					return defaultOnKillEvent(env, mobs, var1, var1 + 1, 1);
				}
				else if (var1 == 4)
				{
					qs.setQuestVar(2);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final int itemObjId = item.getObjectId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((id != 182215594) || (qs.getStatus() == QuestStatus.COMPLETE))
		{
			return HandlerResult.UNKNOWN;
		}
		if (!player.isInsideZone(ZoneName.get("DF4_ITEMUSEAREA_Q20033")))
		{
			return HandlerResult.UNKNOWN;
		}
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 3000, 0, 0), true);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), itemObjId, id, 0, 1, 0), true);
			playQuestMovie(env, 553);
			removeQuestItem(env, 182215594, 1);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
		}, 3000);
		return HandlerResult.SUCCESS;
	}
}