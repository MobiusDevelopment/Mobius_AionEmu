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
package com.aionemu.gameserver.taskmanager;

import java.text.ParseException;
import java.util.Date;

import org.quartz.CronExpression;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.dao.ServerVariablesDAO;
import com.aionemu.gameserver.utils.ThreadPoolManager;

public abstract class AbstractCronTask implements Runnable
{
	private final String cronExpressionString;
	private final CronExpression runExpression;
	private int runTime;
	private final long period;
	
	public final int getRunTime()
	{
		return runTime;
	}
	
	protected abstract long getRunDelay();
	
	protected void preInit()
	{
	}
	
	protected void postInit()
	{
	}
	
	public final String getCronExpressionString()
	{
		return cronExpressionString;
	}
	
	protected abstract String getServerTimeVariable();
	
	public long getPeriod()
	{
		return period;
	}
	
	protected void preRun()
	{
	}
	
	protected abstract void executeTask();
	
	protected abstract boolean canRunOnInit();
	
	protected void postRun()
	{
	}
	
	public AbstractCronTask(String cronExpression) throws ParseException
	{
		if (cronExpression == null)
		{
			throw new NullPointerException("cronExpressionString");
		}
		cronExpressionString = cronExpression;
		final ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = dao.load(getServerTimeVariable());
		preInit();
		runExpression = new CronExpression(cronExpressionString);
		final Date nextDate = runExpression.getTimeAfter(new Date());
		final Date nextAfterDate = runExpression.getTimeAfter(nextDate);
		period = nextAfterDate.getTime() - nextDate.getTime();
		postInit();
		if (getRunDelay() == 0)
		{
			if (canRunOnInit())
			{
				ThreadPoolManager.getInstance().schedule(this, 0);
			}
			else
			{
				saveNextRunTime();
			}
		}
		scheduleNextRun();
	}
	
	private void scheduleNextRun()
	{
		CronService.getInstance().schedule(this, cronExpressionString, true);
	}
	
	private void saveNextRunTime()
	{
		final Date nextDate = runExpression.getTimeAfter(new Date());
		final ServerVariablesDAO dao = DAOManager.getDAO(ServerVariablesDAO.class);
		runTime = (int) (nextDate.getTime() / 1000);
		dao.store(getServerTimeVariable(), runTime);
	}
	
	@Override
	public final void run()
	{
		if (getRunDelay() > 0)
		{
			ThreadPoolManager.getInstance().schedule(this, getRunDelay());
		}
		else
		{
			preRun();
			executeTask();
			saveNextRunTime();
			postRun();
		}
	}
}