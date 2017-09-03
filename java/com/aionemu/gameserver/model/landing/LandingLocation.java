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
package com.aionemu.gameserver.model.landing;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.landing.LandingTemplate;
import com.aionemu.gameserver.services.abysslandingservice.Landing;

import javolution.util.FastMap;

public class LandingLocation
{
	protected int siege;
	protected int commander;
	protected int artifact;
	protected int base;
	protected int monuments;
	protected int quest;
	protected int facility;
	protected Timestamp levelUpDate;
	protected int id;
	protected int level;
	protected int points;
	protected boolean isActive;
	protected Race race;
	protected LandingTemplate template;
	protected Landing<LandingLocation> activeLanding;
	protected FastMap<Integer, Player> players = new FastMap<>();
	private final List<VisibleObject> spawned = new ArrayList<>();
	private PersistentState persistentState;
	
	public LandingLocation()
	{
	}
	
	public LandingLocation(LandingTemplate template)
	{
		this.template = template;
		id = template.getId();
	}
	
	public boolean isActive()
	{
		return isActive;
	}
	
	public void setActiveLanding(Landing<LandingLocation> landing)
	{
		isActive = landing != null;
		activeLanding = landing;
	}
	
	public Landing<LandingLocation> getActiveLanding()
	{
		return activeLanding;
	}
	
	public final LandingTemplate getTemplate()
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
	
	public int getLevel()
	{
		if (level == 0)
		{
			return level + 1;
		}
		else
		{
			return level;
		}
	}
	
	public void setLevel(int level)
	{
		this.level = level;
	}
	
	public int getPoints()
	{
		return points;
	}
	
	public void setPoints(int pts)
	{
		points = pts;
	}
	
	public int getSiegePoints()
	{
		return siege;
	}
	
	public void setSiegePoints(int pts)
	{
		siege = pts;
	}
	
	public int getCommanderPoints()
	{
		return commander;
	}
	
	public void setCommanderPoints(int pts)
	{
		commander = pts;
	}
	
	public int getArtifactPoints()
	{
		return artifact;
	}
	
	public void setArtifactPoints(int pts)
	{
		artifact = pts;
	}
	
	public int getBasePoints()
	{
		return base;
	}
	
	public void setBasePoints(int pts)
	{
		base = pts;
	}
	
	public int getQuestPoints()
	{
		return quest;
	}
	
	public void setQuestPoints(int pts)
	{
		quest = pts;
	}
	
	public int getFacilityPoints()
	{
		return facility;
	}
	
	public void setFacilityPoints(int pts)
	{
		facility = pts;
	}
	
	public int getMonumentsPoints()
	{
		return monuments;
	}
	
	public void setMonumentsPoints(int pts)
	{
		monuments = pts;
	}
	
	public Timestamp getLevelUpDate()
	{
		return levelUpDate;
	}
	
	public Timestamp setLevelUpDate(Timestamp timestamp)
	{
		return levelUpDate = timestamp;
	}
	
	public PersistentState getPersistentState()
	{
		return persistentState;
	}
	
	public void setPersistentState(PersistentState state)
	{
		if ((persistentState == PersistentState.NEW) && (state == PersistentState.UPDATE_REQUIRED))
		{
			return;
		}
		else
		{
			persistentState = state;
		}
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public Race setRace(Race race)
	{
		return this.race = race;
	}
}