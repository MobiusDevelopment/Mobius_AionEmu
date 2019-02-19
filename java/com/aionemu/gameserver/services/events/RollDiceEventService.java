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
package com.aionemu.gameserver.services.events;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EVENT_DICE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */
public class RollDiceEventService
{
	// private static final Logger log = LoggerFactory.getLogger(RollDiceEventService.class);
	
	public void initEvent()
	{
	}
	
	public void onEnterWorld(Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_EVENT_DICE(1, 0, 1, 0, 0, 0));
	}
	
	public static RollDiceEventService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected static final RollDiceEventService instance = new RollDiceEventService();
	}
}