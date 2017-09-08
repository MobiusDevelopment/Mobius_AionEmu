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
package system.handlers.quest.harbinger_landing;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _25480Clearing_The_Way extends QuestHandler
{
	private static final int questId = 25480;
	private static final Set<Integer> ab1BLv8D;
	
	public _25480Clearing_The_Way()
	{
		super(questId);
	}
	
	static
	{
		ab1BLv8D = new HashSet<>();
		ab1BLv8D.add(805822);
		ab1BLv8D.add(805823);
		ab1BLv8D.add(805824);
		ab1BLv8D.add(805825);
	}
	
	@Override
	public void register()
	{
		final Iterator<Integer> iter = ab1BLv8D.iterator();
		while (iter.hasNext())
		{
			final int ab1Id = iter.next();
			qe.registerQuestNpc(ab1Id).addOnQuestStart(questId);
			qe.registerQuestNpc(ab1Id).addOnTalkEvent(questId);
			qe.registerQuestNpc(884009).addOnKillEvent(questId);
			qe.registerQuestNpc(884010).addOnKillEvent(questId);
			qe.registerQuestNpc(884011).addOnKillEvent(questId);
			qe.registerQuestNpc(884012).addOnKillEvent(questId);
			qe.registerQuestNpc(884013).addOnKillEvent(questId);
			qe.registerQuestNpc(884014).addOnKillEvent(questId);
			qe.registerQuestNpc(884015).addOnKillEvent(questId);
			qe.registerQuestNpc(884016).addOnKillEvent(questId);
			qe.registerQuestNpc(884017).addOnKillEvent(questId);
			qe.registerQuestNpc(884018).addOnKillEvent(questId);
			qe.registerQuestNpc(884019).addOnKillEvent(questId);
			qe.registerQuestNpc(884020).addOnKillEvent(questId);
			qe.registerQuestNpc(884021).addOnKillEvent(questId);
			qe.registerQuestNpc(884022).addOnKillEvent(questId);
			qe.registerQuestNpc(884023).addOnKillEvent(questId);
			qe.registerQuestNpc(884024).addOnKillEvent(questId);
			qe.registerQuestNpc(884025).addOnKillEvent(questId);
			qe.registerQuestNpc(884026).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final int targetId = env.getTargetId();
		final Player player = env.getPlayer();
		if (!ab1BLv8D.contains(targetId))
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
		else if (qs.getStatus() == QuestStatus.START)
		{
			switch (dialog)
			{
				case START_DIALOG:
				{
					return sendQuestDialog(env, 2375);
				}
				case SELECT_REWARD:
				{
					changeQuestStep(env, 1, 2, true);
					return sendQuestEndDialog(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (env.getDialog() == QuestDialog.START_DIALOG)
			{
				return sendQuestDialog(env, 10002);
			}
			else
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
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			switch (env.getTargetId())
			{
				case 884009:
				case 884010:
				case 884011:
				case 884012:
				case 884013:
				case 884014:
				case 884015:
				case 884016:
				case 884017:
				case 884018:
				case 884019:
				case 884020:
				case 884021:
				case 884022:
				case 884023:
				case 884024:
				case 884025:
				case 884026:
				{
					if (qs.getQuestVarById(1) < 1)
					{
						qs.setQuestVarById(1, qs.getQuestVarById(1) + 1);
						updateQuestStatus(env);
					}
					if (qs.getQuestVarById(1) >= 1)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
					}
				}
			}
		}
		return false;
	}
}