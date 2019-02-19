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
 * @author xTz
 */
public class InGameShopConfig
{
	
	/**
	 * Enable in game shop
	 */
	@Property(key = "gameserver.ingameshop.enable", defaultValue = "false")
	public static boolean ENABLE_IN_GAME_SHOP;
	
	/**
	 * Enable gift system between factions
	 */
	@Property(key = "gameserver.ingameshop.gift", defaultValue = "false")
	public static boolean ENABLE_GIFT_OTHER_RACE;
	
	@Property(key = "gameserver.ingameshop.allow.gift", defaultValue = "true")
	public static boolean ALLOW_GIFTS;
}
