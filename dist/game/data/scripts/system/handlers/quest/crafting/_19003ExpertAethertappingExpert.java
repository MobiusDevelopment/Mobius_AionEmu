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
package system.handlers.quest.crafting;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Gigi
 */
public class _19003ExpertAethertappingExpert extends QuestHandler
{
	private static final int questId = 19003;
	
	public _19003ExpertAethertappingExpert()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203782).addOnQuestStart(questId);
		qe.registerQuestNpc(203782).addOnTalkEvent(questId);
		qe.registerQuestNpc(203700).addOnTalkEvent(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 19002);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 203782)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					if (giveQuestItem(env, 182206128, 1))
					{
						return sendQuestDialog(env, 1011);
					}
					else
					{
						return true;
					}
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (targetId)
			{
				case 203700:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							qs.setStatus(QuestStatus.REWARD);
							updateQuestStatus(env);
							return sendQuestDialog(env, 2375);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 203700)
			{
				if (env.getDialogId() == 39)
				{
					return sendQuestDialog(env, 5);
				}
				else
				{
					player.getSkillList().addSkill(player, 30003, 400);
					removeQuestItem(env, 182206128, 1);
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
}