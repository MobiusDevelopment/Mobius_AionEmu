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
package com.aionemu.gameserver.configs;

import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.configs.CommonsConfig;
import com.aionemu.commons.configs.DatabaseConfig;
import com.aionemu.commons.configuration.ConfigurableProcessor;
import com.aionemu.commons.utils.PropertiesUtils;
import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.configs.administration.DeveloperConfig;
import com.aionemu.gameserver.configs.administration.PanelConfig;
import com.aionemu.gameserver.configs.main.AIConfig;
import com.aionemu.gameserver.configs.main.AStationConfig;
import com.aionemu.gameserver.configs.main.AbyssLandingConfig;
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.BrokerConfig;
import com.aionemu.gameserver.configs.main.CacheConfig;
import com.aionemu.gameserver.configs.main.CleaningConfig;
import com.aionemu.gameserver.configs.main.CraftConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.configs.main.FallDamageConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.configs.main.GeoDataConfig;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.configs.main.HTMLConfig;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.configs.main.InGameShopConfig;
import com.aionemu.gameserver.configs.main.LegionConfig;
import com.aionemu.gameserver.configs.main.LoggingConfig;
import com.aionemu.gameserver.configs.main.MembershipConfig;
import com.aionemu.gameserver.configs.main.NameConfig;
import com.aionemu.gameserver.configs.main.PeriodicSaveConfig;
import com.aionemu.gameserver.configs.main.PlayerTransferConfig;
import com.aionemu.gameserver.configs.main.PricesConfig;
import com.aionemu.gameserver.configs.main.PunishmentConfig;
import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.configs.main.PvPModConfig;
import com.aionemu.gameserver.configs.main.RankingConfig;
import com.aionemu.gameserver.configs.main.RateConfig;
import com.aionemu.gameserver.configs.main.SecurityConfig;
import com.aionemu.gameserver.configs.main.ShutdownConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.configs.main.ThreadConfig;
import com.aionemu.gameserver.configs.main.WeddingsConfig;
import com.aionemu.gameserver.configs.main.WorldConfig;
import com.aionemu.gameserver.configs.network.IPConfig;
import com.aionemu.gameserver.configs.network.NetworkConfig;

/**
 * @author Nemesiss, SoulKeeper
 */
public class Config
{
	protected static final Logger log = LoggerFactory.getLogger(Config.class);
	
	public static void load()
	{
		try
		{
			Properties myProps = null;
			try
			{
				log.info("Loading: mygs.properties");
				myProps = PropertiesUtils.load("./config/mygs.properties");
			}
			catch (Exception e)
			{
				log.info("No override properties found");
			}
			final String administration = "./config/administration";
			final Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
			PropertiesUtils.overrideProperties(adminProps, myProps);
			ConfigurableProcessor.process(AdminConfig.class, adminProps);
			ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
			ConfigurableProcessor.process(PanelConfig.class, adminProps);
			final String main = "./config/main";
			final Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
			PropertiesUtils.overrideProperties(mainProps, myProps);
			ConfigurableProcessor.process(AIConfig.class, mainProps);
			ConfigurableProcessor.process(BrokerConfig.class, mainProps);
			ConfigurableProcessor.process(CommonsConfig.class, mainProps);
			ConfigurableProcessor.process(CacheConfig.class, mainProps);
			ConfigurableProcessor.process(CleaningConfig.class, mainProps);
			ConfigurableProcessor.process(CraftConfig.class, mainProps);
			ConfigurableProcessor.process(CustomConfig.class, mainProps);
			ConfigurableProcessor.process(DropConfig.class, mainProps);
			ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
			ConfigurableProcessor.process(EventsConfig.class, mainProps);
			ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
			ConfigurableProcessor.process(AStationConfig.class, mainProps);
			ConfigurableProcessor.process(GSConfig.class, mainProps);
			ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
			ConfigurableProcessor.process(GroupConfig.class, mainProps);
			ConfigurableProcessor.process(HousingConfig.class, mainProps);
			ConfigurableProcessor.process(HTMLConfig.class, mainProps);
			ConfigurableProcessor.process(InGameShopConfig.class, mainProps);
			ConfigurableProcessor.process(AbyssLandingConfig.class, mainProps);
			ConfigurableProcessor.process(LegionConfig.class, mainProps);
			ConfigurableProcessor.process(LoggingConfig.class, mainProps);
			ConfigurableProcessor.process(MembershipConfig.class, mainProps);
			ConfigurableProcessor.process(NameConfig.class, mainProps);
			ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
			ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
			ConfigurableProcessor.process(PricesConfig.class, mainProps);
			ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
			ConfigurableProcessor.process(PvPConfig.class, mainProps);
			ConfigurableProcessor.process(RankingConfig.class, mainProps);
			ConfigurableProcessor.process(RateConfig.class, mainProps);
			ConfigurableProcessor.process(SecurityConfig.class, mainProps);
			ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
			ConfigurableProcessor.process(SiegeConfig.class, mainProps);
			ConfigurableProcessor.process(ThreadConfig.class, mainProps);
			ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
			ConfigurableProcessor.process(WorldConfig.class, mainProps);
			ConfigurableProcessor.process(AdvCustomConfig.class, mainProps);
			ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
			ConfigurableProcessor.process(PvPModConfig.class, mainProps);
			final String network = "./config/network";
			final Properties[] networkProps = PropertiesUtils.loadAllFromDirectory(network);
			PropertiesUtils.overrideProperties(networkProps, myProps);
			ConfigurableProcessor.process(DatabaseConfig.class, networkProps);
			ConfigurableProcessor.process(NetworkConfig.class, networkProps);
		}
		catch (Exception e)
		{
			log.error("Can't load gameserver configuration: ", e);
			throw new Error("Can't load gameserver configuration: ", e);
		}
		IPConfig.load();
	}
	
	public static void reload()
	{
		try
		{
			Properties myProps = null;
			try
			{
				log.info("Loading: mygs.properties");
				myProps = PropertiesUtils.load("./config/mygs.properties");
			}
			catch (Exception e)
			{
				log.info("No override properties found");
			}
			final String administration = "./config/administration";
			final Properties[] adminProps = PropertiesUtils.loadAllFromDirectory(administration);
			PropertiesUtils.overrideProperties(adminProps, myProps);
			ConfigurableProcessor.process(AdminConfig.class, adminProps);
			ConfigurableProcessor.process(DeveloperConfig.class, adminProps);
			ConfigurableProcessor.process(PanelConfig.class, adminProps);
			final String main = "./config/main";
			final Properties[] mainProps = PropertiesUtils.loadAllFromDirectory(main);
			PropertiesUtils.overrideProperties(mainProps, myProps);
			ConfigurableProcessor.process(AIConfig.class, mainProps);
			ConfigurableProcessor.process(BrokerConfig.class, mainProps);
			ConfigurableProcessor.process(CommonsConfig.class, mainProps);
			ConfigurableProcessor.process(CacheConfig.class, mainProps);
			ConfigurableProcessor.process(CleaningConfig.class, mainProps);
			ConfigurableProcessor.process(CraftConfig.class, mainProps);
			ConfigurableProcessor.process(CustomConfig.class, mainProps);
			ConfigurableProcessor.process(DropConfig.class, mainProps);
			ConfigurableProcessor.process(EnchantsConfig.class, mainProps);
			ConfigurableProcessor.process(EventsConfig.class, mainProps);
			ConfigurableProcessor.process(FallDamageConfig.class, mainProps);
			ConfigurableProcessor.process(AStationConfig.class, mainProps);
			ConfigurableProcessor.process(GSConfig.class, mainProps);
			ConfigurableProcessor.process(GeoDataConfig.class, mainProps);
			ConfigurableProcessor.process(GroupConfig.class, mainProps);
			ConfigurableProcessor.process(HousingConfig.class, mainProps);
			ConfigurableProcessor.process(HTMLConfig.class, mainProps);
			ConfigurableProcessor.process(InGameShopConfig.class, mainProps);
			ConfigurableProcessor.process(AbyssLandingConfig.class, mainProps);
			ConfigurableProcessor.process(LegionConfig.class, mainProps);
			ConfigurableProcessor.process(LoggingConfig.class, mainProps);
			ConfigurableProcessor.process(MembershipConfig.class, mainProps);
			ConfigurableProcessor.process(NameConfig.class, mainProps);
			ConfigurableProcessor.process(PeriodicSaveConfig.class, mainProps);
			ConfigurableProcessor.process(PlayerTransferConfig.class, mainProps);
			ConfigurableProcessor.process(PricesConfig.class, mainProps);
			ConfigurableProcessor.process(PunishmentConfig.class, mainProps);
			ConfigurableProcessor.process(PvPConfig.class, mainProps);
			ConfigurableProcessor.process(RankingConfig.class, mainProps);
			ConfigurableProcessor.process(RateConfig.class, mainProps);
			ConfigurableProcessor.process(SecurityConfig.class, mainProps);
			ConfigurableProcessor.process(ShutdownConfig.class, mainProps);
			ConfigurableProcessor.process(SiegeConfig.class, mainProps);
			ConfigurableProcessor.process(ThreadConfig.class, mainProps);
			ConfigurableProcessor.process(WeddingsConfig.class, mainProps);
			ConfigurableProcessor.process(WorldConfig.class, mainProps);
			ConfigurableProcessor.process(AdvCustomConfig.class, mainProps);
			ConfigurableProcessor.process(AutoGroupConfig.class, mainProps);
			ConfigurableProcessor.process(PvPModConfig.class, mainProps);
		}
		catch (Exception e)
		{
			log.error("Can't reload configuration: ", e);
			throw new Error("Can't reload configuration: ", e);
		}
	}
}