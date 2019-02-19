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
public class _23817Free_Spirit extends QuestHandler
{
	private static final int questId = 23817;
	
	public _23817Free_Spirit()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804590).addOnTalkEvent(questId); // Banuan.
		qe.registerQuestNpc(804594).addOnTalkEvent(questId); // Commander Anoha.
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
			if (targetId == 804594)
			{ // Commander Anoha.
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else if (dialog == QuestDialog.SET_REWARD)
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
			if (targetId == 804590)
			{ // Banuan.
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
					QuestService.addNewSpawn(600090000, player.getInstanceId(), 804594, npc.getX(), npc.getY(), npc.getZ(), (byte) 0); // Commander Anoha.
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