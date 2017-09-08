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

/**
 * @author Rinzler (Encom)
 */
public class _15322Scouting_Enshar extends QuestHandler
{
	public static final int questId = 15322;
	
	private static final int[] DF5_B =
	{
		219670,
		219671,
		219672,
		219673,
		219674,
		219675,
		219676,
		219677,
		219678,
		219679,
		219988
	};
	private static final int[] DF5_D =
	{
		219702,
		219703,
		219704,
		219705,
		219706,
		219707,
		219708,
		219709,
		219710,
		219711,
		219712,
		219713
	};
	private static final int[] DF5_F =
	{
		219733,
		219734,
		219735,
		219736,
		219737,
		219740,
		219741,
		219992,
		219993
	};
	private static final int[] DF5_H =
	{
		219752,
		219753,
		219754,
		219755,
		219756,
		219757,
		219758,
		219759,
		219760,
		219761
	};
	// private final static int[] DF5_J = {219791, 219792, 219793, 219794, 219795, 219796, 219797, 219798, 219799, 219997, 220012};
	
	public _15322Scouting_Enshar()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(805330).addOnQuestStart(questId); // Potencia.
		qe.registerQuestNpc(805330).addOnTalkEvent(questId); // Potencia.
		for (int mob : DF5_B)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		for (int mob2 : DF5_D)
		{
			qe.registerQuestNpc(mob2).addOnKillEvent(questId);
		}
		for (int mob3 : DF5_F)
		{
			qe.registerQuestNpc(mob3).addOnKillEvent(questId);
		}
		for (int mob4 : DF5_H)
		{
			qe.registerQuestNpc(mob4).addOnKillEvent(questId);
		}
		/*
		 * for (int mob5: DF5_J) { qe.registerQuestNpc(mob5).addOnKillEvent(questId); }
		 */
		// qe.registerOnEnterZone(ZoneName.get("WYRMCAST_LANDS_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("WASHRUN_STRETCH_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("THE_BLOOD_GRAINS_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("AURELIAN_TIMBERS_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("BONECREAK_VALLEY_220080000"), questId);
		qe.registerOnEnterZone(ZoneName.get("AEQUIS_HEADQUARTERS_210070000"), questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 805330)
			{ // Potencia.
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
			return defaultOnKillEvent(env, DF5_B, var1, var1 + 1, 1);
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
			return defaultOnKillEvent(env, DF5_D, var1, var1 + 1, 1);
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
			return defaultOnKillEvent(env, DF5_F, var1, var1 + 1, 1);
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
			return defaultOnKillEvent(env, DF5_H, var1, var1 + 1, 1);
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
		 * /Bug no idea why if (var == 9 && var1 >= 0 && var1 < 9) { return defaultOnKillEvent(env, DF5_J, var1, var1 + 1, 1); } else if (var == 9 && var1 == 9) { qs.setQuestVarById(1, 0); qs.setQuestVar(10); qs.setStatus(QuestStatus.REWARD); updateQuestStatus(env); return true; }
		 */
		return false;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if (zoneName == ZoneName.get("AEQUIS_HEADQUARTERS_210070000"))
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
			if (zoneName == ZoneName.get("WASHRUN_STRETCH_220080000"))
			{
				if (var == 0)
				{
					changeQuestStep(env, 0, 1, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("AURELIAN_TIMBERS_220080000"))
			{
				if (var == 2)
				{
					changeQuestStep(env, 2, 3, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("BONECREAK_VALLEY_220080000"))
			{
				if (var == 4)
				{
					changeQuestStep(env, 4, 5, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("THE_BLOOD_GRAINS_220080000"))
			{
				if (var == 6)
				{
					changeQuestStep(env, 6, 7, false);
					return true;
				}
			}
			/*
			 * else if (zoneName == ZoneName.get("WYRMCAST_LANDS_220080000")) { if (var == 8) { changeQuestStep(env, 8, 9, false); return true; } }
			 */
		}
		return false;
	}
}