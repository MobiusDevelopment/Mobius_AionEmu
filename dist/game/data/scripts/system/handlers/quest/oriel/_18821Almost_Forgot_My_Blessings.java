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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _18821Almost_Forgot_My_Blessings extends QuestHandler
{
	private static final int questId = 18821;
	private static final Set<Integer> butlersElyos;
	
	public _18821Almost_Forgot_My_Blessings()
	{
		super(questId);
	}
	
	static
	{
		butlersElyos = new HashSet<>();
		butlersElyos.add(810017);
		butlersElyos.add(810018);
		butlersElyos.add(810019);
		butlersElyos.add(810020);
		butlersElyos.add(810021);
	}
	
	@Override
	public void register()
	{
		final Iterator<Integer> iter = butlersElyos.iterator();
		while (iter.hasNext())
		{
			final int butlerId = iter.next();
			qe.registerQuestNpc(butlerId).addOnQuestStart(questId);
			qe.registerQuestNpc(butlerId).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final int targetId = env.getTargetId();
		if (!butlersElyos.contains(targetId))
		{
			return false;
		}
		final House house = player.getActiveHouse();
		if ((house == null) || (house.getButler() == null) || (house.getButler().getNpcId() != targetId))
		{
			return false;
		}
		final QuestDialog dialog = env.getDialog();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			switch (dialog)
			{
				case START_DIALOG:
					return sendQuestDialog(env, 1011);
				case ACCEPT_QUEST:
					return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (dialog)
			{
				case START_DIALOG:
					return sendQuestDialog(env, 2375);
				case SELECT_REWARD:
					changeQuestStep(env, 0, 0, true);
					return sendQuestDialog(env, 5);
				case SELECT_NO_REWARD:
					return sendQuestEndDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			switch (dialog)
			{
				case USE_OBJECT:
					return sendQuestDialog(env, 5);
				case SELECT_NO_REWARD:
					sendQuestEndDialog(env);
					return true;
			}
		}
		return false;
	}
}