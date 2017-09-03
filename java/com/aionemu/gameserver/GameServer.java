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
package com.aionemu.gameserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantLock;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.DatabaseFactory;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.network.NioServer;
import com.aionemu.commons.network.ServerCfg;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.AEInfos;
import com.aionemu.gameserver.ai2.AI2Engine;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.configs.Config;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.PvPModConfig;
import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.instance.InstanceEngine;
import com.aionemu.gameserver.model.GameEngine;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.house.MaintenanceTask;
import com.aionemu.gameserver.model.siege.Influence;
import com.aionemu.gameserver.network.BannedMacManager;
import com.aionemu.gameserver.network.aion.GameConnectionFactoryImpl;
import com.aionemu.gameserver.network.ls.LoginServer;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;
import com.aionemu.gameserver.services.AdminService;
import com.aionemu.gameserver.services.AgentService;
import com.aionemu.gameserver.services.AnnouncementService;
import com.aionemu.gameserver.services.AnohaService;
import com.aionemu.gameserver.services.AtreianPassportService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.BeritraService;
import com.aionemu.gameserver.services.BrokerService;
import com.aionemu.gameserver.services.ChallengeTaskService;
import com.aionemu.gameserver.services.ConquestService;
import com.aionemu.gameserver.services.CuringZoneService;
import com.aionemu.gameserver.services.DatabaseCleaningService;
import com.aionemu.gameserver.services.DebugService;
import com.aionemu.gameserver.services.DisputeLandService;
import com.aionemu.gameserver.services.DynamicRiftService;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.ExchangeService;
import com.aionemu.gameserver.services.FlyRingService;
import com.aionemu.gameserver.services.GameTimeService;
import com.aionemu.gameserver.services.HousingBidService;
import com.aionemu.gameserver.services.IdianDepthsService;
import com.aionemu.gameserver.services.InstanceRiftService;
import com.aionemu.gameserver.services.IuService;
import com.aionemu.gameserver.services.LimitedItemTradeService;
import com.aionemu.gameserver.services.MoltenusService;
import com.aionemu.gameserver.services.NightmareCircusService;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.PeriodicSaveService;
import com.aionemu.gameserver.services.PetitionService;
import com.aionemu.gameserver.services.ProtectorConquerorService;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.services.RoadService;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.services.ShieldService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.SpringZoneService;
import com.aionemu.gameserver.services.SvsService;
import com.aionemu.gameserver.services.TownService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.WeatherService;
import com.aionemu.gameserver.services.WeddingService;
import com.aionemu.gameserver.services.ZorshivDredgionService;
import com.aionemu.gameserver.services.abyss.AbyssRankCleaningService;
import com.aionemu.gameserver.services.abyss.AbyssRankUpdateService;
import com.aionemu.gameserver.services.abysslandingservice.LandingUpdateService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.events.BGService;
import com.aionemu.gameserver.services.events.CrazyDaevaService;
import com.aionemu.gameserver.services.events.FFAService;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.services.instance.AsyunatarService;
import com.aionemu.gameserver.services.instance.DredgionService2;
import com.aionemu.gameserver.services.instance.EngulfedOphidanBridgeService;
import com.aionemu.gameserver.services.instance.IdgelDomeLandmarkService;
import com.aionemu.gameserver.services.instance.IdgelDomeService;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.instance.IronWallWarfrontService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.SuspiciousOphidanBridgeService;
import com.aionemu.gameserver.services.player.LunaShopService;
import com.aionemu.gameserver.services.player.PlayerEventService;
import com.aionemu.gameserver.services.player.PlayerEventService2;
import com.aionemu.gameserver.services.player.PlayerEventService3;
import com.aionemu.gameserver.services.player.PlayerEventService4;
import com.aionemu.gameserver.services.player.PlayerLimitService;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativityEssenceService;
import com.aionemu.gameserver.services.reward.RewardService;
import com.aionemu.gameserver.services.teleport.HotspotTeleportService;
import com.aionemu.gameserver.services.territory.TerritoryService;
import com.aionemu.gameserver.services.thedevils.EventGebukMantan;
import com.aionemu.gameserver.services.thedevils.ExpoEventAsmo;
import com.aionemu.gameserver.services.thedevils.ExpoEventElyos;
import com.aionemu.gameserver.services.thedevils.TreasureAbyss;
import com.aionemu.gameserver.services.transfers.PlayerTransferService;
import com.aionemu.gameserver.spawnengine.ShugoImperialTombSpawnManager;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.TemporarySpawnEngine;
import com.aionemu.gameserver.taskmanager.TaskManagerFromDB;
import com.aionemu.gameserver.taskmanager.tasks.PacketBroadcaster;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.ThreadUncaughtExceptionHandler;
import com.aionemu.gameserver.utils.Util;
import com.aionemu.gameserver.utils.chathandlers.ChatProcessor;
import com.aionemu.gameserver.utils.cron.ThreadPoolManagerRunnableRunner;
import com.aionemu.gameserver.utils.gametime.DateTimeUtil;
import com.aionemu.gameserver.utils.gametime.GameTimeManager;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.aionemu.gameserver.world.zone.ZoneService;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

public class GameServer
{
	private static final Logger log = LoggerFactory.getLogger(GameServer.class);
	private static int ELYOS_COUNT = 0;
	private static int ASMOS_COUNT = 0;
	private static double ELYOS_RATIO = 0.0;
	private static double ASMOS_RATIO = 0.0;
	private static final ReentrantLock lock = new ReentrantLock();
	
	private static Set<StartupHook> startUpHooks = new HashSet<>();
	
	private static void initalizeLoggger()
	{
		new File("./log/backup/").mkdirs();
		final File[] files = new File("log").listFiles((FilenameFilter) (dir, name) -> name.endsWith(".log"));
		if ((files != null) && (files.length > 0))
		{
			final byte[] buf = new byte[1024];
			try
			{
				final String outFilename = "./log/backup/" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date()) + ".zip";
				final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);
				for (File logFile : files)
				{
					final FileInputStream in = new FileInputStream(logFile);
					out.putNextEntry(new ZipEntry(logFile.getName()));
					int len;
					while ((len = in.read(buf)) > 0)
					{
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
					logFile.delete();
				}
				out.close();
			}
			catch (IOException e)
			{
			}
		}
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try
		{
			final JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure("config/slf4j-logback.xml");
		}
		catch (JoranException je)
		{
			throw new RuntimeException("Failed to configure loggers, shutting down...", je);
		}
	}
	
	public static void main(String[] args)
	{
		final long start = System.currentTimeMillis();
		final GameEngine[] parallelEngines =
		{
			QuestEngine.getInstance(),
			InstanceEngine.getInstance(),
			AI2Engine.getInstance(),
			ChatProcessor.getInstance()
		};
		final CountDownLatch progressLatch = new CountDownLatch(parallelEngines.length);
		initalizeLoggger();
		initUtilityServicesAndConfig();
		DataManager.getInstance();
		Util.printSection("Zone");
		ZoneService.getInstance().load(null);
		Util.printSection("World");
		World.getInstance();
		Util.printSection("Luna Shop initialization");
		LunaShopService.getInstance().init();
		Util.printSection("Drops");
		DropRegistrationService.getInstance();
		final GameServer gs = new GameServer();
		DAOManager.getDAO(PlayerDAO.class).setPlayersOffline(false);
		Util.printSection("HDDBan");
		BannedMacManager.getInstance();
		// FIXME?
		// BannedHDDManager.getInstance();
		// NetworkBannedManager.getInstance();
		Util.printSection("Cleaning");
		DatabaseCleaningService.getInstance();
		AbyssRankCleaningService.getInstance();
		Util.printSection("Geodata");
		GeoService.getInstance().initializeGeo();
		System.gc();
		for (int i = 0; i < parallelEngines.length; i++)
		{
			final int index = i;
			ThreadPoolManager.getInstance().execute(() -> parallelEngines[index].load(progressLatch));
		}
		try
		{
			progressLatch.await();
		}
		catch (InterruptedException e1)
		{
		}
		// Siege
		Util.printSection("Siege Location Data");
		SiegeService.getInstance().initSiegeLocations();
		// Base
		Util.printSection("Base Location Data");
		BaseService.getInstance().initBaseLocations();
		Util.printSection("Base Reset");
		BaseService.getInstance().initBaseReset();
		// Vortex
		Util.printSection("Vortex Location Data");
		VortexService.getInstance().initVortex();
		VortexService.getInstance().initVortexLocations();
		// Beritra Invasion
		Util.printSection("Beritra Location Data");
		BeritraService.getInstance().initBeritra();
		BeritraService.getInstance().initBeritraLocations();
		// Agent Fight
		Util.printSection("Agent Location Data");
		AgentService.getInstance().initAgent();
		AgentService.getInstance().initAgentLocations();
		// Berserk Anoha
		Util.printSection("Anoha Location Data");
		AnohaService.getInstance().initAnoha();
		AnohaService.getInstance().initAnohaLocations();
		// Panesterra
		Util.printSection("Svs Location Data");
		SvsService.getInstance().initSvs();
		SvsService.getInstance().initSvsLocations();
		// Rvr 4.9
		Util.printSection("Rvr Location Data");
		RvrService.getInstance().initRvr();
		RvrService.getInstance().initRvrLocations();
		// Live Party Concert Hall 4.3
		Util.printSection("Concert Location Data");
		IuService.getInstance().initConcertLocations();
		// Nightmare Circus 4.3
		Util.printSection("Nightmare Circus Location Data");
		NightmareCircusService.getInstance().initCircus();
		NightmareCircusService.getInstance().initCircusLocations();
		// Dynamic Rift
		Util.printSection("Dynamic Rift Location Data");
		DynamicRiftService.getInstance().initDynamicRiftLocations();
		// Instance Rift 4.9
		Util.printSection("Instance Rift Location Data");
		InstanceRiftService.getInstance().initInstance();
		InstanceRiftService.getInstance().initInstanceLocations();
		// Zorshiv Dredgion
		Util.printSection("Zorshiv Dredgion Location Data");
		ZorshivDredgionService.getInstance().initZorshivDredgion();
		ZorshivDredgionService.getInstance().initZorshivDredgionLocations();
		// Moltenus
		Util.printSection("Moltenus Location Data");
		MoltenusService.getInstance().initMoltenus();
		MoltenusService.getInstance().initMoltenusLocations();
		// Rift
		Util.printSection("Rift Location Data");
		RiftService.getInstance().initRiftLocations();
		// Conquest/Offering 4.8
		Util.printSection("Conquest/Offering Location Data");
		ConquestService.getInstance().initOffering();
		ConquestService.getInstance().initConquestLocations();
		// Abyss Landing 4.9.1
		Util.printSection("Abyss Landing Location Data");
		AbyssLandingService.getInstance().initLandingLocations();
		LandingUpdateService.getInstance().initResetQuestPoints();
		LandingUpdateService.getInstance().initResetAbyssLandingPoints();
		AbyssLandingSpecialService.getInstance().initLandingSpecialLocations();
		// Idian Depths 4.8
		Util.printSection("Idian Depths Location Data");
		IdianDepthsService.getInstance().initIdianDepthsLocations();
		Util.printSection("Spawns");
		SpawnEngine.spawnAll();
		RiftService.getInstance().initRifts();
		TerritoryService.getInstance().initTerritory();
		// Event 4.3/4.7
		if (EventsConfig.IMPERIAL_TOMB_ENABLE)
		{ // Shugo Imperial Tomb 4.3
			ShugoImperialTombSpawnManager.getInstance().start();
		}
		if (EventsConfig.ENABLE_CRAZY)
		{
			Util.printSection("Crazy Daeva");
			CrazyDaevaService.getInstance().startTimer();
		}
		TemporarySpawnEngine.spawnAll();
		Util.printSection("PvP System");
		if (PvPModConfig.FFA_ENABLED)
		{
			FFAService.getInstance();
		}
		if (PvPModConfig.BG_ENABLED)
		{
			LadderService.getInstance();
			BGService.getInstance();
		}
		Util.printSection("Limits");
		LimitedItemTradeService.getInstance().start();
		if (CustomConfig.LIMITS_ENABLED)
		{
			PlayerLimitService.getInstance().scheduleUpdate();
		}
		GameTimeManager.startClock();
		Util.printSection("Siege Schedule initialization");
		SiegeService.getInstance().initSieges();
		Util.printSection("TaskManagers");
		PacketBroadcaster.getInstance();
		GameTimeService.getInstance();
		AnnouncementService.getInstance();
		DebugService.getInstance();
		WeatherService.getInstance();
		BrokerService.getInstance();
		Influence.getInstance();
		ExchangeService.getInstance();
		PeriodicSaveService.getInstance();
		PetitionService.getInstance();
		if (AIConfig.SHOUTS_ENABLE)
		{
			NpcShoutsService.getInstance();
		}
		InstanceService.load();
		FlyRingService.getInstance();
		CuringZoneService.getInstance();
		SpringZoneService.getInstance();
		RoadService.getInstance();
		HTMLCache.getInstance();
		if (RankingConfig.TOP_RANKING_UPDATE_SETTING)
		{
			AbyssRankUpdateService.getInstance().scheduleUpdateHour();
		}
		else
		{
			AbyssRankUpdateService.getInstance().scheduleUpdateMinute();
		}
		TaskManagerFromDB.getInstance();
		if (SiegeConfig.SIEGE_SHIELD_ENABLED)
		{
			ShieldService.getInstance().spawnAll();
		}
		// Dredgion
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("[Baranath/Chantra/Terath] Dredgion");
			DredgionService2.getInstance().initDredgion();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("[Ashunatal] Dredgion 5.1");
			AsyunatarService.getInstance().initAsyunatar();
		}
		// Battlefield
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Kamar Battlefield 4.3");
			KamarBattlefieldService.getInstance().initKamarBattlefield();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Engulfed Ophidan Bridge 4.5");
			EngulfedOphidanBridgeService.getInstance().initEngulfedOphidan();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Ophidan Warpath 5.1");
			SuspiciousOphidanBridgeService.getInstance().initSuspiciousOphidan();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Iron Wall Warfront 4.5");
			IronWallWarfrontService.getInstance().initIronWallWarfront();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Idgel Dome 4.7");
			IdgelDomeService.getInstance().initIdgelDome();
		}
		if (AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			Util.printSection("Idgel Dome Landmark 5.1");
			IdgelDomeLandmarkService.getInstance().initLandmark();
		}
		// Aionunity Event
		Util.printSection("Elyos Babi Event");
		ExpoEventElyos.ScheduleCron();
		Util.printSection("Asmodians Babi Event");
		ExpoEventAsmo.ScheduleCron();
		Util.printSection("Treasure Abyss Event");
		TreasureAbyss.ScheduleCron();
		Util.printSection("Gebuk Mantan Event");
		EventGebukMantan.ScheduleCron();
		// Custom
		if (CustomConfig.ENABLE_REWARD_SERVICE)
		{
			RewardService.getInstance();
		}
		if (EventsConfig.EVENT_ENABLED)
		{
			PlayerEventService.getInstance();
		}
		if (EventsConfig.EVENT_ENABLED2)
		{
			PlayerEventService2.getInstance();
		}
		if (EventsConfig.EVENT_ENABLED2)
		{
			PlayerEventService3.getInstance();
		}
		if (EventsConfig.EVENT_ENABLED2)
		{
			PlayerEventService4.getInstance();
		}
		if (EventsConfig.ENABLE_EVENT_SERVICE)
		{
			EventService.getInstance().start();
		}
		if (EventsConfig.ENABLE_ATREIAN_PASSPORT)
		{
			AtreianPassportService.getInstance().onStart();
		}
		if (WeddingsConfig.WEDDINGS_ENABLE)
		{
			WeddingService.getInstance();
		}
		AdminService.getInstance();
		CreativityEssenceService.getInstance();
		PlayerTransferService.getInstance();
		MaintenanceTask.getInstance();
		HousingBidService.getInstance().start();
		TownService.getInstance();
		ChallengeTaskService.getInstance();
		HotspotTeleportService.getInstance();
		Util.printSection("Protector/Conqueror initialization");
		ProtectorConquerorService.getInstance().initSystem();
		Util.printSection("Dispute Land initialization");
		DisputeLandService.getInstance().initDisputeLand();
		BaseService.getInstance().initBases();
		Util.printSection("System");
		System.gc();
		AEInfos.printAllInfos();
		Util.printSection("GameServerLog");
		log.info("AionEmu Server started in " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
		gs.startServers();
		Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());
		if (GSConfig.ENABLE_RATIO_LIMITATION)
		{
			addStartupHook(() ->
			{
				lock.lock();
				try
				{
					ASMOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ASMODIANS);
					ELYOS_COUNT = DAOManager.getDAO(PlayerDAO.class).getCharacterCountForRace(Race.ELYOS);
					computeRatios();
				}
				catch (Exception e)
				{
				}
				finally
				{
					lock.unlock();
				}
				displayRatios(false);
			});
		}
		onStartup();
	}
	
	private void startServers()
	{
		Util.printSection("Starting Network");
		final NioServer nioServer = new NioServer(NetworkConfig.NIO_READ_WRITE_THREADS, new ServerCfg(NetworkConfig.GAME_BIND_ADDRESS, NetworkConfig.GAME_PORT, "Game Connections", new GameConnectionFactoryImpl()));
		final LoginServer ls = LoginServer.getInstance();
		ls.setNioServer(nioServer);
		nioServer.connect();
		ls.connect();
	}
	
	private static void initUtilityServicesAndConfig()
	{
		Thread.setDefaultUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		CronService.initSingleton(ThreadPoolManagerRunnableRunner.class);
		Config.load();
		DateTimeUtil.init();
		Util.printSection("DataBase");
		DatabaseFactory.init();
		DAOManager.init();
		Util.printSection("IDFactory");
		IDFactory.getInstance();
		Util.printSection("Threads");
		ThreadConfig.load();
		ThreadPoolManager.getInstance();
	}
	
	public static synchronized void addStartupHook(StartupHook hook)
	{
		if (startUpHooks != null)
		{
			startUpHooks.add(hook);
		}
		else
		{
			hook.onStartup();
		}
	}
	
	private static synchronized void onStartup()
	{
		final Set<StartupHook> startupHooks = startUpHooks;
		startUpHooks = null;
		for (StartupHook hook : startupHooks)
		{
			hook.onStartup();
		}
	}
	
	public static void updateRatio(Race race, int i)
	{
		lock.lock();
		try
		{
			switch (race)
			{
				case ASMODIANS:
					ASMOS_COUNT += i;
					break;
				case ELYOS:
					ELYOS_COUNT += i;
					break;
				default:
					break;
			}
			computeRatios();
		}
		catch (Exception e)
		{
		}
		finally
		{
			lock.unlock();
		}
		displayRatios(true);
	}
	
	private static void computeRatios()
	{
		if ((ASMOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT) && (ELYOS_COUNT <= GSConfig.RATIO_MIN_CHARACTERS_COUNT))
		{
			ASMOS_RATIO = GameServer.ELYOS_RATIO = 50.0;
		}
		else
		{
			ASMOS_RATIO = (ASMOS_COUNT * 100.0) / (ASMOS_COUNT + ELYOS_COUNT);
			ELYOS_RATIO = (ELYOS_COUNT * 100.0) / (ASMOS_COUNT + ELYOS_COUNT);
		}
	}
	
	private static void displayRatios(boolean updated)
	{
		log.info("FACTIONS RATIO " + (updated ? "UPDATED " : "") + ": E " + String.format("%.1f", GameServer.ELYOS_RATIO) + " % / A " + String.format("%.1f", GameServer.ASMOS_RATIO) + " %");
	}
	
	public static double getRatiosFor(Race race)
	{
		switch (race)
		{
			case ASMODIANS:
				return ASMOS_RATIO;
			case ELYOS:
				return ELYOS_RATIO;
			default:
				return 0.0;
		}
	}
	
	public static int getCountFor(Race race)
	{
		switch (race)
		{
			case ASMODIANS:
				return ASMOS_COUNT;
			case ELYOS:
				return ELYOS_COUNT;
			default:
				return 0;
		}
	}
	
	public static abstract interface StartupHook
	{
		void onStartup();
	}
}