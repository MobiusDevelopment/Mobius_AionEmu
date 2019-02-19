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
package com.aionemu.commons.scripting.impl.javacompiler;

import java.io.File;
import java.io.IOException;

import javax.tools.SimpleJavaFileObject;

import org.apache.commons.io.FileUtils;

/**
 * This class is simple wrapper for SimpleJavaFileObject that load class source from file sytem
 * @author SoulKeeper
 */
public class JavaSourceFromFile extends SimpleJavaFileObject
{
	/**
	 * Construct a JavaFileObject of the given kind and with the given File.
	 * @param file the file with source of this file object
	 * @param kind the kind of this file object
	 */
	public JavaSourceFromFile(File file, Kind kind)
	{
		super(file.toURI(), kind);
	}
	
	/**
	 * Returns class source represented as string.
	 * @param ignoreEncodingErrors not used
	 * @return class source
	 * @throws IOException if something goes wrong
	 */
	@SuppressWarnings("deprecation")
	@Override
	public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException
	{
		return FileUtils.readFileToString(new File(toUri()));
	}
}
