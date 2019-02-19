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
package system.handlers.ai.worlds.gelkmaros;

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
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("ragnarok")
public class RagnarokAI2 extends AggressiveNpcAI2
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
		if (hpPercentage <= 90)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				// Attack of poison and paralysis begins.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400610, 0);
				startPhaseTask();
			}
		}
		if (hpPercentage <= 50)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				// Attack that restricts physical and magical assaults begins.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400611, 0);
				startPhaseTask();
			}
		}
		if (hpPercentage <= 10)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				// Powerful continuous attacks and reflections begin.
				NpcShoutsService.getInstance().sendMsg(getOwner(), 1400613, 0);
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
				SkillEngine.getInstance().getSkill(getOwner(), 18679, 60, getOwner()).useNoAnimationSkill(); // Self Harm.
				final List<Player> players = getLifedPlayers();
				if (!players.isEmpty())
				{
					final int size = players.size();
					if (players.size() < 6)
					{
						for (Player p : players)
						{
							spawnRagnarok(p);
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
							spawnRagnarok(players.get(Rnd.get(players.size())));
						}
					}
				}
			}
		}, 3000, 15000);
	}
	
	private void spawnRagnarok(Player player)
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
					switch (Rnd.get(1, 2))
					{
						case 1:
						{
							spawn(281950, x, y, z, (byte) 0); // Ragnarok's Parasite.
							break;
						}
						case 2:
						{
							// Ragnarok's acidic fluid appears.
							NpcShoutsService.getInstance().sendMsg(getOwner(), 1400612, 2000);
							spawn(281951, x, y, z, (byte) 0); // Ragnarok Slime.
							break;
						}
					}
				}
			}, 1000);
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
	protected void handleBackHome()
	{
		cancelPhaseTask();
		isStartedEvent.set(false);
		isAggred.set(false);
		super.handleBackHome();
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
		final WorldPosition p = getPosition();
		if (p != null)
		{
			deleteNpcs(p.getWorldMapInstance().getNpcs(281950)); // Ragnarok's Parasite.
			deleteNpcs(p.getWorldMapInstance().getNpcs(281951)); // Ragnarok Slime.
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