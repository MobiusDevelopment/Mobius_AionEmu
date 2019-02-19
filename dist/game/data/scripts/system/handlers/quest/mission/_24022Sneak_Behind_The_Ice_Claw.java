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
package system.handlers.quest.mission;

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
public class _24022Sneak_Behind_The_Ice_Claw extends QuestHandler
{
	
	private static final int questId = 24022;
	
	public _24022Sneak_Behind_The_Ice_Claw()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		final int[] npcs =
		{
			204329,
			204335,
			204332,
			700246,
			204301,
			802047
		};
		qe.registerOnLevelUp(questId);
		qe.registerOnEnterZoneMissionEnd(questId);
		qe.registerQuestNpc(204417).addOnKillEvent(questId);
		qe.registerQuestNpc(212877).addOnKillEvent(questId);
		qe.registerQuestItem(182215364, questId); // Hard Flint.
		for (int npc : npcs)
		{
			qe.registerQuestNpc(npc).addOnTalkEvent(questId);
		}
	}
	
	@Override
	public boolean onZoneMissionEndEvent(QuestEnv env)
	{
		return defaultOnZoneMissionEndEvent(env);
	}
	
	@Override
	public boolean onLvlUpEvent(QuestEnv env)
	{
		return defaultOnLvlUpEvent(env, 24021, true);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		if (qs == null)
		{
			return false;
		}
		final int var = qs.getQuestVarById(0);
		final int targetId = env.getTargetId();
		if (qs.getStatus() == QuestStatus.START)
		{
			switch (targetId)
			{
				case 204329:
				{ // Tofa.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 0)
							{
								return sendQuestDialog(env, 1011);
							}
							else if (var == 7)
							{
								return sendQuestDialog(env, 3399);
							}
						}
						case STEP_TO_1:
						{
							return defaultCloseDialog(env, 0, 1);
						}
						case SET_REWARD:
						{
							return defaultCloseDialog(env, 7, 7, true, false);
						}
					}
					break;
				}
				case 204335:
				{ // Aprily.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 1)
							{
								return sendQuestDialog(env, 1352);
							}
						}
						case STEP_TO_2:
						{
							return defaultCloseDialog(env, 1, 2);
						}
					}
					break;
				}
				case 204332:
				{ // Jorund.
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
							if (var == 2)
							{
								return defaultCloseDialog(env, 2, 3, 182215364, 1, 0, 0);
							}
						}
					}
					break;
				}
				case 700246:
				{ // Dead Fire.
					if (dialog == QuestDialog.USE_OBJECT)
					{
						if (var == 3)
						{
							if (player.getInventory().getItemCountByItemId(182215365) > 0)
							{
								final Npc npc = (Npc) env.getVisibleObject();
								QuestService.addNewSpawn(220020000, player.getInstanceId(), 204417, npc.getX(), npc.getY(), npc.getZ(), (byte) 0);
								removeQuestItem(env, 182215365, 1);
								return defaultCloseDialog(env, 3, 4);
							}
						}
					}
				}
				case 802047:
				{ // Landver.
					switch (dialog)
					{
						case START_DIALOG:
						{
							if (var == 5)
							{
								return sendQuestDialog(env, 2716);
							}
						}
						case STEP_TO_6:
						{
							player.getTitleList().addTitle(58, true, 0);
							return defaultCloseDialog(env, 5, 6);
						}
					}
					break;
				}
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 204301)
			{ // Aegir
				if (dialog == QuestDialog.USE_OBJECT)
				{
					removeQuestItem(env, 182215364, 1);
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
		if (player.isInsideZone(ZoneName.get("ALTAR_OF_TRIAL_220020000")))
		{
			return HandlerResult.fromBoolean(useQuestItem(env, item, 3, 4, false));
		}
		return HandlerResult.FAILED;
	}
	
	@Override
	public boolean onKillEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int targetId = env.getTargetId();
			switch (targetId)
			{
				case 204417:
				{
					return defaultOnKillEvent(env, 204417, 4, 5);
				}
				case 212877:
				{
					return defaultOnKillEvent(env, 212877, 6, 7);
				}
			}
		}
		return false;
	}
}