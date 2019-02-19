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

import java.util.HashMap;
import java.util.Map;

import com.aionemu.commons.database.DB;
import com.aionemu.loginserver.GameServerInfo;
import com.aionemu.loginserver.dao.GameServersDAO;

/**
 * GameServers DAO implementation for MySQL5
 * @author -Nemesiss-
 */
public class MySQL5GameServersDAO extends GameServersDAO
{
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Map<Byte, GameServerInfo> getAllGameServers()
	{
		
		final Map<Byte, GameServerInfo> result = new HashMap<Byte, GameServerInfo>();
		DB.select("SELECT * FROM gameservers", resultSet ->
		{
			while (resultSet.next())
			{
				byte id = resultSet.getByte("id");
				String ipMask = resultSet.getString("mask");
				String password = resultSet.getString("password");
				GameServerInfo gsi = new GameServerInfo(id, ipMask, password);
				result.put(id, gsi);
			}
		});
		return result;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean supports(String s, int i, int i1)
	{
		return MySQL5DAOUtils.supports(s, i, i1);
	}
}
