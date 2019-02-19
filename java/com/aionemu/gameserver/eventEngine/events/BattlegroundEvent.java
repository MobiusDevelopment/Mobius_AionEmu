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
package com.aionemu.gameserver.eventEngine.events;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.eventEngine.Event;
import com.aionemu.gameserver.services.events.LadderService;

/**
 * Created by wanke on 12/02/2017.
 */
public class BattlegroundEvent extends Event
{
	private final List<Integer> battlegrounds = new ArrayList<>();
	
	@Override
	public void execute()
	{
		LadderService.getInstance().createNormalBgs(this);
	}
	
	public int getBgCount()
	{
		return battlegrounds.size();
	}
	
	public void onCreate(Integer bgId)
	{
		if (!battlegrounds.contains(bgId))
		{
			battlegrounds.add(bgId);
		}
	}
	
	public void onEnd(Integer bgId)
	{
		battlegrounds.remove(bgId);
		if (battlegrounds.isEmpty())
		{
			onEnd();
		}
	}
	
	public void onEnd()
	{
		super.finish();
	}
	
	@Override
	protected void onReset()
	{
		battlegrounds.clear();
	}
	
	@Override
	public boolean cancel(boolean mayInterruptIfRunning)
	{
		return false;
	}
}