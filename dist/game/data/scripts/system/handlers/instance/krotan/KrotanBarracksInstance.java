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
package system.handlers.instance.krotan;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
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
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@InstanceID(301300000)
public class KrotanBarracksInstance extends GeneralInstanceHandler
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
	private Map<Integer, StaticDoor> doors;
	private Future<?> chestKrotanBarracksTask;
	private final List<Npc> krotanBarracksChest = new ArrayList<>();
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 233627: // Treasurer Wasukani.
			case 233628: // Treasurer Baruna.
			case 233629: // Treasurer Matazawa.
			case 233630: // Treasurer Hittite.
				switch (Rnd.get(1, 3))
				{
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000056, 1)); // Krotan Armory Key.
						break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000057, 1)); // Krotan Supply Base Key.
						break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000058, 1)); // Krotan Operations Room Key.
						break;
				}
				break;
			case 233632: // Weakened Krotan Lord.
			case 233633: // Enraged Krotan Lord.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000060, 1)); // Krotan Gold Room Key.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053619, 1)); // Ancient Manastone Bundle.
					}
					switch (Rnd.get(1, 2))
					{
						case 1:
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053623, 1)); // Fire Dragon King's Weapon Bundle [Mythic].
							break;
						case 2:
							dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054244, 1)); // Dreaming Nether Water Dragon King's Weapon Chest [Mythic].
							break;
					}
				}
				break;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		switch (Rnd.get(1, 4))
		{
			case 1:
				spawn(233627, 527.769f, 212.12146f, 178.46744f, (byte) 90); // Treasurer Wasukani.
				break;
			case 2:
				spawn(233628, 527.769f, 212.12146f, 178.46744f, (byte) 90); // Treasurer Baruna.
				break;
			case 3:
				spawn(233629, 527.769f, 212.12146f, 178.46744f, (byte) 90); // Treasurer Matazawa.
				break;
			case 4:
				spawn(233630, 527.769f, 212.12146f, 178.46744f, (byte) 90); // Treasurer Hittite.
				break;
		}
		switch (Rnd.get(1, 2))
		{
			case 1:
				spawn(233632, 526.6656f, 845.7792f, 199.44875f, (byte) 90); // Weakened Krotan Lord.
				break;
			case 2:
				spawn(233633, 526.6656f, 845.7792f, 199.44875f, (byte) 90); // Enraged Krotan Lord.
				break;
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 237288: // Ereshkigal Luksha Keeper.
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						deleteNpc(731580);
					}
				}, 5000);
				break;
			case 237290: // Ereshkigal Luksha Cryoslinger.
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						deleteNpc(700545);
					}
				}, 5000);
				break;
			case 237291: // Ereshkigal Luksha Shiverblade.
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						deleteNpc(700546);
					}
				}, 5000);
				break;
			case 233631: // Ebonlord Arknamium.
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 5000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						deleteNpc(700547);
					}
				}, 5000);
				break;
			case 233632: // Weakened Krotan Lord.
			case 233633: // Enraged Krotan Lord.
				doors.get(11).setOpen(true);
				doors.get(15).setOpen(true);
				doors.get(17).setOpen(true);
				doors.get(18).setOpen(true);
				doors.get(19).setOpen(true);
				doors.get(20).setOpen(true);
				doors.get(28).setOpen(true);
				doors.get(74).setOpen(true);
				doors.get(76).setOpen(true);
				doors.get(79).setOpen(true);
				doors.get(80).setOpen(true);
				chestKrotanBarracksTask.cancel(true);
				sendMsg("[Congratulation]: you finish <Krotan Barracks>");
				instance.doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						if (player.isOnline())
						{
							PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 0));
						}
					}
				});
				break;
		}
	}
	
	@Override
	public void onEnterInstance(final Player player)
	{
		super.onInstanceCreate(instance);
		if (!isStartTimer1)
		{
			isStartTimer1 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			krotanBarracksChest.add((Npc) spawn(702288, 478.56662f, 815.6565f, 199.76048f, (byte) 70));
			krotanBarracksChest.add((Npc) spawn(702288, 471.32745f, 834.5498f, 199.76048f, (byte) 63));
			krotanBarracksChest.add((Npc) spawn(702288, 470.52844f, 854.9471f, 199.76048f, (byte) 56));
			krotanBarracksChest.add((Npc) spawn(702289, 477.76843f, 873.94354f, 199.76036f, (byte) 50));
			krotanBarracksChest.add((Npc) spawn(702289, 490.90323f, 889.6053f, 199.76036f, (byte) 43));
			krotanBarracksChest.add((Npc) spawn(702289, 508.64328f, 899.91547f, 199.76036f, (byte) 36));
			krotanBarracksChest.add((Npc) spawn(702290, 528.42053f, 903.5909f, 199.76036f, (byte) 29));
			krotanBarracksChest.add((Npc) spawn(702290, 548.2363f, 900.31604f, 199.76036f, (byte) 23));
			krotanBarracksChest.add((Npc) spawn(702290, 565.53644f, 890.173f, 199.76036f, (byte) 16));
			krotanBarracksChest.add((Npc) spawn(702291, 578.9111f, 874.7958f, 199.76036f, (byte) 9));
			krotanBarracksChest.add((Npc) spawn(702291, 585.83545f, 855.7736f, 199.76036f, (byte) 3));
			krotanBarracksChest.add((Npc) spawn(702291, 586.7527f, 835.4556f, 199.76036f, (byte) 116));
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer2();
					sendMsg(1400245);
					krotanBarracksChest.get(0).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer2()
	{
		if (!isStartTimer2)
		{
			isStartTimer2 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer3();
					sendMsg(1400245);
					krotanBarracksChest.get(1).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer3()
	{
		if (!isStartTimer3)
		{
			isStartTimer3 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer4();
					sendMsg(1400245);
					krotanBarracksChest.get(2).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer4()
	{
		if (!isStartTimer4)
		{
			isStartTimer4 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer5();
					sendMsg(1400245);
					krotanBarracksChest.get(3).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer5()
	{
		if (!isStartTimer5)
		{
			isStartTimer5 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer6();
					sendMsg(1400245);
					krotanBarracksChest.get(4).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer6()
	{
		if (!isStartTimer6)
		{
			isStartTimer6 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer7();
					sendMsg(1400245);
					krotanBarracksChest.get(5).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer7()
	{
		if (!isStartTimer7)
		{
			isStartTimer7 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer8();
					sendMsg(1400245);
					krotanBarracksChest.get(6).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer8()
	{
		if (!isStartTimer8)
		{
			isStartTimer8 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer9();
					sendMsg(1400245);
					krotanBarracksChest.get(7).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer9()
	{
		if (!isStartTimer9)
		{
			isStartTimer9 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer10();
					sendMsg(1400245);
					krotanBarracksChest.get(8).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer10()
	{
		if (!isStartTimer10)
		{
			isStartTimer10 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer11();
					sendMsg(1400245);
					krotanBarracksChest.get(9).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer11()
	{
		if (!isStartTimer11)
		{
			isStartTimer11 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					StartTimer12();
					sendMsg(1400245);
					krotanBarracksChest.get(10).getController().onDelete();
				}
			}, 300000);
		}
	}
	
	private void StartTimer12()
	{
		if (!isStartTimer12)
		{
			isStartTimer12 = true;
			System.currentTimeMillis();
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					if (player.isOnline())
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(0, 300));
					}
				}
			});
			chestKrotanBarracksTask = ThreadPoolManager.getInstance().schedule(new Runnable()
			{
				@Override
				public void run()
				{
					sendMsg(1400244);
					sendMsg(1400245);
					krotanBarracksChest.get(11).getController().onDelete();
				}
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
		storage.decreaseByItemId(185000056, storage.getItemCountByItemId(185000056));
		storage.decreaseByItemId(185000057, storage.getItemCountByItemId(185000057));
		storage.decreaseByItemId(185000058, storage.getItemCountByItemId(185000058));
		storage.decreaseByItemId(185000059, storage.getItemCountByItemId(185000059));
		storage.decreaseByItemId(185000060, storage.getItemCountByItemId(185000060));
	}
	
	private void sendMsg(final String str)
	{
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendMessage(player, str);
			}
		});
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				instance.doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
						{
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	private void deleteNpc(int npcId)
	{
		if (getNpc(npcId) != null)
		{
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 527.334f, 122.056f, 175.954f, (byte) 0);
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}