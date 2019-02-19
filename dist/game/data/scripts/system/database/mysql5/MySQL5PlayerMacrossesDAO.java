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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerMacrossesDAO;
import com.aionemu.gameserver.model.gameobjects.player.MacroList;

/**
 * @author Aquanox
 */
public class MySQL5PlayerMacrossesDAO extends PlayerMacrossesDAO
{
	
	static Logger log = LoggerFactory.getLogger(MySQL5PlayerMacrossesDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_macrosses` (`player_id`, `order`, `macro`) VALUES (?,?,?)";
	public static final String UPDATE_QUERY = "UPDATE `player_macrosses` SET `macro`=? WHERE `player_id`=? AND `order`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_macrosses` WHERE `player_id`=? AND `order`=?";
	public static final String SELECT_QUERY = "SELECT `order`, `macro` FROM `player_macrosses` WHERE `player_id`=?";
	
	/**
	 * Add a macro information into database
	 * @param playerId player object id
	 * @param macro macro contents.
	 */
	@Override
	public void addMacro(int playerId, int macroPosition, String macro)
	{
		DB.insertUpdate(INSERT_QUERY, stmt ->
		{
			log.debug("[DAO: MySQL5PlayerMacrossesDAO] storing macro " + playerId + " " + macroPosition);
			stmt.setInt(1, playerId);
			stmt.setInt(2, macroPosition);
			stmt.setString(3, macro);
			stmt.execute();
		});
	}
	
	@Override
	public void updateMacro(int playerId, int macroPosition, String macro)
	{
		DB.insertUpdate(UPDATE_QUERY, stmt ->
		{
			log.debug("[DAO: MySQL5PlayerMacrossesDAO] updating macro " + playerId + " " + macroPosition);
			stmt.setString(1, macro);
			stmt.setInt(2, playerId);
			stmt.setInt(3, macroPosition);
			stmt.execute();
		});
	}
	
	/** {@inheritDoc} */
	@Override
	public void deleteMacro(int playerId, int macroPosition)
	{
		DB.insertUpdate(DELETE_QUERY, stmt ->
		{
			log.debug("[DAO: MySQL5PlayerMacrossesDAO] removing macro " + playerId + " " + macroPosition);
			stmt.setInt(1, playerId);
			stmt.setInt(2, macroPosition);
			stmt.execute();
		});
	}
	
	/** {@inheritDoc} */
	@Override
	public MacroList restoreMacrosses(int playerId)
	{
		final Map<Integer, String> macrosses = new HashMap<>();
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(SELECT_QUERY);
			stmt.setInt(1, playerId);
			final ResultSet rset = stmt.executeQuery();
			log.debug("[DAO: MySQL5PlayerMacrossesDAO] loading macroses for playerId: " + playerId);
			while (rset.next())
			{
				final int order = rset.getInt("order");
				final String text = rset.getString("macro");
				macrosses.put(order, text);
			}
			rset.close();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Could not restore MacroList data for player " + playerId + " from DB: " + e.getMessage(), e);
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return new MacroList(macrosses);
	}
	
	/** {@inheritDoc} */
	@Override
	public boolean supports(String databaseName, int majorVersion, int minorVersion)
	{
		return MySQL5DAOUtils.supports(databaseName, majorVersion, minorVersion);
	}
}
