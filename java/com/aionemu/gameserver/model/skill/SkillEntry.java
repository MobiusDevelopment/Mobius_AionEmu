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
package com.aionemu.gameserver.model.skill;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;

/**
 * @author ATracer
 */
public abstract class SkillEntry
{
	
	protected final int skillId;
	
	protected int skillLevel;
	
	SkillEntry(int skillId, int skillLevel)
	{
		this.skillId = skillId;
		this.skillLevel = skillLevel;
	}
	
	public final int getSkillId()
	{
		return skillId;
	}
	
	public final int getSkillLevel()
	{
		return skillLevel;
	}
	
	public final String getSkillName()
	{
		return DataManager.SKILL_DATA.getSkillTemplate(getSkillId()).getName();
	}
	
	public void setSkillLvl(int skillLevel)
	{
		this.skillLevel = skillLevel;
	}
	
	public final SkillTemplate getSkillTemplate()
	{
		return DataManager.SKILL_DATA.getSkillTemplate(getSkillId());
	}
	
}
