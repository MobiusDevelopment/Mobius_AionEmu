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
package system.handlers.admincommands;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author VladimirZ
 */
public class Online extends AdminCommand
{
	public Online()
	{
		super("online");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		
		final int playerCount = DAOManager.getDAO(PlayerDAO.class).getOnlinePlayerCount();
		
		if (playerCount == 1)
		{
			PacketSendUtility.sendMessage(admin, "There is " + (playerCount) + " player online !");
		}
		else
		{
			PacketSendUtility.sendMessage(admin, "There are " + (playerCount) + " players online !");
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax: //online");
	}
}
