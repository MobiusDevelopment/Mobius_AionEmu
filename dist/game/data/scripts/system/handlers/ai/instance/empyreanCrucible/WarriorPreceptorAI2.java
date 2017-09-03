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
package system.handlers.ai.instance.empyreanCrucible;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("warrior_preceptor")
public class WarriorPreceptorAI2 extends AggressiveNpcAI2
{
	private final AtomicBoolean isHome = new AtomicBoolean(true);
	private Future<?> task;
	
	@Override
	public void handleDespawned()
	{
		cancelTask();
		super.handleDespawned();
	}
	
	@Override
	public void handleDied()
	{
		cancelTask();
		super.handleDied();
	}
	
	@Override
	public void handleBackHome()
	{
		cancelTask();
		isHome.set(true);
		super.handleBackHome();
	}
	
	@Override
	public void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isHome.compareAndSet(true, false))
		{
			startSkillTask();
		}
	}
	
	private void startSkillTask()
	{
		task = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if (isAlreadyDead())
				{
					cancelTask();
				}
				else
				{
					startSkillEvent();
				}
			}
		}, 30000, 30000);
	}
	
	private void cancelTask()
	{
		if ((task != null) && !task.isCancelled())
		{
			task.cancel(true);
		}
	}
	
	private void startSkillEvent()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 19595, 46, getTargetPlayer()).useNoAnimationSkill();
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!isAlreadyDead())
				{
					SkillEngine.getInstance().getSkill(getOwner(), 19596, 46, getOwner()).useNoAnimationSkill();
				}
			}
		}, 6000);
	}
	
	private Player getTargetPlayer()
	{
		final List<Player> players = new ArrayList<>();
		for (final Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 15))
			{
				players.add(player);
			}
		}
		return !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
	}
}