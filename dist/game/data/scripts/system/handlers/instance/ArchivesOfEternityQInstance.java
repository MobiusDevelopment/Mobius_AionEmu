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
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;

/****/
/**
 * Author Rinzler (Encom) /** Source KOR: https://www.youtube.com/watch?v=8Qt-ZODwhoA /
 ****/

@InstanceID(301570000)
public class ArchivesOfEternityQInstance extends GeneralInstanceHandler
{
	private Map<Integer, StaticDoor> doors;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onEnterInstance(Player player)
	{
		sendMovie(player, 935);
	}
	
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
			 * MALE ELYOS
			 */
			case 857788: // IDEternity_Q_HD_Wind_Li_M_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Spring.
				sendMsg(1403365);
				spawn(857786, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Water_Li_M_N_65_An.
				break;
			case 857786: // IDEternity_Q_HD_Water_Li_M_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Earth.
				sendMsg(1403366);
				spawn(857787, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Earth_Li_M_N_65_An.
				break;
			case 857787: // IDEternity_Q_HD_Earth_Li_M_N_65_An.
				despawnNpc(npc);
				spawn(857785, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Fire_Li_M_N_65_An.
				break;
			case 857785: // IDEternity_Q_HD_Fire_Li_M_N_65_An.
				despawnNpc(npc);
				doors.get(90).setOpen(true);
				deleteNpc(703130); // IDEternity_Q_FOBJ_Q10521_D.
				sendMovie(player, 927);
				spawnIDEternityQ_Out_Portal_L();
				sendMsg("[Congratulation]: you are a <Archdaeva>");
				break;
			/**
			 * FEMALE ELYOS
			 */
			case 857795: // IDEternity_Q_HD_Wind_Li_F_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Spring.
				sendMsg(1403365);
				spawn(857793, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Water_Li_F_N_65_An.
				break;
			case 857793: // IDEternity_Q_HD_Water_Li_F_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Earth.
				sendMsg(1403366);
				spawn(857794, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Earth_Li_F_N_65_An.
				break;
			case 857794: // IDEternity_Q_HD_Earth_Li_F_N_65_An.
				despawnNpc(npc);
				spawn(857792, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Fire_Li_F_N_65_An.
				break;
			case 857792: // IDEternity_Q_HD_Fire_Li_F_N_65_An.
				despawnNpc(npc);
				doors.get(90).setOpen(true);
				deleteNpc(703130); // IDEternity_Q_FOBJ_Q10521_D.
				sendMovie(player, 928);
				spawnIDEternityQ_Out_Portal_L();
				sendMsg("[Congratulation]: you are a <Archdaeva>");
				break;
			/**
			 * MALE ASMODIANS
			 */
			case 857799: // IDEternity_Q_HD_Wind_Da_M_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Spring.
				sendMsg(1403365);
				spawn(857797, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Water_Da_M_N_65_An.
				break;
			case 857797: // IDEternity_Q_HD_Water_Da_M_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Earth.
				sendMsg(1403366);
				spawn(857798, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Earth_Da_M_N_65_An.
				break;
			case 857798: // IDEternity_Q_HD_Earth_Da_M_N_65_An.
				despawnNpc(npc);
				spawn(857796, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Fire_Da_M_N_65_An.
				break;
			case 857796: // IDEternity_Q_HD_Fire_Da_M_N_65_An.
				despawnNpc(npc);
				doors.get(90).setOpen(true);
				deleteNpc(703130); // IDEternity_Q_FOBJ_Q10521_D.
				sendMovie(player, 927);
				spawnIDEternityQ_Out_Portal_D();
				sendMsg("[Congratulation]: you are a <Archdaeva>");
				break;
			/**
			 * FEMALE ASMODIANS
			 */
			case 857803: // IDEternity_Q_HD_Wind_Da_F_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Spring.
				sendMsg(1403365);
				spawn(857801, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Water_Da_F_N_65_An.
				break;
			case 857801: // IDEternity_Q_HD_Water_Da_F_N_65_An.
				despawnNpc(npc);
				// You are graced with the aura of Blessed Earth.
				sendMsg(1403366);
				spawn(857802, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Earth_Da_F_N_65_An.
				break;
			case 857802: // IDEternity_Q_HD_Earth_Da_F_N_65_An.
				despawnNpc(npc);
				spawn(857800, 231.63109f, 511.9707f, 468.80215f, (byte) 0); // IDEternity_Q_HD_Fire_Da_F_N_65_An.
				break;
			case 857800: // IDEternity_Q_HD_Fire_Da_F_N_65_An.
				despawnNpc(npc);
				doors.get(90).setOpen(true);
				deleteNpc(703130); // IDEternity_Q_FOBJ_Q10521_D.
				sendMovie(player, 928);
				spawnIDEternityQ_Out_Portal_D();
				sendMsg("[Congratulation]: you are a <Archdaeva>");
				break;
		}
	}
	
	private void spawnIDEternityQ_Out_Portal_L()
	{
		final SpawnTemplate outPortal1 = SpawnEngine.addNewSingleTimeSpawn(301570000, 806179, 222.71474f, 511.98355f, 468.78000f, (byte) 0);
		outPortal1.setEntityId(35);
		objects.put(806179, SpawnEngine.spawnObject(outPortal1, instanceId));
	}
	
	private void spawnIDEternityQ_Out_Portal_D()
	{
		final SpawnTemplate outPortal2 = SpawnEngine.addNewSingleTimeSpawn(301570000, 806180, 222.71474f, 511.98355f, 468.78000f, (byte) 0);
		outPortal2.setEntityId(35);
		objects.put(806180, SpawnEngine.spawnObject(outPortal2, instanceId));
	}
	
	private void sendMsg(final String str)
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
	
	private void sendMovie(Player player, int movie)
	{
		if (!movies.contains(movie))
		{
			movies.add(movie);
			PacketSendUtility.sendPacket(player, new SM_PLAY_MOVIE(0, movie));
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		doors.clear();
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}