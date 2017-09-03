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
package system.handlers.quest.gelkmaros;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _21289Aid_To_The_Agent extends QuestHandler
{
	private static final int questId = 21289;
	
	public _21289Aid_To_The_Agent()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestItem(164000103, questId); // Blessing Of Concentration.
		qe.registerQuestNpc(799340).addOnQuestStart(questId); // Athana.
		qe.registerQuestNpc(799340).addOnTalkEvent(questId); // Athana.
		qe.registerQuestNpc(799095).addOnTalkEvent(questId); // Mijien.
		qe.registerOnEnterZone(ZoneName.get("LOST_CITY_OF_MARAYAS_220070000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
		{
			if (targetId == 799340)
			{ // Athana.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						return sendQuestDialog(env, 4762);
					}
					case ASK_ACCEPTION:
					{
						return sendQuestDialog(env, 4);
					}
					case ACCEPT_QUEST:
					{
						QuestService.startQuest(env);
						giveQuestItem(env, 164000103, 10); // Blessing Of Concentration.
						return sendQuestDialog(env, 1003);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799095)
			{ // Mijien.
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else
				{
					return sendQuestEndDialog(env);
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(final QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int itemId = item.getItemId();
			final int var = qs.getQuestVarById(0);
			final int var1 = qs.getQuestVarById(1);
			if (itemId == 164000103)
			{ // Blessing Of Concentration.
				if (var == 0)
				{
					if ((var1 >= 0) && (var1 < 9) && player.isInsideZone(ZoneName.get("LOST_CITY_OF_MARAYAS_220070000")))
					{
						changeQuestStep(env, var1, var1 + 1, false, 1);
						removeQuestItem(env, 164000103, 1); // Blessing Of Concentration.
						return HandlerResult.SUCCESS;
					}
					else if (var1 == 9)
					{
						qs.setStatus(QuestStatus.REWARD);
						updateQuestStatus(env);
						return HandlerResult.SUCCESS;
					}
				}
			}
		}
		return HandlerResult.UNKNOWN;
	}
}