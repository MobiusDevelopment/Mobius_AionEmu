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
package com.aionemu.gameserver.services.nightmarecircusservice;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusLocation;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusStateType;
import com.aionemu.gameserver.services.NightmareCircusService;

/**
 * @author Rinzler (Encom)
 * @param <CL>
 */
public abstract class CircusInstance<CL extends NightmareCircusLocation>
{
	private boolean started;
	private final CL nightmareCircusLocation;
	
	protected abstract void stopNightmareCircus();
	
	protected abstract void startNightmareCircus();
	
	private final AtomicBoolean closed = new AtomicBoolean();
	
	public CircusInstance(CL nightmareCircusLocation)
	{
		this.nightmareCircusLocation = nightmareCircusLocation;
	}
	
	public final void start()
	{
		boolean doubleStart = false;
		synchronized (this)
		{
			if (started)
			{
				doubleStart = true;
			}
			else
			{
				started = true;
			}
		}
		if (doubleStart)
		{
			return;
		}
		startNightmareCircus();
	}
	
	public final void stop()
	{
		if (closed.compareAndSet(false, true))
		{
			stopNightmareCircus();
		}
	}
	
	protected void spawn(NightmareCircusStateType type)
	{
		NightmareCircusService.getInstance().spawn(getNightmareCircusLocation(), type);
	}
	
	protected void despawn()
	{
		NightmareCircusService.getInstance().despawn(getNightmareCircusLocation());
	}
	
	public boolean isClosed()
	{
		return closed.get();
	}
	
	public CL getNightmareCircusLocation()
	{
		return nightmareCircusLocation;
	}
	
	public int getNightmareCircusLocationId()
	{
		return nightmareCircusLocation.getId();
	}
}