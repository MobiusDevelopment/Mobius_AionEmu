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
package com.aionemu.gameserver.services;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PetitionDAO;
import com.aionemu.gameserver.model.Petition;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PETITION;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author zdead
 */
public class PetitionService
{
	private static Logger log = LoggerFactory.getLogger(PetitionService.class);
	
	private static SortedMap<Integer, Petition> registeredPetitions = new TreeMap<>();
	
	public static PetitionService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	public PetitionService()
	{
		log.info("Loading PetitionService ...");
		final Set<Petition> petitions = DAOManager.getDAO(PetitionDAO.class).getPetitions();
		for (Petition p : petitions)
		{
			registeredPetitions.put(p.getPetitionId(), p);
		}
		log.info("Successfully loaded " + registeredPetitions.size() + " database petitions");
	}
	
	public Collection<Petition> getRegisteredPetitions()
	{
		return registeredPetitions.values();
	}
	
	public void deletePetition(int playerObjId)
	{
		final Set<Petition> petitions = new HashSet<>();
		for (Petition p : registeredPetitions.values())
		{
			if (p.getPlayerObjId() == playerObjId)
			{
				petitions.add(p);
			}
		}
		for (Petition p : petitions)
		{
			if (registeredPetitions.containsKey(p.getPetitionId()))
			{
				registeredPetitions.remove(p.getPetitionId());
			}
		}
		
		DAOManager.getDAO(PetitionDAO.class).deletePetition(playerObjId);
		if ((playerObjId > 0) && (World.getInstance().findPlayer(playerObjId) != null))
		{
			final Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
		rebroadcastPlayerData();
	}
	
	public void setPetitionReplied(int petitionId)
	{
		final int playerObjId = registeredPetitions.get(petitionId).getPlayerObjId();
		DAOManager.getDAO(PetitionDAO.class).setReplied(petitionId);
		registeredPetitions.remove(petitionId);
		rebroadcastPlayerData();
		if ((playerObjId > 0) && (World.getInstance().findPlayer(playerObjId) != null))
		{
			final Player p = World.getInstance().findPlayer(playerObjId);
			PacketSendUtility.sendPacket(p, new SM_PETITION());
		}
	}
	
	public synchronized Petition registerPetition(Player sender, int typeId, String title, String contentText, String additionalData)
	{
		final int id = DAOManager.getDAO(PetitionDAO.class).getNextAvailableId();
		final Petition ptt = new Petition(id, sender.getObjectId(), typeId, title, contentText, additionalData, 0);
		DAOManager.getDAO(PetitionDAO.class).insertPetition(ptt);
		registeredPetitions.put(ptt.getPetitionId(), ptt);
		broadcastMessageToGM(sender, ptt.getPetitionId());
		return ptt;
	}
	
	private void rebroadcastPlayerData()
	{
		for (Petition p : registeredPetitions.values())
		{
			final Player player = World.getInstance().findPlayer(p.getPlayerObjId());
			if (player != null)
			{
				PacketSendUtility.sendPacket(player, new SM_PETITION(p));
			}
		}
	}
	
	private void broadcastMessageToGM(Player sender, int petitionId)
	{
		final Iterator<Player> players = World.getInstance().getPlayersIterator();
		while (players.hasNext())
		{
			final Player p = players.next();
			if (p.getAccessLevel() > 0)
			{
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, "New Support Petition from: " + sender.getName() + " (#" + petitionId + ")");
			}
		}
	}
	
	public boolean hasRegisteredPetition(Player player)
	{
		return hasRegisteredPetition(player.getObjectId());
	}
	
	public boolean hasRegisteredPetition(int playerObjId)
	{
		boolean result = false;
		for (Petition p : registeredPetitions.values())
		{
			if (p.getPlayerObjId() == playerObjId)
			{
				result = true;
			}
		}
		return result;
	}
	
	public Petition getPetition(int playerObjId)
	{
		for (Petition p : registeredPetitions.values())
		{
			if (p.getPlayerObjId() == playerObjId)
			{
				return p;
			}
		}
		return null;
	}
	
	public synchronized int getNextAvailablePetitionId()
	{
		return 0;
	}
	
	public int getWaitingPlayers(int playerObjId)
	{
		int counter = 0;
		for (Petition p : registeredPetitions.values())
		{
			if (p.getPlayerObjId() == playerObjId)
			{
				break;
			}
			counter++;
		}
		return counter;
	}
	
	public int calculateWaitTime(int playerObjId)
	{
		final int timePerPetition = 15;
		final int timeBetweenPetition = 30;
		int result = timeBetweenPetition;
		for (Petition p : registeredPetitions.values())
		{
			if (p.getPlayerObjId() == playerObjId)
			{
				break;
			}
			result += timePerPetition;
			result += timeBetweenPetition;
		}
		return result;
	}
	
	public void onPlayerLogin(Player player)
	{
		if (hasRegisteredPetition(player))
		{
			PacketSendUtility.sendPacket(player, new SM_PETITION(getPetition(player.getObjectId())));
		}
	}
	
	private static class SingletonHolder
	{
		protected static final PetitionService instance = new PetitionService();
	}
}
