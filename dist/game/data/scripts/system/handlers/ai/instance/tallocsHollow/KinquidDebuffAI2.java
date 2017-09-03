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
package system.handlers.ai.instance.tallocsHollow;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("kinquid_debuff")
public class KinquidDebuffAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
		super.handleCreatureMoved(creature);
		if ((creature instanceof Npc) && isInRange(creature, 10))
		{
			final Npc npc = (Npc) creature;
			if (npc.getNpcId() == 215467)
			{ // Kindquid.
				SkillEngine.getInstance().getSkill(getOwner(), getNpcId() == 282008 ? 19235 : 19236, 46, getOwner()).useNoAnimationSkill();
			}
		}
	}
}