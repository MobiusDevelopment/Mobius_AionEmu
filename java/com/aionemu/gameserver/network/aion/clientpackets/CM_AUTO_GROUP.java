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
package com.aionemu.gameserver.network.aion.clientpackets;

import com.aionemu.gameserver.configs.main.AutoGroupConfig;
import com.aionemu.gameserver.configs.main.PvPModConfig;
import com.aionemu.gameserver.model.autogroup.EntryRequestType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.services.instance.AsyunatarService;
import com.aionemu.gameserver.services.instance.DredgionService2;
import com.aionemu.gameserver.services.instance.EngulfedOphidanBridgeService;
import com.aionemu.gameserver.services.instance.IdgelDomeLandmarkService;
import com.aionemu.gameserver.services.instance.IdgelDomeService;
import com.aionemu.gameserver.services.instance.IronWallWarfrontService;
import com.aionemu.gameserver.services.instance.KamarBattlefieldService;
import com.aionemu.gameserver.services.instance.SuspiciousOphidanBridgeService;
import com.aionemu.gameserver.utils.PacketSendUtility;

public class CM_AUTO_GROUP extends AionClientPacket
{
	private byte instanceMaskId;
	private byte windowId;
	private byte entryRequestId;
	
	public CM_AUTO_GROUP(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		instanceMaskId = (byte) readD();
		windowId = (byte) readC();
		entryRequestId = (byte) readC();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (!AutoGroupConfig.AUTO_GROUP_ENABLED)
		{
			PacketSendUtility.sendMessage(player, "Auto Group is disabled");
			return;
		}
		switch (windowId)
		{
			case 100:
				final EntryRequestType ert = EntryRequestType.getTypeById(entryRequestId);
				if (ert == null)
				{
					return;
				}
				AutoGroupService.getInstance().startLooking(player, instanceMaskId, ert);
				break;
			case 101:
				AutoGroupService.getInstance().unregisterLooking(player, instanceMaskId);
				break;
			case 102:
				AutoGroupService.getInstance().pressEnter(player, instanceMaskId);
				break;
			case 103:
				AutoGroupService.getInstance().cancelEnter(player, instanceMaskId);
				break;
			case 104:
				AsyunatarService.getInstance().showWindow(player, instanceMaskId);
				DredgionService2.getInstance().showWindow(player, instanceMaskId);
				KamarBattlefieldService.getInstance().showWindow(player, instanceMaskId);
				EngulfedOphidanBridgeService.getInstance().showWindow(player, instanceMaskId);
				SuspiciousOphidanBridgeService.getInstance().showWindow(player, instanceMaskId);
				IronWallWarfrontService.getInstance().showWindow(player, instanceMaskId);
				IdgelDomeService.getInstance().showWindow(player, instanceMaskId);
				IdgelDomeLandmarkService.getInstance().showWindow(player, instanceMaskId);
				break;
			case 105:
				break;
		}
		if (PvPModConfig.BG_ENABLED)
		{
			LadderService.getInstance().handleWindow(player, windowId, entryRequestId);
		}
	}
}