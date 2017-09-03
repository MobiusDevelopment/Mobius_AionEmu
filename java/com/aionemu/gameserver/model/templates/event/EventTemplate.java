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
package com.aionemu.gameserver.model.templates.event;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.SpawnsData2;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.templates.Guides.GuideTemplate;
import com.aionemu.gameserver.model.templates.spawns.Spawn;
import com.aionemu.gameserver.model.templates.spawns.SpawnMap;
import com.aionemu.gameserver.model.templates.spawns.SpawnSpotTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;
import com.aionemu.gameserver.world.World;

/**
 * @author Rolandas
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "EventTemplate")
public class EventTemplate
{
	private static Logger log = LoggerFactory.getLogger(EventTemplate.class);
	
	@XmlElement(name = "event_drops", required = false)
	protected EventDrops eventDrops;
	
	@XmlElement(name = "quests", required = false)
	protected EventQuestList quests;
	
	@XmlElement(name = "spawns", required = false)
	protected SpawnsData2 spawns;
	
	@XmlElement(name = "inventory_drop", required = false)
	protected InventoryDrop inventoryDrop;
	
	@XmlList
	@XmlElement(name = "surveys", required = false)
	protected List<String> surveys;
	
	@XmlAttribute(name = "name", required = true)
	protected String name;
	
	@XmlAttribute(name = "start", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar startDate;
	
	@XmlAttribute(name = "end", required = true)
	@XmlSchemaType(name = "dateTime")
	protected XMLGregorianCalendar endDate;
	
	@XmlAttribute(name = "theme", required = false)
	private String theme;
	
	@XmlTransient
	protected List<VisibleObject> spawnedObjects;
	
	@XmlTransient
	private Future<?> invDropTask = null;
	
	public String getName()
	{
		return name;
	}
	
	public EventDrops EventDrop()
	{
		return eventDrops;
	}
	
	public DateTime getStartDate()
	{
		return DateTimeUtil.getDateTime(startDate.toGregorianCalendar());
	}
	
	public DateTime getEndDate()
	{
		return DateTimeUtil.getDateTime(endDate.toGregorianCalendar());
	}
	
	public List<Integer> getStartableQuests()
	{
		if (quests == null)
		{
			return new ArrayList<>();
		}
		return quests.getStartableQuests();
	}
	
	public List<Integer> getMaintainableQuests()
	{
		if (quests == null)
		{
			return new ArrayList<>();
		}
		return quests.getMaintainQuests();
	}
	
	public boolean isActive()
	{
		return getStartDate().isBeforeNow() && getEndDate().isAfterNow();
	}
	
	public boolean isExpired()
	{
		return !isActive();
	}
	
	@XmlTransient
	volatile boolean isStarted = false;
	
	public void setStarted()
	{
		isStarted = true;
	}
	
	public boolean isStarted()
	{
		return isStarted;
	}
	
	public void Start()
	{
		if (isStarted)
		{
			return;
		}
		
		if ((spawns != null) && (spawns.size() > 0))
		{
			if (spawnedObjects == null)
			{
				spawnedObjects = new ArrayList<>();
			}
			for (SpawnMap map : spawns.getTemplates())
			{
				DataManager.SPAWNS_DATA2.addNewSpawnMap(map);
				final Collection<Integer> instanceIds = World.getInstance().getWorldMap(map.getMapId()).getAvailableInstanceIds();
				for (Integer instanceId : instanceIds)
				{
					int spawnCount = 0;
					for (Spawn spawn : map.getSpawns())
					{
						spawn.setEventTemplate(this);
						for (SpawnSpotTemplate spot : spawn.getSpawnSpotTemplates())
						{
							final SpawnTemplate t = SpawnEngine.addNewSpawn(map.getMapId(), spawn.getNpcId(), spot.getX(), spot.getY(), spot.getZ(), spot.getHeading(), spawn.getRespawnTime());
							t.setEventTemplate(this);
							SpawnEngine.spawnObject(t, instanceId);
							spawnCount++;
						}
					}
					log.info("Spawned event objects in " + map.getMapId() + " [" + instanceId + "] : " + spawnCount + " (" + getName() + ")");
				}
			}
			DataManager.SPAWNS_DATA2.afterUnmarshal(null, null);
			DataManager.SPAWNS_DATA2.clearTemplates();
		}
		
		if (inventoryDrop != null)
		{
			invDropTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> World.getInstance().doOnAllPlayers(player ->
			{
				if (player.getCommonData().getLevel() >= inventoryDrop.getStartLevel())
				{
					ItemService.dropItemToInventory(player, inventoryDrop.getDropItem());
				}
			}), inventoryDrop.getInterval() * 60000, inventoryDrop.getInterval() * 60000);
		}
		
		if (surveys != null)
		{
			for (String survey : surveys)
			{
				final GuideTemplate template = DataManager.GUIDE_HTML_DATA.getTemplateByTitle(survey);
				if (template != null)
				{
					template.setActivated(true);
				}
			}
		}
		
		isStarted = true;
	}
	
	public void Stop()
	{
		if (!isStarted)
		{
			return;
		}
		
		if (spawnedObjects != null)
		{
			for (VisibleObject o : spawnedObjects)
			{
				if (o.isSpawned())
				{
					o.getController().delete();
				}
			}
			DataManager.SPAWNS_DATA2.removeEventSpawnObjects(spawnedObjects);
			log.info("Despawned " + spawnedObjects.size() + " event objects (" + getName() + ")");
			spawnedObjects.clear();
			spawnedObjects = null;
		}
		
		if (invDropTask != null)
		{
			invDropTask.cancel(false);
			invDropTask = null;
		}
		
		if (surveys != null)
		{
			for (String survey : surveys)
			{
				final GuideTemplate template = DataManager.GUIDE_HTML_DATA.getTemplateByTitle(survey);
				if (template != null)
				{
					template.setActivated(false);
				}
			}
		}
		
		isStarted = false;
	}
	
	public void addSpawnedObject(VisibleObject object)
	{
		if (spawnedObjects == null)
		{
			spawnedObjects = new ArrayList<>();
		}
		spawnedObjects.add(object);
	}
	
	/**
	 * @return the theme name
	 */
	public String getTheme()
	{
		if (theme != null)
		{
			return theme.toLowerCase();
		}
		return theme;
	}
	
}
