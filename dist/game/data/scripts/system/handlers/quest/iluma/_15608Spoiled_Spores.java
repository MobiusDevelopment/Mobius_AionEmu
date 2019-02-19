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
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _15608Spoiled_Spores extends QuestHandler
{
	public static final int questId = 15608;
	private static final int[] LF6I6NamedNoName68Ah =
	{
		241175
	}; // 텐타클루드.
	
	public _15608Spoiled_Spores()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 15606);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806165).addOnQuestStart(questId); // Canella.
		qe.registerQuestNpc(806165).addOnTalkEvent(questId); // Canella.
		qe.registerQuestNpc(703139).addOnTalkEvent(questId); // 검은 버섯.
		for (int boss : LF6I6NamedNoName68Ah)
		{
			qe.registerQuestNpc(boss).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15608_A_DYNAMIC_ENV_210100000"), questId);
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15608_B_DYNAMIC_ENV_210100000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806165) // Canella.
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
			if (targetId == 806165) // Canella.
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
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							changeQuestStep(env, 1, 2, false);
							return sendQuestDialog(env, 10000);
						}
						return sendQuestDialog(env, 10001);
					}
				}
			}
			if (targetId == 703139) // 검은 버섯.
			{
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 1)
						{
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806165) // Canella.
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					removeQuestItem(env, 182215995, 1);
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
		final int targetId = env.getTargetId();
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 3)
			{
				switch (targetId)
				{
					case 241175: // 텐타클루드.
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
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
			if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15608_A_DYNAMIC_ENV_210100000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15608_B_DYNAMIC_ENV_210100000"))
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
}