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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Antraxx
 */
@AIName("virhana")
public class VirhanaTheGreatAI2 extends AggressiveNpcAI2
{
	private int count;
	private boolean isStart;
	
	@Override
	public void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (!isStart)
		{
			isStart = true;
			scheduleRage();
		}
	}
	
	@Override
	protected void handleBackHome()
	{
		super.handleBackHome();
		isStart = false;
	}
	
	private void scheduleRage()
	{
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		AI2Actions.useSkill(this, 19121); // Seal Of Reflection.
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startRage();
			}
		}, 70000);
	}
	
	private void startRage()
	{
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		if (count < 12)
		{
			AI2Actions.useSkill(this, 18897); // Earthly Retribution.
			count++;
			ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					startRage();
				}
			}, 10000);
		}
		else
		{
			count = 0;
			scheduleRage();
		}
	}
}