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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPGRADE_ARCADE;
import com.aionemu.gameserver.services.events.UpgradeArcadeService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */
public class CM_UPGRADE_ARCADE extends AionClientPacket
{
	// private static final Logger log = LoggerFactory.getLogger(CM_UPGRADE_ARCADE.class);
	
	private int type;
	private int wtfPoint;
	
	public CM_UPGRADE_ARCADE(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		type = readC();
		wtfPoint = readD();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (player == null)
		{
			return;
		}
		if ((type == 0) && (wtfPoint == 1))
		{
			UpgradeArcadeService.getInstance().updateTypeOne(player);
		}
		else if (type == 1)
		{
			UpgradeArcadeService.getInstance().upgradeTypeTwo(player);
		}
		else if (type == 2)
		{
			UpgradeArcadeService.getInstance().upgradeTypeThree(player);
		}
		else if (type == 3)
		{
			UpgradeArcadeService.getInstance().upgradeTypeSix(player);
		}
		else if (type == 5)
		{
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(10));
		}
	}
}