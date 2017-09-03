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
package com.aionemu.gameserver.model.templates.challenge;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlType(name = "ChallengeType")
@XmlEnum
public enum ChallengeType
{
	LEGION(1),
	TOWN(2);
	
	private int id;
	
	public int getId()
	{
		return id;
	}
	
	private ChallengeType(int id)
	{
		this.id = id;
	}
	
	public String value()
	{
		return name();
	}
	
	public static ChallengeType fromValue(String paramString)
	{
		return valueOf(paramString);
	}
}