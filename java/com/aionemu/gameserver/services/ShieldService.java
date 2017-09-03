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
package com.aionemu.gameserver.services;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.CollisionDieActor;
import com.aionemu.gameserver.controllers.observer.ShieldObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.shield.Shield;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeShield;
import com.aionemu.gameserver.model.templates.shield.ShieldTemplate;
import com.aionemu.gameserver.world.zone.ZoneInstance;

import javolution.util.FastMap;

public class ShieldService
{
	Logger log = LoggerFactory.getLogger(ShieldService.class);
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		protected static final ShieldService instance = new ShieldService();
	}
	
	private final FastMap<Integer, Shield> sphereShields = new FastMap<>();
	private final FastMap<Integer, List<SiegeShield>> registeredShields = new FastMap<>(0);
	
	public static ShieldService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private ShieldService()
	{
	}
	
	public void load(int mapId)
	{
		for (final ShieldTemplate template : DataManager.SHIELD_DATA.getShieldTemplates())
		{
			if (template.getMap() != mapId)
			{
				continue;
			}
			final Shield f = new Shield(template);
			sphereShields.put(f.getId(), f);
		}
	}
	
	public void spawnAll()
	{
		for (final Shield shield : sphereShields.values())
		{
			shield.spawn();
			log.debug("Added " + shield.getName() + " at m=" + shield.getWorldId() + ",x=" + shield.getX() + ",y=" + shield.getY() + ",z=" + shield.getZ());
		}
		for (final List<SiegeShield> otherShields : registeredShields.values())
		{
			for (final SiegeShield shield : otherShields)
			{
				log.debug("Not bound shield " + shield.getGeometry().getName());
			}
		}
	}
	
	public ActionObserver createShieldObserver(int locationId, Creature observed)
	{
		if (sphereShields.containsKey(locationId))
		{
			return new ShieldObserver(sphereShields.get(locationId), observed);
		}
		return null;
	}
	
	public ActionObserver createShieldObserver(SiegeShield geoShield, Creature observed)
	{
		ActionObserver observer = null;
		if (GeoDataConfig.GEO_SHIELDS_ENABLE)
		{
			observer = new CollisionDieActor(observed, geoShield.getGeometry());
			((CollisionDieActor) observer).setEnabled(true);
		}
		return observer;
	}
	
	public void registerShield(int worldId, SiegeShield shield)
	{
		List<SiegeShield> mapShields = registeredShields.get(worldId);
		if (mapShields == null)
		{
			mapShields = new ArrayList<>();
			registeredShields.put(worldId, mapShields);
		}
		mapShields.add(shield);
	}
	
	public void attachShield(SiegeLocation location)
	{
		final List<SiegeShield> mapShields = registeredShields.get(location.getTemplate().getWorldId());
		if (mapShields == null)
		{
			return;
		}
		final ZoneInstance zone = location.getZone().get(0);
		final List<SiegeShield> shields = new ArrayList<>();
		for (int index = mapShields.size() - 1; index >= 0; index--)
		{
			final SiegeShield shield = mapShields.get(index);
			final Vector3f center = shield.getGeometry().getWorldBound().getCenter();
			if (zone.getAreaTemplate().isInside3D(center.x, center.y, center.z))
			{
				shields.add(shield);
				mapShields.remove(index);
				final Shield sphereShield = sphereShields.get(location.getLocationId());
				if (sphereShield != null)
				{
					sphereShields.remove(location.getLocationId());
				}
				shield.setSiegeLocationId(location.getLocationId());
			}
		}
		if (shields.size() == 0)
		{
			log.warn("Could not find a shield for locId: " + location.getLocationId());
		}
		else
		{
			location.setShields(shields);
		}
	}
}