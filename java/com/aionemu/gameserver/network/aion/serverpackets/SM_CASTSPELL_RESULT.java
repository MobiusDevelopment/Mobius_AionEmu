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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.skillengine.model.Skill;

public class SM_CASTSPELL_RESULT extends AionServerPacket
{
	private final Creature effector;
	private final Creature target;
	private final Skill skill;
	private int cooldown;
	private final int hitTime;
	private final List<Effect> effects;
	private final int spellStatus;
	private final int dashStatus;
	private int targetType;
	private final boolean chainSuccess;
	
	public SM_CASTSPELL_RESULT(Skill skill, List<Effect> effects, int hitTime, boolean chainSuccess, int spellStatus, int dashStatus)
	{
		this.skill = skill;
		effector = skill.getEffector();
		target = skill.getFirstTarget();
		this.effects = effects;
		cooldown = effector.getSkillCooldown(skill.getSkillTemplate());
		this.spellStatus = spellStatus;
		this.chainSuccess = chainSuccess;
		targetType = 0;
		this.hitTime = hitTime;
		this.dashStatus = dashStatus;
	}
	
	public SM_CASTSPELL_RESULT(Skill skill, List<Effect> effects, int hitTime, boolean chainSuccess, int spellStatus, int dashStatus, int targetType)
	{
		this(skill, effects, hitTime, chainSuccess, spellStatus, dashStatus);
		this.targetType = targetType;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(effector.getObjectId());
		writeC(targetType);
		switch (targetType)
		{
			case 0:
			case 3:
			case 4:
			{
				writeD(target.getObjectId());
				break;
			}
			case 1:
			{
				writeF(skill.getX());
				writeF(skill.getY());
				writeF(skill.getZ());
				break;
			}
			case 2:
			{
				writeF(skill.getX());
				writeF(skill.getY());
				writeF(skill.getZ());
				writeF(0);
				writeF(0);
				writeF(0);
				writeF(0);
				writeF(0);
				writeF(0);
				writeF(0);
				writeF(0);
				break;
			}
		}
		writeH(skill.getSkillTemplate().getSkillId());
		writeC(skill.getSkillTemplate().getLvl());
		cooldown = skill.StigmaEnchantCoolDown(skill, cooldown);
		writeD(cooldown);
		writeH(hitTime);
		writeC(0);
		
		if (effects.isEmpty())
		{
			writeH(16);
		}
		else if (chainSuccess && (skill.getChainCategory() != null))
		{
			writeH(32);
		}
		else if (skill.getChainCategory() == null)
		{
			writeH(160);
		}
		else
		{
			writeH(0);
		}
		writeC(dashStatus);
		switch (dashStatus)
		{
			case 1:
			case 2:
			case 3:
			case 4:
			case 6:
			{
				writeC(skill.getH());
				writeF(skill.getX());
				writeF(skill.getY());
				writeF(skill.getZ());
				break;
			}
			default:
			{
				break;
			}
		}
		writeH(effects.size());
		for (Effect effect : effects)
		{
			final Creature effected = effect.getEffected();
			if (effected != null)
			{
				writeD(effected.getObjectId());
				writeC(effect.getEffectResult().getId());
				writeC((int) ((100f * effected.getLifeStats().getCurrentHp()) / target.getLifeStats().getMaxHp()));
			}
			else
			{
				writeD(0);
				writeC(0);
				writeC(0);
			}
			writeC((int) ((100f * effector.getLifeStats().getCurrentHp()) / effector.getLifeStats().getMaxHp()));
			
			writeC(spellStatus);
			writeC(effect.getSkillMoveType().getId());
			writeH(0);
			writeC(effect.getCarvedSignet());
			switch (spellStatus)
			{
				case 1:
				case 2:
				case 4:
				case 8:
				{
					writeF(effect.getTargetX());
					writeF(effect.getTargetY());
					writeF(effect.getTargetZ());
					break;
				}
				case 16:
				case 3:
				{
					writeC(effect.getEffector().getHeading());
					break;
				}
				default:
				{
					switch (effect.getSkillMoveType())
					{
						case PULL:
						case KNOCKBACK:
						{
							writeF(effect.getTargetX());
							writeF(effect.getTargetY());
							writeF(effect.getTargetZ());
						}
						default:
						{
							break;
						}
					}
					break;
				}
			}
			writeC(1);
			{
				writeC(effect.isMphealInstant() ? 1 : 0);
				if (effect.isDelayedDamage())
				{
					writeD(0);
				}
				else
				{
					writeD(effect.getReserved1());
				}
				writeC(effect.getAttackStatus().getId());
				if (effect.getEffected() instanceof Player)
				{
					if (effect.getAttackStatus().isCounterSkill())
					{
						((Player) effect.getEffected()).setLastCounterSkill(effect.getAttackStatus());
					}
				}
				writeC(effect.getShieldDefense());
				
				switch (effect.getShieldDefense())
				{
					case 0:
					case 2:
					{
						break;
					}
					case 8:
					case 10:
					{
						writeD(effect.getMpShield());
						writeD(effect.getProtectorId());
						writeD(effect.getProtectedDamage());
						writeD(effect.getProtectedSkillId());
						break;
					}
					default:
					{
						writeD(effect.getProtectorId());
						writeD(effect.getProtectedDamage());
						writeD(effect.getProtectedSkillId());
						writeD(effect.getReflectedDamage());
						writeD(effect.getReflectedSkillId());
						break;
					}
				}
			}
		}
	}
}