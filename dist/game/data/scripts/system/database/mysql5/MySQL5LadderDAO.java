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
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.LadderDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * Created by wanke on 12/02/2017.
 */

public class MySQL5LadderDAO extends LadderDAO
{
	@Override
	public void addWin(Player player)
	{
		addPlayerLadderData(player, "wins", 1);
	}
	
	@Override
	public void addLoss(Player player)
	{
		addPlayerLadderData(player, "losses", 1);
	}
	
	@Override
	public void addLeave(Player player)
	{
		addPlayerLadderData(player, "leaves", 1);
	}
	
	@Override
	public void addRating(Player player, int rating)
	{
		addPlayerLadderData(player, "rating", rating);
	}
	
	@Override
	public void setWins(Player player, int wins)
	{
		setPlayerLadderData(player, "wins", wins);
	}
	
	@Override
	public void setLosses(Player player, int losses)
	{
		setPlayerLadderData(player, "losses", losses);
	}
	
	@Override
	public void setLeaves(Player player, int leaves)
	{
		setPlayerLadderData(player, "leaves", leaves);
	}
	
	@Override
	public void setRating(Player player, int rating)
	{
		setPlayerLadderData(player, "rating", rating);
	}
	
	@Override
	public int getWins(Player player)
	{
		return getPlayerLadderData(player, "wins");
	}
	
	@Override
	public int getLosses(Player player)
	{
		return getPlayerLadderData(player, "losses");
	}
	
	@Override
	public int getLeaves(Player player)
	{
		return getPlayerLadderData(player, "leaves");
	}
	
	@Override
	public int getRating(Player player)
	{
		return getPlayerLadderData(player, "rating");
	}
	
	@Override
	public void updateRanks()
	{
		Connection con = null;
		final List<PlayerInfo> players = new ArrayList<>();
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT player_id, last_update, rating, wins, rank FROM ladder_player WHERE wins > 0 OR losses > 0 OR leaves > 0 ORDER BY rating, wins DESC");
			final ResultSet rset = stmt.executeQuery();
			while (rset.next())
			{
				final PlayerInfo plInfo = new PlayerInfo(rset.getInt("player_id"), rset.getInt("rating"), rset.getTimestamp("last_update"), rset.getInt("wins"), rset.getInt("rank"));
				players.add(plInfo);
			}
			rset.close();
			stmt.close();
		}
		catch (final SQLException e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		Collections.sort(players, new Comparator<PlayerInfo>()
		{
			@Override
			public int compare(PlayerInfo o1, PlayerInfo o2)
			{
				int result = Integer.valueOf(o1.getRating()).compareTo(Integer.valueOf(o2.getRating()));
				if (result != 0)
				{
					return -result;
				}
				result = Integer.valueOf(o1.getWins()).compareTo(Integer.valueOf(o2.getWins()));
				if (result != 0)
				{
					return -result;
				}
				result = Integer.valueOf(o1.getPlayerId()).compareTo(Integer.valueOf(o2.getPlayerId()));
				return result;
			}
		});
		if (players.size() > 0)
		{
			int i = 1;
			try
			{
				con = DatabaseFactory.getConnection();
				final PreparedStatement stmtRank = con.prepareStatement("UPDATE ladder_player SET rank = ? WHERE player_id = ?");
				final PreparedStatement stmtLast = con.prepareStatement("UPDATE ladder_player SET last_rank = ?, last_update = ? WHERE player_id = ?");
				for (final PlayerInfo plInfo : players)
				{
					final int playerId = plInfo.getPlayerId();
					final Timestamp update = plInfo.getLastUpdate();
					if ((update == null) || update.equals(new Timestamp(0)) || ((update.getTime() + (24 * 60 * 60 * 1000)) < System.currentTimeMillis()))
					{
						stmtLast.setInt(1, plInfo.getRank());
						stmtLast.setTimestamp(2, new Timestamp(System.currentTimeMillis()));
						stmtLast.setInt(3, playerId);
						stmtLast.addBatch();
					}
					stmtRank.setInt(1, i);
					stmtRank.setInt(2, playerId);
					stmtRank.addBatch();
					i++;
				}
				stmtRank.executeBatch();
				stmtLast.executeBatch();
				stmtRank.close();
				stmtLast.close();
			}
			catch (final SQLException e)
			{
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
	}
	
	@Override
	public int getRank(Player player)
	{
		return getPlayerLadderData(player, "rank");
	}
	
	public void addPlayerLadderData(Player player, String data, int value)
	{
		Connection con = null;
		if (checkExists(player))
		{
			try
			{
				con = DatabaseFactory.getConnection();
				final PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = " + data + " + ? WHERE player_id = ?");
				stmt.setInt(1, value);
				stmt.setInt(2, player.getObjectId());
				stmt.execute();
			}
			catch (final SQLException e)
			{
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
		else
		{
			try
			{
				con = DatabaseFactory.getConnection();
				final PreparedStatement stmt = con.prepareStatement("INSERT INTO ladder_player (player_id, " + data + ") VALUES (?, ?)");
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, "rating".equals(data) ? 1000 + value : value);
				stmt.execute();
			}
			catch (final SQLException e)
			{
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
	}
	
	public void setPlayerLadderData(Player player, String data, int value)
	{
		Connection con = null;
		if (checkExists(player))
		{
			try
			{
				con = DatabaseFactory.getConnection();
				final PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = ? WHERE player_id = ?");
				stmt.setInt(1, value);
				stmt.setInt(2, player.getObjectId());
				stmt.execute();
			}
			catch (final SQLException e)
			{
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
		else
		{
			try
			{
				con = DatabaseFactory.getConnection();
				final PreparedStatement stmt = con.prepareStatement("INSERT INTO ladder_player (player_id, " + data + ") VALUES (?, ?)");
				stmt.setInt(1, player.getObjectId());
				stmt.setInt(2, value);
				stmt.execute();
			}
			catch (final SQLException e)
			{
			}
			finally
			{
				DatabaseFactory.close(con);
			}
		}
	}
	
	public void setPlayerLadderData(Integer playerId, String data, int value)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET " + data + " = ? WHERE player_id = ?");
			stmt.setInt(1, value);
			stmt.setInt(2, playerId);
			stmt.execute();
		}
		catch (final SQLException e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	public int getPlayerLadderData(Player player, String data)
	{
		Connection con = null;
		int value = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT " + data + " FROM ladder_player WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			final ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				value = rset.getInt(data);
			}
			rset.close();
			stmt.close();
		}
		catch (final Exception e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		if (data.equals("rating") && (value == 0))
		{
			return 1000;
		}
		return value;
	}
	
	public int getPlayerLadderData(Integer playerId, String data)
	{
		Connection con = null;
		int value = 0;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT " + data + " FROM ladder_player WHERE player_id = ?");
			stmt.setInt(1, playerId);
			final ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				value = rset.getInt(data);
			}
			rset.close();
			stmt.close();
		}
		catch (final Exception e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		if (data.equals("rating") && (value == 0))
		{
			return 1000;
		}
		return value;
	}
	
	public Timestamp getPlayerLadderUpdate(Player player)
	{
		Connection con = null;
		Timestamp value = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT last_update FROM ladder_player WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			final ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				value = rset.getTimestamp("last_update");
			}
			rset.close();
			stmt.close();
		}
		catch (final Exception e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return value;
	}
	
	public void setPlayerLadderUpdate(Player player, Timestamp value)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET last_update = ? WHERE player_id = ?");
			stmt.setTimestamp(1, value);
			stmt.setInt(2, player.getObjectId());
			stmt.execute();
		}
		catch (final SQLException e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	public void setPlayerLadderUpdate(Integer playerId, Timestamp value)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("UPDATE ladder_player SET last_update = ? WHERE player_id = ?");
			stmt.setTimestamp(1, value);
			stmt.setInt(2, playerId);
			stmt.execute();
		}
		catch (final SQLException e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
	}
	
	@Override
	public PlayerLadderData getPlayerLadderData(Player player)
	{
		Connection con = null;
		PlayerLadderData data = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT * FROM ladder_player WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			final ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				data = new PlayerLadderData(player, rset.getInt("rating"), rset.getInt("rank"), rset.getInt("wins"), rset.getInt("losses"), rset.getInt("leaves"), rset.getTimestamp("last_update"));
			}
			rset.close();
			stmt.close();
		}
		catch (final Exception e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		if (data == null)
		{
			data = new PlayerLadderData(player, 1000, 0, 0, 0, 0, new Timestamp(0));
		}
		return data;
	}
	
	private boolean checkExists(Player player)
	{
		Connection con = null;
		boolean exists = false;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement("SELECT rating FROM ladder_player WHERE player_id = ?");
			stmt.setInt(1, player.getObjectId());
			final ResultSet rset = stmt.executeQuery();
			if (rset.next())
			{
				exists = true;
			}
			rset.close();
			stmt.close();
		}
		catch (final Exception e)
		{
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return exists;
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
	
	private class PlayerInfo
	{
		private final int playerId;
		private final int rating;
		private final Timestamp lastUpdate;
		private final int wins;
		private final int rank;
		
		public PlayerInfo(int playerId, int rating, Timestamp lastUpdate, int wins, int rank)
		{
			this.playerId = playerId;
			this.rating = rating;
			this.lastUpdate = lastUpdate;
			this.wins = wins;
			this.rank = rank;
		}
		
		public int getPlayerId()
		{
			return playerId;
		}
		
		public int getRating()
		{
			return rating;
		}
		
		public Timestamp getLastUpdate()
		{
			return lastUpdate;
		}
		
		public int getWins()
		{
			return wins;
		}
		
		public int getRank()
		{
			return rank;
		}
	}
}