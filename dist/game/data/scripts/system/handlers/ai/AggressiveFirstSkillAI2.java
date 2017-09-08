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
package system.handlers.ai;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author Ritsu
 * @Reworked Majka Ajural
 */
@AIName("aggressive_first_skill")
public class AggressiveFirstSkillAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleBackHome()
	{
		super.handleBackHome();
		if (getSkillList().getUseInSpawnedSkill() != null)
		{
			final int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			final int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}
	
	@Override
	protected void handleRespawned()
	{
		super.handleRespawned();
		if (getSkillList().getUseInSpawnedSkill() != null)
		{
			final int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			final int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		if (getSkillList().getUseInSpawnedSkill() != null)
		{
			final int skillId = getSkillList().getUseInSpawnedSkill().getSkillId();
			final int skillLevel = getSkillList().getSkillLevel(skillId);
			SkillEngine.getInstance().getSkill(getOwner(), skillId, skillLevel, getOwner()).useSkill();
		}
	}
}