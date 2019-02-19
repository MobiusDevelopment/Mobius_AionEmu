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
package system.handlers.quest.theobomos;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestDialog;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author Cheatkiller
 */
public class _3057CursedZirius extends QuestHandler
{
	
	private static final int questId = 3057;
	
	public _3057CursedZirius()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(798213).addOnQuestStart(questId);
		qe.registerQuestNpc(798213).addOnTalkEvent(questId);
		qe.registerQuestNpc(214576).addOnAttackEvent(questId);
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		final QuestDialog dialog = env.getDialog();
		final int targetId = env.getTargetId();
		
		if ((qs == null) || (qs.getStatus() == QuestStatus.NONE))
		{
			if (targetId == 798213)
			{
				if (dialog == QuestDialog.START_DIALOG)
				{
					return sendQuestDialog(env, 4762);
				}
				return sendQuestStartDialog(env);
			}
		}
		else if (qs.getStatus() == QuestStatus.REWARD)
		{
			if (targetId == 798213)
			{
				switch (dialog)
				{
					case USE_OBJECT:
					{
						return sendQuestDialog(env, 10002);
					}
					default:
					{
						return sendQuestEndDialog(env);
					}
				}
			}
		}
		return false;
	}
	
	@Override
	public boolean onAttackEvent(QuestEnv env)
	{
		final Player player = env.getPlayer();
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs != null) && (qs.getStatus() == QuestStatus.START))
		{
			final int targetId = env.getTargetId();
			if (targetId == 214576)
			{
				if (MathUtil.getDistance(env.getVisibleObject(), 1691.41f, 219.09f, 72.62f) <= 30)
				{
					((Npc) env.getVisibleObject()).getController().onDie(player);
					changeQuestStep(env, 0, 1, true);
					return true;
				}
			}
		}
		return false;
	}
}
