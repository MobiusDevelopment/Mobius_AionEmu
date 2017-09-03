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
package com.aionemu.commons.network;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.util.Iterator;

/**
 * This is implementation of <code>Dispatcher</code> that may only accept connections.
 * @author -Nemesiss-
 * @see com.aionemu.commons.network.Dispatcher
 * @see java.nio.channels.Selector
 */
public class AcceptDispatcherImpl extends Dispatcher
{
	/**
	 * Constructor that accept <code>String</code> name as parameter.
	 * @param name
	 * @throws IOException
	 */
	public AcceptDispatcherImpl(String name) throws IOException
	{
		super(name, null);
	}
	
	/**
	 * Dispatch <code>Selector</code> selected-key set.
	 * @see com.aionemu.commons.network.Dispatcher#dispatch()
	 */
	@Override
	void dispatch() throws IOException
	{
		if (selector.select() != 0)
		{
			final Iterator<SelectionKey> selectedKeys = selector.selectedKeys().iterator();
			while (selectedKeys.hasNext())
			{
				final SelectionKey key = selectedKeys.next();
				selectedKeys.remove();
				
				if (key.isValid())
				{
					accept(key);
				}
			}
		}
	}
	
	/**
	 * This method should never be called on this implementation of <code>Dispatcher</code>
	 * @throws UnsupportedOperationException always!
	 * @see com.aionemu.commons.network.Dispatcher#closeConnection(com.aionemu.commons.network.AConnection)
	 */
	@Override
	void closeConnection(AConnection con)
	{
		throw new UnsupportedOperationException("This method should never be called!");
	}
}
