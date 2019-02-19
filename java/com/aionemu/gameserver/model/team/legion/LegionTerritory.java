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
package com.aionemu.gameserver.model.team.legion;

public class LegionTerritory
{
	int territoryId = 0;
	int legionId = 0;
	String legionName = "";
	
	public LegionTerritory(int id)
	{
		territoryId = id;
	}
	
	public LegionTerritory()
	{
	}
	
	public int getId()
	{
		return territoryId;
	}
	
	public void setTerritoryId(int terretoryId)
	{
		territoryId = terretoryId;
	}
	
	public int getLegionId()
	{
		return legionId;
	}
	
	public void setLegionId(int legionId)
	{
		this.legionId = legionId;
	}
	
	public String getLegionName()
	{
		return legionName;
	}
	
	public void setLegionName(String legionName)
	{
		this.legionName = legionName;
	}
}