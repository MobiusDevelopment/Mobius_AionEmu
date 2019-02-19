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
package system.handlers.quest.mission;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _24015Totem_Plowed extends QuestHandler
{
	private static final int questId = 24015;
	
	public _24015Totem_Plowed()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(203669).addOnTalkEvent(questId);
		qe.registerQuestNpc(203557).addOnTalkEvent(questId);
		qe.registerQuestNpc(700099).addOnKillEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("BLACK_CLAW_OUTPOST_220030000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24014, true);
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
			switch (targetId)
			{
				case 203669:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 5)
							{
								return sendQuestDialog(env, 1352);
							}
							break;
						}
						case STEP_TO_1:
						{
							if (var == 0)
							{
								player.getTransformModel().setModelId(202501);
								PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
								ThreadPoolManager.getInstance().schedule(() ->
								{
									if (!player.getTransformModel().isActive())
									{
										return;
									}
									player.getTransformModel().setModelId(0);
									PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, false));
								}, 300000);
								return defaultCloseDialog(env, 0, 1);
							}
							break;
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 5, 5, true, false);
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203557)
			{
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
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if ((var >= 2) && (var < 4))
		{
			qs.setQuestVarById(0, var + 1);
			updateQuestStatus(env);
			return true;
		}
		else if (var == 4)
		{
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName != ZoneName.get("BLACK_CLAW_OUTPOST_220030000"))
		{
			return false;
		}
		final Player player = env.getPlayer();
		if (player == null)
		{
			return false;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		if (qs.getQuestVarById(0) == 1)
		{
			qs.setQuestVarById(0, 2);
			updateQuestStatus(env);
			return true;
		}
		return false;
	}
}