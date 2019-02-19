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
package com.aionemu.gameserver.model.templates.instance_bonusatrr;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "InstanceBonusAttr", propOrder =
{
	"penaltyAttr"
})
public class InstanceBonusAttr
{
	@XmlElement(name = "penalty_attr")
	protected List<InstancePenaltyAttr> penaltyAttr;
	
	@XmlAttribute(name = "buff_id", required = true)
	protected int buffId;
	
	public List<InstancePenaltyAttr> getPenaltyAttr()
	{
		if (penaltyAttr == null)
		{
			penaltyAttr = new ArrayList<>();
		}
		return penaltyAttr;
	}
	
	public int getBuffId()
	{
		return buffId;
	}
	
	public void setBuffId(int value)
	{
		buffId = value;
	}
}