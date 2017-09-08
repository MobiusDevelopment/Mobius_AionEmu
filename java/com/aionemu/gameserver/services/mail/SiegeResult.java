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
package com.aionemu.gameserver.services.mail;

/**
 * @author Rolandas
 */
public enum SiegeResult
{
	DEFENCE(0),
	OCCUPY(1),
	PROTECT(2),
	DEFENDER(3),
	EMPTY(4),
	FAIL(5);
	
	private int value;
	
	private SiegeResult(int value)
	{
		this.value = value;
	}
	
	public int getId()
	{
		return value;
	}
}