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

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.templates.item.ItemTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /** Modified by Ranastic /
 ****/

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "BonusAddExpAction")
public class BonusAddExpAction extends AbstractItemAction
{
	@XmlAttribute(name = "rate")
	protected Integer rate;
	
	public BonusAddExpAction()
	{
	}
	
	public BonusAddExpAction(Integer rate)
	{
		this.rate = rate;
	}
	
	public Integer getRate()
	{
		return rate;
	}
	
	public void setRate(Integer rate)
	{
		this.rate = rate;
	}
	
	@Override
	public boolean canAct(Player player, Item parentItem, Item targetItem)
	{
		if (parentItem == null)
		{
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_ITEM_COLOR_ERROR);
			return false;
		}
		return true;
	}
	
	@Override
	public void act(final Player player, final Item parentItem, final Item targetItem)
	{
		final ItemTemplate itemTemplate = parentItem.getItemTemplate();
		player.getCommonData().addExp((long) ((player.getCommonData().getExpNeed() * getRate()) / 100f), RewardType.HUNTING);
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), parentItem.getObjectId(), itemTemplate.getTemplateId()), true);
		player.getInventory().decreaseByObjectId(parentItem.getObjectId(), 1);
	}
}