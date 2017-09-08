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
package com.aionemu.gameserver.utils.stats.enums;

/**
 * @author ATracer
 */
public enum ATTACK_SPEED
{
	WARRIOR(1400),
	GLADIATOR(1400),
	TEMPLAR(1400),
	SCOUT(1400),
	ASSASSIN(1400),
	RANGER(1400),
	MAGE(1400),
	SORCERER(1400),
	SPIRIT_MASTER(1400),
	PRIEST(1400),
	CLERIC(1400),
	CHANTER(1400),
	// News Class 4.3
	TECHNIST(1400),
	GUNSLINGER(1400),
	MUSE(1400),
	SONGWEAVER(1400),
	// News Class 4.5
	AETHERTECH(1400);
	
	private int value;
	
	private ATTACK_SPEED(int value)
	{
		this.value = value;
	}
	
	public int getValue()
	{
		return value;
	}
}