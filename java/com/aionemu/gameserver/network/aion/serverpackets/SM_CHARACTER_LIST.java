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
package com.aionemu.gameserver.network.aion.serverpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dao.MailDAO;
import com.aionemu.gameserver.model.account.Account;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.PlayerInfo;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.player.PlayerService;

/**
 * In this packet Server is sending Character List to client.
 * @author Nemesiss, AEJTester
 */
public class SM_CHARACTER_LIST extends PlayerInfo
{
	
	private static Logger log = LoggerFactory.getLogger(SM_CHARACTER_LIST.class);
	
	/**
	 * PlayOk2 - we dont care...
	 */
	private final int playOk2;
	private final int unkValue;
	
	/**
	 * Constructs new <tt>SM_CHARACTER_LIST </tt> packet
	 * @param unkValue
	 * @param playOk2
	 */
	public SM_CHARACTER_LIST(int unkValue, int playOk2)
	{
		this.playOk2 = playOk2;
		this.unkValue = unkValue;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(unkValue);// 5.0 unk protocol
		if (unkValue == 0)
		{
			writeD(playOk2);
			writeC(0);
		}
		else if (unkValue == 2)
		{
			writeD(playOk2);
			final Account account = con.getAccount();
			writeC(account.size()); // characters count
			for (PlayerAccountData playerData : account.getSortedAccountsList())
			{
				final PlayerCommonData pcd = playerData.getPlayerCommonData();
				final Player player = PlayerService.getPlayer(pcd.getPlayerObjId(), account);
				writePlayerInfo(playerData);
				writeD(player.getPlayerSettings().getDisplay());// display helmet 0 show, 5 dont show
				writeD(0);
				writeD(0);
				writeD(DAOManager.getDAO(MailDAO.class).haveUnread(pcd.getPlayerObjId()) ? 1 : 0); // mail
				writeD(0); // unk
				writeD(0); // unk
				writeQ(BrokerService.getInstance().getCollectedMoney(pcd)); // collected money from broker
				writeD(0);
				if (GSConfig.SERVER_COUNTRY_CODE == 1)
				{
					writeB(new byte[122 + 24]); // 5.1 protocol
				}
			}
		}
	}
}