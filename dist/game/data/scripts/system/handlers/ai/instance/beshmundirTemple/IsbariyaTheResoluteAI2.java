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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.geometry.Point3D;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.MathUtil;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Luzien
 * @author Antraxx
 */
@AIName("isbariya")
public class IsbariyaTheResoluteAI2 extends AggressiveNpcAI2
{
	private int stage = 0;
	private Future<?> basicSkillTask;
	private final AtomicBoolean isStart = new AtomicBoolean(false);
	private final List<Point3D> soulLocations = new ArrayList<>();
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isStart.compareAndSet(false, true))
		{
			NpcShoutsService.getInstance().sendMsg(getOwner(), 342051, getObjectId(), 0, 1000);
			getPosition().getWorldMapInstance().getDoors().get(535).setOpen(false);
			startBasicSkillTask();
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		soulLocations.add(new Point3D(1580.5f, 1572.8f, 304.64f));
		soulLocations.add(new Point3D(1582.1f, 1571.2f, 304.64f));
		soulLocations.add(new Point3D(1583.3f, 1569.9f, 304.64f));
		soulLocations.add(new Point3D(1585.3f, 1568.1f, 304.64f));
		soulLocations.add(new Point3D(1586.4f, 1567.1f, 304.64f));
		soulLocations.add(new Point3D(1588.3f, 1566.2f, 304.64f));
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
		NpcShoutsService.getInstance().sendMsg(getOwner(), 342055, getObjectId(), 0, 0);
		cancelSkillTask();
	}
	
	@Override
	protected void handleBackHome()
	{
		NpcShoutsService.getInstance().sendMsg(getOwner(), 342056, getObjectId(), 0, 0);
		super.handleBackHome();
		isStart.set(false);
		cancelSkillTask();
		stage = 0;
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if ((hpPercentage <= 75) && (stage < 1))
		{
			stage = 1;
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1400460);
			launchSpecial();
		}
		if ((hpPercentage <= 50) && (stage < 2))
		{
			NpcShoutsService.getInstance().sendMsg(getOwner(), 1400459);
			stage = 2;
		}
		if ((hpPercentage <= 25) && (stage < 3))
		{
			stage = 3;
		}
	}
	
	private void startBasicSkillTask()
	{
		basicSkillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				if (isAlreadyDead())
				{
					cancelSkillTask();
				}
				else
				{
					SkillEngine.getInstance().getSkill(getOwner(), 18912 + Rnd.get(2), 55, getOwner()).useNoAnimationSkill();
				}
			}
		}, 0, 24000);
	}
	
	private void cancelSkillTask()
	{
		if ((basicSkillTask != null) && !basicSkillTask.isCancelled())
		{
			basicSkillTask.cancel(true);
		}
	}
	
	private void launchSpecial()
	{
		if (isAlreadyDead() || (stage == 0))
		{
			return;
		}
		int delay = 10000;
		switch (stage)
		{
			case 1:
			{
				SkillEngine.getInstance().getSkill(getOwner(), 18959, 50, getTargetPlayer()).useNoAnimationSkill();
				spawnSouls();
				delay = 25000;
				break;
			}
			case 2:
			{
				rndSpawn(281660, 5);
				break;
			}
			case 3:
			{
				rndSpawn(281659, 1);
				AI2Actions.useSkill(this, 18993);
				delay = 20000;
				break;
			}
		}
		scheduleSpecial(delay);
	}
	
	private void rndSpawn(int npcId, int count)
	{
		for (int i = 0; i < count; i++)
		{
			final SpawnTemplate template = rndSpawnInRange(npcId);
			SpawnEngine.spawnObject(template, getPosition().getInstanceId());
		}
	}
	
	private void spawnSouls()
	{
		final List<Point3D> points = new ArrayList<>();
		points.addAll(soulLocations);
		final int count = Rnd.get(3, 6);
		for (int i = 0; i < count; i++)
		{
			if (!points.isEmpty())
			{
				final Point3D spawn = points.remove(Rnd.get(points.size()));
				spawn(281645, spawn.getX(), spawn.getY(), spawn.getZ(), (byte) 18);
			}
		}
	}
	
	private Player getTargetPlayer()
	{
		final List<Player> players = new ArrayList<>();
		for (Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 40) && (player != getTarget()))
			{
				players.add(player);
			}
		}
		return !players.isEmpty() ? players.get(Rnd.get(players.size())) : null;
	}
	
	private void scheduleSpecial(int delay)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				launchSpecial();
			}
		}, delay);
	}
	
	private SpawnTemplate rndSpawnInRange(int npcId)
	{
		final float direction = Rnd.get(0, 199) / 100f;
		final float x1 = (float) (Math.cos(Math.PI * direction) * 5);
		final float y1 = (float) (Math.sin(Math.PI * direction) * 5);
		return SpawnEngine.addNewSingleTimeSpawn(getPosition().getMapId(), npcId, getPosition().getX() + x1, getPosition().getY() + y1, getPosition().getZ(), getPosition().getHeading());
	}
}