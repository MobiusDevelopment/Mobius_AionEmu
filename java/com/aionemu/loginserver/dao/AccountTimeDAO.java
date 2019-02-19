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
import com.aionemu.loginserver.model.AccountTime;

/**
 * DAO to manage account time
 */
public abstract class AccountTimeDAO implements DAO
{
	/**
	 * Updates @link com.aionemu.loginserver.model.AccountTime data of account
	 * @param accountId account id
	 * @param accountTime account time set
	 * @return was update successfull or not
	 */
	public abstract boolean updateAccountTime(int accountId, AccountTime accountTime);
	
	/**
	 * Updates @link com.aionemu.loginserver.model.AccountTime data of account
	 * @param accountId
	 * @return AccountTime
	 */
	public abstract AccountTime getAccountTime(int accountId);
	
	/**
	 * Returns uniquire class name for all implementations
	 * @return uniquire class name for all implementations
	 */
	@Override
	public final String getClassName()
	{
		return AccountTimeDAO.class.getName();
	}
}
