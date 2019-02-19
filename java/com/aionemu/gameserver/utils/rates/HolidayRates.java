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
package com.aionemu.gameserver.utils.rates;

import java.util.Calendar;
import java.util.Date;

import com.aionemu.gameserver.configs.main.RateConfig;

/**
 * @author Dr2co
 */
public class HolidayRates
{
	private static Calendar calendar = Calendar.getInstance();
	
	public static int getHolidayRate(int membership)
	{
		if (RateConfig.ENABLE_HOLIDAY_RATE)
		{
			final Date date = new Date();
			calendar.setTime(date);
			int rate = 0;
			switch (membership)
			{
				case 0:
				{
					rate = RateConfig.HOLIDAY_RATE_REGULAR;
					break;
				}
				case 1:
				{
					rate = RateConfig.HOLIDAY_RATE_PREMIUM;
					break;
				}
				case 2:
				{
					rate = RateConfig.HOLIDAY_RATE_VIP;
					break;
				}
			}
			
			for (String level : RateConfig.HOLIDAY_RATE_DAYS.split(","))
			{
				if (calendar.get(Calendar.DAY_OF_WEEK) == Integer.parseInt(level))
				{
					return rate;
				}
			}
		}
		return 0;
	}
}
