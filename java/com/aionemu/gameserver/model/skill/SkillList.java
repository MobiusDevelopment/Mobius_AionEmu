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

import com.aionemu.gameserver.model.gameobjects.Creature;

/**
 * @author ATracer
 * @param <T>
 */
public interface SkillList<T extends Creature>
{
	
	/**
	 * Add skill to list
	 * @param creature
	 * @param skillId
	 * @param skillLevel
	 * @return true if operation was successful
	 */
	boolean addSkill(T creature, int skillId, int skillLevel);
	
	boolean addLinkedSkill(T creature, int skillId);
	
	/**
	 * Remove skill from list
	 * @param skillId
	 * @return true if operation was successful
	 */
	boolean removeSkill(int skillId);
	
	/**
	 * Check whether skill is present in list
	 * @param skillId
	 * @return
	 */
	boolean isSkillPresent(int skillId);
	
	int getSkillLevel(int skillId);
	
	/**
	 * Size of skill list
	 * @return
	 */
	int size();
	
}
