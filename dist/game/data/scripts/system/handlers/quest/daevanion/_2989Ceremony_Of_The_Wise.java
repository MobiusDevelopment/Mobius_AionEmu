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
package system.handlers.quest.daevanion;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _2989Ceremony_Of_The_Wise extends QuestHandler
{
	private static final int questId = 2989;
	
	public _2989Ceremony_Of_The_Wise()
	{
		super(questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 2988, true);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(204056).addOnQuestStart(questId);
		qe.registerQuestNpc(204057).addOnQuestStart(questId);
		qe.registerQuestNpc(204058).addOnQuestStart(questId);
		qe.registerQuestNpc(204059).addOnQuestStart(questId);
		qe.registerQuestNpc(801222).addOnQuestStart(questId);
		qe.registerQuestNpc(801223).addOnQuestStart(questId);
		qe.registerQuestNpc(204146).addOnQuestStart(questId);
		qe.registerQuestNpc(204146).addOnTalkEvent(questId);
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
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 204146)
			{
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 1011);
				}
				else
				{
					return sendQuestStartDialog(env);
				}
			}
		}
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			final PlayerClass playerClass = player.getCommonData().getPlayerClass();
			switch (targetId)
			{
				case 204056:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if ((playerClass == PlayerClass.GLADIATOR) || (playerClass == PlayerClass.TEMPLAR))
							{
								return sendQuestDialog(env, 1352);
							}
							else
							{
								return sendQuestDialog(env, 1438);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 204057:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if ((playerClass == PlayerClass.ASSASSIN) || (playerClass == PlayerClass.RANGER))
							{
								return sendQuestDialog(env, 1693);
							}
							else
							{
								return sendQuestDialog(env, 1779);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 204058:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if ((playerClass == PlayerClass.SORCERER) || (playerClass == PlayerClass.SPIRIT_MASTER))
							{
								return sendQuestDialog(env, 2034);
							}
							else
							{
								return sendQuestDialog(env, 2120);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 204059:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if ((playerClass == PlayerClass.CLERIC) || (playerClass == PlayerClass.CHANTER) || (playerClass == PlayerClass.AETHERTECH))
							{
								return sendQuestDialog(env, 2375);
							}
							else
							{
								return sendQuestDialog(env, 2461);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 801222:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (playerClass == PlayerClass.GUNSLINGER)
							{
								return sendQuestDialog(env, 2548);
							}
							else
							{
								return sendQuestDialog(env, 2568);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 801223:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (playerClass == PlayerClass.SONGWEAVER)
							{
								return sendQuestDialog(env, 2633);
							}
							else
							{
								return sendQuestDialog(env, 2653);
							}
						case STEP_TO_1:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
				case 204146:
					switch (env.getDialog())
					{
						case START_DIALOG:
							if (var == 1)
							{
								return sendQuestDialog(env, 2716);
							}
							else if (var == 2)
							{
								return sendQuestDialog(env, 3057);
							}
							else if (var == 3)
							{
								if (player.getCommonData().getDp() < 4000)
								{
									return sendQuestDialog(env, 3484);
								}
								else
								{
									return sendQuestDialog(env, 3398);
								}
							}
							else if (var == 4)
							{
								if (player.getCommonData().getDp() < 4000)
								{
									return sendQuestDialog(env, 3825);
								}
								else
								{
									return sendQuestDialog(env, 3739);
								}
							}
						case SELECT_REWARD:
							if (var == 3)
							{
								playQuestMovie(env, 137);
								player.getCommonData().setDp(0);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							else if (var == 4)
							{
								playQuestMovie(env, 137);
								player.getCommonData().setDp(0);
								qs.setStatus(QuestStatus.REWARD);
								updateQuestStatus(env);
								return sendQuestDialog(env, 5);
							}
							else
							{
								return sendQuestEndDialog(env);
							}
						case STEP_TO_2:
							qs.setQuestVarById(0, var + 1);
							updateQuestStatus(env);
							return sendQuestDialog(env, 3057);
						case STEP_TO_4:
							qs.setQuestVarById(0, 3);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
						case STEP_TO_5:
							qs.setQuestVarById(0, 4);
							updateQuestStatus(env);
							PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
							return true;
					}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204146)
			{
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
}