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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledFuture;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.eventEngine.EventScheduler;
import com.aionemu.gameserver.eventEngine.events.BattlegroundEvent;

/**
 * Created by wanke on 12/02/2017.
 */
public class BGService
{
	Logger log = LoggerFactory.getLogger(EventsService.class);
	private static final int DELAY = 60 * 100;
	private final List<ScheduledFuture<?>> futures = new ArrayList<>();
	
	BGService()
	{
		register(DELAY);
		log.info("<Battleground> initialized!");
	}
	
	public void register(int delay)
	{
		if (futures.isEmpty())
		{
			final BattlegroundEvent bgEvent = new BattlegroundEvent();
			bgEvent.setPriority(1);
			futures.add(EventScheduler.getInstance().scheduleAtFixedRate(bgEvent, delay, 6 * 60 * 1000));
		}
	}
	
	private static class SingletonHolder
	{
		protected static final BGService instance = new BGService();
	}
	
	public static BGService getInstance()
	{
		return SingletonHolder.instance;
	}
}
