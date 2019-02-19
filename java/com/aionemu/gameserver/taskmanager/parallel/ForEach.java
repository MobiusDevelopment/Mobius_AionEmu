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
package com.aionemu.gameserver.taskmanager.parallel;

import java.util.Collection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.internal.chmv8.CountedCompleter;
import com.aionemu.commons.utils.internal.chmv8.ForkJoinTask;
import com.google.common.base.Predicate;

/**
 * @author Rolandas <br>
 *         To use forEach method, statically import the method</tt>
 * @param <E>
 */
public final class ForEach<E>extends CountedCompleter<E>
{
	private static final Logger log = LoggerFactory.getLogger(ForEach.class);
	private static final long serialVersionUID = 7902148320917998146L;
	
	public static <E> ForkJoinTask<E> forEach(Collection<E> list, Predicate<E> operation)
	{
		if (list.size() > 0)
		{
			@SuppressWarnings("unchecked")
			final E[] objects = list.toArray((E[]) new Object[list.size()]);
			final CountedCompleter<E> completer = new ForEach<>(null, operation, 0, objects.length, objects);
			return completer;
		}
		return null;
	}
	
	@SafeVarargs
	public static <E> ForkJoinTask<E> forEach(Predicate<E> operation, E... list)
	{
		if ((list != null) && (list.length > 0))
		{
			final CountedCompleter<E> completer = new ForEach<>(null, operation, 0, list.length, list);
			return completer;
		}
		return null;
	}
	
	final E[] list;
	final Predicate<E> operation;
	final int lo, hi;
	
	@SafeVarargs
	private ForEach(CountedCompleter<E> rootTask, Predicate<E> operation, int lo, int hi, E... list)
	{
		super(rootTask);
		this.list = list;
		this.operation = operation;
		this.lo = lo;
		this.hi = hi;
	}
	
	@Override
	public void compute()
	{
		final int l = lo;
		int h = hi;
		while ((h - l) >= 2)
		{
			final int mid = (l + h) >>> 1;
			addToPendingCount(1);
			new ForEach<>(this, operation, mid, h, list).fork();
			h = mid;
		}
		if (h > l)
		{
			try
			{
				operation.apply(list[l]);
			}
			catch (Throwable ex)
			{
				onExceptionalCompletion(ex, this);
			}
		}
		propagateCompletion();
	}
	
	@Override
	public boolean onExceptionalCompletion(Throwable ex, CountedCompleter<?> caller)
	{
		log.warn("", ex);
		return true;
	}
}