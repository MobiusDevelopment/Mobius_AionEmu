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
package com.aionemu.gameserver.model.templates.housing;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 * @see client_housing_object_place_tag.xml</tt>
 */
@XmlType(name = "LimitType")
@XmlEnum
public enum LimitType
{
	NONE(0, new int[]
	{
		0,
		0,
		0,
		0,
		0
	}, new int[]
	{
		0,
		0,
		0,
		0,
		0
	}),
	OWNER_POT(1, new int[]
	{
		8,
		6,
		4,
		3,
		8
	}, new int[]
	{
		0,
		0,
		0,
		0,
		4
	}),
	VISITOR_POT(2, new int[]
	{
		9,
		7,
		5,
		2,
		8
	}, new int[]
	{
		0,
		0,
		0,
		0,
		4
	}),
	STORAGE(3, new int[]
	{
		7,
		6,
		5,
		4,
		8
	}, new int[]
	{
		0,
		0,
		0,
		0,
		4
	}),
	POT(4, new int[]
	{
		7,
		6,
		5,
		4,
		3
	}, new int[]
	{
		7,
		6,
		5,
		4,
		1
	}),
	COOKING(5, new int[]
	{
		2,
		2,
		2,
		2,
		2
	}, new int[]
	{
		2,
		2,
		2,
		2,
		2
	}),
	PICTURE(6, new int[]
	{
		1,
		1,
		1,
		1,
		1
	}, new int[]
	{
		1,
		1,
		1,
		1,
		0
	}),
	JUKEBOX(7, new int[]
	{
		1,
		1,
		1,
		1,
		1
	}, new int[]
	{
		1,
		1,
		1,
		1,
		0
	});
	
	int id;
	int[] personalLimits;
	int[] trialLimits;
	
	private LimitType(int id, int[] maxPersonalLimits, int[] maxTrialLimits)
	{
		this.id = id;
		personalLimits = maxPersonalLimits;
		trialLimits = maxTrialLimits;
	}
	
	public String value()
	{
		return name();
	}
	
	public int getId()
	{
		return id;
	}
	
	public int getObjectPlaceLimit(HouseType houseType)
	{
		return personalLimits[houseType.getLimitTypeIndex()];
	}
	
	public int getTrialObjectPlaceLimit(HouseType houseType)
	{
		return trialLimits[houseType.getLimitTypeIndex()];
	}
	
	public static LimitType fromValue(String value)
	{
		return valueOf(value);
	}
}