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
package com.aionemu.gameserver.model.gameobjects;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author ATracer
 */
public enum PetAction
{
	ADOPT(1),
	SURRENDER(2),
	SPAWN(3),
	DISMISS(4),
	TALK_WITH_MERCHANT(6),
	TALK_WITH_MINDER(7),
	FOOD(9),
	RENAME(10),
	MOOD(12),
	UNKNOWN(255);
	
	private static TIntObjectHashMap<PetAction> petActions;
	
	static
	{
		petActions = new TIntObjectHashMap<>();
		for (final PetAction action : values())
		{
			petActions.put(action.getActionId(), action);
		}
	}
	
	private int actionId;
	
	private PetAction(int actionId)
	{
		this.actionId = actionId;
	}
	
	public int getActionId()
	{
		return actionId;
	}
	
	public static PetAction getActionById(int actionId)
	{
		final PetAction action = petActions.get(actionId);
		return action != null ? action : UNKNOWN;
	}
}
