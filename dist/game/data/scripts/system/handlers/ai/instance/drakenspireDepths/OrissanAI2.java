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
package system.handlers.ai.instance.drakenspireDepths;

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

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("orissan")
public class OrissanAI2 extends AggressiveNpcAI2
{
	private int orissanPhase = 0;
	private Future<?> crystalTask;
	private boolean canThink = true;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if ((hpPercentage == 95) && (orissanPhase < 1))
		{
			orissanPhase = 1;
			spawnFrigidCrystal();
		}
		if ((hpPercentage == 75) && (orissanPhase < 2))
		{
			orissanPhase = 2;
			spawnFrigidCrystal();
		}
		if ((hpPercentage == 55) && (orissanPhase < 3))
		{
			orissanPhase = 3;
			spawnFrigidCrystal();
		}
		if ((hpPercentage == 35) && (orissanPhase < 4))
		{
			orissanPhase = 4;
			spawnFrigidCrystal();
		}
		if ((hpPercentage == 15) && (orissanPhase < 5))
		{
			orissanPhase = 5;
			spawnFrigidCrystal();
		}
	}
	
	private void spawnFrigidCrystal()
	{
		crystalTask = ThreadPoolManager.getInstance().scheduleAtFixedRate((Runnable) () ->
		{
			if (isAlreadyDead())
			{
				cancelPhaseTask();
			}
			else
			{
				SkillEngine.getInstance().getSkill(getOwner(), 21635, 46, getOwner()).useNoAnimationSkill(); // Summon Crystal.
				final List<Player> players = getLifedPlayers();
				if (!players.isEmpty())
				{
					final int size = players.size();
					if (players.size() < 6)
					{
						for (Player p : players)
						{
							spawnFrigidCrystal(p);
						}
					}
					else
					{
						final int count = Rnd.get(6, size);
						for (int i = 0; i < count; i++)
						{
							if (players.isEmpty())
							{
								break;
							}
							spawnFrigidCrystal(players.get(Rnd.get(players.size())));
						}
					}
				}
			}
		}, 20000, 40000);
	}
	
	private void spawnFrigidCrystal(Player player)
	{
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		if ((x > 0) && (y > 0) && (z > 0))
		{
			ThreadPoolManager.getInstance().schedule((Runnable) () ->
			{
				if (!isAlreadyDead())
				{
					spawn(855699, x, y, z, (byte) 0); // Frigid Crystal.
				}
			}, 3000);
		}
	}
	
	private List<Player> getLifedPlayers()
	{
		final List<Player> players = new ArrayList<>();
		for (Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player))
			{
				players.add(player);
			}
		}
		return players;
	}
	
	void cancelPhaseTask()
	{
		if ((crystalTask != null) && !crystalTask.isDone())
		{
			crystalTask.cancel(true);
		}
	}
	
	@Override
	protected void handleDespawned()
	{
		cancelPhaseTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
	}
	
	@Override
	public boolean canThink()
	{
		return canThink;
	}
	
	@Override
	protected void handleBackHome()
	{
		canThink = true;
		cancelPhaseTask();
		isAggred.set(false);
		super.handleBackHome();
	}
}