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

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.KillSpawned;

import javolution.util.FastMap;

/**
 * @author vlog, reworked Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "KillSpawnedData")
public class KillSpawnedData extends MonsterHuntData
{
	@XmlElement(name = "spawned_monster", required = true)
	protected List<SpawnedMonster> spawnedMonster;
	
	@Override
	public void register(QuestEngine questEngine)
	{
		final FastMap<List<Integer>, SpawnedMonster> spawnedMonsters = new FastMap<>();
		for (SpawnedMonster m : spawnedMonster)
		{
			spawnedMonsters.put(m.getNpcIds(), m);
		}
		final KillSpawned template = new KillSpawned(id, startNpcIds, endNpcIds, spawnedMonsters);
		questEngine.addQuestHandler(template);
	}
}