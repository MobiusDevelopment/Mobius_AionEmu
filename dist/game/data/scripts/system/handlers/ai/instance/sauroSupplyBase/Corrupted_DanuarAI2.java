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
package system.handlers.ai.instance.sauroSupplyBase;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("corrupted_danuar")
public class Corrupted_DanuarAI2 extends AggressiveNpcAI2
{
	private Future<?> skillTask;
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		startpower();
	}
	
	private void startpower()
	{
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				AI2Actions.targetSelf(Corrupted_DanuarAI2.this);
				AI2Actions.useSkill(Corrupted_DanuarAI2.this, 21185); // Curse Of The Rune.
			}
		}, 3000, 5000);
	}
	
	private void cancelskillTask()
	{
		if ((skillTask != null) && !skillTask.isCancelled())
		{
			skillTask.cancel(true);
		}
	}
	
	@Override
	protected void handleDied()
	{
		cancelskillTask();
		super.handleDied();
	}
	
	@Override
	protected void handleDespawned()
	{
		cancelskillTask();
		super.handleDespawned();
	}
}