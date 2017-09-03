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
 * @author Romanz Hckd05
 */
public class TreasureAbyss
{
	private static final Logger log = LoggerFactory.getLogger(TreasureAbyss.class);
	private static List<float[]> floatArray = new ArrayList<>();
	private static final String ABYSS_EVENT_SCHEDULE = EventsConfig.ABYSS_EVENT_SCHEDULE;
	private static int WORLD_ID = 400010000;
	private static int NPC_ID = 801988;
	private static int[] rewards =
	{
		186000097,
		186000235,
		186000051,
		186000052,
		186000053,
		186000054,
		186000055,
		186000056,
		186000057,
		186000058,
		186000059,
		186000060,
		186000061,
		186000062,
		186000063,
		186000064,
		186000065,
		186000066,
		186000147,
		186000242,
		166000085,
		166000090,
		166000095,
		166000100,
		166000105,
		166000110,
		166000115
	};
	private static Npc mainN;
	
	public static void ScheduleCron()
	{
		CronService.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				startEvent();
			}
			
		}, ABYSS_EVENT_SCHEDULE);
		log.info("Treasure Abyss Event start to:" + EventsConfig.ABYSS_EVENT_SCHEDULE + " duration 30 min");
	}
	
	public static void startEvent()
	{
		initCoordinates();
		
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			
			@Override
			public void visit(Player object)
			{
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event Balaur Treasure Chest di mulai location Core cepat ikuti untuk mengambil hadiah!");
			}
		});
		
		initPig();
		
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			
			@Override
			public void run()
			{
				endEvent();
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
					ItemService.addItem(player, id, 1);
					World.getInstance().doOnAllPlayers(new Visitor<Player>()
					{
						
						@Override
						public void visit(Player object)
						{
							PacketSendUtility.sendYellowMessageOnCenter(object, player.getName() + "Saya menemuka Balaur Treasure Chest [item:%d]!");
						}
					});
				}
				mainN.getObserveController().removeObserver(this);
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
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event Balaur Treasure Chest telah selesai terimakasih atas partisipasinya!");
			}
		});
		
		mainN.getController().onDelete();
	}
	
	private static void initCoordinates()
	{
		floatArray.add(new float[]
		{
			1125.5845f,
			2092.2786f,
			2886.942f,
			0f
		});
		floatArray.add(new float[]
		{
			898.8214f,
			2055.8772f,
			2947.6978f,
			0f
		});
		floatArray.add(new float[]
		{
			1037.4642f,
			1913.2968f,
			2921.2324f,
			0f
		});
		floatArray.add(new float[]
		{
			1077.2566f,
			1884.0425f,
			2918.736f,
			0f
		});
		floatArray.add(new float[]
		{
			1081.3489f,
			1929.2251f,
			2918.9656f,
			0f
		});
		floatArray.add(new float[]
		{
			1052.0624f,
			1958.3436f,
			2914.1443f,
			0f
		});
		floatArray.add(new float[]
		{
			925.501f,
			1941.7659f,
			2900.174f,
			0f
		});
		floatArray.add(new float[]
		{
			910.02704f,
			1973.1254f,
			2926.4226f,
			0f
		});
		floatArray.add(new float[]
		{
			926.10034f,
			1912.359f,
			2922.1255f,
			0f
		});
		floatArray.add(new float[]
		{
			864.4592f,
			2132.6792f,
			2877.5903f,
			0f
		});
		floatArray.add(new float[]
		{
			813.31464f,
			2103.9673f,
			2904.7732f,
			0f
		});
		floatArray.add(new float[]
		{
			637.50116f,
			2190.0144f,
			2740.6816f,
			0f
		});
		floatArray.add(new float[]
		{
			659.5982f,
			2058.945f,
			2727.988f,
			0f
		});
		floatArray.add(new float[]
		{
			703.89545f,
			2009.956f,
			2732.8267f,
			0f
		});
		floatArray.add(new float[]
		{
			452.45828f,
			2054.4714f,
			2748.3901f,
			0f
		});
		floatArray.add(new float[]
		{
			879.64886f,
			2072.6255f,
			2986.4968f,
			0f
		});
		floatArray.add(new float[]
		{
			871.85144f,
			1966.9083f,
			2915.3523f,
			0f
		});
		floatArray.add(new float[]
		{
			1024.3107f,
			1924.4081f,
			2935.2603f,
			0f
		});
		floatArray.add(new float[]
		{
			1066.7672f,
			1907.3596f,
			2939.5806f,
			0f
		});
		floatArray.add(new float[]
		{
			1007.0575f,
			1909.2692f,
			2928.553f,
			0f
		});
		floatArray.add(new float[]
		{
			844.80316f,
			2223.1123f,
			2866.799f,
			0f
		});
		floatArray.add(new float[]
		{
			789.19257f,
			2296.9492f,
			2849.082f,
			0f
		});
		floatArray.add(new float[]
		{
			799.8223f,
			2342.593f,
			2857.1492f,
			0f
		});
		floatArray.add(new float[]
		{
			729.75464f,
			2390.949f,
			2903.714f,
			0f
		});
		floatArray.add(new float[]
		{
			1189.0593f,
			2034.7089f,
			2871.6875f,
			0f
		});
		floatArray.add(new float[]
		{
			1211.694f,
			2169.0952f,
			2881.6824f,
			0f
		});
		floatArray.add(new float[]
		{
			1146.1387f,
			2222.8052f,
			2893.3176f,
			0f
		});
		floatArray.add(new float[]
		{
			965.65607f,
			2079.0503f,
			2859.521f,
			0f
		});
		floatArray.add(new float[]
		{
			1070.2789f,
			2073.4214f,
			2868.2988f,
			0f
		});
		floatArray.add(new float[]
		{
			1080.5293f,
			2122.5925f,
			2868.7004f,
			0f
		});
	}
}
