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
package com.aionemu.gameserver.network.ls.clientpackets;

import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.network.ls.LsClientPacket;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;

/**
 * @author KID
 */
public class CM_PTRANSFER_RESPONSE extends LsClientPacket
{
	public CM_PTRANSFER_RESPONSE(int opCode)
	{
		super(opCode);
	}
	
	@Override
	protected void readImpl()
	{
		final int actionId = readD();
		switch (actionId)
		{
			case 20: // send info
			{
				final int targetAccount = readD();
				final int taskId = readD();
				final String name = readS();
				final String account = readS();
				final int len = readD();
				final byte[] db = readB(len);
				PlayerTransferService.getInstance().cloneCharacter(taskId, targetAccount, name, account, db);
			}
				break;
			case 21:// ok
			{
				final int taskId = readD();
				PlayerTransferService.getInstance().onOk(taskId);
			}
				break;
			case 22:// error
			{
				final int taskId = readD();
				final String reason = readS();
				PlayerTransferService.getInstance().onError(taskId, reason);
			}
				break;
			case 23:
			{
				final byte serverId = readSC();
				if (NetworkConfig.GAMESERVER_ID != serverId)
				{
					try
					{
						throw new Exception("Requesting player transfer for server id " + serverId + " but this is " + NetworkConfig.GAMESERVER_ID + " omgshit!");
					}
					catch (final Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					final byte targetServerId = readSC();
					final int account = readD();
					final int targetAccount = readD();
					final int playerId = readD();
					final int taskId = readD();
					PlayerTransferService.getInstance().startTransfer(account, targetAccount, playerId, targetServerId, taskId);
				}
			}
				break;
		}
	}
	
	@Override
	protected void runImpl()
	{
		
	}
}
