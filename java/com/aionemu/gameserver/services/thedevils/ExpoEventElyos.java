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
 * @author Romanz hckd05
 */
public class ExpoEventElyos
{
	private static final Logger log = LoggerFactory.getLogger(ExpoEventElyos.class);
	private static List<float[]> floatArray = new ArrayList<>();
	private static final String BABI_EVENT_SCHEDULE_ELYOS = EventsConfig.BABI_EVENT_SCHEDULE_ELYOS;
	private static int WORLD_ID = 110010000;
	private static int NPC_ID = 219158;
	private static int[] rewards =
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
			
		}, BABI_EVENT_SCHEDULE_ELYOS);
		log.info("Pig Event start to:" + EventsConfig.BABI_EVENT_SCHEDULE_ELYOS + " duration 30 min");
	}
	
	public static void startEvent()
	{
		initCoordinates();
		
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			
			@Override
			public void visit(Player object)
			{
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event, mencari babi hutan di mulai location sanctum cepat ikuti untuk mengambil hadiah!");
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
					ItemService.addItem(player, id, EventsConfig.BABI_EVENT_COUNT_REWARD);
					World.getInstance().doOnAllPlayers(new Visitor<Player>()
					{
						
						@Override
						public void visit(Player object)
						{
							PacketSendUtility.sendYellowMessageOnCenter(object, player.getName() + "Menemukan babi hutan dan mendapatkan [item:%d]");
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
				PacketSendUtility.sendYellowMessageOnCenter(object, "Event mencari babi hutan telah selesai terimakasih atas partisipasinya!");
			}
		});
		
		mainN.getController().onDelete();
	}
	
	private static void initCoordinates()
	{
		floatArray.add(new float[]
		{
			1375.3103f,
			1480.2904f,
			570.00366f,
			0f
		});
		floatArray.add(new float[]
		{
			1317.2561f,
			1522.925f,
			567.9356f,
			0f
		});
		floatArray.add(new float[]
		{
			1429.9823f,
			1652.2386f,
			573.19714f,
			0f
		});
		floatArray.add(new float[]
		{
			1484.9032f,
			1651.9423f,
			573.2296f,
			0f
		});
		floatArray.add(new float[]
		{
			1642.8931f,
			1615.0457f,
			548.74054f,
			0f
		});
		floatArray.add(new float[]
		{
			1603.292f,
			1490.8684f,
			549.5507f,
			0f
		});
		floatArray.add(new float[]
		{
			1667.1558f,
			1361.4783f,
			557.2329f,
			0f
		});
		floatArray.add(new float[]
		{
			1750.8691f,
			1402.9253f,
			574.8727f,
			0f
		});
		floatArray.add(new float[]
		{
			1722.307f,
			1515.548f,
			587.0522f,
			0f
		});
		floatArray.add(new float[]
		{
			1889.2927f,
			1762.3921f,
			576.82635f,
			0f
		});
		floatArray.add(new float[]
		{
			1982.785f,
			1751.1995f,
			576.7586f,
			0f
		});
		floatArray.add(new float[]
		{
			1898.7987f,
			1621.6406f,
			590.35803f,
			0f
		});
		floatArray.add(new float[]
		{
			2051.538f,
			1527.4951f,
			581.1387f,
			0f
		});
		floatArray.add(new float[]
		{
			1938.7406f,
			1458.2668f,
			590.36285f,
			0f
		});
		floatArray.add(new float[]
		{
			1838.0339f,
			1465.5217f,
			590.1739f,
			0f
		});
		floatArray.add(new float[]
		{
			1896.2542f,
			1580.0668f,
			590.10864f,
			0f
		});
		floatArray.add(new float[]
		{
			1812.2086f,
			2113.3728f,
			527.92975f,
			0f
		});
		floatArray.add(new float[]
		{
			1851.6115f,
			2197.7754f,
			528.51184f,
			0f
		});
		floatArray.add(new float[]
		{
			1755.1313f,
			2009.3539f,
			517.90717f,
			0f
		});
		floatArray.add(new float[]
		{
			1909.5095f,
			2143.6946f,
			524.3187f,
			0f
		});
		floatArray.add(new float[]
		{
			1337.0829f,
			1399.552f,
			575.2538f,
			0f
		});
		floatArray.add(new float[]
		{
			1584.59f,
			1511.4647f,
			572.50555f,
			0f
		});
		floatArray.add(new float[]
		{
			1688.3009f,
			1615.3771f,
			566.76227f,
			0f
		});
		floatArray.add(new float[]
		{
			1549.2594f,
			1380.8866f,
			563.80085f,
			0f
		});
		floatArray.add(new float[]
		{
			1588.8115f,
			1425.2795f,
			573.0344f,
			0f
		});
		floatArray.add(new float[]
		{
			1536.8796f,
			1599.3599f,
			572.9935f,
			0f
		});
	}
}
