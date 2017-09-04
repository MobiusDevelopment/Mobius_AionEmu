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
package com.aionemu.gameserver.skillengine.effect;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SubEffect")
public class SubEffect
{
	@XmlAttribute(name = "skill_id", required = true)
	private int skillId;
	@XmlAttribute
	protected int chance = 100;
	
	@XmlAttribute(name = "addeffect")
	protected boolean addEffect = false;
	
	/**
	 * @return the skillId
	 */
	public int getSkillId()
	{
		return skillId;
	}
	
	/**
	 * @return the chance
	 */
	public int getChance()
	{
		return chance;
	}
	
	public boolean isAddEffect()
	{
		return addEffect;
	}
}
