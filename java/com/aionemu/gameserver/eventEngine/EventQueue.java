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
package com.aionemu.gameserver.eventEngine;

import java.util.AbstractQueue;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.PriorityQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by wanke on 12/02/2017.
 * @param <E>
 */

public class EventQueue<E extends DelayedEvent>extends AbstractQueue<E> implements BlockingQueue<E>
{
	final transient ReentrantLock lock = new ReentrantLock();
	private final transient Condition available = lock.newCondition();
	final PriorityQueue<E> q = new PriorityQueue<>();
	
	public EventQueue()
	{
	}
	
	public EventQueue(Collection<? extends E> c)
	{
		addAll(c);
	}
	
	@Override
	public boolean add(E e)
	{
		return offer(e);
	}
	
	@Override
	public boolean offer(E e)
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			final E first = q.peek();
			q.offer(e);
			if ((first == null) || (e.compareTo(first) < 0))
			{
				available.signalAll();
			}
			return true;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public void put(E e)
	{
		offer(e);
	}
	
	@Override
	public boolean offer(E e, long timeout, TimeUnit unit)
	{
		return offer(e);
	}
	
	@Override
	public E poll()
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			final E first = q.peek();
			if ((first == null) || (first.getDelay(TimeUnit.NANOSECONDS) > 0))
			{
				return null;
			}
			final E x = q.poll();
			assert x != null;
			if (q.size() != 0)
			{
				available.signalAll();
			}
			return x;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public E take() throws InterruptedException
	{
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try
		{
			for (;;)
			{
				final E first = q.peek();
				if (first == null)
				{
					available.await();
				}
				else
				{
					final long delay = first.getDelay(TimeUnit.NANOSECONDS);
					if (delay > 0)
					{
						@SuppressWarnings("unused")
						final long tl = available.awaitNanos(delay);
					}
					else
					{
						final E x = q.poll();
						assert x != null;
						if (q.size() != 0)
						{
							available.signalAll();
						}
						return x;
					}
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public E poll(long timeout, TimeUnit unit) throws InterruptedException
	{
		long nanos = unit.toNanos(timeout);
		final ReentrantLock lock = this.lock;
		lock.lockInterruptibly();
		try
		{
			for (;;)
			{
				final E first = q.peek();
				if (first == null)
				{
					if (nanos <= 0)
					{
						return null;
					}
					nanos = available.awaitNanos(nanos);
				}
				else
				{
					long delay = first.getDelay(TimeUnit.NANOSECONDS);
					if (delay > 0)
					{
						if (nanos <= 0)
						{
							return null;
						}
						if (delay > nanos)
						{
							delay = nanos;
						}
						final long timeLeft = available.awaitNanos(delay);
						nanos -= delay - timeLeft;
					}
					else
					{
						final E x = q.poll();
						assert x != null;
						if (q.size() != 0)
						{
							available.signalAll();
						}
						return x;
					}
				}
			}
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public E peek()
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			return q.peek();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public int size()
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			return q.size();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public int drainTo(Collection<? super E> c)
	{
		if (c == null)
		{
			throw new NullPointerException();
		}
		if (c == this)
		{
			throw new IllegalArgumentException();
		}
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			int n = 0;
			for (;;)
			{
				final E first = q.peek();
				if ((first == null) || (first.getDelay(TimeUnit.NANOSECONDS) > 0))
				{
					break;
				}
				c.add(q.poll());
				++n;
			}
			if (n > 0)
			{
				available.signalAll();
			}
			return n;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public int drainTo(Collection<? super E> c, int maxElements)
	{
		if (c == null)
		{
			throw new NullPointerException();
		}
		if (c == this)
		{
			throw new IllegalArgumentException();
		}
		if (maxElements <= 0)
		{
			return 0;
		}
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			int n = 0;
			while (n < maxElements)
			{
				final E first = q.peek();
				if ((first == null) || (first.getDelay(TimeUnit.NANOSECONDS) > 0))
				{
					break;
				}
				c.add(q.poll());
				++n;
			}
			if (n > 0)
			{
				available.signalAll();
			}
			return n;
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public void clear()
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			q.clear();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public int remainingCapacity()
	{
		return Integer.MAX_VALUE;
	}
	
	@Override
	public Object[] toArray()
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			return q.toArray();
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public <T> T[] toArray(T[] a)
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			return q.toArray(a);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public boolean remove(Object o)
	{
		final ReentrantLock lock = this.lock;
		lock.lock();
		try
		{
			return q.remove(o);
		}
		finally
		{
			lock.unlock();
		}
	}
	
	@Override
	public Iterator<E> iterator()
	{
		return new Itr(toArray());
	}
	
	private class Itr implements Iterator<E>
	{
		final Object[] array;
		int cursor;
		int lastRet;
		
		Itr(Object[] array)
		{
			lastRet = -1;
			this.array = array;
		}
		
		@Override
		public boolean hasNext()
		{
			return cursor < array.length;
		}
		
		@Override
		@SuppressWarnings("unchecked")
		public E next()
		{
			if (cursor >= array.length)
			{
				throw new NoSuchElementException();
			}
			lastRet = cursor;
			return (E) array[cursor++];
		}
		
		@Override
		public void remove()
		{
			if (lastRet < 0)
			{
				throw new IllegalStateException();
			}
			final Object x = array[lastRet];
			lastRet = -1;
			lock.lock();
			try
			{
				for (@SuppressWarnings("rawtypes")
				final Iterator it = q.iterator(); it.hasNext();)
				{
					if (it.next() == x)
					{
						it.remove();
						return;
					}
				}
			}
			finally
			{
				lock.unlock();
			}
		}
	}
}