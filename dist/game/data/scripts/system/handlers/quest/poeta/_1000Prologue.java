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
package system.handlers.quest.poeta;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author MrPoke
 */
public class _1000Prologue extends QuestHandler
{
	private static final int questId = 1000;
	
	public _1000Prologue()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerOnMovieEndQuest(1, questId);
		qe.registerOnEnterZone(ZoneName.get("AKARIOS_PLAINS_210010000"), questId);
	}
	
	@Override
	public boolean onEnterZoneEvent(QuestEnv env, ZoneName zoneName)
	{
		if (zoneName == ZoneName.get("AKARIOS_PLAINS_210010000"))
		{
			final Player player = env.getPlayer();
			if (player.getCommonData().getRace() != Race.ELYOS)
			{
				return false;
			}
			QuestState qs = player.getQuestStateList().getQuestState(questId);
			if (qs == null)
			{
				env.setQuestId(questId);
				QuestService.startQuest(env);
			}
			qs = player.getQuestStateList().getQuestState(questId);
			if (qs.getStatus() == QuestStatus.START)
			{
				PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(1, 1));
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean onMovieEndEvent(QuestEnv env, int movieId)
	{
		if (movieId != 1)
		{
			return false;
		}
		final Player player = env.getPlayer();
		if (player.getCommonData().getRace() != Race.ELYOS)
		{
			return false;
		}
		final QuestState qs = player.getQuestStateList().getQuestState(questId);
		if ((qs == null) || (qs.getStatus() != QuestStatus.START))
		{
			return false;
		}
		qs.setStatus(QuestStatus.REWARD);
		QuestService.finishQuest(env);
		return true;
	}
}