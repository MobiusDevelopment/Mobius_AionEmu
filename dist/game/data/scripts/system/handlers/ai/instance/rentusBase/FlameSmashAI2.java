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
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.skillengine.SkillEngine;

/**
 * @author xTz
 */
@AIName("flame_smash")
public class FlameSmashAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		starLifeTask();
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			if (!isAlreadyDead())
			{
				SkillEngine.getInstance().getSkill(getOwner(), getNpcId() == 283008 ? 20540 : 20539, 60, getOwner()).useNoAnimationSkill();
			}
		}, 500);
	}
	
	private void starLifeTask()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () -> despawn(), 7000);
	}
	
	void despawn()
	{
		if (!isAlreadyDead())
		{
			AI2Actions.deleteOwner(this);
		}
	}
}