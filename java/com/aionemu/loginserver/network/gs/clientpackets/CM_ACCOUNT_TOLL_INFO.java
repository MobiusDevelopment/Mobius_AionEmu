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
package com.aionemu.loginserver.network.gs.clientpackets;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.loginserver.dao.AccountDAO;
import com.aionemu.loginserver.dao.PremiumDAO;
import com.aionemu.loginserver.model.Account;
import com.aionemu.loginserver.network.gs.GsClientPacket;

/**
 * @author xTz
 */
public class CM_ACCOUNT_TOLL_INFO extends GsClientPacket
{
	
	private int type;
	private long toll;
	private String accountName;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void readImpl()
	{
		type = readC();
		toll = readQ();
		accountName = readS();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void runImpl()
	{
		final Account account = DAOManager.getDAO(AccountDAO.class).getAccount(accountName);
		
		if (account != null)
		{
			if (type == 10)
			{
				// TODO DAOManager.getDAO(PremiumDAO.class).updatePointBoutique(account.getId(), toll);
			}
			else
			{
				DAOManager.getDAO(PremiumDAO.class).updatePoints(account.getId(), toll, 0);
			}
		}
	}
}