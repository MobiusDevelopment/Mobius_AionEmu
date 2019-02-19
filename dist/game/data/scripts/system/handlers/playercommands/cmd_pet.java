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
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created By Ataba
 */
public class cmd_pet extends PlayerCommand
{
	public cmd_pet()
	{
		super("pet");
	}
	
	// @Override
	// public void execute(Player player, String...params){
	// if(params[0].equals("add")){
	// ItemService.addItem(player,190000000, 1); //Pet
	// PacketSendUtility.sendMessage(player, "\uE020 You Just Added a Buffer Pet! \uE020");
	// }
	// }
	@Override
	public void execute(Player player, String... param)
	{
		if (param.length < 1)
		{
			PacketSendUtility.sendMessage(player, "synax : .pet add  -- To Add a Buffer Pet");
			return;
		}
		if (param[0].equals("add"))
		{
			ItemService.addItem(player, 190000000, 1); // Pet
			PacketSendUtility.sendMessage(player, "\uE020 You Just Added a Buffer Pet! \uE020");
		}
	}
	
	@Override
	public void onFail(Player player, String msg)
	{
		PacketSendUtility.sendMessage(player, " " + "synax : .pet add  -- To Add a Buffer Pet\n");
	}
}
