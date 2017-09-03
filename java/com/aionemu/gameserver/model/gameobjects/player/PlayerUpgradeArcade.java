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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerUpgradeArcadeDAO;
import com.aionemu.gameserver.model.gameobjects.PersistentState;

/**
 * @author Ranastic
 */

public class PlayerUpgradeArcade
{
	Logger log = LoggerFactory.getLogger(PlayerUpgradeArcade.class);
	int frenzyMeter;
	int upgradeLvl;
	private PersistentState persistentState;
	
	public PlayerUpgradeArcade(int frenzyMeter, int upgradeLvl)
	{
		this.frenzyMeter = frenzyMeter;
		this.upgradeLvl = upgradeLvl;
		persistentState = PersistentState.NEW;
	}
	
	public void setFrenzyMeter(int meter)
	{
		frenzyMeter = meter;
	}
	
	public int getFrenzyMeter()
	{
		return frenzyMeter;
	}
	
	public void setUpgradeLvl(int upgradeLvl)
	{
		this.upgradeLvl = upgradeLvl;
	}
	
	public int getUpgradeLvl()
	{
		return upgradeLvl;
	}
	
	public PlayerUpgradeArcade()
	{
	}
	
	public void setFrenzyMeterByObjId(int playerId)
	{
		DAOManager.getDAO(PlayerUpgradeArcadeDAO.class).setFrenzyMeterByObjId(playerId, getFrenzyMeter());
	}
	
	public void setUpgradeLvlByObjId(int playerId)
	{
		DAOManager.getDAO(PlayerUpgradeArcadeDAO.class).setUpgradeLvlByObjId(playerId, getUpgradeLvl());
	}
	
	public PersistentState getPersistentState()
	{
		return persistentState;
	}
	
	public void setPersistentState(PersistentState persistentState)
	{
		switch (persistentState)
		{
			case UPDATE_REQUIRED:
				if (this.persistentState == PersistentState.NEW)
				{
					break;
				}
			default:
				this.persistentState = persistentState;
		}
	}
}