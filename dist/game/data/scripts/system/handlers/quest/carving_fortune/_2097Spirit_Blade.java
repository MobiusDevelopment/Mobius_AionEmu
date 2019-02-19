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
package system.handlers.quest.carving_fortune;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class _2097Spirit_Blade extends QuestHandler
{
	private static final int questId = 2097;
	
	public _2097Spirit_Blade()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnLevelUp(questId);
		qe.registerQuestNpc(203550).addOnTalkEvent(questId);
		qe.registerQuestNpc(203546).addOnTalkEvent(questId);
		qe.registerQuestNpc(279034).addOnTalkEvent(questId);
		qe.registerQuestNpc(700509).addOnTalkEvent(questId);
		qe.registerQuestNpc(700510).addOnTalkEvent(questId);
		qe.registerQuestItem(182207085, questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 2096);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVars().getQuestVars();
		int targetId = 0;
		final QuestDialog dialog = env.getDialog();
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 203550:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
						}
						case STEP_TO_1:
						{
							if (var == 0)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						}
					}
					break;
				}
				case 203546:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
							return true;
						}
						case STEP_TO_2:
						{
							if (var == 1)
							{
								qs.setQuestVarById(0, var + 1);
								updateQuestStatus(env);
								PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
								return true;
							}
						}
					}
					break;
				}
				case 279034:
				{
					switch (env.getDialog())
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case CHECK_COLLECTED_ITEMS:
						{
							if (var == 2)
							{
								if (QuestService.collectItemCheck(env, true))
								{
									if (!giveQuestItem(env, 182207085, 1))
									{
										return true;
									}
									qs.setStatus(QuestStatus.REWARD);
									updateQuestStatus(env);
									PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(env.getVisibleObject().getObjectId(), 10));
									return sendQuestDialog(env, 10000);
								}
								return sendQuestDialog(env, 10001);
							}
						}
					}
					break;
				}
				case 700509:
				{
					if (dialog == QuestDialog.USE_OBJECT)
					{
						giveQuestItem(env, 182207086, 1);
						return true;
					}
					break;
				}
				case 700510:
				{
					if (dialog == QuestDialog.USE_OBJECT)
					{
						giveQuestItem(env, 182207087, 1);
						return true;
					}
					break;
				}
			}
		}
		else if ((qs.getStatus() == QuestStatus.REWARD) && (targetId == 203550))
		{
			return sendQuestEndDialog(env);
		}
		return false;
	}
}