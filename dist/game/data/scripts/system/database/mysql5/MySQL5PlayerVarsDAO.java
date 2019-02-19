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
import java.util.Map;

import com.aionemu.commons.database.DB;
import com.aionemu.commons.database.IUStH;
import com.aionemu.commons.database.ParamReadStH;
import com.aionemu.gameserver.dao.MySQL5DAOUtils;
import com.aionemu.gameserver.dao.PlayerVarsDAO;

import javolution.util.FastMap;

/**
 * @author KID
 */
public class MySQL5PlayerVarsDAO extends PlayerVarsDAO
{
	@Override
	public Map<String, Object> load(int playerId)
	{
		final Map<String, Object> map = FastMap.newInstance();
		DB.select("SELECT param,value FROM player_vars WHERE player_id=?", new ParamReadStH()
		{
			
			@Override
			public void handleRead(ResultSet rset) throws SQLException
			{
				while (rset.next())
				{
					final String key = rset.getString("param");
					final String value = rset.getString("value");
					map.put(key, value);
				}
			}
			
			@Override
			public void setParams(PreparedStatement st) throws SQLException
			{
				st.setInt(1, playerId);
			}
		});
		
		return map;
	}
	
	@Override
	public boolean set(int playerId, String key, Object value)
	{
		final boolean result = DB.insertUpdate("INSERT INTO player_vars (`player_id`, `param`, `value`, `time`) VALUES (?,?,?,NOW())", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerId);
				stmt.setString(2, key);
				stmt.setString(3, value.toString());
				stmt.execute();
			}
		});
		
		return result;
	}
	
	@Override
	public boolean remove(int playerId, String key)
	{
		final boolean result = DB.insertUpdate("DELETE FROM player_vars WHERE player_id=? AND param=?", new IUStH()
		{
			
			@Override
			public void handleInsertUpdate(PreparedStatement stmt) throws SQLException
			{
				stmt.setInt(1, playerId);
				stmt.setString(2, key);
				stmt.execute();
			}
		});
		
		return result;
	}
	
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
