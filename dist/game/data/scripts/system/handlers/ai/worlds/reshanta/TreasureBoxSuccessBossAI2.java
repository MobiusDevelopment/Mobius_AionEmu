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
package system.handlers.ai.worlds.reshanta;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("treasure_box_success_boss")
public class TreasureBoxSuccessBossAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
	}
	
	@Override
	protected void handleDied()
	{
		switch (getNpcId())
		{
			// Siel's Western Fortress.
			case 263001:
			case 263006:
			case 263011:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 2827.0532f, 2604.545f, 1456.3451f, (byte) 102);
						spawn(701481, 2823.5054f, 2603.0269f, 1456.3495f, (byte) 97);
						spawn(701481, 2816.0642f, 2603.0618f, 1456.3497f, (byte) 84);
						spawn(701481, 2812.686f, 2604.8992f, 1456.3516f, (byte) 79);
						spawn(701481, 2830.26f, 2609.0352f, 1456.3516f, (byte) 111);
						spawn(701481, 2831.6018f, 2613.2336f, 1456.3492f, (byte) 118);
					}
				}, 10000);
				break;
			// Siel's Eastern Fortress.
			case 263301:
			case 263306:
			case 263311:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 2616.5698f, 2870.3818f, 1461.044f, (byte) 40);
						spawn(701481, 2621.4722f, 2872.264f, 1461.044f, (byte) 33);
						spawn(701481, 2612.515f, 2867.8733f, 1461.044f, (byte) 43);
						spawn(701481, 2608.5383f, 2863.8474f, 1461.044f, (byte) 47);
						spawn(701481, 2625.575f, 2872.504f, 1461.044f, (byte) 28);
						spawn(701481, 2606.2075f, 2860.1685f, 1461.044f, (byte) 50);
					}
				}, 10000);
				break;
			// Sulfur Fortress.
			case 264501:
			case 264506:
			case 264511:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1398.2168f, 1193.4137f, 1469.1918f, (byte) 25);
						spawn(701481, 1403.2175f, 1190.7214f, 1469.1918f, (byte) 17);
						spawn(701481, 1393.6261f, 1194.53f, 1469.1918f, (byte) 29);
						spawn(701481, 1387.8093f, 1194.1909f, 1469.1918f, (byte) 32);
						spawn(701481, 1383.5468f, 1193.1848f, 1469.1918f, (byte) 36);
						spawn(701481, 1378.7225f, 1190.306f, 1469.1918f, (byte) 43);
					}
				}, 10000);
				break;
			// Temple Of Scales.
			case 257000:
			case 257005:
			case 257010:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1730.2123f, 2233.581f, 328.6944f, (byte) 108);
						spawn(701481, 1727.2301f, 2233.2742f, 328.69946f, (byte) 82);
						spawn(701481, 1730.976f, 2238.1265f, 328.7828f, (byte) 6);
						spawn(701481, 1724.5315f, 2235.8772f, 328.77167f, (byte) 6);
					}
				}, 10000);
				break;
			// Altar Of Avarice.
			case 257300:
			case 257305:
			case 257310:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 882.04834f, 1976.6747f, 341.38403f, (byte) 72);
						spawn(701481, 884.64703f, 1975.8315f, 341.38733f, (byte) 96);
						spawn(701481, 887.6784f, 1978.6472f, 341.47156f, (byte) 113);
						spawn(701481, 882.11316f, 1981.1346f, 341.47067f, (byte) 52);
					}
				}, 10000);
				break;
			// Vorgaltem Citadel.
			case 257600:
			case 257605:
			case 257610:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1198.194f, 806.8985f, 313.88486f, (byte) 19);
						spawn(701481, 1195.4772f, 807.10376f, 313.8834f, (byte) 43);
						spawn(701481, 1199.5294f, 803.10785f, 313.97015f, (byte) 119);
						spawn(701481, 1193.7506f, 803.35754f, 313.96732f, (byte) 60);
					}
				}, 10000);
				break;
			// Crimsom Temple.
			case 257900:
			case 257905:
			case 257910:
				treasureChest();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						spawn(701481, 1881.3556f, 1044.9172f, 330.67426f, (byte) 18);
						spawn(701481, 1878.7448f, 1045.0604f, 330.67276f, (byte) 46);
						spawn(701481, 1877.0585f, 1041.5327f, 330.75238f, (byte) 61);
						spawn(701481, 1882.8416f, 1041.1636f, 330.76022f, (byte) 119);
					}
				}, 10000);
				break;
		}
		super.handleDied();
	}
	
	private void treasureChest()
	{
		getPosition().getWorldMapInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// A treasure chest has appeared.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_IDAbRe_Core_NmdC_BoxSpawn);
			}
		});
	}
}