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

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class _21323The_Shugo_Runaround extends QuestHandler
{
	private static final int questId = 21323;
	
	public _21323The_Shugo_Runaround()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799226).addOnQuestStart(questId); // Barretta.
		qe.registerQuestNpc(799226).addOnTalkEvent(questId); // Barretta.
		qe.registerQuestNpc(702726).addOnTalkEvent(questId); // Taserunerk.
		qe.registerQuestNpc(702728).addOnTalkEvent(questId); // Uzirunerk.
		qe.registerQuestNpc(702746).addOnTalkEvent(questId); // Zinarunerk.
		qe.registerQuestNpc(702747).addOnTalkEvent(questId); // Ruinrunerk.
		qe.registerQuestNpc(702748).addOnTalkEvent(questId); // Potarunerk.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestDialog dialog = env.getDialog();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 799226)
			{ // Barretta.
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
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 702726:
				{ // Taserunerk.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1011);
						}
						case STEP_TO_1:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 702728:
				{ // Uzirunerk.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1352);
						}
						case STEP_TO_2:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 702746:
				{ // Zinarunerk.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 1693);
						}
						case STEP_TO_3:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 702747:
				{ // Ruinrunerk.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 2034);
						}
						case STEP_TO_4:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
				case 702748:
				{ // Potarunerk.
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							return sendQuestDialog(env, 2375);
						}
						case SET_REWARD:
						{
							qs.setQuestVarById(0, qs.getQuestVarById(0) + 1);
							updateQuestStatus(env);
							qs.setStatus(QuestStatus.REWARD);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 799226)
			{ // Barretta.
				if (dialog == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}