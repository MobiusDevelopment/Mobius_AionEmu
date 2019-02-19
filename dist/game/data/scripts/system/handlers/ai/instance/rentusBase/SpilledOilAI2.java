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
package system.handlers.ai.instance.rentusBase;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author xTz
 */
@AIName("spilled_oil")
public class SpilledOilAI2 extends AggressiveNpcAI2
{
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		attackOilSoak();
		startLifeTask();
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () -> AI2Actions.deleteOwner(SpilledOilAI2.this), 20000); // 20 Secondes.
	}
	
	private void attackOilSoak()
	{
		ThreadPoolManager.getInstance().scheduleAtFixedRate((Runnable) () ->
		{
			AI2Actions.targetCreature(SpilledOilAI2.this, getPosition().getWorldMapInstance().getNpc(217311)); // Kuhara The Volatile.
			AI2Actions.targetCreature(SpilledOilAI2.this, getPosition().getWorldMapInstance().getNpc(236298)); // Kuhara The Volatile.
			AI2Actions.useSkill(SpilledOilAI2.this, 19658); // Oil Soak.
		}, 3000, 8000);
	}
}