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
package com.aionemu.commons.services.cron;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;

public abstract class RunnableRunner implements Job
{
	public static final String KEY_RUNNABLE_OBJECT = "cronservice.scheduled.runnable.instance";
	public static final String KEY_PROPERTY_IS_LONGRUNNING_TASK = "cronservice.scheduled.runnable.islognrunning";
	public static final String KEY_CRON_EXPRESSION = "cronservice.scheduled.runnable.cronexpression";
	
	@Override
	public void execute(JobExecutionContext context)
	{
		
		final JobDataMap jdm = context.getJobDetail().getJobDataMap();
		
		final Runnable r = (Runnable) jdm.get(KEY_RUNNABLE_OBJECT);
		final boolean longRunning = jdm.getBoolean(KEY_PROPERTY_IS_LONGRUNNING_TASK);
		
		if (longRunning)
		{
			executeLongRunningRunnable(r);
		}
		else
		{
			executeRunnable(r);
		}
	}
	
	public abstract void executeRunnable(Runnable r);
	
	public abstract void executeLongRunningRunnable(Runnable r);
}
