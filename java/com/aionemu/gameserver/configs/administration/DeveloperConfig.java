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
package com.aionemu.gameserver.configs.administration;

import com.aionemu.commons.configuration.Property;

public class DeveloperConfig
{
	@Property(key = "gameserver.developer.spawn.enable", defaultValue = "true")
	public static boolean SPAWN_ENABLE;
	@Property(key = "gameserver.developer.itemstat.id", defaultValue = "0")
	public static int ITEM_STAT_ID;
	@Property(key = "gameserver.developer.spawn.check", defaultValue = "false")
	public static boolean SPAWN_CHECK;
}