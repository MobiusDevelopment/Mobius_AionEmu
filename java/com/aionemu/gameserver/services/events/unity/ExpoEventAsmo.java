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
package com.aionemu.gameserver.services.events.unity;

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

/**
 * @author Hckd05
 */
public class ExpoEventAsmo
{
	private static final Logger log = LoggerFactory.getLogger(ExpoEventAsmo.class);
	private static List<float[]> floatArray = new ArrayList<>();
	private static final String BABI_EVENT_SCHEDULE_ASMO = EventsConfig.BABI_EVENT_SCHEDULE_ASMO;
	private static int WORLD_ID = 120010000;
	private static int NPC_ID = 219158;
	static int[] rewards =
	{
		164002096,
		164002097,
		164002093,
		164002094,
		164002099,
		164002100,
		164002093,
		164002094,
		164002099,
		164002100,
		186000030,
		186000031,
		164000076,
		164000134,
		164000073,
		186000143,
		162000080,
		161000003,
		162000077,
		162000078,
		186000030,
		186000031,
		164000076,
		164000134,
		164000073,
		186000143,
		162000080,
		160009014,
		162000077,
		162000078,
		186000030,
		186000031,
		164000076,
		186000096,
		186000147,
		186000143,
		162000080,
		166000090,
		166000100,
		166000110,
		186000030,
		186000031,
		164000076,
		164000134,
		164000073,
		186000143,
		162000080,
		161000003,
		162000077,
		162000078,
		164002096,
		164002097,
		164002093,
		164002094,
		164002099,
		164002100,
		164002093,
		164002094,
		164002099,
		164002100,
		186000030,
		186000031,
		164000076,
		164000134,
		164000073,
		186000143,
		162000080,
		160009016,
		162000077,
		162000078,
		186000030,
		186000031,
		164000076,
		186000096,
		186000147,
		186000143,
		162000080,
		166000090,
		166000100,
		166000110,
		164002096,
		164002097,
		164002093,
		164002094,
		164002099,
		164002100,
		164002093,
		164002094,
		164002099,
		164002100,
		188051508,
		188051509,
		188051510,
		188051396,
		188051411,
		188051412,
		188051416,
		188051389,
		188051430,
		188051395,
		188051398,
		164002096,
		164002097,
		164002093,
		164002094,
		164002099,
		164002100,
		164002093,
		164002094,
		164002099,
		164002100,
		188052074,
		188052075,
		188052718,
		188052438,
		188100099
	};
	static Npc mainN;
	
	public static void ScheduleCron()
	{
		CronService.getInstance().schedule(() -> startEvent(), BABI_EVENT_SCHEDULE_ASMO);
		log.info("Pig Event start to:" + EventsConfig.BABI_EVENT_SCHEDULE_ASMO + " duration 30 min");
	}
	
	public static void startEvent()
	{
		initCoordinates();
		
		World.getInstance().doOnAllPlayers(object -> PacketSendUtility.sendYellowMessageOnCenter(object, "Event, look for wild boar in the start location Pandaemonium quickly follow to take the prize!"));
		
		initPig();
		
		ThreadPoolManager.getInstance().schedule((Runnable) () -> endEvent(), 30 * 60 * 1000);
		
	}
	
	static void initPig()
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
					ItemService.addItem(player, id, EventsConfig.BABI_EVENT_COUNT_REWARD);
					World.getInstance().doOnAllPlayers(object -> PacketSendUtility.sendYellowMessageOnCenter(object, "Menemukan babi hutan dan mendapatkan [item:%d]"));
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
		World.getInstance().doOnAllPlayers(object -> PacketSendUtility.sendYellowMessageOnCenter(object, "The search for a wild boar has been completed. Thanks for the participation!"));
		
		mainN.getController().onDelete();
	}
	
	private static void initCoordinates()
	{
		floatArray.add(new float[]
		{
			1632.9166f,
			1424.3572f,
			193.127f,
			0f
		});
		floatArray.add(new float[]
		{
			1486.164f,
			1336.1799f,
			176.9295f,
			0f
		});
		floatArray.add(new float[]
		{
			1449.3718f,
			1314.1283f,
			195.35262f,
			0f
		});
		floatArray.add(new float[]
		{
			1367.562f,
			1217.9772f,
			208.37625f,
			0f
		});
		floatArray.add(new float[]
		{
			1336.7334f,
			1052.437f,
			206.088f,
			0f
		});
		floatArray.add(new float[]
		{
			1377.034f,
			1065.1808f,
			214.92575f,
			0f
		});
		floatArray.add(new float[]
		{
			1224.6565f,
			1105.8811f,
			208.88635f,
			0f
		});
		floatArray.add(new float[]
		{
			1161.7792f,
			1260.4316f,
			209.67445f,
			0f
		});
		floatArray.add(new float[]
		{
			1151.4117f,
			1348.3867f,
			209.6744f,
			0f
		});
		floatArray.add(new float[]
		{
			1215.5375f,
			1444.1869f,
			209.33066f,
			0f
		});
		floatArray.add(new float[]
		{
			1197.442f,
			1575.0511f,
			214.03578f,
			0f
		});
		floatArray.add(new float[]
		{
			11238.8937f,
			1487.0414f,
			214.13577f,
			0f
		});
		floatArray.add(new float[]
		{
			1343.2433f,
			1533.0582f,
			209.80017f,
			0f
		});
		floatArray.add(new float[]
		{
			1363.108f,
			1464.0858f,
			209.09084f,
			0f
		});
		floatArray.add(new float[]
		{
			1394.5568f,
			1382.9987f,
			208.08516f,
			0f
		});
		floatArray.add(new float[]
		{
			1516.5026f,
			1407.1633f,
			201.70367f,
			0f
		});
		floatArray.add(new float[]
		{
			1493.8002f,
			1460.6648f,
			176.9295f,
			0f
		});
		floatArray.add(new float[]
		{
			1275.4224f,
			1352.994f,
			204.4608f,
			0f
		});
		floatArray.add(new float[]
		{
			1312.3104f,
			1234.2994f,
			214.66357f,
			0f
		});
		floatArray.add(new float[]
		{
			1235.8165f,
			1170.0706f,
			215.13603f,
			0f
		});
		floatArray.add(new float[]
		{
			1075.684f,
			1065.3511f,
			201.52081f,
			0f
		});
		floatArray.add(new float[]
		{
			992.7121f,
			1044.0405f,
			201.52101f,
			0f
		});
		floatArray.add(new float[]
		{
			942.4062f,
			1131.7365f,
			206.84773f,
			0f
		});
		floatArray.add(new float[]
		{
			956.87994f,
			1188.8582f,
			201.4773f,
			0f
		});//
		floatArray.add(new float[]
		{
			1226.273f,
			1306.5815f,
			208.125f,
			0f
		});
		floatArray.add(new float[]
		{
			1016.5605f,
			1518.2861f,
			220.50787f,
			0f
		});
	}
}
