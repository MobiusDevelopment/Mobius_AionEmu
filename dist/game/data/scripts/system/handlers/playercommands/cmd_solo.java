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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

public class cmd_solo extends PlayerCommand
{
	
	public cmd_solo()
	{
		super("vs");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (!LadderService.getInstance().isInQueue(player))
		{
			if (LadderService.getInstance().registerForSolo(player))
			{
				PacketSendUtility.sendSys3Message(player, "\uE005", "You are now registered in queue <1Vs1>");
			}
			else
			{
				PacketSendUtility.sendSys3Message(player, "\uE005", "Failed to save in queue <1Vs1>");
			}
		}
		else
		{
			LadderService.getInstance().unregisterFromQueue(player);
			PacketSendUtility.sendSys3Message(player, "\uE005", "You are now unsubscribed from queue <1Vs1>");
		}
	}
}