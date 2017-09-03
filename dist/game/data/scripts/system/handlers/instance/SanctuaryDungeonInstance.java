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

import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import javolution.util.FastMap;

/****/
/**
 * Author Rinzler (Encom) /** Source KOR: https://www.youtube.com/watch?v=8Qt-ZODwhoA /
 ****/

@InstanceID(301580000)
public class SanctuaryDungeonInstance extends GeneralInstanceHandler
{
	private Race spawnRace;
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onEnterInstance(Player player)
	{
		switch (player.getRace())
		{
			case ELYOS:
				spawnIDF6_OP_L_Door_Out();
				break;
			case ASMODIANS:
				spawnIDF6_OP_D_Door_Out();
				break;
		}
		if (spawnRace == null)
		{
			spawnRace = player.getRace();
			SpawnSanctuaryDungeonRace();
		}
	}
	
	private void SpawnSanctuaryDungeonRace()
	{
		final int npc1 = spawnRace == Race.ASMODIANS ? 806080 : 806076;
		spawn(npc1, 432.54724f, 479.6076f, 99.59915f, (byte) 31);
	}
	
	private void spawnIDF6_OP_L_Door_Out()
	{
		final SpawnTemplate outPortal1 = SpawnEngine.addNewSingleTimeSpawn(301580000, 806189, 432.7019f, 475.63489f, 99.471016f, (byte) 0);
		outPortal1.setEntityId(20);
		objects.put(806189, SpawnEngine.spawnObject(outPortal1, instanceId));
	}
	
	private void spawnIDF6_OP_D_Door_Out()
	{
		final SpawnTemplate outPortal2 = SpawnEngine.addNewSingleTimeSpawn(301580000, 806190, 432.7019f, 475.63489f, 99.471016f, (byte) 0);
		outPortal2.setEntityId(22);
		objects.put(806190, SpawnEngine.spawnObject(outPortal2, instanceId));
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}