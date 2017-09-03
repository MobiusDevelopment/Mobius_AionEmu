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
package com.aionemu.gameserver.dataholders;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.luna.LunaConsumeRewardsTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Ranastic
 */
@XmlRootElement(name = "luna_consume_rewards")
@XmlAccessorType(XmlAccessType.FIELD)
public class LunaConsumeRewardsData
{
	
	@XmlElement(name = "luna_consume_reward")
	private List<LunaConsumeRewardsTemplate> lunaList;
	
	@XmlTransient
	private final TIntObjectHashMap<LunaConsumeRewardsTemplate> lunaData = new TIntObjectHashMap<>();
	
	@XmlTransient
	private final TIntObjectHashMap<LunaConsumeRewardsTemplate> lunaConsumeCountData = new TIntObjectHashMap<>();
	
	@XmlTransient
	private final Map<Integer, LunaConsumeRewardsTemplate> lunaDataMap = new HashMap<>(1);
	
	void afterUnmarshal(Unmarshaller paramUnmarshaller, Object paramObject)
	{
		for (LunaConsumeRewardsTemplate lunaConsume : lunaList)
		{
			lunaData.put(lunaConsume.getId(), lunaConsume);
			lunaConsumeCountData.put(lunaConsume.getSumCount(), lunaConsume);
			lunaDataMap.put(lunaConsume.getId(), lunaConsume);
		}
	}
	
	public int size()
	{
		return lunaData.size();
	}
	
	public LunaConsumeRewardsTemplate getLunaConsumeRewardsId(int id)
	{
		return lunaData.get(id);
	}
	
	public LunaConsumeRewardsTemplate getLunaConsumeRewardsBypoint(int point)
	{
		return lunaConsumeCountData.get(point);
	}
	
	public Map<Integer, LunaConsumeRewardsTemplate> getAll()
	{
		return lunaDataMap;
	}
}
