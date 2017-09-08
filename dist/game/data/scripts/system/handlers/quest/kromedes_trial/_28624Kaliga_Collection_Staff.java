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

/**
 * @author Rinzler (Encom)
 */
public class _28624Kaliga_Collection_Staff extends QuestHandler
{
	private static final int questId = 28624;
	
	public _28624Kaliga_Collection_Staff()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(730332).addOnQuestStart(questId); // Kaliga's Staff Rack.
		qe.registerQuestNpc(730332).addOnTalkEvent(questId); // Kaliga's Staff Rack.
	}
	
	@Override
	public boolean onCanAct(QuestEnv env, QuestActionType questEventType, Object... objects)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(env.getQuestId());
		final int targetId = env.getTargetId();
		if (targetId == 730332)
		{ // Kaliga's Staff Rack.
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
		if (targetId == 730332)
		{ // Kaliga's Staff Rack.
			final PlayerClass playerClass = player.getCommonData().getPlayerClass();
			if (((playerClass == PlayerClass.PRIEST) || (playerClass == PlayerClass.CHANTER) || (playerClass == PlayerClass.CLERIC)) && (player.getCommonData().getRace() == Race.ASMODIANS))
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