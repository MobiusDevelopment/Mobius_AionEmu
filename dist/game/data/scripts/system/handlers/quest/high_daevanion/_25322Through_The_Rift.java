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
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.world.zone.ZoneName;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

public class _25322Through_The_Rift extends QuestHandler
{
	public static final int questId = 25322;
	
	private static final int[] LF5_B =
	{
		235801,
		235802,
		235803,
		235804,
		235805,
		235806,
		235807,
		235808,
		235809,
		235810,
		235811,
		235812
	};
	private static final int[] LF5_D =
	{
		235852,
		235853,
		235854,
		235855,
		235856,
		235857,
		235858,
		235859,
		235860,
		235861
	};
	private static final int[] LF5_F =
	{
		235876,
		235877,
		235878,
		235879,
		235882,
		235883,
		235884,
		235885,
		235886
	};
	private static final int[] LF5_H =
	{
		235888,
		235889,
		235890,
		235891,
		235892,
		235893,
		235894,
		235895,
		235896
	};
	// private final static int[] LF5_J = {235939, 235940, 235941, 235942, 235944, 235945, 235947};
	
	public _25322Through_The_Rift()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805342).addOnQuestStart(questId); // Hikait.
		qe.registerQuestNpc(805342).addOnTalkEvent(questId); // Hikait.
		for (int mob : LF5_B)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob2 : LF5_D)
		{
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
		for (int mob3 : LF5_F)
		{
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
		}
		for (int mob4 : LF5_H)
		{
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
		}
		/*
		 * for (int mob5: LF5_J) { qe.registerQuestNpc(mob5).addOnKillEvent(questId); }
		 */
		// qe.registerOnEnterZone(ZoneName.get("WAILING_DUNES_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("CRIMSON_HILLS_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("TWILIGHT_TEMPLE_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DRAGONREST_TEMPLE_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("PERENNIAL_MOSSWOOD_210070000"), questId);
		qe.registerOnEnterZone(ZoneName.get("DRAGON_LORDS_CENTERPIECE_210070000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805342)
			{ // Hikait.
				if (env.getDialog() == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 10002);
				}
				else if (env.getDialog() == QuestDialog.SELECT_REWARD)
				{
					return sendQuestDialog(env, 5);
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
		final int var = qs.getQuestVarById(0);
		final int var1 = qs.getQuestVarById(1);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		if ((var == 1) && (var1 >= 0) && (var1 < 9))
		{
			return defaultOnKillEvent(env, LF5_B, var1, var1 + 1, 1);
		}
		else if ((var == 1) && (var1 == 9))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 1, 2, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 3) && (var1 >= 0) && (var1 < 9))
		{
			return defaultOnKillEvent(env, LF5_D, var1, var1 + 1, 1);
		}
		else if ((var == 3) && (var1 == 9))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 3, 4, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 5) && (var1 >= 0) && (var1 < 9))
		{
			return defaultOnKillEvent(env, LF5_F, var1, var1 + 1, 1);
		}
		else if ((var == 5) && (var1 == 9))
		{
			qs.setQuestVarById(1, 0);
			changeQuestStep(env, 5, 6, false);
			updateQuestStatus(env);
			return true;
		}
		if ((var == 7) && (var1 >= 0) && (var1 < 9))
		{
			return defaultOnKillEvent(env, LF5_H, var1, var1 + 1, 1);
		}
		else if ((var == 7) && (var1 == 9))
		{
			qs.setQuestVarById(1, 0);
			qs.setQuestVar(10);
			qs.setStatus(QuestStatus.REWARD);
			updateQuestStatus(env);
			return true;
		}
		/*
		 * /Bug no idea why if (var == 9 && var1 >= 0 && var1 < 9) { return defaultOnKillEvent(env, LF5_J, var1, var1 + 1, 1); } else if (var == 9 && var1 == 9) { qs.setQuestVarById(1, 0); qs.setQuestVar(10); qs.setStatus(QuestStatus.REWARD); updateQuestStatus(env); return true; }
		 */
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (zoneName == ZoneName.get("DRAGONREST_TEMPLE_220080000"))
		{
			if ((qs == null) || qs.canRepeat())
			{
				env.setQuestId(questId);
				if (QuestService.startQuest(env))
				{
					return true;
				}
			}
		}
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("CRIMSON_HILLS_210070000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("DRAGON_LORDS_CENTERPIECE_210070000"))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("TWILIGHT_TEMPLE_210070000"))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("PERENNIAL_MOSSWOOD_210070000"))
			{
				if (var == 6)
				{
					changeQuestStep(env, 6, 7, false);
					return true;
				}
			}
			/*
			 * else if (zoneName == ZoneName.get("WAILING_DUNES_210070000")) { if (var == 8) { changeQuestStep(env, 8, 9, false); return true; } }
			 */
		}
		return false;
	}
}