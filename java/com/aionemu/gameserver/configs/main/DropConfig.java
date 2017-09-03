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

public class DropConfig
{
	@Property(key = "gameserver.drop.reduction.disable", defaultValue = "false")
	public static boolean DISABLE_DROP_REDUCTION;
	
	@Property(key = "gameserver.unique.drop.announce.enable", defaultValue = "true")
	public static boolean ENABLE_UNIQUE_DROP_ANNOUNCE;
	
	@Property(key = "gameserver.drop.noreduction", defaultValue = "0")
	public static String DISABLE_DROP_REDUCTION_IN_ZONES;
}