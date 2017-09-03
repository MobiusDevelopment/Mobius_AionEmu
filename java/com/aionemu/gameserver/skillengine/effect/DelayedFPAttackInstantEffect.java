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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Sippolo
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "DelayedFPAttackInstantEffect")
public class DelayedFPAttackInstantEffect extends EffectTemplate
{
	
	@XmlAttribute
	protected int delay;
	@XmlAttribute
	protected boolean percent;
	
	@Override
	public void calculate(Effect effect)
	{
		if (!(effect.getEffected() instanceof Player))
		{
			return;
		}
		if (!super.calculate(effect, null, null))
		{
			return;
		}
		
		final int maxFP = ((Player) effect.getEffected()).getLifeStats().getMaxFp();
		final int newValue = (percent) ? (int) ((maxFP * value) / 100) : value;
		
		effect.setReserved2(newValue);
	}
	
	@Override
	public void applyEffect(Effect effect)
	{
		final Player effected = (Player) effect.getEffected();
		final int newValue = effect.getReserved2();
		
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				effected.getLifeStats().reduceFp(newValue);
			}
		}, delay);
	}
}
