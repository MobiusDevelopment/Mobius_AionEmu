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
import com.aionemu.gameserver.model.instance.instancereward.ContaminatedUnderpathReward;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.playerreward.ContaminatedUnderpathPlayerReward;
import com.aionemu.gameserver.model.items.storage.Storage;
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
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(301630000)
public class ContaminatedUnderpathInstance extends GeneralInstanceHandler
{
	private int rank;
	private long instanceTime;
	@SuppressWarnings("unused")
	private Future<?> instanceTimer;
	private int IDLunaDefYZombie751ST;
	private int IDLunaDefYZombieF751ST;
	private int IDLunaDefZombieSpider753RD;
	private int IDLunaDefZombieButcherAsN75;
	private int IDLunaDefZombieVampireFD753RD;
	boolean isInstanceDestroyed;
	private Map<Integer, StaticDoor> doors;
	private ContaminatedUnderpathReward instanceReward;
	private final FastList<Future<?>> contaminedTask = FastList.newInstance();
	
	protected ContaminatedUnderpathPlayerReward getPlayerReward(Integer object)
	{
		return (ContaminatedUnderpathPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player)
	{
		instanceReward.addPlayerReward(new ContaminatedUnderpathPlayerReward(player.getObjectId()));
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
			case 703384: // Infected Bone Mound.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182216109, 1)); // Maad-S Molar.
				break;
			}
			case 703385: // Infected Flesh Lump.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182216110, 1)); // Maad-S Skin Tissue.
				break;
			}
			case 833866: // Bright Aether Supply Chest.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182007405, 3)); // Bright Aether.
				break;
			}
			case 834253: // Maedrunerk Legion Treasures.
			{
				break;
			}
		}
	}
	
	private void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(182007405, storage.getItemCountByItemId(182007405)); // Bright Aether.
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 833812: // Flame Switch.
			{
				despawnNpc(npc);
				spawn(833813, 232.31128f, 239.1524f, 160.36285f, (byte) 90); // Contaminated Underpath Fire.
				spawn(833813, 225.83708f, 239.09781f, 160.36285f, (byte) 90); // Contaminated Underpath Fire.
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		final int npcId = npc.getNpcId();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 243647: // MAD-74C.
			{
				points = 50;
				startGringolTheDevourerWave();
				break;
			}
			case 245547: // Experimental Reian.
			{
				points = 600;
				IDLunaDefYZombie751ST++;
				if (IDLunaDefYZombie751ST == 16)
				{
					startUndergroundWave2();
				}
				break;
			}
			case 245548: // Experimental Reian.
			{
				points = 600;
				IDLunaDefYZombieF751ST++;
				if (IDLunaDefYZombieF751ST == 16)
				{
					startMAD74CWave();
					// You’re hearing heavy breathing.
					sendMsgByRace(1403656, Race.PC_ALL, 0);
				}
				break;
			}
			case 245556: // Gringol The Devourer.
			{
				points = 2500;
				IDLunaDefZombieButcherAsN75++;
				if (IDLunaDefZombieButcherAsN75 == 6)
				{
					startUndergroundWave3();
					// You’re hearing lots of footsteps.
					sendMsgByRace(1403655, Race.PC_ALL, 0);
				}
				break;
			}
			case 245557: // Experimental Reian.
			case 245558: // Experimental Reian.
			{
				points = 600;
				break;
			}
			case 245565: // Zombie Transfectant.
			{
				points = 150;
				IDLunaDefZombieSpider753RD++;
				if (IDLunaDefZombieSpider753RD == 24)
				{
					startUndergroundWave4();
				}
				break;
			}
			case 245573: // Floating Zombie Test Subject.
			{
				IDLunaDefZombieVampireFD753RD++;
				if (IDLunaDefZombieVampireFD753RD == 4)
				{
					startMAADSWave();
				}
				break;
			}
			case 245575: // MAAD-S.
			{
				points = 500000;
				spawn(703384, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); // Infected Bone Mound.
				spawn(703385, npc.getX(), npc.getY(), npc.getZ(), npc.getHeading()); // Infected Flesh Lump.
				ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 5000);
				break;
			}
		}
		if (instanceReward.getInstanceScoreType().isStartProgress())
		{
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
		switch (npcId)
		{
			case 833863: // Frontal Barricade.
			{
				// The barricade in the front was destroyed.
				sendMsgByRace(1403609, Race.PC_ALL, 0);
				instanceReward.addPoints(-500);
				break;
			}
			case 833864: // Rear Barricade.
			{
				// The barricade in the back was destroyed. Atreia is in danger.
				sendMsgByRace(1403610, Race.PC_ALL, 0);
				instanceReward.addPoints(-25000);
				break;
			}
		}
	}
	
	void undergroundRaid(Npc npc)
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
	
	private void startUndergroundWave2()
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245548, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245548, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 1000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245548, 222.73087f, 285.1328f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 224.51028f, 285.3419f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 226.20146f, 285.4962f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 227.90823f, 285.6521f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 229.46863f, 285.79477f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 231.11894f, 285.9454f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 232.91904f, 286.11008f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245548, 234.55264f, 286.2593f, 160.3114f, (byte) 91));
		}, 15000);
	}
	
	private void startMAD74CWave()
	{
		ThreadPoolManager.getInstance().schedule(() -> undergroundRaid((Npc) spawn(243647, 229.36754f, 283.24008f, 160.3114f, (byte) 90)), 1000);
	}
	
	private void startGringolTheDevourerWave()
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245556, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245556, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245556, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
		}, 1000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245556, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245556, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245556, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
		}, 15000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245557, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245557, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 25000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245558, 222.73087f, 285.1328f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 224.51028f, 285.3419f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 226.20146f, 285.4962f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 227.90823f, 285.6521f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 229.46863f, 285.79477f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 231.11894f, 285.9454f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 232.91904f, 286.11008f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245558, 234.55264f, 286.2593f, 160.3114f, (byte) 91));
		}, 35000);
	}
	
	private void startUndergroundWave3()
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245557, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245557, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245557, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 10000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245565, 222.73087f, 285.1328f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 224.51028f, 285.3419f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 226.20146f, 285.4962f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 227.90823f, 285.6521f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 229.46863f, 285.79477f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 231.11894f, 285.9454f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 232.91904f, 286.11008f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 234.55264f, 286.2593f, 160.3114f, (byte) 91));
		}, 20000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245565, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245565, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 30000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245565, 222.73087f, 285.1328f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 224.51028f, 285.3419f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 226.20146f, 285.4962f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 227.90823f, 285.6521f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 229.46863f, 285.79477f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 231.11894f, 285.9454f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 232.91904f, 286.11008f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245565, 234.55264f, 286.2593f, 160.3114f, (byte) 91));
		}, 40000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245567, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245567, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245567, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 50000);
	}
	
	private void startUndergroundWave4()
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245571, 222.75296f, 282.81735f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245571, 224.62003f, 282.99942f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 226.25397f, 283.0834f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 227.87476f, 283.16495f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 229.36754f, 283.24008f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 231.03639f, 283.3239f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 232.78214f, 283.41165f, 160.3114f, (byte) 90));
			undergroundRaid((Npc) spawn(245571, 234.50366f, 283.59793f, 160.3114f, (byte) 90));
		}, 1000);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			undergroundRaid((Npc) spawn(245572, 222.73087f, 285.1328f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245573, 224.51028f, 285.3419f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245572, 226.20146f, 285.4962f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245573, 227.90823f, 285.6521f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245572, 229.46863f, 285.79477f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245573, 231.11894f, 285.9454f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245572, 232.91904f, 286.11008f, 160.3114f, (byte) 91));
			undergroundRaid((Npc) spawn(245573, 234.55264f, 286.2593f, 160.3114f, (byte) 91));
		}, 15000);
	}
	
	private void startMAADSWave()
	{
		ThreadPoolManager.getInstance().schedule(() -> undergroundRaid((Npc) spawn(245575, 229.36754f, 283.24008f, 160.3114f, (byte) 90)), 1000);
	}
	
	private int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000)
		{
			return (int) (60000 - result);
		}
		else if (result < 3600000) // 1 Hour.
		{
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
		if (totalPoints > 540000) // Rank S.
		{
			rank = 1;
		}
		else if (totalPoints > 100) // Rank A.
		{
			rank = 2;
		}
		else if (totalPoints > 50) // Rank B.
		{
			rank = 3;
		}
		else if (totalPoints > 50) // Rank C.
		{
			rank = 4;
		}
		else if (totalPoints > 50) // Rank D.
		{
			rank = 5;
		}
		else if (totalPoints >= 0) // Rank F.
		{
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
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			// The defense turret platform has appeared.
			// You can use Bright Aether to transform it for 15 seconds.
			sendMsgByRace(1403696, Race.PC_ALL, 0);
			// You’re hearing a sharp yell.
			sendMsgByRace(1403657, Race.PC_ALL, 10000);
			doors.get(28).setOpen(true);
			instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
			sendPacket(0, 0);
		}, 60000));
		contaminedTask.add(ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 3600000)); // 1 Hour.
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!instanceReward.containPlayer(player.getObjectId()))
		{
			addPlayerReward(player);
		}
		final ContaminatedUnderpathPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded())
		{
			doReward(player);
		}
		switch (player.getRace())
		{
			case ELYOS:
			{
				// Luna Detachment Transformation.
				SkillEngine.getInstance().applyEffectDirectly(21345, player, player, 3000000 * 1);
				break;
			}
			case ASMODIANS:
			{
				// Luna Detachment Transformation.
				SkillEngine.getInstance().applyEffectDirectly(21346, player, player, 3000000 * 1);
				break;
			}
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
		sendMsg("[Congratulation]: you finish <Contaminated Underpath 5.0.5>");
		sendPacket(0, 0);
	}
	
	@Override
	public void doReward(Player player)
	{
		final ContaminatedUnderpathPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded())
		{
			playerReward.setRewarded();
			final int contaminatedRank = instanceReward.getRank();
			switch (contaminatedRank)
			{
				case 1: // Rank S
				{
					playerReward.setContaminatedPremiumRewardBundle(1);
					// Contaminated Premium Reward Bundle.
					ItemService.addItem(player, 188055598, 1);
					break;
				}
				case 2: // Rank A
				{
					playerReward.setContaminatedHighestRewardBundle(1);
					// Contaminated Highest Reward Bundle.
					ItemService.addItem(player, 188055599, 1);
					break;
				}
				case 3: // Rank B
				{
					playerReward.setContaminatedUnderpathSpecialPouch(1);
					// Contaminated Underpath Special Pouch.
					ItemService.addItem(player, 188055664, 1);
					break;
				}
				case 4: // Rank C
				{
					playerReward.setContaminatedUnderpathSpecialPouch(1);
					// Contaminated Underpath Special Pouch.
					ItemService.addItem(player, 188055664, 1);
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
		instanceReward = new ContaminatedUnderpathReward(mapId, instanceId);
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
		for (FastList.Node<Future<?>> n = contaminedTask.head(), end = contaminedTask.tail(); (n = n.getNext()) != end;)
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
	
	// private void deleteNpc(int npcId)
	// {
	// if (getNpc(npcId) != null)
	// {
	// getNpc(npcId).getController().onDelete();
	// }
	// }
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
		removeEffects(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
		removeEffects(player);
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21345);
		effectController.removeEffect(21346);
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
		return TeleportService2.teleportTo(player, mapId, instanceId, 229.76685f, 172.37976f, 164.60033f, (byte) 30);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}