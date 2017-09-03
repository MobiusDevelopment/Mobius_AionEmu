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
package system.handlers.ai.rvr;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.ActionItemNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("field_gun")
public class Field_GunAI2 extends ActionItemNpcAI2
{
	private final AtomicBoolean canUse = new AtomicBoolean(true);
	
	@Override
	protected void handleDialogStart(Player player)
	{
		super.handleDialogStart(player);
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		if (canUse.compareAndSet(true, false))
		{
			final int morphSkill = getMorphSkill();
			SkillEngine.getInstance().getSkill(getOwner(), morphSkill >> 8, morphSkill & 0xFF, player).useNoAnimationSkill();
			AI2Actions.deleteOwner(this);
		}
	}
	
	private int getMorphSkill()
	{
		switch (getNpcId())
		{
			case 209472: // Baltasar Hill Field Gun.
			case 831339: // Asmodians Field Gun.
				return 0x4F8D3C;
			case 209471: // Jamanok Inn Field Gun.
			case 831338: // Elyos Field Gun.
				return 0x4F8C3C;
		}
		return 0;
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}