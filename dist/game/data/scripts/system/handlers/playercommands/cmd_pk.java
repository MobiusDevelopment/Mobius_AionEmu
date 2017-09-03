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
package system.handlers.playercommands;

import java.util.Iterator;

import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.ls.LoginServer;
import com.aionemu.gameserver.network.ls.serverpackets.SM_ACCOUNT_TOLL_INFO;
import com.aionemu.gameserver.services.events.BanditService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;
import com.aionemu.gameserver.world.World;

/**
 * Created by wanke on 13/02/2017.
 */

public class cmd_pk extends PlayerCommand
{
	public cmd_pk()
	{
		super("pk");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (!player.isBandit())
		{
			long toll = player.getClientConnection().getAccount().getToll();
			if (toll > PvPConfig.TOLL_PK_COST)
			{
				toll -= PvPConfig.TOLL_PK_COST;
				if (toll < 0)
				{
					toll = 0;
				}
				if (LoginServer.getInstance().sendPacket(new SM_ACCOUNT_TOLL_INFO(toll, player.getClientConnection().getAccount().getLuna(), player.getAcountName())))
				{
					player.getClientConnection().getAccount().setToll(toll);
				}
			}
			else
			{
				PacketSendUtility.sendMessage(player, "You dont have anough toll");
				return;
			}
			BanditService.getInstance().startBandit(player);
			PacketSendUtility.sendSys3Message(player, "\uE005", "<[PK] Bandit> started !!!");
			
			final Iterator<Player> iter = World.getInstance().getPlayersIterator();
			final String message = "Player " + player.getName() + " became a PK monster in " + player.getWorldType().name() + ", end his insanity to win " + PvPConfig.TOLL_PK_COST + " toll bounty;";
			Player target;
			while (iter.hasNext())
			{
				target = iter.next();
				
				PacketSendUtility.sendBrightYellowMessageOnCenter(target, message);
			}
		}
		else
		{
			BanditService.getInstance().stopBandit(player);
			PacketSendUtility.sendSys3Message(player, "\uE005", "<[PK] Bandit> stop !!!");
		}
	}
}