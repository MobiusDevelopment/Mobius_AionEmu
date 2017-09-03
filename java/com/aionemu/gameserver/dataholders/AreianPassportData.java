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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.event.AtreianPassport;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Ranastic
 */
@XmlRootElement(name = "login_events")
@XmlAccessorType(XmlAccessType.FIELD)
public class AreianPassportData
{
	
	@XmlElement(name = "login_event")
	private List<AtreianPassport> tlist;
	
	@XmlTransient
	private final TIntObjectHashMap<AtreianPassport> passportData = new TIntObjectHashMap<>();
	
	@XmlTransient
	private final Map<Integer, AtreianPassport> passportDataMap = new HashMap<>(1);
	
	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject)
	{
		for (final AtreianPassport atreianPassport : tlist)
		{
			passportData.put(atreianPassport.getId(), atreianPassport);
			passportDataMap.put(atreianPassport.getId(), atreianPassport);
		}
	}
	
	public int size()
	{
		return passportData.size();
	}
	
	public AtreianPassport getAtreianPassportId(int id)
	{
		return passportData.get(id);
	}
	
	public Map<Integer, AtreianPassport> getAll()
	{
		return passportDataMap;
	}
}