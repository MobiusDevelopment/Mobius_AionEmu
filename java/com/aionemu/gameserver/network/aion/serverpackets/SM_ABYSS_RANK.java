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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author Nemiroff Date: 25.01.2010
 * @author GiGatR00n v4.7.5.x
 */
public class SM_ABYSS_RANK extends AionServerPacket
{
	private final AbyssRank rank;
	private final int currentRankId;
	
	public SM_ABYSS_RANK(AbyssRank rank)
	{
		this.rank = rank;
		currentRankId = rank.getRank().getId();
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeQ(rank.getAp()); // Rank AP.
		writeD(rank.getGp()); // Rank GP.
		writeD(currentRankId); // Current Rank Ap/Gp.
		writeD(rank.getTopRanking()); // Top Ranking.
		if (currentRankId < 9)
		{
			final int nextRankId = currentRankId < AbyssRankEnum.values().length ? currentRankId + 1 : currentRankId;
			writeD((100 * rank.getAp()) / AbyssRankEnum.getRankById(nextRankId).getApRequired());
		}
		else if ((currentRankId >= 9) && (currentRankId <= 18))
		{
			final int nextRankId = currentRankId < AbyssRankEnum.values().length ? currentRankId + 1 : currentRankId;
			writeD((100 * rank.getGp()) / AbyssRankEnum.getRankById(nextRankId).getGpRequired());
		}
		writeD(rank.getAllKill()); // All Kill.
		writeD(rank.getMaxRank()); // Max Rank.
		writeD(rank.getDailyKill()); // Daily Kill.
		writeQ(rank.getDailyAP()); // Daily AP.
		writeD(rank.getDailyGP()); // Daily GP.
		writeD(rank.getWeeklyKill()); // Weekly Kill.
		writeQ(rank.getWeeklyAP()); // Weekly AP.
		writeD(rank.getWeeklyGP()); // Weekly GP.
		writeD(rank.getLastKill()); // Last Kill.
		writeQ(rank.getLastAP()); // Last AP.
		writeD(rank.getLastGP()); // Last GP.
		writeC(0x00);
	}
}