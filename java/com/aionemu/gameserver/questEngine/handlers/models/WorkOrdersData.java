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
package com.aionemu.gameserver.questEngine.handlers.models;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.WorkOrders;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "WorkOrdersData", propOrder =
{
	"giveComponent"
})
public class WorkOrdersData extends XMLQuest
{
	@XmlElement(name = "give_component", required = true)
	protected List<QuestItems> giveComponent;
	
	@XmlAttribute(name = "start_npc_ids", required = true)
	protected List<Integer> startNpcIds;
	
	@XmlAttribute(name = "recipe_id", required = true)
	protected int recipeId;
	
	public List<QuestItems> getGiveComponent()
	{
		if (giveComponent == null)
		{
			giveComponent = new ArrayList<>();
		}
		return giveComponent;
	}
	
	public List<Integer> getStartNpcIds()
	{
		return startNpcIds;
	}
	
	public int getRecipeId()
	{
		return recipeId;
	}
	
	@Override
	public void register(QuestEngine questEngine)
	{
		questEngine.addQuestHandler(new WorkOrders(this));
	}
}