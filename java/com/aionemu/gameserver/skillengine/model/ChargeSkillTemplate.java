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
package com.aionemu.gameserver.skillengine.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Dr.Nism
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "charge_skill")
public class ChargeSkillTemplate
{
	@XmlAttribute
	private int id;
	
	@XmlAttribute(name = "min_charge")
	private int minCharge;
	
	@XmlElement(name = "charge")
	private List<ChargeTemplate> charges;
	
	@XmlAttribute(name = "bonus_type", required = true)
	protected BonusChargeType type = BonusChargeType.NONE;
	
	/**
	 * @return the Id
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * @return the MinCharge
	 */
	public int getMinCharge()
	{
		return minCharge;
	}
	
	/**
	 * @return the Charges
	 */
	public List<ChargeTemplate> getCharges()
	{
		return charges;
	}
	
	/**
	 * @return the BonusChargeType
	 */
	public BonusChargeType getBonusChargeType()
	{
		return type;
	}
}