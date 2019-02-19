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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.cache.HTMLCache;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastMap;

/**
 * @author xTz
 */
@InstanceID(300190000)
public class TalocsHollowInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(49).setOpen(true);
		spawnHugeInsectEgg();
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		switch (player.getRace())
		{
			case ELYOS:
			{
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				addTalocFruitE(player);
				addTalocTearsE(player);
				sendMovie(player, 434);
				break;
			}
			case ASMODIANS:
			{
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				addTalocFruitA(player);
				addTalocTearsA(player);
				sendMovie(player, 438);
				break;
			}
		}
		// You must destroy the enemies of Taloc. It allows you to acquire objects with great power.
		sendMsgByRace(1400704, Race.PC_ALL, 5000);
		// An object of great power waits in your cube. Transform into a mighty being with Taloc's Fruit.
		sendMsgByRace(1400752, Race.PC_ALL, 10000);
		// An object of great power waits in your cube. Launch a powerful aerial attack with Taloc's Tears.
		sendMsgByRace(1400753, Race.PC_ALL, 15000);
		// An object of great power waits in Shishir's carcass. Obtain it, then register it in the skill window.
		sendMsgByRace(1400754, Race.PC_ALL, 60000);
		// An object of great power waits in Gellmar's carcass. Obtain it, then register it in the skill window.
		sendMsgByRace(1400755, Race.PC_ALL, 80000);
		// An object of great power waits in Neith's carcass. Obtain it, then register it in the skill window.
		sendMsgByRace(1400756, Race.PC_ALL, 100000);
		HTMLService.showHTML(player, HTMLCache.getInstance().getHTML("instances/talocHollow.xhtml"));
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 215456: // Shishir.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000088, 1)); // Shishir's Corrosive Fluid.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000137, 1)); // Shishir's Powerstone.
				break;
			}
			case 215478: // Neith.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000108, 1)); // Dorkin's Pocket Knife.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000139, 1)); // Neith's Sleepstone.
				break;
			}
			case 215482: // Gellmar.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000138, 1)); // Gellmar's Wardstone.
				break;
			}
		}
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(182215618, storage.getItemCountByItemId(182215618)); // Taloc Fruit.
		storage.decreaseByItemId(182215593, storage.getItemCountByItemId(182215593)); // Taloc Fruit.
		storage.decreaseByItemId(182215619, storage.getItemCountByItemId(182215619)); // Taloc's Tears.
		storage.decreaseByItemId(182215592, storage.getItemCountByItemId(182215592)); // Taloc's Tears.
		storage.decreaseByItemId(164000137, storage.getItemCountByItemId(164000137)); // Shishir's Powerstone.
		storage.decreaseByItemId(164000138, storage.getItemCountByItemId(164000138)); // Gellmar's Wardstone.
		storage.decreaseByItemId(164000139, storage.getItemCountByItemId(164000139)); // Neith's Sleepstone.
	}
	
	private void addTalocFruitE(Player player)
	{
		ItemService.addItem(player, 182215618, 1); // Taloc Fruit.
	}
	
	private void addTalocTearsE(Player player)
	{
		ItemService.addItem(player, 182215619, 1); // Taloc's Tears.
	}
	
	private void addTalocFruitA(Player player)
	{
		ItemService.addItem(player, 182215593, 1); // Taloc Fruit.
	}
	
	private void addTalocTearsA(Player player)
	{
		ItemService.addItem(player, 182215592, 1); // Taloc's Tears.
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 700940: // Healing Plant.
			{
				despawnNpc(npc);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 20000);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, 20000);
				break;
			}
			case 700941: // Huge Healing Plant.
			{
				despawnNpc(npc);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 30000);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, 30000);
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		if (player == null)
		{
			return;
		}
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 215457: // Ancient Octanus.
			{
				// You sense a movement in Taloc's Roots. You won't be able to meet him unless you hurry.
				sendMsgByRace(1400659, Race.PC_ALL, 0);
				break;
			}
			case 215480: // Queen Mosqua.
			{
				deleteNpc(700738); // Huge Insect Egg.
				sendMovie(player, 435);
				spawnCrackedHugeInsectEgg();
				sendMsg("<Celestius> appear in <Mosqua Nest>");
				spawn(215488, 543.03f, 834.08f, 1377.29f, (byte) 00); // Celestius.
				break;
			}
			case 215488: // Celestius.
			{
				deleteNpc(700740); // Contaminated Fragment Of Aion Tower.
				sendMovie(player, 437);
				spawnPurifiedFragmentOfAionTower();
				ItemService.addItem(player, 188900011, 1); // Blessing Box Of Growth V.
				ItemService.addItem(player, 170170044, 1); // [Souvenir] Taloc's Komad Statue.
				sendMsg("[Congratulation]: you finish <Taloc's Hollow>");
				spawn(799503, 539.94135f, 813.3849f, 1377.4283f, (byte) 27); // Taloc's Mirage.
				break;
			}
			case 700739: // Cracked Huge Insect Egg.
			{
				ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnGeyser(), 5000);
				despawnNpc(npc);
				// Cracks appear on the surface of Queen Mosqua's egg.
				sendMsgByRace(1400476, Race.PC_ALL, 0);
				// An ascending air current is rising from the spot where the egg was.
				// You can fly vertically up by spreading your wings and riding the current.
				sendMsgByRace(1400477, Race.PC_ALL, 5000);
				break;
			}
			case 700942: // Bug Fluid.
			{
				despawnNpc(npc);
				break;
			}
		}
	}
	
	private void spawnHugeInsectEgg()
	{
		final SpawnTemplate IDElim2FEntity = SpawnEngine.addNewSingleTimeSpawn(300190000, 700738, 653.63f, 838.66998f, 1304.72f, (byte) 0);
		IDElim2FEntity.setEntityId(90);
		objects.put(700738, SpawnEngine.spawnObject(IDElim2FEntity, instanceId));
	}
	
	private void spawnCrackedHugeInsectEgg()
	{
		final SpawnTemplate IDElim3FEntity = SpawnEngine.addNewSingleTimeSpawn(300190000, 700739, 653.63f, 838.66998f, 1304.72f, (byte) 0);
		IDElim3FEntity.setEntityId(11);
		objects.put(700739, SpawnEngine.spawnObject(IDElim3FEntity, instanceId));
	}
	
	private void spawnPurifiedFragmentOfAionTower()
	{
		final SpawnTemplate AIONPieceCle = SpawnEngine.addNewSingleTimeSpawn(300190000, 700741, 636.35999f, 769.53003f, 1387.38f, (byte) 0);
		AIONPieceCle.setEntityId(92);
		objects.put(700741, SpawnEngine.spawnObject(AIONPieceCle, instanceId));
	}
	
	void spawnGeyser()
	{
		final SpawnTemplate IDElimWindBox = SpawnEngine.addNewSingleTimeSpawn(300190000, 281817, 653.77478f, 838.88306f, 1303.8502f, (byte) 0);
		IDElimWindBox.setEntityId(1308);
		objects.put(281817, SpawnEngine.spawnObject(IDElimWindBox, instanceId));
	}
	
	@Override
	public void onEnterZone(Player player, ZoneInstance zone)
	{
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("KINQUIDS_DEN_300190000"))
		{
			sendMovie(player, 463);
			// Smoke is being discharged. Exposure to smoke will destroy Kinquid's Barrier.
			sendMsgByRace(1400660, Race.PC_ALL, 0);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("MOSQUAS_NEST_300190000"))
		{
			sendMovie(player, 464);
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("COCCOONING_CHAMBER_300190000"))
		{
			// The cocoons are wriggling--something's inside!
			sendMsgByRace(1400475, Race.PC_ALL, 2000);
			// You can save one of the two Reians imprisoned in the cocoon.
			sendMsgByRace(1400630, Race.PC_ALL, 8000);
		}
	}
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	private void deleteNpc(int npcId)
	{
		if (getNpc(npcId) != null)
		{
			getNpc(npcId).getController().onDelete();
		}
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	protected void sendMsgByRace(int msg, Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () -> instance.doOnAllPlayers(player ->
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 199.87788f, 212.72118f, 1098.889f, (byte) 0);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
	
	@Override
	public void onInstanceDestroy()
	{
		movies.clear();
	}
}