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
package com.aionemu.gameserver.world;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.instance.handlers.InstanceHandler;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.alliance.PlayerAlliance;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.model.team2.league.League;
import com.aionemu.gameserver.model.templates.quest.QuestNpc;
import com.aionemu.gameserver.model.templates.world.WorldMapTemplate;
import com.aionemu.gameserver.model.templates.zone.ZoneClassName;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.world.exceptions.DuplicateAionObjectException;
import com.aionemu.gameserver.world.knownlist.Visitor;
import com.aionemu.gameserver.world.zone.RegionZone;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;
import com.aionemu.gameserver.world.zone.ZoneService;

import gnu.trove.map.hash.TIntObjectHashMap;
import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * World map instance object.
 * @author -Nemesiss-
 */
public abstract class WorldMapInstance
{
	
	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(WorldMapInstance.class);
	/**
	 * Size of region
	 */
	public static final int regionSize = WorldConfig.WORLD_REGION_SIZE;
	/**
	 * WorldMap witch is parent of this instance.
	 */
	private final WorldMap parent;
	/**
	 * Map of active regions.
	 */
	protected final TIntObjectHashMap<MapRegion> regions = new TIntObjectHashMap<>();
	
	/**
	 * All objects spawned in this world map instance
	 */
	private final Map<Integer, VisibleObject> worldMapObjects = new FastMap<Integer, VisibleObject>().shared();
	
	/**
	 * All players spawned in this world map instance
	 */
	private final FastMap<Integer, Player> worldMapPlayers = new FastMap<Integer, Player>().shared();
	
	private final Set<Integer> registeredObjects = Collections.newSetFromMap(new FastMap<Integer, Boolean>().shared());
	
	private PlayerGroup registeredGroup = null;
	
	private Future<?> emptyInstanceTask = null;
	
	/**
	 * Id of this instance (channel)
	 */
	private final int instanceId;
	
	private final FastList<Integer> questIds = new FastList<>();
	
	private InstanceHandler instanceHandler;
	
	private Map<ZoneName, ZoneInstance> zones = new HashMap<>();
	
	// TODO: Merge this with owner
	private Integer soloPlayer;
	
	private PlayerAlliance registredAlliance;
	private League registredLeague;
	
	/**
	 * Constructor.
	 * @param parent
	 */
	public WorldMapInstance(WorldMap parent, int instanceId)
	{
		this.parent = parent;
		this.instanceId = instanceId;
		zones = ZoneService.getInstance().getZoneInstancesByWorldId(parent.getMapId());
		initMapRegions();
	}
	
	/**
	 * Return World map id.
	 * @return world map id
	 */
	public Integer getMapId()
	{
		return getParent().getMapId();
	}
	
	/**
	 * Returns WorldMap witch is parent of this instance
	 * @return parent
	 */
	public WorldMap getParent()
	{
		return parent;
	}
	
	public WorldMapTemplate getTemplate()
	{
		return parent.getTemplate();
	}
	
	/**
	 * Returns MapRegion that contains coordinates of VisibleObject. If the region doesn't exist, it's created.
	 * @param object
	 * @return a MapRegion
	 */
	MapRegion getRegion(VisibleObject object)
	{
		return getRegion(object.getX(), object.getY(), object.getZ());
	}
	
	/**
	 * Returns MapRegion that contains given x,y coordinates. If the region doesn't exist, it's created.
	 * @param x
	 * @param y
	 * @return a MapRegion
	 */
	public abstract MapRegion getRegion(float x, float y, float z);
	
	/**
	 * Create new MapRegion and add link to neighbours.
	 * @param regionId
	 * @return newly created map region
	 */
	protected abstract MapRegion createMapRegion(int regionId);
	
	protected abstract void initMapRegions();
	
	public abstract boolean isPersonal();
	
	public abstract int getOwnerId();
	
	/**
	 * Returs {@link World} instance to which belongs this WorldMapInstance
	 * @return World
	 */
	public World getWorld()
	{
		return getParent().getWorld();
	}
	
	/**
	 * @param object
	 */
	public void addObject(VisibleObject object)
	{
		if (worldMapObjects.put(object.getObjectId(), object) != null)
		{
			throw new DuplicateAionObjectException("Object with templateId " + String.valueOf(object.getObjectTemplate().getTemplateId()) + " already spawned in the instance " + String.valueOf(getMapId()) + " " + String.valueOf(getInstanceId()));
		}
		if (object instanceof Npc)
		{
			final QuestNpc data = QuestEngine.getInstance().getQuestNpc(((Npc) object).getNpcId());
			if (data != null)
			{
				for (final int id : data.getOnQuestStart())
				{
					if (!questIds.contains(id))
					{
						questIds.add(id);
					}
				}
			}
		}
		if (object instanceof Player)
		{
			if (getParent().isPossibleFly())
			{
				((Player) object).setInsideZoneType(ZoneType.FLY);
			}
			worldMapPlayers.put(object.getObjectId(), (Player) object);
		}
	}
	
	/**
	 * @param object
	 */
	public void removeObject(AionObject object)
	{
		worldMapObjects.remove(object.getObjectId());
		if (object instanceof Player)
		{
			if (getParent().isPossibleFly())
			{
				((Player) object).unsetInsideZoneType(ZoneType.FLY);
			}
			worldMapPlayers.remove(object.getObjectId());
		}
	}
	
	/**
	 * @param npcId
	 * @return npc
	 */
	public Npc getNpc(int npcId)
	{
		for (final Iterator<VisibleObject> iter = objectIterator(); iter.hasNext();)
		{
			final VisibleObject obj = iter.next();
			if (obj instanceof Npc)
			{
				final Npc npc = (Npc) obj;
				if (npc.getNpcId() == npcId)
				{
					return npc;
				}
			}
		}
		return null;
	}
	
	public List<Player> getPlayersInside()
	{
		final List<Player> playersInside = new ArrayList<>();
		final Iterator<Player> players = playerIterator();
		while (players.hasNext())
		{
			playersInside.add(players.next());
		}
		return playersInside;
	}
	
	/**
	 * @param npcId
	 * @return List<npc>
	 */
	public List<Npc> getNpcs(int npcId)
	{
		final List<Npc> npcs = new ArrayList<>();
		for (final Iterator<VisibleObject> iter = objectIterator(); iter.hasNext();)
		{
			final VisibleObject obj = iter.next();
			if (obj instanceof Npc)
			{
				final Npc npc = (Npc) obj;
				if (npc.getNpcId() == npcId)
				{
					npcs.add(npc);
				}
			}
		}
		return npcs;
	}
	
	/**
	 * @return List<npcs>
	 */
	public List<Npc> getNpcs()
	{
		final List<Npc> npcs = new ArrayList<>();
		for (final Iterator<VisibleObject> iter = objectIterator(); iter.hasNext();)
		{
			final VisibleObject obj = iter.next();
			if (obj instanceof Npc)
			{
				npcs.add((Npc) obj);
			}
		}
		return npcs;
	}
	
	/**
	 * @return List<doors>
	 */
	public Map<Integer, StaticDoor> getDoors()
	{
		final Map<Integer, StaticDoor> doors = new HashMap<>();
		for (final Iterator<VisibleObject> iter = objectIterator(); iter.hasNext();)
		{
			final VisibleObject obj = iter.next();
			if (obj instanceof StaticDoor)
			{
				final StaticDoor door = (StaticDoor) obj;
				doors.put(door.getSpawn().getEntityId(), door);
			}
		}
		return doors;
	}
	
	/**
	 * @return List<트랩>
	 */
	public List<Trap> getTraps(Creature p)
	{
		final List<Trap> traps = new ArrayList<>();
		for (final Iterator<VisibleObject> iter = objectIterator(); iter.hasNext();)
		{
			final VisibleObject obj = iter.next();
			if (obj instanceof Trap)
			{
				final Trap t = (Trap) obj;
				if (t.getCreatorId() == p.getObjectId())
				{
					traps.add(t);
				}
			}
		}
		return traps;
	}
	
	/**
	 * @return the instanceIndex
	 */
	public int getInstanceId()
	{
		return instanceId;
	}
	
	public final boolean isBeginnerInstance()
	{
		if (parent == null)
		{
			return false;
		}
		
		if (parent.getTemplate().isInstance())
		{
			// TODO: check Karamatis and Ataxiar for exception in FastTrack ?
			// return parent.getTemplate().getBeginnerTwinCount() > 0;
			return false;
		}
		
		int twinCount = parent.getTemplate().getTwinCount();
		if (twinCount == 0)
		{
			twinCount = 1;
		}
		return getInstanceId() > twinCount;
	}
	
	/**
	 * Check player is in instance
	 * @param objId
	 * @return
	 */
	public boolean isInInstance(int objId)
	{
		return worldMapPlayers.containsKey(objId);
	}
	
	/**
	 * @return
	 */
	public Iterator<VisibleObject> objectIterator()
	{
		return worldMapObjects.values().iterator();
	}
	
	/**
	 * @return
	 */
	public Iterator<Player> playerIterator()
	{
		return worldMapPlayers.values().iterator();
	}
	
	public void registerGroup(PlayerGroup group)
	{
		registeredGroup = group;
		register(group.getTeamId());
	}
	
	public void registerGroup(PlayerAlliance group)
	{
		registredAlliance = group;
		register(group.getObjectId());
	}
	
	public void registerGroup(League group)
	{
		registredLeague = group;
		register(group.getObjectId());
	}
	
	public PlayerAlliance getRegistredAlliance()
	{
		return registredAlliance;
	}
	
	public League getRegistredLeague()
	{
		return registredLeague;
	}
	
	/**
	 * @param objectId
	 */
	public void register(int objectId)
	{
		registeredObjects.add(objectId);
	}
	
	/**
	 * @param objectId
	 * @return
	 */
	public boolean isRegistered(int objectId)
	{
		return registeredObjects.contains(objectId);
	}
	
	/**
	 * @return the emptyInstanceTask
	 */
	public Future<?> getEmptyInstanceTask()
	{
		return emptyInstanceTask;
	}
	
	/**
	 * @param emptyInstanceTask the emptyInstanceTask to set
	 */
	public void setEmptyInstanceTask(Future<?> emptyInstanceTask)
	{
		this.emptyInstanceTask = emptyInstanceTask;
	}
	
	/**
	 * @return the registeredGroup
	 */
	public PlayerGroup getRegisteredGroup()
	{
		return registeredGroup;
	}
	
	/**
	 * @return
	 */
	public int playersCount()
	{
		return worldMapPlayers.size();
	}
	
	public FastList<Integer> getQuestIds()
	{
		return questIds;
	}
	
	public final InstanceHandler getInstanceHandler()
	{
		return instanceHandler;
	}
	
	public final void setInstanceHandler(InstanceHandler instanceHandler)
	{
		this.instanceHandler = instanceHandler;
	}
	
	public Player getPlayer(Integer object)
	{
		for (final Player player : worldMapPlayers.values())
		{
			if (object == player.getObjectId())
			{
				return player;
			}
		}
		return null;
	}
	
	/**
	 * @param visitor
	 */
	public void doOnAllPlayers(Visitor<Player> visitor)
	{
		try
		{
			for (final Player player : worldMapPlayers.values())
			{
				if (player != null)
				{
					visitor.visit(player);
				}
			}
		}
		catch (final Exception ex)
		{
			log.error("Exception when running visitor on all players" + ex);
		}
	}
	
	protected ZoneInstance[] filterZones(int mapId, int regionId, float startX, float startY, float minZ, float maxZ)
	{
		final List<ZoneInstance> regionZones = new ArrayList<>();
		final RegionZone regionZone = new RegionZone(startX, startY, minZ, maxZ);
		
		for (final ZoneInstance zoneInstance : zones.values())
		{
			if (zoneInstance.getAreaTemplate().intersectsRectangle(regionZone))
			{
				regionZones.add(zoneInstance);
			}
			else if (zoneInstance.getZoneTemplate().getZoneType() == ZoneClassName.DUMMY)
			{
				log.error("Region " + regionId + " should intersect with whole map zone!!! (map=" + mapId + ")");
			}
		}
		return regionZones.toArray(new ZoneInstance[regionZones.size()]);
	}
	
	/**
	 * @param player
	 * @param zoneName
	 * @return
	 */
	public boolean isInsideZone(VisibleObject object, ZoneName zoneName)
	{
		final ZoneInstance zoneTemplate = zones.get(zoneName);
		if (zoneTemplate == null)
		{
			return false;
		}
		return isInsideZone(object.getPosition(), zoneName);
	}
	
	/**
	 * @param pos
	 * @param zone
	 * @return
	 */
	public boolean isInsideZone(WorldPosition pos, ZoneName zoneName)
	{
		final MapRegion mapRegion = getRegion(pos.getX(), pos.getY(), pos.getZ());
		return mapRegion.isInsideZone(zoneName, pos.getX(), pos.getY(), pos.getZ());
	}
	
	public void setSoloPlayerObj(Integer obj)
	{
		soloPlayer = obj;
	}
	
	public Integer getSoloPlayerObj()
	{
		return soloPlayer;
	}
	
}
