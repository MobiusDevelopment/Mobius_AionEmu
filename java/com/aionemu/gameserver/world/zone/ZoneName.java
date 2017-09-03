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
package com.aionemu.gameserver.world.zone;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javolution.util.FastMap;

/**
 * @author Rolandas
 */
public final class ZoneName
{
	
	private static final Logger log = LoggerFactory.getLogger(ZoneName.class);
	
	private static final FastMap<String, ZoneName> zoneNames = new FastMap<>();
	public static final String NONE = "NONE";
	public static final String ABYSS_CASTLE = "_ABYSS_CASTLE_AREA_";
	
	static
	{
		zoneNames.put(NONE, new ZoneName(NONE));
		zoneNames.put(ABYSS_CASTLE, new ZoneName(ABYSS_CASTLE));
	}
	
	private final String _name;
	
	private ZoneName(String name)
	{
		_name = name;
	}
	
	public String name()
	{
		return _name;
	}
	
	public int id()
	{
		return _name.hashCode();
	}
	
	public static ZoneName createOrGet(String name)
	{
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
		{
			return zoneNames.get(name);
		}
		final ZoneName newZone = new ZoneName(name);
		zoneNames.put(name, newZone);
		return newZone;
	}
	
	public static int getId(String name)
	{
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
		{
			return zoneNames.get(name).id();
		}
		return zoneNames.get(NONE).id();
	}
	
	public static ZoneName get(String name)
	{
		name = name.toUpperCase();
		if (zoneNames.containsKey(name))
		{
			return zoneNames.get(name);
		}
		log.warn("Missing zone : " + name);
		return zoneNames.get(NONE);
	}
	
	@Override
	public String toString()
	{
		return _name;
	}
	
}
