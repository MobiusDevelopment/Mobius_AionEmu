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
package system.handlers.ai.instance.dredgion;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_CUSTOM_SETTINGS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.GeneralNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("shulack_drudge")
public class Shulack_DrudgeAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogFinish(Player player)
	{
		addItems(player);
		super.handleDialogFinish(player);
	}
	
	@Override
	protected void handleDialogStart(Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	private void addItems(Player player)
	{
		final int itemId = player.getRace() == Race.ELYOS ? 182212606 : 182212607;
		final Item dredgionSupplies = player.getInventory().getFirstItemByItemId(itemId);
		if (dredgionSupplies == null)
		{
			ItemService.addItem(player, itemId, 1);
			getOwner().setNpcType(NpcType.NON_ATTACKABLE);
			getKnownList().doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					PacketSendUtility.sendPacket(player, new SM_CUSTOM_SETTINGS(getOwner().getObjectId(), 0, NpcType.NON_ATTACKABLE.getId(), 0));
				}
			});
		}
	}
}