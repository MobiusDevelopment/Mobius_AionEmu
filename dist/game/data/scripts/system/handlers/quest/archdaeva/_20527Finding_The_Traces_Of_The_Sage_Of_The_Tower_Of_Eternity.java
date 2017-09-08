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
package system.handlers.quest.archdaeva;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
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
public class _20527Finding_The_Traces_Of_The_Sage_Of_The_Tower_Of_Eternity extends QuestHandler
{
	public static final int questId = 20527;
	private static final int[] npcs =
	{
		806079,
		806296,
		703321,
		703322,
		703323,
		731711,
		731712,
		731713
	};
	private static final int[] DF6MissionLightRa71An =
	{
		244123
	}; // 서광의 잠입부대 가디언 정찰병.
	
	public _20527Finding_The_Traces_Of_The_Sage_Of_The_Tower_Of_Eternity()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
		for (int mob : DF6MissionLightRa71An)
		{
			qe.registerQuestNpc(mob).addOnKillEvent(questId);
		}
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182216087, questId); // 서광의 잠입부대 비밀 문서.
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerOnEnterZone(ZoneName.get("AZURELIGHT_FOREST_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("ZENZEN_TRIBAL_GROUNDS_220110000"), questId);
		qe.registerOnEnterZone(ZoneName.get("FEATHERFERN_JUNGLE_220110000"), questId);
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 20526, true);
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
		final int var = qs.getQuestVarById(0);
		int targetId = 0;
		if (env.getVisibleObject() instanceof Npc)
		{
			targetId = ((Npc) env.getVisibleObject()).getNpcId();
		}
		if (qs.getStatus() == QuestStatus.START)
		{
			if (targetId == 806079)
			{ // Feregran.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 0)
						{
							return sendQuestDialog(env, 1011);
						}
						else if (var == 2)
						{
							return sendQuestDialog(env, 1695);
						}
						else if (var == 12)
						{
							return sendQuestDialog(env, 7182);
						}
						else if (var == 13)
						{
							return sendQuestDialog(env, 7523);
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
						changeQuestStep(env, 0, 1, false);
						return closeDialogWindow(env);
					}
					case SELECT_REWARD:
					{
						changeQuestStep(env, 2, 3, false);
						return closeDialogWindow(env);
					}
					case STEP_TO_14:
					{
						removeQuestItem(env, 182216106, 1); // 암호화 된 비밀 문서.
						return defaultCloseDialog(env, 13, 14, false, false, 182216087, 1, 0, 0); // 서광의 잠입부대 비밀 문서.
					}
					case CHECK_COLLECTED_ITEMS:
					{
						return checkQuestItems(env, 12, 13, false, 10000, 10001);
					}
					case FINISH_DIALOG:
					{
						if (var == 13)
						{
							defaultCloseDialog(env, 13, 13);
						}
						else if (var == 12)
						{
							defaultCloseDialog(env, 12, 12);
						}
					}
				}
			}
			if (targetId == 806296)
			{ // 위자보보.
				switch (env.getDialog())
				{
					case START_DIALOG:
					{
						if (var == 1)
						{
							return sendQuestDialog(env, 1355);
						}
					}
					case STEP_TO_2:
					{
						changeQuestStep(env, 1, 2, false);
						return closeDialogWindow(env);
					}
				}
			}
			if (targetId == 703321)
			{ // 움찔거리는 나무 넝쿨.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 4)
						{
							changeQuestStep(env, 4, 5, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 731711)
			{ // 빛나는 영원의 탑 파편.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 5)
						{
							changeQuestStep(env, 5, 6, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 703322)
			{ // 움직이는 가마솥.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 7)
						{
							changeQuestStep(env, 7, 8, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 731712)
			{ // 영롱한 영원의 탑 파편.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 8)
						{
							changeQuestStep(env, 8, 9, false);
							return closeDialogWindow(env);
						}
					}
				}
			}
			if (targetId == 703323)
			{ // 꿈틀거리는 자루.
				switch (env.getDialog())
				{
					case USE_OBJECT:
					{
						if (var == 10)
						{
							changeQuestStep(env, 10, 11, false);
							QuestService.addNewSpawn(220110000, 1, 244123, 2014.653f, 634.15436f, 274.15616f, (byte) 36); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2001.704f, 649.0103f, 274.18814f, (byte) 10); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2005.439f, 635.8378f, 274.1702f, (byte) 20); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2030.114f, 640.89404f, 274.76425f, (byte) 115); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2022.2664f, 660.66705f, 273.7082f, (byte) 4); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2022.0647f, 674.1195f, 274.75f, (byte) 92); // 서광의 잠입부대 가디언 정찰병.
							QuestService.addNewSpawn(220110000, 1, 244123, 2010.6356f, 652.08374f, 273.75f, (byte) 102); // 서광의 잠입부대 가디언 정찰병.
							return closeDialogWindow(env);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 806079)
			{ // Feregran.
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
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (var == 11)
			{
				final int var1 = qs.getQuestVarById(1);
				if ((var1 >= 0) && (var1 < 6))
				{
					return defaultOnKillEvent(env, DF6MissionLightRa71An, var1, var1 + 1, 1);
				}
				else if (var1 == 6)
				{
					qs.setQuestVar(12);
					updateQuestStatus(env);
					QuestService.addNewSpawn(220110000, 1, 244124, player.getX(), player.getY(), player.getZ(), (byte) 0); // 잠입부대 부사관 라노메스.
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getQuestVarById(0) == 14))
		{
			return HandlerResult.fromBoolean(useQuestItem(env, item, 14, 15, true));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int var = qs.getQuestVarById(0);
			if (zoneName == ZoneName.get("AZURELIGHT_FOREST_220110000"))
			{
				if (var == 3)
				{
					changeQuestStep(env, 3, 4, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("ZENZEN_TRIBAL_GROUNDS_220110000"))
			{
				if (var == 6)
				{
					changeQuestStep(env, 6, 7, false);
					return true;
				}
			}
			else if (zoneName == ZoneName.get("FEATHERFERN_JUNGLE_220110000"))
			{
				if (var == 9)
				{
					changeQuestStep(env, 9, 10, false);
					return true;
				}
			}
		}
		return false;
	}
}