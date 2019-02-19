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
package com.aionemu.gameserver.dataholders;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.teleport.TeleporterTemplate;

/**
 * This is a container holding and serving all {@link NpcTemplate} instances.<br>
 * Briefly: Every {@link Npc} instance represents some class of NPCs among which each have the same id, name, items, statistics. Data for such NPC class is defined in {@link NpcTemplate} and is uniquely identified by npc id.
 * @author orz
 */
@XmlRootElement(name = "npc_teleporter")
@XmlAccessorType(XmlAccessType.FIELD)
public class TeleporterData
{
	@XmlElement(name = "teleporter_template")
	private List<TeleporterTemplate> teleporterTemplates;
	
	public int size()
	{
		return teleporterTemplates.size();
	}
	
	public TeleporterTemplate getTeleporterTemplateByNpcId(int npcId)
	{
		for (TeleporterTemplate template : teleporterTemplates)
		{
			if (template.getNpcIds().contains(npcId))
			{
				return template;
			}
		}
		return null;
	}
	
	/**
	 * Returns an {@link NpcTemplate} object with given id.
	 * @param teleportId id of NPC
	 * @return NpcTemplate object containing data about NPC with that id.
	 */
	public TeleporterTemplate getTeleporterTemplateByTeleportId(int teleportId)
	{
		for (TeleporterTemplate template : teleporterTemplates)
		{
			if (template.getTeleportId() == teleportId)
			{
				return template;
			}
		}
		return null;
	}
}
