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
package system.handlers.ai.worlds.iluma;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("auronos")
public class AuronosAI2 extends AggressiveNpcAI2
{
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			removeTerraShield();
		}
	}
	
	private void removeTerraShield()
	{
		getOwner().getEffectController().removeEffect(22847); // Terrashield.
	}
	
	private void terraShield()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 22847, 1, getOwner()).useNoAnimationSkill(); // Terrashield.
	}
	
	@Override
	protected void handleSpawned()
	{
		terraShield();
		super.handleSpawned();
	}
	
	@Override
	protected void handleBackHome()
	{
		terraShield();
		super.handleBackHome();
	}
}