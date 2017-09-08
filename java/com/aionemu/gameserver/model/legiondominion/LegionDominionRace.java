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
package com.aionemu.gameserver.model.legiondominion;

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Race;

public enum LegionDominionRace
{
	ELYOS(0, 1800481),
	ASMODIANS(1, 1800483),
	BALAUR(2, 1800485);
	
	private int raceId;
	private DescriptionId descriptionId;
	
	private LegionDominionRace(int id, int descriptionId)
	{
		raceId = id;
		this.descriptionId = new DescriptionId(descriptionId);
	}
	
	public int getRaceId()
	{
		return raceId;
	}
	
	public static LegionDominionRace getByRace(Race race)
	{
		switch (race)
		{
			case ASMODIANS:
			{
				return LegionDominionRace.ASMODIANS;
			}
			case ELYOS:
			{
				return LegionDominionRace.ELYOS;
			}
			default:
			{
				return LegionDominionRace.BALAUR;
			}
		}
	}
	
	public DescriptionId getDescriptionId()
	{
		return descriptionId;
	}
}