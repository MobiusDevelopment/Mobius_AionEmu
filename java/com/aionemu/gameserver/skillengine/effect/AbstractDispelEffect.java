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

import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.SkillTargetSlot;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractDispelEffect")
public class AbstractDispelEffect extends EffectTemplate
{
	
	@XmlAttribute
	protected int dpower;
	
	@XmlAttribute
	protected int power;
	
	@XmlAttribute(name = "dispel_level")
	protected int dispelLevel;
	
	@Override
	public void applyEffect(Effect effect)
	{
	}
	
	public void applyEffect(Effect effect, DispelCategoryType type, SkillTargetSlot slot)
	{
		final boolean isItemTriggered = effect.getItemTemplate() != null;
		final int count = value + (delta * effect.getSkillLevel());
		final int finalPower = power + (dpower * effect.getSkillLevel());
		
		effect.getEffected().getEffectController().removeEffectByDispelCat(type, slot, count, dispelLevel, finalPower, isItemTriggered);
	}
}
