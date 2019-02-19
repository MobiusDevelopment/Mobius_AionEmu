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
package com.aionemu.gameserver.services.anohaservice;

import java.util.Map;

import com.aionemu.gameserver.model.anoha.AnohaLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AnohaService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Rinzler (Encom)
 */

public class AnohaStartRunnable implements Runnable
{
	final int id;
	
	public AnohaStartRunnable(int id)
	{
		this.id = id;
	}
	
	@Override
	public void run()
	{
		// Berserk Anoha Sword Effect.
		AnohaService.getInstance().adventSwordEffectSP(id);
		World.getInstance().doOnAllPlayers(player ->
		{
			AnohaService.getInstance().sendRequest(player);
			// Berserk Anoha will return to Kaldor in 30 minutes.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF5_Fortress_Named_Spawn_System);
		});
		ThreadPoolManager.getInstance().schedule(() ->
		{
			final Map<Integer, AnohaLocation> locations = AnohaService.getInstance().getAnohaLocations();
			for (AnohaLocation loc : locations.values())
			{
				if (loc.getId() == id)
				{
					AnohaService.getInstance().startAnoha(loc.getId());
				}
			}
			World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Anoha_Spawn));
		}, 1800000);
	}
}