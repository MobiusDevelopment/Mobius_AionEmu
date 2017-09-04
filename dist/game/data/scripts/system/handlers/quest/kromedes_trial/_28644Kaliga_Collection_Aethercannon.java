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
package system.handlers.quest.kromedes_trial;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestActionType;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _28644Kaliga_Collection_Aethercannon extends QuestHandler
{
	private static final int questId = 28644;
	
	public _28644Kaliga_Collection_Aethercannon()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(730776).addOnQuestStart(questId); // Kaliga's Aethercannon Rack.
		qe.registerQuestNpc(730776).addOnTalkEvent(questId); // Kaliga's Aethercannon Rack.
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		final int targetId = env.getTargetId();
		if (targetId == 730776)
		{ // Kaliga's Aethercannon Rack.
			if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (targetId == 730776)
		{ // Kaliga's Aethercannon Rack.
			final PlayerClass playerClass = player.getCommonData().getPlayerClass();
			if (((playerClass == PlayerClass.TECHNIST) || (playerClass == PlayerClass.GUNSLINGER)) && (player.getCommonData().getRace() == Race.ASMODIANS))
			{
				if ((qs == null) || (qs.getStatus() == QuestStatus.NONE) || qs.canRepeat())
				{
					if (env.getDialog() == QuestDialog.USE_OBJECT)
					{
						return sendQuestDialog(env, 1011);
					}
					else
					{
						return sendQuestStartDialog(env);
					}
				}
				else if ((qs != null) && (qs.getStatus() == QuestStatus.START) && (qs.getQuestVarById(0) == 0))
				{
					if (env.getDialog() == QuestDialog.USE_OBJECT)
					{
						return sendQuestDialog(env, 2375);
					}
					else if (env.getDialogId() == 39)
					{
						if (player.getInventory().getItemCountByItemId(185000102) >= 1)
						{
							removeQuestItem(env, 185000102, 1);
							qs.setStatus(QuestStatus.REWARD);
							qs.setQuestVar(1);
							qs.setCompleteCount(0);
							updateQuestStatus(env);
							return sendQuestDialog(env, 5);
						}
						else
						{
							return sendQuestDialog(env, 2716);
						}
					}
				}
				else if ((qs != null) && (qs.getStatus() == QuestStatus.REWARD))
				{
					final int var = qs.getQuestVarById(0);
					switch (env.getDialog())
					{
						case USE_OBJECT:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 5);
							}
						}
						case SELECT_NO_REWARD:
						{
							QuestService.finishQuest(env, qs.getQuestVars().getQuestVars() - 1);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
			return false;
		}
		return false;
	}
}