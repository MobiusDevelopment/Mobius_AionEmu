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

import com.aionemu.gameserver.controllers.attack.AttackResult;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author -Nemesiss-, Sweetkr
 * @author GiGatR00n v4.7.5.x
 */
public class SM_ATTACK extends AionServerPacket
{
	private final int attackno;
	private final int time;
	private final int type;
	private final int SimpleAttackType;
	private final List<AttackResult> attackList;
	private final Creature attacker;
	private final Creature target;
	
	public SM_ATTACK(Creature attacker, Creature target, int attackno, int time, int type, List<AttackResult> attackList)
	{
		this.attacker = attacker;
		this.target = target;
		this.attackno = attackno;
		this.time = time;
		this.type = type;
		this.attackList = attackList;
		SimpleAttackType = attacker.getController().getSimpleAttackType();
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(attacker.getObjectId());
		writeC(attackno);
		writeH(time);
		writeC((byte) SimpleAttackType);
		writeC(type);
		writeD(target.getObjectId());
		final int attackerMaxHp = attacker.getLifeStats().getMaxHp();
		final int attackerCurrHp = attacker.getLifeStats().getCurrentHp();
		final int targetMaxHp = target.getLifeStats().getMaxHp();
		final int targetCurrHp = target.getLifeStats().getCurrentHp();
		writeC((int) ((100f * targetCurrHp) / targetMaxHp));
		writeC((int) ((100f * attackerCurrHp) / attackerMaxHp));
		switch (attackList.get(0).getAttackStatus().getId())
		{
			case 196:
			case 4:
			case 5:
			case 213:
			{
				writeH(32);
				break;
			}
			case 194:
			case 2:
			case 3:
			case 211:
			{
				writeH(64);
				break;
			}
			case 192:
			case 0:
			case 1:
			case 209:
			{
				writeH(128);
				break;
			}
			case 198:
			case 6:
			case 7:
			case 215:
			{
				writeH(256);
				break;
			}
			default:
			{
				writeH(0);
				break;
			}
		}
		if (target instanceof Player)
		{
			if (attackList.get(0).getAttackStatus().isCounterSkill())
			{
				((Player) target).setLastCounterSkill(attackList.get(0).getAttackStatus());
			}
		}
		writeH(0);
		writeC(attackList.size());
		for (AttackResult attack : attackList)
		{
			writeD(attack.getDamage());
			writeC(attack.getAttackStatus().getId());
			final byte shieldType = (byte) attack.getShieldType();
			writeC(shieldType);
			switch (shieldType)
			{
				case 0:
				case 2:
				{
					break;
				}
				case 8:
				case 10:
				{
					writeD(attack.getShieldMp());
					writeD(attack.getProtectorId());
					writeD(attack.getProtectedDamage());
					writeD(attack.getProtectedSkillId());
					break;
				}
				default:
				{
					writeD(attack.getProtectorId());
					writeD(attack.getProtectedDamage());
					writeD(attack.getProtectedSkillId());
					writeD(attack.getReflectedDamage());
					writeD(attack.getReflectedSkillId());
					writeD(0);
					writeD(0);
					break;
				}
			}
		}
		writeC(0);
	}
}