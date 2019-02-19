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
import com.aionemu.gameserver.model.instance.instancereward.SmolderingReward;
import com.aionemu.gameserver.model.instance.playerreward.SmolderingPlayerReward;
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
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(302000000)
public class SmolderingFireTempleInstance extends GeneralInstanceHandler
{
	private int rank;
	private long instanceTime;
	private int vengefulObscura;
	@SuppressWarnings("unused")
	private boolean isInstanceDestroyed;
	Map<Integer, StaticDoor> doors;
	SmolderingReward instanceReward;
	private final FastList<Future<?>> smolderingTask = FastList.newInstance();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	protected SmolderingPlayerReward getPlayerReward(Integer object)
	{
		return (SmolderingPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player)
	{
		instanceReward.addPlayerReward(new SmolderingPlayerReward(player.getObjectId()));
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
			case 244435: // Potion Chest.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002085, 2)); // Hero GM’s Secret Remedy Of Recovery.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002086, 2)); // Hero GM’s Quality Secret Remedy Of Recovery.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002087, 2)); // Hero GM’s Secret Remedy Of DP Recovery.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002088, 2)); // Hero GM’s Quality Secret Remedy Of DP Recovery.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002089, 2)); // Hero GM’s Secret Remedy Of Recovery.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 162002090, 2)); // Hero GM’s Quality Secret Remedy Of Recovery.
					}
				}
				break;
			}
			case 834058: // Smoldering Fire Temple Treasure Chest.
			case 834059: // Smoldering Fire Temple Premium Treasure Chest.
			case 834060: // Smoldering Fire Temple Treasure Chest.
			case 834061: // Smoldering Fire Temple Quality Treasure Chest.
			{
				switch (Rnd.get(1, 4))
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
					case 3:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054629, 1)); // Highest Grade Reward Bundle.
						break;
					}
					case 4:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054630, 1)); // High Grade Reward Bundle.
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
		storage.decreaseByItemId(185000270, storage.getItemCountByItemId(185000270)); // Nostalgic Fire Temple Treasure Chest Key.
		storage.decreaseByItemId(162002031, storage.getItemCountByItemId(162002085)); // Hero GM’s Secret Remedy Of Recovery.
		storage.decreaseByItemId(162002032, storage.getItemCountByItemId(162002086)); // Hero GM’s Quality Secret Remedy Of Recovery.
		storage.decreaseByItemId(162002033, storage.getItemCountByItemId(162002087)); // Hero GM’s Secret Remedy Of DP Recovery.
		storage.decreaseByItemId(162002034, storage.getItemCountByItemId(162002088)); // Hero GM’s Quality Secret Remedy Of DP Recovery.
		storage.decreaseByItemId(162002035, storage.getItemCountByItemId(162002089)); // Hero GM’s Secret Remedy Of Recovery.
		storage.decreaseByItemId(162002036, storage.getItemCountByItemId(162002090)); // Hero GM’s Quality Secret Remedy Of Recovery.
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 244084: // Flame Spirit.
			case 244085: // Fire Spirit.
			case 244091: // Flame Spirit.
			{
				points = 180;
				despawnNpc(npc);
				break;
			}
			case 244086: // Kalgolem.
			case 244092: // Fire Spirit.
			{
				points = 160;
				despawnNpc(npc);
				break;
			}
			case 244087: // Enhanced Kalgolem.
			case 244088: // Vengeful Obscura.
			{
				points = 250;
				despawnNpc(npc);
				break;
			}
			case 244093: // Vengeful Obscura.
			{
				points = 250;
				despawnNpc(npc);
				vengefulObscura++;
				if (vengefulObscura == 12)
				{
					spawn(244097, 416.1324f, 97.165924f, 117.19401f, (byte) 50); // Temple Guardian.
				}
				break;
			}
			case 244089: // Vengeful Obscura.
			{
				points = 660;
				despawnNpc(npc);
				break;
			}
			case 244094: // Enhanced Orange Crystal Molgat.
			{
				points = 1740;
				despawnNpc(npc);
				break;
			}
			case 244095: // Enhanced Silver Blade Rotan.
			{
				points = 2040;
				despawnNpc(npc);
				doors.get(8).setOpen(true);
				break;
			}
			case 244096: // Enhanced Tough Sipus.
			{
				points = 12000;
				despawnNpc(npc);
				final SpawnTemplate kromedeOpenRoom1 = SpawnEngine.addNewSingleTimeSpawn(302000000, 834067, 292.34671f, 166.54131f, 119.53692f, (byte) 0);
				kromedeOpenRoom1.setEntityId(40);
				objects.put(834067, SpawnEngine.spawnObject(kromedeOpenRoom1, instanceId));
				break;
			}
			case 244097: // Temple Guardian.
			{
				points = 14400;
				despawnNpc(npc);
				final SpawnTemplate kromedeOpenRoom2 = SpawnEngine.addNewSingleTimeSpawn(302000000, 834066, 169.24069f, 417.35110f, 140.77321f, (byte) 0);
				kromedeOpenRoom2.setEntityId(3);
				objects.put(834066, SpawnEngine.spawnObject(kromedeOpenRoom2, instanceId));
				spawn(244098, 416.1324f, 97.165924f, 117.19401f, (byte) 50); // Enraged Lady Angerr.
				break;
			}
			case 244098: // Enraged Lady Angerr.
			{
				points = 48000;
				despawnNpc(npc);
				spawn(244099, 416.1324f, 97.165924f, 117.19401f, (byte) 50); // Enraged Judge Kaliga.
				break;
			}
			case 244099: // Enraged Judge Kaliga.
			{
				points = 272000;
				despawnNpc(npc);
				spawn(244100, 416.1324f, 97.165924f, 117.19401f, (byte) 50); // Enraged Kromede.
				break;
			}
			case 244100: // Enraged Kromede.
			{
				points = 500000;
				despawnNpc(npc);
				spawn(834068, 416.1324f, 97.165924f, 117.19401f, (byte) 50); // Old Fire Temple Fortune Server.
				ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 3000);
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
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		final PlayerEffectController effectController = player.getEffectController();
		switch (npc.getNpcId())
		{
			case 834055: // GM Stomper.
			{
				if (player.getCommonData().getRace() == Race.ELYOS)
				{
					effectController.removeEffect(21376);
					effectController.removeEffect(21377);
					SkillEngine.getInstance().getSkill(npc, 21375, 1, player).useNoAnimationSkill();
				}
				else if (player.getCommonData().getRace() == Race.ASMODIANS)
				{
					effectController.removeEffect(21379);
					effectController.removeEffect(21380);
					SkillEngine.getInstance().getSkill(npc, 21378, 1, player).useNoAnimationSkill();
				}
				break;
			}
			case 834056: // GM Shine.
			{
				if (player.getCommonData().getRace() == Race.ELYOS)
				{
					effectController.removeEffect(21375);
					effectController.removeEffect(21377);
					SkillEngine.getInstance().getSkill(npc, 21376, 1, player).useNoAnimationSkill();
				}
				else if (player.getCommonData().getRace() == Race.ASMODIANS)
				{
					effectController.removeEffect(21378);
					effectController.removeEffect(21380);
					SkillEngine.getInstance().getSkill(npc, 21379, 1, player).useNoAnimationSkill();
				}
				break;
			}
			case 834057: // GM Iris.
			{
				if (player.getCommonData().getRace() == Race.ELYOS)
				{
					effectController.removeEffect(21375);
					effectController.removeEffect(21376);
					SkillEngine.getInstance().getSkill(npc, 21377, 1, player).useNoAnimationSkill();
				}
				else if (player.getCommonData().getRace() == Race.ASMODIANS)
				{
					effectController.removeEffect(21378);
					effectController.removeEffect(21379);
					SkillEngine.getInstance().getSkill(npc, 21380, 1, player).useNoAnimationSkill();
				}
				break;
			}
		}
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21375);
		effectController.removeEffect(21376);
		effectController.removeEffect(21377);
		effectController.removeEffect(21378);
		effectController.removeEffect(21379);
		effectController.removeEffect(21380);
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
		smolderingTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			doors.get(2).setOpen(true);
			// The member recruitment window has passed. You cannot recruit any more members.
			sendMsgByRace(1401181, Race.PC_ALL, 0);
			instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
			sendPacket(0, 0);
		}, 60000));
		smolderingTask.add(ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 600000));
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!instanceReward.containPlayer(player.getObjectId()))
		{
			addPlayerReward(player);
		}
		final SmolderingPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded())
		{
			doReward(player);
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
		sendMsg("[Congratulation]: you finish <Smoldering Fire Temple 5.1>");
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
		final SmolderingPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded())
		{
			playerReward.setRewarded();
			final int smolderingRank = instanceReward.getRank();
			switch (smolderingRank)
			{
				case 1: // Rank S
				{
					playerReward.setSmolderingKey(6);
					// Smoldering Fire Temple Treasure Key.
					ItemService.addItem(player, 185000270, 6);
					break;
				}
				case 2: // Rank A
				{
					playerReward.setSmolderingKey(4);
					// Smoldering Fire Temple Treasure Key.
					ItemService.addItem(player, 185000270, 4);
					break;
				}
				case 3: // Rank B
				{
					playerReward.setSmolderingKey(3);
					// Smoldering Fire Temple Treasure Key.
					ItemService.addItem(player, 185000270, 3);
					break;
				}
				case 4: // Rank C
				{
					playerReward.setSmolderingKey(2);
					// Smoldering Fire Temple Treasure Key.
					ItemService.addItem(player, 185000270, 2);
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
		instanceReward = new SmolderingReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = smolderingTask.head(), end = smolderingTask.tail(); (n = n.getNext()) != end;)
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 156.9308f, 426.65845f, 139.86337f, (byte) 118);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}