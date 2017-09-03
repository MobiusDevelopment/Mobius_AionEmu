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
package com.aionemu.loginserver.dao;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.loginserver.service.ptransfer.PlayerTransferTask;

import javolution.util.FastList;

/**
 * @author KID
 */
public abstract class PlayerTransferDAO implements DAO
{
	
	public abstract FastList<PlayerTransferTask> getNew();
	
	public abstract boolean update(PlayerTransferTask task);
	
	@Override
	public final String getClassName()
	{
		return PlayerTransferDAO.class.getName();
	}
}
