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
package com.aionemu.gameserver.model.templates.item.bonuses;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.stats.ModifiersTemplate;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "RandomBonus", propOrder =
{
	"modifiers"
})
public class RandomBonus
{
	@XmlElement(required = true)
	protected List<ModifiersTemplate> modifiers;
	
	@XmlAttribute(required = true)
	protected int id;
	
	@XmlAttribute(name = "type", required = true)
	private StatBonusType bonusType;
	
	public List<ModifiersTemplate> getModifiers()
	{
		if (modifiers == null)
		{
			modifiers = new ArrayList<>();
		}
		return modifiers;
	}
	
	/**
	 * Gets the value of the id property.
	 * @return
	 */
	public int getId()
	{
		return id;
	}
	
	public StatBonusType getBonusType()
	{
		return bonusType;
	}
}