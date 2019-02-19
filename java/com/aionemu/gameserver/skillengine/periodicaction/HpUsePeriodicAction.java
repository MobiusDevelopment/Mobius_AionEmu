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
package com.aionemu.gameserver.skillengine.periodicaction;

import javax.xml.bind.annotation.XmlAttribute;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author antness
 */
public class HpUsePeriodicAction extends PeriodicAction
{
	
	@XmlAttribute(name = "value")
	protected int value;
	@XmlAttribute(name = "delta")
	protected int delta;
	
	@Override
	public void act(Effect effect)
	{
		final Creature effected = effect.getEffected();
		if (effected.getLifeStats().getCurrentHp() < value)
		{
			effect.endEffect();
			return;
		}
		effected.getLifeStats().reduceHp(value, effected);
	}
	
}
