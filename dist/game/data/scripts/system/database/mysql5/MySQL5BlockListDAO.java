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
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.BlockListDAO;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.BlockList;
import com.aionemu.gameserver.model.gameobjects.player.BlockedPlayer;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;

/**
 * @author Ben
 */
public class MySQL5BlockListDAO extends BlockListDAO
{
	
	public static final String LOAD_QUERY = "SELECT blocked_player, reason FROM blocks WHERE player=?";
	public static final String ADD_QUERY = "INSERT INTO blocks (player, blocked_player, reason) VALUES (?, ?, ?)";
	public static final String DEL_QUERY = "DELETE FROM blocks WHERE player=? AND blocked_player=?";
	public static final String SET_REASON_QUERY = "UPDATE blocks SET reason=? WHERE player=? AND blocked_player=?";
	private static Logger log = LoggerFactory.getLogger(MySQL5BlockListDAO.class);
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addBlockedUser(int playerObjId, int objIdToBlock, String reason)
	{
		return DB.insertUpdate(ADD_QUERY, new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerObjId);
				stmt.setInt(2, objIdToBlock);
				stmt.setString(3, reason);
				stmt.execute();
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean delBlockedUser(int playerObjId, int objIdToDelete)
	{
		return DB.insertUpdate(DEL_QUERY, new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerObjId);
				stmt.setInt(2, objIdToDelete);
				stmt.execute();
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public BlockList load(Player player)
	{
		final Map<Integer, BlockedPlayer> list = new HashMap<>();
		
		DB.select(LOAD_QUERY, new ParamReadStH()
		{
			
			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				final PlayerDAO playerDao = DAOManager.getDAO(PlayerDAO.class);
				while (rset.next())
				{
					final int blockedOid = rset.getInt("blocked_player");
					final PlayerCommonData pcd = playerDao.loadPlayerCommonData(blockedOid);
					if (pcd == null)
					{
						log.error("Attempt to load block list for " + player.getName() + " tried to load a player which does not exist: " + blockedOid);
					}
					else
					{
						list.put(blockedOid, new BlockedPlayer(pcd, rset.getString("reason")));
					}
				}
				
			}
			
			@Override
			public void setParams(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, player.getObjectId());
			}
		});
		return new BlockList(list);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean setReason(int playerObjId, int blockedPlayerObjId, String reason)
	{
		return DB.insertUpdate(SET_REASON_QUERY, new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setString(1, reason);
				stmt.setInt(2, playerObjId);
				stmt.setInt(3, blockedPlayerObjId);
				stmt.execute();
				
			}
		});
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
