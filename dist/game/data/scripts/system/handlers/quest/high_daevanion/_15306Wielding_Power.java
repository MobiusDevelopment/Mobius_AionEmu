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
package system.handlers.quest.high_daevanion;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;

/**
 * @author Rinzler (Encom)
 */
public class _15306Wielding_Power extends QuestHandler
{
	private static final int questId = 15306;
	
	private static final int[] LDF4AdvanceMobs =
	{
		233939,
		233940,
		233941,
		233942,
		233900,
		234312,
		234313,
		234692
	};
	private static final int[] LDF5FortressMobs =
	{
		234292,
		234294,
		234295,
		234296,
		234298,
		234529
	};
	private static final int[] Ab1NewMobs =
	{
		883301,
		883302,
		883303,
		883304,
		883305,
		883306,
		883307,
		883308
	};
	private static final int[] Boss1 =
	{
		232853,
		233491,
		234190,
		233544
	};
	private static final int[] Boss2 =
	{
		236277,
		231073,
		231130
	};
	
	public _15306Wielding_Power()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805327).addOnQuestStart(questId); // Rike.
		qe.registerQuestNpc(805327).addOnTalkEvent(questId); // Rike.
		qe.registerQuestNpc(805328).addOnTalkEvent(questId); // Efaion.
		qe.registerQuestNpc(702829).addOnTalkEvent(questId); // Sealed Book.
		qe.registerQuestNpc(702862).addOnTalkEvent(questId); // Cursed Book.
		for (int mob : LDF4AdvanceMobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : LDF5FortressMobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob : Ab1NewMobs)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int boss : Boss1)
		{
			qe.registerQuestNpc(boss).addOnKillEvent(questId);
		}
		for (int boss : Boss2)
		{
			qe.registerQuestNpc(boss).addOnKillEvent(questId);
		}
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		final int var = qs.getQuestVarById(0);
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 805327)
			{ // Rike.
				switch (env.getDialog())
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
					case REFUSE_QUEST_SIMPLE:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 805328)
			{ // Efaion.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 1)
						{
							return sendQuestDialog(env, 1352);
						}
						else if (var == 2)
						{
							return sendQuestDialog(env, 1693);
						}
						else if (var == 9)
						{
							return sendQuestDialog(env, 3741);
						}
					}
					case SELECT_ACTION_1012:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1012);
						}
					}
					case STEP_TO_1:
					{
						giveQuestItem(env, 152222052, 1); // Morph Method: Divine Aether.
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_3:
					{
						removeQuestItem(env, 182215836, 150); // Divine Aether.
						removeQuestItem(env, 182215837, 10); // Sealed Book.
						removeQuestItem(env, 182215921, 10); // Strange Fragment.
						removeQuestItem(env, 182215838, 3); // Honorable Daeva Decoration.
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_9:
					{
						giveQuestItem(env, 182215864, 1); // Daevanion Weapon Prototype.
						changeQuestStep(env, 9, 10, true);
						return closeDialogWindow(env);
					}
					case CHECK_COLLECTED_ITEMS:
					{
						return checkQuestItems(env, 1, 2, false, 10000, 10001);
					}
					case FINISH_DIALOG:
					{
						if (var == 1)
						{
							defaultCloseDialog(env, 2, 2);
						}
						else if (var == 1)
						{
							defaultCloseDialog(env, 1, 1);
						}
					}
				}
			}
			else if (targetId == 702829)
			{ // Sealed Book.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						return closeDialogWindow(env);
					}
				}
			}
			else if (targetId == 702862)
			{ // Cursed Book.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						return closeDialogWindow(env);
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805327)
			{ // Rike.
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
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 3)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 59))
				{
					return defaultOnKillEvent(env, LDF4AdvanceMobs, var1, var1 + 1, 1);
				}
				else if (var1 == 59)
				{
					qs.setQuestVar(4);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 4)
			{
				final int var2 = qs.getQuestVarById(2);
				if ((var2 >= 0) && (var2 < 59))
				{
					return defaultOnKillEvent(env, LDF5FortressMobs, var2, var2 + 1, 1);
				}
				else if (var2 == 59)
				{
					qs.setQuestVar(5);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 5)
			{
				final int var3 = qs.getQuestVarById(3);
				if ((var3 >= 0) && (var3 < 29))
				{
					return defaultOnKillEvent(env, Ab1NewMobs, var3, var3 + 1, 1);
				}
				else if (var3 == 29)
				{
					qs.setQuestVar(6);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 6)
			{
				final int var4 = qs.getQuestVarById(4);
				if ((var4 >= 0) && (var4 < 1))
				{
					return defaultOnKillEvent(env, Boss1, var4, var4 + 1, 1);
				}
				else if (var4 == 1)
				{
					qs.setQuestVar(7);
					updateQuestStatus(env);
					return true;
				}
			}
			else if (var == 7)
			{
				final int var5 = qs.getQuestVarById(5);
				if ((var5 >= 0) && (var5 < 5))
				{
					return defaultOnKillEvent(env, Boss2, var5, var5 + 1, 1);
				}
				else if (var5 == 5)
				{
					qs.setQuestVar(8);
					updateQuestStatus(env);
					return true;
				}
			}
		}
		return false;
	}
}