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
package com.aionemu.gameserver.controllers.observer;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.geoEngine.collision.CollisionResults;
import com.aionemu.gameserver.geoEngine.math.Ray;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.geoEngine.scene.Spatial;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author MrPoke
 * @moved Rolandas
 */
public abstract class AbstractCollisionObserver extends ActionObserver
{
	
	protected Creature creature;
	protected Vector3f oldPos;
	protected Spatial geometry;
	protected byte intentions;
	private final AtomicBoolean isRunning = new AtomicBoolean();
	
	public AbstractCollisionObserver(Creature creature, Spatial geometry, byte intentions)
	{
		super(ObserverType.MOVE_OR_DIE);
		this.creature = creature;
		this.geometry = geometry;
		oldPos = new Vector3f(creature.getX(), creature.getY(), creature.getZ());
		this.intentions = intentions;
	}
	
	@Override
	public void moved()
	{
		if (!isRunning.getAndSet(true))
		{
			ThreadPoolManager.getInstance().execute(new Runnable()
			{
				
				@Override
				public void run()
				{
					try
					{
						final Vector3f pos = new Vector3f(creature.getX(), creature.getY(), creature.getZ());
						final Vector3f dir = oldPos.clone();
						final Float limit = pos.distance(dir);
						dir.subtractLocal(pos).normalizeLocal();
						final Ray r = new Ray(pos, dir);
						r.setLimit(limit);
						final CollisionResults results = new CollisionResults(intentions, true, creature.getInstanceId());
						geometry.collideWith(r, results);
						onMoved(results);
						oldPos = pos;
					}
					finally
					{
						isRunning.set(false);
					}
				}
			});
		}
	}
	
	public abstract void onMoved(CollisionResults result);
}
