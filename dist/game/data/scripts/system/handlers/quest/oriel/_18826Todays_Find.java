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
package system.handlers.quest.oriel;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _18826Todays_Find extends QuestHandler
{
	private static final int questId = 18826;
	
	public _18826Todays_Find()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(830660).addOnQuestStart(questId); // Gomirunerk.
		qe.registerQuestNpc(830661).addOnQuestStart(questId); // Lisandinerk.
		qe.registerQuestNpc(830660).addOnTalkEvent(questId); // Gomirunerk.
		qe.registerQuestNpc(830661).addOnTalkEvent(questId); // Lisandinerk.
		qe.registerQuestNpc(730522).addOnTalkEvent(questId); // Vintage Grab Box.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			switch (targetId)
			{
				case 830660: // Gomirunerk.
				case 830661:
				{ // Lisandinerk.
					switch (dialog)
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1011);
						}
						default:
						{
							return sendQuestStartDialog(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 730522)
			{ // Vintage Grab Box.
				switch (dialog)
				{
					case USE_OBJECT:
					{
						return sendQuestDialog(env, 2375);
					}
					case SELECT_REWARD:
					{
						changeQuestStep(env, 0, 0, true);
						return sendQuestDialog(env, 5);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 730522)
			{ // Vintage Grab Box.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}