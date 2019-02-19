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
package system.handlers.ai.worlds.enshar;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("nepilim_1")
public class Petrified_Jotun_WorkerAI2 extends AggressiveNpcAI2
{
	@Override
	public void think()
	{
	}
	
	private final AtomicBoolean startedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
		if (creature instanceof Player)
		{
			final Player player = (Player) creature;
			if (MathUtil.getDistance(getOwner(), player) <= 10)
			{
				if (startedEvent.compareAndSet(false, true))
				{
					spawn(219776, getOwner().getX(), getOwner().getY(), getOwner().getZ(), getOwner().getHeading());
					AI2Actions.deleteOwner(Petrified_Jotun_WorkerAI2.this);
					AI2Actions.scheduleRespawn(this);
					ThreadPoolManager.getInstance().schedule(() -> despawnNpc(219776), 300000); // 5 Minutes.
				}
			}
		}
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
	
	void despawnNpc(int npcId)
	{
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null)
		{
			final List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs)
			{
				npc.getController().onDelete();
			}
		}
	}
}