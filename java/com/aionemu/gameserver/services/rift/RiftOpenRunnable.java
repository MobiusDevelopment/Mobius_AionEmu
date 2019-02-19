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
package com.aionemu.gameserver.services.rift;

import java.util.Map;

import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.services.RiftService;

/**
 * @author Source
 */
public class RiftOpenRunnable implements Runnable
{
	private final int worldId;
	
	public RiftOpenRunnable(int worldId)
	{
		this.worldId = worldId;
	}
	
	@Override
	public void run()
	{
		final Map<Integer, RiftLocation> locations = RiftService.getInstance().getRiftLocations();
		for (RiftLocation loc : locations.values())
		{
			if (loc.getWorldId() == worldId)
			{
				RiftService.getInstance().openRifts(loc);
			}
		}
		RiftInformer.sendRiftsInfo(worldId);
	}
}