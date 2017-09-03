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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.PlayerTransformDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public class MySQL5PlayerTransfoDAO extends PlayerTransformDAO
{
	private static final Logger log = LoggerFactory.getLogger(MySQL5PlayerTransfoDAO.class);
	public static final String INSERT_QUERY = "INSERT INTO `player_transform` (`player_id`, `panel_id`, `item_id`) VALUES (?,?,?)";
	public static final String SELECT_QUERY = "SELECT * FROM `player_transform` WHERE `player_id`=?";
	public static final String DELETE_QUERY = "DELETE FROM `player_transform` WHERE `player_id`=?";
	
	@Override
	public void loadPlTransfo(Player player)
	{
		DB.select(SELECT_QUERY, new ParamReadStH()
		{
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, player.getObjectId());
			}
			
			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while (rset.next())
				{
					final int panelId = rset.getInt("panel_id");
					final int itemId = rset.getInt("item_id");
					player.getTransformModel().setPanelId(panelId);
					player.getTransformModel().setItemId(itemId);
				}
			}
		});
	}
	
	@Override
	public boolean storePlTransfo(int playerId, int panelId, int itemId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(INSERT_QUERY);
			stmt.setInt(1, playerId);
			stmt.setInt(2, panelId);
			stmt.setInt(3, itemId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Could not store f2p for player " + playerId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	@Override
	public boolean deletePlTransfo(int playerId)
	{
		Connection con = null;
		try
		{
			con = DatabaseFactory.getConnection();
			final PreparedStatement stmt = con.prepareStatement(DELETE_QUERY);
			stmt.setInt(1, playerId);
			stmt.execute();
			stmt.close();
		}
		catch (Exception e)
		{
			log.error("Could not delete f2p for player " + playerId + " from DB: " + e.getMessage(), e);
			return false;
		}
		finally
		{
			DatabaseFactory.close(con);
		}
		return true;
	}
	
	@Override
	public boolean supports(String arg0, int arg1, int arg2)
	{
		return com.aionemu.gameserver.dao.MySQL5DAOUtils.supports(arg0, arg1, arg2);
	}
}