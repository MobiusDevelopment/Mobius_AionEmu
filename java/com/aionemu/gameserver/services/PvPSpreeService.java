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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.PvPConfig;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

public class PvPSpreeService
{
	private static final Logger log = LoggerFactory.getLogger("PVP_LOG");
	private static final String STRING_SPREE1 = "Bloody Storm";
	private static final String STRING_SPREE2 = "Carnage";
	private static final String STRING_SPREE3 = "Genocide";
	
	public static void increaseRawKillCount(Player winner)
	{
		final int currentRawKillCount = winner.getRawKillCount();
		winner.setRawKillCount(currentRawKillCount + 1);
		final int newRawKillCount = currentRawKillCount + 1;
		PacketSendUtility.sendWhiteMessageOnCenter(winner, "You killed " + newRawKillCount + " players in a row for the moment.");
		
		if ((newRawKillCount == PvPConfig.SPREE_KILL_COUNT) || (newRawKillCount == PvPConfig.RAMPAGE_KILL_COUNT) || (newRawKillCount == PvPConfig.GENOCIDE_KILL_COUNT))
		{
			if (newRawKillCount == PvPConfig.SPREE_KILL_COUNT)
			{
				updateSpreeLevel(winner, 1);
			}
			if (newRawKillCount == PvPConfig.RAMPAGE_KILL_COUNT)
			{
				updateSpreeLevel(winner, 2);
			}
			if (newRawKillCount == PvPConfig.GENOCIDE_KILL_COUNT)
			{
				updateSpreeLevel(winner, 3);
			}
		}
	}
	
	private static void updateSpreeLevel(Player winner, int level)
	{
		winner.setSpreeLevel(level);
		sendUpdateSpreeMessage(winner, level);
	}
	
	private static void sendUpdateSpreeMessage(Player winner, int level)
	{
		for (final Player p : World.getInstance().getAllPlayers())
		{
			if (level == 1)
			{
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + " Of " + winner.getCommonData().getRace().toString().toLowerCase() + " has started a " + STRING_SPREE1 + " !");
			}
			if (level == 2)
			{
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + " Of " + winner.getCommonData().getRace().toString().toLowerCase() + " is performing a true " + STRING_SPREE2 + " ! Stop him fast !");
			}
			if (level == 3)
			{
				PacketSendUtility.sendBrightYellowMessageOnCenter(p, winner.getName() + " Of " + winner.getCommonData().getRace().toString().toLowerCase() + " is doing a " + STRING_SPREE3 + " ! Run away if you can! !");
			}
		}
		log.info("[PvP][Spree] {Player : " + winner.getName() + "} is now on a level " + level + " Killing Spree");
	}
	
	public static void cancelSpree(Player victim, Creature killer, boolean isPvPDeath)
	{
		final int killsBeforeDeath = victim.getRawKillCount();
		victim.setRawKillCount(0);
		if (victim.getSpreeLevel() > 0)
		{
			victim.setSpreeLevel(0);
			sendEndSpreeMessage(victim, killer, isPvPDeath, killsBeforeDeath);
		}
	}
	
	private static void sendEndSpreeMessage(Player victim, Creature killer, boolean isPvPDeath, int killsBeforeDeath)
	{
		final String spreeEnder = isPvPDeath ? ((Player) killer).getName() : "A monster";
		for (final Player p : World.getInstance().getAllPlayers())
		{
			PacketSendUtility.sendWhiteMessageOnCenter(p, "The killing spree of " + victim.getName() + " has been stopped by " + spreeEnder + " after " + killsBeforeDeath + " uninterrupted murders !");
		}
		log.info("[PvP][Spree] {The killing spree of " + victim.getName() + "} has been stopped by " + spreeEnder + "}");
	}
}