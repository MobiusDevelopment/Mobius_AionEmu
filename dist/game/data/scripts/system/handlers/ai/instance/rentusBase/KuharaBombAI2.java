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
package system.handlers.ai.instance.rentusBase;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("kuhara_bomb")
public class KuharaBombAI2 extends AggressiveNpcAI2
{
	private Npc kuharaTheVolatile1;
	private Npc kuharaTheVolatile2;
	private final AtomicBoolean isDestroyed = new AtomicBoolean(false);
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		setStateIfNot(AIState.FOLLOWING);
		kuharaTheVolatile1 = getPosition().getWorldMapInstance().getNpc(217311); // Kuhara The Volatile.
		kuharaTheVolatile2 = getPosition().getWorldMapInstance().getNpc(236298); // Kuhara The Volatile.
	}
	
	@Override
	protected void handleMoveArrived()
	{
		if (isDestroyed.compareAndSet(false, true))
		{
			if ((kuharaTheVolatile1 != null) && !CreatureActions.isAlreadyDead(kuharaTheVolatile1))
			{
				SkillEngine.getInstance().getSkill(getOwner(), 19659, 60, kuharaTheVolatile1).useNoAnimationSkill(); // Bomb Explosion.
			}
			else if ((kuharaTheVolatile2 != null) && !CreatureActions.isAlreadyDead(kuharaTheVolatile2))
			{
				SkillEngine.getInstance().getSkill(getOwner(), 19659, 60, kuharaTheVolatile2).useNoAnimationSkill(); // Bomb Explosion.
			}
		}
	}
}