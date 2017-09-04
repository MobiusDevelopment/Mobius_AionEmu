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
package system.handlers.ai.event.conquestOffering;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.ActionItemNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("conquest_npc_buff")
public class Conquest_Npc_BuffAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		super.handleDialogStart(player);
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		switch (getNpcId())
		{
			case 856175: // Pawrunerk.
			{
				effectController.removeEffect(21925);
				effectController.removeEffect(21926);
				effectController.removeEffect(21927);
				SkillEngine.getInstance().getSkill(player, 21924, 1, player).useNoAnimationSkill(); // Boost Attack Power.
				break;
			}
			case 856176: // Chitrunerk.
			{
				effectController.removeEffect(21924);
				effectController.removeEffect(21926);
				effectController.removeEffect(21927);
				SkillEngine.getInstance().getSkill(player, 21925, 1, player).useNoAnimationSkill(); // Movement Speed Increase.
				break;
			}
			case 856177: // Rapirunerk.
			{
				effectController.removeEffect(21924);
				effectController.removeEffect(21925);
				effectController.removeEffect(21927);
				SkillEngine.getInstance().getSkill(player, 21926, 1, player).useNoAnimationSkill(); // Attack Speed/Casting Speed Increase.
				break;
			}
			case 856178: // Dandrunerk.
			{
				effectController.removeEffect(21924);
				effectController.removeEffect(21925);
				effectController.removeEffect(21926);
				SkillEngine.getInstance().getSkill(player, 21927, 1, player).useNoAnimationSkill(); // Boost Defense.
				break;
			}
		}
		AI2Actions.deleteOwner(this);
	}
}