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

import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.templates.base.BaseTemplate;

import javolution.util.FastMap;

/**
 * @author Source
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "base_locations")
public class BaseData
{
	@XmlElement(name = "base_location")
	private List<BaseTemplate> baseTemplates;
	@XmlTransient
	private final FastMap<Integer, BaseLocation> base = new FastMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (BaseTemplate template : baseTemplates)
		{
			base.put(template.getId(), new BaseLocation(template));
		}
	}
	
	public int size()
	{
		return base.size();
	}
	
	public FastMap<Integer, BaseLocation> getBaseLocations()
	{
		return base;
	}
}