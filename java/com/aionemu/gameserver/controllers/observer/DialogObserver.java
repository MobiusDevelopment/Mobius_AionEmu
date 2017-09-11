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
package com.aionemu.gameserver.controllers.observer;

import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.MathUtil;

/**
 * @author nrg
 */
public abstract class DialogObserver extends ActionObserver
{
	
	private final Player responder;
	private final Creature requester;
	private final int maxDistance;
	
	public DialogObserver(Creature requester, Player responder, int maxDistance)
	{
		super(ObserverType.MOVE);
		this.responder = responder;
		this.requester = requester;
		this.maxDistance = maxDistance;
	}
	
	@Override
	public void moved()
	{
		if (!MathUtil.isIn3dRange(responder, requester, maxDistance))
		{
			tooFar(requester, responder);
		}
	}
	
	/**
	 * Is called when player is too far away from dialog serving object
	 * @param requester
	 * @param responder
	 */
	public abstract void tooFar(Creature requester, Player responder);
}
