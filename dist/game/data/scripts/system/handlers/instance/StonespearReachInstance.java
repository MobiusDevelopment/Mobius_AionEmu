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

import java.util.concurrent.Future;

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.StonespearReachReward;
import com.aionemu.gameserver.model.instance.playerreward.StonespearReachPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(301500000)
public class StonespearReachInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private long instanceTime;
	private Future<?> instanceTimer;
	private StonespearReachReward instanceReward;
	private final FastList<Future<?>> stonespearTask = FastList.newInstance();
	
	protected StonespearReachPlayerReward getPlayerReward(Player player)
	{
		final Integer object = player.getObjectId();
		if (instanceReward.getPlayerReward(object) == null)
		{
			addPlayerToReward(player);
		}
		return (StonespearReachPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	private void addPlayerToReward(Player player)
	{
		instanceReward.addPlayerReward(new StonespearReachPlayerReward(player.getObjectId()));
	}
	
	private boolean containPlayer(Integer object)
	{
		return instanceReward.containPlayer(object);
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
		}
	}
	
	private void SpawnRaceInstance()
	{
		final int territoryManager = spawnRace == Race.ASMODIANS ? 833489 : 833488; // Legion Territory Manager.
		spawn(territoryManager, 165.91524f, 264.50375f, 97.454155f, (byte) 0);
	}
	
	protected void startInstanceTask()
	{
		instanceTime = System.currentTimeMillis();
		instanceReward.setInstanceStartTime();
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			spawn(855833, 251.47273f, 264.46713f, 96.30522f, (byte) 61);
			spawn(855833, 230.85971f, 285.67032f, 96.418526f, (byte) 90);
			spawn(855833, 211.20746f, 264.05276f, 96.53291f, (byte) 0);
			spawn(855833, 231.29951f, 243.66095f, 96.36497f, (byte) 29);
		}, 90000));
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
		}, 220000));
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
		}, 400000));
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
		}, 600000));
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
		}, 900000));
		stonespearTask.add(ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 1800000));
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 855833:
			{
				points = 2000;
				despawnNpc(npc);
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
	
	private int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 90000)
		{
			return (int) (90000 - result);
		}
		else if (result < 1800000)
		{ // 30-Mins
			return (int) (1800000 - (result - 90000));
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
		int rank = 0;
		if (totalPoints > 71600)
		{ // Rank S.
			rank = 1;
		}
		else if (totalPoints > 41000)
		{ // Rank A.
			rank = 2;
		}
		else if (totalPoints > 26000)
		{ // Rank B.
			rank = 3;
		}
		else if (totalPoints > 14000)
		{ // Rank C.
			rank = 4;
		}
		else if (totalPoints > 8800)
		{ // Rank D.
			rank = 5;
		}
		else if (totalPoints > 0)
		{ // Rank F.
			rank = 6;
		}
		else
		{
			rank = 8;
		}
		return rank;
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!containPlayer(player.getObjectId()))
		{
			addPlayerToReward(player);
		}
		if (instanceTimer == null)
		{
			instanceTime = System.currentTimeMillis();
			instanceTimer = ThreadPoolManager.getInstance().schedule(() ->
			{
				deleteNpc(833284);
				// The member recruitment window has passed. You cannot recruit any more members.
				sendMsgByRace(1401181, Race.PC_ALL, 5000);
				instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
				sendPacket(0, 0);
			}, 90000);
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
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[Congratulation]: you finish <Stonespear Reach 4.8>");
		sendPacket(0, 0);
	}
	
	@Override
	public void doReward(Player player)
	{
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = stonespearTask.head(), end = stonespearTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		instanceReward = new StonespearReachReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		startInstanceTask();
	}
	
	@Override
	public void onInstanceDestroy()
	{
		if (instanceTimer != null)
		{
			instanceTimer.cancel(false);
		}
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	void deleteNpc(int npcId)
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
		ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player ->
		{
			if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
			}
		}), time);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}