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
package com.aionemu.gameserver.services.zorshivdredgionservice;

import java.util.Map;

import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import com.aionemu.gameserver.services.ZorshivDredgionService;

/**
 * @author Rinzler (Encom)
 */
public class DredgionStartRunnable implements Runnable
{
	private final int id;
	
	public DredgionStartRunnable(int id)
	{
		this.id = id;
	}
	
	@Override
	public void run()
	{
		// The Balaur Dredgion has appeared at levinshor.
		ZorshivDredgionService.getInstance().levinshorMsg(id);
		// The Balaur Dredgion has appeared at inggison.
		ZorshivDredgionService.getInstance().inggisonMsg(id);
		final Map<Integer, ZorshivDredgionLocation> locations = ZorshivDredgionService.getInstance().getZorshivDredgionLocations();
		for (ZorshivDredgionLocation loc : locations.values())
		{
			if (loc.getId() == id)
			{
				ZorshivDredgionService.getInstance().startZorshivDredgion(loc.getId());
			}
		}
	}
}