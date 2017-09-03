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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _24024A_Nepra_Protector extends QuestHandler
{
	private static final int questId = 24024;
	
	public _24024A_Nepra_Protector()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(212861).addOnKillEvent(questId);
		qe.registerQuestNpc(204369).addOnTalkEvent(questId);
		qe.registerQuestNpc(204361).addOnTalkEvent(questId);
		qe.registerOnEnterZone(ZoneName.get("ALTAR_OF_THE_BLACK_DRAGON_220020000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24023, true);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("ALTAR_OF_THE_BLACK_DRAGON_220020000"))
		{
			final Player player = env.getPlayer();
			if (player == null)
			{
				return false;
			}
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if (var == 2)
				{
					playQuestMovie(env, 81);
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		return defaultOnKillEvent(env, 212861, 3, true);
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
		final int targetId = env.getTargetId();
		final QuestDialog dialog = env.getDialog();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204369:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
						case STEP_TO_1:
							if (var == 0)
							{
								playQuestMovie(env, 80);
								return defaultCloseDialog(env, 0, 1);
							}
					}
				}
					break;
				case 204361:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						case STEP_TO_2:
							if (var == 1)
							{
								return defaultCloseDialog(env, 1, 2);
							}
							
					}
				}
					break;
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204369)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}