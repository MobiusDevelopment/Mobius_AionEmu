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
package system.handlers.ai.instance.shugoEmperorVault;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.handler.CreatureEventHandler;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("healing_spring")
public class Healing_SpringAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
		CreatureEventHandler.onCreatureSee(this, creature);
		if (creature instanceof Player)
		{
			if (!creature.getEffectController().hasAbnormalEffect(17560))
			{ // Bless Of Guardian Spring.
				SkillEngine.getInstance().getSkill(getOwner(), 17560, 1, creature).useNoAnimationSkill(); // Bless Of Guardian Spring.
			}
		}
	}
}