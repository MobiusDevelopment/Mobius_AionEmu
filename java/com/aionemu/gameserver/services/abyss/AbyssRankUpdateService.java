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
package com.aionemu.gameserver.services.abyss;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimerTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;
import com.aionemu.gameserver.world.World;

/**
 * @author ATracer
 * @author ThunderBolt - GloryPoints
 */
public class AbyssRankUpdateService
{
	private static final Logger log = LoggerFactory.getLogger(AbyssRankUpdateService.class);
	
	AbyssRankUpdateService()
	{
	}
	
	public static AbyssRankUpdateService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public void scheduleUpdateHour()
	{
		final ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		final int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < (System.currentTimeMillis() / 1000))
		{
			performUpdate();
		}
		log.info("Start <Abyss Ranking> update");
		CronService.getInstance().schedule(() -> performUpdate(), RankingConfig.TOP_RANKING_UPDATE_RULE, true);
	}
	
	public void scheduleUpdateMinute()
	{
		final ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		final int nextTime = dao.load("abyssRankUpdate");
		if (nextTime < (System.currentTimeMillis() / 1000))
		{
			performUpdate();
		}
		log.info("Start <Abyss Ranking> update");
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new TimerTask()
		{
			@Override
			public void run()
			{
				performUpdate();
			}
		}, 0, RankingConfig.TOP_RANKING_UPDATE_RULE2 * 60 * 1000);
	}
	
	/**
	 * Perform update of all <Rank>
	 */
	public void performUpdate()
	{
		log.info("Abyss Rank: executing rank update");
		final long startTime = System.currentTimeMillis();
		World.getInstance().doOnAllPlayers(player ->
		{
			AbyssPointsService.AbyssRankCheck(player);
			player.getAbyssRank().doUpdate();
			DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
		});
		updateLimitedGpRanks();
		AbyssRankingCacheUpdate();
		log.info("AbyssRankUpdate: execution time: " + ((System.currentTimeMillis() - startTime) / 1000));
	}
	
	public void AbyssRankingCacheUpdate()
	{
		ThreadPoolManager.getInstance().schedule(new TimerTask()
		{
			@Override
			public void run()
			{
				AbyssRankingCache.getInstance().reloadRankings();
			}
		}, 3 * 1000);
	}
	
	private void updateLimitedGpRanks()
	{
		updateAllRanksGpForRace(Race.ASMODIANS, AbyssRankEnum.STAR1_OFFICER.getGpRequired(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
		updateAllRanksGpForRace(Race.ELYOS, AbyssRankEnum.STAR1_OFFICER.getGpRequired(), RankingConfig.TOP_RANKING_MAX_OFFLINE_DAYS);
	}
	
	private void updateAllRanksGpForRace(Race race, int gpLimit, int activeAfterDays)
	{
		final Map<Integer, Integer> playerGpMap = DAOManager.getDAO(AbyssRankDAO.class).loadPlayersGp(race, gpLimit, activeAfterDays);
		final List<Entry<Integer, Integer>> playerGpEntries = new ArrayList<>(playerGpMap.entrySet());
		Collections.sort(playerGpEntries, new PlayerGpComparator<Integer, Integer>());
		selectGpRank(AbyssRankEnum.SUPREME_COMMANDER, playerGpEntries);
		selectGpRank(AbyssRankEnum.COMMANDER, playerGpEntries);
		selectGpRank(AbyssRankEnum.GREAT_GENERAL, playerGpEntries);
		selectGpRank(AbyssRankEnum.GENERAL, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR5_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR4_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR3_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR2_OFFICER, playerGpEntries);
		selectGpRank(AbyssRankEnum.STAR1_OFFICER, playerGpEntries);
		updateToNoQuotaGpRank(playerGpEntries);
	}
	
	private void selectGpRank(AbyssRankEnum rank, List<Entry<Integer, Integer>> playerGpEntries)
	{
		final int quota = ((rank.getId() > 9) && (rank.getId() < 18)) ? rank.getQuota() - AbyssRankEnum.getRankById(rank.getId() + 1).getQuota() : rank.getQuota();
		for (int i = 0; i < quota; i++)
		{
			if (playerGpEntries.isEmpty())
			{
				return;
			}
			// check next player in list
			final Entry<Integer, Integer> playerGp = playerGpEntries.get(0);
			// check if there are some players left in map
			if (playerGp == null)
			{
				return;
			}
			final int playerId = playerGp.getKey();
			final int gp = playerGp.getValue();
			// check if this (and the rest) player has required gp count
			if (gp < rank.getGpRequired())
			{
				return;
			}
			// remove player and update its rankGp
			playerGpEntries.remove(0);
			updateGpRankTo(rank, playerId);
		}
	}
	
	private void updateToNoQuotaGpRank(List<Entry<Integer, Integer>> playerGpEntries)
	{
		for (Entry<Integer, Integer> playerGpEntry : playerGpEntries)
		{
			updateGpRankTo(AbyssRankEnum.SUPREME_COMMANDER, playerGpEntry.getKey());
		}
	}
	
	protected void updateRankTo(AbyssRankEnum newRank, int playerId)
	{
		// check if rank is changed for online players
		final Player onlinePlayer = World.getInstance().findPlayer(playerId);
		if (onlinePlayer != null)
		{
			final AbyssRank abyssRank = onlinePlayer.getAbyssRank();
			final AbyssRankEnum currentRank = abyssRank.getRank();
			if (currentRank != newRank)
			{
				abyssRank.setRank(newRank);
				AbyssPointsService.checkRankChanged(onlinePlayer, currentRank, newRank);
			}
		}
		else
		{
			DAOManager.getDAO(AbyssRankDAO.class).updateAbyssRank(playerId, newRank);
		}
	}
	
	protected void updateGpRankTo(AbyssRankEnum newRank, int playerId)
	{
		// check if rankGp is changed for online players
		final Player onlinePlayer = World.getInstance().findPlayer(playerId);
		if (onlinePlayer != null)
		{
			final AbyssRank abyssRank = onlinePlayer.getAbyssRank();
			final AbyssRankEnum currentRank = abyssRank.getRank();
			if (currentRank != newRank)
			{
				abyssRank.setRank(newRank);
				AbyssPointsService.checkRankGpChanged(onlinePlayer, currentRank, newRank);
			}
		}
		else
		{
			DAOManager.getDAO(AbyssRankDAO.class).updateAbyssRank(playerId, newRank);
		}
	}
	
	private static class SingletonHolder
	{
		protected static final AbyssRankUpdateService instance = new AbyssRankUpdateService();
	}
	
	private static class PlayerGpComparator<K, V extends Comparable<V>> implements Comparator<Entry<K, V>>
	{
		public PlayerGpComparator()
		{
		}
		
		@Override
		public int compare(Entry<K, V> o1, Entry<K, V> o2)
		{
			return -o1.getValue().compareTo(o2.getValue()); // descending order
		}
	}
}