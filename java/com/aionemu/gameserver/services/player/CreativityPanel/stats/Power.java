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
package com.aionemu.gameserver.services.player.CreativityPanel.stats;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatAddFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;

/**
 * @author Ranastic (Encom)
 */
public class Power implements StatOwner
{
	private final List<IStatFunction> power = new ArrayList<>();
	
	public void onChange(Player player, int point)
	{
		if (point >= 1)
		{
			power.clear();
			player.getGameStats().endEffect(this);
			power.add(new StatAddFunction(StatEnum.PHYSICAL_ATTACK, (int) (1.5f * point), true));
			power.add(new StatAddFunction(StatEnum.PHYSICAL_DEFENSE, (int) (7.5f * point), true));
			player.getGameStats().addEffect(this, power);
		}
		else if (point == 0)
		{
			power.clear();
			power.add(new StatAddFunction(StatEnum.PHYSICAL_ATTACK, (int) (1.5f * point), false));
			power.add(new StatAddFunction(StatEnum.PHYSICAL_DEFENSE, (int) (7.5f * point), false));
			player.getGameStats().endEffect(this);
		}
	}
	
	public static Power getInstance()
	{
		return NewSingletonHolder.INSTANCE;
	}
	
	private static class NewSingletonHolder
	{
		static final Power INSTANCE = new Power();
	}
}