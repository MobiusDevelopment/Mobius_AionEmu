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
package com.aionemu.gameserver.model;

import java.util.NoSuchElementException;

/**
 * @author synchro2
 */
public enum SellLimit
{
	LIMIT_1_65(1, 65, 292000047L),
	LIMIT_66_83(66, 83, 392000047L);
	
	private int playerMinLevel;
	private int playerMaxLevel;
	private long limit;
	
	private SellLimit(int playerMinLevel, int playerMaxLevel, long limit)
	{
		this.playerMinLevel = playerMinLevel;
		this.playerMaxLevel = playerMaxLevel;
		this.limit = limit;
	}
	
	public static long getSellLimit(int playerLevel)
	{
		for (SellLimit sellLimit : values())
		{
			if ((sellLimit.playerMinLevel <= playerLevel) && (sellLimit.playerMaxLevel >= playerLevel))
			{
				return sellLimit.limit;
			}
		}
		throw new NoSuchElementException("Sell limit for player level: " + playerLevel + " was not found");
	}
}