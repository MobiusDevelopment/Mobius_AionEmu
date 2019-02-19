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
package com.aionemu.gameserver.model.templates.item;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Stigma")
public class Stigma
{
	@XmlElement(name = "require_skill")
	protected List<RequireSkill> requireSkill;
	
	@XmlAttribute
	protected List<String> skill;
	
	@XmlAttribute
	protected int shard;
	
	public List<StigmaSkill> getSkills()
	{
		final List<StigmaSkill> list = new ArrayList<>();
		for (String st : skill)
		{
			final String[] array = st.split(":");
			list.add(new StigmaSkill(Integer.parseInt(array[0]), Integer.parseInt(array[1])));
		}
		return list;
	}
	
	public int getShard()
	{
		return shard;
	}
	
	public List<RequireSkill> getRequireSkill()
	{
		if (requireSkill == null)
		{
			requireSkill = new ArrayList<>();
		}
		return requireSkill;
	}
	
	public class StigmaSkill
	{
		
		private final int skillId;
		private final int skillLvl;
		
		public StigmaSkill(int skillLvl, int skillId)
		{
			this.skillId = skillId;
			this.skillLvl = skillLvl;
		}
		
		public int getSkillLvl()
		{
			return skillLvl;
		}
		
		public int getSkillId()
		{
			return skillId;
		}
	}
	
	public List<Integer> getSkillIdOnly()
	{
		final List<Integer> ids = new ArrayList<>();
		final List<String> skill = this.skill;
		if (skill.size() != 1)
		{ // Dual Skills like Exhausting Wave
			String[] tempArray = new String[0];
			for (String parts : skill)
			{ // loops each of the 1:534 and 1:4342
				tempArray = parts.split(":");
				ids.add(Integer.parseInt(tempArray[1]));
			}
			return ids;
		}
		
		// Single 1 Skill
		for (String st : this.skill)
		{
			final String[] array = st.split(":");
			ids.add(Integer.parseInt(array[1]));
		}
		return ids;
	}
}