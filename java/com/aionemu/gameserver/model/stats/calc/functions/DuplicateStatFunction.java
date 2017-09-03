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
package com.aionemu.gameserver.model.stats.calc.functions;

import static ch.lambdaj.Lambda.forEach;
import static ch.lambdaj.Lambda.on;
import static ch.lambdaj.Lambda.selectMax;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;

class DuplicateStatFunction extends StatFunction
{
	@Override
	public void apply(Stat2 stat)
	{
		final Item mainWeapon = ((Player) stat.getOwner()).getEquipment().getMainHandWeapon();
		final Item offWeapon = ((Player) stat.getOwner()).getEquipment().getOffHandWeapon();
		if (mainWeapon != null)
		{
			StatFunction func1 = null;
			StatFunction func2 = null;
			final List<StatFunction> functions = new ArrayList<>();
			final List<StatFunction> functions1 = mainWeapon.getItemTemplate().getModifiers();
			if (functions1 != null)
			{
				final List<StatFunction> f1 = getFunctions(functions1, stat, mainWeapon);
				if (!f1.isEmpty())
				{
					func1 = f1.get(0);
					functions.addAll(f1);
				}
			}
			if (mainWeapon.hasFusionedItem())
			{
				final ItemTemplate template = mainWeapon.getFusionedItemTemplate();
				final List<StatFunction> functions2 = template.getModifiers();
				if (functions2 != null)
				{
					final List<StatFunction> f2 = getFunctions(functions2, stat, mainWeapon);
					if (!f2.isEmpty())
					{
						func2 = f2.get(0);
						functions.addAll(f2);
					}
				}
			}
			else if (offWeapon != null)
			{
				final List<StatFunction> functions2 = offWeapon.getItemTemplate().getModifiers();
				if (functions2 != null)
				{
					functions.addAll(getFunctions(functions2, stat, offWeapon));
				}
			}
			if ((func1 != null) && (func2 != null))
			{
				if (Math.abs(func1.getValue()) >= Math.abs(func2.getValue()))
				{
					functions.remove(func2);
				}
				else
				{
					functions.remove(func1);
				}
			}
			if (!functions.isEmpty())
			{
				if ((getName() == StatEnum.PVP_ATTACK_RATIO) || (getName() == StatEnum.PVP_DEFEND_RATIO))
				{
					forEach(functions).apply(stat);
				}
				else
				{
					((StatFunction) selectMax(functions, on(StatFunction.class).getValue())).apply(stat);
				}
				functions.clear();
			}
		}
	}
	
	private List<StatFunction> getFunctions(List<StatFunction> list, Stat2 stat, Item item)
	{
		final List<StatFunction> functions = new ArrayList<>();
		for (final StatFunction func : list)
		{
			final StatFunctionProxy func2 = new StatFunctionProxy(item, func);
			if ((func.getName() == getName()) && func2.validate(stat, func2))
			{
				functions.add(func);
			}
		}
		return functions;
	}
	
	@Override
	public int getPriority()
	{
		return 60;
	}
}
