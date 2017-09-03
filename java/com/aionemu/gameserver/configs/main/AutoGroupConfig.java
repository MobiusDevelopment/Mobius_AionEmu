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

public class AutoGroupConfig
{
	@Property(key = "gameserver.autogroup.enable", defaultValue = "true")
	public static boolean AUTO_GROUP_ENABLED;
	
	// DREDGION
	@Property(key = "gameserver.dredgion.timer", defaultValue = "60")
	public static long DREDGION_TIMER;
	@Property(key = "gameserver.dredgion.enable", defaultValue = "true")
	public static boolean DREDGION_ENABLED;
	@Property(key = "gameserver.dredgion.schedule.midday", defaultValue = "0 0 12 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.dredgion.schedule.evening", defaultValue = "0 0 20 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_EVENING;
	@Property(key = "gameserver.dredgion.schedule.midnight", defaultValue = "0 0 23 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String DREDGION_SCHEDULE_MIDNIGHT;
	
	// KAMAR BATTLEFIELD 4.3
	@Property(key = "gameserver.kamar.timer", defaultValue = "60")
	public static long KAMAR_TIMER;
	@Property(key = "gameserver.kamar.enable", defaultValue = "true")
	public static boolean KAMAR_ENABLED;
	@Property(key = "gameserver.kamar.schedule.evening", defaultValue = "0 0 20 ? * SAT *")
	public static String KAMAR_SCHEDULE_EVENING;
	
	// ENGULFED OPHIDAN BRIDGE 4.5
	@Property(key = "gameserver.ophidan.timer", defaultValue = "60")
	public static long OPHIDAN_TIMER;
	@Property(key = "gameserver.ophidan.enable", defaultValue = "true")
	public static boolean OPHIDAN_ENABLED;
	@Property(key = "gameserver.ophidan.schedule.midday", defaultValue = "0 0 12 ? * TUE,THU,SAT,SUN *")
	public static String OPHIDAN_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.ophidan.schedule.midnight", defaultValue = "0 0 23 ? * TUE,THU,SAT *")
	public static String OPHIDAN_SCHEDULE_MIDNIGHT;
	
	// IRON WALL WARFRONT 4.5
	@Property(key = "gameserver.bastion.timer", defaultValue = "60")
	public static long BASTION_TIMER;
	@Property(key = "gameserver.bastion.enable", defaultValue = "true")
	public static boolean BASTION_ENABLED;
	@Property(key = "gameserver.bastion.schedule.evening", defaultValue = "0 0 20 ? * SUN *")
	public static String BASTION_SCHEDULE_EVENING;
	@Property(key = "gameserver.bastion.schedule.midnight", defaultValue = "0 0 23 ? * SUN *")
	public static String BASTION_SCHEDULE_MIDNIGHT;
	
	// IDGEL DOME 4.7
	@Property(key = "gameserver.idgel.dome.timer", defaultValue = "60")
	public static long IDGEL_TIMER;
	@Property(key = "gameserver.idgel.dome.enable", defaultValue = "true")
	public static boolean IDGEL_ENABLED;
	@Property(key = "gameserver.idgel.dome.schedule.midday", defaultValue = "0 0 12 ? * MON,WED,FRI *")
	public static String IDGEL_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.idgel.dome.schedule.midnight", defaultValue = "0 0 23 ? * MON,WED,FRI *")
	public static String IDGEL_SCHEDULE_MIDNIGHT;
	
	// ASHUNATAL DREDGION 5.1
	@Property(key = "gameserver.ashunatal.timer", defaultValue = "60")
	public static long ASHUNATAL_TIMER;
	@Property(key = "gameserver.ashunatal.enable", defaultValue = "true")
	public static boolean ASHUNATAL_ENABLED;
	@Property(key = "gameserver.ashunatal.schedule.midday", defaultValue = "0 0 12 ? * MON,TUE,WED,THU,FRI,SAT,SUN *")
	public static String ASHUNATAL_SCHEDULE_MIDDAY;
	@Property(key = "gameserver.ashunatal.schedule.evening", defaultValue = "0 0 20 ? * MON,TUE,WED,THU,FRI *")
	public static String ASHUNATAL_SCHEDULE_EVENING;
	
	// OPHIDAN WARPATH 5.1
	@Property(key = "gameserver.ophidan.warpath.timer", defaultValue = "60")
	public static long OPHIDAN_WARPATH_TIMER;
	@Property(key = "gameserver.ophidan.warpath.enable", defaultValue = "true")
	public static boolean OPHIDAN_WARPATH_ENABLED;
	@Property(key = "gameserver.ophidan.warpath.schedule.midnight", defaultValue = "0 0 23 ? * TUE,THU,SAT *")
	public static String OPHIDAN_WARPATH_SCHEDULE_MIDNIGHT;
	
	// IDGEL DOME LANDMARK 5.1
	@Property(key = "gameserver.idgel.dome.landmark.timer", defaultValue = "60")
	public static long IDGEL_DOME_LANDMARK_TIMER;
	@Property(key = "gameserver.idgel.dome.landmark.enable", defaultValue = "true")
	public static boolean IDGEL_DOME_LANDMARK_ENABLED;
	@Property(key = "gameserver.idgel.dome.landmark.schedule.midnight", defaultValue = "0 0 23 ? * MON,WED,FRI *")
	public static String IDGEL_DOME_LANDMARK_SCHEDULE_MIDNIGHT;
}