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
package com.aionemu.commons.utils.concurrent;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

import com.aionemu.commons.network.util.ThreadUncaughtExceptionHandler;

/**
 * @author -Nemesiss-
 */
public class PriorityThreadFactory implements ThreadFactory
{
	/**
	 * Priority of new threads
	 */
	private final int prio;
	/**
	 * Thread group name
	 */
	private final String name;
	
	/*
	 * Default pool for the thread group, can be null for default
	 */
	private ExecutorService threadPool;
	
	/**
	 * Number of created threads
	 */
	private final AtomicInteger threadNumber = new AtomicInteger(1);
	/**
	 * ThreadGroup for created threads
	 */
	private final ThreadGroup group;
	
	/**
	 * Constructor.
	 * @param name
	 * @param prio
	 */
	public PriorityThreadFactory(String name, int prio)
	{
		this.prio = prio;
		this.name = name;
		group = new ThreadGroup(this.name);
	}
	
	public PriorityThreadFactory(String name, ExecutorService defaultPool)
	{
		this(name, Thread.NORM_PRIORITY);
		setDefaultPool(defaultPool);
	}
	
	protected void setDefaultPool(ExecutorService pool)
	{
		threadPool = pool;
	}
	
	protected ExecutorService getDefaultPool()
	{
		return threadPool;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Thread newThread(Runnable r)
	{
		final Thread t = new Thread(group, r);
		t.setName(name + "-" + threadNumber.getAndIncrement());
		t.setPriority(prio);
		t.setUncaughtExceptionHandler(new ThreadUncaughtExceptionHandler());
		return t;
	}
}
