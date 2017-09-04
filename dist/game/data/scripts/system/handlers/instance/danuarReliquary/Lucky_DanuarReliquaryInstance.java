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
package system.handlers.instance.danuarReliquary;

import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@InstanceID(301330000)
public class Lucky_DanuarReliquaryInstance extends GeneralInstanceHandler
{
	private int ideanKilled;
	private int cloneModorKilled;
	Future<?> luckyReliquaryTask;
	protected boolean isInstanceDestroyed = false;
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 701795: // Lucky Danuar Reliquary Box.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053619, 1)); // Ancient Manastone Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); // Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052388, 1)); // Modor's Equipment Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053083, 1)); // Tempering Solution Chest.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053353, 1)); // Lucky Danuar Reliquary Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052951, 1)); // [Event] Prestige Supplies.
					}
					switch (Rnd.get(1, 2))
					{
						case 1:
						{
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053623, 1)); // Fire Dragon King's Weapon Bundle [Mythic].
							break;
						}
						case 2:
						{
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054244, 1)); // Dreaming Nether Water Dragon King's Weapon Chest [Mythic].
							break;
						}
					}
				}
				break;
			}
		}
	}
	
	/**
	 * Modor activated the Danuar Bomb of grudge.
	 */
	void startLuckyReliquaryTimer()
	{
		// Modor activated the Danuar Bomb of grudge. You have 15 minutes to defeat her.
		sendMsgByRace(1401676, Race.PC_ALL, 5000);
		sendMessage(1401677, 10 * 60 * 1000); // 10 minutes elapsed.
		sendMessage(1401678, 15 * 60 * 1000); // The bomb has detonated.
		instance.doOnAllPlayers(player ->
		{
			if (player.isOnline())
			{
				luckyReliquaryTask = ThreadPoolManager.getInstance().schedule(() ->
				{
					instance.doOnAllPlayers(player1 -> onExitInstance(player1));
					onInstanceDestroy();
				}, 900000); // 15 Minutes.
			}
		});
	}
	
	@Override
	public void onDie(Npc npc)
	{
		// final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 284380:
			case 284381:
			case 284382:
			case 284659:
			case 284660:
			case 284662:
			case 284663:
			case 284664:
			{
				despawnNpc(npc);
				break;
			}
			case 284377: // Danuar Reliquary Novun.
			case 284378: // Idean Lapilima.
			case 284379: // Idean Obscura.
			{
				ideanKilled++;
				if (ideanKilled == 1)
				{
				}
				else if (ideanKilled == 2)
				{
				}
				else if (ideanKilled == 3)
				{
					spawn(231304, 256.45197f, 257.91986f, 241.78688f, (byte) 90); // Cursed Queen's Modor.
					instance.doOnAllPlayers(player ->
					{
						if (player.isOnline())
						{
							startLuckyReliquaryTimer();
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 900)); // 15 Minutes.
						}
					});
				}
				despawnNpc(npc);
				break;
			}
			case 284383: // Clone's Modor.
			{
				cloneModorKilled++;
				if (cloneModorKilled == 1)
				{
				}
				else if (cloneModorKilled == 2)
				{
				}
				else if (cloneModorKilled == 3)
				{
				}
				else if (cloneModorKilled == 4)
				{
				}
				else if (cloneModorKilled == 5)
				{
					spawn(231305, 256.45197f, 257.91986f, 241.78688f, (byte) 90); // Enraged Queen's Modor.
				}
				despawnNpc(npc);
				break;
			}
			case 231305: // Enraged Queen's Modor.
			{
				luckyReliquaryTask.cancel(true);
				sendMsg("[Congratulation]: you finish <[Lucky] Danuar Reliquary>");
				spawn(730907, 256.45197f, 257.91986f, 241.78688f, (byte) 90); // Lucky Danuar Reliquary Exit.
				spawn(701795, 256.39725f, 255.52034f, 241.78006f, (byte) 90); // Lucky Danuar Reliquary Box.
				instance.doOnAllPlayers(player ->
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
					}
				});
				break;
			}
		}
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	protected void sendMsgByRace(int msg, Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player ->
		{
			if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
			}
		}), time);
	}
	
	private void sendMessage(int msgId, long delay)
	{
		if (delay == 0)
		{
			sendMsg(msgId);
		}
		else
		{
			ThreadPoolManager.getInstance().schedule(() -> sendMsg(msgId), delay);
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		isInstanceDestroyed = true;
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	@Override
	public void onExitInstance(Player player)
	{
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 255.30792f, 245.11589f, 241.87251f, (byte) 29);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}