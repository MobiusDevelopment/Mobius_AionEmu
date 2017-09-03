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
package com.aionemu.gameserver.model.templates.item.actions;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.teleport.MultiReturn;
import com.aionemu.gameserver.model.templates.teleport.MultiReturnLocationList;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.services.teleport.MultiReturnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MultiReturnAction")
public class MultiReturnAction extends AbstractItemAction
{
	/**
	 * 6 ELYOS. 7 ASMODIANS.
	 */
	@XmlAttribute(name = "id")
	private int id;
	
	public int getId()
	{
		return id;
	}
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		return true;
	}
	
	@Override
	public void act(final Player player, final Item parentItem, Item targetItem)
	{
	}
	
	public void act(final Player player, final Item MultiReturn, final int SelectedMapIndex)
	{
		PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), MultiReturn.getObjectId(), MultiReturn.getItemTemplate().getTemplateId(), 5000, 0, 0));
		player.getController().cancelTask(TaskId.ITEM_USE);
		final ItemUseObserver observer = new ItemUseObserver()
		{
			@Override
			public void abort()
			{
				player.getController().cancelTask(TaskId.ITEM_USE);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), MultiReturn.getObjectId(), MultiReturn.getItemTemplate().getTemplateId(), 0, 2, 0));
				player.getObserveController().removeObserver(this);
				player.removeItemCoolDown(MultiReturn.getItemTemplate().getUseLimits().getDelayId());
			}
		};
		player.getObserveController().attach(observer);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				player.getObserveController().removeObserver(observer);
				if (player.getInventory().decreaseByObjectId(MultiReturn.getObjectId(), 1))
				{
					final int MultiReturnId = getId();
					final MultiReturn rItem = DataManager.MULTI_RETURN_ITEM_DATA.getMultiReturnById(MultiReturnId);
					if ((rItem != null) && (rItem.getMultiReturnList() != null))
					{
						final MultiReturnLocationList ReturnData = rItem.getReturnDataById(SelectedMapIndex);
						if (ReturnData != null)
						{
							final int ReturnCount = rItem.getMultiReturnList().size();
							if (SelectedMapIndex <= (ReturnCount - 1))
							{
								final int worldId = ReturnData.getWorldId();
								final int LocId = MultiReturnService.getTeleportWorldId(worldId, player.getRace());
								MultiReturnService.Teleport(player, LocId, worldId);
							}
						}
					}
				}
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), MultiReturn.getObjectId(), MultiReturn.getItemTemplate().getTemplateId(), 0, 1, 0));
			}
		}, 5000));
	}
}