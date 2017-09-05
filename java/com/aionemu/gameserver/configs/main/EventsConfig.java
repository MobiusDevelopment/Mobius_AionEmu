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

public class EventsConfig
{
	@Property(key = "gameserver.enable.decor", defaultValue = "0")
	public static int ENABLE_DECOR;
	@Property(key = "gameserver.events.give.juice", defaultValue = "160009017")
	public static int EVENT_GIVE_JUICE;
	@Property(key = "gameserver.events.give.cake", defaultValue = "160010073")
	public static int EVENT_GIVE_CAKE;
	
	// VIP Tickets
	@Property(key = "gameserver.vip.tickets.enable", defaultValue = "false")
	public static boolean ENABLE_VIP_TICKETS;
	@Property(key = "gameserver.vip.tickets.time", defaultValue = "60")
	public static int VIP_TICKETS_PERIOD;
	
	// Event Awake [Event JAP]
	@Property(key = "gameserver.event.awake.enable", defaultValue = "false")
	public static boolean ENABLE_AWAKE_EVENT;
	@Property(key = "gameserver.event.seed.transformation.time", defaultValue = "60")
	public static int SEED_TRANSFORMATION_PERIOD;
	
	// Shugo Imperial Tomb 4.3
	@Property(key = "gameserver.shugo.imperial.tomb.enable", defaultValue = "true")
	public static boolean IMPERIAL_TOMB_ENABLE;
	@Property(key = "gameserver.shugo.imperial.tomb.timer.from.start.to.end", defaultValue = "10")
	public static long IMPERIAL_TOMB_TIMER;
	@Property(key = "gameserver.shugo.imperial.tomb.time.to.start", defaultValue = "0 0 0,12,20,0 ? * *")
	public static String IMPERIAL_TOMB_TIMES;
	
	// Crazy Daeva
	@Property(key = "gameserver.crazy.daeva.enable", defaultValue = "false")
	public static boolean ENABLE_CRAZY;
	@Property(key = "gameserver.crazy.daeva.tag", defaultValue = "<Crazy>")
	public static String CRAZY_TAG;
	@Property(key = "gameserver.crazy.daeva.lowest.rnd", defaultValue = "10")
	public static int CRAZY_LOWEST_RND;
	@Property(key = "gameserver.crazy.daeva.time.to.start", defaultValue = "0 0 0,12,20,0 ? * *")
	public static String CRAZY_TIMES;
	@Property(key = "gameserver.crazy.daeva.endtime", defaultValue = "5")
	public static int CRAZY_ENDTIME;
	
	@Property(key = "gameserver.atreian.passport.enable", defaultValue = "false")
	public static boolean ENABLE_ATREIAN_PASSPORT;
	
	// Aionunity events
	@Property(key = "gameserver.event.aionunity.enable", defaultValue = "false")
	public static boolean ENABLE_AIONUNITY_EVENTS;
	@Property(key = "gameserver.babi.mulai.elyos", defaultValue = "0 0 20 ? * SAT")
	public static String BABI_EVENT_SCHEDULE_ELYOS;
	@Property(key = "gameserver.babi.mulai.asmo", defaultValue = "0 0 20 ? * SAT")
	public static String BABI_EVENT_SCHEDULE_ASMO;
	@Property(key = "gameserver.babi.reward.count", defaultValue = "5")
	public static int BABI_EVENT_COUNT_REWARD;
	@Property(key = "gameserver.mantan.mulai", defaultValue = "0 0 20 ? * SAT")
	public static String MANTAN_EVENT_SCHEDULE;
	@Property(key = "gameserver.mantan.reward.count", defaultValue = "1")
	public static int MANTAN_EVENT_COUNT_REWARD;
	@Property(key = "gameserver.abyss.event.time", defaultValue = "0 0 15 ? * SUN")
	public static String ABYSS_EVENT_SCHEDULE;
	
	// Login reward service
	@Property(key = "gameserver.event.rewarding.membership", defaultValue = "false")
	public static boolean EVENT_MEMBER_LOGIN;
	@Property(key = "gameserver.event.membership", defaultValue = "0")
	public static int EVENT_REWARD_MEMBERSHIP;
	@Property(key = "gameserver.event.level", defaultValue = "45")
	public static int EVENT_REWARD_LEVEL;
	@Property(key = "gameserver.event.period", defaultValue = "60")
	public static int EVENT_PERIOD2;
	@Property(key = "gameserver.event.item.elyos", defaultValue = "162002024")
	public static int EVENT_ITEM_ELYOS;
	@Property(key = "gameserver.event.item.asmo", defaultValue = "162002024")
	public static int EVENT_ITEM_ASMO;
	@Property(key = "gameserver.event.count", defaultValue = "1")
	public static int EVENT_ITEM_COUNT;
	@Property(key = "gameserver.event.membership.rate", defaultValue = "false")
	public static boolean EVENT_REWARD_MEMBERSHIP_RATE;
	
	// Arcade Upgrade
	@Property(key = "gameserver.event.arcade.enable", defaultValue = "false")
	public static boolean ENABLE_EVENT_ARCADE;
	@Property(key = "gameserver.event.arcade.chance", defaultValue = "70")
	public static int EVENT_ARCADE_CHANCE;
	@Property(key = "gameserver.event.arcade.onlyFrenzyRewardChance", defaultValue = "50")
	public static int EVENT_ARCADE_FRENZYITEM_CHANCE;
	@Property(key = "gameserver.event.arcade.frenzy_time", defaultValue = "90")
	public static int EVENT_ARCADE_FRENZY_TIME;
}