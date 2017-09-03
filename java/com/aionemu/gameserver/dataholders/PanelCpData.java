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

import com.aionemu.gameserver.model.templates.panel_cp.PanelCp;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rinzler (Encom)
 */

@XmlRootElement(name = "panel_cps")
@XmlAccessorType(XmlAccessType.FIELD)
public class PanelCpData
{
	@XmlElement(name = "panel_cp")
	private List<PanelCp> pclist;
	
	@XmlTransient
	private final TIntObjectHashMap<PanelCp> cpData = new TIntObjectHashMap<>();
	
	@XmlTransient
	private final Map<Integer, PanelCp> cpDataMap = new HashMap<>(1);
	
	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject)
	{
		for (final PanelCp panelCp : pclist)
		{
			cpData.put(panelCp.getId(), panelCp);
			cpDataMap.put(panelCp.getId(), panelCp);
		}
	}
	
	public int size()
	{
		return cpData.size();
	}
	
	public PanelCp getPanelCpId(int id)
	{
		return cpData.get(id);
	}
	
	public Map<Integer, PanelCp> getAll()
	{
		return cpDataMap;
	}
}