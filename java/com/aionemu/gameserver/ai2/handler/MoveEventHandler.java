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
package com.aionemu.gameserver.ai2.handler;

import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.geo.GeoService;

/**
 * @author ATracer
 */
public class MoveEventHandler
{
	/**
	 * @param npcAI
	 */
	public static void onMoveValidate(NpcAI2 npcAI)
	{
		npcAI.getOwner().getController().onMove();
		TargetEventHandler.onTargetTooFar(npcAI);
		if ((npcAI.getOwner().getTarget() != null) && (npcAI.getOwner().getObjectTemplate().getStatsTemplate().getRunSpeed() != 0))
		{
			if (!GeoService.getInstance().canSee(npcAI.getOwner(), npcAI.getOwner().getTarget()) && !MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), 15))
			{
				npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
			}
		}
	}
	
	/**
	 * @param npcAI
	 */
	public static void onMoveArrived(NpcAI2 npcAI)
	{
		npcAI.getOwner().getController().onMove();
		TargetEventHandler.onTargetReached(npcAI);
		if ((npcAI.getOwner().getTarget() != null) && (npcAI.getOwner().getObjectTemplate().getStatsTemplate().getRunSpeed() != 0))
		{
			if (!GeoService.getInstance().canSee(npcAI.getOwner(), npcAI.getOwner().getTarget()) && !MathUtil.isInRange(npcAI.getOwner(), npcAI.getOwner().getTarget(), 15))
			{
				npcAI.onGeneralEvent(AIEventType.TARGET_GIVEUP);
			}
		}
	}
}
