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
package com.aionemu.gameserver.dao;

import java.util.Map;

import com.aionemu.commons.database.dao.DAO;

/**
 * @author KID
 */
public abstract class PlayerVarsDAO implements DAO
{
	@Override
	public String getClassName()
	{
		return PlayerVarsDAO.class.getName();
	}
	
	public abstract Map<String, Object> load(int playerId);
	
	public abstract boolean set(int playerId, String key, Object value);
	
	public abstract boolean remove(int playerId, String key);
}
