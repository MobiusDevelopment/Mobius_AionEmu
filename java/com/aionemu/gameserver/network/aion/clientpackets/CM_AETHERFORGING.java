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
import com.aionemu.gameserver.services.craft.CraftService;

/**
 * @author Ranastic
 */

public class CM_AETHERFORGING extends AionClientPacket
{
	private int itemID;
	@SuppressWarnings("unused")
	private long itemCount;
	private int actionId;
	private int targetTemplateId;
	private int recipeId;
	private int materialsCount;
	private int craftType;
	
	public CM_AETHERFORGING(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		final Player player = getConnection().getActivePlayer();
		actionId = readC();
		targetTemplateId = readD();
		recipeId = readD();
		readD();
		materialsCount = readH();
		craftType = readC();
		if ((actionId == 1) && (craftType == 0))
		{
			for (int i = 0; i < materialsCount; i++)
			{
				itemID = readD();
				itemCount = readQ();
				CraftService.checkComponents(player, recipeId, itemID, materialsCount);
			}
		}
		else if ((actionId == 0) && (craftType == 0))
		{
			CraftService.stopAetherforging(player, recipeId);
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if ((player == null) || !player.isSpawned())
		{
			return;
		}
		if (player.getController().isInShutdownProgress())
		{
			return;
		}
		switch (actionId)
		{
			case 0:
			{
				CraftService.stopAetherforging(player, recipeId);
				break;
			}
			case 1:
			{
				CraftService.startAetherforging(player, recipeId, craftType);
			}
		}
	}
}