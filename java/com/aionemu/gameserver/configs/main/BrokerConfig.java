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
 * @author GiGatR00n v4.7.5.x
 */
public class BrokerConfig
{
	@Property(key = "gameserver.broker.save.manager.interval", defaultValue = "6")
	public static int SAVE_MANAGER_INTERVAL;
	@Property(key = "gameserver.broker.time.check.expired.items.interval", defaultValue = "60")
	public static int CHECK_EXPIRED_ITEMS_INTERVAL;
	@Property(key = "gameserver.broker.antihack.punishment", defaultValue = "0")
	public static int ANTI_HACK_PUNISHMENT;
	@Property(key = "gameserver.broker.items.expiretime", defaultValue = "8")
	public static int ITEMS_EXPIRE_TIME;
}