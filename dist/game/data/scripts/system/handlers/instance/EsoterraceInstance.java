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
import java.util.Map;
import java.util.Set;

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
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(300250000)
public class EsoterraceInstance extends GeneralInstanceHandler
{
	private int labManagerKilled;
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onInstanceCreate(instance);
		if (movies.contains(473))
		{
			return;
		}
		sendMovie(player, 473);
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 217206: // Warden Surama.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053789, 1)); // Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052951, 1)); // [Event] Prestige Supplies.
					}
				}
				break;
			}
			case 217185: // Dalia Charlands.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000111, 1)); // Dalia Key.
				break;
			}
			case 701025: // Esoterrace Sundries Box.
			{
				switch (Rnd.get(1, 6))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190000050, 1)); // Whitebeard Manduri Egg.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020089, 1)); // Blue Merek.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020148, 1)); // Infernal Diabol.
						break;
					}
					case 4:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190020204, 1)); // Cheering Dandi's.
						break;
					}
					case 5:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190070004, 1)); // Su-ro Kim Summoning Lamp.
						break;
					}
					case 6:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 190070012, 1)); // Pink Merek Egg.
						break;
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 282295: // Command Gate Control.
			{
				doors.get(39).setOpen(true);
				// A heavy door has opened somewhere.
				sendMsgByRace(1401839, Race.PC_ALL, 0);
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			/**
			 * From the main entrance, players have access to the larger half of the Esoterrace Secret Laboratory. In the middle of the Laboratory is a Surkana Feeder. Destroy this to face Warden Surama, the “Hard Mode” final Named Monster. Leave it alone to face Kexkra, the normal final Named
			 * Monster.
			 */
			case 282291: // Surkana Feeder.
			{
				despawnNpc(npc);
				deleteNpc(217204); // Kexkra.
				// The Surkana Supplier has overloaded.
				sendMsgByRace(1400996, Race.PC_ALL, 0);
				// The Surkana Supplier has been broken.
				sendMsgByRace(1401037, Race.PC_ALL, 4000);
				spawn(217205, 1315.99f, 1170.77f, 51.8004f, (byte) 87); // Kexkra Prototype.
				break;
			}
			/**
			 * Dalia Charlands is the first Named Monster of Esoterrace. Before engaging the boss, be sure to clear out the surrounding area of patrolling monsters. Dalia is a straightforward encounter that doesn't have a lot of gimmicks, just be sure to watch out for the occasional area of effect
			 * attack. When Dalia is defeated, Dalia's Watcher will appear, follow it to find the path to the next area. Be sure each player in the Group loots the Dalia Key from Dalia Charlands. Like a Quest item, each player can loot this key, and earn access to one of the Entwined Treasure Chests
			 * nearby which contain Abyss relics! In addition to the Entwined Treasure Chests, a Huge Entwined Chest will spawn beneath Dalia once it is defeated, which can be opened with the Swirl Key. Should the Group befall certain death after defeating Dalia, the Windstream found at the
			 * beginning of the Instanced Dungeon will now transport players directly to the Dalia Garden.
			 */
			case 217185: // Dalia Charlands.
			{
				// Dalia Charlands has vanished.
				sendMsgByRace(1401036, Race.PC_ALL, 0);
				// The Surkana Steam Jet has generated an updraft.
				sendMsgByRace(1400997, Race.PC_ALL, 5000);
				spawn(703052, 392.27563f, 543.89026f, 318.3265f, (byte) 18); // Windstream A
				spawn(703054, 392.27563f, 543.89026f, 318.3265f, (byte) 18); // Windstream B
				sendMsg("<Captain Murugan> appear");
				spawn(217195, 789.48f, 1115.29f, 363.131f, (byte) 89); // Captain Murugan.
				spawn(701023, 1264.862061f, 644.995178f, 296.831818f, (byte) 0, 112);
				break;
			}
			case 217282: // Esoterrace Investigator.
			case 217283: // Senior Lab Researcher.
			case 217284: // Lab Supervisor.
			{
				labManagerKilled++;
				if (labManagerKilled == 1)
				{
					doors.get(367).setOpen(false);
				}
				else if (labManagerKilled == 2)
				{
					doors.get(69).setOpen(false);
				}
				else if (labManagerKilled == 3)
				{
					doors.get(111).setOpen(true);
					// The door to the Laboratory Yard is now open.
					sendMsgByRace(1400920, Race.PC_ALL, 0);
				}
				break;
			}
			case 217281: // Lab Gatekeeper.
			{
				doors.get(70).setOpen(true);
				// The door to the Laboratory Air Conditioning Room is now open.
				sendMsgByRace(1400921, Race.PC_ALL, 0);
				break;
			}
			case 286930: // Esoterrace Mage.
			{
				despawnNpc(npc);
				spawn(799580, 1034.11f, 985.01f, 327.35095f, (byte) 105); // Keening Sirokin.
				spawn(701025, 1038.636963f, 987.741455f, 328.356415f, (byte) 0, 725); // Esoterrace Sundries Box.
				break;
			}
			/**
			 * Inside the Laboratory Air Conditioning Room, players will encounter the second Named Monster of Esoterrace, Captain Murugan. Be wary of Captain Murugan's deadly combo skills; expect the primary target to take massive damage throughout the encounter! When Captain Murugan is defeated,
			 * two doors will open in the Laboratory Air Conditioning Room, granting access to Chilled Treasure chests which contain Abyss relics.
			 */
			case 217195: // Captain Murugan.
			{
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						doors.get(45).setOpen(true);
						// With the gatekeeper down, the door on the left is open!
						sendMsgByRace(1401229, Race.PC_ALL, 0);
						break;
					}
					case 2:
					{
						doors.get(67).setOpen(true);
						// With the gatekeeper down, the door on the right is open!
						sendMsgByRace(1401230, Race.PC_ALL, 0);
						break;
					}
				}
				// The Laboratory Ventilator is now open.
				sendMsgByRace(1400922, Race.PC_ALL, 3000);
				// The Surkana Steam Jet has generated an updraft.
				sendMsgByRace(1400997, Race.PC_ALL, 6000);
				spawn(703056, 392.27563f, 543.89026f, 318.3265f, (byte) 18); // Windstream C
				spawn(703058, 392.27563f, 543.89026f, 318.3265f, (byte) 18); // Windstream D
				doors.get(52).setOpen(true);
				spawn(701024, 751.67f, 1136.08f, 365.031f, (byte) 105, 41); // Chilled Treasure.
				spawn(701024, 827.596f, 1136.16f, 365.031f, (byte) 73, 77); // Chilled Treasure.
				break;
			}
			case 282293: // Esoterrace Ventilator.
			{
				despawnNpc(npc);
				// The Drana Production Lab walkway is now open.
				sendMsgByRace(1400923, Race.PC_ALL, 0);
				break;
			}
			case 217289: // Esoterrace Biolab Watchman.
			{
				doors.get(122).setOpen(true);
				// The outer wall of the Bio Lab has collapsed.
				sendMsgByRace(1400924, Race.PC_ALL, 0);
				break;
			}
			/**
			 * When Kexkra is defeated, a treasure chest will spawn containing Abyss relics and Platinum Medals. In addition, the treasure chest has a chance to contain Fabled armor from the Surama set.
			 */
			case 217204: // Kexkra.
			{
				despawnNpc(npc);
				sendMsg("[Congratulation]: you finish <Esoterrace>");
				spawn(701044, 1341.19f, 1181.25f, 51.515f, (byte) 67); // Esoterrace Dimensional Rift Exit.
				spawn(701027, 1326.7705f, 1173.1145f, 51.493996f, (byte) 70, 726); // Laboratory Treasure Chest.
				spawn(701027, 1321.9897f, 1179.5394f, 51.493996f, (byte) 79, 727); // Laboratory Treasure Chest.
				break;
			}
			case 217205: // Kexkra Prototype.
			{
				despawnNpc(npc);
				sendMovie(player, 472);
				spawn(217206, 1315.99f, 1170.77f, 51.8004f, (byte) 87); // Warden Surama.
				spawn(701047, 1316.5045f, 1171.0127f, 52.589924f, (byte) 0, 180); // Flame Wall.
				break;
			}
			/**
			 * Players will start this encounter facing the Kexkra Prototype. As the encounter wears on, an event will cause Warden Surama to join the battle. When Warden Surama is defeated, two treasure chests will spawn, one of which has a chance to contain Fabled armor from the Surama series, and
			 * the other Fabled weapons from the Surama series.
			 */
			case 217206: // Warden Surama.
			{
				sendMsg("[Congratulation]: you finish <Esoterrace>");
				spawn(701044, 1341.19f, 1181.25f, 51.515f, (byte) 67); // Esoterrace Dimensional Rift Exit.
				spawn(701027, 1326.7705f, 1173.1145f, 51.493996f, (byte) 70, 726); // Laboratory Treasure Chest.
				spawn(701027, 1321.9897f, 1179.5394f, 51.493996f, (byte) 79, 727); // Laboratory Treasure Chest.
				break;
			}
		}
	}
	
	private void deleteNpc(int npcId)
	{
		if (getNpc(npcId) != null)
		{
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
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
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 370.20117f, 528.2504f, 324.73434f, (byte) 13);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
	
	@Override
	public void onEnterZone(Player player, ZoneInstance zone)
	{
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("DRANA_PRODUCTION_LAB_300250000"))
		{
			// Defeat all Drana Production Lab Section Managers to open the Laboratory Yard door.
			sendMsgByRace(1400919, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("ESOTERRACE_SECRET_LABORATORY_300250000"))
		{
			sendMsg("Use <Command Gate Control> to open <Esoterrace Secret Laboratory Door>");
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
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000111, storage.getItemCountByItemId(185000111)); // Dalia Key.
	}
	
	@Override
	public void onInstanceDestroy()
	{
		doors.clear();
		movies.clear();
	}
}