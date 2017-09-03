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
import javax.xml.bind.annotation.XmlType;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.configs.main.EnchantsConfig;
import com.aionemu.gameserver.controllers.observer.ItemUseObserver;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.TaskId;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INVENTORY_UPDATE_ITEM;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Author Ranastic (Encom) /
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "TemperingAction")
public class TemperingAction extends AbstractItemAction
{
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if (targetItem.getItemTemplate().getMaxAuthorize() == 0)
		{
			return false;
		}
		if (targetItem.getItemTemplate().isAccessory() && !targetItem.getItemTemplate().isPlume() && (targetItem.getAuthorize() >= 8))
		{
			// %0 cannot be tempered anymore.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANT_MORE_AUTHORIZE(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		if (targetItem.getItemTemplate().isPlume() && !targetItem.getItemTemplate().isAccessory() && (targetItem.getAuthorize() >= 15))
		{
			// %0 cannot be tempered anymore.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANT_MORE_AUTHORIZE(new DescriptionId(targetItem.getNameId())));
			return false;
		}
		return targetItem.getAuthorize() < targetItem.getItemTemplate().getMaxAuthorize();
	}
	
	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem)
	{
		if (player.isAuthorizeBoost())
		{
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 1500, 0, 0));
		}
		else
		{
			PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), parentItem.getItemTemplate().getTemplateId(), 5000, 0, 0));
		}
		final ItemUseObserver observer = new ItemUseObserver()
		{
			@Override
			public void abort()
			{
				player.getController().cancelTask(TaskId.ITEM_USE);
				player.getObserveController().removeObserver(this);
				PacketSendUtility.sendPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemTemplate().getTemplateId(), 0, 3, 0));
				ItemPacketService.updateItemAfterInfoChange(player, targetItem);
				// You have canceled the tempering of %0.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_CANCEL(targetItem.getNameId()));
			}
		};
		player.getObserveController().attach(observer);
		final boolean isTemperingSuccess = isSuccess(player);
		player.getController().addTask(TaskId.ITEM_USE, ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (player.getInventory().decreaseByItemId(parentItem.getItemId(), 1))
				{
					if (!isTemperingSuccess)
					{
						PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 2, 0));
						targetItem.setAuthorize(0);
						// Tempering of %0 has failed and the temperance level has decreased to 0.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_FAILED(targetItem.getNameId()));
					}
					else
					{
						PacketSendUtility.broadcastPacketAndReceive(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId().intValue(), player.getObjectId().intValue(), parentItem.getObjectId().intValue(), parentItem.getItemId(), 0, 1, 0));
						targetItem.setAuthorize(targetItem.getAuthorize() + 1);
						// You have successfully tempered %0. +%num1 temperance level achieved.
						PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_ITEM_AUTHORIZE_SUCCEEDED(targetItem.getNameId(), targetItem.getAuthorize()));
					}
					PacketSendUtility.sendPacket(player, new SM_INVENTORY_UPDATE_ITEM(player, targetItem));
					player.getObserveController().removeObserver(observer);
					if (targetItem.isEquipped())
					{
						player.getGameStats().updateStatsVisually();
					}
					ItemPacketService.updateItemAfterInfoChange(player, targetItem);
					if (targetItem.isEquipped())
					{
						player.getEquipment().setPersistentState(PersistentState.UPDATE_REQUIRED);
					}
					else
					{
						player.getInventory().setPersistentState(PersistentState.UPDATE_REQUIRED);
					}
				}
			}
		}, 5000));
	}
	
	public boolean isSuccess(Player player)
	{
		return Rnd.get(0, 100) < calcTemperingRate();
		
	}
	
	private float calcTemperingRate()
	{
		final float base = 5;
		final float staticRate = 40;
		final float failRate = Rnd.get(0, 2);
		float resultRate = EnchantsConfig.TEMPERING_RATE > 10 ? 10 : EnchantsConfig.TEMPERING_RATE;
		resultRate = (resultRate * base) - failRate;
		return (resultRate + staticRate);
	}
}