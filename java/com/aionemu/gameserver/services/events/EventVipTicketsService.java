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

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class EventVipTicketsService
{
	EventVipTicketsService()
	{
		/**
		 * VIP Tickets.
		 */
		final AnnounceVIPTickets vipTickets = new AnnounceVIPTickets();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> World.getInstance().doOnAllPlayers(vipTickets), EventsConfig.VIP_TICKETS_PERIOD * 60000, EventsConfig.VIP_TICKETS_PERIOD * 60000);
	}
	
	private static final class AnnounceVIPTickets implements Visitor<Player>
	{
		public AnnounceVIPTickets()
		{
		}
		
		@Override
		public void visit(Player player)
		{
			if (EventsConfig.ENABLE_VIP_TICKETS)
			{
				if ((player.getLevel() >= 1) && (player.getLevel() <= 65))
				{
					HTMLService.sendGuideHtml(player, "VIP_Benefits");
					// You can become stronger with the VIP benefits.\n See the VIP Tickets in the in-game shop.
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_VIP_LOBBY_NOTICE_CASE12_POPUP_01, 0, 0));
				}
				if ((player.getLevel() >= 66) && (player.getLevel() <= 83))
				{
					HTMLService.sendGuideHtml(player, "ArchDaeva_Benefits");
					// You can become stronger with the VIP benefits.\n See the VIP Tickets in the in-game shop.
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_VIP_LOBBY_NOTICE_CASE12_POPUP_01, 0, 0));
				}
			}
		}
	}
	
	public static EventVipTicketsService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected static final EventVipTicketsService instance = new EventVipTicketsService();
	}
}