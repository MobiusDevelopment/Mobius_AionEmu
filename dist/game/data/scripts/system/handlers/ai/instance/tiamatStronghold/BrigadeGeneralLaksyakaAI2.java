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
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("brigadegenerallaksyaka")
public class BrigadeGeneralLaksyakaAI2 extends AggressiveNpcAI2
{
	private boolean isFinalBuff;
	private Future<?> phaseTask;
	private Future<?> skeletonTask;
	private boolean canThink = true;
	private final AtomicBoolean isHome = new AtomicBoolean(true);
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	private final AtomicBoolean isStartedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			getPosition().getWorldMapInstance().getDoors().get(56).setOpen(false);
		}
		if (!isFinalBuff && (getOwner().getLifeStats().getHpPercentage() <= 25))
		{
			isFinalBuff = true;
			AI2Actions.useSkill(this, 20731);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if (hpPercentage <= 95)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startPhaseTask();
			}
		}
		if (hpPercentage <= 70)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startSkillTask();
			}
		}
		if (hpPercentage <= 50)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startPhaseTask();
			}
		}
		if (hpPercentage <= 30)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startSkillTask();
			}
		}
		if (hpPercentage <= 10)
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
				final List<Player> players = getLifedPlayers();
				if (!players.isEmpty())
				{
					final int size = players.size();
					if (players.size() < 6)
					{
						for (Player p : players)
						{
							spawnLaksyakaOffering(p);
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
							spawnLaksyakaOffering(players.get(Rnd.get(players.size())));
						}
					}
				}
			}
		}, 20000, 40000);
	}
	
	void spawnLaksyakaOffering(Player player)
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
					spawn(283115, x, y, z, (byte) 0); // Laksyaka Offering.
				}
			}, 3000);
		}
	}
	
	private void startSkillTask()
	{
		skeletonTask = ThreadPoolManager.getInstance().scheduleAtFixedRate((Runnable) () ->
		{
			if (isAlreadyDead())
			{
				cancelTask();
			}
			else
			{
				startSkeletonEvent();
			}
		}, 5000, 40000);
	}
	
	void cancelPhaseTask()
	{
		if ((phaseTask != null) && !phaseTask.isDone())
		{
			phaseTask.cancel(true);
		}
	}
	
	void cancelTask()
	{
		if ((skeletonTask != null) && !skeletonTask.isCancelled())
		{
			skeletonTask.cancel(true);
		}
	}
	
	void startSkeletonEvent()
	{
		final Npc tiamatEye = getPosition().getWorldMapInstance().getNpc(283178); // Tiamat's Eye.
		final List<Player> players = new ArrayList<>();
		for (Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player))
			{
				players.add(player);
			}
		}
		final Player player = !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
		SkillEngine.getInstance().applyEffectDirectly(20865, tiamatEye, player, 30000); // Body Snatch.
	}
	
	List<Player> getLifedPlayers()
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
	
	@Override
	public boolean canThink()
	{
		return canThink;
	}
	
	@Override
	protected void handleDied()
	{
		cancelTask();
		cancelPhaseTask();
		final WorldPosition p = getPosition();
		if (p != null)
		{
			deleteNpcs(p.getWorldMapInstance().getNpcs(283115)); // Laksyaka Offering.
		}
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
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		getOwner().setNpcType(NpcType.PEACE);
	}
	
	@Override
	protected void handleDespawned()
	{
		super.handleDespawned();
		cancelPhaseTask();
		cancelTask();
	}
	
	@Override
	protected void handleBackHome()
	{
		super.handleBackHome();
		cancelTask();
		cancelPhaseTask();
		canThink = true;
		isHome.set(true);
		isAggred.set(false);
		isFinalBuff = false;
		isStartedEvent.set(false);
	}
}