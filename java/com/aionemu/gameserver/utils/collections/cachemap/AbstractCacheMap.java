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
package com.aionemu.gameserver.utils.collections.cachemap;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;

/**
 * Base class for {@link WeakCacheMap} and {@link SoftCacheMap}
 * @author Luno
 * @param <K>
 * @param <V>
 */
abstract class AbstractCacheMap<K, V> implements CacheMap<K, V>
{
	
	private final Logger log;
	
	protected final String cacheName;
	protected final String valueName;
	
	/** Map storing references to cached objects */
	protected final Map<K, Reference<V>> cacheMap = new HashMap<>();
	
	protected final ReferenceQueue<V> refQueue = new ReferenceQueue<>();
	
	/**
	 * @param cacheName
	 * @param valueName
	 * @param log
	 */
	AbstractCacheMap(String cacheName, String valueName, Logger log)
	{
		this.cacheName = "#CACHE  [" + cacheName + "]#  ";
		this.valueName = valueName;
		this.log = log;
	}
	
	/** {@inheritDoc} */
	@Override
	public void put(K key, V value)
	{
		cleanQueue();
		
		if (cacheMap.containsKey(key))
		{
			throw new IllegalArgumentException("Key: " + key + " already exists in map");
		}
		
		final Reference<V> entry = newReference(key, value, refQueue);
		
		cacheMap.put(key, entry);
		
		if (log.isDebugEnabled())
		{
			log.debug(cacheName + " : added " + valueName + " for key: " + key);
		}
	}
	
	/** {@inheritDoc} */
	@Override
	public V get(K key)
	{
		cleanQueue();
		
		final Reference<V> reference = cacheMap.get(key);
		
		if (reference == null)
		{
			return null;
		}
		
		final V res = reference.get();
		
		if ((res != null) && log.isDebugEnabled())
		{
			log.debug(cacheName + " : obtained " + valueName + " for key: " + key);
		}
		
		return res;
	}
	
	@Override
	public boolean contains(K key)
	{
		cleanQueue();
		return cacheMap.containsKey(key);
	}
	
	protected abstract void cleanQueue();
	
	@Override
	public void remove(K key)
	{
		cacheMap.remove(key);
	}
	
	protected abstract Reference<V> newReference(K key, V value, ReferenceQueue<V> queue);
}
