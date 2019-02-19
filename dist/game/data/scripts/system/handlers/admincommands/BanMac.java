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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author KID, nrg
 */
public class BanMac extends AdminCommand
{
	public BanMac()
	{
		super("banmac");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if ((params == null) || (params.length < 1))
		{
			onFail(player, "Please add one or more parameters");
			return;
		}
		
		int time;
		String address;
		String targetName = "direct_type";
		
		// try parsing
		try
		{
			time = Integer.parseInt(params[0]);
			
			if (time == 0)
			{
				time = 60 * 24 * 365 * 10;
			}
		}
		catch (NumberFormatException e)
		{
			onFail(player, "Please enter a valid integer amount of minutes");
			return;
		}
		
		// is mac defined?
		if (params.length > 1)
		{
			address = params[1];
		}
		else
		{ // no address defined
			final VisibleObject target = player.getTarget();
			if ((target != null) && (target instanceof Player))
			{
				if (target.getObjectId() == player.getObjectId())
				{
					onFail(player, "Omg, disselect yourself please.");
					return;
				}
				
				final Player targetpl = (Player) target;
				address = targetpl.getClientConnection().getMacAddress();
				targetName = targetpl.getName();
				targetpl.getClientConnection().closeNow();
			}
			else
			{
				onFail(player, "You should select a player or give me any mac address");
				return;
			}
		}
		
		BannedMacManager.getInstance().banAddress(address, System.currentTimeMillis() + (time * 60 * 1000), "author=" + player.getName() + ", " + player.getObjectId() + "; target=" + targetName);
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		if (!message.equals(""))
		{
			PacketSendUtility.sendMessage(player, message);
		}
		PacketSendUtility.sendMessage(player, "Syntax: //banmac [time in minutes] <mac>");
		PacketSendUtility.sendMessage(player, "Note: 0 minutes will cause permanent ban");
	}
	
}
