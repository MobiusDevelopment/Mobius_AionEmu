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
package system.handlers.ai.siege;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.ActionItemNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("emptyaethericcannon")
public class EmptyAethericCannonAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		super.handleDialogStart(player);
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		final Npc owner = getOwner();
		HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("weapon_siege/weaponSiege.xhtml"));
		SkillEngine.getInstance().getSkill(player, 21385, 1, player).useNoAnimationSkill();
		AI2Actions.deleteOwner(this);
		AI2Actions.scheduleRespawn(this);
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}