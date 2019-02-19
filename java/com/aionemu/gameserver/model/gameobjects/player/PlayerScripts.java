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
package com.aionemu.gameserver.model.gameobjects.player;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.HousingConfig;
import com.aionemu.gameserver.dao.HouseScriptsDAO;
import com.aionemu.gameserver.model.house.PlayerScript;
import com.aionemu.gameserver.utils.xml.CompressUtil;

/**
 * @author Rolandas
 */
public class PlayerScripts
{
	private static final Logger logger = LoggerFactory.getLogger(PlayerScripts.class);
	private final Map<Integer, PlayerScript> scripts;
	private final int houseObjId;
	
	public PlayerScripts(int houseObjectId)
	{
		scripts = new HashMap<>(8);
		for (int index = 0; index < 8; index++)
		{
			scripts.put(index, new PlayerScript());
		}
		houseObjId = houseObjectId;
	}
	
	public Map<Integer, PlayerScript> getScripts()
	{
		return Collections.unmodifiableMap(scripts);
	}
	
	@SuppressWarnings("null")
	public boolean addScript(int position, String scriptXML)
	{
		final PlayerScript script = scripts.get(position);
		
		if (scriptXML == null)
		{
			script.setData(null, -1);
		}
		else if ("".equals(scriptXML))
		{
			script.setData(new byte[0], 0);
		}
		
		try
		{
			byte[] bytes = CompressUtil.Compress(scriptXML);
			final int oldLength = bytes.length;
			bytes = Arrays.copyOf(bytes, bytes.length + 8);
			for (int i = oldLength; i < bytes.length; i++)
			{
				bytes[i] = -51;
			}
			script.setData(bytes, scriptXML.length() * 2);
		}
		catch (Exception ex)
		{
			logger.error("Script compression failed: " + ex);
			return false;
		}
		
		return script == null;
	}
	
	public String getUncompressedScript(int position)
	{
		if (!scripts.containsKey(position))
		{
			return null;
		}
		final PlayerScript script = scripts.get(position);
		byte[] bytes = null;
		
		script.readLock();
		bytes = script.getCompressedBytes();
		script.readUnlock();
		
		if (bytes == null)
		{
			return null;
		}
		if (bytes.length == 0)
		{
			return "";
		}
		try
		{
			return CompressUtil.Decompress(bytes);
		}
		catch (Exception ex)
		{
			logger.error("Script decompression failed: " + ex);
		}
		return null;
	}
	
	public boolean addScript(int position, byte[] compressedXML, int uncompressedSize)
	{
		String content = null;
		int size = -1;
		
		if (compressedXML != null)
		{
			if (compressedXML.length == 0)
			{
				content = "";
				size = 0;
			}
			else
			{
				try
				{
					content = CompressUtil.Decompress(compressedXML);
					final byte[] bytes = content.getBytes("UTF-16LE");
					if (bytes.length != uncompressedSize)
					{
						return false;
					}
					size = uncompressedSize;
				}
				catch (Exception ex)
				{
					return false;
				}
			}
		}
		final PlayerScript script = scripts.get(position);
		script.readLock();
		final byte[] bytes = script.getCompressedBytes();
		script.readUnlock();
		script.setData(compressedXML, size);
		
		if (bytes == null)
		{
			DAOManager.getDAO(HouseScriptsDAO.class).addScript(houseObjId, position, content);
		}
		else
		{
			DAOManager.getDAO(HouseScriptsDAO.class).updateScript(houseObjId, position, content);
		}
		
		if (HousingConfig.HOUSE_SCRIPT_DEBUG)
		{
			logger.info(content);
		}
		return true;
	}
	
	public boolean removeScript(int position)
	{
		final PlayerScript script = scripts.get(position);
		
		script.readLock();
		final byte[] bytes = script.getCompressedBytes();
		script.readUnlock();
		
		if (bytes == null)
		{
			return false;
		}
		
		script.setData(null, -1);
		DAOManager.getDAO(HouseScriptsDAO.class).deleteScript(houseObjId, position);
		
		return true;
	}
	
	public int getSize()
	{
		return 8;
	}
}
