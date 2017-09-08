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
public class _15605Mystery_Of_The_Aetherion extends QuestHandler
{
	public static final int questId = 15605;
	private static final int[] LF6F224NamedRuinsGolem70Al =
	{
		241162
	}; // 라이노드.
	
	public _15605Mystery_Of_The_Aetherion()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 15604);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(806162).addOnQuestStart(questId); // Polonius.
		qe.registerQuestNpc(806162).addOnTalkEvent(questId); // Polonius.
		qe.registerQuestNpc(703137).addOnTalkEvent(questId); // 희미한 고대의 유적.
		qe.registerQuestNpc(703138).addOnTalkEvent(questId); // 빛나는 고대의 유적.
		for (int boss : LF6F224NamedRuinsGolem70Al)
		{
			qe.registerQuestNpc(boss).addOnKillEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("LF6_SENSORY_AREA_Q15605_A_NAMED_210100000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 806162)
			{ // Polonius.
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
			if (targetId == 806162)
			{ // Polonius.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1)
						{
							return sendQuestDialog(env, 1353);
						}
					}
					case STEP_TO_2:
					{
						playQuestMovie(env, 1002);
						giveQuestItem(env, 182215995, 1);
						changeQuestStep(env, 1, 2, false);
						removeQuestItem(env, 182216010, 1); // 희미한 고대의 유적 조각.
						removeQuestItem(env, 182216011, 1); // 빛나는 고대의 유적 조각.
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						if (QuestService.collectItemCheck(env, true))
						{
							changeQuestStep(env, 0, 1, false);
							return sendQuestDialog(env, 10000);
						}
						else
						{
							return sendQuestDialog(env, 10001);
						}
					}
				}
			}
			if (targetId == 703137)
			{ // 희미한 고대의 유적.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 0)
						{
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 703138)
			{ // 빛나는 고대의 유적.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 0)
						{
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806162)
			{ // Polonius.
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
					case 241162:
					{ // 라이노드.
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
			if (zoneName == ZoneName.get("LF6_SENSORY_AREA_Q15605_A_NAMED_210100000"))
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