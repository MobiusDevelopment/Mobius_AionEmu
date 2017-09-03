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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

/**
 * @author Ranastic
 */

public class AStationConfig
{
	@Property(key = "a.station.server.id", defaultValue = "2")
	public static int A_STATION_SERVER_ID;
	@Property(key = "a.station.max.level", defaultValue = "65")
	public static int A_STATION_MAX_LEVEL;
	@Property(key = "a.station.enable", defaultValue = "true")
	public static boolean A_STATION_ENABLE;
}