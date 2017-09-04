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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.ShugoEmperorVaultReward;
import com.aionemu.gameserver.model.instance.playerreward.ShugoEmperorVaultPlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;
import javolution.util.FastMap;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@InstanceID(301590000)
public class EmperorTrillirunerkSafeInstance extends GeneralInstanceHandler
{
	private int rank;
	private Race spawnRace;
	private long instanceTime;
	@SuppressWarnings("unused")
	private boolean isInstanceDestroyed;
	Map<Integer, StaticDoor> doors;
	ShugoEmperorVaultReward instanceReward;
	private final FastList<Future<?>> vaultTask = FastList.newInstance();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	protected ShugoEmperorVaultPlayerReward getPlayerReward(Integer object)
	{
		return (ShugoEmperorVaultPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player)
	{
		instanceReward.addPlayerReward(new ShugoEmperorVaultPlayerReward(player.getObjectId()));
	}
	
	@SuppressWarnings("unused")
	private boolean containPlayer(Integer object)
	{
		return instanceReward.containPlayer(object);
	}
	
	@Override
	public InstanceReward<?> getInstanceReward()
	{
		return instanceReward;
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 235643: // Indirunerk Jonakak's Supply Box.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002079, 2)); // Shugo Warrior Secret Remedy.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002080, 2)); // High-Quality Shugo Warrior Secret Remedy.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002081, 2)); // Shugo Monk Secret Remedy.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002082, 2)); // High-Quality Shugo Monk Secret Remedy.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002083, 2)); // Shugo Warrior Secret Remedy.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002084, 2)); // High-Quality Shugo Warrior Secret Remedy.
					}
				}
				break;
			}
			case 832929: // Emperor's Treasure Box.
			case 832930: // Emperor's Quality Treasure Box.
			{
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054631, 1)); // Middle Grade Reward Bundle.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054632, 1)); // Low Grade Reward Bundle.
						break;
					}
				}
				break;
			}
			case 832931: // Emperor's Premium Treasure Box.
			{
				switch (Rnd.get(1, 4))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054629, 1)); // Highest Grade Reward Bundle.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054630, 1)); // High Grade Reward Bundle.
						break;
					}
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054631, 1)); // Middle Grade Reward Bundle.
						break;
					}
					case 4:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054632, 1)); // Low Grade Reward Bundle.
						break;
					}
				}
				break;
			}
		}
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000222, storage.getItemCountByItemId(185000222)); // Rusted Vault Key.
		storage.decreaseByItemId(162002031, storage.getItemCountByItemId(162002079)); // Shugo Warrior Secret Remedy.
		storage.decreaseByItemId(162002032, storage.getItemCountByItemId(162002080)); // High-Quality Shugo Warrior Secret Remedy.
		storage.decreaseByItemId(162002033, storage.getItemCountByItemId(162002081)); // Shugo Monk Secret Remedy.
		storage.decreaseByItemId(162002034, storage.getItemCountByItemId(162002082)); // High-Quality Shugo Monk Secret Remedy.
		storage.decreaseByItemId(162002035, storage.getItemCountByItemId(162002083)); // Shugo Warrior Secret Remedy.
		storage.decreaseByItemId(162002036, storage.getItemCountByItemId(162002084)); // High-Quality Shugo Warrior Secret Remedy.
	}
	
	private void SpawnRaceInstance()
	{
		final int templarerk2 = spawnRace == Race.ASMODIANS ? 833494 : 833491; // Brave Templarerk's Soul.
		final int gladiatorerk2 = spawnRace == Race.ASMODIANS ? 833495 : 833492; // Furious Gladiatorerk's Soul.
		final int sorcererk2 = spawnRace == Race.ASMODIANS ? 833496 : 833493; // Roiling Sorcererk's Soul.
		spawn(templarerk2, 541.1751f, 302.90582f, 400.49493f, (byte) 76);
		spawn(templarerk2, 465.46735f, 638.66113f, 395.375f, (byte) 100);
		spawn(templarerk2, 420.09814f, 688.6983f, 398.42203f, (byte) 14);
		spawn(gladiatorerk2, 543.03845f, 302.22098f, 400.48618f, (byte) 89);
		spawn(gladiatorerk2, 467.3807f, 640.5601f, 395.41f, (byte) 112);
		spawn(gladiatorerk2, 417.19376f, 691.69653f, 398.42203f, (byte) 14);
		spawn(sorcererk2, 544.87866f, 302.53723f, 400.55246f, (byte) 98);
		spawn(sorcererk2, 467.43195f, 643.59753f, 395.5f, (byte) 6);
		spawn(sorcererk2, 414.0031f, 694.8936f, 398.42203f, (byte) 14);
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 235629: // Intruder Skirmisher.
			case 235630: // Intruder Scout.
			{
				points = 180;
				despawnNpc(npc);
				break;
			}
			case 235631: // Brainwashed Peon.
			{
				points = 160;
				despawnNpc(npc);
				break;
			}
			case 235633: // Intruder Marksman.
			{
				points = 1070;
				despawnNpc(npc);
				break;
			}
			case 235634: // Watchman Hokuruki.
			{
				points = 2040;
				despawnNpc(npc);
				// Use the open entrance to move to the next area.
				sendMsgByRace(1402781, Race.PC_ALL, 0);
				final SpawnTemplate OpenedVaultDoor = SpawnEngine.addNewSingleTimeSpawn(301590000, 832924, 469.53888f, 657.56543f, 396.91852f, (byte) 0);
				OpenedVaultDoor.setEntityId(432);
				objects.put(832924, SpawnEngine.spawnObject(OpenedVaultDoor, instanceId));
				spawn(235643, 486.0f, 638.0f, 395.875f, (byte) 108); // Indirunerk Jonakak's Supply Box.
				break;
			}
			case 235635: // Intruder Challenger.
			case 235650: // Intruder Assassin.
			{
				points = 700;
				despawnNpc(npc);
				break;
			}
			case 235637: // Intruder Guard.
			{
				points = 820;
				despawnNpc(npc);
				break;
			}
			case 235640: // Captain Mirez.
			{
				points = 12000;
				despawnNpc(npc);
				// Gradi's second officer has appeared! Prepare for Longknife Zodica!
				sendMsgByRace(1402679, Race.PC_ALL, 0);
				// The Second Henchman of Gradi appears!
				sendMsgByRace(1402885, Race.PC_ALL, 2000);
				spawn(235685, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // Longknife Zodica.
				break;
			}
			case 235641: // Shugo Turncoat.
			{
				points = 660;
				despawnNpc(npc);
				break;
			}
			case 235647: // Grand Commander Gradi.
			{
				points = 400000;
				despawnNpc(npc);
				// All the intruders have fled. You've cleared the Vault!
				sendMsgByRace(1402681, Race.PC_ALL, 2000);
				spawn(832932, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // The Shugo Emperor's Butler.
				ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 3000);
				break;
			}
			case 235649: // Intruder Sniper.
			{
				points = 760;
				despawnNpc(npc);
				break;
			}
			case 235651: // Intruder Gladiator.
			{
				points = 1400;
				despawnNpc(npc);
				break;
			}
			case 235652: // Intruder Warrior.
			case 235653: // Intruder Sharpeye.
			{
				points = 250;
				despawnNpc(npc);
				break;
			}
			case 235660: // Ruthless Jabaraki.
			{
				points = 1740;
				despawnNpc(npc);
				// Gradi's first officer has appeared! Prepare for Captain Mirez!
				sendMsgByRace(1402678, Race.PC_ALL, 0);
				// The First Henchman of Gradi appears!
				sendMsgByRace(1402884, Race.PC_ALL, 2000);
				spawn(235640, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // Captain Mirez.
				break;
			}
			case 235680: // Intruder Brawler.
			case 235681: // Intruder Lookout.
			{
				points = 530;
				despawnNpc(npc);
				break;
			}
			case 235683: // Elite Captain Rupasha.
			{
				points = 272000;
				despawnNpc(npc);
				// Greedy Gradi, the intruder commander, has appeared. Get ready for a fight!
				sendMsgByRace(1402743, Race.PC_ALL, 0);
				// The Fifth Henchman of Gradi appears!
				sendMsgByRace(1402888, Race.PC_ALL, 2000);
				spawn(235647, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // Grand Commander Gradi.
				break;
			}
			case 235684: // Sorcerer Budyn.
			{
				points = 48000;
				despawnNpc(npc);
				// Gradi's final officer has appeared! Prepare for Elite Captain Rupasha!
				sendMsgByRace(1402742, Race.PC_ALL, 0);
				// The Fourth Henchman of Gradi appears!
				sendMsgByRace(1402887, Race.PC_ALL, 2000);
				spawn(235683, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // Elite Captain Rupasha.
				break;
			}
			case 235685: // Longknife Zodica.
			{
				points = 14400;
				despawnNpc(npc);
				// Gradi's third officer has appeared! Prepare for Sorcerer Budyn!
				sendMsgByRace(1402680, Race.PC_ALL, 0);
				// The Third Henchman of Gradi appears!
				sendMsgByRace(1402886, Race.PC_ALL, 2000);
				spawn(235684, 360.03033f, 757.95233f, 398.42203f, (byte) 104); // Sorcerer Budyn.
				break;
			}
		}
		if (instanceReward.getInstanceScoreType().isStartProgress())
		{
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21829);
		effectController.removeEffect(21830);
		effectController.removeEffect(21831);
		effectController.removeEffect(21832);
		effectController.removeEffect(21833);
		effectController.removeEffect(21834);
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
		removeEffects(player);
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
		removeEffects(player);
	}
	
	int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000)
		{
			return (int) (60000 - result);
		}
		else if (result < 600000)
		{ // 10 Minutes.
			return (int) (600000 - (result - 60000));
		}
		return 0;
	}
	
	void sendPacket(int nameId, int point)
	{
		instance.doOnAllPlayers(player ->
		{
			if (nameId != 0)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId((nameId * 2) + 1), point));
			}
			PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
		});
	}
	
	private int checkRank(int totalPoints)
	{
		if (totalPoints > 878600)
		{ // Rank S.
			rank = 1;
		}
		else if (totalPoints > 463800)
		{ // Rank A.
			rank = 2;
		}
		else if (totalPoints > 165100)
		{ // Rank B.
			rank = 3;
		}
		else if (totalPoints > 54000)
		{ // Rank C.
			rank = 4;
		}
		else if (totalPoints > 180)
		{ // Rank D.
			rank = 5;
		}
		else if (totalPoints >= 0)
		{ // Rank F.
			rank = 8;
		}
		else
		{
			rank = 8;
		}
		return rank;
	}
	
	protected void startInstanceTask()
	{
		instanceTime = System.currentTimeMillis();
		vaultTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			doors.get(430).setOpen(true);
			// The member recruitment window has passed. You cannot recruit any more members.
			sendMsgByRace(1401181, Race.PC_ALL, 0);
			instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
			sendPacket(0, 0);
		}, 60000));
		vaultTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			instance.doOnAllPlayers(player -> stopInstance(player));
			spawn(832950, 362.71112f, 760.5198f, 398.42203f, (byte) 104); // The Shugo Emperor's Exit.
		}, 600000));
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!instanceReward.containPlayer(player.getObjectId()))
		{
			addPlayerReward(player);
		}
		final ShugoEmperorVaultPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded())
		{
			doReward(player);
		}
		if (spawnRace == null)
		{
			spawnRace = player.getRace();
			SpawnRaceInstance();
		}
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player)
	{
		stopInstanceTask();
		instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[Congratulation]: you finish <Emperor Trillirunerk's Safe 4.9.1>");
		sendPacket(0, 0);
	}
	
	// private void rewardGroup()
	// {
	// for (Player p : instance.getPlayersInside())
	// {
	// doReward(p);
	// }
	// }
	
	@Override
	public void doReward(Player player)
	{
		final ShugoEmperorVaultPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded())
		{
			playerReward.setRewarded();
			final int vaultRank = instanceReward.getRank();
			switch (vaultRank)
			{
				case 1: // Rank S
				{
					playerReward.setRustedVaultKey(6);
					ItemService.addItem(player, 185000222, 6); // Rusted Vault Key.
					break;
				}
				case 2: // Rank A
				{
					playerReward.setRustedVaultKey(4);
					ItemService.addItem(player, 185000222, 4); // Rusted Vault Key.
					break;
				}
				case 3: // Rank B
				{
					playerReward.setRustedVaultKey(3);
					ItemService.addItem(player, 185000222, 3); // Rusted Vault Key.
					break;
				}
				case 4: // Rank C
				{
					playerReward.setRustedVaultKey(2);
					ItemService.addItem(player, 185000222, 2); // Rusted Vault Key.
					break;
				}
				case 5: // Rank D
				{
					break;
				}
				case 6: // Rank F
				{
					break;
				}
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		instanceReward = new ShugoEmperorVaultReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = vaultTask.head(), end = vaultTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		stopInstanceTask();
		isInstanceDestroyed = true;
		instanceReward.clear();
		doors.clear();
	}
	
	protected void despawnNpc(Npc npc)
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 549.35394f, 300.31052f, 399.91537f, (byte) 30);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}