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
package com.aionemu.gameserver.model.templates.siegelocation;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.siege.SiegeType;

/**
 * @author Sarynth modified by antness & Source & Wakizashi
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "siegelocation")
public class SiegeLocationTemplate
{
	@XmlAttribute(name = "id")
	protected int id;
	
	@XmlAttribute(name = "type")
	protected SiegeType type;
	
	@XmlAttribute(name = "world")
	protected int world;
	
	@XmlElement(name = "artifact_activation")
	protected ArtifactActivation artifactActivation;
	
	@XmlElement(name = "door_repair")
	protected DoorRepair doorRepair;
	
	@XmlElement(name = "siege_reward")
	protected List<SiegeReward> siegeRewards;
	
	@XmlElement(name = "legion_reward")
	protected List<SiegeLegionReward> siegeLegionRewards;
	
	@XmlAttribute(name = "name_id")
	protected int nameId = 0;
	
	@XmlAttribute(name = "buff_id")
	protected int buffId = 0;
	
	@XmlAttribute(name = "buff_idA")
	protected int buffIdA = 0;
	
	@XmlAttribute(name = "buff_idE")
	protected int buffIdE = 0;
	
	@XmlAttribute(name = "owner_gp")
	protected int ownerGp = 0;
	
	@XmlAttribute(name = "repeat_count")
	protected int repeatCount = 1;
	
	@XmlAttribute(name = "repeat_interval")
	protected int repeatInterval = 1;
	
	@XmlAttribute(name = "siege_duration")
	protected int siegeDuration;
	
	@XmlAttribute(name = "influence")
	protected int influenceValue;
	
	@XmlAttribute(name = "occupy_count")
	protected int occupyCount = 0;
	
	@XmlList
	@XmlAttribute(name = "fortress_dependency")
	protected List<Integer> fortressDependency;
	
	// Luna Shop 5.0.5
	@XmlElement(name = "luna_boost_price")
	protected List<LunaBoostPrice> lunaBoostPrice;
	@XmlElement(name = "luna_teleport_price")
	protected List<LunaTeleportPrice> lunaTeleportPrice;
	@XmlElement(name = "luna_reward")
	protected List<LunaReward> lunaReward;
	@XmlElement(name = "luna_teleport")
	protected List<LunaTeleport> lunaTeleport;
	
	public int getId()
	{
		return id;
	}
	
	public SiegeType getType()
	{
		return type;
	}
	
	public int getWorldId()
	{
		return world;
	}
	
	public ArtifactActivation getActivation()
	{
		return artifactActivation;
	}
	
	public DoorRepair getRepair()
	{
		return doorRepair;
	}
	
	public List<SiegeReward> getSiegeRewards()
	{
		return siegeRewards;
	}
	
	public List<SiegeLegionReward> getSiegeLegionRewards()
	{
		return siegeLegionRewards;
	}
	
	// Luna Shop 5.0.5
	public List<LunaBoostPrice> getLunaBoostPrice()
	{
		return lunaBoostPrice;
	}
	
	public List<LunaTeleportPrice> getLunaTeleportPrice()
	{
		return lunaTeleportPrice;
	}
	
	public List<LunaReward> getLunaReward()
	{
		return lunaReward;
	}
	
	public List<LunaTeleport> getLunaTeleport()
	{
		return lunaTeleport;
	}
	
	public int getNameId()
	{
		return nameId;
	}
	
	public int getBuffId()
	{
		return buffId;
	}
	
	public int getBuffIdA()
	{
		return buffIdA;
	}
	
	public int getBuffIdE()
	{
		return buffIdE;
	}
	
	public int getOwnerGp()
	{
		return ownerGp;
	}
	
	public int getOccupyCount()
	{
		return occupyCount;
	}
	
	public int getRepeatCount()
	{
		return repeatCount;
	}
	
	public int getRepeatInterval()
	{
		return repeatInterval;
	}
	
	public List<Integer> getFortressDependency()
	{
		if (fortressDependency == null)
		{
			return Collections.emptyList();
		}
		return fortressDependency;
	}
	
	public int getSiegeDuration()
	{
		return siegeDuration;
	}
	
	public int getInfluenceValue()
	{
		return influenceValue;
	}
}