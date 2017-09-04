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
package com.aionemu.commons.scripting.url;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

import com.aionemu.commons.scripting.ScriptClassLoader;

/**
 * This class represents URL Connection that is used to "connect" to scripts binary data that was loaded by specified {@link com.aionemu.commons.scripting.impl.javacompiler.ScriptCompilerImpl}.<br>
 * <br>
 * TODO: Implement all methods of {@link URLConnection} to ensure valid behaviour
 * @author SoulKeeper
 */
public class VirtualClassURLConnection extends URLConnection
{
	/**
	 * Input stream, is assigned from class
	 */
	private final InputStream is;
	
	/**
	 * Creates URL connections that "connects" to class binary data
	 * @param url class name
	 * @param cl classloader
	 */
	protected VirtualClassURLConnection(URL url, ScriptClassLoader cl)
	{
		super(url);
		is = new ByteArrayInputStream(cl.getByteCode(url.getHost()));
	}
	
	/**
	 * This method is ignored
	 */
	@Override
	public void connect()
	{
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public InputStream getInputStream()
	{
		return is;
	}
}
