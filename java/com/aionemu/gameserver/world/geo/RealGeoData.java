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
package com.aionemu.gameserver.world.geo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.GeoWorldLoader;
import com.aionemu.gameserver.geoEngine.models.GeoMap;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.utils.Util;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
public class RealGeoData implements GeoData
{
	private static final Logger log = LoggerFactory.getLogger(RealGeoData.class);
	
	private final TIntObjectHashMap<GeoMap> geoMaps = new TIntObjectHashMap<>();
	
	@Override
	public void loadGeoMaps()
	{
		Map<String, Spatial> models = loadMeshes();
		loadWorldMaps(models);
		models.clear();
		models = null;
	}
	
	/**
	 * @param models
	 */
	protected void loadWorldMaps(Map<String, Spatial> models)
	{
		log.info("Loading geo maps..");
		Util.printProgressBarHeader(71);
		final List<Integer> mapsWithErrors = new ArrayList<>();
		final int totalSize = DataManager.WORLD_MAPS_DATA.size();
		int currentProgress = 0;
		int lastProgress = 1;
		
		for (WorldMapTemplate map : DataManager.WORLD_MAPS_DATA)
		{
			final GeoMap geoMap = new GeoMap(Integer.toString(map.getMapId()), map.getWorldSize());
			try
			{
				if (GeoWorldLoader.loadWorld(map.getMapId(), models, geoMap))
				{
					geoMaps.put(map.getMapId(), geoMap);
				}
			}
			catch (Throwable t)
			{
				mapsWithErrors.add(map.getMapId());
				geoMaps.put(map.getMapId(), DummyGeoData.DUMMY_MAP);
			}
			
			currentProgress++;
			if ((totalSize / currentProgress) < (140 / lastProgress))
			{
				Util.printCurrentProgress();
				lastProgress++;
			}
		}
		Util.printEndProgress();
		
		if (mapsWithErrors.size() > 0)
		{
		}
	}
	
	/**
	 * @return
	 */
	protected Map<String, Spatial> loadMeshes()
	{
		log.info("Loading meshes..");
		Map<String, Spatial> models = null;
		try
		{
			models = GeoWorldLoader.loadMeshs("data/geodata/meshs.geo");
		}
		catch (IOException e)
		{
			throw new IllegalStateException("Problem loading meshes", e);
		}
		return models;
	}
	
	@Override
	public GeoMap getMap(int worldId)
	{
		final GeoMap geoMap = geoMaps.get(worldId);
		return geoMap != null ? geoMap : DummyGeoData.DUMMY_MAP;
	}
}
