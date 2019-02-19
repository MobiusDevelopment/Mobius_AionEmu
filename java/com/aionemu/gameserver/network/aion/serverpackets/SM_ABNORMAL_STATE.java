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

import java.util.Collection;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;

/**
 * @author Avol, ATracer
 */
public class SM_ABNORMAL_STATE extends AionServerPacket
{
	
	private final Collection<Effect> effects;
	private final int abnormals;
	
	public SM_ABNORMAL_STATE(Collection<Effect> effects, int abnormals)
	{
		this.effects = effects;
		this.abnormals = abnormals;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(abnormals);
		writeD(0);
		writeD(0);// unk 4.5
		writeC(0x7F);// unk 4.5 what's that? O.o
		writeH(effects.size());
		
		for (Effect effect : effects)
		{
			writeD(effect.getEffectorId());
			writeH(effect.getSkillId());
			writeC(effect.getSkillLevel());
			writeC(effect.getTargetSlot());
			writeD(effect.getRemainingTime());
		}
	}
}
