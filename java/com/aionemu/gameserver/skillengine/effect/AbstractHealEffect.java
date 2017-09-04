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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.LOG;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS.TYPE;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.HealType;

/**
 * @author ATracer modified by Wakizashi, kecimis
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "AbstractHealEffect")
public abstract class AbstractHealEffect extends EffectTemplate
{
	@XmlAttribute
	protected boolean percent;
	
	public void calculate(Effect effect, HealType healType)
	{
		if (!super.calculate(effect, null, null))
		{
			return;
		}
		final Creature effector = effect.getEffector();
		
		final int valueWithDelta = value + (delta * effect.getSkillLevel());
		final int currentValue = getCurrentStatValue(effect);
		final int maxCurValue = getMaxStatValue(effect);
		int possibleHealValue = 0;
		if (percent)
		{
			possibleHealValue = (maxCurValue * valueWithDelta) / 100;
		}
		else
		{
			possibleHealValue = valueWithDelta;
		}
		
		int finalHeal = possibleHealValue;
		
		if (healType == HealType.HP)
		{
			final int baseHeal = possibleHealValue;
			if (effect.getItemTemplate() == null)
			{
				final int boostHealAdd = effector.getGameStats().getStat(StatEnum.HEAL_BOOST, 0).getCurrent();
				// Apply percent Heal Boost bonus (ex. Passive skills)
				int boostHeal = (effector.getGameStats().getStat(StatEnum.HEAL_BOOST, baseHeal).getCurrent() - boostHealAdd);
				// Apply Add Heal Boost bonus (ex. Skills like Benevolence)
				boostHeal += (boostHeal * boostHealAdd) / 1000;
				finalHeal = effector.getGameStats().getStat(StatEnum.HEAL_SKILL_BOOST, boostHeal).getCurrent();
			}
			finalHeal = effect.getEffected().getGameStats().getStat(StatEnum.HEAL_SKILL_DEBOOST, finalHeal).getCurrent();
			// finalHeal = effector.getGameStats().getStat(StatEnum.HEAL_SKILL_DEBOOST, finalHeal).getCurrent();
		}
		
		if (finalHeal < 0)
		{
			finalHeal = currentValue > -finalHeal ? finalHeal : -currentValue;
		}
		else
		{
			finalHeal = (maxCurValue - currentValue) < finalHeal ? (maxCurValue - currentValue) : finalHeal;
		}
		
		if ((healType == HealType.HP) && effect.getEffected().getEffectController().isAbnormalSet(AbnormalState.DISEASE))
		{
			finalHeal = 0;
		}
		
		effect.setReservedInt(position, finalHeal);
		effect.setReserved1(-finalHeal);
	}
	
	public void applyEffect(Effect effect, HealType healType)
	{
		final Creature effected = effect.getEffected();
		final int healValue = effect.getReservedInt(position);
		
		if (healValue == 0)
		{
			return;
		}
		
		switch (healType)
		{
			case HP:
				if ((this instanceof ProcHealInstantEffect) || effect.getSkillTemplate().getEffects().isEffectTypePresent(EffectType.HEALINSTANT))// item heal, eg potions
				{
					effected.getLifeStats().increaseHp(TYPE.HP, healValue, 0, LOG.REGULAR);
				}
				else // TODO shouldnt send value, on retail sm_attack_status is send only to update hp bar
				if (healValue > 0)
				{
					effected.getLifeStats().increaseHp(TYPE.REGULAR, healValue, 0, LOG.REGULAR);
				}
				else
				{
					effected.getLifeStats().reduceHp(-healValue, effected);
				}
				break;
			case MP:
				if (this instanceof ProcMPHealInstantEffect)// item heal, eg potions
				{
					effected.getLifeStats().increaseMp(TYPE.MP, healValue, 0, LOG.REGULAR);
				}
				else
				{
					effected.getLifeStats().increaseMp(TYPE.HEAL_MP, healValue, 0, LOG.REGULAR);
				}
				break;
			case FP:
				effected.getLifeStats().increaseFp(TYPE.FP, healValue);
				break;
			case DP:
				((Player) effected).getCommonData().addDp(healValue);
				break;
		}
	}
	
	protected abstract int getCurrentStatValue(Effect effect);
	
	protected abstract int getMaxStatValue(Effect effect);
}