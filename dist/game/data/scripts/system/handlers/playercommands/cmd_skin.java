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
import com.aionemu.gameserver.network.aion.serverpackets.SM_TRANSFORM;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by Kill3r
 */
public class cmd_skin extends PlayerCommand
{
	public cmd_skin()
	{
		super("skin");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		final int skin = 0;
		player.getTransformModel().setModelId(skin);
		PacketSendUtility.broadcastPacketAndReceive(player, new SM_TRANSFORM(player, true));
		
		PacketSendUtility.sendMessage(player, "You have removed the candy form but you will still have the stats.");
		
	}
}
