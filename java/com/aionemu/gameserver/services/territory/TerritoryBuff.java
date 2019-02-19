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
package com.aionemu.gameserver.services.territory;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

public class TerritoryBuff implements StatOwner
{
	private final List<IStatFunction> functions = new ArrayList<>();
	
	public void applyEffect(Player player)
	{
		final int addvalue = 60;
		if (hasBuff())
		{
			endEffect(player);
		}
		functions.add(new StatAddFunction(StatEnum.PVP_DEFEND_RATIO, addvalue, true));
		player.getGameStats().addEffect(this, functions);
	}
	
	public boolean hasBuff()
	{
		return !functions.isEmpty();
	}
	
	public void endEffect(Player player)
	{
		functions.clear();
		player.getGameStats().endEffect(this);
	}
}