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
package system.handlers.ai.instance.illuminaryObelisk;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@AIName("eastern_shield_generator")
public class Eastern_Shield_GeneratorAI2 extends NpcAI2
{
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	protected void handleDialogStart(Player player)
	{
		if (player.getInventory().getFirstItemByItemId(164000289) != null)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
		else
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402211));
		}
	}
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			switch (getNpcId())
			{
				case 702010: // Eastern Shield Generator.
				{
					announceIDF5U3Defence01();
					break;
				}
			}
		}
	}
	
	private void announceIDF5U3Defence01()
	{
		getPosition().getWorldMapInstance().doOnAllPlayers(player ->
		{
			if (player.isOnline())
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDF5_U3_DEFENCE_01_ATTACKED);
			}
		});
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final int instanceId = getPosition().getInstanceId();
		switch (getNpcId())
		{
			case 702010: // Eastern Shield Generator.
			{
				switch (player.getWorldId())
				{
					case 301230000: // Illuminary Obelisk 4.5
					{
						if ((dialogId == 10000) && player.getInventory().decreaseByItemId(164000289, 3))
						{
							startWESG1();
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402224));
							final SpawnTemplate CastShadowPSSM = SpawnEngine.addNewSingleTimeSpawn(301230000, 702014, 255.7926f, 338.22058f, 325.56473f, (byte) 0);
							CastShadowPSSM.setEntityId(60);
							objects.put(702014, SpawnEngine.spawnObject(CastShadowPSSM, instanceId));
						}
						break;
					}
				}
				switch (player.getWorldId())
				{
					case 301370000: // [Infernal] Illuminary Obelisk 4.7
					{
						if ((dialogId == 10000) && player.getInventory().decreaseByItemId(164000289, 3))
						{
							startIWESG1();
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402224));
							final SpawnTemplate CastShadowPSSM = SpawnEngine.addNewSingleTimeSpawn(301370000, 702014, 255.7926f, 338.22058f, 325.56473f, (byte) 0);
							CastShadowPSSM.setEntityId(60);
							objects.put(702014, SpawnEngine.spawnObject(CastShadowPSSM, instanceId));
						}
						break;
					}
				}
				break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
	
	private void startWESG1()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			attackGenerator((Npc) spawn(233723, 252.68709f, 333.483f, 325.59268f, (byte) 90), 252.4865f, 296.63016f, 321.30084f, false);
			attackGenerator((Npc) spawn(233724, 255.74022f, 333.2762f, 325.49332f, (byte) 90), 255.46408f, 297.3457f, 321.3599f, false);
			attackGenerator((Npc) spawn(233725, 258.72256f, 333.27713f, 325.58722f, (byte) 90), 258.93884f, 295.81204f, 321.29742f, false);
		}, 1000);
	}
	
	private void startIWESG1()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			attackGenerator((Npc) spawn(234723, 252.68709f, 333.483f, 325.59268f, (byte) 90), 252.4865f, 296.63016f, 321.30084f, false);
			attackGenerator((Npc) spawn(234724, 255.74022f, 333.2762f, 325.49332f, (byte) 90), 255.46408f, 297.3457f, 321.3599f, false);
			attackGenerator((Npc) spawn(234725, 258.72256f, 333.27713f, 325.58722f, (byte) 90), 258.93884f, 295.81204f, 321.29742f, false);
		}, 1000);
	}
	
	private void attackGenerator(Npc npc, float x, float y, float z, boolean despawn)
	{
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
}