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
package system.handlers.ai.worlds.gelkmaros;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.GeneralNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("marchutan_agent")
public class Marchutan_AgentAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		announceMarchutanAgent();
		startLifeTask();
	}
	
	private void announceMarchutanAgent()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// The Agent's Adjutant has appeared.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_Reward, 0);
				// The Agent is here, and it is angry!
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_Spawned, 20000);
			}
		});
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				AI2Actions.deleteOwner(Marchutan_AgentAI2.this);
			}
		}, 600000); // 10 Minutes.
	}
}