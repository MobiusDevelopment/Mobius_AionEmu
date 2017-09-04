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

/**
 * @author Rinzler (Encom)
 */
public class IronWallWarfrontService
{
	// private static final Logger log = LoggerFactory.getLogger(IronWallWarfrontService.class);
	
	boolean registerAvailable;
	final FastList<Integer> playersWithCooldown = FastList.newInstance();
	public static final byte minLevel = 66, capLevel = 84;
	public static final int maskId = 109;
	
	public void initIronWallWarfront()
	{
		if (AutoGroupConfig.BASTION_ENABLED)
		{
			// Iron Wall Warfront SUN "8PM-9PM"
			CronService.getInstance().schedule(() -> startBastionRegistration(), AutoGroupConfig.BASTION_SCHEDULE_EVENING);
			// Iron Wall Warfront SUN "11PM-0AM"
			CronService.getInstance().schedule(() -> startBastionRegistration(), AutoGroupConfig.BASTION_SCHEDULE_MIDNIGHT);
		}
	}
	
	private void startUregisterBastionTask()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			registerAvailable = false;
			playersWithCooldown.clear();
			AutoGroupService.getInstance().unRegisterInstance(maskId);
			final Iterator<Player> iter = World.getInstance().getPlayersIterator();
			while (iter.hasNext())
			{
				final Player player = iter.next();
				if (player.getLevel() > minLevel)
				{
					final int instanceMaskId = getInstanceMaskId(player);
					if (instanceMaskId > 0)
					{
						PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon, true));
					}
				}
			}
		}, AutoGroupConfig.BASTION_TIMER * 60 * 1000);
	}
	
	void startBastionRegistration()
	{
		registerAvailable = true;
		startUregisterBastionTask();
		final Iterator<Player> iter = World.getInstance().getPlayersIterator();
		while (iter.hasNext())
		{
			final Player player = iter.next();
			if ((player.getLevel() > minLevel) && (player.getLevel() < capLevel))
			{
				final int instanceMaskId = getInstanceMaskId(player);
				if (instanceMaskId > 0)
				{
					PacketSendUtility.sendPacket(player, new SM_AUTO_GROUP(instanceMaskId, SM_AUTO_GROUP.wnd_EntryIcon));
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_INSTANCE_OPEN_IDF5_TD_war);
				}
			}
		}
	}
	
	public boolean isBastionAvailable()
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
		return maskId;
	}
	
	public void addCoolDown(Player player)
	{
		playersWithCooldown.add(player.getObjectId());
	}
	
	public boolean hasCoolDown(Player player)
	{
		return playersWithCooldown.contains(player.getObjectId());
	}
	
	public void showWindow(Player player, byte instanceMaskId)
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
		protected static final IronWallWarfrontService instance = new IronWallWarfrontService();
	}
	
	public static IronWallWarfrontService getInstance()
	{
		return SingletonHolder.instance;
	}
}