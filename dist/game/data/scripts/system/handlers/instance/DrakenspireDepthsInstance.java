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

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
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
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAY_MOVIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastMap;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(301390000)
public class DrakenspireDepthsInstance extends GeneralInstanceHandler
{
	private int deathChar;
	private Race sealSceneRace;
	private Map<Integer, StaticDoor> doors;
	protected boolean isInstanceDestroyed = false;
	private final List<Integer> movies = new ArrayList<>();
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		doors = instance.getDoors();
		// ** Breakwall Twin's Boss **//
		final SpawnTemplate IDsealFire1st = SpawnEngine.addNewSingleTimeSpawn(301390000, 702695, 558.32593f, 152.40855f, 1683.0303f, (byte) 0);
		IDsealFire1st.setEntityId(408);
		objects.put(702695, SpawnEngine.spawnObject(IDsealFire1st, instanceId));
		// ** Breakwall Twin's Boss **//
		final SpawnTemplate IDsealFire2st = SpawnEngine.addNewSingleTimeSpawn(301390000, 702696, 558.32593f, 212.02460f, 1683.0303f, (byte) 0);
		IDsealFire2st.setEntityId(409);
		objects.put(702696, SpawnEngine.spawnObject(IDsealFire2st, instanceId));
		switch (Rnd.get(1, 2))
		{
			case 1:
				spawn(236229, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); // Orissan.
				break;
			case 2:
				spawn(236232, 812.2527f, 568.7095f, 1701.0443f, (byte) 93); // Reverted Orissan.
				break;
		}
		switch (Rnd.get(1, 3))
		{
			case 1:
				spawn(236224, 850.943f, 343.2288f, 1723.6771f, (byte) 26); // Rapacious Kadena.
				break;
			case 2:
				spawn(236224, 879.0986f, 276.86612f, 1715.0032f, (byte) 76); // Rapacious Kadena.
				break;
			case 3:
				spawn(236224, 786.0551f, 363.54608f, 1697.079f, (byte) 86); // Rapacious Kadena.
				break;
		}
		switch (Rnd.get(1, 3))
		{
			case 1:
				spawn(236244, 151.88565f, 518.48145f, 1749.5945f, (byte) 9); // Beritra.
				break;
			case 2:
				spawn(236245, 151.88565f, 518.48145f, 1749.5945f, (byte) 9); // Enraged Beritra.
				break;
			case 3:
				spawn(236246, 151.88565f, 518.48145f, 1749.5945f, (byte) 9); // Crazed Beritra.
				break;
		}
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 236223: // Fetid Phantomscorch Chimera.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000219, 1)); // Crossroads Choice Key.
				break;
		}
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		super.onInstanceCreate(instance);
		if (sealSceneRace == null)
		{
			sealSceneRace = player.getRace();
			ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnIDSealScene01(), 20000);
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		final Player player = npc.getAggroList().getMostPlayerDamage();
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 855621: // Magma Glutten.
			case 855622: // Flamekite Geist.
				despawnNpc(npc);
				break;
			case 236106: // Deathchar Slayer.
			case 236109: // Elite Deathchar Patroler.
			case 236113: // Deathchar Slayer.
			case 236116: // Elite Deathchar Patroler.
				deathChar++;
				if (deathChar == 4)
				{
					// The Lava Protector and Heatvent Protector are sharing the Fount.
					sendMsgByRace(1402682, Race.PC_ALL, 0);
					// When both Protectors are defeated at the same time, the Fount is destroyed and the Protectors can no longer resurrect.
					sendMsgByRace(1402683, Race.PC_ALL, 10000);
					if (player != null)
					{
						switch (player.getRace())
						{
							case ELYOS:
								spawn(209686, 412.57935f, 177.6678f, 1684.2161f, (byte) 0);
								spawn(209687, 412.36823f, 187.22583f, 1684.2161f, (byte) 0);
								break;
							case ASMODIANS:
								spawn(209751, 412.57935f, 177.6678f, 1684.2161f, (byte) 0);
								spawn(209752, 412.36823f, 187.22583f, 1684.2161f, (byte) 0);
								break;
						}
					}
				}
				break;
			case 236221: // Fetid Deathchar Patroler.
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209684, 498.12088f, 206.94075f, 1688.1917f, (byte) 0);
								spawn(209684, 498.19083f, 215.10863f, 1688.1915f, (byte) 0);
								spawn(209685, 504.2284f, 211.17325f, 1688.1797f, (byte) 1);
							}, 5000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209749, 498.12088f, 206.94075f, 1688.1917f, (byte) 0);
								spawn(209749, 498.19083f, 215.10863f, 1688.1915f, (byte) 0);
								spawn(209750, 504.2284f, 211.17325f, 1688.1797f, (byte) 1);
							}, 5000);
							break;
					}
				}
				break;
			case 236222: // Fetid Deathchar Necromancer.
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209684, 497.9484f, 147.90346f, 1688.2479f, (byte) 1);
								spawn(209684, 498.11224f, 155.47922f, 1688.2467f, (byte) 0);
								spawn(209685, 504.3097f, 152.23334f, 1688.1984f, (byte) 0);
							}, 5000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209749, 497.9484f, 147.90346f, 1688.2479f, (byte) 1);
								spawn(209749, 498.11224f, 155.47922f, 1688.2467f, (byte) 0);
								spawn(209750, 504.3097f, 152.23334f, 1688.1984f, (byte) 0);
							}, 5000);
							break;
					}
				}
				break;
			case 236225: // Fountless Lava Protector.
				// The Protector that shares the Fount is still alive.
				sendMsgByRace(1402690, Race.PC_ALL, 0);
				// The vanquished Protector will be resurrected by the power of the Fount in 15 seconds.
				sendMsgByRace(1402691, Race.PC_ALL, 2000);
				// The Protector that shares the Fount has successfully resurrected.
				sendMsgByRace(1402692, Race.PC_ALL, 4000);
				break;
			case 236226: // Fountless Heatvent Protector.
				// The Protector that shares the Fount is still alive.
				sendMsgByRace(1402690, Race.PC_ALL, 0);
				// The vanquished Protector will be resurrected by the power of the Fount in 15 seconds.
				sendMsgByRace(1402691, Race.PC_ALL, 2000);
				// The Protector that shares the Fount has successfully resurrected.
				sendMsgByRace(1402692, Race.PC_ALL, 4000);
				break;
			case 236227: // Lava Protector.
			case 236228: // Heatvent Protector.
				deleteNpc(702404); // Twin's Firewall.
				deleteNpc(702695); // Breakwall Twin's Boss.
				deleteNpc(702696); // Breakwall Twin's Boss.
				// The Protectors' Fount has been destroyed and they will not be resurrected.
				sendMsgByRace(1402688, Race.PC_ALL, 0);
				// Obtain the Crossroads Choice Key carried by the Fetid Phantomscorch Master.
				sendMsgByRace(1403121, Race.PC_ALL, 60000);
				final Npc lavaProtector = instance.getNpc(236227); // Lava Protector.
				final Npc heatventProtector = instance.getNpc(236228); // Heatvent Protector.
				if (isDead(lavaProtector) && isDead(heatventProtector))
				{
					if (player != null)
					{
						switch (player.getRace())
						{
							case ELYOS:
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									spawn(209690, 552.93365f, 155.68227f, 1683.7301f, (byte) 0);
									spawn(209690, 552.90247f, 215.51768f, 1683.7301f, (byte) 0);
									spawn(209693, 552.98486f, 148.64922f, 1683.7301f, (byte) 1);
									spawn(209693, 553.1255f, 208.44653f, 1683.7301f, (byte) 1);
								}, 2000);
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									spawn(209694, 583.8417f, 177.45332f, 1683.7301f, (byte) 8);
									spawn(209695, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
									final Npc PCGuard_Li_Talk_A = getNpc(209695);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402727, PCGuard_Li_Talk_A.getObjectId(), 0, 1500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402728, PCGuard_Li_Talk_A.getObjectId(), 0, 3500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402729, PCGuard_Li_Talk_A.getObjectId(), 0, 5500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Li_Talk_A, 1402730, PCGuard_Li_Talk_A.getObjectId(), 0, 7500);
								}, 30000);
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									deleteNpc(209694);
									deleteNpc(209695);
									// Detachment Demolisher has opened the path to the next area.
									sendMsgByRace(1402689, Race.PC_ALL, 0);
									spawn(209698, 583.8417f, 177.45332f, 1683.7301f, (byte) 8);
									spawn(209700, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
								}, 40000);
								break;
							case ASMODIANS:
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									spawn(209755, 552.93365f, 155.68227f, 1683.7301f, (byte) 0);
									spawn(209755, 552.90247f, 215.51768f, 1683.7301f, (byte) 0);
									spawn(209758, 552.98486f, 148.64922f, 1683.7301f, (byte) 1);
									spawn(209758, 553.1255f, 208.44653f, 1683.7301f, (byte) 1);
								}, 2000);
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									spawn(209759, 583.8417f, 177.45332f, 1683.7301f, (byte) 8);
									spawn(209760, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
									final Npc PCGuard_Da_Talk_A = getNpc(209760);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402727, PCGuard_Da_Talk_A.getObjectId(), 0, 1500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402728, PCGuard_Da_Talk_A.getObjectId(), 0, 3500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402729, PCGuard_Da_Talk_A.getObjectId(), 0, 5500);
									NpcShoutsService.getInstance().sendMsg(PCGuard_Da_Talk_A, 1402730, PCGuard_Da_Talk_A.getObjectId(), 0, 7500);
								}, 30000);
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									deleteNpc(209759);
									deleteNpc(209760);
									// Detachment Demolisher has opened the path to the next area.
									sendMsgByRace(1402689, Race.PC_ALL, 0);
									spawn(209763, 583.8417f, 177.45332f, 1683.7301f, (byte) 8);
									spawn(209765, 582.48083f, 183.74684f, 1683.7301f, (byte) 116);
								}, 40000);
								break;
						}
					}
				}
				break;
			case 236159: // Phantomscorch Bonerival.
			case 236162: // Elite Phantomscorch Chimera.
			case 236165: // Phantomscorch Contender.
				final Npc phantomscorchBonerival = instance.getNpc(236159); // Phantomscorch Bonerival.
				final Npc elitePhantomscorchChimera = instance.getNpc(236162); // Elite Phantomscorch Chimera.
				final Npc phantomscorchContender = instance.getNpc(236165); // Phantomscorch Contender.
				if (isDead(phantomscorchBonerival) && isDead(elitePhantomscorchChimera) && isDead(phantomscorchContender))
				{
					if (player != null)
					{
						switch (player.getRace())
						{
							case ELYOS:
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									// Orissan begins to Ascend through Ascension Dominance.
									sendMsgByRace(1402693, Race.PC_ALL, 0);
									// Orissan has Ascended through Ascension Dominance.
									sendMsgByRace(1402694, Race.PC_ALL, 8000);
									// Slay Orissan before the next Ascension Dominance begins.
									sendMsgByRace(1402698, Race.PC_ALL, 16000);
									spawn(209704, 821.6019f, 523.7112f, 1706.6428f, (byte) 33);
									spawn(209705, 815.1346f, 522.75665f, 1706.7778f, (byte) 32);
								}, 2000);
								break;
							case ASMODIANS:
								ThreadPoolManager.getInstance().schedule((Runnable) () ->
								{
									// Orissan begins to Ascend through Ascension Dominance.
									sendMsgByRace(1402693, Race.PC_ALL, 0);
									// Orissan has Ascended through Ascension Dominance.
									sendMsgByRace(1402694, Race.PC_ALL, 8000);
									// Slay Orissan before the next Ascension Dominance begins.
									sendMsgByRace(1402698, Race.PC_ALL, 16000);
									spawn(209769, 821.6019f, 523.7112f, 1706.6428f, (byte) 33);
									spawn(209770, 815.1346f, 522.75665f, 1706.7778f, (byte) 32);
								}, 2000);
								break;
						}
					}
				}
				break;
			case 236229: // Orissan.
			case 236232: // Reverted Orissan.
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							sendMovie(player, 914);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209706, 810.4212f, 550.19934f, 1701.044f, (byte) 31);
								spawn(209707, 818.40704f, 552.7704f, 1701.044f, (byte) 36);
							}, 10000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209710, 807.78894f, 578.7186f, 1701.0446f, (byte) 34);
								spawn(209710, 815.84827f, 579.7431f, 1701.0446f, (byte) 30);
								spawn(209711, 814.1693f, 588.4347f, 1701.0449f, (byte) 34);
								spawn(209711, 806.99536f, 587.9815f, 1701.0448f, (byte) 30);
							}, 15000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209712, 811.5f, 583.0642f, 1701.0447f, (byte) 32);
								spawn(209713, 810.85767f, 588.2299f, 1701.0449f, (byte) 32);
								final Npc PCGuard_Li = getNpc(209713);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402727, PCGuard_Li.getObjectId(), 0, 1500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402728, PCGuard_Li.getObjectId(), 0, 3500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402729, PCGuard_Li.getObjectId(), 0, 5500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Li, 1402730, PCGuard_Li.getObjectId(), 0, 7500);
							}, 20000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								deleteNpc(700546);
								// Glide at the Soulfade Labyrinth to use the wind road.
								sendMsgByRace(1402941, Race.PC_ALL, 0);
							}, 30000);
							break;
						case ASMODIANS:
							sendMovie(player, 914);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209771, 810.4212f, 550.19934f, 1701.044f, (byte) 31);
								spawn(209772, 818.40704f, 552.7704f, 1701.044f, (byte) 36);
							}, 10000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209775, 807.78894f, 578.7186f, 1701.0446f, (byte) 34);
								spawn(209775, 815.84827f, 579.7431f, 1701.0446f, (byte) 30);
								spawn(209776, 814.1693f, 588.4347f, 1701.0449f, (byte) 34);
								spawn(209776, 806.99536f, 587.9815f, 1701.0448f, (byte) 30);
							}, 15000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209777, 811.5f, 583.0642f, 1701.0447f, (byte) 32);
								spawn(209778, 810.85767f, 588.2299f, 1701.0449f, (byte) 32);
								final Npc PCGuard_Da = getNpc(209778);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402727, PCGuard_Da.getObjectId(), 0, 1500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402728, PCGuard_Da.getObjectId(), 0, 3500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402729, PCGuard_Da.getObjectId(), 0, 5500);
								NpcShoutsService.getInstance().sendMsg(PCGuard_Da, 1402730, PCGuard_Da.getObjectId(), 0, 7500);
							}, 20000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								deleteNpc(700546);
								// Glide at the Soulfade Labyrinth to use the wind road.
								sendMsgByRace(1402941, Race.PC_ALL, 0);
							}, 30000);
							break;
					}
				}
				break;
			case 236166: // Phantomscorch Bonerival.
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209722, 631.6413f, 847.15717f, 1599.8486f, (byte) 90);
								spawn(209722, 639.45526f, 847.40265f, 1599.9614f, (byte) 90);
							}, 5000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209720, 635.39325f, 886.9716f, 1600.7146f, (byte) 90);
								spawn(209721, 635.0717f, 901.48553f, 1600.49f, (byte) 89);
								spawn(209731, 640.8788f, 891.53687f, 1600.5566f, (byte) 94);
								spawn(209731, 629.2198f, 891.1963f, 1600.5817f, (byte) 92);
							}, 10000);
							ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnIDSeal4ThStageElyos(), 12000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawnEmpyreanLordsSiegeWeapon();
								// The Empyrean Lord's Stormcannon is being charged for the Empyrean Firestorm.
								sendMsgByRace(1402703, Race.PC_ALL, 0);
								// Defend the Detachment and its siege weapons from the Guhena Legion.
								sendMsgByRace(1402705, Race.PC_ALL, 5000);
								// If the Detachment loses too many soldiers, they will not be able to assist during the battle against Beritra.
								sendMsgByRace(1402706, Race.PC_ALL, 10000);
							}, 15000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawnWaveDoor();
								// The Guhena Legion has detected intruders and will begin attacking.
								sendMsgByRace(1402704, Race.PC_ALL, 0);
							}, 25000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal1();
								doors.get(271).setOpen(true);
								// The Guhena Legion's Commander Virtsha has appeared. You must defeat every captain and commander.
								sendMsgByRace(1402710, Race.PC_ALL, 0);
							}, 30000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal2();
								// The Guhena Legion's second wave of attack has started. There will be three more attack waves.
								sendMsgByRace(1402707, Race.PC_ALL, 0);
							}, 65000);
							ThreadPoolManager.getInstance().schedule((Runnable) () -> startRaidSeal2_1(), 67000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal3();
								doors.get(267).setOpen(true);
								// The Guhena Legion's third wave of attack has started. There will be two more attack waves.
								sendMsgByRace(1402708, Race.PC_ALL, 0);
							}, 87000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209787, 631.6413f, 847.15717f, 1599.8486f, (byte) 90);
								spawn(209787, 639.45526f, 847.40265f, 1599.9614f, (byte) 90);
							}, 5000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawn(209785, 635.39325f, 886.9716f, 1600.7146f, (byte) 90);
								spawn(209786, 635.0717f, 901.48553f, 1600.49f, (byte) 89);
								spawn(209796, 640.8788f, 891.53687f, 1600.5566f, (byte) 94);
								spawn(209796, 629.2198f, 891.1963f, 1600.5817f, (byte) 92);
							}, 10000);
							ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnIDSeal4ThStageAsmodians(), 12000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawnEmpyreanLordsSiegeWeapon();
								// The Empyrean Lord's Stormcannon is being charged for the Empyrean Firestorm.
								sendMsgByRace(1402703, Race.PC_ALL, 0);
								// Defend the Detachment and its siege weapons from the Guhena Legion.
								sendMsgByRace(1402705, Race.PC_ALL, 5000);
								// If the Detachment loses too many soldiers, they will not be able to assist during the battle against Beritra.
								sendMsgByRace(1402706, Race.PC_ALL, 10000);
							}, 15000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								spawnWaveDoor();
								// The Guhena Legion has detected intruders and will begin attacking.
								sendMsgByRace(1402704, Race.PC_ALL, 0);
							}, 25000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal1();
								doors.get(271).setOpen(true);
								// The Guhena Legion's Commander Virtsha has appeared. You must defeat every captain and commander.
								sendMsgByRace(1402710, Race.PC_ALL, 0);
							}, 30000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal2();
								// The Guhena Legion's second wave of attack has started. There will be three more attack waves.
								sendMsgByRace(1402707, Race.PC_ALL, 0);
							}, 65000);
							ThreadPoolManager.getInstance().schedule((Runnable) () -> startRaidSeal2_1(), 67000);
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal3();
								doors.get(267).setOpen(true);
								// The Guhena Legion's third wave of attack has started. There will be two more attack waves.
								sendMsgByRace(1402708, Race.PC_ALL, 0);
							}, 87000);
							break;
					}
				}
				break;
			case 236235: // Flamesquelch Command Destroyer.
				despawnNpc(npc);
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal4();
								startRaidSeal5();
								doors.get(7).setOpen(true);
								doors.get(310).setOpen(true);
								// The Guhena Legion's fourth wave of attack has started. There will be one more attack wave.
								sendMsgByRace(1402709, Race.PC_ALL, 0);
							}, 15000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal4();
								startRaidSeal5();
								doors.get(7).setOpen(true);
								doors.get(310).setOpen(true);
								// The Guhena Legion's fourth wave of attack has started. There will be one more attack wave.
								sendMsgByRace(1402709, Race.PC_ALL, 0);
							}, 15000);
							break;
					}
				}
				break;
			case 236236: // Flamesquelch Command Destroyer.
				despawnNpc(npc);
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal6();
								doors.get(210).setOpen(true);
								doors.get(312).setOpen(true);
								// The Detachment has suffered severe losses and will not be able to assist any further.
								sendMsgByRace(1402712, Race.PC_ALL, 0);
							}, 15000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal6();
								doors.get(210).setOpen(true);
								doors.get(312).setOpen(true);
								// The Detachment has suffered severe losses and will not be able to assist any further.
								sendMsgByRace(1402712, Race.PC_ALL, 0);
							}, 15000);
							break;
					}
				}
				break;
			case 236237: // Flamesquelch Command Sorcerer.
				despawnNpc(npc);
				if (player != null)
				{
					switch (player.getRace())
					{
						case ELYOS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal3();
								startRaidSeal4();
								// The Detachment has suffered heavy losses and can only assist in limited capacity.
								sendMsgByRace(1402713, Race.PC_ALL, 0);
							}, 15000);
							break;
						case ASMODIANS:
							ThreadPoolManager.getInstance().schedule((Runnable) () ->
							{
								startRaidSeal3();
								startRaidSeal4();
								// The Detachment has suffered heavy losses and can only assist in limited capacity.
								sendMsgByRace(1402713, Race.PC_ALL, 0);
							}, 15000);
							break;
					}
				}
				break;
			case 236238: // Flamesquelch Command Burnsmark.
				despawnNpc(npc);
				// The Detachment has suffered some losses, but can assist at almost full capacity.
				sendMsgByRace(1402714, Race.PC_ALL, 0);
				// You have successfully protected the Detachment. They will assist you during the battle against Beritra.
				sendMsgByRace(1402715, Race.PC_ALL, 10000);
				ThreadPoolManager.getInstance().schedule((Runnable) () ->
				{
					deleteNpc(700545);
					spawnEternalAltarOfTormentEntrance();
					// The Empyrean Lord's Stormcannon has blown open the Seal of Darkness.
					sendMsgByRace(1402711, Race.PC_ALL, 0);
				}, 20000);
				break;
			case 236204: // Flamesquelch Destoyer.
			case 236205: // Flamesquelch Burnsmark.
			case 236206: // Flamesquelch Rangerblaze.
			case 236216: // Elite Flamesquelch Destroyer.
			case 236217: // Elite Flamesquelch Burnsmark.
			case 236218: // Elite Flamesquelch Rangerblaze.
			case 236219: // Elite Flamesquelch Sorcerer.
			case 236220: // Elite Flamesquelch Extinguisher.
				despawnNpc(npc);
				break;
			case 236247: // Beritra [Dragon Form]
				despawnNpc(npc);
				spawn(731548, 147.01088f, 517.9374f, 1749.5007f, (byte) 2); // Drakenspire Depths Exit.
				spawn(702769, 152.10716f, 518.6436f, 1749.5945f, (byte) 68); // Ominous Darkness.
				spawn(731578, 152.10033f, 518.63507f, 1749.605f, (byte) 0, 211);
				spawn(833012, 152.30666f, 522.0088f, 1749.5466f, (byte) 88); // Cloak Of Balaur Lord Beritra.
				spawn(833015, 161.93251f, 518.47626f, 1749.4482f, (byte) 59); // Sacred Beast Of Balaur Lord Beritra.
				sendMsg("[Congratulation]: you finish <Drakenspire Depths>");
				sendMovie(player, 916);
				ThreadPoolManager.getInstance().schedule((Runnable) () -> spawnIDSealSceneEnding(), 20000);
				break;
		}
	}
	
	private void spawnIDSealScene01()
	{
		final int pcGuard1 = sealSceneRace == Race.ASMODIANS ? 209744 : 209679;
		final int pcGuard2 = sealSceneRace == Race.ASMODIANS ? 209744 : 209679;
		final int pcGuard3 = sealSceneRace == Race.ASMODIANS ? 209745 : 209680;
		final int pcGuard4 = sealSceneRace == Race.ASMODIANS ? 209746 : 209681;
		spawn(pcGuard1, 353.3932f, 185.61818f, 1684.2164f, (byte) 1);
		spawn(pcGuard2, 353.4143f, 179.24579f, 1684.2164f, (byte) 0);
		spawn(pcGuard3, 349.18713f, 192.32655f, 1684.2164f, (byte) 119);
		spawn(pcGuard4, 349.34637f, 173.6827f, 1684.2164f, (byte) 1);
	}
	
	private void spawnIDSealSceneEnding()
	{
		final int IDSealSceneEndingQuestNPC = sealSceneRace == Race.ASMODIANS ? 209804 : 209739; // Parsia/Masionel.
		final int IDSealSceneEndingPCGuard1 = sealSceneRace == Race.ASMODIANS ? 209807 : 209742;
		final int IDSealSceneEndingPCGuard2 = sealSceneRace == Race.ASMODIANS ? 209807 : 209742;
		final int IDSealSceneEndingPCGuard3 = sealSceneRace == Race.ASMODIANS ? 209807 : 209742;
		final int IDSealSceneEndingPCGuard4 = sealSceneRace == Race.ASMODIANS ? 209807 : 209742;
		spawn(IDSealSceneEndingQuestNPC, 156.73125f, 518.6457f, 1749.5138f, (byte) 0);
		spawn(IDSealSceneEndingPCGuard1, 155.51422f, 514.9752f, 1749.5044f, (byte) 106);
		spawn(IDSealSceneEndingPCGuard2, 155.3807f, 521.9788f, 1749.5118f, (byte) 15);
		spawn(IDSealSceneEndingPCGuard3, 148.80602f, 521.83466f, 1749.516f, (byte) 45);
		spawn(IDSealSceneEndingPCGuard4, 148.60063f, 515.0635f, 1749.5034f, (byte) 75);
	}
	
	private void spawnEmpyreanLordsSiegeWeapon()
	{
		final int empyreanLordsSiegeWeapon = sealSceneRace == Race.ASMODIANS ? 702720 : 702719; // Empyrean Lord's Siege Weapon.
		spawn(empyreanLordsSiegeWeapon, 635.24457f, 890.4639f, 1600.5914f, (byte) 90);
	}
	
	private void spawnEternalAltarOfTormentEntrance()
	{
		final SpawnTemplate EnvSkyBoxObject = SpawnEngine.addNewSingleTimeSpawn(301390000, 804697, 635.69067f, 959.46039f, 1615.0714f, (byte) 0);
		EnvSkyBoxObject.setEntityId(50);
		objects.put(804697, SpawnEngine.spawnObject(EnvSkyBoxObject, instanceId));
	}
	
	private void spawnWaveDoor()
	{
		final SpawnTemplate AionFXPostGlow = SpawnEngine.addNewSingleTimeSpawn(301390000, 731581, 635.3889f, 784.05261f, 1596.7184f, (byte) 0);
		AionFXPostGlow.setEntityId(548);
		objects.put(731581, SpawnEngine.spawnObject(AionFXPostGlow, instanceId));
	}
	
	private void moveToSealForward(Npc npc, float x, float y, float z, boolean despawn)
	{
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void spawnIDSeal4ThStageElyos()
	{
		moveToSealForward((Npc) spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 632.21173f, 887.62164f, 1600.698f, false);
		moveToSealForward((Npc) spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.4854f, 889.0385f, 1600.6517f, false);
		moveToSealForward((Npc) spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.0168f, 896.49756f, 1600.4114f, false);
		moveToSealForward((Npc) spawn(209722, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 631.50134f, 895.16174f, 1600.5238f, false);
	}
	
	private void spawnIDSeal4ThStageAsmodians()
	{
		moveToSealForward((Npc) spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 632.21173f, 887.62164f, 1600.698f, false);
		moveToSealForward((Npc) spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.4854f, 889.0385f, 1600.6517f, false);
		moveToSealForward((Npc) spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 638.0168f, 896.49756f, 1600.4114f, false);
		moveToSealForward((Npc) spawn(209787, 636.07764f, 846.96954f, 1599.9142f, (byte) 30), 631.50134f, 895.16174f, 1600.5238f, false);
	}
	
	private void raidSeal(Npc npc)
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
	
	public void startRaidSeal1()
	{
		raidSeal((Npc) spawn(236204, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc) spawn(236204, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc) spawn(236205, 686.8446f, 823.1334f, 1610.0796f, (byte) 46));
		raidSeal((Npc) spawn(236205, 689.8247f, 826.4868f, 1610.1107f, (byte) 46));
	}
	
	public void startRaidSeal2()
	{
		raidSeal((Npc) spawn(236206, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc) spawn(236206, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc) spawn(236235, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal2_1()
	{
		raidSeal((Npc) spawn(236206, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc) spawn(236206, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc) spawn(236206, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal3()
	{
		raidSeal((Npc) spawn(236216, 686.8446f, 823.1334f, 1610.0796f, (byte) 46));
		raidSeal((Npc) spawn(236216, 689.8247f, 826.4868f, 1610.1107f, (byte) 46));
		raidSeal((Npc) spawn(236216, 579.17377f, 823.6274f, 1609.9344f, (byte) 14));
		raidSeal((Npc) spawn(236216, 582.5257f, 820.4343f, 1609.9154f, (byte) 15));
	}
	
	public void startRaidSeal4()
	{
		raidSeal((Npc) spawn(236220, 632.9971f, 788.14307f, 1596.5493f, (byte) 28));
		raidSeal((Npc) spawn(236220, 637.02356f, 787.7114f, 1596.4082f, (byte) 29));
		raidSeal((Npc) spawn(236220, 635.31866f, 789.59717f, 1596.6062f, (byte) 30));
	}
	
	public void startRaidSeal5()
	{
		raidSeal((Npc) spawn(236218, 574.2141f, 879.9431f, 1600.7627f, (byte) 0));
		raidSeal((Npc) spawn(236218, 574.22955f, 875.5384f, 1601.1173f, (byte) 0));
		raidSeal((Npc) spawn(236219, 703.2663f, 874.8648f, 1604.521f, (byte) 59));
		raidSeal((Npc) spawn(236219, 703.3903f, 880.198f, 1604.7985f, (byte) 62));
		raidSeal((Npc) spawn(236236, 702.1438f, 877.7707f, 1604.4375f, (byte) 59));
	}
	
	public void startRaidSeal6()
	{
		raidSeal((Npc) spawn(236217, 576.9567f, 939.24054f, 1620.987f, (byte) 104));
		raidSeal((Npc) spawn(236217, 573.4911f, 935.99945f, 1621.0607f, (byte) 104));
		raidSeal((Npc) spawn(236237, 575.50415f, 937.4111f, 1620.9528f, (byte) 106));
		raidSeal((Npc) spawn(236217, 687.77435f, 933.2536f, 1617.8989f, (byte) 68));
		raidSeal((Npc) spawn(236217, 689.8956f, 929.0121f, 1617.4075f, (byte) 68));
		raidSeal((Npc) spawn(236238, 688.66656f, 931.058f, 1617.5339f, (byte) 72));
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
	
	void deleteNpc(int npcId)
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
	
	@Override
	protected Npc getNpc(int npcId)
	{
		if (!isInstanceDestroyed)
		{
			return instance.getNpc(npcId);
		}
		return null;
	}
	
	private boolean isDead(Npc npc)
	{
		return ((npc == null) || npc.getLifeStats().isAlreadyDead());
	}
	
	@Override
	public void onInstanceDestroy()
	{
		doors.clear();
		isInstanceDestroyed = true;
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
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 326.8173f, 183.01366f, 1687.2552f, (byte) 1);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}