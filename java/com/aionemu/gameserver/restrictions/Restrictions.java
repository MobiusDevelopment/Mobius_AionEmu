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
package com.aionemu.gameserver.restrictions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.model.Skill;

/**
 * @author lord_rex
 */
public interface Restrictions
{
	boolean isRestricted(Player player, Class<? extends Restrictions> callingRestriction);
	
	boolean canAttack(Player player, VisibleObject target);
	
	boolean canAffectBySkill(Player player, VisibleObject target, Skill skill);
	
	boolean canUseSkill(Player player, Skill skill);
	
	boolean canChat(Player player);
	
	boolean canInviteToGroup(Player player, Player target);
	
	boolean canInviteToAlliance(Player player, Player target);
	
	boolean canInviteToLeague(Player player, Player target);
	
	boolean canChangeEquip(Player player);
	
	boolean canUseWarehouse(Player player);
	
	boolean canTrade(Player player);
	
	boolean canUseItem(Player player, Item item);
}