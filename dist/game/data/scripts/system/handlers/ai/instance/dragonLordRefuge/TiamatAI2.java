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
package system.handlers.ai.instance.dragonLordRefuge;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("tiamat")
public class TiamatAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		SkillEngine.getInstance().getSkill(getOwner(), 20975, 1, getOwner()).useNoAnimationSkill(); // Fissure Incarnate.
		SkillEngine.getInstance().getSkill(getOwner(), 20976, 1, getOwner()).useNoAnimationSkill(); // Wrath Incarnate.
		SkillEngine.getInstance().getSkill(getOwner(), 20977, 1, getOwner()).useNoAnimationSkill(); // Gravity Incarnate.
		SkillEngine.getInstance().getSkill(getOwner(), 20978, 1, getOwner()).useNoAnimationSkill(); // Petrification Incarnate.
		SkillEngine.getInstance().getSkill(getOwner(), 20984, 1, getOwner()).useNoAnimationSkill(); // Unbreakable Wing.
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}