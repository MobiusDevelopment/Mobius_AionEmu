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
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * Author Rinzler (Encom) The only foe you will face in Stage 10 is Vanktrist. A smaller, less powerful underling of the Master Boss of the Empyrean Crucible, Vanktrist employs some of the same abilities, albeit at power levels less than 9000. The common abilities in Vanktrist's arsenal are Young
 * Space Twist, which deals damage and stuns and Void Flame. At about 75% health, he will begin to use his Gravitational Shift ability which will slow your movement speed. Soon after, he will summon a couple Vortexes to assist him. These are non-elite and relatively easy to defeat, so you may want
 * to focus on them first. Once he reaches 50%, he will add the Young Adrift skill to his attacks, which deals moderate damage and is used fairly often. Since defeating Vanktrist is essential to progressing your bonus round quest, be sure to pull out all the stops, utilizing potions, DP, and high
 * cooldown skills to make sure you are victorious!
 */

@AIName("vanktrist")
public class VanktristAI2 extends AggressiveNpcAI2
{
	private Future<?> phaseTask;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	private final AtomicBoolean isStartedEvent = new AtomicBoolean(false);
	
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
		if (hpPercentage <= 75)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startPhaseTask();
			}
		}
		if (hpPercentage <= 50)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startPhaseTask();
			}
		}
	}
	
	private void startPhaseTask()
	{
		phaseTask = ThreadPoolManager.getInstance().scheduleAtFixedRate((Runnable) () ->
		{
			if (isAlreadyDead())
			{
				cancelPhaseTask();
			}
			else
			{
				SkillEngine.getInstance().getSkill(getOwner(), 19567, 46, getOwner()).useNoAnimationSkill(); // Gravitational Shift.
				final List<Player> players = getLifedPlayers();
				if (!players.isEmpty())
				{
					final int size = players.size();
					if (players.size() < 1)
					{
						for (Player p : players)
						{
							spawnWeakenedDimensionalVortex(p);
						}
					}
					else
					{
						final int count = Rnd.get(1, size);
						for (int i = 0; i < count; i++)
						{
							if (players.isEmpty())
							{
								break;
							}
							spawnWeakenedDimensionalVortex(players.get(Rnd.get(players.size())));
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnWeakenedDimensionalVortex(Player player)
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
					spawn(217804, x, y, z, (byte) 0); // Weakened Dimensional Vortex.
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
		if ((phaseTask != null) && !phaseTask.isDone())
		{
			phaseTask.cancel(true);
		}
	}
	
	@Override
	protected void handleDespawned()
	{
		cancelPhaseTask();
		super.handleDespawned();
	}
	
	@Override
	protected void handleBackHome()
	{
		cancelPhaseTask();
		isStartedEvent.set(false);
		isAggred.set(false);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDied()
	{
		final WorldPosition p = getPosition();
		if (p != null)
		{
			deleteNpcs(p.getWorldMapInstance().getNpcs(217804)); // Vortex.
		}
		cancelPhaseTask();
		super.handleDied();
	}
	
	private void deleteNpcs(List<Npc> npcs)
	{
		for (Npc npc : npcs)
		{
			if (npc != null)
			{
				npc.getController().onDelete();
			}
		}
	}
}