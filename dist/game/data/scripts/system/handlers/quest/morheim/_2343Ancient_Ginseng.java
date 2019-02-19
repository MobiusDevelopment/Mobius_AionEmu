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
package system.handlers.quest.morheim;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;

/**
 * @author Rinzler (Encom)
 */
public class _2343Ancient_Ginseng extends QuestHandler
{
	private static final int questId = 2343;
	
	public _2343Ancient_Ginseng()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestItem(182204134, questId);
		qe.registerQuestNpc(700243).addOnTalkEvent(questId);
		qe.registerCanAct(questId, 700243);
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			return HandlerResult.fromBoolean(sendQuestDialog(env, 4));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		int targetId = 0;
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 0)
			{
				if (env.getDialog() == QuestDialog.ACCEPT_QUEST)
				{
					QuestService.startQuest(env);
					return closeDialogWindow(env);
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 700243)
			{
				final Npc npc = player.getPosition().getWorldMapInstance().getNpc(212814);
				if (npc == null)
				{
					QuestService.addNewSpawn(player.getWorldId(), player.getInstanceId(), 212814, 1008.36f, 481.88f, 509.3f, (byte) 60);
					removeQuestItem(env, 182204134, 1);
					qs.setStatus(QuestStatus.REWARD);
					QuestService.finishQuest(env);
					return true;
				}
			}
		}
		return false;
	}
}