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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatRateFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatSetFunction;
import com.aionemu.gameserver.model.stats.container.CreatureGameStats;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.change.Change;
import com.aionemu.gameserver.skillengine.condition.Conditions;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BuffEffect")
public abstract class BuffEffect extends EffectTemplate
{
	@XmlAttribute
	protected boolean maxstat;
	
	private static final Logger log = LoggerFactory.getLogger(BuffEffect.class);
	
	@Override
	public void applyEffect(Effect effect)
	{
		effect.addToEffectedController();
	}
	
	/**
	 * Will be called from effect controller when effect ends
	 */
	@Override
	public void endEffect(Effect effect)
	{
		final Creature effected = effect.getEffected();
		effected.getGameStats().endEffect(effect);
	}
	
	/**
	 * Will be called from effect controller when effect starts
	 */
	@Override
	public void startEffect(Effect effect)
	{
		if (change == null)
		{
			return;
		}
		
		final Creature effected = effect.getEffected();
		final CreatureGameStats<? extends Creature> cgs = effected.getGameStats();
		
		final List<IStatFunction> modifiers = getModifiers(effect);
		
		if (modifiers.size() > 0)
		{
			cgs.addEffect(effect, modifiers);
		}
		
		if (maxstat)
		{
			effected.getLifeStats().increaseHp(TYPE.HP, effected.getGameStats().getMaxHp().getCurrent());
			effected.getLifeStats().increaseMp(TYPE.HEAL_MP, effected.getGameStats().getMaxMp().getCurrent());
		}
	}
	
	/**
	 * @param effect
	 * @return
	 */
	protected List<IStatFunction> getModifiers(Effect effect)
	{
		final int skillId = effect.getSkillId();
		final int skillLvl = effect.getSkillLevel();
		
		final List<IStatFunction> modifiers = new ArrayList<>();
		
		for (Change changeItem : change)
		{
			if (changeItem.getStat() == null)
			{
				log.warn("Skill stat has wrong name for skillid: " + skillId);
				continue;
			}
			
			final int valueWithDelta = changeItem.getValue() + (changeItem.getDelta() * skillLvl);
			
			final Conditions conditions = changeItem.getConditions();
			switch (changeItem.getFunc())
			{
				case ADD:
				{
					modifiers.add(new StatAddFunction(changeItem.getStat(), valueWithDelta, true).withConditions(conditions));
					break;
				}
				case PERCENT:
				{
					modifiers.add(new StatRateFunction(changeItem.getStat(), valueWithDelta, true).withConditions(conditions));
					break;
				}
				case REPLACE:
				{
					modifiers.add(new StatSetFunction(changeItem.getStat(), valueWithDelta, true).withConditions(conditions));
					break;
				}
			}
		}
		return modifiers;
	}
	
	@Override
	public void onPeriodicAction(Effect effect)
	{
		// TODO Auto-generated method stub
	}
}
