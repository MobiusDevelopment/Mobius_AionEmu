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
package com.aionemu.gameserver.skillengine.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.aionemu.gameserver.model.gameobjects.player.Player;

import javolution.util.FastMap;

/**
 * @author kecimis
 */
public class ChainSkills
{
	private final Map<String, ChainSkill> multiSkills = new FastMap<>();
	private final ChainSkill chainSkill = new ChainSkill("", 0, 0);
	
	public int getChainCount(Player player, SkillTemplate template, String category)
	{
		if (category == null)
		{
			return 0;
		}
		final long nullTime = player.getSkillCoolDown(template.getDelayId());
		if (multiSkills.get(category) != null)
		{
			if ((System.currentTimeMillis() >= nullTime) && (multiSkills.get(category).getUseTime() <= nullTime))
			{
				multiSkills.get(category).setChainCount(0);
			}
			return multiSkills.get(category).getChainCount();
		}
		return 0;
	}
	
	public long getLastChainUseTime(String category)
	{
		if (multiSkills.get(category) != null)
		{
			return multiSkills.get(category).getUseTime();
		}
		else if (chainSkill.getCategory().equals(category))
		{
			return chainSkill.getUseTime();
		}
		else
		{
			return 0;
		}
	}
	
	public boolean chainSkillEnabled(String category, int time)
	{
		long useTime = 0;
		if (multiSkills.get(category) != null)
		{
			useTime = multiSkills.get(category).getUseTime();
		}
		else if (chainSkill.getCategory().equals(category))
		{
			useTime = chainSkill.getUseTime();
		}
		if ((useTime + time) >= System.currentTimeMillis())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void addChainSkill(String category, boolean multiCast)
	{
		if (multiCast)
		{
			if (multiSkills.get(category) != null)
			{
				if (multiCast)
				{
					multiSkills.get(category).increaseChainCount();
				}
				multiSkills.get(category).setUseTime(System.currentTimeMillis());
			}
			else
			{
				multiSkills.put(category, new ChainSkill(category, (multiCast ? 1 : 0), System.currentTimeMillis()));
			}
		}
		else
		{
			chainSkill.updateChainSkill(category);
		}
	}
	
	public Collection<ChainSkill> getChainSkills()
	{
		final Collection<ChainSkill> collection = new ArrayList<>();
		collection.add(chainSkill);
		collection.addAll(multiSkills.values());
		return collection;
	}
}