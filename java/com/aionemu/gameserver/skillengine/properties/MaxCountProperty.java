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
package com.aionemu.gameserver.skillengine.properties;

import java.util.SortedMap;
import java.util.TreeMap;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author MrPoke
 */
public class MaxCountProperty
{
	
	public static boolean set(Skill skill, Properties properties)
	{
		final TargetRangeAttribute value = properties.getTargetType();
		final int maxcount = properties.getTargetMaxCount();
		
		switch (value)
		{
			case AREA:
			{
				int areaCounter = 0;
				final Creature firstTarget = skill.getFirstTarget();
				if (firstTarget == null)
				{
					return false;
				}
				final SortedMap<Double, Creature> sortedMap = new TreeMap<>();
				for (Creature creature : skill.getEffectedList())
				{
					sortedMap.put(MathUtil.getDistance(firstTarget, creature), creature);
				}
				skill.getEffectedList().clear();
				for (Creature creature : sortedMap.values())
				{
					if (areaCounter >= maxcount)
					{
						break;
					}
					skill.getEffectedList().add(creature);
					areaCounter++;
				}
			}
			default:
			{
				break;
			}
		}
		return true;
	}
}
