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
package system.handlers.quest.levinshor;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _13704Fona_Quick_Fix extends QuestHandler
{
	private static final int questId = 13704;
	
	public _13704Fona_Quick_Fix()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(802331).addOnQuestStart(questId); // Fona.
		qe.registerQuestNpc(802331).addOnTalkEvent(questId); // Fona.
		qe.registerQuestItem(182215527, questId); // Intrusion Mine.
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
			if (targetId == 802331)
			{ // Fona.
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				return sendQuestStartDialog(env, 182215527, 1); // Intrusion Mine.
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 802331)
			{ // Fona.
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 2375);
				}
				removeQuestItem(env, 182215527, 1); // Intrusion Mine.
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			if (qs.getQuestVarById(0) == 0)
			{
				qs.setQuestVar(1);
				changeQuestStep(env, 1, 1, true);
				return HandlerResult.SUCCESS;
			}
		}
		return HandlerResult.FAILED;
	}
}