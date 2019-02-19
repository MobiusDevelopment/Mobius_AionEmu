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
package system.handlers.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
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

/**
 * @author Rinzler (Encom)
 */
@InstanceID(300160000)
public class LowerUdasTempleInstance extends GeneralInstanceHandler
{
	private boolean isStartTimer1 = false;
	private boolean isStartTimer2 = false;
	private boolean isStartTimer3 = false;
	private boolean isStartTimer4 = false;
	private boolean isStartTimer5 = false;
	private boolean isStartTimer6 = false;
	private boolean isStartTimer7 = false;
	private boolean isStartTimer8 = false;
	private boolean isStartTimer9 = false;
	private boolean isStartTimer10 = false;
	private boolean isStartTimer11 = false;
	private boolean isStartTimer12 = false;
	private Future<?> chestUdasTempleTask;
	private final List<Npc> udasTempleChest = new ArrayList<>();
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 702658: // Abbey Box.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053579, 1)); // [Event] Abbey Bundle.
				break;
			}
			case 702659: // Noble Abbey Box.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053580, 1)); // [Event] Noble Abbey Bundle.
				break;
			}
			case 215796: // Gradarim The Collector.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000087, 1)); // Jotun Vault Key.
				break;
			}
			case 215786: // Garha The Punisher.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000086, 1)); // Shadowy Prison Key.
				break;
			}
			case 215797: // Bergrisar.
			case 216149: // Udas Temple Treasure Box.
			case 216150: // Udas Temple Treasure Box.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188052306, 1)); // Udas Temple Contribution Bundle.
				break;
			}
			case 215783: // Nexus.
			case 215795: // Debilkarim The Maker.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053788, 1)); // Greater Stigma Support Bundlele.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052951, 1)); // [Event] Prestige Supplies.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 166500000, 1)); // Amplification Stone.
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 215795: // Debilkarim The Maker.
			{
				chestUdasTempleTask.cancel(true);
				sendMsg("[Congratulation]: you finish <Lower Udas Temple>");
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						spawn(702658, 575.1232f, 1295.7212f, 187.85898f, (byte) 113); // Abbey Box.
						break;
					}
					case 2:
					{
						spawn(702659, 575.1232f, 1295.7212f, 187.85898f, (byte) 113); // Noble Abbey Box.
						break;
					}
				}
				instance.doOnAllPlayers(player1 ->
				{
					if (player1.isOnline())
					{
						PacketSendUtility.sendPacket(player1, new SM_QUEST_ACTION(0, 0));
					}
				});
				break;
			}
		}
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onInstanceCreate(instance);
		if (!isStartTimer1)
		{
			isStartTimer1 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player1 ->
			{
				if (player1.isOnline())
				{
					PacketSendUtility.sendPacket(player1, new SM_QUEST_ACTION(0, 300));
				}
			});
			udasTempleChest.add((Npc) spawn(216149, 445.99957f, 1178.3578f, 193.02937f, (byte) 21));
			udasTempleChest.add((Npc) spawn(216149, 448.85532f, 1205.2148f, 191.59023f, (byte) 15));
			udasTempleChest.add((Npc) spawn(216149, 452.71637f, 1180.77f, 190.47333f, (byte) 85));
			udasTempleChest.add((Npc) spawn(216149, 440.6775f, 1198.4562f, 191.70049f, (byte) 50));
			udasTempleChest.add((Npc) spawn(216149, 449.19788f, 1197.8282f, 190.50172f, (byte) 24));
			udasTempleChest.add((Npc) spawn(216149, 436.17404f, 1185.6791f, 190.22073f, (byte) 13));
			udasTempleChest.add((Npc) spawn(216150, 442.38748f, 1186.572f, 190.88919f, (byte) 14));
			udasTempleChest.add((Npc) spawn(216150, 433.22824f, 1198.147f, 192.34004f, (byte) 0));
			udasTempleChest.add((Npc) spawn(216150, 462.2652f, 1180.8121f, 191.70518f, (byte) 85));
			udasTempleChest.add((Npc) spawn(216150, 455.50082f, 1176.3575f, 192.6768f, (byte) 34));
			udasTempleChest.add((Npc) spawn(216150, 436.63177f, 1192.1348f, 190.88254f, (byte) 119));
			udasTempleChest.add((Npc) spawn(216150, 438.38586f, 1202.9849f, 192.8323f, (byte) 105));
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer2();
				sendMsg(1400245);
				udasTempleChest.get(0).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer2()
	{
		if (!isStartTimer2)
		{
			isStartTimer2 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer3();
				sendMsg(1400245);
				udasTempleChest.get(1).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer3()
	{
		if (!isStartTimer3)
		{
			isStartTimer3 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer4();
				sendMsg(1400245);
				udasTempleChest.get(2).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer4()
	{
		if (!isStartTimer4)
		{
			isStartTimer4 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer5();
				sendMsg(1400245);
				udasTempleChest.get(3).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer5()
	{
		if (!isStartTimer5)
		{
			isStartTimer5 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer6();
				sendMsg(1400245);
				udasTempleChest.get(4).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer6()
	{
		if (!isStartTimer6)
		{
			isStartTimer6 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer7();
				sendMsg(1400245);
				udasTempleChest.get(5).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer7()
	{
		if (!isStartTimer7)
		{
			isStartTimer7 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer8();
				sendMsg(1400245);
				udasTempleChest.get(6).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer8()
	{
		if (!isStartTimer8)
		{
			isStartTimer8 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer9();
				sendMsg(1400245);
				udasTempleChest.get(7).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer9()
	{
		if (!isStartTimer9)
		{
			isStartTimer9 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer10();
				sendMsg(1400245);
				udasTempleChest.get(8).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer10()
	{
		if (!isStartTimer10)
		{
			isStartTimer10 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer11();
				sendMsg(1400245);
				udasTempleChest.get(9).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer11()
	{
		if (!isStartTimer11)
		{
			isStartTimer11 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				StartTimer12();
				sendMsg(1400245);
				udasTempleChest.get(10).getController().onDelete();
			}, 300000);
		}
	}
	
	private void StartTimer12()
	{
		if (!isStartTimer12)
		{
			isStartTimer12 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(player ->
			{
				if (player.isOnline())
				{
					PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
				}
			});
			chestUdasTempleTask = ThreadPoolManager.getInstance().schedule(() ->
			{
				sendMsg(1400244);
				sendMsg(1400245);
				udasTempleChest.get(11).getController().onDelete();
			}, 300000);
		}
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000086, storage.getItemCountByItemId(185000086)); // Jotun Vault Key.
		storage.decreaseByItemId(185000087, storage.getItemCountByItemId(185000087)); // Shadowy Prison Key.
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 1145.000f, 283.000f, 115.53778f, (byte) 0);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}