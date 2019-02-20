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
package system.handlers.ai.instance.beshmundirTemple;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Luzien
 */
@AIName("templeSoul")
public class SacrificialSoulAI2 extends AggressiveNpcAI2
{
	private Npc boss;
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		AI2Actions.useSkill(this, 18901); // Time Wrinkle.
		setStateIfNot(AIState.FOLLOWING);
		boss = getPosition().getWorldMapInstance().getNpc(216263); // Isbariya The Resolute.
		if ((boss != null) && !CreatureActions.isAlreadyDead(boss))
		{
			AI2Actions.targetCreature(this, boss);
			getMoveController().moveToTargetObject();
		}
	}
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (creature.getEffectController().hasAbnormalEffect(18959)) // Sixth Sense.
		{
			getMoveController().abortMove();
			AI2Actions.deleteOwner(this);
		}
	}
	
	@Override
	protected void handleMoveArrived()
	{
		if ((boss != null) && !CreatureActions.isAlreadyDead(boss))
		{
			SkillEngine.getInstance().getSkill(getOwner(), 18960, 55, boss).useNoAnimationSkill(); // Call Of The Grave.
			AI2Actions.deleteOwner(this);
		}
	}
	
	@Override
	public boolean canThink()
	{
		return false;
	}
}