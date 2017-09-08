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
package system.database.mysql5;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.AbyssRankingResult;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author ATracer, Divinity, nrg
 */
public class MySQL5AbyssRankDAO extends AbyssRankDAO
{
	private static final Logger log = LoggerFactory.getLogger(MySQL5AbyssRankDAO.class);
	public static final String SELECT_QUERY = "SELECT daily_ap, daily_gp, weekly_ap, weekly_gp, ap, gp, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_gp, last_update FROM abyss_rank WHERE player_id = ?";
	public static final String INSERT_QUERY = "INSERT INTO abyss_rank (player_id, daily_ap, daily_gp, weekly_ap, weekly_gp, ap, gp, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_gp, last_update) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	public static final String UPDATE_QUERY = "UPDATE abyss_rank SET  daily_ap = ?, daily_gp = ?, weekly_ap = ?, weekly_gp = ?, ap = ?, gp = ?, rank = ?, top_ranking = ?, daily_kill = ?, weekly_kill = ?, all_kill = ?, max_rank = ?, last_kill = ?, last_ap = ?, last_gp = ?, last_update = ? WHERE player_id = ?";
	public static final String SELECT_PLAYERS_RANKING = "SELECT abyss_rank.rank, abyss_rank.ap, abyss_rank.gp, abyss_rank.old_rank_pos, abyss_rank.rank_pos, players.name, legions.name, players.id, players.title_id, players.player_class, players.exp FROM abyss_rank INNER JOIN players ON abyss_rank.player_id = players.id LEFT JOIN legion_members ON legion_members.player_id = players.id LEFT JOIN legions ON legions.id = legion_members.legion_id WHERE players.race = ? AND abyss_rank.gp > 1243 ORDER BY abyss_rank.gp DESC LIMIT 0, 300";
	public static final String SELECT_LEGIONS_RANKING = "SELECT legions.id, legions.name, legions.contribution_points, legions.level as lvl, legions.old_rank_pos, legions.rank_pos FROM legions,legion_members,players WHERE players.race = ? AND legion_members.rank = 'BRIGADE_GENERAL' AND legion_members.player_id = players.id AND legion_members.legion_id = legions.id AND legions.contribution_points > 0 GROUP BY id ORDER BY legions.contribution_points DESC LIMIT 0, 50";
	public static final String SELECT_AP_PLAYER = "SELECT player_id, ap FROM abyss_rank, players WHERE abyss_rank.player_id = players.id AND players.race = ? AND ap > ? ORDER by ap DESC";
	public static final String SELECT_AP_PLAYER_ACTIVE_ONLY = "SELECT player_id, ap FROM abyss_rank, players WHERE abyss_rank.player_id = players.id AND players.race = ? AND ap > ? AND UNIX_TIMESTAMP(CURDATE())-UNIX_TIMESTAMP(players.last_online) <= ? * 24 * 60 * 60 ORDER BY ap DESC";
	public static final String SELECT_GP_PLAYER = "SELECT player_id, gp FROM abyss_rank, players WHERE abyss_rank.player_id = players.id AND players.race = ? AND gp > ? ORDER by gp DESC";
	public static final String SELECT_GP_PLAYER_ACTIVE_ONLY = "SELECT player_id, gp FROM abyss_rank, players WHERE abyss_rank.player_id = players.id AND players.race = ? AND gp > ? AND UNIX_TIMESTAMP(CURDATE())-UNIX_TIMESTAMP(players.last_online) <= ? * 24 * 60 * 60 ORDER BY gp DESC";
	public static final String UPDATE_RANK = "UPDATE abyss_rank SET  rank = ?, top_ranking = ? WHERE player_id = ?";
	public static final String SELECT_LEGION_COUNT = "SELECT COUNT(player_id) as players FROM legion_members WHERE legion_id = ?";
	public static final String UPDATE_PLAYER_RANK_LIST = "UPDATE abyss_rank SET abyss_rank.old_rank_pos = abyss_rank.rank_pos, abyss_rank.rank_pos = @a:=@a+1 where player_id in (SELECT id FROM players where race = ?) order by gp desc" + (GSConfig.ABYSSRANKING_SMALL_CACHE ? " limit 500" : "");
	public static final String UPDATE_LEGION_RANK_LIST = "UPDATE legions SET legions.old_rank_pos = legions.rank_pos, legions.rank_pos = @a:=@a+1 where id in (SELECT legion_id FROM legion_members, players where rank = 'BRIGADE_GENERAL' AND players.id = legion_members.player_id and players.race = ?) order by legions.contribution_points DESC" + (GSConfig.ABYSSRANKING_SMALL_CACHE ? " limit 75" : "");
	public static final String DELETE_QUERY = "DELETE FROM `abyss_rank` WHERE player_id=?";
	
	@Override
	public AbyssRank loadAbyssRank(int playerId)
	{
		AbyssRank abyssRank = null;
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			final ResultSet resultSet = stmt.executeQuery();
			if (resultSet.next())
			{
				final int daily_ap = resultSet.getInt("daily_ap");
				final int daily_gp = resultSet.getInt("daily_gp");
				final int weekly_ap = resultSet.getInt("weekly_ap");
				final int weekly_gp = resultSet.getInt("weekly_gp");
				final int ap = resultSet.getInt("ap");
				final int gp = resultSet.getInt("gp");
				final int rank = resultSet.getInt("rank");
				final int top_ranking = resultSet.getInt("top_ranking");
				final int daily_kill = resultSet.getInt("daily_kill");
				final int weekly_kill = resultSet.getInt("weekly_kill");
				final int all_kill = resultSet.getInt("all_kill");
				final int max_rank = resultSet.getInt("max_rank");
				final int last_kill = resultSet.getInt("last_kill");
				final int last_ap = resultSet.getInt("last_ap");
				final int last_gp = resultSet.getInt("last_gp");
				final long last_update = resultSet.getLong("last_update");
				abyssRank = new AbyssRank(daily_ap, daily_gp, weekly_ap, weekly_gp, ap, gp, rank, top_ranking, daily_kill, weekly_kill, all_kill, max_rank, last_kill, last_ap, last_gp, last_update);
				abyssRank.setPersistentState(PersistentState.UPDATED);
			}
			else
			{
				abyssRank = new AbyssRank(0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 1, 0, 0, 0, System.currentTimeMillis());
				abyssRank.setPersistentState(PersistentState.NEW);
			}
			resultSet.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error("loadAbyssRank", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return abyssRank;
	}
	
	@Override
	public void loadAbyssRank(Player player)
	{
		final AbyssRank rank = loadAbyssRank(player.getObjectId());
		player.setAbyssRank(rank);
	}
	
	@Override
	public boolean storeAbyssRank(Player player)
	{
		final AbyssRank rank = player.getAbyssRank();
		boolean result = false;
		switch (rank.getPersistentState())
		{
			case NEW:
			{
				result = addRank(player.getObjectId(), rank);
				break;
			}
			case UPDATE_REQUIRED:
			{
				result = updateRank(player.getObjectId(), rank);
				break;
			}
		}
		rank.setPersistentState(PersistentState.UPDATED);
		return result;
	}
	
	private boolean addRank(int objectId, AbyssRank rank)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, objectId);
			stmt.setInt(2, rank.getDailyAP());
			stmt.setInt(3, rank.getDailyGP());
			stmt.setInt(4, rank.getWeeklyAP());
			stmt.setInt(5, rank.getWeeklyGP());
			stmt.setInt(6, rank.getAp());
			stmt.setInt(7, rank.getGp());
			stmt.setInt(8, rank.getRank().getId());
			stmt.setInt(9, rank.getTopRanking());
			stmt.setInt(10, rank.getDailyKill());
			stmt.setInt(11, rank.getWeeklyKill());
			stmt.setInt(12, rank.getAllKill());
			stmt.setInt(13, rank.getMaxRank());
			stmt.setInt(14, rank.getLastKill());
			stmt.setInt(15, rank.getLastAP());
			stmt.setInt(16, rank.getLastGP());
			stmt.setLong(17, rank.getLastUpdate());
			stmt.execute();
			stmt.close();
			return true;
		}
		catch (SQLException e)
		{
			log.error("addRank", e);
			
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	private boolean updateRank(int objectId, AbyssRank rank)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setInt(1, rank.getDailyAP());
			stmt.setInt(2, rank.getDailyGP());
			stmt.setInt(3, rank.getWeeklyAP());
			stmt.setInt(4, rank.getWeeklyGP());
			stmt.setInt(5, rank.getAp());
			stmt.setInt(6, rank.getGp());
			stmt.setInt(7, rank.getRank().getId());
			stmt.setInt(8, rank.getTopRanking());
			stmt.setInt(9, rank.getDailyKill());
			stmt.setInt(10, rank.getWeeklyKill());
			stmt.setInt(11, rank.getAllKill());
			stmt.setInt(12, rank.getMaxRank());
			stmt.setInt(13, rank.getLastKill());
			stmt.setInt(14, rank.getLastAP());
			stmt.setInt(15, rank.getLastGP());
			stmt.setLong(16, rank.getLastUpdate());
			stmt.setInt(17, objectId);
			stmt.execute();
			stmt.close();
			return true;
		}
		catch (SQLException e)
		{
			log.error("updateRank", e);
			
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public ArrayList<AbyssRankingResult> getAbyssRankingPlayers(Race race)
	{
		Connection con = null;
		final ArrayList<AbyssRankingResult> results = new ArrayList<>();
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(SELECT_PLAYERS_RANKING);
			stmt.setString(1, race.toString());
			final ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				final String name = resultSet.getString("players.name");
				final int playerAbyssRank = resultSet.getInt("abyss_rank.rank");
				final int ap = resultSet.getInt("abyss_rank.ap");
				final int gp = resultSet.getInt("abyss_rank.gp");
				final int playerTitle = resultSet.getInt("players.title_id");
				final int playerId = resultSet.getInt("players.id");
				final String playerClassStr = resultSet.getString("players.player_class");
				final int playerLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getLevelForExp(resultSet.getLong("players.exp"));
				final String playerLegion = resultSet.getString("legions.name");
				final int oldRankPos = resultSet.getInt("old_rank_pos");
				final int rankPos = resultSet.getInt("rank_pos");
				final PlayerClass playerClass = PlayerClass.getPlayerClassByString(playerClassStr);
				if (playerClass == null)
				{
					continue;
				}
				final AbyssRankingResult rsl = new AbyssRankingResult(name, playerAbyssRank, playerId, ap, gp, playerTitle, playerClass, playerLevel, playerLegion, oldRankPos, rankPos);
				results.add(rsl);
			}
			resultSet.close();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error("getAbyssRankingPlayers", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return results;
	}
	
	@Override
	public ArrayList<AbyssRankingResult> getAbyssRankingLegions(Race race)
	{
		final ArrayList<AbyssRankingResult> results = new ArrayList<>();
		DB.select(SELECT_LEGIONS_RANKING, new ParamReadStH()
		{
			@Override
			public void handleRead(ResultSet arg0) throws SQLException
			{
				while (arg0.next())
				{
					final String name = arg0.getString("legions.name");
					final int cp = arg0.getInt("legions.contribution_points");
					final int legionId = arg0.getInt("legions.id");
					final int legionLevel = arg0.getInt("lvl");
					final int legionMembers = getLegionMembersCount(legionId);
					final int oldRankPos = arg0.getInt("old_rank_pos");
					final int rankPos = arg0.getInt("rank_pos");
					final AbyssRankingResult rsl = new AbyssRankingResult(cp, name, legionId, legionLevel, legionMembers, oldRankPos, rankPos);
					results.add(rsl);
				}
			}
			
			@Override
			public void setParams(PreparedStatement arg0) throws SQLException
			{
				arg0.setString(1, race.toString());
			}
		});
		return results;
	}
	
	int getLegionMembersCount(int legionId)
	{
		final int[] result = new int[1];
		DB.select(SELECT_LEGION_COUNT, new ParamReadStH()
		{
			@Override
			public void handleRead(ResultSet arg0) throws SQLException
			{
				while (arg0.next())
				{
					result[0] += arg0.getInt("players");
				}
			}
			
			@Override
			public void setParams(PreparedStatement arg0) throws SQLException
			{
				arg0.setInt(1, legionId);
			}
		});
		return result[0];
	}
	
	@Override
	public Map<Integer, Integer> loadPlayersAp(Race race, int lowerApLimit, int maxOfflineDays)
	{
		final Map<Integer, Integer> results = new HashMap<>();
		DB.select(maxOfflineDays > 0 ? SELECT_AP_PLAYER_ACTIVE_ONLY : SELECT_AP_PLAYER, new ParamReadStH()
		{
			@Override
			public void handleRead(ResultSet rs) throws SQLException
			{
				while (rs.next())
				{
					final int playerId = rs.getInt("player_id");
					final int ap = rs.getInt("ap");
					results.put(playerId, ap);
				}
			}
			
			@Override
			public void setParams(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, race.toString());
				ps.setInt(2, lowerApLimit);
				
				if (maxOfflineDays > 0)
				{
					ps.setInt(3, maxOfflineDays);
				}
			}
		});
		return results;
	}
	
	@Override
	public Map<Integer, Integer> loadPlayersGp(Race race, int lowerGpLimit, int maxOfflineDays)
	{
		final Map<Integer, Integer> results = new HashMap<>();
		DB.select(maxOfflineDays > 0 ? SELECT_GP_PLAYER_ACTIVE_ONLY : SELECT_GP_PLAYER, new ParamReadStH()
		{
			@Override
			public void handleRead(ResultSet rs) throws SQLException
			{
				while (rs.next())
				{
					final int playerId = rs.getInt("player_id");
					final int gp = rs.getInt("gp");
					results.put(playerId, gp);
				}
			}
			
			@Override
			public void setParams(PreparedStatement ps) throws SQLException
			{
				ps.setString(1, race.toString());
				ps.setInt(2, lowerGpLimit);
				
				if (maxOfflineDays > 0)
				{
					ps.setInt(3, maxOfflineDays);
				}
			}
		});
		return results;
	}
	
	@Override
	public void updateAbyssRank(int playerId, AbyssRankEnum rankEnum)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(UPDATE_RANK);
			stmt.setInt(1, rankEnum.getId());
			stmt.setInt(2, rankEnum.getQuota());
			stmt.setInt(3, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (SQLException e)
		{
			log.error("updateAbyss/GloryRank", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
	
	@Override
	public void updateRankList()
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			PreparedStatement stmt = con.prepareStatement(UPDATE_PLAYER_RANK_LIST);
			stmt = con.prepareStatement(UPDATE_PLAYER_RANK_LIST);
			stmt.addBatch("SET @a:=0;");
			stmt.setString(1, "ELYOS");
			stmt.addBatch();
			stmt.addBatch("SET @a:=0;");
			stmt.setString(1, "ASMODIANS");
			stmt.addBatch();
			stmt.executeBatch();
			stmt.close();
			stmt = con.prepareStatement(UPDATE_LEGION_RANK_LIST);
			stmt.addBatch("SET @a:=0;");
			stmt.setString(1, "ELYOS");
			stmt.addBatch();
			stmt.addBatch("SET @a:=0;");
			stmt.setString(1, "ASMODIANS");
			stmt.addBatch();
			stmt.executeBatch();
		}
		catch (SQLException e)
		{
			log.error("updateRank", e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public void removePlayer(List<Player> listP)
	{
		PreparedStatement stmt = null;
		Connection con = null;
		try
		{
			for (Player player : listP)
			{
				con = DatabaseFactory.getConnection();
				stmt = con.prepareStatement(DELETE_QUERY);
				stmt.setInt(1, player.getObjectId());
				stmt.addBatch();
			}
			stmt.executeBatch();
			con.commit();
		}
		catch (Exception e)
		{
			log.error("Error while Deleting player From Abyss Rank", e);
		}
		finally
		{
			DatabaseFactory.close(stmt);
		}
	}
}