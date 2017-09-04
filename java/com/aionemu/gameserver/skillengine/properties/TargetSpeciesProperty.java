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

import java.util.Iterator;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Skill;

public class TargetSpeciesProperty
{
	
	public static boolean set(Skill skill, Properties properties)
	{
		final TargetSpeciesAttribute value = properties.getTargetSpecies();
		
		final List<Creature> effectedList = skill.getEffectedList();
		switch (value)
		{
			case NPC:
			{
				for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();)
				{
					final Creature nextEffected = iter.next();
					if (!(nextEffected instanceof Npc))
					{
						iter.remove();
					}
				}
				break;
			}
			case PC:
			{
				for (Iterator<Creature> iter = effectedList.iterator(); iter.hasNext();)
				{
					final Creature nextEffected = iter.next();
					if (!(nextEffected instanceof Player))
					{
						iter.remove();
					}
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
