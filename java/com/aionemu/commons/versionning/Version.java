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
package com.aionemu.commons.versionning;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.jar.Attributes;
import java.util.jar.JarFile;
import java.util.jar.Manifest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author lord_rex
 */
public class Version
{
	private static final Logger log = LoggerFactory.getLogger(Version.class);
	private String revision;
	private String date;
	private String branch;
	private String commitTime;
	
	public Version()
	{
	}
	
	public Version(Class<?> c)
	{
		loadInformation(c);
	}
	
	public void loadInformation(Class<?> c)
	{
		File jarName = null;
		try
		{
			jarName = Locator.getClassSource(c);
			final JarFile jarFile = new JarFile(jarName);
			
			final Attributes attrs = jarFile.getManifest().getMainAttributes();
			revision = getAttribute("Revision", attrs);
			date = getAttribute("Date", attrs);
			branch = getAttribute("Branch", attrs);
			commitTime = getAttribute("CommitTime", attrs);
		}
		catch (final IOException e)
		{
			log.error("Unable to get Soft information\nFile name '" + (jarName == null ? "null" : jarName.getAbsolutePath()) + "' isn't a valid jar", e);
		}
		
	}
	
	public void transferInfo(String jarName, String type, File fileToWrite)
	{
		try
		{
			if (!fileToWrite.exists())
			{
				log.error("Unable to Find File :" + fileToWrite.getName() + " Please Update your " + type);
				return;
			}
			// Open the JAR file
			final JarFile jarFile = new JarFile("./" + jarName);
			// Get the manifest
			final Manifest manifest = jarFile.getManifest();
			// Write the manifest to a file
			final OutputStream fos = new FileOutputStream(fileToWrite);
			manifest.write(fos);
			fos.close();
		}
		catch (final IOException e)
		{
			log.error("Error, " + e);
		}
	}
	
	public final String getRevision()
	{
		return revision;
	}
	
	public final String getDate()
	{
		return date;
	}
	
	public final String getBranch()
	{
		return branch;
	}
	
	public final String getCommitTime()
	{
		return commitTime;
	}
	
	private final String getAttribute(String attribute, Attributes attrs)
	{
		final String date = attrs.getValue(attribute);
		return date != null ? date : "Unknown " + attribute;
	}
}
