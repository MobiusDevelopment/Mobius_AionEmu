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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("adjutantanuhart")
public class AdjutantAnuhartAI2 extends AggressiveNpcAI2
{
	private Future<?> bladeStormTask;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	private final List<Integer> percents = new ArrayList<>();
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			startBladeStormTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void startBladeStormTask()
	{
		bladeStormTask = ThreadPoolManager.getInstance().scheduleAtFixedRate((Runnable) () ->
		{
			if (isAlreadyDead())
			{
				cancelTask();
			}
			else
			{
				startBladeStormEvent();
			}
		}, 5000, 40000);
	}
	
	private void startBladeStormEvent()
	{
		shield();
		SkillEngine.getInstance().getSkill(getOwner(), 20747, 1, getOwner()).useNoAnimationSkill();
		spawn(283099, getOwner().getX(), getOwner().getY(), getOwner().getZ(), (byte) 0); // Blade Storm.
	}
	
	void cancelTask()
	{
		if ((bladeStormTask != null) && !bladeStormTask.isCancelled())
		{
			bladeStormTask.cancel(true);
		}
	}
	
	private void checkPercentage(int hpPercentage)
	{
		for (Integer percent : percents)
		{
			if (hpPercentage <= percent)
			{
				switch (percent)
				{
					case 90:
					{
						swiftAttack(20940);
						startBladeStormTask();
						break;
					}
					case 70:
					{
						swiftAttack(20939);
						break;
					}
					case 50:
					{
						swiftAttack(20938);
						startBladeStormTask();
						break;
					}
					case 30:
					{
						swiftAttack(20940);
						break;
					}
					case 20:
					{
						swiftAttack(20939);
						startBladeStormTask();
						break;
					}
					case 10:
					{
						swiftAttack(20938);
						break;
					}
				}
				percents.remove(percent);
				break;
			}
		}
	}
	
	private void swiftAttack(int buff)
	{
		AI2Actions.targetSelf(this);
		AI2Actions.useSkill(this, buff);
	}
	
	private void shield()
	{
		AI2Actions.targetSelf(this);
		AI2Actions.useSkill(this, 20749);
	}
	
	private void addPercent()
	{
		percents.clear();
		Collections.addAll(percents, new Integer[]
		{
			90,
			70,
			50,
			30,
			20,
			10
		});
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		addPercent();
	}
	
	@Override
	protected void handleBackHome()
	{
		addPercent();
		super.handleBackHome();
		cancelTask();
	}
	
	@Override
	protected void handleDespawned()
	{
		super.handleDespawned();
		cancelTask();
	}
	
	@Override
	protected void handleDied()
	{
		cancelTask();
		percents.clear();
		super.handleDied();
	}
}