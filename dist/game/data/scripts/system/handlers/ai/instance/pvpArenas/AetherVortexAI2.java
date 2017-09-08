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
package system.handlers.ai.instance.pvpArenas;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("aether_vortex")
public class AetherVortexAI2 extends AggressiveNpcAI2
{
	private Future<?> eventTask;
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		startEventTask();
	}
	
	@Override
	protected void handleDied()
	{
		cancelEventTask();
		super.handleDied();
	}
	
	@Override
	protected void handleDespawned()
	{
		cancelEventTask();
		super.handleDespawned();
	}
	
	private void cancelEventTask()
	{
		if ((eventTask != null) && !eventTask.isDone())
		{
			eventTask.cancel(true);
		}
	}
	
	private void startEventTask()
	{
		eventTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if (isAlreadyDead())
				{
					cancelEventTask();
				}
				else
				{
					SkillEngine.getInstance().getSkill(getOwner(), 20059, 60, getOwner()).useNoAnimationSkill();
				}
			}
		}, 1000, 1000);
	}
}