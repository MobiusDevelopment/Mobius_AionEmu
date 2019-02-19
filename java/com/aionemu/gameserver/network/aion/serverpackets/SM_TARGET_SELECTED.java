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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Dr.Nism
 */
public class SM_TARGET_SELECTED extends AionServerPacket
{
	private int level;
	private int maxHp;
	private int currentHp;
	private int maxMp;
	private int currentMp;
	private int targetObjId;
	
	public SM_TARGET_SELECTED(Player player)
	{
		if (player != null)
		{
			if (player.getTarget() instanceof Player)
			{
				final Player pl = (Player) player.getTarget();
				level = pl.getLevel();
				maxHp = pl.getLifeStats().getMaxHp();
				currentHp = pl.getLifeStats().getCurrentHp();
				maxMp = pl.getLifeStats().getMaxMp();
				currentMp = pl.getLifeStats().getCurrentMp();
			}
			else if (player.getTarget() instanceof Creature)
			{
				final Creature creature = (Creature) player.getTarget();
				level = creature.getLevel();
				maxHp = creature.getLifeStats().getMaxHp();
				currentHp = creature.getLifeStats().getCurrentHp();
				maxMp = creature.getLifeStats().getMaxMp();
				currentMp = creature.getLifeStats().getCurrentMp();
			}
			else
			{
				// TODO: check various gather on retail
				level = 0;
				maxHp = 0;
				currentHp = 0;
				maxMp = 0;
				currentMp = 0;
			}
			
			if (player.getTarget() != null)
			{
				targetObjId = player.getTarget().getObjectId();
			}
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeD(targetObjId);
		writeH(level);
		writeD(maxHp);
		writeD(currentHp);
		writeD(maxMp);
		writeD(currentMp);
	}
	
}