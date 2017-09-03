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
package com.aionemu.gameserver.services.player;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.AbyssRankDAO;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.PlayerPassportsDAO;
import com.aionemu.gameserver.dao.PlayerQuestListDAO;
import com.aionemu.gameserver.dao.PlayerSkillListDAO;
import com.aionemu.gameserver.dao.PlayerStigmasEquippedDAO;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.world.World;

class GeneralUpdateTask implements Runnable
{
	private static final Logger log = LoggerFactory.getLogger(GeneralUpdateTask.class);
	private final int playerId;
	
	GeneralUpdateTask(int playerId)
	{
		this.playerId = playerId;
	}
	
	@Override
	public void run()
	{
		final Player player = World.getInstance().findPlayer(playerId);
		if (player != null)
		{
			try
			{
				DAOManager.getDAO(AbyssRankDAO.class).storeAbyssRank(player);
				DAOManager.getDAO(PlayerSkillListDAO.class).storeSkills(player);
				DAOManager.getDAO(PlayerQuestListDAO.class).store(player);
				DAOManager.getDAO(PlayerPassportsDAO.class).store(player);
				DAOManager.getDAO(PlayerDAO.class).storePlayer(player);
				DAOManager.getDAO(PlayerStigmasEquippedDAO.class).storeItems(player);
				for (final House house : player.getHouses())
				{
					house.save();
				}
			}
			catch (final Exception ex)
			{
				log.error("Exception during periodic saving of player " + player.getName(), ex);
			}
		}
	}
}