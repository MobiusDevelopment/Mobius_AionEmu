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
package com.aionemu.gameserver.world;

import com.aionemu.gameserver.world.zone.ZoneInstance;

/**
 * @author ATracer
 */
public class WorldMap2DInstance extends WorldMapInstance
{
	
	private int ownerId;
	
	public WorldMap2DInstance(WorldMap parent, int instanceId, int ownerId)
	{
		super(parent, instanceId);
		this.ownerId = ownerId;
	}
	
	@Override
	protected MapRegion createMapRegion(int regionId)
	{
		final float startX = RegionUtil.getXFrom2dRegionId(regionId);
		final float startY = RegionUtil.getYFrom2dRegionId(regionId);
		final int size = getParent().getWorldSize();
		final float maxZ = Math.round((float) size / regionSize) * regionSize;
		final ZoneInstance[] zones = filterZones(getMapId(), regionId, startX, startY, 0, maxZ);
		return new MapRegion(regionId, this, zones);
	}
	
	@Override
	protected void initMapRegions()
	{
		final int size = getParent().getWorldSize();
		// Create all mapRegion
		for (int x = 0; x <= size; x = x + regionSize)
		{
			for (int y = 0; y <= size; y = y + regionSize)
			{
				final int regionId = RegionUtil.get2dRegionId(x, y);
				regions.put(regionId, createMapRegion(regionId));
			}
		}
		
		// Add Neighbour
		for (int x = 0; x <= size; x = x + regionSize)
		{
			for (int y = 0; y <= size; y = y + regionSize)
			{
				final int regionId = RegionUtil.get2dRegionId(x, y);
				final MapRegion mapRegion = regions.get(regionId);
				for (int x2 = x - regionSize; x2 <= (x + regionSize); x2 += regionSize)
				{
					for (int y2 = y - regionSize; y2 <= (y + regionSize); y2 += regionSize)
					{
						if ((x2 == x) && (y2 == y))
						{
							continue;
						}
						final int neighbourId = RegionUtil.get2dRegionId(x2, y2);
						final MapRegion neighbour = regions.get(neighbourId);
						if (neighbour != null)
						{
							mapRegion.addNeighbourRegion(neighbour);
						}
					}
				}
			}
		}
	}
	
	@Override
	public MapRegion getRegion(float x, float y, float z)
	{
		final int regionId = RegionUtil.get2dRegionId(x, y);
		return regions.get(regionId);
	}
	
	/**
	 * @return the ownerId
	 */
	@Override
	public int getOwnerId()
	{
		return ownerId;
	}
	
	/**
	 * @param ownerId the ownerId to set
	 */
	public void setOwnerId(int ownerId)
	{
		this.ownerId = ownerId;
	}
	
	@Override
	public boolean isPersonal()
	{
		return ownerId != 0;
	}
	
}
