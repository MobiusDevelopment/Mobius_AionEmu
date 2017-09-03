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
package system.handlers.ai.instance.tiamatStronghold;

import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("bladestorm")
public class BladeStormAI2 extends AggressiveNpcAI2
{
	private Future<?> stormBladeTask;
	
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		stormBlade();
		startLifeTask();
	}
	
	private void stormBlade()
	{
		stormBladeTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				AI2Actions.targetCreature(BladeStormAI2.this, getPosition().getWorldMapInstance().getNpc(219357)); // Adjudant Anuhart.
				AI2Actions.useSkill(BladeStormAI2.this, 20748); // Storm Blade.
			}
		}, 3000, 8000);
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				AI2Actions.deleteOwner(BladeStormAI2.this);
			}
		}, 10000);
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}