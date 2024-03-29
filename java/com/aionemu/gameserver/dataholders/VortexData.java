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

import com.aionemu.gameserver.model.templates.vortex.VortexTemplate;
import com.aionemu.gameserver.model.vortex.VortexLocation;

import javolution.util.FastMap;

/**
 * @author Source
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "dimensional_vortex")
public class VortexData
{
	@XmlElement(name = "vortex_location")
	private List<VortexTemplate> vortexTemplates;
	@XmlTransient
	private final FastMap<Integer, VortexLocation> vortex = new FastMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (VortexTemplate template : vortexTemplates)
		{
			vortex.put(template.getId(), new VortexLocation(template));
		}
	}
	
	public int size()
	{
		return vortex.size();
	}
	
	public VortexLocation getVortexLocation(int invasionWorldId)
	{
		for (VortexLocation loc : vortex.values())
		{
			if (loc.getInvasionWorldId() == invasionWorldId)
			{
				return loc;
			}
		}
		return null;
	}
	
	public FastMap<Integer, VortexLocation> getVortexLocations()
	{
		return vortex;
	}
	
}