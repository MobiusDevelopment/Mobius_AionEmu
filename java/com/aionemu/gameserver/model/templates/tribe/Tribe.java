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
package com.aionemu.gameserver.model.templates.tribe;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.TribeClass;

/**
 * @author ATracer
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Tribe")
public class Tribe
{
	@XmlList
	protected List<TribeClass> aggressive;
	
	@XmlList
	protected List<TribeClass> hostile;
	
	@XmlList
	protected List<TribeClass> friendly;
	
	@XmlList
	protected List<TribeClass> neutral;
	
	@XmlList
	protected List<TribeClass> none;
	
	@XmlList
	protected List<TribeClass> support;
	
	@XmlAttribute
	protected TribeClass base = TribeClass.NONE;
	
	@XmlAttribute(required = true)
	protected TribeClass name;
	
	public List<TribeClass> getAggressive()
	{
		if (aggressive == null)
		{
			aggressive = Collections.emptyList();
		}
		return aggressive;
	}
	
	public List<TribeClass> getHostile()
	{
		if (hostile == null)
		{
			hostile = Collections.emptyList();
		}
		return hostile;
	}
	
	public List<TribeClass> getFriendly()
	{
		if (friendly == null)
		{
			friendly = Collections.emptyList();
		}
		return friendly;
	}
	
	public List<TribeClass> getNeutral()
	{
		if (neutral == null)
		{
			neutral = Collections.emptyList();
		}
		return neutral;
	}
	
	public List<TribeClass> getNone()
	{
		if (none == null)
		{
			none = Collections.emptyList();
		}
		return none;
	}
	
	public List<TribeClass> getSupport()
	{
		if (support == null)
		{
			support = Collections.emptyList();
		}
		return support;
	}
	
	public TribeClass getBase()
	{
		return base;
	}
	
	public TribeClass getName()
	{
		return name;
	}
	
	public final boolean isGuard()
	{
		return name.isGuard();
	}
	
	public final boolean isBasic()
	{
		return name.isBasicClass();
	}
	
	@Override
	public String toString()
	{
		return name + " (" + base + ")";
	}
}