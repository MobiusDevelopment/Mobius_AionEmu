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
package com.aionemu.chatserver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.chatserver.configs.Config;
import com.aionemu.chatserver.network.netty.NettyServer;
import com.aionemu.chatserver.service.BroadcastService;
import com.aionemu.chatserver.service.ChatService;
import com.aionemu.chatserver.service.GameServerService;
import com.aionemu.chatserver.service.RestartService;
import com.aionemu.chatserver.utils.IdFactory;
import com.aionemu.commons.utils.AEInfos;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.joran.JoranConfigurator;
import ch.qos.logback.core.joran.spi.JoranException;

/**
 * @author ATracer, KID, nrg
 */
public class ChatServer
{
	
	/**
	 * Logger for this class.
	 */
	private static final Logger log = LoggerFactory.getLogger(ChatServer.class);
	
	private static void initalizeLoggger()
	{
		new File("./log/backup/").mkdirs();
		final File[] files = new File("log").listFiles((FilenameFilter) (dir, name) -> name.endsWith(".log"));
		
		if ((files != null) && (files.length > 0))
		{
			final byte[] buf = new byte[1024];
			try
			{
				final String outFilename = "./log/backup/" + new SimpleDateFormat("yyyy-MM-dd HHmmss").format(new Date()) + ".zip";
				final ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
				out.setMethod(ZipOutputStream.DEFLATED);
				out.setLevel(Deflater.BEST_COMPRESSION);
				
				for (final File logFile : files)
				{
					final FileInputStream in = new FileInputStream(logFile);
					out.putNextEntry(new ZipEntry(logFile.getName()));
					int len;
					while ((len = in.read(buf)) > 0)
					{
						out.write(buf, 0, len);
					}
					out.closeEntry();
					in.close();
					logFile.delete();
				}
				out.close();
			}
			catch (final IOException e)
			{
			}
		}
		final LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
		try
		{
			final JoranConfigurator configurator = new JoranConfigurator();
			configurator.setContext(lc);
			lc.reset();
			configurator.doConfigure("config/slf4j-logback.xml");
		}
		catch (final JoranException je)
		{
			throw new RuntimeException("Failed to configure loggers, shutting down...", je);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		final long start = System.currentTimeMillis();
		
		initalizeLoggger();
		
		Config.load();
		AEInfos.printAllInfos();
		IdFactory.getInstance();
		GameServerService.getInstance();
		BroadcastService.getInstance();
		ChatService.getInstance();
		NettyServer.getInstance();
		RestartService.getInstance();
		
		Runtime.getRuntime().addShutdownHook(ShutdownHook.getInstance());
		log.info("AionEmu Chat Server started in " + ((System.currentTimeMillis() - start) / 1000) + " seconds.");
	}
}
