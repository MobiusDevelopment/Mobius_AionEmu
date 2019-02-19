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

class NpcSkillParameterEntry extends NpcSkillEntry
{
	public NpcSkillParameterEntry(int skillId, int skillLevel)
	{
		super(skillId, skillLevel);
	}
	
	@Override
	public boolean isReady(int hpPercentage, long fightingTimeInMSec)
	{
		return true;
	}
	
	@Override
	public boolean chanceReady()
	{
		return true;
	}
	
	@Override
	public boolean hpReady(int hpPercentage)
	{
		return true;
	}
	
	@Override
	public boolean timeReady(long fightingTimeInMSec)
	{
		return true;
	}
	
	@Override
	public boolean hasCooldown()
	{
		return false;
	}
	
	@Override
	public boolean UseInSpawned()
	{
		return true;
	}
}
