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
package com.aionemu.gameserver.dataholders.loadingutils.adapters;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import com.aionemu.gameserver.model.items.NpcEquippedGear;

/**
 * @author Luno
 */
public class NpcEquippedGearAdapter extends XmlAdapter<NpcEquipmentList, NpcEquippedGear>
{
	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#marshal(java.lang.Object)
	 */
	@Override
	public NpcEquipmentList marshal(NpcEquippedGear v)
	{
		return null;
	}
	
	/*
	 * (non-Javadoc)
	 * @see javax.xml.bind.annotation.adapters.XmlAdapter#unmarshal(java.lang.Object)
	 */
	@Override
	public NpcEquippedGear unmarshal(NpcEquipmentList v)
	{
		return new NpcEquippedGear(v);
	}
}
