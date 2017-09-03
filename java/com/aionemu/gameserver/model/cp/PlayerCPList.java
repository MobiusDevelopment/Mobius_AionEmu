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
package com.aionemu.gameserver.model.cp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerCPDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;

public final class PlayerCPList implements CPList<Player>
{
	private final Map<Integer, PlayerCPEntry> entry;
	
	public PlayerCPList()
	{
		entry = new HashMap<>(0);
	}
	
	public PlayerCPList(List<PlayerCPEntry> entries)
	{
		this();
		for (PlayerCPEntry e : entries)
		{
			entry.put(e.getSlot(), e);
		}
	}
	
	public PlayerCPEntry[] getAllCP()
	{
		final List<PlayerCPEntry> allCp = new ArrayList<>();
		allCp.addAll(entry.values());
		return allCp.toArray(new PlayerCPEntry[allCp.size()]);
	}
	
	public PlayerCPEntry[] getBasicCP()
	{
		return entry.values().toArray(new PlayerCPEntry[entry.size()]);
	}
	
	@Override
	public boolean addPoint(Player player, int slot, int point)
	{
		return addPoint(player, slot, point, PersistentState.NEW);
	}
	
	private synchronized boolean addPoint(Player player, int slot, int point, PersistentState state)
	{
		entry.put(slot, new PlayerCPEntry(slot, point, state));
		DAOManager.getDAO(PlayerCPDAO.class).storeCP(player.getObjectId(), slot, point);
		return true;
	}
	
	@Override
	public synchronized boolean removePoint(Player player, int slot)
	{
		final PlayerCPEntry entries = entry.get(slot);
		if (entries != null)
		{
			entries.setPersistentState(PersistentState.DELETED);
			entry.remove(slot);
			DAOManager.getDAO(PlayerCPDAO.class).deleteCP(player.getObjectId(), slot);
		}
		return entry != null;
	}
	
	@Override
	public int size()
	{
		return entry.size();
	}
}