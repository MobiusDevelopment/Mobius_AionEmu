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
package com.aionemu.gameserver.skillengine.effect;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.VisibleObjectSpawner;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "SummonTrapEffect")
public class SummonTrapEffect extends SummonEffect
{
	@Override
	public void applyEffect(Effect effect)
	{
		final Creature effector = effect.getEffector();
		if (effect.getEffector().getTarget() == null)
		{
			effect.getEffector().setTarget(effect.getEffector());
		}
		final double radian = Math.toRadians(MathUtil.convertHeadingToDegree(effect.getEffector().getHeading()));
		float x = effect.getX();
		float y = effect.getY();
		float z = effect.getZ();
		if ((x == 0) && (y == 0))
		{
			final Creature effected = effect.getEffected();
			x = effected.getX() + (float) (Math.cos(radian) * 2);
			y = effected.getY() + (float) (Math.sin(radian) * 2);
			z = effected.getZ();
		}
		final byte heading = effector.getHeading();
		final int worldId = effector.getWorldId();
		final int instanceId = effector.getInstanceId();
		if ((npcId == 749300) || (npcId == 749301) || // Scrapped Mechanisms.
			(npcId == 833699) || (npcId == 833700))
		{ // Highdeva_Fire_NPC.
			x = effector.getX();
			y = effector.getY();
			z = effector.getZ();
		}
		maxTraps(effector);
		final SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, x, y, z, heading);
		final Trap trap = VisibleObjectSpawner.spawnTrap(spawn, instanceId, effector);
		final Future<?> task = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				trap.getController().onDelete();
			}
		}, time * 1000);
		trap.getController().addTask(TaskId.DESPAWN, task);
	}
	
	private void maxTraps(Creature effector)
	{
		final List<Trap> traps = effector.getPosition().getWorldMapInstance().getTraps(effector);
		if (traps.size() >= 2)
		{
			final Iterator<Trap> trapIter = traps.iterator();
			final Trap t = trapIter.next();
			t.getController().cancelTask(TaskId.DESPAWN);
			t.getController().onDelete();
		}
	}
}