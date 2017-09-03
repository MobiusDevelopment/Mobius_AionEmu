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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.BaseDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;

public class MySQL5BaseDAO extends BaseDAO
{
	private static final Logger log = LoggerFactory.getLogger(MySQL5BaseDAO.class);
	
	public static final String SELECT_QUERY = "SELECT * FROM `base_location`";
	public static final String UPDATE_QUERY = "UPDATE `base_location` SET `race` = ? WHERE `id` = ?";
	public static final String INSERT_QUERY = "INSERT INTO `base_location` (`id`, `race`) VALUES(?, ?)";
	
	@Override
	public boolean loadBaseLocations(final Map<Integer, BaseLocation> locations)
	{
		boolean success = true;
		Connection con = null;
		final List<Integer> loaded = new ArrayList<>();
		PreparedStatement stmt = null;
		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(SELECT_QUERY);
			final ResultSet resultSet = stmt.executeQuery();
			while (resultSet.next())
			{
				final BaseLocation loc = locations.get(resultSet.getInt("id"));
				loc.setRace(Race.valueOf(resultSet.getString("race")));
				loaded.add(loc.getId());
			}
			resultSet.close();
		}
		catch (final Exception e)
		{
			log.warn("Error loading Siege informaiton from database: " + e.getMessage(), e);
			success = false;
		}
		finally
		{
			DatabaseFactory.close(stmt, con);
		}
		for (final Map.Entry<Integer, BaseLocation> entry : locations.entrySet())
		{
			final BaseLocation sLoc = entry.getValue();
			if (!loaded.contains(sLoc.getId()))
			{
				insertBaseLocation(sLoc);
			}
		}
		return success;
	}
	
	@Override
	public boolean updateBaseLocation(final BaseLocation locations)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_QUERY);
			stmt.setString(1, locations.getRace().toString());
			stmt.setInt(2, locations.getId());
			stmt.execute();
		}
		catch (final Exception e)
		{
			log.error("Error update Base Location: " + "id: " + locations.getId());
			return false;
		}
		finally
		{
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}
	
	private boolean insertBaseLocation(final BaseLocation locations)
	{
		Connection con = null;
		PreparedStatement stmt = null;
		try
		{
			con = DatabaseFactory.getConnection();
			stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, locations.getId());
			stmt.setString(2, Race.NPC.toString());
			stmt.execute();
		}
		catch (final Exception e)
		{
			log.error("Error insert Base Location: " + locations.getId(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(stmt, con);
		}
		return true;
	}
	
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}