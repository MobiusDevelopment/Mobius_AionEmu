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
package com.aionemu.gameserver.services;

import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.schedule.SiegeSchedule;
import com.aionemu.gameserver.configs.schedule.SiegeSchedule.Fortress;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.PlayerInitialData;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.BindPointPosition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.model.siege.OutpostLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.model.templates.world.WeatherEntry;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ABYSS_ARTIFACT_INFO3;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FORTRESS_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INFLUENCE_RATIO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_RIFT_ANNOUNCE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SHIELD_EFFECT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SIEGE_LOCATION_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.siegeservice.ArtifactSiege;
import com.aionemu.gameserver.services.siegeservice.FortressSiege;
import com.aionemu.gameserver.services.siegeservice.OutpostSiege;
import com.aionemu.gameserver.services.siegeservice.Siege;
import com.aionemu.gameserver.services.siegeservice.SiegeAutoRace;
import com.aionemu.gameserver.services.siegeservice.SiegeException;
import com.aionemu.gameserver.services.siegeservice.SiegeStartRunnable;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldType;
import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import javolution.util.FastMap;

/**
 * 3.0 siege update (https://docs.google.com/document/d/1HVOw8-w9AlRp4ci0ei4iAzNaSKzAHj_xORu-qIQJFmc/edit#)
 * @author SoulKeeper, Source
 */
public class SiegeService
{
	/**
	 * Just a logger
	 */
	private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	
	/**
	 * Balaur protector spawn schedule.
	 */
	private static final String SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE = "0 0 * ? * *";
	
	/**
	 * Singleton that is loaded on the class initialization. Guys, we really do not SingletonHolder classes
	 */
	private static final SiegeService instance = new SiegeService();
	
	/**
	 * Map that holds fortressId to Siege. We can easily know what fortresses is under siege ATM :)
	 */
	private final Map<Integer, Siege<?>> activeSieges = new FastMap<Integer, Siege<?>>().shared();
	
	/**
	 * Object that holds siege schedule.<br>
	 * And maybe other useful information (in future).
	 */
	private SiegeSchedule siegeSchedule;
	
	private Map<Integer, ArtifactLocation> artifacts;
	private Map<Integer, FortressLocation> fortresses;
	private Map<Integer, OutpostLocation> outposts;
	private Map<Integer, SiegeLocation> locations;
	
	public static SiegeService getInstance()
	{
		return instance;
	}
	
	public void initSiegeLocations()
	{
		if (SiegeConfig.SIEGE_ENABLED)
		{
			log.info("Initializing sieges...");
			if (siegeSchedule != null)
			{
				log.error("SiegeService should not be initialized two times!");
				return;
			}
			artifacts = DataManager.SIEGE_LOCATION_DATA.getArtifacts();
			fortresses = DataManager.SIEGE_LOCATION_DATA.getFortress();
			outposts = DataManager.SIEGE_LOCATION_DATA.getOutpost();
			locations = DataManager.SIEGE_LOCATION_DATA.getSiegeLocations();
			DAOManager.getDAO(SiegeDAO.class).loadSiegeLocations(locations);
		}
		else
		{
			artifacts = Collections.emptyMap();
			fortresses = Collections.emptyMap();
			outposts = Collections.emptyMap();
			locations = Collections.emptyMap();
			log.info("Sieges are disabled in config.");
		}
	}
	
	@SuppressWarnings("deprecation")
	public void initSieges()
	{
		if (!SiegeConfig.SIEGE_ENABLED)
		{
			return;
		}
		for (Integer i : getSiegeLocations().keySet())
		{
			deSpawnNpcs(i);
		}
		for (FortressLocation f : getFortresses().values())
		{
			spawnNpcs(f.getLocationId(), f.getRace(), SiegeModType.PEACE);
		}
		for (OutpostLocation o : getOutposts().values())
		{
			if ((SiegeRace.BALAUR != o.getRace()) && (o.getLocationRace() != o.getRace()))
			{
				spawnNpcs(o.getLocationId(), o.getRace(), SiegeModType.PEACE);
			}
		}
		for (ArtifactLocation a : getStandaloneArtifacts().values())
		{
			spawnNpcs(a.getLocationId(), a.getRace(), SiegeModType.PEACE);
		}
		siegeSchedule = SiegeSchedule.load();
		for (Fortress f : siegeSchedule.getFortressesList())
		{
			for (String siegeTime : f.getSiegeTimes())
			{
				CronService.getInstance().schedule(new SiegeStartRunnable(f.getId()), siegeTime);
			}
		}
		for (ArtifactLocation artifact : artifacts.values())
		{
			if (artifact.isStandAlone())
			{
				log.debug("Starting siege of artifact #" + artifact.getLocationId());
				startSiege(artifact.getLocationId());
			}
			else
			{
				log.debug("Artifact #" + artifact.getLocationId() + " siege was not started, it belongs to fortress");
			}
		}
		updateFortressNextState();
		CronService.getInstance().schedule(() ->
		{
			updateFortressNextState();
			World.getInstance().doOnAllPlayers(player ->
			{
				for (FortressLocation fortress1 : getFortresses().values())
				{
					PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress1.getLocationId(), false));
				}
				PacketSendUtility.sendPacket(player, new SM_FORTRESS_STATUS());
				for (FortressLocation fortress2 : getFortresses().values())
				{
					PacketSendUtility.sendPacket(player, new SM_FORTRESS_INFO(fortress2.getLocationId(), true));
				}
			});
		}, SIEGE_LOCATION_STATUS_BROADCAST_SCHEDULE);
	}
	
	public void checkSiegeStart(int locationId)
	{
		if (SiegeConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(locationId))
		{
			SiegeAutoRace.AutoSiegeRace(locationId);
		}
		else
		{
			startSiege(locationId);
		}
	}
	
	public void startSiege(int siegeLocationId)
	{
		// Siege should not be started two times. Never.
		Siege<?> siege;
		synchronized (this)
		{
			if (activeSieges.containsKey(siegeLocationId))
			{
				return;
			}
			siege = newSiege(siegeLocationId);
			activeSieges.put(siegeLocationId, siege);
		}
		
		siege.startSiege();
		
		// certain sieges are endless
		// should end only manually on siege boss death
		if (siege.isEndless())
		{
			return;
		}
		
		// schedule siege end
		ThreadPoolManager.getInstance().schedule(() -> stopSiege(siegeLocationId), siege.getSiegeLocation().getSiegeDuration() * 1000);
	}
	
	public void stopSiege(int siegeLocationId)
	{
		
		log.debug("Stopping siege of siege location: " + siegeLocationId);
		
		// Just a check here...
		// If fortresses was captured in 99% the siege timer will return here
		// without concurrent race
		if (!isSiegeInProgress(siegeLocationId))
		{
			log.debug("Siege of siege location " + siegeLocationId + " is not in progress, it was captured earlier?");
			return;
		}
		
		// We need synchronization here for that 1% of cases :)
		// It may happen that fortresses siege is stopping in the same time by 2 different threads
		// 1 is for killing the boss
		// 2 is for the schedule
		// it might happen that siege will be stopping by other thread, but in such case siege object will be null
		Siege<?> siege;
		synchronized (this)
		{
			siege = activeSieges.remove(siegeLocationId);
		}
		if ((siege == null) || siege.isFinished())
		{
			return;
		}
		
		siege.stopSiege();
	}
	
	/**
	 * Updates next state for fortresses
	 */
	protected void updateFortressNextState()
	{
		
		// get current hour and add 1 hour
		final Calendar currentHourPlus1 = Calendar.getInstance();
		currentHourPlus1.set(Calendar.MINUTE, 0);
		currentHourPlus1.set(Calendar.SECOND, 0);
		currentHourPlus1.set(Calendar.MILLISECOND, 0);
		currentHourPlus1.add(Calendar.HOUR, 1);
		
		// filter fortress siege start runnables
		Map<Runnable, JobDetail> siegeStartRunables = CronService.getInstance().getRunnables();
		siegeStartRunables = Maps.filterKeys(siegeStartRunables, (Predicate<Runnable>) (@Nullable Runnable runnable) -> (runnable instanceof SiegeStartRunnable));
		
		// Create map FortressId-To-AllTriggers
		final Map<Integer, List<Trigger>> siegeIdToStartTriggers = Maps.newHashMap();
		for (Map.Entry<Runnable, JobDetail> entry : siegeStartRunables.entrySet())
		{
			final SiegeStartRunnable fssr = (SiegeStartRunnable) entry.getKey();
			
			List<Trigger> storage = siegeIdToStartTriggers.get(fssr.getLocationId());
			if (storage == null)
			{
				storage = Lists.newArrayList();
				siegeIdToStartTriggers.put(fssr.getLocationId(), storage);
			}
			storage.addAll(CronService.getInstance().getJobTriggers(entry.getValue()));
		}
		
		// update each fortress next state
		for (Map.Entry<Integer, List<Trigger>> entry : siegeIdToStartTriggers.entrySet())
		{
			
			final List<Date> nextFireDates = Lists.newArrayListWithCapacity(entry.getValue().size());
			for (Trigger trigger : entry.getValue())
			{
				nextFireDates.add(trigger.getNextFireTime());
			}
			Collections.sort(nextFireDates);
			
			// clear non-required times
			final Date nextSiegeDate = nextFireDates.get(0);
			final Calendar siegeStartHour = Calendar.getInstance();
			siegeStartHour.setTime(nextSiegeDate);
			siegeStartHour.set(Calendar.MINUTE, 0);
			siegeStartHour.set(Calendar.SECOND, 0);
			siegeStartHour.set(Calendar.MILLISECOND, 0);
			
			// update fortress state that will be valid in 1 h
			final SiegeLocation fortress = getSiegeLocation(entry.getKey());
			
			final Calendar siegeCalendar = Calendar.getInstance();
			siegeCalendar.set(Calendar.MINUTE, 0);
			siegeCalendar.set(Calendar.SECOND, 0);
			siegeCalendar.set(Calendar.MILLISECOND, 0);
			siegeCalendar.add(Calendar.HOUR, 0);
			siegeCalendar.add(Calendar.SECOND, getRemainingSiegeTimeInSeconds(fortress.getLocationId()));
			
			if (SiegeConfig.SIEGE_AUTO_RACE && SiegeAutoRace.isAutoSiege(fortress.getLocationId()))
			{
				siegeStartHour.add(Calendar.HOUR, 1);
			}
			if ((currentHourPlus1.getTimeInMillis() == siegeStartHour.getTimeInMillis()) || (siegeCalendar.getTimeInMillis() > currentHourPlus1.getTimeInMillis()))
			{
				fortress.setNextState(1);
			}
			else
			{
				fortress.setNextState(0);
			}
		}
	}
	
	/**
	 * TODO: WTF is it?
	 * @return seconds before hour end
	 */
	public int getSecondsBeforeHourEnd()
	{
		final Calendar c = Calendar.getInstance();
		final int minutesAsSeconds = c.get(Calendar.MINUTE) * 60;
		final int seconds = c.get(Calendar.SECOND);
		return 3600 - (minutesAsSeconds + seconds);
	}
	
	/**
	 * TODO: Check if it's valid
	 * <p/>
	 * If siege duration is endless - will return -1
	 * @param siegeLocationId Scheduled siege end time
	 * @return remaining seconds in current hour
	 */
	public int getRemainingSiegeTimeInSeconds(int siegeLocationId)
	{
		
		final Siege<?> siege = getSiege(siegeLocationId);
		if ((siege == null) || siege.isFinished())
		{
			return 0;
		}
		
		if (!siege.isStarted())
		{
			return siege.getSiegeLocation().getSiegeDuration();
		}
		
		// endless siege
		if (siege.getSiegeLocation().getSiegeDuration() == -1)
		{
			return -1;
		}
		
		final Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.SECOND, siege.getSiegeLocation().getSiegeDuration());
		
		final int result = (int) ((calendar.getTimeInMillis() - System.currentTimeMillis()) / 1000);
		return result > 0 ? result : 0;
	}
	
	public Siege<?> getSiege(SiegeLocation loc)
	{
		return activeSieges.get(loc.getLocationId());
	}
	
	public Siege<?> getSiege(Integer siegeLocationId)
	{
		return activeSieges.get(siegeLocationId);
	}
	
	public boolean isSiegeInProgress(int fortressId)
	{
		return activeSieges.containsKey(fortressId);
	}
	
	public Map<Integer, OutpostLocation> getOutposts()
	{
		return outposts;
	}
	
	public OutpostLocation getOutpost(int id)
	{
		return outposts.get(id);
	}
	
	public Map<Integer, FortressLocation> getFortresses()
	{
		return fortresses;
	}
	
	public FortressLocation getFortress(int fortressId)
	{
		return fortresses.get(fortressId);
	}
	
	public Map<Integer, ArtifactLocation> getArtifacts()
	{
		return artifacts;
	}
	
	public ArtifactLocation getArtifact(int id)
	{
		return getArtifacts().get(id);
	}
	
	public Map<Integer, ArtifactLocation> getStandaloneArtifacts()
	{
		return Maps.filterValues(artifacts, (Predicate<ArtifactLocation>) (@Nullable ArtifactLocation input) -> (input != null) && input.isStandAlone());
	}
	
	public Map<Integer, ArtifactLocation> getFortressArtifacts()
	{
		return Maps.filterValues(artifacts, (Predicate<ArtifactLocation>) (@Nullable ArtifactLocation input) -> (input != null) && (input.getOwningFortress() != null));
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations()
	{
		return locations;
	}
	
	public SiegeLocation getSiegeLocation(int locationId)
	{
		return locations.get(locationId);
	}
	
	public Map<Integer, SiegeLocation> getSiegeLocations(int worldId)
	{
		final Map<Integer, SiegeLocation> mapLocations = new FastMap<>();
		for (SiegeLocation location : getSiegeLocations().values())
		{
			if (location.getWorldId() == worldId)
			{
				mapLocations.put(location.getLocationId(), location);
			}
		}
		return mapLocations;
	}
	
	protected Siege<?> newSiege(int siegeLocationId)
	{
		if (fortresses.containsKey(siegeLocationId))
		{
			return new FortressSiege(fortresses.get(siegeLocationId));
		}
		if (outposts.containsKey(siegeLocationId))
		{
			return new OutpostSiege(outposts.get(siegeLocationId));
		}
		if (artifacts.containsKey(siegeLocationId))
		{
			return new ArtifactSiege(artifacts.get(siegeLocationId));
		}
		throw new SiegeException("Unknown siege handler for siege location: " + siegeLocationId);
	}
	
	public void cleanLegionId(int legionId)
	{
		for (SiegeLocation loc : getSiegeLocations().values())
		{
			if (loc.getLegionId() == legionId)
			{
				loc.setLegionId(0);
				break;
			}
		}
	}
	
	public void updateOutpostStatusByFortress(FortressLocation fortress)
	{
		for (OutpostLocation outpost : getOutposts().values())
		{
			if (outpost.getFortressDependency().contains(fortress.getLocationId()))
			{
				SiegeRace fortressRace = fortress.getRace();
				for (Integer fortressId : outpost.getFortressDependency())
				{
					final SiegeRace sr = getFortresses().get(fortressId).getRace();
					if (fortressRace != sr)
					{
						fortressRace = SiegeRace.BALAUR;
						break;
					}
					
				}
				
				final boolean isSpawned = outpost.isSilentraAllowed();
				SiegeRace newOwnerRace;
				if (SiegeRace.BALAUR == fortressRace)
				{
					newOwnerRace = SiegeRace.BALAUR;
				}
				else
				{
					newOwnerRace = fortressRace == SiegeRace.ELYOS ? SiegeRace.ASMODIANS : SiegeRace.ELYOS;
				}
				
				if (outpost.getRace() != newOwnerRace)
				{
					stopSiege(outpost.getLocationId());
					deSpawnNpcs(outpost.getLocationId());
					
					outpost.setRace(newOwnerRace);
					DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(outpost);
					broadcastStatusAndUpdate(outpost, isSpawned);
					
					if (SiegeRace.BALAUR != outpost.getRace())
					{
						if (outpost.isSiegeAllowed())
						{
							startSiege(outpost.getLocationId());
						}
						else
						{
							spawnNpcs(outpost.getLocationId(), outpost.getRace(), SiegeModType.PEACE);
						}
					}
				}
			}
		}
	}
	
	public void spawnNpcs(int siegeLocationId, SiegeRace race, SiegeModType type)
	{
		final List<SpawnGroup2> siegeSpawns = DataManager.SPAWNS_DATA2.getSiegeSpawnsByLocId(siegeLocationId);
		for (SpawnGroup2 group : siegeSpawns)
		{
			for (SpawnTemplate template : group.getSpawnTemplates())
			{
				final SiegeSpawnTemplate siegetemplate = (SiegeSpawnTemplate) template;
				if (siegetemplate.getSiegeRace().equals(race) && siegetemplate.getSiegeModType().equals(type))
				{
					SpawnEngine.spawnObject(siegetemplate, 1);
				}
			}
		}
	}
	
	public void deSpawnNpcs(int siegeLocationId)
	{
		final Collection<SiegeNpc> siegeNpcs = World.getInstance().getLocalSiegeNpcs(siegeLocationId);
		for (SiegeNpc npc : siegeNpcs)
		{
			npc.getController().onDelete();
		}
	}
	
	public boolean isSiegeNpcInActiveSiege(Npc npc)
	{
		if ((npc instanceof SiegeNpc))
		{
			final FortressLocation fort = getFortress(((SiegeNpc) npc).getSiegeId());
			if (fort != null)
			{
				if (fort.isVulnerable())
				{
					return true;
				}
				if (fort.getNextState() == 1)
				{
					return npc.getSpawn().getRespawnTime() >= getSecondsBeforeHourEnd();
				}
			}
		}
		return false;
	}
	
	public void broadcastUpdate()
	{
		broadcast(new SM_SIEGE_LOCATION_INFO(), null);
	}
	
	public void broadcastUpdate(SiegeLocation loc)
	{
		Influence.getInstance().recalculateInfluence();
		broadcast(new SM_SIEGE_LOCATION_INFO(loc), new SM_INFLUENCE_RATIO());
	}
	
	public void broadcast(AionServerPacket pkt1, AionServerPacket pkt2)
	{
		World.getInstance().doOnAllPlayers(player ->
		{
			if (pkt1 != null)
			{
				PacketSendUtility.sendPacket(player, pkt1);
			}
			if (pkt2 != null)
			{
				PacketSendUtility.sendPacket(player, pkt2);
			}
		});
	}
	
	public void broadcastUpdate(SiegeLocation loc, DescriptionId nameId)
	{
		final SM_SIEGE_LOCATION_INFO pkt = new SM_SIEGE_LOCATION_INFO(loc);
		final SM_SYSTEM_MESSAGE info = loc.getLegionId() == 0 ? new SM_SYSTEM_MESSAGE(1301039, loc.getRace().getDescriptionId(), nameId) : new SM_SYSTEM_MESSAGE(1301038, LegionService.getInstance().getLegion(loc.getLegionId()).getLegionName(), nameId);
		
		broadcast(pkt, info, loc.getRace());
	}
	
	private void broadcast(AionServerPacket pkt, AionServerPacket info, SiegeRace race)
	{
		World.getInstance().doOnAllPlayers(player ->
		{
			if (player.getRace().getRaceId() == race.getRaceId())
			{
				PacketSendUtility.sendPacket(player, info);
			}
			PacketSendUtility.sendPacket(player, pkt);
		});
	}
	
	public void broadcastStatusAndUpdate(OutpostLocation outpost, boolean oldSilentraState)
	{
		SM_SYSTEM_MESSAGE info = null;
		if (oldSilentraState != outpost.isSilentraAllowed())
		{
			if (outpost.isSilentraAllowed())
			{
				info = outpost.getLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTUNDERPASS_SPAWN : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKUNDERPASS_SPAWN;
			}
			else
			{
				info = outpost.getLocationId() == 2111 ? SM_SYSTEM_MESSAGE.STR_FIELDABYSS_LIGHTUNDERPASS_DESPAWN : SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKUNDERPASS_DESPAWN;
			}
		}
		
		broadcast(new SM_RIFT_ANNOUNCE(getOutpost(3111).isSilentraAllowed(), getOutpost(2111).isSilentraAllowed()), info);
	}
	
	private void broadcast(SM_RIFT_ANNOUNCE rift, SM_SYSTEM_MESSAGE info)
	{
		World.getInstance().doOnAllPlayers(player ->
		{
			PacketSendUtility.sendPacket(player, rift);
			if (((info != null) && player.getWorldType().equals(WorldType.BALAUREA)) || ((info != null) && player.getWorldType().equals(WorldType.PANESTERRA)))
			{
				PacketSendUtility.sendPacket(player, info);
			}
		});
	}
	
	public void validateLoginZone(Player player)
	{
		final BindPointPosition bind = player.getBindPoint();
		int mapId;
		float x;
		float y;
		float z;
		byte h;
		if (bind != null)
		{
			mapId = bind.getMapId();
			x = bind.getX();
			y = bind.getY();
			z = bind.getZ();
			h = bind.getHeading();
		}
		else
		{
			final PlayerInitialData.LocationData start = DataManager.PLAYER_INITIAL_DATA.getSpawnLocation(player.getRace());
			mapId = start.getMapId();
			x = start.getX();
			y = start.getY();
			z = start.getZ();
			h = start.getHeading();
		}
		
		for (FortressLocation fortress : getFortresses().values())
		{
			if (fortress.isInActiveSiegeZone(player) && fortress.isEnemy(player))
			{
				World.getInstance().setPosition(player, mapId, x, y, z, h);
				return;
			}
		}
	}
	
	public void onPlayerLogin(Player player)
	{
		if (SiegeConfig.SIEGE_ENABLED)
		{
			PacketSendUtility.sendPacket(player, new SM_INFLUENCE_RATIO());
			PacketSendUtility.sendPacket(player, new SM_SIEGE_LOCATION_INFO());
			PacketSendUtility.sendPacket(player, new SM_RIFT_ANNOUNCE(getOutpost(3111).isSilentraAllowed(), getOutpost(2111).isSilentraAllowed()));
		}
	}
	
	public void onEnterSiegeWorld(Player player)
	{
		final FastMap<Integer, SiegeLocation> worldLocations = new FastMap<>();
		final FastMap<Integer, ArtifactLocation> worldArtifacts = new FastMap<>();
		
		for (SiegeLocation location : getSiegeLocations().values())
		{
			if (location.getWorldId() == player.getWorldId())
			{
				worldLocations.put(location.getLocationId(), location);
			}
		}
		for (ArtifactLocation artifact : getArtifacts().values())
		{
			if (artifact.getWorldId() == player.getWorldId())
			{
				worldArtifacts.put(artifact.getLocationId(), artifact);
			}
		}
		PacketSendUtility.sendPacket(player, new SM_SHIELD_EFFECT(worldLocations.values()));
		PacketSendUtility.sendPacket(player, new SM_ABYSS_ARTIFACT_INFO3(worldArtifacts.values()));
	}
	
	public void onWeatherChanged(WeatherEntry entry)
	{
	}
	
	public int getFortressId(int locId)
	{
		switch (locId)
		{
			case 36:
			case 54:
			{
				return 1131;
			}
			case 37:
			case 55:
			{
				return 1132;
			}
			case 39:
			case 56:
			{
				return 1141;
			}
			case 45:
			case 57:
			case 72:
			case 75:
			{
				return 1221;
			}
			case 46:
			case 58:
			case 73:
			case 76:
			{
				return 1231;
			}
			case 47:
			case 59:
			case 74:
			case 77:
			{
				return 1241;
			}
			// 2.0
			case 90:
			{
				return 2011;
			}
			case 91:
			{
				return 2021;
			}
			case 93:
			{
				return 3011;
			}
			case 94:
			{
				return 3021;
			}
			// 4.7
			case 102:
			{
				return 7011;
			}
			case 103:
			{
				return 10111;
			}
			case 104:
			{
				return 10211;
			}
			case 105:
			{
				return 10311;
			}
			case 106:
			{
				return 10411;
			}
		}
		return 0;
	}
}