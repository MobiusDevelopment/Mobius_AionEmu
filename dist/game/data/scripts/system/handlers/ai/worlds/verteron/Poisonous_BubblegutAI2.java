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
package system.handlers.ai.worlds.verteron;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.world.WorldPosition;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("poisonous_bubblegut")
public class Poisonous_BubblegutAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned()
	{
		protectionFluid();
		super.handleSpawned();
	}
	
	private void protectionFluid()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 16447, 1, getOwner()).useNoAnimationSkill(); // Spout Sticky Protection Fluid.
	}
	
	@Override
	protected void handleDied()
	{
		final WorldPosition p = getPosition();
		switch (getNpcId())
		{
			case 210318: // Poisonous Bubblegut.
				if (p != null)
				{
					spawn(203195, p.getX(), p.getY(), p.getZ(), (byte) 0); // Kato.
				}
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(203195); // Kato.
					}
				}, 60000);
				break;
		}
		super.handleDied();
	}
	
	private void despawnNpc(int npcId)
	{
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null)
		{
			final List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (final Npc npc : npcs)
			{
				npc.getController().onDelete();
			}
		}
	}
}