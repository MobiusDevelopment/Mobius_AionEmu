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

import com.aionemu.gameserver.model.templates.CubeExpandTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * This is for the Cube Expanders.
 * @author dragoon112
 */
@XmlRootElement(name = "cube_expander")
@XmlAccessorType(XmlAccessType.FIELD)
public class CubeExpandData
{
	@XmlElement(name = "cube_npc")
	private List<CubeExpandTemplate> clist;
	private final TIntObjectHashMap<CubeExpandTemplate> npctlistData = new TIntObjectHashMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		for (CubeExpandTemplate npc : clist)
		{
			npctlistData.put(npc.getNpcId(), npc);
		}
	}
	
	public int size()
	{
		return npctlistData.size();
	}
	
	public CubeExpandTemplate getCubeExpandListTemplate(int id)
	{
		return npctlistData.get(id);
	}
}
