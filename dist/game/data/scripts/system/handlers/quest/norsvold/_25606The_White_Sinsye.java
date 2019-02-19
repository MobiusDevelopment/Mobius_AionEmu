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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _25606The_White_Sinsye extends QuestHandler
{
	public static final int questId = 25606;
	private static final int[] DF6G6NamedSinsi68Ah =
	{
		241220
	}; // 백신시.
	
	public _25606The_White_Sinsye()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 25605);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806175).addOnQuestStart(questId); // Chaelsean.
		qe.registerQuestNpc(806175).addOnTalkEvent(questId); // Chaelsean.
		qe.registerQuestNpc(806178).addOnTalkEvent(questId); // 배회하는 영혼.
		qe.registerQuestNpc(806156).addOnTalkEvent(questId); // 기억을 잃은 여인.
		qe.registerQuestNpc(806157).addOnTalkEvent(questId); // 리니.
		qe.registerQuestNpc(703140).addOnTalkEvent(questId); // 리니의 소품 상자.
		for (int mobs : DF6G6NamedSinsi68Ah)
		{
			qe.registerQuestNpc(mobs).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_A_DYNAMIC_ENV_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_B_DYNAMIC_ENV_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DF6_SENSORY_AREA_Q25606_C_DYNAMIC_ENV_220110000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final Npc npc = (Npc) env.getVisibleObject();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806175) // Chaelsean.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case SELECT_ACTION_4763:
					{
						return sendQuestDialog(env, 4848);
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
			if (targetId == 806178) // 배회하는 영혼.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
					}
					case SELECT_ACTION_1353:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1353);
						}
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 873);
						changeQuestStep(env, 1, 2, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 806156) // 기억을 잃은 여인.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 3)
						{
							return sendQuestDialog(env, 2035);
						}
					}
					case STEP_TO_4:
					{
						changeQuestStep(env, 3, 4, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 806157) // 리니.
			{
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 6)
						{
							return sendQuestDialog(env, 3057);
						}
					}
					case STEP_TO_7:
					{
						changeQuestStep(env, 6, 7, false);
						npc.getController().onDelete();
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 703140) // 리니의 소품 상자.
			{
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 7)
						{
							giveQuestItem(env, 182216006, 1); // 리니의 일기장.
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806175) // Chaelsean.
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					removeQuestItem(env, 182216006, 1); // 리니의 일기장.
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
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = env.getTargetId();
		final Npc npc = (Npc) env.getVisibleObject();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 5)
			{
				switch (targetId)
				{
					case 241220: // 백신시.
					{
						qs.setQuestVar(6);
						updateQuestStatus(env);
						QuestService.addNewSpawn(220110000, 1, 806157, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // 리니.
						return true;
					}
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
			if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_A_DYNAMIC_ENV_220110000"))
			{
				if (var == 0)
				{
					QuestService.addNewSpawn(220110000, 1, 806178, player.getX(), player.getY(), player.getZ(), (byte) 0); // 배회하는 영혼.
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_B_DYNAMIC_ENV_220110000"))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("DF6_SENSORY_AREA_Q25606_C_DYNAMIC_ENV_220110000"))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
		}
		return false;
	}
}