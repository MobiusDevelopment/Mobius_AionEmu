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
package com.aionemu.gameserver.model.landing_special;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.landing_special.LandingSpecialTemplate;
import com.aionemu.gameserver.services.abysslandingservice.landingspecialservice.SpecialLanding;

import javolution.util.FastMap;

public class LandingSpecialLocation
{
	protected int id;
	protected boolean isActive;
	protected LandingSpecialStateType type;
	protected LandingSpecialTemplate template;
	protected SpecialLanding<LandingSpecialLocation> activeLandingSpecial;
	protected FastMap<Integer, Player> players = new FastMap<>();
	private final List<VisibleObject> spawned = new ArrayList<>();
	
	public LandingSpecialLocation()
	{
	}
	
	public LandingSpecialLocation(LandingSpecialTemplate template)
	{
		this.template = template;
		id = template.getId();
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActiveLanding(SpecialLanding<LandingSpecialLocation> landingSpecial)
	{
		isActive = landingSpecial != null;
		activeLandingSpecial = landingSpecial;
	}
	
	public SpecialLanding<LandingSpecialLocation> getActiveLandingSpecial()
	{
		return activeLandingSpecial;
	}
	
	public final LandingSpecialTemplate getTemplate()
	{
		return template;
	}
	
	public int getId()
	{
		return id;
	}
	
	public List<VisibleObject> getSpawned()
	{
		return spawned;
	}
	
	public FastMap<Integer, Player> getPlayers()
	{
		return players;
	}
	
	public void setType(LandingSpecialStateType type)
	{
		this.type = type;
	}
	
	public LandingSpecialStateType getType()
	{
		return type;
	}
}