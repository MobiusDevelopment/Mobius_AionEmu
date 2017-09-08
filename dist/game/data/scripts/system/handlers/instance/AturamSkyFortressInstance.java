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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
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
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.zone.ZoneInstance;
import com.aionemu.gameserver.world.zone.ZoneName;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(300240000)
public class AturamSkyFortressInstance extends GeneralInstanceHandler
{
	private boolean msgIsSended;
	private int energyGenerators;
	private int balaurSpyCrystal;
	private int drakanChiefOfStaff;
	private int drakanPettyOfficer;
	private boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		doors.get(177).setOpen(true);
		doors.get(307).setOpen(false);
		doors.get(308).setOpen(false);
		final Npc npc = instance.getNpc(217371); // Weapon Hugen.
		if (npc != null)
		{
			npc.getEffectController().unsetAbnormal(AbnormalState.SLEEP.getId());
			SkillEngine.getInstance().getSkill(npc, 21571, 60, npc).useNoAnimationSkill();
		}
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		if (movies.contains(467))
		{
			return;
		}
		sendMovie(player, 467);
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 219185: // Aldreen [Kaichin Engineering League]
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 110900259, 1)); // Shulack Work Clothes.
				break;
			}
			case 217371: // Weapon Hugen.
			{
				switch (Rnd.get(1, 3))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051566, 1)); // Hugen's Pants Box.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051567, 1)); // Hugen's Pauldrons Box.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051573, 1)); // Hugen's Belt Box.
						break;
					}
				}
				break;
			}
			case 217373: // Popuchin.
			{
				switch (Rnd.get(1, 3))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051569, 1)); // Popuchin's Shoe Box.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051570, 1)); // Popuchin's Hairpin Box.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051572, 1)); // Popuchin's Ring Box.
						break;
					}
				}
				break;
			}
			case 217376: // Ashunatal Shadowslip.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); // Major Stigma Support Bundle.
				switch (Rnd.get(1, 3))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051565, 1)); // Ashunatal's Jacket Box.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051568, 1)); // Ashunatal's Glove Box.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188051571, 1)); // Ashunatal's Earrings Box.
						break;
					}
				}
				break;
			}
			case 701033: // Balaur Armament Box.
			{
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000110, 5)); // Fine Life Serum.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164000111, 5)); // Fine Mana Serum.
						break;
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		if (isInstanceDestroyed)
		{
			return;
		}
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 702654: // Dredgion Generator I.
			{
				// Power Generator No.1 has been destroyed. A Power Generator Protector appears.
				sendMsgByRace(1402734, Race.PC_ALL, 3000);
				ThreadPoolManager.getInstance().schedule(() -> spawn(282277, 572.8088f, 459.4094f, 647.93896f, (byte) 15), 3000);
				break;
			}
			case 702653: // Dredgion Generator II.
			{
				// Power Generator No.2 has been destroyed. A Power Generator Protector appears.
				sendMsgByRace(1402735, Race.PC_ALL, 3000);
				ThreadPoolManager.getInstance().schedule(() -> spawn(282280, 581.1f, 401.3544f, 648.6401f, (byte) 9), 3000);
				break;
			}
			case 702650: // Dredgion Generator III.
			{
				// Power Generator No.3 has been destroyed. A Power Generator Protector appears.
				sendMsgByRace(1402736, Race.PC_ALL, 3000);
				ThreadPoolManager.getInstance().schedule(() -> spawn(282281, 524.1896f, 489.7742f, 649.916f, (byte) 34), 3000);
				break;
			}
			case 702651: // Dredgion Generator IV.
			{
				// Power Generator No.4 has been destroyed. A Power Generator Protector appears.
				sendMsgByRace(1402737, Race.PC_ALL, 3000);
				ThreadPoolManager.getInstance().schedule(() -> spawn(282279, 467.7094f, 465.6622f, 647.93896f, (byte) 40), 3000);
				break;
			}
			case 702652: // Dredgion Generator V.
			{
				// Power Generator No. 5 has been destroyed. A Power Generator Protector appears.
				sendMsgByRace(1402738, Race.PC_ALL, 3000);
				doors.get(126).setOpen(true);
				ThreadPoolManager.getInstance().schedule(() -> spawn(282278, 449.5576f, 420.7812f, 652.9143f, (byte) 89), 3000);
				break;
			}
			case 217373: // Popuchin.
			{
				spawnActivatedBalaurTeleporter1();
				spawn(730375, 374.85f, 424.32f, 653.52f, (byte) 0); // Popukin's Treasure Box.
				break;
			}
			/*
			 * case 702656: //Balaur Spy Crystal. balaurSpyCrystal++; if (balaurSpyCrystal == 7) { //All Spy Crystals have been destroyed and the door to the Talon Laboratory has opened. sendMsgByRace(1402740, Race.PC_ALL, 0); doors.get(68).setOpen(true); } //The Spy Crystal has been destroyed.
			 * sendMsgByRace(1402739, Race.PC_ALL, 0); despawnNpc(npc); break;
			 */
			case 217343: // Talon Guardian.
			{
				removeEffects(player);
				doors.get(68).setOpen(true);
				// All Spy Crystals have been destroyed and the door to the Talon Laboratory has opened.
				sendMsgByRace(1402740, Race.PC_ALL, 0);
				break;
			}
			case 701043: // Energy Generator.
			{
				despawnNpc(npc);
				deleteNpc(701110); // Enhanced Barrier.
				// The Energy Generator has been destroyed and the Protective Shield has disappeared.
				sendMsgByRace(1400913, Race.PC_ALL, 0);
				// The Outer Protective Wall is gone, and Weapon H is waking from its dormant state.
				sendMsgByRace(1400909, Race.PC_ALL, 5000);
				break;
			}
			case 217371: // Weapon Hugen.
			{
				spawnShulackFlitter();
				spawnActivatedBalaurTeleporter2();
				spawn(730374, 815.397f, 288.390f, 602.764f, (byte) 91); // H-Core.
				break;
			}
			case 217370: // Drakan Petty Officer.
			{
				drakanPettyOfficer++;
				if (drakanPettyOfficer == 4)
				{
					doors.get(174).setOpen(true);
					doors.get(307).setOpen(true);
					doors.get(308).setOpen(true);
					startOfficerWalkerEvent();
				}
				else if (drakanPettyOfficer == 8)
				{
					doors.get(175).setOpen(true);
					startMarbataWalkerEvent();
				}
				despawnNpc(npc);
				break;
			}
			case 217656: // Drakan Chief Of Staff.
			{
				drakanChiefOfStaff++;
				if (drakanChiefOfStaff == 1)
				{
					startOfficerWalkerEvent();
				}
				else if (drakanChiefOfStaff == 2)
				{
					doors.get(178).setOpen(true);
				}
				despawnNpc(npc);
				break;
			}
			case 217382: // Commander Barus.
			{
				doors.get(230).setOpen(true);
				AbyssPointsService.addGp(player, 100);
				AbyssPointsService.addAp(player, 2000);
				// The door to Ashunatal's Ready Room is now open. You can see Ashunatal behind the door.
				sendMsgByRace(1401048, Race.PC_ALL, 2000);
				break;
			}
			case 218577: // Marabata Watchman.
			{
				despawnNpc(npc);
				spawn(217382, 258.3894f, 796.7554f, 901.6453f, (byte) 80); // Commander Barus.
				break;
			}
			case 701029: // Energy Generator.
			{
				final Npc weaponHugen = instance.getNpc(217371); // Weapon Hugen.
				energyGenerators++;
				if (weaponHugen != null)
				{
					if (energyGenerators == 1)
					{
						// The Energy Generator is becoming unstable.
						sendMsgByRace(1400910, Race.PC_ALL, 0);
					}
					else if (energyGenerators == 2)
					{
						// The Energy Generator has been destroyed and the power of the Protective Shield has been reduced.
						sendMsgByRace(1400911, Race.PC_ALL, 0);
					}
					else if (energyGenerators == 3)
					{
						// The Energy Generator has been destroyed and the power of the Protective Shield has been greatly reduced.
						sendMsgByRace(1400912, Race.PC_ALL, 0);
					}
					else if (energyGenerators == 4)
					{
						weaponHugen.getEffectController().removeEffect(21571);
						spawnBarrierControler();
					}
				}
				despawnNpc(npc);
				break;
			}
			case 217376: // Ashunatal Shadowslip.
			{
				AbyssPointsService.addAp(player, 2000);
				AbyssPointsService.addGp(player, 200);
				// There is a huge Surkana device here.
				// Since Ashunatal risked her life to protect it, you should destroy it and interfere with the Balaur's plans.
				sendMsgByRace(1401401, Race.PC_ALL, 2000);
				sendMsg("Congratulation]: you finish <Aturam Sky Fortress>");
				break;
			}
			case 217369: // Drakan Crewhand.
			case 217368: // Drakan Combatant.
			case 217655: // Veteran Escort Officer.
			{
				despawnNpc(npc);
				break;
			}
		}
	}
	
	private void spawnBarrierControler()
	{
		final SpawnTemplate barrierControler = SpawnEngine.addNewSingleTimeSpawn(300240000, 701043, 815.66711f, 249.08171f, 603.10529f, (byte) 0);
		barrierControler.setEntityId(18);
		objects.put(701043, SpawnEngine.spawnObject(barrierControler, instanceId));
	}
	
	private void spawnShulackFlitter()
	{
		final SpawnTemplate shulackFlitter = SpawnEngine.addNewSingleTimeSpawn(300240000, 730390, 637.00262f, 497.52673f, 658.33716f, (byte) 0);
		shulackFlitter.setEntityId(86);
		objects.put(730390, SpawnEngine.spawnObject(shulackFlitter, instanceId));
	}
	
	private void spawnActivatedBalaurTeleporter1()
	{
		final SpawnTemplate activatedBalaurTeleporter1 = SpawnEngine.addNewSingleTimeSpawn(300240000, 702664, 352.29132f, 424.08679f, 655.7467f, (byte) 0);
		activatedBalaurTeleporter1.setEntityId(297);
		objects.put(702664, SpawnEngine.spawnObject(activatedBalaurTeleporter1, instanceId));
	}
	
	private void spawnActivatedBalaurTeleporter2()
	{
		final SpawnTemplate activatedBalaurTeleporter2 = SpawnEngine.addNewSingleTimeSpawn(300240000, 730392, 814.94f, 303.36319f, 603.42773f, (byte) 0);
		activatedBalaurTeleporter2.setEntityId(97);
		objects.put(730392, SpawnEngine.spawnObject(activatedBalaurTeleporter2, instanceId));
	}
	
	private void rushWalk(Npc npc)
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			if (!isInstanceDestroyed)
			{
				for (Player player : instance.getPlayersInside())
				{
					npc.setTarget(player);
					((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
					npc.setState(1);
					npc.getMoveController().moveToTargetObject();
					PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
				}
			}
		}, 1000);
	}
	
	private void startMarbataWalkerEvent()
	{
		// Alarms rang in the Waiting Room. High-powered Drakan are heading your way!
		sendMsgByRace(1401050, Race.PC_ALL, 0);
		rushWalk((Npc) spawn(218577, 193.45583f, 802.1455f, 900.7575f, (byte) 103)); // Marabata Watchman.
		rushWalk((Npc) spawn(217655, 198.34431f, 801.4107f, 900.66125f, (byte) 110)); // Veteran Escort Officer.
		rushWalk((Npc) spawn(217655, 197.13315f, 798.7863f, 900.6499f, (byte) 110)); // Veteran Escort Officer.
	}
	
	private void startOfficerWalkerEvent()
	{
		// The door of the Aircrew Room is now open. Kill the Drakan!
		sendMsgByRace(1401049, Race.PC_ALL, 0);
		rushWalk((Npc) spawn(217655, 146.53816f, 713.5974f, 901.0108f, (byte) 111)); // Veteran Escort Officer.
		rushWalk((Npc) spawn(217655, 144.84991f, 720.9318f, 901.0604f, (byte) 96)); // Veteran Escort Officer.
		rushWalk((Npc) spawn(217655, 146.19899f, 709.60455f, 901.0078f, (byte) 110)); // Veteran Escort Officer.
		rushWalk((Npc) spawn(217656, 144.11845f, 716.8327f, 901.046f, (byte) 100)); // Drakan Chief Of Staff.
		rushWalk((Npc) spawn(217369, 144.96825f, 712.83344f, 901.0133f, (byte) 110)); // Drakan Crewhand.
		rushWalk((Npc) spawn(217369, 144.75804f, 718.4293f, 901.05493f, (byte) 80)); // Drakan Crewhand.
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(164000163, storage.getItemCountByItemId(164000163)); // Talon Summoning Device.
		storage.decreaseByItemId(164000202, storage.getItemCountByItemId(164000202)); // Bottomless Bucket.
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(19407); // Powerful Defense.
		effectController.removeEffect(19408); // Strong Defense.
		effectController.removeEffect(19520); // Overclock.
		effectController.removeEffect(21807); // Board Swift Runner.
		effectController.removeEffect(21808); // Board Swift Runner.
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
		removeEffects(player);
	}
	
	@Override
	public void onInstanceDestroy()
	{
		isInstanceDestroyed = true;
		movies.clear();
		doors.clear();
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(false, false, 0, 8));
		return true;
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 205494: // Hariken's Supply Chest.
			{
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				despawnNpc(npc);
				addHarikenItems(player);
				break;
			}
			case 731533: // Magma Tachysphere.
			{
				// You must board Swift Runner 1.
				sendMsgByRace(1402752, Race.PC_ALL, 0);
				SkillEngine.getInstance().getSkill(npc, 21807, 60, player).useNoAnimationSkill(); // Board Swift Runner.
				break;
			}
			case 731534: // Magma Tachysphere.
			{
				// You must board Swift Runner 2.
				sendMsgByRace(1402753, Race.PC_ALL, 0);
				SkillEngine.getInstance().getSkill(npc, 21808, 60, player).useNoAnimationSkill(); // Board Swift Runner.
				break;
			}
			case 730397: // Recharger.
			{
				despawnNpc(npc);
				// You feel more physically fit as the energy covers you.
				sendMsgByRace(1400926, Race.PC_ALL, 0);
				SkillEngine.getInstance().getSkill(npc, 19520, 51, player).useNoAnimationSkill(); // Overclock.
				break;
			}
			case 730398: // Flagon.
			{
				despawnNpc(npc);
				// You have recovered HP from the Shulack Drink.
				sendMsgByRace(1400927, Race.PC_ALL, 0);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 10000);
				player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, 10000);
				break;
			}
			case 730410: // Warehouse Latch.
			{
				doors.get(90).setOpen(true);
				// You see an unlabeled handle on the wall. Switch it if you dare.
				sendMsgByRace(1401027, Race.PC_ALL, 0);
				// You have been detected by the Monitoring Lamp! Enemies are coming!
				sendMsgByRace(1401028, Race.PC_ALL, 10000);
				break;
			}
		}
	}
	
	private void addHarikenItems(Player player)
	{
		ItemService.addItem(player, 164000163, 1); // Talon Summoning Device.
		ItemService.addItem(player, 164000202, 1); // Bottomless Bucket.
	}
	
	@Override
	public void onEnterZone(Player player, ZoneInstance zone)
	{
		if (zone.getAreaTemplate().getZoneName() == ZoneName.get("SKY_FORTRESS_WAREHOUSE_ZONE_300240000"))
		{
			if (!msgIsSended)
			{
				msgIsSended = true;
				// You see a large obelisk pulsing with energy. Go on. Take some.
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1401023));
			}
		}
		else if (zone.getAreaTemplate().getZoneName() == ZoneName.get("DREDGION_FABRICATION_ZONE_300240000"))
		{
			// You've heard the contents of a Flagon may help recover HP.
			sendMsgByRace(1401024, Race.PC_ALL, 2000);
			// Power Generator Protection System is in operation.
			// Shutting down the protection system requires destroying the Power Generators in the correct sequence.
			sendMsgByRace(1402751, Race.PC_ALL, 10000);
			// Destroy the Power Generators in the correct sequence to attack Popuchin.
			sendMsgByRace(1402755, Race.PC_ALL, 20000);
		}
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		doors.get(126).setOpen(true);
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 685.2202f, 462.81192f, 655.21655f, (byte) 60);
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
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
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
}