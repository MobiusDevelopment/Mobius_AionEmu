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

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author alexa026
 * @author ATracer
 * @author kecimis
 */
public class SM_ATTACK_STATUS extends AionServerPacket
{
	private final Creature creature;
	private final TYPE type;
	private final int skillId;
	private final int value;
	private final int logId;
	
	public static enum TYPE
	{
		NATURAL_HP(3),
		USED_HP(4),
		REGULAR(5),
		ABSORBED_HP(6),
		DAMAGE(7),
		HP(7),
		PROTECTDMG(8),
		DELAYDAMAGE(10),
		FALL_DAMAGE(17),
		HEAL_MP(19),
		ABSORBED_MP(20),
		MP(21),
		NATURAL_MP(22),
		FP_RINGS(23),
		FP(25),
		NATURAL_FP(26),
		AUTO_HEAL_FP(27);
		
		private int value;
		
		private TYPE(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
	public static enum LOG
	{
		SPELLATK(1),
		HEAL(3),
		MPHEAL(4),
		SKILLLATKDRAININSTANT(23),
		SPELLATKDRAININSTANT(24),
		POISON(25),
		BLEED(26),
		PROCATKINSTANT(92),
		DELAYEDSPELLATKINSTANT(95),
		SPELLATKDRAIN(130),
		FPHEAL(133),
		REGULARHEAL(170),
		REGULAR(189),
		ATTACK(193);// old 190 new 193
		
		private int value;
		
		private LOG(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
	}
	
	public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value, LOG log)
	{
		this.creature = creature;
		this.type = type;
		this.skillId = skillId;
		this.value = value;
		logId = log.getValue();
	}
	
	public SM_ATTACK_STATUS(Creature creature, TYPE type, int skillId, int value)
	{
		this(creature, type, skillId, value, LOG.REGULAR);
	}
	
	public SM_ATTACK_STATUS(Creature creature, int value)
	{
		this(creature, TYPE.REGULAR, 0, value, LOG.REGULAR);
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(creature.getObjectId());
		switch (type)
		{
			case DAMAGE:
			case DELAYDAMAGE:
			{
				writeD(-value);
				break;
			}
			default:
			{
				writeD(value);
			}
		}
		writeD(0x00);// 5.0
		writeC(type.getValue());
		writeC(creature.getLifeStats().getHpPercentage());
		writeH(skillId);
		writeH(logId);
	}
}