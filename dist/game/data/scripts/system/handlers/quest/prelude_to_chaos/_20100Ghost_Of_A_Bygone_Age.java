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
package system.handlers.quest.prelude_to_chaos;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.HandlerResult;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
public class _20100Ghost_Of_A_Bygone_Age extends QuestHandler
{
	private static final int questId = 20100;
	
	public _20100Ghost_Of_A_Bygone_Age()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcIds =
		{
			804719,
			802363,
			802463
		};
		qe.registerOnLevelUp(questId);
		qe.registerQuestItem(182215449, questId);
		for (int npcId : npcIds)
		{
			qe.registerQuestNpc(npcId).addOnTalkEvent(questId);
		}
		qe.registerOnEnterZone(ZoneName.get("PINNACLE_CATARACT_OUTPOST_600100000"), questId);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 804719:
				{ // Haldor.
					switch (dialog)
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
							TeleportService2.teleportTo(player, 600100000, 1786.3436f, 1738.9655f, 298.82288f, (byte) 13, TeleportAnimation.BEAM_ANIMATION);
							changeQuestStep(env, 0, 1, false);
							return closeDialogWindow(env);
						}
					}
					break;
				}
				case 802363:
				{ // Girtan.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 2)
							{
								return sendQuestDialog(env, 1693);
							}
						}
						case STEP_TO_3:
						{
							return defaultCloseDialog(env, 2, 3);
						}
					}
					break;
				}
				case 802463:
				{ // Kahrun.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 3)
							{
								return sendQuestDialog(env, 2034);
							}
						}
						case STEP_TO_4:
						{
							return defaultCloseDialog(env, 3, 4, 182215449, 1, 0, 0);
						}
					}
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 802463)
			{ // Kahrun.
				if (env.getDialog() == QuestDialog.USE_OBJECT)
				{
					return sendQuestDialog(env, 10002);
				}
				return sendQuestEndDialog(env);
			}
		}
		return false;
	}
	
	@Override
	public HandlerResult onItemUseEvent(QuestEnv env, Item item)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getQuestVarById(0) == 4))
		{
			return HandlerResult.fromBoolean(useQuestItem(env, item, 4, 4, true));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("PINNACLE_CATARACT_OUTPOST_600100000"))
		{
			final Player player = env.getPlayer();
			if (player == null)
			{
				return false;
			}
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			if ((qs != null) && (qs.getStatus() == QuestStatus.START))
			{
				final int var = qs.getQuestVarById(0);
				if (var == 1)
				{
					changeQuestStep(env, 1, 2, false);
					return true;
				}
			}
		}
		return false;
	}
}