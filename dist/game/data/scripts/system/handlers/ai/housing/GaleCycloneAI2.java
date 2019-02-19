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
package system.handlers.ai.housing;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.controllers.observer.GaleCycloneObserver;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@AIName("galecyclone")
public class GaleCycloneAI2 extends NpcAI2
{
	boolean blocked;
	private final FastMap<Integer, GaleCycloneObserver> observed = new FastMap<Integer, GaleCycloneObserver>().shared();
	
	@Override
	protected void handleCreatureSee(Creature creature)
	{
		if (blocked)
		{
			return;
		}
		if (creature instanceof Player)
		{
			final Player player = (Player) creature;
			final GaleCycloneObserver observer = new GaleCycloneObserver(player, getOwner())
			{
				@Override
				public void onMove()
				{
					if (!blocked)
					{
						SkillEngine.getInstance().getSkill(getOwner(), 20528, 50, player).useNoAnimationSkill();
					}
				}
			};
			player.getObserveController().addObserver(observer);
			observed.put(player.getObjectId(), observer);
		}
	}
	
	@Override
	protected void handleCreatureNotSee(Creature creature)
	{
		if (blocked)
		{
			return;
		}
		if (creature instanceof Player)
		{
			final Player player = (Player) creature;
			final Integer obj = player.getObjectId();
			final GaleCycloneObserver observer = observed.remove(obj);
			if (observer != null)
			{
				player.getObserveController().removeObserver(observer);
			}
		}
	}
	
	@Override
	protected void handleDied()
	{
		clear();
		super.handleDied();
	}
	
	@Override
	protected void handleDespawned()
	{
		clear();
		super.handleDespawned();
	}
	
	private void clear()
	{
		blocked = true;
		for (Integer obj : observed.keySet())
		{
			final Player player = getKnownList().getKnownPlayers().get(obj);
			final GaleCycloneObserver observer = observed.remove(obj);
			if (player != null)
			{
				player.getObserveController().removeObserver(observer);
			}
		}
	}
}