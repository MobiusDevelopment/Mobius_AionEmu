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

import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LUNA_SHOP_LIST;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.LunaShopService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic
 */

public class CM_LUNA_SHOP extends AionClientPacket
{
	// private static final Logger log = LoggerFactory.getLogger(CM_LUNA_SHOP.class);
	private int actionId;
	private int indun_id;
	private int indun_unk;
	private int recipe_id;
	private int material_item_id;
	private long material_item_count;
	private int teleportId;
	private int slot;
	private int ItemObjId;
	@SuppressWarnings("unused")
	private int lunaCost;
	
	public CM_LUNA_SHOP(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		actionId = readC();
		switch (actionId)
		{
			case 0: // Taki's Missions Teleport.
			{
				indun_id = readD();
				indun_unk = readC();
				break;
			}
			case 2: // Karunerk's Workshop.
			{
				recipe_id = readD();
				break;
			}
			case 4: // Buy Necessary Materials.
			{
				material_item_id = readD();
				material_item_count = readQ();
				break;
			}
			case 6:
			case 7:
			{
				teleportId = readD();
				break;
			}
			case 8: // Dorinerk's Wardrobe.
			{
				break;
			}
			case 9: // Expand wardrobe slot
			{
				break;
			}
			case 10: // Apply wardrobe appearance
			{
				slot = readC();
				ItemObjId = readD();
				break;
			}
			case 11: // Modify appearance
			{
				slot = readC();
				ItemObjId = readD();
				lunaCost = readC();
				break;
			}
			case 12: // Open Chest.
			{
				break;
			}
			case 14: // Taki's Adventure.
			{
				indun_id = readD();
				break;
			}
		}
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		if (player == null)
		{
			return;
		}
		if (actionId == 0)
		{
			if (player.isInGroup2())
			{
				PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You must leave your group or alliance to enter <Luna Instance>", ChatType.BRIGHT_YELLOW_CENTER), true);
				return;
			}
			else if (player.getLevel() < 10)
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
				return;
			}
			LunaShopService.getInstance().takiAdventureTeleport(player, indun_unk, indun_id);
		}
		else if (actionId == 2)
		{// Karunerk's Workshop
			LunaShopService.getInstance().specialDesign(player, recipe_id);
		}
		else if (actionId == 3)
		{
			LunaShopService.getInstance().craftBox(player);
		}
		else if (actionId == 4)
		{// Buy Necessary Materials
			LunaShopService.getInstance().buyMaterials(player, material_item_id, material_item_count);
		}
		else if (actionId == 5)
		{
			PacketSendUtility.sendPacket(player, new SM_LUNA_SHOP_LIST(actionId));
		}
		else if ((actionId == 6) || (actionId == 7))
		{
			LunaShopService.getInstance().teleport(player, actionId, teleportId);
		}
		else if (actionId == 8)
		{
			LunaShopService.getInstance().dorinerkWardrobeLoad(player);
		}
		else if (actionId == 9)
		{
			LunaShopService.getInstance().dorinerkWardrobeExtendSlots(player);
		}
		else if (actionId == 10)
		{
			LunaShopService.getInstance().dorinerkWardrobeAct(player, slot, ItemObjId);
		}
		else if (actionId == 11)
		{
			LunaShopService.getInstance().dorinerkWardrobeModifyAppearance(player, slot, ItemObjId);
		}
		else if (actionId == 12)
		{
			LunaShopService.getInstance().munirunerksTreasureChamber(player);
		}
		else if (actionId == 14)
		{
			LunaShopService.getInstance().takiAdventure(player, indun_id);
		}
	}
}