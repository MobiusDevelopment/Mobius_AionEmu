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
package system.handlers.ai.worlds.panesterra;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("enraged_guardian_general")
public class EnragedGuardianGeneralAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
	}
	
	@Override
	protected void handleDied()
	{
		/**
		 * Enraged Guardian General "Ai2" coded because "Panesterra Fortress" are not considered like basic fortress. And so "Enraged Guardian General" does not pop chest after is dead.
		 */
		switch (getNpcId())
		{
			case 277410: // Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1015.6511f, 1010.91876f, 1552.5599f, (byte) 85);
						spawn(701481, 1038.5582f, 1023.6584f, 1552.5657f, (byte) 116);
						spawn(701481, 1017.0155f, 1037.0358f, 1552.5647f, (byte) 32);
					}
				}, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(802219, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
					}
				}, 480000);
				break;
			case 277425: // Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1015.6511f, 1010.91876f, 1552.5599f, (byte) 85);
						spawn(701481, 1038.5582f, 1023.6584f, 1552.5657f, (byte) 116);
						spawn(701481, 1017.0155f, 1037.0358f, 1552.5647f, (byte) 32);
					}
				}, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(802221, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
					}
				}, 480000);
				break;
			case 277440: // Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1015.6511f, 1010.91876f, 1552.5599f, (byte) 85);
						spawn(701481, 1038.5582f, 1023.6584f, 1552.5657f, (byte) 116);
						spawn(701481, 1017.0155f, 1037.0358f, 1552.5647f, (byte) 32);
					}
				}, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(802223, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
					}
				}, 480000);
				break;
			case 277455: // Enraged Guardian General.
				killedTheGuardianGeneral();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1015.6511f, 1010.91876f, 1552.5599f, (byte) 85);
						spawn(701481, 1038.5582f, 1023.6584f, 1552.5657f, (byte) 116);
						spawn(701481, 1017.0155f, 1037.0358f, 1552.5647f, (byte) 32);
					}
				}, 10000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(802225, 1024.12f, 1078.747f, 1530.2688f, (byte) 90);
					}
				}, 480000);
				break;
		}
		super.handleDied();
	}
	
	private void killedTheGuardianGeneral()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// Loading the Advance Corridor Shield... Please wait.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_01, 0);
				// The entrance to the Transidium Annex will open in 8 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_02, 10000);
				// The entrance to the Transidium Annex will open in 6 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_03, 120000);
				// The entrance to the Transidium Annex will open in 4 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_04, 240000);
				// The entrance to the Transidium Annex will open in 2 minutes.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_05, 360000);
				// The entrance to the Transidium Annex will open in 1 minute.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_06, 420000);
				// The entrance to the Transidium Annex has opened.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_08, 480000);
			}
		});
	}
}