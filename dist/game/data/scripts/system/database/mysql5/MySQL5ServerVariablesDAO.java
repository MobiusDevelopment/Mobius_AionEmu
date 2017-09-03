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

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.ServerVariablesDAO;

/**
 * @author Ben
 */
public class MySQL5ServerVariablesDAO extends ServerVariablesDAO
{
	
	private static Logger log = LoggerFactory.getLogger(ServerVariablesDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int load(String var)
	{
		final PreparedStatement ps = DB.prepareStatement("SELECT `value` FROM `server_variables` WHERE `key`=?");
		try
		{
			ps.setString(1, var);
			final ResultSet rs = ps.executeQuery();
			if (rs.next())
			{
				return Integer.parseInt(rs.getString("value"));
			}
		}
		catch (final SQLException e)
		{
			log.error("Error loading last saved server time", e);
		}
		finally
		{
			DB.close(ps);
		}
		
		return 0;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean store(String var, int time)
	{
		boolean success = false;
		final PreparedStatement ps = DB.prepareStatement("REPLACE INTO `server_variables` (`key`,`value`) VALUES (?,?)");
		try
		{
			ps.setString(1, var);
			ps.setString(2, String.valueOf(time));
			success = ps.executeUpdate() > 0;
		}
		catch (final SQLException e)
		{
			log.error("Error storing server time", e);
		}
		finally
		{
			DB.close(ps);
		}
		
		return success;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
