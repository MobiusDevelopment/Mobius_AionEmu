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
package com.aionemu.gameserver.dataholders;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

import com.aionemu.gameserver.model.templates.event.EventTemplate;

import gnu.trove.map.hash.THashMap;

/**
 * <p>
 * Java class for EventData complex type.
 * <p>
 * The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="EventData">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="event" maxOccurs="unbounded" minOccurs="0">
 *           &lt;complexType>
 *             &lt;complexContent>
 *               &lt;extension base="{}EventTemplate">
 *               &lt;/extension>
 *             &lt;/complexContent>
 *           &lt;/complexType>
 *         &lt;/element>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "events_config")
public class EventData
{
	@XmlElementWrapper(name = "events")
	@XmlElement(name = "event")
	protected List<EventTemplate> events;
	
	@XmlTransient
	protected String active;
	
	@XmlTransient
	private final THashMap<String, EventTemplate> activeEvents = new THashMap<>();
	
	@XmlTransient
	private final THashMap<String, EventTemplate> allEvents = new THashMap<>();
	
	@XmlTransient
	private int counter = 0;
	
	void afterUnmarshal(Unmarshaller u, Object parent)
	{
		if (events == null)
		{
			return;
		}
		
		counter = 0;
		allEvents.clear();
		activeEvents.clear();
		
		for (EventTemplate ev : events)
		{
			if (ev.isActive())
			{
				activeEvents.put(ev.getName(), ev);
				active += ev.getName() + ", ";
				counter++;
			}
			allEvents.put(ev.getName(), ev);
		}
		if ((active != null) && !active.isEmpty())
		{
			active = active.substring(0, active.lastIndexOf(", "));
			active += ".";
		}
		
		events.clear();
		events = null;
	}
	
	public int size()
	{
		return counter;
	}
	
	public String getActiveText()
	{
		return active;
	}
	
	public List<EventTemplate> getAllEvents()
	{
		final List<EventTemplate> result = new ArrayList<>();
		synchronized (allEvents)
		{
			result.addAll(allEvents.values());
		}
		
		return result;
	}
	
	public void setAllEvents(List<EventTemplate> events, String active)
	{
		if (events == null)
		{
			events = new ArrayList<>();
		}
		this.events = events;
		
		for (EventTemplate et : this.events)
		{
			if (allEvents.containsKey(et.getName()))
			{
				final EventTemplate oldEvent = allEvents.get(et.getName());
				if (oldEvent.isActive() && oldEvent.isStarted())
				{
					et.setStarted();
				}
			}
		}
		afterUnmarshal(null, null);
	}
	
	public List<EventTemplate> getActiveEvents()
	{
		final List<EventTemplate> result = new ArrayList<>();
		synchronized (activeEvents)
		{
			result.addAll(activeEvents.values());
		}
		
		return result;
	}
	
	public boolean Contains(String eventName)
	{
		return activeEvents.containsKey(eventName);
	}
}
