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
package com.aionemu.gameserver.services.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Source
 */
public class PlayerEventService4
{
	
	private static final Logger log = LoggerFactory.getLogger(PlayerEventService.class);
	
	private PlayerEventService4()
	{
		
		final EventCollector visitor = new EventCollector();
		ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				World.getInstance().doOnAllPlayers(visitor);
			}
		}, EventsConfig.EVENT_PERIOD3 * 60000, EventsConfig.EVENT_PERIOD3 * 60000);
	}
	
	private static final class EventCollector implements Visitor<Player>
	{
		
		@Override
		public void visit(Player player)
		{
			final int membership = player.getClientConnection().getAccount().getMembership();
			final int rate = EventsConfig.EVENT_REWARD_MEMBERSHIP_RATE ? membership + 1 : 1;
			final int level = player.getLevel();
			if ((membership >= EventsConfig.EVENT_REWARD_MEMBERSHIP) && (level >= EventsConfig.EVENT_REWARD_LEVEL4))
			{
				try
				{
					if (player.getInventory().isFull())
					{
						log.warn("[EventReward] player " + player.getName() + " tried to receive item with full inventory.");
					}
					else
					{
						ItemService.addItem(player, (player.getRace() == Race.ELYOS ? EventsConfig.EVENT_ITEM_ELYOS4 : EventsConfig.EVENT_ITEM_ASMO4), EventsConfig.EVENT_ITEM_COUNT4 * rate);
					}
				}
				catch (final Exception ex)
				{
					log.error("Exception during event rewarding of player " + player.getName(), ex);
				}
			}
		}
	}
	
	public static PlayerEventService4 getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		
		protected static final PlayerEventService4 instance = new PlayerEventService4();
	}
}
