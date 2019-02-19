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
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

public class EventAwakeService
{
	EventAwakeService()
	{
		/**
		 * Event Awake [Event JAP] http://event2.ncsoft.jp/1.0/aion/1503awake/
		 */
		final EventAwake awake = new EventAwake();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> World.getInstance().doOnAllPlayers(awake), EventsConfig.SEED_TRANSFORMATION_PERIOD * 60000, EventsConfig.SEED_TRANSFORMATION_PERIOD * 60000);
	}
	
	private static final class EventAwake implements Visitor<Player>
	{
		public EventAwake()
		{
		}
		
		@Override
		public void visit(Player player)
		{
			if (EventsConfig.ENABLE_AWAKE_EVENT)
			{
				if ((player.getLevel() >= 10) && (player.getLevel() <= 64))
				{
					HTMLService.sendGuideHtml(player, "Event_Awake_10");
				}
				if ((player.getLevel() >= 65) && (player.getLevel() <= 83))
				{
					HTMLService.sendGuideHtml(player, "Event_Awake_65");
				}
			}
		}
	}
	
	public static EventAwakeService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected static final EventAwakeService instance = new EventAwakeService();
	}
}