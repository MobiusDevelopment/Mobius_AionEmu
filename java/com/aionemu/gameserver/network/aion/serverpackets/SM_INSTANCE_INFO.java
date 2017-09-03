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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PortalCooldownList;
import com.aionemu.gameserver.model.team2.TemporaryPlayerTeam;
import com.aionemu.gameserver.model.templates.InstanceCooltime;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

import javolution.util.FastMap;

public class SM_INSTANCE_INFO extends AionServerPacket
{
	private final Player player;
	private final boolean isAnswer;
	private final int cooldownId;
	private final int worldId;
	private final TemporaryPlayerTeam<?> playerTeam;
	
	public SM_INSTANCE_INFO(Player player, boolean isAnswer, TemporaryPlayerTeam<?> playerTeam)
	{
		this.player = player;
		this.isAnswer = isAnswer;
		this.playerTeam = playerTeam;
		worldId = 0;
		cooldownId = 0;
	}
	
	public SM_INSTANCE_INFO(Player player, int instanceId)
	{
		this.player = player;
		isAnswer = false;
		playerTeam = null;
		worldId = instanceId;
		cooldownId = DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(instanceId) != null ? DataManager.INSTANCE_COOLTIME_DATA.getInstanceCooltimeByWorldId(instanceId).getId() : 0;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final boolean hasTeam = playerTeam != null;
		writeC(!isAnswer ? 0x2 : hasTeam ? 0x1 : 0x0);
		writeC(cooldownId);
		writeD(0x00);
		writeH(0x01);
		if (cooldownId == 0)
		{
			writeD(player.getObjectId());
			writeH(DataManager.INSTANCE_COOLTIME_DATA.size());
			final PortalCooldownList cooldownList = player.getPortalCooldownList();
			for (FastMap.Entry<Integer, InstanceCooltime> e = DataManager.INSTANCE_COOLTIME_DATA.getAllInstances().head(), end = DataManager.INSTANCE_COOLTIME_DATA.getAllInstances().tail(); (e = e.getNext()) != end;)
			{
				writeD(e.getValue().getId());
				writeD(0x00);
				if (cooldownList.getPortalCooldown(e.getValue().getWorldId()) == 0)
				{
					writeD(0x00);
				}
				else
				{
					writeD((int) (cooldownList.getPortalCooldown(e.getValue().getWorldId()) - System.currentTimeMillis()) / 1000);
				}
				writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(e.getKey()));
				writeD(cooldownList.getPortalCooldownItem(e.getValue().getWorldId()) != null ? cooldownList.getPortalCooldownItem(e.getValue().getWorldId()).getEntryCount() * -1 : 0);
				writeD(0x00);
				writeD(0x00);
				writeD(0x01);
				writeC(0x01);
			}
			writeS(player.getName());
		}
		else
		{
			writeD(player.getObjectId());
			writeH(0x01);
			writeD(cooldownId);
			writeD(0x00);
			final long time = player.getPortalCooldownList().getPortalCooldown(worldId);
			writeD((time == 0 ? 0 : ((int) (time - System.currentTimeMillis()) / 1000)));
			writeD(DataManager.INSTANCE_COOLTIME_DATA.getInstanceEntranceCountByWorldId(worldId));
			writeD(player.getPortalCooldownList().getPortalCooldownItem(worldId) != null ? player.getPortalCooldownList().getPortalCooldownItem(worldId).getEntryCount() * -1 : 0);
			writeD(0x00);
			writeD(0x00);
			writeD(0x01);
			writeC(0x01);
			writeS(player.getName());
		}
	}
}