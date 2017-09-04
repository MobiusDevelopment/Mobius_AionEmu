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
package system.handlers.instance.luna;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.SecretMunitionsFactoryReward;
import com.aionemu.gameserver.model.instance.playerreward.SecretMunitionsFactoryPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
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
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@InstanceID(301640000)
public class SecretMunitionsFactoryInstance extends GeneralInstanceHandler
{
	private int rank;
	private long instanceTime;
	@SuppressWarnings("unused")
	private Future<?> instanceTimer;
	private int destructionGolemKilled;
	private int mechaInfantrymanKilled;
	private boolean isInstanceDestroyed;
	private Future<?> munitionRaidTaskA1;
	private Future<?> munitionRaidTaskA2;
	private Future<?> munitionRaidTaskA3;
	private int maintenanceSoldierKilled;
	private Map<Integer, StaticDoor> doors;
	private SecretMunitionsFactoryReward instanceReward;
	private final FastList<Future<?>> factoryTask = FastList.newInstance();
	
	protected SecretMunitionsFactoryPlayerReward getPlayerReward(Integer object)
	{
		return (SecretMunitionsFactoryPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player)
	{
		instanceReward.addPlayerReward(new SecretMunitionsFactoryPlayerReward(player.getObjectId()));
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
		switch (npcId)
		{
			case 245185: // Mechaturerk’s Core.
				switch (Rnd.get(1, 7))
				{
					case 1:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150000, 5)); // Uncut Crystal.
						break;
					case 2:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150001, 5)); // Chipped Crystal.
						break;
					case 3:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150002, 5)); // Cloudy Crystal.
						break;
					case 4:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150003, 5)); // Clear Crystal.
						break;
					case 5:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150004, 5)); // Flawless Crystal.
						break;
					case 6:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150005, 5)); // Luna’s Light.
						break;
					case 7:
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 152150006, 5)); // Luna’s Blessing.
						break;
				}
				break;
			case 834443: // Mechaturerk’s Treasure Box.
			case 834444: // Mechaturerk’s Special Treasure Box.
				break;
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 243993: // Mechaturerk’s Cannon.
				despawnNpc(npc);
				// A dark energy is spreading.
				sendMsgByRace(1403662, Race.PC_ALL, 0);
				spawn(833835, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); // Mechaturerk's Cannon.
				break;
			case 245759: // Siege Factory Watcher.
				startMunitionRaidA1_1();
				break;
			case 243663: // Mechaturerk Machine Monster.
				despawnNpc(npc);
				deleteNpc(833896); // Factory Gate.
				// The Destruction Golem has appeared!
				sendMsgByRace(1403649, Race.PC_ALL, 0);
				// The Machine Monster’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403645, Race.PC_ALL, 2000);
				spawn(703380, 138.84042f, 256.166f, 191.8727f, (byte) 0); // Machine Monster’s Footlocker.
				spawn(243664, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); // Mechaturerk.
				break;
			case 243664: // Mechaturerk.
				points = 878600;
				munitionRaidTaskA1.cancel(true);
				munitionRaidTaskA2.cancel(true);
				munitionRaidTaskA3.cancel(true);
				// You killed Mechaturerk!
				sendMsgByRace(1403653, Race.PC_ALL, 5000);
				// Mechaturerk’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403646, Race.PC_ALL, 8000);
				// Mechaturerk’s Core has appeared inside the Munitions Factory.
				sendMsgByRace(1403647, Race.PC_ALL, 11000);
				// The Destruction Golem’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403648, Race.PC_ALL, 14000);
				switch (Rnd.get(1, 2))
				{
					case 1:
						spawn(834443, 149.65579f, 260.02966f, 191.8727f, (byte) 0); // Mechaturerk’s Treasure Box.
						break;
					case 2:
						spawn(834444, 149.65579f, 260.02966f, 191.8727f, (byte) 0); // Mechaturerk’s Special Treasure Box.
						break;
				}
				ThreadPoolManager.getInstance().schedule((Runnable) () -> instance.doOnAllPlayers(player1 -> stopInstance(player1)), 8000);
				break;
			case 243853: // Mechaturerk Maintenance Soldier.
				maintenanceSoldierKilled++;
				if (maintenanceSoldierKilled == 10)
				{
					startMunitionRaidA3_1();
					munitionRaidTaskA1.cancel(true);
					// The Maintenance Soldier’s Footlocker has appeared inside the Munitions Factory.
					sendMsgByRace(1403641, Race.PC_ALL, 2000);
					spawn(703376, 138.75412f, 269.4629f, 191.8727f, (byte) 0); // Maintenance Soldier’s Footlocker.
				}
				break;
			case 243968: // Remirunerk.
				points = 500;
				// Remirunrunerk’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403643, Race.PC_ALL, 2000);
				spawn(703378, 138.79507f, 263.1448f, 191.8727f, (byte) 0); // Remirunrunerk’s Footlocker.
				break;
			case 243969: // Bomirunrunerk.
				points = 500;
				// Bomirunrunerk’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403644, Race.PC_ALL, 2000);
				spawn(703379, 138.76562f, 259.84332f, 191.8727f, (byte) 0); // Bomirunrunerk’s Footlocker.
				break;
			case 244028: // Mechaturerk Gunner.
				// The Gunner’s Footlocker has appeared inside the Munitions Factory.
				sendMsgByRace(1403642, Race.PC_ALL, 2000);
				spawn(703377, 138.77333f, 266.49652f, 191.8727f, (byte) 0); // Gunner’s Footlocker.
				break;
			case 244035: // Damaged Mecha Infantryman.
				mechaInfantrymanKilled++;
				if (mechaInfantrymanKilled == 2)
				{
					// The Armored Soldier’s Footlocker has appeared inside the Munitions Factory.
					sendMsgByRace(1403640, Race.PC_ALL, 2000);
					spawn(703375, 138.73476f, 272.44095f, 191.8727f, (byte) 0); // Armored Soldier’s Footlocker.
				}
				break;
			case 244135: // Melee Support Destruction Golem.
				final float x0 = npc.getX();
				final float y0 = npc.getY();
				final float z0 = npc.getZ();
				final byte h0 = npc.getHeading();
				ThreadPoolManager.getInstance().schedule((Runnable) () ->
				{
					if (!isInstanceDestroyed)
					{
						if ((x0 > 0) && (y0 > 0) && (z0 > 0))
						{
							// The recovery plant has emerged.
							sendMsgByRace(1403824, Race.PC_ALL, 0);
							spawn(703349, x0, y0, z0, h0); // Huge Healing Plant.
						}
					}
				}, 1000);
				break;
			case 244136: // Ranged Support Destruction Golem.
				final float x1 = npc.getX();
				final float y1 = npc.getY();
				final float z1 = npc.getZ();
				final byte h1 = npc.getHeading();
				ThreadPoolManager.getInstance().schedule((Runnable) () ->
				{
					if (!isInstanceDestroyed)
					{
						if ((x1 > 0) && (y1 > 0) && (z1 > 0))
						{
							// The recovery plant has emerged.
							sendMsgByRace(1403824, Race.PC_ALL, 0);
							spawn(703349, x1, y1, z1, h1); // Huge Healing Plant.
						}
					}
				}, 1000);
				destructionGolemKilled++;
				if (destructionGolemKilled == 10)
				{
					startMunitionRaidA5_1();
					munitionRaidTaskA2.cancel(true);
					// The Azure Living bomb has appeared!
					sendMsgByRace(1403650, Race.PC_ALL, 0);
					// Use the blue mechanical device!
					sendMsgByRace(1403663, Race.PC_ALL, 5000);
					// The Golden Living bomb has appeared!
					sendMsgByRace(1403651, Race.PC_ALL, 10000);
					// Use the yellow mechanical device!
					sendMsgByRace(1403663, Race.PC_ALL, 15000);
				}
				break;
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
		switch (npc.getNpcId())
		{
			case 703349: // Huge Healing Plant.
				despawnNpc(npc);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 30000);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.MP, 30000);
				break;
			case 243660: // Oil Cask.
				despawnNpc(npc);
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				ItemService.addItem(player, 164002362, 3); // Mechaturerk Oil Cask.
				break;
		}
	}
	
	private void munitionRaid(Npc npc)
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
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
	
	private void startMunitionRaidA1_1()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 10000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 20000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 30000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 40000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 50000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 60000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 70000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 80000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 90000);
		munitionRaidTaskA1 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA1();
			startMunitionRaidA2();
		}, 100000);
	}
	
	private void startMunitionRaidA3_1()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 10000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 20000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 30000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 40000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 50000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 60000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 70000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 80000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 90000);
		munitionRaidTaskA2 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA3();
			startMunitionRaidA4();
		}, 100000);
	}
	
	private void startMunitionRaidA5_1()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 10000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 20000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 30000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 40000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
			// The Azure Living bomb has appeared!
			sendMsgByRace(1403650, Race.PC_ALL, 0);
			// Use the blue mechanical device!
			sendMsgByRace(1403663, Race.PC_ALL, 5000);
			// The Golden Living bomb has appeared!
			sendMsgByRace(1403651, Race.PC_ALL, 10000);
			// Use the yellow mechanical device!
			sendMsgByRace(1403663, Race.PC_ALL, 15000);
		}, 50000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 60000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 70000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 80000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
		}, 90000);
		munitionRaidTaskA3 = ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startMunitionRaidA5();
			startMunitionRaidA6();
			// The Azure Living bomb has appeared!
			sendMsgByRace(1403650, Race.PC_ALL, 0);
			// Use the blue mechanical device!
			sendMsgByRace(1403663, Race.PC_ALL, 5000);
			// The Golden Living bomb has appeared!
			sendMsgByRace(1403651, Race.PC_ALL, 10000);
			// Use the yellow mechanical device!
			sendMsgByRace(1403663, Race.PC_ALL, 15000);
		}, 100000);
	}
	
	/**
	 * Maintenance Soldier.
	 */
	private void startMunitionRaidA1()
	{
		munitionRaid((Npc) spawn(243853, 133.37782f, 229.28152f, 191.94075f, (byte) 15)); // Mechaturerk Maintenance Soldier.
	}
	
	private void startMunitionRaidA2()
	{
		munitionRaid((Npc) spawn(243853, 132.91176f, 289.63672f, 191.98668f, (byte) 106)); // Mechaturerk Maintenance Soldier.
	}
	
	/**
	 * Destruction Golem.
	 */
	private void startMunitionRaidA3()
	{
		munitionRaid((Npc) spawn(244135, 133.37782f, 229.28152f, 191.94075f, (byte) 15)); // Melee Support Destruction Golem.
	}
	
	void startMunitionRaidA4()
	{
		munitionRaid((Npc) spawn(244136, 132.91176f, 289.63672f, 191.98668f, (byte) 106)); // Ranged Support Destruction Golem.
	}
	
	/**
	 * Living Bomb.
	 */
	private void startMunitionRaidA5()
	{
		munitionRaid((Npc) spawn(243661, 133.37782f, 229.28152f, 191.94075f, (byte) 15)); // Azure Living Bomb.
	}
	
	private void startMunitionRaidA6()
	{
		munitionRaid((Npc) spawn(243662, 132.91176f, 289.63672f, 191.98668f, (byte) 106)); // Golden Living Bomb.
	}
	
	private int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000)
		{
			return (int) (60000 - result);
		}
		else if (result < 3600000)
		{ // 1 Hour.
			return (int) (3600000 - (result - 60000));
		}
		return 0;
	}
	
	private void sendPacket(int nameId, int point)
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
		else if (totalPoints > 1000)
		{ // Rank A.
			rank = 2;
		}
		else if (totalPoints > 50)
		{ // Rank B.
			rank = 3;
		}
		else if (totalPoints > 50)
		{ // Rank C.
			rank = 4;
		}
		else if (totalPoints > 50)
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
		factoryTask.add(ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			deleteNpc(833868); // Rock Pile.
			doors.get(27).setOpen(true);
			instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
			sendPacket(0, 0);
		}, 60000));
		factoryTask.add(ThreadPoolManager.getInstance().schedule((Runnable) () -> instance.doOnAllPlayers(player -> stopInstance(player)), 3600000)); // 1 Hour.
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!instanceReward.containPlayer(player.getObjectId()))
		{
			addPlayerReward(player);
		}
		final SecretMunitionsFactoryPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded())
		{
			doReward(player);
		}
		switch (player.getRace())
		{
			case ELYOS:
				// Luna Detachment Transformation.
				SkillEngine.getInstance().applyEffectDirectly(21347, player, player, 3000000 * 1);
				break;
			case ASMODIANS:
				// Luna Detachment Transformation.
				SkillEngine.getInstance().applyEffectDirectly(21348, player, player, 3000000 * 1);
				break;
		}
		ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnLunaDetachment(), 20000);
		sendPacket(0, 0);
	}
	
	private void spawnLunaDetachment()
	{
		spawn(833826, 385.30814f, 286.88065f, 198.56099f, (byte) 6); // Roxy.
		spawn(833827, 386.10965f, 282.91656f, 198.24266f, (byte) 11); // Mak.
		spawn(833828, 386.33496f, 290.52594f, 198.5f, (byte) 115); // Manad.
		spawn(833829, 382.25574f, 283.81686f, 198.50284f, (byte) 7); // Herez.
		spawn(833897, 388.17896f, 279.8141f, 197.98882f, (byte) 14); // Joel.
	}
	
	protected void stopInstance(Player player)
	{
		stopInstanceTask();
		instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[Congratulation]: you finish <Secret Munitions Factory 5.0.5>");
		sendPacket(0, 0);
	}
	
	@Override
	public void doReward(Player player)
	{
		final SecretMunitionsFactoryPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded())
		{
			playerReward.setRewarded();
			final int factoryRank = instanceReward.getRank();
			switch (factoryRank)
			{
				case 1: // Rank S
					playerReward.setMechaturerkSecretBox(1);
					// Mechaturerk's Secret Box.
					ItemService.addItem(player, 188055475, 1);
					break;
				case 2: // Rank A
					playerReward.setMechaturerkNormalTreasureChest(1);
					// Mechaturerk’s Normal Treasure Chest.
					ItemService.addItem(player, 188055647, 1);
					break;
				case 3: // Rank B
					playerReward.setMechaturerkSpecialTreasureBox(1);
					// Mechaturerk’s Special Treasure Box.
					ItemService.addItem(player, 188055648, 1);
					break;
				case 4: // Rank C
					playerReward.setMechaturerkSpecialTreasureBox(1);
					// Mechaturerk’s Special Treasure Box.
					ItemService.addItem(player, 188055648, 1);
					break;
				case 5: // Rank D
					break;
				case 6: // Rank F
					break;
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		instanceReward = new SecretMunitionsFactoryReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
	}
	
	@Override
	public void onInstanceDestroy()
	{
		stopInstanceTask();
		isInstanceDestroyed = true;
		instanceReward.clear();
		doors.clear();
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = factoryTask.head(), end = factoryTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
		}
	}
	
	protected void despawnNpc(Npc npc)
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
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeEffects(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeEffects(player);
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21347);
		effectController.removeEffect(21348);
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 376.39178f, 282.3938f, 198.24043f, (byte) 72);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}