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
package system.handlers.ai.worlds.inggison;

import java.util.List;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastMap;
import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("boss_windstream")
public class Boss_WindstreamAI2 extends AggressiveNpcAI2
{
	private final FastMap<Integer, VisibleObject> objects = new FastMap<>();
	
	@Override
	protected void handleDied()
	{
		switch (getNpcId())
		{
			/**
			 * WINDSTREAM INGGISON
			 */
			case 215584: // Titan Starturtle.
			{
				announceWindBox();
				final SpawnTemplate CastShadowPLSM = SpawnEngine.addNewSingleTimeSpawn(210050000, 281817, 338.26440f, 573.72168f, 458.27939f, (byte) 0);
				CastShadowPLSM.setEntityId(755);
				objects.put(281817, SpawnEngine.spawnObject(CastShadowPLSM, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
			case 216849: // Watcher Garma.
			{
				announceWindBox();
				final SpawnTemplate EnvWeatherShow = SpawnEngine.addNewSingleTimeSpawn(210050000, 281817, 2602.6992f, 1526.0367f, 258.13651f, (byte) 0);
				EnvWeatherShow.setEntityId(754);
				objects.put(281817, SpawnEngine.spawnObject(EnvWeatherShow, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
			case 216848: // Illanthe Hundredyears.
			{
				announceWindBox();
				final SpawnTemplate SkipOnLowSpec = SpawnEngine.addNewSingleTimeSpawn(210050000, 281817, 1745.8660f, 1716.3790f, 226.37808f, (byte) 0);
				SkipOnLowSpec.setEntityId(1039);
				objects.put(281817, SpawnEngine.spawnObject(SkipOnLowSpec, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
			case 217071: // Esalki The Fourth.
			{
				announceWindBox();
				final SpawnTemplate EnvWeatherHide = SpawnEngine.addNewSingleTimeSpawn(210050000, 281817, 2288.1091f, 1067.0475f, 285.73407f, (byte) 0);
				EnvWeatherHide.setEntityId(2311);
				objects.put(281817, SpawnEngine.spawnObject(EnvWeatherHide, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
			case 217072: // Huge Waterfall Starturtle.
			{
				announceWindBox();
				final SpawnTemplate DisplayFilled = SpawnEngine.addNewSingleTimeSpawn(210050000, 281817, 1660.0439f, 928.57129f, 404.99213f, (byte) 0);
				DisplayFilled.setEntityId(2292);
				objects.put(281817, SpawnEngine.spawnObject(DisplayFilled, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
			/**
			 * WINDSTREAM GELKMAROS
			 */
			case 216846: // Agrima.
			{
				announceWindBox();
				final SpawnTemplate FileLadderCGF = SpawnEngine.addNewSingleTimeSpawn(220070000, 281817, 1719.2194f, 2301.7344f, 318.70938f, (byte) 0);
				FileLadderCGF.setEntityId(1821);
				objects.put(281817, SpawnEngine.spawnObject(FileLadderCGF, 1));
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						despawnNpc(281817);
					}
				}, 300000); // 5 Minutes.
				break;
			}
		}
		super.handleDied();
		AI2Actions.scheduleRespawn(this);
	}
	
	private void announceWindBox()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_JUMP_TRIGGER_ON_INFO);
			}
		});
	}
	
	private void despawnNpc(int npcId)
	{
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null)
		{
			final List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs)
			{
				npc.getController().onDelete();
			}
		}
	}
}