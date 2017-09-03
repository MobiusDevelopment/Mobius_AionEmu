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
package com.aionemu.gameserver.model.beritra;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.beritra.BeritraTemplate;
import com.aionemu.gameserver.services.beritraservice.BeritraInvasion;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */

public class BeritraLocation
{
	protected int id;
	protected boolean isActive;
	protected BeritraTemplate template;
	protected BeritraInvasion<BeritraLocation> activeBeritra;
	protected FastMap<Integer, Player> players = new FastMap<>();
	private final List<VisibleObject> spawned = new ArrayList<>();
	
	public BeritraLocation()
	{
	}
	
	public BeritraLocation(BeritraTemplate template)
	{
		this.template = template;
		id = template.getId();
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActiveBeritra(BeritraInvasion<BeritraLocation> beritra)
	{
		isActive = beritra != null;
		activeBeritra = beritra;
	}
	
	public BeritraInvasion<BeritraLocation> getActiveBeritra()
	{
		return activeBeritra;
	}
	
	public final BeritraTemplate getTemplate()
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
}