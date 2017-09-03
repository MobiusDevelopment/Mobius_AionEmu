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
package system.handlers.weddingcommands;

import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.WeddingCommand;

/**
 * @author synchro2
 */
public class cometome extends WeddingCommand
{
	
	public cometome()
	{
		super("cometome");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		
		final Player partner = player.findPartner();
		
		if (partner == null)
		{
			PacketSendUtility.sendMessage(player, "Not online.");
			return;
		}
		
		if ((player.getWorldId() == 510010000) || (player.getWorldId() == 520010000))
		{
			PacketSendUtility.sendMessage(player, "You can't use this command on prison.");
			return;
		}
		
		if ((partner.getWorldId() == 510010000) || (partner.getWorldId() == 520010000))
		{
			PacketSendUtility.sendMessage(player, "You can't teleported " + partner.getName() + ", your partner is on prison.");
			return;
		}
		
		if (player.isInInstance())
		{
			PacketSendUtility.sendMessage(player, "You can't teleport your partner " + partner.getName() + ", you are in Instance.");
			return;
		}
		
		if (!player.isCommandInUse())
		{
			TeleportService2.teleportTo(partner, player.getWorldId(), player.getInstanceId(), player.getX(), player.getY(), player.getZ(), player.getHeading(), TeleportAnimation.BEAM_ANIMATION);
			PacketSendUtility.sendMessage(player, partner.getName() + " teleported to you.");
			player.setCommandUsed(true);
			
			ThreadPoolManager.getInstance().schedule(() -> player.setCommandUsed(false), 60 * 60 * 1000);
		}
		else
		{
			PacketSendUtility.sendMessage(player, "Only 1 TP per hour.");
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Failed");
	}
}
