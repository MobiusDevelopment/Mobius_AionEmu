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

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(300170000)
public class BeshmundirInstance extends GeneralInstanceHandler
{
	private int macunbelloSouls;
	private int warriorMonument;
	private WorldMapInstance instance;
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			/**
			 * PATH TO WATCHER'S NEXUS
			 */
			case 216238: // Captain Lakhara.
			{
				doors.get(470).setOpen(true);
				break;
			}
			case 216739: // Warrior Monument.
			{
				warriorMonument++;
				if (warriorMonument < 15)
				{
				}
				else if (warriorMonument == 15)
				{
					// Ahbana the Wicked has appeared in the Watcher's Nexus.
					sendMsgByRace(1400470, Race.PC_ALL, 5000);
					ThreadPoolManager.getInstance().schedule(new Runnable()
					{
						@Override
						public void run()
						{
							spawn(216239, 1356.9945f, 149.51117f, 246.27036f, (byte) 29); // Ahbana The Wicked.
						}
					}, 3000);
				}
				despawnNpc(npc);
				// The Warrior Monument has been destroyed. Ahbana the Wicked is on alert.
				sendMsgByRace(1400465, Race.PC_ALL, 0);
				break;
			}
			case 799342: // Traitor Kumbanda.
			{
				sendMovie(player, 447);
				break;
			}
			case 216239: // Ahbana The Wicked.
			{
				doors.get(471).setOpen(true);
				break;
			}
			/**
			 * PATH TO MACUNBELLO'S REFUGE
			 */
			case 216583: // Brutal Soulwatcher (1st Island)
			{
				spawn(799518, 933.982971f, 444.269104f, 222.00f, (byte) 21); // Plegeton Boatman II.
				break;
			}
			case 216584: // Brutal Soulwatcher (2nd Island)
			{
				spawn(799519, 788.744690f, 442.353271f, 222.00f, (byte) 0); // Plegeton Boatman III.
				break;
			}
			case 216585: // Brutal Soulwatcher (3th Island)
			{
				sendMovie(player, 445);
				doors.get(467).setOpen(true);
				spawn(799520, 818.57874f, 277.74527f, 220.19385f, (byte) 53); // Plegeton Boatman IV.
				break;
			}
			case 216206: // Elyos Spiritblade.
			case 216207: // Elyos Spiritmage.
			case 216208: // Elyos Spiritbow.
			case 216209: // Elyos Spiritsalve.
			case 216210: // Asmodian Soulsword.
			case 216211: // Asmodian Soulspell.
			case 216212: // Asmodian Soulranger.
			case 216213: // Asmodian Soulmedic.
			{
				macunbelloSouls++;
				if (macunbelloSouls == 12)
				{
					// Macunbello's power is weakening.
					sendMsgByRace(1400466, Race.PC_ALL, 2000);
				}
				else if (macunbelloSouls == 14)
				{
					// Macunbello's power has weakened.
					sendMsgByRace(1400467, Race.PC_ALL, 2000);
				}
				else if (macunbelloSouls == 21)
				{
					// Macunbello has been crippled.
					sendMsgByRace(1400468, Race.PC_ALL, 2000);
				}
				break;
			}
			/**
			 * PATH TO GARDEN OF THE ENTOMBED
			 */
			case 216246: // The Great Virhana.
			{
				doors.get(473).setOpen(true);
				break;
			}
			case 216250: // Dorakiki The Bold.
			{
				deleteNpc(281648); // Sorcerer Haskin.
				deleteNpc(281649); // Chopper.
				// Hiding Lupukin has appeared.
				sendMsgByRace(1400471, Race.PC_ALL, 2000);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(216527, 1164.5264f, 1210.8931f, 283.3387f, (byte) 107); // Lupukin.
					}
				}, 3000);
				break;
			}
			case 216248: // Taros Lifebane.
			{
				doors.get(533).setOpen(true);
				doors.get(535).setOpen(true);
				break;
			}
			/**
			 * PATH TO THE PRISON OF ICE
			 */
			case 216263: // Isbariya The Resolute.
			{
				sendMovie(player, 439);
				// The Seal Protector has fallen. The Rift Orb shines while the seal weakens.
				sendMsgByRace(1400480, Race.PC_ALL, 0);
				deleteNpc(281645); // Sacrificial Soul.
				final SpawnTemplate RiftOrb = SpawnEngine.addNewSingleTimeSpawn(300170000, 730275, 1611.1663f, 1604.7267f, 311.04984f, (byte) 0);
				RiftOrb.setEntityId(426);
				objects.put(730275, SpawnEngine.spawnObject(RiftOrb, instanceId));
				spawn(216264, 556.59375f, 1367.2274f, 224.79459f, (byte) 75); // Stormwing.
				break;
			}
			case 216264: // Stormwing.
			{
				deleteNpc(281794);
				deleteNpc(281795);
				deleteNpc(281796);
				deleteNpc(281797);
				deleteNpc(281798);
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(730287, 565.25275f, 1376.6252f, 224.79459f, (byte) 74); // Prison Of Ice Exit.
					}
				}, 3000);
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
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 1480.8746f, 255.21692f, 243.45709f, (byte) 0);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	private void sendMsg(String str)
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
	
	protected void sendMsgByRace(int msg, Race race, int time)
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
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		doors.clear();
	}
}