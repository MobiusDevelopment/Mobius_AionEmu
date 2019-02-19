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

import com.aionemu.commons.database.dao.DAO;
import com.aionemu.gameserver.model.account.CharacterBanInfo;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.PunishmentService.PunishmentType;

/**
 * @author lord_rex
 */
public abstract class PlayerPunishmentsDAO implements DAO
{
	
	@Override
	public final String getClassName()
	{
		return PlayerPunishmentsDAO.class.getName();
	}
	
	public abstract void loadPlayerPunishments(Player player, PunishmentType punishmentType);
	
	public abstract void storePlayerPunishments(Player player, PunishmentType punishmentType);
	
	public abstract void punishPlayer(int playerId, PunishmentType punishmentType, long expireTime, String reason);
	
	public abstract void punishPlayer(Player player, PunishmentType punishmentType, String reason);
	
	public abstract void unpunishPlayer(int playerId, PunishmentType punishmentType);
	
	public abstract CharacterBanInfo getCharBanInfo(int playerId);
}
