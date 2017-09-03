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

import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.legiondominion.LegionDominionLocation;
import com.aionemu.gameserver.model.templates.legiondominion.LegionDominionTemplate;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dominion_locations")
public class LegionDominionData
{
	@XmlElement(name = "dominion_location")
	private List<LegionDominionTemplate> legionDominionTemplates;
	
	@XmlTransient
	private final FastMap<Integer, LegionDominionLocation> legionDominion = new FastMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (final LegionDominionTemplate template : legionDominionTemplates)
		{
			legionDominion.put(template.getLegionDominionId(), new LegionDominionLocation(template));
		}
	}
	
	public int size()
	{
		return legionDominion.size();
	}
	
	public FastMap<Integer, LegionDominionLocation> getLegionDominionLocations()
	{
		return legionDominion;
	}
}