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
package com.aionemu.gameserver.configs.main;

import com.aionemu.commons.configuration.Property;

public class PvPConfig
{
	
	@Property(key = "gameserver.pvp.chainkill.time.restriction", defaultValue = "0")
	public static int CHAIN_KILL_TIME_RESTRICTION;
	
	@Property(key = "gameserver.pvp.chainkill.number.restriction", defaultValue = "30")
	public static int CHAIN_KILL_NUMBER_RESTRICTION;
	
	@Property(key = "gameserver.pvp.max.leveldiff.restriction", defaultValue = "9")
	public static int MAX_AUTHORIZED_LEVEL_DIFF;
	
	@Property(key = "gameserver.pvp.medal.rewarding.enable", defaultValue = "false")
	public static boolean ENABLE_MEDAL_REWARDING;
	
	@Property(key = "gameserver.pvp.medal.reward.chance", defaultValue = "10")
	public static float MEDAL_REWARD_CHANCE;
	
	@Property(key = "gameserver.pvp.medal.reward.quantity", defaultValue = "1")
	public static int MEDAL_REWARD_QUANTITY;
	
	@Property(key = "gameserver.pvp.toll.rewarding.enable", defaultValue = "false")
	public static boolean ENABLE_TOLL_REWARDING;
	
	@Property(key = "gameserver.pvp.toll.reward.chance", defaultValue = "50")
	public static float TOLL_REWARD_CHANCE;
	
	@Property(key = "gameserver.pvp.toll.reward.quantity", defaultValue = "1")
	public static int TOLL_REWARD_QUANTITY;
	
	@Property(key = "gameserver.pvp.killingspree.enable", defaultValue = "false")
	public static boolean ENABLE_KILLING_SPREE_SYSTEM;
	
	@Property(key = "gameserver.pvp.raw.killcount.spree", defaultValue = "20")
	public static int SPREE_KILL_COUNT;
	
	@Property(key = "gameserver.pvp.raw.killcount.rampage", defaultValue = "35")
	public static int RAMPAGE_KILL_COUNT;
	
	@Property(key = "gameserver.pvp.raw.killcount.genocide", defaultValue = "50")
	public static int GENOCIDE_KILL_COUNT;
	
	@Property(key = "gameserver.pvp.special_reward.type", defaultValue = "0")
	public static int GENOCIDE_SPECIAL_REWARDING;
	
	@Property(key = "gameserver.pvp.special_reward.chance", defaultValue = "2")
	public static float SPECIAL_REWARD_CHANCE;
	
	// Bandit Reward
	@Property(key = "gameserver.pvp.toll.pk.cost", defaultValue = "0")
	public static int TOLL_PK_COST;
	
	// War system bonus reward
	/**
	 * War PvP System
	 */
	@Property(key = "gameserver.pvp.war.enable", defaultValue = "false")
	public static boolean WAR_PVP_SYSTEM;
	
	@Property(key = "gameserver.pvp.special.war.ap", defaultValue = "5000")
	public static int WAR_AP_REWARD;
	
	@Property(key = "gameserver.pvp.special.war.gp", defaultValue = "50")
	public static int WAR_GP_REWARD;
	
	@Property(key = "gameserver.pvp.special.war.mapid", defaultValue = "600010000")
	public static int WAR_MAPID;
	/**
	 * Advanced PvP System
	 */
	@Property(key = "gameserver.pvp.adv.enable", defaultValue = "true")
	public static boolean ADVANCED_PVP_SYSTEM;
	
	@Property(key = "gameserver.pvp.special.adv.ap", defaultValue = "0")
	public static int ADVANCED_AP_REWARD;
	
	@Property(key = "gameserver.pvp.special.adv.reward", defaultValue = "186000147")
	public static int ADVANCED_ITEM_REWARD;
	
	@Property(key = "gameserver.pvp.special.adv.count", defaultValue = "1")
	public static int ADVANCED_ITEM_COUNT;
	
	@Property(key = "gameserver.pvp.special.adv.mapid", defaultValue = "600010000")
	public static int ADVANCED_MAPID;
	
}