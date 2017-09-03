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
package com.aionemu.gameserver.services.thedevils;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.controllers.observer.ActionObserver;
import com.aionemu.gameserver.controllers.observer.ObserverType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Hckd05
 */
public class EventGebukMantan
{
	private static final Logger log = LoggerFactory.getLogger(EventGebukMantan.class);
	private static List<float[]> floatArray = new ArrayList<>();
	private static final String MANTAN_EVENT_SCHEDULE = EventsConfig.MANTAN_EVENT_SCHEDULE;
	private static int WORLD_ID = 600010000;
	private static int NPC_ID = 831573;
	private static int[] rewards =
	{
		166030005,
		186000253,
		186000242,
		186000253,
		162000107,
		169620062,
		188052637
	};
	private static Npc mainN;
	
	public static void ScheduleCron()
	{
		CronService.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				startEvent(); // To change body of generated methods, choose Tools | Templates.
			}
			
		}, MANTAN_EVENT_SCHEDULE);
		log.info("Gebuk Mantan Event start to:" + EventsConfig.MANTAN_EVENT_SCHEDULE + " duration 30 min");
	}
	
	public static void startEvent()
	{
		initCoordinates();
		
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			
			@Override
			public void visit(Player object)
			{
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event, Gebuk mantan di mulai location silentera cayon cepat ikuti untuk mengambil hadiah!");
			}
		});
		
		initPig();
		
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				endEvent(); // To change body of generated methods, choose Tools | Templates.
			}
		}, 30 * 60 * 1000);
		
	}
	
	private static void initPig()
	{
		final float[] coords = floatArray.get(Rnd.get(floatArray.size()));
		final SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(WORLD_ID, NPC_ID, coords[0], coords[1], coords[2], (byte) coords[3]);
		final VisibleObject mainObject = SpawnEngine.spawnObject(spawn, 1);
		if (mainObject instanceof Npc)
		{
			mainN = (Npc) mainObject;
		}
		final ActionObserver observer = new ActionObserver(ObserverType.ATTACKED)
		{
			
			@Override
			public void attacked(Creature creature)
			{
				if (creature instanceof Player)
				{
					final Player player = (Player) creature;
					final int id = rewards[Rnd.get(rewards.length)];
					ItemService.addItem(player, id, EventsConfig.MANTAN_EVENT_COUNT_REWARD);
					World.getInstance().doOnAllPlayers(new Visitor<Player>()
					{
						
						@Override
						public void visit(Player object)
						{
							PacketSendUtility.sendYellowMessageOnCenter(object, player.getName() + "Mantan di gebuk dan mendapatkan [item:%d]");
						}
					});
				}
				mainN.getObserveController().removeObserver(this);
				// mainN.setSpawn(null);
				mainN.getController().onDelete();
				initPig();
			}
		};
		if (mainN != null)
		{
			mainN.getObserveController().attach(observer);
		}
	}
	
	public static void endEvent()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			
			@Override
			public void visit(Player object)
			{
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event Gebuk mantan telah selesai terimakasih atas partisipasinya!");
			}
		});
		
		mainN.getController().onDelete();
	}
	
	private static void initCoordinates()
	{
		floatArray.add(new float[]
		{
			606.51355f,
			677.7499f,
			293.61987f,
			0f
		});
		floatArray.add(new float[]
		{
			739.3467f,
			856.41296f,
			293.67105f,
			0f
		});
		floatArray.add(new float[]
		{
			852.9203f,
			917.0743f,
			303.49966f,
			0f
		});
		floatArray.add(new float[]
		{
			929.9982f,
			803.11456f,
			296.6545f,
			0f
		});
		floatArray.add(new float[]
		{
			1029.5432f,
			963.2532f,
			309.9446f,
			0f
		});
		floatArray.add(new float[]
		{
			524.0727f,
			769.6795f,
			299.61633f,
			0f
		});
		floatArray.add(new float[]
		{
			591.7274f,
			493.83313f,
			312.47827f,
			0f
		});
		floatArray.add(new float[]
		{
			597.36584f,
			1049.0153f,
			307.95035f,
			0f
		});
		floatArray.add(new float[]
		{
			931.0463f,
			683.4869f,
			302.39893f,
			0f
		});
		// Event gebuk mantan untuk seseorang yang susah moveon
		floatArray.add(new float[]
		{
			471.76215f,
			556.68567f,
			312.0697f,
			0f
		});
		floatArray.add(new float[]
		{
			313.79654f,
			147.99403f,
			307.51978f,
			0f
		});
		floatArray.add(new float[]
		{
			724.89526f,
			765.0267f,
			292.55786f,
			0f
		});
		floatArray.add(new float[]
		{
			344.89658f,
			1147.9606f,
			306.45667f,
			0f
		});
		/*
		 * floatArray.add(new float[] { 1363.108f, 1464.0858f, 209.09084f, 0f } ); floatArray.add(new float[] { 1394.5568f, 1382.9987f, 208.08516f, 0f } ); floatArray.add(new float[] { 1516.5026f, 1407.1633f, 201.70367f, 0f } ); floatArray.add(new float[] { 1493.8002f, 1460.6648f, 176.9295f, 0f } );
		 * floatArray.add(new float[] { 1275.4224f, 1352.994f, 204.4608f, 0f } ); floatArray.add(new float[] { 1312.3104f, 1234.2994f, 214.66357f, 0f } ); floatArray.add(new float[] { 1235.8165f, 1170.0706f, 215.13603f, 0f } ); floatArray.add(new float[] { 1075.684f, 1065.3511f, 201.52081f, 0f } );
		 * floatArray.add(new float[] { 992.7121f, 1044.0405f, 201.52101f, 0f } ); floatArray.add(new float[] { 942.4062f, 1131.7365f, 206.84773f, 0f } ); floatArray.add(new float[] { 956.87994f, 1188.8582f, 201.4773f, 0f } );// floatArray.add(new float[] { 1226.273f, 1306.5815f, 208.125f, 0f } );
		 * floatArray.add(new float[] { 1016.5605f, 1518.2861f, 220.50787f, 0f } );
		 */
	}
}
