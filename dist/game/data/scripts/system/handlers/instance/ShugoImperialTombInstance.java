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
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

/****/
/**
 * Author Rinzler (Encom) /** Source: https://www.youtube.com/watch?v=UYCLUuaLVGI + http://aionpowerbook.com/powerbook/Shugo_Imperial_Tomb /
 ****/

@InstanceID(300560000)
public class ShugoImperialTombInstance extends GeneralInstanceHandler
{
	private Future<?> tombRaidTaskA1;
	private Future<?> tombRaidTaskB1;
	private Future<?> tombRaidTaskC1;
	private Future<?> tombRaidTaskC2;
	/////////////////////////////////
	private int strongKoboldWorker;
	private int diligentKoboldWorker;
	private int swiftKrallGraverobber;
	private int krallLookoutCommander;
	private boolean isInstanceDestroyed;
	
	/**
	 * Rewards (Update to 4.3!) Here is a list of items you can get. Note that you need one key for a small chest and three keys for a big chest. Ceranium Medals Mithril Medals Ancient Coins Abyssal Relics AP Crowns Blessed Manastone Socketing Supplement (Fabled) Blessed Manastone Socketing
	 * Supplement (Eternal) Blessed Manastone Socketing Supplement (Mythic) A temporary pet named Acarun, which produces Composite Manastones of L60 Crystal Shield A Cloud mount Dramata's Wings Dramata's Bone Wings
	 */
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 219508: // Diligent Kobold Worker.
			case 219514: // Strong Kobold Worker.
			case 219521: // Swift Krall Graverobber.
			case 219528: // Krall Lookout Commander.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002160, 2)); // Repair.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002158, 2)); // Cursed Chill.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002157, 2)); // Powerful Trickster's Essence.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002156, 2)); // Trickster's Essence.
						switch (Rnd.get(1, 4))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100087, 1)); // Treasure Room Map Piece 1.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100088, 1)); // Treasure Room Map Piece 2.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100089, 1)); // Treasure Room Map Piece 3.
								break;
							case 4:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100090, 1)); // Treasure Room Map Piece 4.
								break;
						}
					}
				}
				break;
			case 219530: // Letu Erezat.
			case 219531: // Captain Lediar.
			case 219544: // Awakened Guardian.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002160, 2)); // Repair.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002158, 2)); // Cursed Chill.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002157, 2)); // Powerful Trickster's Essence.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 164002156, 2)); // Trickster's Essence.
						switch (Rnd.get(1, 4))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100087, 1)); // Treasure Room Map Piece 1.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100088, 1)); // Treasure Room Map Piece 2.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100089, 1)); // Treasure Room Map Piece 3.
								break;
							case 4:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188100090, 1)); // Treasure Room Map Piece 4.
								break;
						}
						switch (Rnd.get(1, 2))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000129, 1)); // Common Treasure Chest Key.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 185000129, 2)); // Common Treasure Chest Key.
								break;
						}
					}
				}
				break;
			case 831122: // Emperor's Relic.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000147, 5)); // Mithril Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000242, 5)); // Ceramium Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182006999, 2)); // Shugo Coin.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						switch (Rnd.get(1, 7))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 187060085, 1)); // Dramata's Bone Wing.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100072, 1)); // Rosenimbus.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100078, 1)); // Goldenimbus.
								break;
							case 4:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100084, 1)); // Ceruleanimbus.
								break;
							case 5:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100090, 1)); // Crimsonimbus.
								break;
							case 6:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001602, 1)); // Mini Crystal Lucid Shield.
								break;
							case 7:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001603, 1)); // Hazy Crystal Shield.
								break;
						}
					}
				}
				break;
			case 831123: // Empress's Relic.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000147, 5)); // Mithril Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000242, 5)); // Ceramium Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182006999, 2)); // Shugo Coin.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						switch (Rnd.get(1, 7))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 187060084, 1)); // Dramata's Wing.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100072, 1)); // Rosenimbus.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100078, 1)); // Goldenimbus.
								break;
							case 4:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100084, 1)); // Ceruleanimbus.
								break;
							case 5:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100090, 1)); // Crimsonimbus.
								break;
							case 6:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001602, 1)); // Mini Crystal Lucid Shield.
								break;
							case 7:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001603, 1)); // Hazy Crystal Shield.
								break;
						}
					}
				}
				break;
			case 831124: // Crow Prince's Relic.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000147, 5)); // Mithril Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 186000242, 5)); // Ceramium Medal.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 182006999, 2)); // Shugo Coin.
				for (final Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						switch (Rnd.get(1, 7))
						{
							case 1:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190020060, 1)); // Pointytail Acarun Egg.
								break;
							case 2:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100072, 1)); // Rosenimbus.
								break;
							case 3:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100078, 1)); // Goldenimbus.
								break;
							case 4:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100084, 1)); // Ceruleanimbus.
								break;
							case 5:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 190100090, 1)); // Crimsonimbus.
								break;
							case 6:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001602, 1)); // Mini Crystal Lucid Shield.
								break;
							case 7:
								dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 115001603, 1)); // Hazy Crystal Shield.
								break;
						}
					}
				}
				break;
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		spawn(831110, 183.95969f, 237.51074f, 536.16974f, (byte) 71); // Crown Prince's Admirer.
	}
	
	@Override
	public void onEnterInstance(final Player player)
	{
		// Ancient Elite Shugo Warrior.
		SkillEngine.getInstance().applyEffectDirectly(21096, player, player, 3600000 * 1);
	}
	
	@Override
	public void onDie(Npc npc)
	{
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 219508: // Diligent Kobold Worker.
				diligentKoboldWorker++;
				if (diligentKoboldWorker == 12)
				{
					startTombRaidA1_1();
					// A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					// Hold a little longer and you will survive.
					sendMsgByRace(1402833, Race.PC_ALL, 5000);
					// Only a few enemies left!
					sendMsgByRace(1402834, Race.PC_ALL, 10000);
				}
				else if (diligentKoboldWorker == 24)
				{
					tombRaidTaskA1.cancel(true);
					spawn(831095, 344.28635f, 425.418f, 294.75867f, (byte) 56); // Shugo Warrior Transformation Device.
					spawn(831114, 183.95969f, 237.51074f, 536.16974f, (byte) 71); // Crown Prince's Delighted Admirer.
					spawn(831111, 340.27893f, 426.2435f, 294.7574f, (byte) 56); // Empress' Admirer.
				}
				break;
			case 219514: // Strong Kobold Worker.
				strongKoboldWorker++;
				if (strongKoboldWorker == 12)
				{
					startTombRaidB1_1();
					// A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					// Hold a little longer and you will survive.
					sendMsgByRace(1402833, Race.PC_ALL, 5000);
					// Only a few enemies left!
					sendMsgByRace(1402834, Race.PC_ALL, 10000);
				}
				else if (strongKoboldWorker == 24)
				{
					spawnFairyGuardian();
					tombRaidTaskB1.cancel(true);
					// Prepare for combat! More enemies swarming in!
					sendMsgByRace(1402832, Race.PC_ALL, 0);
				}
				break;
			case 219521: // Swift Krall Graverobber.
				swiftKrallGraverobber++;
				if (swiftKrallGraverobber == 12)
				{
					startTombRaidC1_1();
					// A second wave of pillagers will arrive in 10 seconds!
					sendMsgByRace(1401586, Race.PC_ALL, 0);
					// Hold a little longer and you will survive.
					sendMsgByRace(1402833, Race.PC_ALL, 5000);
					// Only a few enemies left!
					sendMsgByRace(1402834, Race.PC_ALL, 10000);
				}
				else if (swiftKrallGraverobber == 24)
				{
					startLetuErezat();
					tombRaidTaskC1.cancel(true);
					// Hold a little longer and you will survive.
					sendMsgByRace(1402833, Race.PC_ALL, 5000);
					// Only a few enemies left!
					sendMsgByRace(1402834, Race.PC_ALL, 10000);
				}
				break;
			case 219528: // Krall Lookout Commander.
				krallLookoutCommander++;
				if (krallLookoutCommander == 24)
				{
					startCaptainLediar();
					// Hold a little longer and you will survive.
					sendMsgByRace(1402833, Race.PC_ALL, 5000);
					// Only a few enemies left!
					sendMsgByRace(1402834, Race.PC_ALL, 10000);
					tombRaidTaskC2.cancel(true);
				}
				break;
			case 219530: // Letu Erezat.
				startTombRaidC1_2();
				// Hold a little longer and you will survive.
				sendMsgByRace(1402833, Race.PC_ALL, 0);
				// Only a few enemies left!
				sendMsgByRace(1402834, Race.PC_ALL, 5000);
				break;
			case 219531: // Captain Lediar.
				deleteNpc(831130); // Crown Prince's Monument.
				spawn(831116, 443.322f, 110.39832f, 212.20023f, (byte) 92); // Emperor's Delighted Admirer.
				spawn(831119, 440.2393f, 109.80865f, 212.20023f, (byte) 94); // Marayrinerk.
				spawn(831350, 452.43765f, 106.14462f, 212.20023f, (byte) 68); // Imperial Shrine.
				break;
			case 219544: // Awakened Guardian.
				spawn(831095, 465.13556f, 111.26043f, 214.702f, (byte) 8); // Shugo Warrior Transformation Device.
				spawn(831115, 329.33588f, 432.96265f, 294.76144f, (byte) 100); // Empress's Delighted Admirer.
				spawn(831112, 452.43765f, 106.14462f, 212.20023f, (byte) 68); // Emperor's Admirer.
				break;
		}
	}
	
	private void spawnFairyGuardian()
	{
		spawn(219544, 315.94565f, 431.73035f, 294.58875f, (byte) 116);
		spawn(219505, 314.94418f, 428.22006f, 294.58875f, (byte) 115);
		spawn(219505, 318.11328f, 427.6605f, 294.58875f, (byte) 115);
		spawn(219505, 319.69778f, 434.42917f, 294.58875f, (byte) 115);
		spawn(219505, 316.08636f, 435.35806f, 294.58875f, (byte) 115);
		spawn(219505, 321.05954f, 430.44263f, 294.58875f, (byte) 115);
	}
	
	private void tombRaid(final Npc npc, float x, float y, float z, boolean despawn)
	{
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	/**
	 * TOMB RAID A
	 */
	private void startTombRaidA1_1()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
			}
		}, 10000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 20000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
			}
		}, 30000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 40000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
			}
		}, 50000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 60000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
			}
		}, 70000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 80000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
			}
		}, 90000);
		tombRaidTaskA1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombARaidLeft();
				startTombARaidRight();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 100000);
	}
	
	private void startTombARaidLeft()
	{
		tombRaid((Npc) spawn(219508, 201.46498f, 279.1183f, 550.49426f, (byte) 78), 167.5225f, 239.59024f, 535.81213f, false);
		tombRaid((Npc) spawn(219509, 201.46498f, 279.1183f, 550.49426f, (byte) 78), 167.5225f, 239.59024f, 535.81213f, false);
		tombRaid((Npc) spawn(219510, 201.46498f, 279.1183f, 550.49426f, (byte) 78), 167.5225f, 239.59024f, 535.81213f, false);
	}
	
	private void startTombARaidRight()
	{
		tombRaid((Npc) spawn(219508, 220.23207f, 268.67975f, 550.4942f, (byte) 78), 189.45801f, 229.36856f, 535.81213f, false);
		tombRaid((Npc) spawn(219509, 220.23207f, 268.67975f, 550.4942f, (byte) 78), 189.45801f, 229.36856f, 535.81213f, false);
		tombRaid((Npc) spawn(219510, 220.23207f, 268.67975f, 550.4942f, (byte) 78), 189.45801f, 229.36856f, 535.81213f, false);
	}
	
	/**
	 * TOMB RAID B
	 */
	private void startTombRaidB1_1()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
			}
		}, 10000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 20000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
			}
		}, 30000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 40000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
			}
		}, 50000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 60000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
			}
		}, 70000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 80000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
			}
		}, 90000);
		tombRaidTaskB1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombBRaidLeft();
				startTombBRaidRight();
				startTombBRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 100000);
	}
	
	private void startTombBRaidLeft()
	{
		tombRaid((Npc) spawn(219514, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
		tombRaid((Npc) spawn(219515, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
		tombRaid((Npc) spawn(219516, 313.3913f, 443.98557f, 296.40808f, (byte) 13), 333.79936f, 453.9313f, 296.40808f, false);
	}
	
	private void startTombBRaidRight()
	{
		tombRaid((Npc) spawn(219514, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
		tombRaid((Npc) spawn(219515, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
		tombRaid((Npc) spawn(219516, 316.16348f, 431.76547f, 294.58875f, (byte) 78), 335.83118f, 427.14017f, 294.7588f, false);
	}
	
	private void startTombBRaidCenter()
	{
		tombRaid((Npc) spawn(219514, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
		tombRaid((Npc) spawn(219515, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
		tombRaid((Npc) spawn(219516, 307.97067f, 422.196f, 296.40808f, (byte) 78), 322.31113f, 405.27344f, 296.40808f, false);
	}
	
	/**
	 * TOMB RAID C-1
	 */
	private void startTombRaidC1_1()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
			}
		}, 10000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 20000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
			}
		}, 30000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 40000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
			}
		}, 50000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 60000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
			}
		}, 70000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 80000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
			}
		}, 90000);
		tombRaidTaskC1 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft();
				startTombCRaidRight();
				startTombCRaidCenter();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 100000);
	}
	
	private void startTombCRaidLeft()
	{
		tombRaid((Npc) spawn(219521, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
		tombRaid((Npc) spawn(219522, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
		tombRaid((Npc) spawn(219523, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
	}
	
	private void startTombCRaidRight()
	{
		tombRaid((Npc) spawn(219521, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
		tombRaid((Npc) spawn(219522, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
		tombRaid((Npc) spawn(219523, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
	}
	
	private void startTombCRaidCenter()
	{
		tombRaid((Npc) spawn(219521, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
		tombRaid((Npc) spawn(219522, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
		tombRaid((Npc) spawn(219523, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
	}
	
	/**
	 * TOMB RAID C-2
	 */
	private void startTombRaidC1_2()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
			}
		}, 10000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 20000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
			}
		}, 30000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 40000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
			}
		}, 50000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 60000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
			}
		}, 70000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 80000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
			}
		}, 90000);
		tombRaidTaskC2 = ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startTombCRaidLeft2();
				startTombCRaidRight2();
				startTombCRaidCenter2();
				// More pillagers will arrive in 5 seconds!
				sendMsgByRace(1401607, Race.PC_ALL, 0);
			}
		}, 100000);
	}
	
	private void startTombCRaidLeft2()
	{
		tombRaid((Npc) spawn(219527, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
		tombRaid((Npc) spawn(219528, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
		tombRaid((Npc) spawn(219529, 425.89014f, 58.281246f, 222.14124f, (byte) 15), 451.03403f, 84.020836f, 214.33578f, false);
	}
	
	private void startTombCRaidRight2()
	{
		tombRaid((Npc) spawn(219527, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
		tombRaid((Npc) spawn(219528, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
		tombRaid((Npc) spawn(219529, 395.60565f, 117.61483f, 222.1441f, (byte) 1), 433.06375f, 120.66558f, 214.33475f, false);
	}
	
	private void startTombCRaidCenter2()
	{
		tombRaid((Npc) spawn(219527, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
		tombRaid((Npc) spawn(219528, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
		tombRaid((Npc) spawn(219529, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false);
	}
	
	private void startLetuErezat()
	{
		tombRaid((Npc) spawn(219530, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false); // Letu Erezat.
	}
	
	private void startCaptainLediar()
	{
		tombRaid((Npc) spawn(219531, 419.153f, 91.08885f, 214.33856f, (byte) 9), 449.21487f, 104.769394f, 212.20023f, false); // Captain Lediar.
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 831095: // Shugo Warrior Transformation Device.
				SkillEngine.getInstance().getSkill(npc, 21096, 60, player).useNoAnimationSkill();
				break;
		}
	}
	
	public void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(182006989, storage.getItemCountByItemId(182006989)); // Emperor's Golden Tag.
		storage.decreaseByItemId(182006990, storage.getItemCountByItemId(182006990)); // Empress' Silver Tag.
		storage.decreaseByItemId(182006991, storage.getItemCountByItemId(182006991)); // Crown Prince's Brass Tag.
		storage.decreaseByItemId(182006999, storage.getItemCountByItemId(182006999)); // Shugo Coin.
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21096);
	}
	
	private void despawnNpc(Npc npc)
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
	
	protected void sendMsgByRace(final int msg, final Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				instance.doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
						{
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time);
	}
	
	@Override
	public boolean onDie(final Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}