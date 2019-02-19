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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * @author Phantom, ATracer
 */
public class Remove extends AdminCommand
{
	public Remove()
	{
		super("remove");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		if (params.length < 2)
		{
			PacketSendUtility.sendMessage(admin, "syntax //remove <player> <item ID> <quantity>");
			return;
		}
		
		int itemId = 0;
		long itemCount = 1;
		final Player target = World.getInstance().findPlayer(Util.convertName(params[0]));
		if (target == null)
		{
			PacketSendUtility.sendMessage(admin, "Player isn't online.");
			return;
		}
		
		try
		{
			itemId = Integer.parseInt(params[1]);
			if (params.length == 3)
			{
				itemCount = Long.parseLong(params[2]);
			}
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(admin, "Parameters need to be an integer.");
			return;
		}
		
		final Storage bag = target.getInventory();
		
		final long itemsInBag = bag.getItemCountByItemId(itemId);
		if (itemsInBag == 0)
		{
			PacketSendUtility.sendMessage(admin, "Items with that id are not found in the player's bag.");
			return;
		}
		
		final Item item = bag.getFirstItemByItemId(itemId);
		bag.decreaseByObjectId(item.getObjectId(), itemCount);
		
		PacketSendUtility.sendMessage(admin, "Item(s) removed succesfully");
		PacketSendUtility.sendMessage(target, "Admin removed an item from your bag");
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax //remove <player> <item ID> <quantity>");
	}
}
