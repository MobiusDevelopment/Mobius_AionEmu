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

import java.sql.Timestamp;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.account.PlayerAccountData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;

/**
 * Class that is responsible for storing/loading player data
 * @author SoulKeeper, Saelya
 * @author cura
 */
public abstract class PlayerDAO implements IDFactoryAwareDAO
{
	public abstract boolean isNameUsed(String name);
	
	public abstract Map<Integer, String> getPlayerNames(Collection<Integer> playerObjectIds);
	
	public abstract void storePlayer(Player player);
	
	public abstract boolean saveNewPlayer(PlayerCommonData pcd, int accountId, String accountName);
	
	public abstract PlayerCommonData loadPlayerCommonData(int playerObjId);
	
	public abstract void deletePlayer(int playerId);
	
	public abstract void updateDeletionTime(int objectId, Timestamp deletionDate);
	
	public abstract void storeCreationTime(int objectId, Timestamp creationDate);
	
	public abstract void setCreationDeletionTime(PlayerAccountData acData);
	
	public abstract List<Integer> getPlayerOidsOnAccount(int accountId);
	
	public abstract void storeLastOnlineTime(int objectId, Timestamp lastOnline);
	
	public abstract void onlinePlayer(Player player, boolean online);
	
	public abstract void setPlayersOffline(boolean online);
	
	public abstract PlayerCommonData loadPlayerCommonDataByName(String name);
	
	public abstract int getAccountIdByName(String name);
	
	public abstract String getPlayerNameByObjId(int playerObjId);
	
	public abstract int getPlayerIdByName(String playerName);
	
	public abstract void storePlayerName(PlayerCommonData recipientCommonData);
	
	public abstract int getCharacterCountOnAccount(int accountId);
	
	public abstract int getCharacterCountForRace(Race race);
	
	public abstract int getOnlinePlayerCount();
	
	public abstract List<Integer> getPlayersToDelete(int paramInt1, int paramInt2);
	
	public abstract void setPlayerLastTransferTime(int playerId, long time);
	
	@Override
	public final String getClassName()
	{
		return PlayerDAO.class.getName();
	}
	
	public abstract Timestamp getCharacterCreationDateId(int obj);
	
	public abstract void updateLegionJoinRequestState(int playerId, LegionJoinRequestState state);
	
	public abstract void clearJoinRequest(int playerId);
	
	public abstract void getJoinRequestState(Player player);
	
	public abstract int getPlayerLunaConsumeByObjId(int playerObjId);
}