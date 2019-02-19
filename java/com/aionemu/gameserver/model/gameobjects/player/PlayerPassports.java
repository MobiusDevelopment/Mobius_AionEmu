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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

import com.aionemu.gameserver.model.templates.event.AtreianPassport;

/**
 * @author Ranastic
 */
public class PlayerPassports
{
	private final SortedMap<Integer, AtreianPassport> passports = new TreeMap<>();
	
	public synchronized boolean addPassport(int id, AtreianPassport ap)
	{
		if (passports.containsKey(id))
		{
			return false;
		}
		passports.put(id, ap);
		return true;
	}
	
	public synchronized boolean removePassport(int id)
	{
		if (passports.containsKey(id))
		{
			passports.remove(id);
			return true;
		}
		return false;
	}
	
	public Collection<AtreianPassport> getAllPassports()
	{
		return passports.values();
	}
}
