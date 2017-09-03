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
import com.aionemu.gameserver.services.CubeExpandService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * @author Ghostfur
 */

public class cmd_expandcube extends PlayerCommand
{
	
	public cmd_expandcube()
	{
		super("expandcube");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (player.getNpcExpands() >= 15)
		{
			PacketSendUtility.sendMessage(player, "You have fully unlocked the cube!\nThere is no more cubes to unlock!");
			return;
		}
		while (player.getNpcExpands() < 15)
		{
			CubeExpandService.expand(player, true);
		}
		PacketSendUtility.sendMessage(player, "You have fully unlocked the cube!\nThere is no more cubes to unlock!");
	}
	
	@Override
	public void onFail(Player admin, String message)
	{
		PacketSendUtility.sendMessage(admin, "Syntaxe : .expandcube");
	}
}
