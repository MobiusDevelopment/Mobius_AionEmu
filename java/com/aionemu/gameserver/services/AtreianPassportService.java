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

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.model.templates.event.AttendType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATREIAN_PASSPORT;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemAddType;
import com.aionemu.gameserver.services.item.ItemPacketService.ItemUpdateType;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.item.ItemService.ItemUpdatePredicate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */

public class AtreianPassportService
{
	private static final Logger log = LoggerFactory.getLogger(AtreianPassportService.class);
	private final Timestamp t = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private final Map<Integer, AtreianPassport> cumu = new HashMap<>(1);
	private final Map<Integer, AtreianPassport> daily = new HashMap<>(1);
	private final Map<Integer, AtreianPassport> anny = new HashMap<>(1);
	public Map<Integer, AtreianPassport> data = new HashMap<>(1);
	private final Calendar calendar = Calendar.getInstance();
	
	private int year = 0;
	private int arrival = 0;
	private int currentPassport = 0;
	private int cachedPassport = 0;
	private int check = 0;
	private boolean cumuIsActive = false;
	
	public void onLogin(Player player)
	{
		check = 0;
		year = 0;
		arrival = 0;
		currentPassport = 0;
		cachedPassport = 0;
		cumuIsActive = false;
		if (player == null)
		{
			return;
		}
		final PlayerCommonData pcd = player.getCommonData();
		arrival = getArrival();
		checkForNewMonth(pcd);
		if (checkOnlineDate(pcd))
		{
			final int stamps = pcd.getPassportStamps();
			final int newStamps = stamps + 1;
			pcd.setPassportStamps(newStamps);
		}
		for (AtreianPassport atp : cumu.values())
		{
			if (atp.getPeriodStart().isBeforeNow() && atp.getPeriodEnd().isAfterNow())
			{
				if (year == 0)
				{
					year = atp.getPeriodStart().getYear();
				}
				if ((atp.getAttendNum() == pcd.getPassportStamps()) && checkOnlineDate(pcd))
				{
					currentPassport = atp.getId();
					check = 1;
					atp.setRewardId(1);
					pcd.setPassportReward(0);
					pcd.playerPassports.put(atp.getId(), atp);
					cumuIsActive = true;
				}
				else
				{
					atp.setRewardId(0);
					pcd.playerPassports.put(atp.getId(), atp);
					cumuIsActive = false;
					if (currentPassport == 0)
					{
						currentPassport = atp.getId();
					}
				}
			}
		}
		for (AtreianPassport atp : daily.values())
		{
			if (atp.getPeriodStart().isBeforeNow() && atp.getPeriodEnd().isAfterNow())
			{
				if (year == 0)
				{
					year = atp.getPeriodStart().getYear();
				}
				if (checkOnlineDate(pcd))
				{
					if ((currentPassport != 0) && cumuIsActive)
					{
						setCachedPassport(currentPassport);
					}
					else
					{
						currentPassport = atp.getId();
						check = 1;
						atp.setRewardId(1);
						pcd.setPassportReward(0);
						pcd.playerPassports.put(atp.getId(), atp);
					}
				}
				else if (isCached())
				{
					currentPassport = getCachedPassport();
					check = 1;
					atp.setRewardId(1);
					pcd.setPassportReward(0);
					pcd.playerPassports.put(atp.getId(), atp);
				}
				else
				{
					atp.setRewardId(0);
					break;
				}
			}
		}
		pcd.setLastStamp(t);
		checkCompletedPassports(pcd);
		if (checkOnlineDate(pcd))
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ATTEND_MSG_ATTEND_REWARD_GET);
		}
		PacketSendUtility.sendPacket(player, new SM_ATREIAN_PASSPORT(format(pcd.getPlayerPassports()), pcd.getPassportStamps(), currentPassport, t, year, arrival, check));
		check = 0;
		currentPassport = 0;
		year = 0;
		arrival = 0;
		cachedPassport = 0;
		cumuIsActive = false;
		pcd.playerPassports.clear();
	}
	
	@SuppressWarnings("deprecation")
	private void checkForNewMonth(PlayerCommonData pcd)
	{
		if (pcd.getLastStamp() == null)
		{
			return;
		}
		if (pcd.getLastStamp().getMonth() != calendar.getTime().getMonth())
		{
			pcd.setPassportStamps(0);
		}
	}
	
	private void checkCompletedPassports(PlayerCommonData pcd)
	{
		for (AtreianPassport pp : pcd.getCompletedPassports().getAllPassports())
		{
			if (pcd.getPlayerPassports().containsValue(pp))
			{
				pcd.playerPassports.remove(pp);
			}
			if (pp.getRewardId() == 0)
			{
				if ((pp.getPeriodEnd().isBeforeNow() && (pp.getAttendType() == AttendType.ANNIVERSARY)) || (pp.getPeriodEnd().isBeforeNow() && (pp.getAttendType() == AttendType.DAILY)))
				{
					continue;
				}
				else
				{
					pp.setRewardId(3);
				}
			}
			else if ((pp.getRewardId() == 3) && (pp.getAttendType() == AttendType.CUMULATIVE))
			{
				check = 0;
			}
			else if ((pp.getRewardId() == 1) && (pp.getAttendType() == AttendType.CUMULATIVE))
			{
				check = 1;
			}
			else
			{
				check = 0;
			}
			if (pp.getAttendType() == AttendType.CUMULATIVE)
			{
				if (pp.getAttendNum() != pcd.getPassportStamps())
				{
					pp.setRewardId(0);
					check = 0;
				}
			}
			pcd.playerPassports.put(pp.getId(), pp);
		}
	}
	
	private Map<Integer, AtreianPassport> format(Map<Integer, AtreianPassport> atp)
	{
		final Map<Integer, AtreianPassport> finalPassports = new TreeMap<>(atp);
		return finalPassports;
	}
	
	private boolean checkOnlineDate(PlayerCommonData pcd)
	{
		final long lastOnline = pcd.getLastStamp().getTime();
		final long secondsOffline = (System.currentTimeMillis() / 1000) - (lastOnline / 1000);
		double hours = secondsOffline / 3600d;
		if (hours > 24)
		{
			hours = 24;
		}
		if (hours == 24)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private boolean isCached()
	{
		if (cachedPassport != 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public void onStart()
	{
		final Map<Integer, AtreianPassport> raw = DataManager.ATREIAN_PASSPORT_DATA.getAll();
		if (raw.size() != 0)
		{
			getPassports(raw);
		}
		else
		{
			log.warn("[ATREIAN PASSPORT] passports from static data = 0");
		}
		log.info("<Atreian Passport> initialized");
	}
	
	public void onGetReward(Player player, int timestamp, List<Integer> passportId)
	{
		for (Integer i : passportId)
		{
			final AtreianPassport atp = data.get(i);
			ItemService.addItem(player, atp.getRewardItem(), atp.getRewardItemNum(), new ItemUpdatePredicate(ItemAddType.ITEM_COLLECT, ItemUpdateType.INC_PASSPORT_ADD));
			player.getCommonData().setPassportReward(1);
			if (atp.getAttendType() != AttendType.DAILY)
			{
				player.getCommonData().addToCompletedPassports(atp);
			}
		}
		onLogin(player);
	}
	
	public void getPassports(Map<Integer, AtreianPassport> raw)
	{
		data.putAll(raw);
		for (AtreianPassport atp : data.values())
		{
			switch (atp.getAttendType())
			{
				case DAILY:
					getDailyPassports(atp.getId(), atp);
					break;
				case CUMULATIVE:
					getCumulativePassports(atp.getId(), atp);
					break;
				case ANNIVERSARY:
					getAnniversaryPassports(atp.getId(), atp);
					break;
			}
		}
	}
	
	public void getDailyPassports(int id, AtreianPassport atp)
	{
		if (daily.containsValue(id))
		{
			return;
		}
		daily.put(id, atp);
	}
	
	public void getCumulativePassports(int id, AtreianPassport atp)
	{
		if (cumu.containsValue(id))
		{
			return;
		}
		cumu.put(id, atp);
	}
	
	public void getAnniversaryPassports(int id, AtreianPassport atp)
	{
		if (anny.containsValue(id))
		{
			return;
		}
		anny.put(id, atp);
	}
	
	public int getArrival()
	{
		switch (calendar.get(Calendar.MONTH))
		{
			case Calendar.NOVEMBER:
				return 1;
			case Calendar.DECEMBER:
				return 2;
			case Calendar.JANUARY:
				return 3;
			case Calendar.FEBRUARY:
				return 4;
			case Calendar.MARCH:
				return 5;
			case Calendar.APRIL:
				return 6;
			case Calendar.MAY:
				return 7;
			case Calendar.JUNE:
				return 8;
			case Calendar.JULY:
				return 9;
			case Calendar.AUGUST:
				return 10;
			case Calendar.SEPTEMBER:
				return 11;
			case Calendar.OCTOBER:
				return 12;
			default:
				return 0;
		}
	}
	
	public int getCachedPassport()
	{
		return cachedPassport;
	}
	
	public void setCachedPassport(int cachedPassport)
	{
		this.cachedPassport = cachedPassport;
	}
	
	public static AtreianPassportService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	private static class SingletonHolder
	{
		protected static final AtreianPassportService instance = new AtreianPassportService();
	}
}