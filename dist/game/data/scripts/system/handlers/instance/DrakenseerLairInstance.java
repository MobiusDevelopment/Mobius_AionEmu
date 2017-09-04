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

import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.flyring.FlyRing;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.flyring.FlyRingTemplate;
import com.aionemu.gameserver.model.utils3d.Point3D;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/****/
/**
 * Author Rinzler (Encom) /** Source: https://www.youtube.com/watch?v=hO-QSwBfeXI /
 ****/

@InstanceID(301620000)
public class DrakenseerLairInstance extends GeneralInstanceHandler
{
	private int abyssGateEnhancerKilled;
	private boolean isStartTimer = false;
	protected boolean isInstanceDestroyed = false;
	private final FastList<Future<?>> drakenseerLairTask = FastList.newInstance();
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 220450: // Akhal The Oracle.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188055152, 1)); // 아크할의 환영 신석 꾸러미.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054283, 1)); // Blood Mark Box.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188053789, 1)); // Major Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053619, 1)); // Ancient Manastone Bundle.
						switch (Rnd.get(1, 2))
						{
							case 1:
							{
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054910, 1)); // 아크할의 무기 상자.
								break;
							}
							case 2:
							{
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188054911, 1)); // 아크할의 방어구 상자.
								break;
							}
						}
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		spawnDrakenseerLairRings();
		// You have entered Drakenseer's Lair.
		sendMsgByRace(1403376, Race.PC_ALL, 5000);
		final Npc npc = instance.getNpc(220450); // Akhal The Oracle.
		if (npc != null)
		{
			SkillEngine.getInstance().getSkill(npc, 21791, 60, npc).useNoAnimationSkill(); // Turning Tide.
		}
	}
	
	@Override
	public boolean onPassFlyingRing(Player player, String flyingRing)
	{
		if (flyingRing.equals("DRAKENSEER_LAIR"))
		{
			if (!isStartTimer)
			{
				isStartTimer = true;
				System.currentTimeMillis();
				instance.doOnAllPlayers(player1 ->
				{
					if (player1.isOnline())
					{
						startDrakenseerLairTimer();
						PacketSendUtility.sendPacket(player1, new SM_QUEST_ACTION(0, 600));
						// Destroy the Shielding Conduits within 10 minutes and defeat Akhal.
						PacketSendUtility.sendPacket(player1, new SM_SYSTEM_MESSAGE(1403377));
					}
				});
			}
		}
		return false;
	}
	
	private void spawnDrakenseerLairRings()
	{
		final FlyRing f1 = new FlyRing(new FlyRingTemplate("DRAKENSEER_LAIR", mapId, new Point3D(283.44757, 342.6241, 336.25607), new Point3D(276.73062, 339.42966, 345.29074), new Point3D(270.43948, 340.3889, 336.3338), 93), instanceId);
		f1.spawn();
	}
	
	protected void startDrakenseerLairTimer()
	{
		// Enter Drakenseer's Lair and destroy the Shielding Conduits.
		sendMessage(1403375, 1 * 60 * 1000);
		// You have one minute left to destroy the remaining Shielding Conduits.
		sendMessage(1403382, 9 * 60 * 1000);
		drakenseerLairTask.add(ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			instance.doOnAllPlayers(player -> onExitInstance(player));
			onInstanceDestroy();
		}, 600000)); // 10 Minutes.
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 857974: // Balaur Abyss Gate Enhancer A.
			case 857975: // Balaur Abyss Gate Enhancer B.
			case 857976: // Balaur Abyss Gate Enhancer C.
			{
				abyssGateEnhancerKilled++;
				if (abyssGateEnhancerKilled == 1)
				{
					// Two Shielding Conduits remain.
					sendMsgByRace(1403379, Race.PC_ALL, 0);
				}
				else if (abyssGateEnhancerKilled == 2)
				{
					// One Shielding Conduit remains.
					sendMsgByRace(1403380, Race.PC_ALL, 0);
				}
				else if (abyssGateEnhancerKilled == 3)
				{
					stopDrakenseerLairTimer(player);
					// With all the Shielding Conduits destroyed, Akhal finally appears.
					sendMsgByRace(1403381, Race.PC_ALL, 2000);
					final Npc akhalTheOracle = instance.getNpc(220450); // Akhal The Oracle.
					akhalTheOracle.getEffectController().removeEffect(21791); // Turning Tide.
					instance.doOnAllPlayers(player1 ->
					{
						if (player1.isOnline())
						{
							PacketSendUtility.sendPacket(player1, new SM_QUEST_ACTION(0, 0));
						}
					});
				}
				despawnNpc(npc);
				break;
			}
			case 220450: // Akhal The Oracle.
			{
				spawn(806240, 299.1905f, 258.07004f, 319.67477f, (byte) 110); // Drakenseer's Lair Exit.
				sendMsg("[Congratulation]: you finish <Drakenseer's Lair 5.0>");
				break;
			}
		}
	}
	
	protected void stopDrakenseerLairTimer(Player player)
	{
		stopDrakenseerLairTask();
	}
	
	private void stopDrakenseerLairTask()
	{
		for (FastList.Node<Future<?>> n = drakenseerLairTask.head(), end = drakenseerLairTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
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
	
	private void sendMessage(int msgId, long delay)
	{
		if (delay == 0)
		{
			sendMsg(msgId);
		}
		else
		{
			ThreadPoolManager.getInstance().schedule((Runnable) () -> sendMsg(msgId), delay);
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		stopDrakenseerLairTask();
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
	public void onPlayerLogOut(Player player)
	{
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 275.4503f, 349.80862f, 336.47577f, (byte) 96);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}