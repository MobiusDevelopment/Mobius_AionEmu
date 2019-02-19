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
package system.handlers.quest.iluma;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _15606The_Hall_Of_Zephyr extends QuestHandler
{
	public static final int questId = 15606;
	
	public _15606The_Hall_Of_Zephyr()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 15605);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806163).addOnQuestStart(questId); // Thaleia.
		qe.registerQuestNpc(806163).addOnTalkEvent(questId); // Thaleia.
		qe.registerQuestNpc(703147).addOnTalkEvent(questId); // 고대 서풍의 데바 보관함.
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15606_A_DYNAMIC_ENV_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15606_B_DYNAMIC_ENV_210100000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806163) // Thaleia.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case ACCEPT_QUEST:
					case ACCEPT_QUEST_SIMPLE:
					{
						return sendQuestStartDialog(env);
					}
					case REFUSE_QUEST_SIMPLE:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			final int var = qs.getQuestVarById(0);
			if (targetId == 806163) // Thaleia.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 2)
						{
							return sendQuestDialog(env, 1694);
						}
						else if (var == 4)
						{
							return sendQuestDialog(env, 2375);
						}
					}
					case STEP_TO_3:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							removeQuestItem(env, 182215996, 1); // 서풍의 데바 훈련 기록.
							changeQuestStep(env, 4, 5, true);
							return sendQuestDialog(env, 10000);
						}
						return sendQuestDialog(env, 10001);
					}
				}
			}
			if (targetId == 703147) // 고대 서풍의 데바 보관함.
			{
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 1)
						{
							giveQuestItem(env, 182215996, 1); // 서풍의 데바 훈련 기록.
							changeQuestStep(env, 1, 2, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806163) // Thaleia.
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
			if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15606_A_DYNAMIC_ENV_210100000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					TeleportService2.teleportTo(player, 210100000, 671.3528f, 1609.7289f, 333.86536f, (byte) 72);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15606_B_DYNAMIC_ENV_210100000"))
			{
				if (var == 3)
				{
					changeQuestStep(env, 3, 4, false);
					TeleportService2.teleportTo(player, 210100000, 358.1248f, 1679.0687f, 355.41916f, (byte) 60);
					return true;
				}
			}
		}
		return false;
	}
}