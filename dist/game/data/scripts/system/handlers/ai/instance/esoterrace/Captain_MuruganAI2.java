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
package system.handlers.ai.instance.esoterrace;

import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("captain_murugan")
public class Captain_MuruganAI2 extends AggressiveNpcAI2
{
	private Future<?> task;
	private Future<?> specialSkillTask;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			getPosition().getWorldMapInstance().getDoors().get(70).setOpen(false);
			startTaskEvent();
		}
	}
	
	private void startTaskEvent()
	{
		final VisibleObject target = getTarget();
		if ((target != null) && (target instanceof Player))
		{
			SkillEngine.getInstance().getSkill(getOwner(), 19324, 1, target).useNoAnimationSkill();
		}
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(() ->
		{
			if (isAlreadyDead())
			{
				cancelTask();
			}
			else
			{
				// I'll get rid of the cursed ones first!
				sendMsg(1500193, getObjectId(), false, 0);
				SkillEngine.getInstance().getSkill(getOwner(), 19325, 1, getOwner()).useNoAnimationSkill();
				if (getLifeStats().getHpPercentage() <= 50)
				{
					specialSkillTask = ThreadPoolManager.getInstance().schedule(() ->
					{
						if (!isAlreadyDead())
						{
							// I'll get rid of the cursed ones first!
							sendMsg(1500193, getObjectId(), false, 0);
							final VisibleObject target2 = getTarget();
							if ((target2 != null) && (target2 instanceof Player))
							{
								SkillEngine.getInstance().getSkill(getOwner(), 19324, 1, target2).useNoAnimationSkill();
							}
							specialSkillTask = ThreadPoolManager.getInstance().schedule(() ->
							{
								if (!isAlreadyDead())
								{
									final VisibleObject target1 = getTarget();
									if ((target1 != null) && (target1 instanceof Player))
									{
										SkillEngine.getInstance().getSkill(getOwner(), 19324, 1, target1).useNoAnimationSkill();
									}
								}
							}, 4000);
						}
					}, 10000);
				}
			}
		}, 20000, 20000);
	}
	
	private void cancelTask()
	{
		if ((task != null) && !task.isDone())
		{
			task.cancel(true);
		}
	}
	
	private void cancelSpecialSkillTask()
	{
		if ((specialSkillTask != null) && !specialSkillTask.isDone())
		{
			specialSkillTask.cancel(true);
		}
	}
	
	@Override
	protected void handleBackHome()
	{
		cancelTask();
		cancelSpecialSkillTask();
		super.handleBackHome();
	}
	
	@Override
	protected void handleDespawned()
	{
		cancelTask();
		cancelSpecialSkillTask();
		super.handleDespawned();
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time)
	{
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
	
	@Override
	protected void handleDied()
	{
		cancelTask();
		cancelSpecialSkillTask();
		// Power is... overflowing...
		sendMsg(1500195, getObjectId(), false, 0);
		// My lord Surama... I.. am... sorry.
		sendMsg(1500194, getObjectId(), false, 5000);
		super.handleDied();
	}
}