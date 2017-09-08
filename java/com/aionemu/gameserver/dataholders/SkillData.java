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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.skillengine.model.SkillTemplate;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
@XmlRootElement(name = "skill_data")
@XmlAccessorType(XmlAccessType.FIELD)
public class SkillData
{
	@XmlElement(name = "skill_template")
	private List<SkillTemplate> skillTemplates;
	
	private HashMap<Integer, ArrayList<Integer>> cooldownGroups;
	
	private final TIntObjectHashMap<SkillTemplate> skillData = new TIntObjectHashMap<>();
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		skillData.clear();
		for (SkillTemplate st : skillTemplates)
		{
			skillData.put(st.getSkillId(), st);
		}
	}
	
	public SkillTemplate getSkillTemplate(int skillId)
	{
		return skillData.get(skillId);
	}
	
	public int size()
	{
		return skillData.size();
	}
	
	public List<SkillTemplate> getSkillTemplates()
	{
		return skillTemplates;
	}
	
	public void setSkillTemplates(List<SkillTemplate> skillTemplates)
	{
		this.skillTemplates = skillTemplates;
		afterUnmarshal(null, null);
	}
	
	public void initializeCooldownGroups()
	{
		cooldownGroups = new HashMap<>();
		for (SkillTemplate skillTemplate : skillTemplates)
		{
			final int delayId = skillTemplate.getDelayId();
			if (!cooldownGroups.containsKey(delayId))
			{
				cooldownGroups.put(delayId, new ArrayList<Integer>());
			}
			cooldownGroups.get(delayId).add(skillTemplate.getSkillId());
		}
	}
	
	public ArrayList<Integer> getSkillsForDelayId(int delayId)
	{
		if (cooldownGroups == null)
		{
			initializeCooldownGroups();
		}
		return cooldownGroups.get(delayId);
	}
	
	public TIntObjectHashMap<SkillTemplate> getSkillData()
	{
		return skillData;
	}
}