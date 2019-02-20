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
package system.handlers.quest.cygnea;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _15042Calling_Kaisinel_Butterfly extends QuestHandler
{
	private static final int questId = 15042;
	
	public _15042Calling_Kaisinel_Butterfly()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(804885).addOnQuestStart(questId); // Telos
		qe.registerQuestNpc(804885).addOnTalkEvent(questId); // Telos
		qe.registerQuestItem(182215676, questId);
		qe.registerQuestItem(182215753, questId);
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
			if (targetId == 804885) // Telos
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env, 182215676, 1);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 804885) // Telos
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				removeQuestItem(env, 182215676, 1);
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final int id = item.getItemTemplate().getTemplateId();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((id != 182215676) || (qs.getStatus() == QuestStatus.COMPLETE))
		{
			return HandlerResult.UNKNOWN;
		}
		if (!player.isInsideZone(ZoneName.get("LF5_ItemUseArea_Q15042")))
		{
			return HandlerResult.UNKNOWN;
		}
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			if (var == 0)
			{
				if (var1 < 2)
				{
					qs.setQuestVarById(1, var1 + 1);
					updateQuestStatus(env);
				}
				else if (var1 == 2)
				{
					giveQuestItem(env, 182215753, 1);
					removeQuestItem(env, 182215676, 1);
					qs.setQuestVar(1);
					qs.setStatus(QuestStatus.REWARD);
					updateQuestStatus(env);
					return HandlerResult.SUCCESS;
				}
			}
		}
		return HandlerResult.SUCCESS;
	}
}