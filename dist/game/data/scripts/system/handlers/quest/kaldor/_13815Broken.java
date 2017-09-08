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
package system.handlers.quest.kaldor;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rinzler (Encom)
 */
public class _13815Broken extends QuestHandler
{
	private static final int questId = 13815;
	
	public _13815Broken()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804586).addOnTalkEvent(questId); // Venia.
		qe.registerQuestNpc(804595).addOnTalkEvent(questId); // Commander Anoha.
		qe.registerQuestNpc(855263).addOnKillEvent(questId); // Berserk Anoha.
		qe.registerQuestNpc(855263).addOnAddAggroListEvent(getQuestId()); // Berserk Anoha.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 804595)
			{ // Commander Anoha.
				if (dialog == QuestDialog.START_DIALOG)
				{
					giveQuestItem(env, 182215545, 1);
					return sendQuestDialog(env, 1011);
				}
				else if (dialog == QuestDialog.ASK_ACCEPTION)
				{
					return sendQuestDialog(env, 4);
				}
				else if (dialog == QuestDialog.ACCEPT_QUEST)
				{
					final Npc npc = (Npc) env.getVisibleObject();
					npc.getController().onDelete();
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804586)
			{ // Venia.
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
			final int var = qs.getQuestVarById(0);
			if (var == 0)
			{
				final int targetId = env.getTargetId();
				if (targetId == 855263)
				{ // Berserk Anoha.
					final Npc npc = (Npc) env.getVisibleObject();
					QuestService.addNewSpawn(600090000, player.getInstanceId(), 804595, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // Commander Anoha.
					return defaultOnKillEvent(env, 855263, 1, true); // Berserk Anoha.
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onAddAggroListEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			QuestService.startQuest(env);
			return true;
		}
		return false;
	}
}