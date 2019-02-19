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

import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.npc.NpcRank;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
public class BossAnnounceKillerService
{
	public static void bossKiller(Player player, Npc owner)
	{
		final NpcRank npcRank = owner.getObjectTemplate().getRank();
		final DescriptionId NameId = new DescriptionId(owner.getObjectTemplate().getNameId());
		if ((npcRank == NpcRank.EXPERT) || (npcRank == NpcRank.VETERAN) || (npcRank == NpcRank.MASTER))
		{
			if (player != null)
			{
				if (!player.isOnline())
				{
					PacketSendUtility.sendWhiteMessageOnCenter(player, "Player: [" + player.getName() + "] Race: [" + player.getCommonData().getRace() + "] has killed [" + NameId + "].");
				}
				if (!player.isInGroup2())
				{
					PacketSendUtility.sendWhiteMessageOnCenter(player, "Player: [" + player.getName() + "] Race: [" + player.getCommonData().getRace() + "] and his group has killed [" + NameId + "].");
				}
				if (!player.isInAlliance2())
				{
					PacketSendUtility.sendWhiteMessageOnCenter(player, "Player: [" + player.getName() + "] Race: [" + player.getCommonData().getRace() + "] and his alliance has killed [" + NameId + "].");
				}
				if (!player.isInLeague())
				{
					PacketSendUtility.sendWhiteMessageOnCenter(player, "Player: [" + player.getName() + "] Race: [" + player.getCommonData().getRace() + "] and his league has killed [" + NameId + "].");
				}
				else
				{
					PacketSendUtility.sendWhiteMessageOnCenter(player, "Player: [" + player.getName() + "] Race: [" + player.getCommonData().getRace() + "] has killed [" + NameId + "].");
				}
			}
		}
	}
}