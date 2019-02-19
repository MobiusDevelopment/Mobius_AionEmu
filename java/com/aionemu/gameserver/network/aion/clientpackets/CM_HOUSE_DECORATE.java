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

import com.aionemu.gameserver.model.gameobjects.HouseDecoration;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.templates.housing.PartType;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_HOUSE_EDIT;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;

/**
 * @author Rolandas
 */
public class CM_HOUSE_DECORATE extends AionClientPacket
{
	int objectId;
	int templateId;
	int lineNr;
	
	public CM_HOUSE_DECORATE(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		objectId = readD();
		templateId = readD();
		lineNr = readH();
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (player == null)
		{
			return;
		}
		final House house = player.getHouseRegistry().getOwner();
		final PartType partType = PartType.getForLineNr(lineNr);
		final int floor = lineNr - partType.getStartLineNr();
		if (objectId == 0)
		{
			final HouseDecoration decor = house.getRegistry().getDefaultPartByType(partType, floor);
			if (decor.isUsed())
			{
				return;
			}
			house.getRegistry().setPartInUse(decor, floor);
		}
		else
		{
			final HouseDecoration decor = house.getRegistry().getCustomPartByObjId(objectId);
			house.getRegistry().setPartInUse(decor, floor);
			sendPacket(new SM_HOUSE_EDIT(4, 2, objectId));
		}
		sendPacket(new SM_HOUSE_EDIT(4, 2, objectId));
		house.getRegistry().setPersistentState(PersistentState.UPDATE_REQUIRED);
		house.getController().updateAppearance();
		QuestEngine.getInstance().onHouseItemUseEvent(new QuestEnv(null, player, 0, 0), templateId);
	}
}