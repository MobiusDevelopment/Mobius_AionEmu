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
package com.aionemu.gameserver.services.instance;

import java.util.Iterator;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.services.CronService;
import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_AUTO_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

import javolution.util.FastList;

public class DredgionService2
{
	
	private boolean registerAvailable;
	private final FastList<Integer> playersWithCooldown = new FastList<>();
	private final SM_AUTO_GROUP[] autoGroupUnreg, autoGroupReg;
	private final byte maskLvlGradeC = 1, maskLvlGradeB = 2, maskLvlGradeA = 3;
	public static final byte minLevel = 46, capLevel = 61;
	
	public DredgionService2()
	{
		autoGroupUnreg = new SM_AUTO_GROUP[maskLvlGradeA + 1];
		autoGroupReg = new SM_AUTO_GROUP[autoGroupUnreg.length];
		for (byte i = maskLvlGradeC; i <= maskLvlGradeA; i++)
		{
			autoGroupUnreg[i] = new SM_AUTO_GROUP(i, SM_AUTO_GROUP.wnd_EntryIcon, true);
			autoGroupReg[i] = new SM_AUTO_GROUP(i, SM_AUTO_GROUP.wnd_EntryIcon);
		}
	}
	
	public void initDredgion()
	{
		if (AutoGroupConfig.DREDGION_ENABLED)
		{
			// Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "12PM-1PM"
			CronService.getInstance().schedule(() -> startDredgionRegistration(), AutoGroupConfig.DREDGION_SCHEDULE_MIDDAY);
			// Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "8PM-9PM"
			CronService.getInstance().schedule(() -> startDredgionRegistration(), AutoGroupConfig.DREDGION_SCHEDULE_EVENING);
			// Dredgion MON-TUE-WED-THU-FRI-SAT-SUN "23PM-0AM"
			CronService.getInstance().schedule(() -> startDredgionRegistration(), AutoGroupConfig.DREDGION_SCHEDULE_MIDNIGHT);
		}
	}
	
	private void startUregisterDredgionTask()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			registerAvailable = false;
			playersWithCooldown.clear();
			AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeA);
			AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeB);
			AutoGroupService.getInstance().unRegisterInstance(maskLvlGradeC);
			final Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext())
			{
				final Player player = iter.next();
				if (player.getLevel() > minLevel)
				{
					final int instanceMaskId = getInstanceMaskId(player);
					if (instanceMaskId > 0)
					{
						PacketSendUtility.sendPacket(player, autoGroupUnreg[instanceMaskId]);
					}
				}
			}
		}, AutoGroupConfig.DREDGION_TIMER * 60 * 1000);
	}
	
	private void startDredgionRegistration()
	{
		registerAvailable = true;
		startUregisterDredgionTask();
		final Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext())
		{
			final Player player = iter.next();
			if ((player.getLevel() > minLevel) && (player.getLevel() < capLevel))
			{
				final int instanceMaskId = getInstanceMaskId(player);
				if (instanceMaskId > 0)
				{
					PacketSendUtility.sendPacket(player, autoGroupReg[instanceMaskId]);
					switch (instanceMaskId)
					{
						case maskLvlGradeC:
						{
							// An infiltration route into the Dredgion is open.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDAB1_DREADGION);
							break;
						}
						case maskLvlGradeB:
						{
							// An infiltration passage into the Chantra Dredgion has opened.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDDREADGION_02);
							break;
						}
						case maskLvlGradeA:
						{
							// An infiltration passage into the Terath Dredgion has opened.
							PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDDREADGION_03);
							break;
						}
					}
				}
			}
		}
	}
	
	public boolean isDredgionAvailable()
	{
		return registerAvailable;
	}
	
	public byte getInstanceMaskId(Player player)
	{
		final int level = player.getLevel();
		if ((level < minLevel) || (level >= capLevel))
		{
			return 0;
		}
		if (level < 51)
		{
			return maskLvlGradeC;
		}
		else if (level < 56)
		{
			return maskLvlGradeB;
		}
		else
		{
			return maskLvlGradeA;
		}
	}
	
	public void addCoolDown(Player player)
	{
		playersWithCooldown.add(player.getObjectId());
	}
	
	public boolean hasCoolDown(Player player)
	{
		return playersWithCooldown.contains(player.getObjectId());
	}
	
	public void showWindow(Player player, int instanceMaskId)
	{
		if (getInstanceMaskId(player) != instanceMaskId)
		{
			return;
		}
		if (!playersWithCooldown.contains(player.getObjectId()))
		{
			PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId));
		}
	}
	
	private static class SingletonHolder
	{
		protected static final DredgionService2 instance = new DredgionService2();
	}
	
	public static DredgionService2 getInstance()
	{
		return SingletonHolder.instance;
	}
}