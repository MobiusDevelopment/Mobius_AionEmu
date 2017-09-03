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

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatArmorMasteryFunction;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ArmorMasteryEffect")
public class ArmorMasteryEffect extends BuffEffect
{
	
	@XmlAttribute(name = "armor")
	private ArmorType armorType;
	
	@Override
	public void startEffect(Effect effect)
	{
		if (change == null)
		{
			return;
		}
		
		final List<IStatFunction> modifiers = getModifiers(effect);
		final List<IStatFunction> masteryModifiers = new ArrayList<>(modifiers.size());
		for (final IStatFunction modifier : modifiers)
		{
			masteryModifiers.add(new StatArmorMasteryFunction(armorType, modifier.getName(), modifier.getValue(), modifier.isBonus()));
		}
		if (masteryModifiers.size() > 0)
		{
			effect.getEffected().getGameStats().addEffect(effect, masteryModifiers);
		}
	}
}
