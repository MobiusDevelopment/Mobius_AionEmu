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

import java.util.HashSet;
import java.util.Set;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.handlers.template.MentorMonsterHunt;

import javolution.util.FastMap;

/**
 * @author MrPoke reworked Bobobear
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "MentorMonsterHuntData")
public class MentorMonsterHuntData extends MonsterHuntData
{
	@XmlAttribute(name = "min_mente_level")
	protected int minMenteLevel = 1;
	
	@XmlAttribute(name = "max_mente_level")
	protected int maxMenteLevel = 99;
	
	public int getMinMenteLevel()
	{
		return minMenteLevel;
	}
	
	public int getMaxMenteLevel()
	{
		return maxMenteLevel;
	}
	
	@Override
	public void register(QuestEngine questEngine)
	{
		final FastMap<Monster, Set<Integer>> monsterNpcs = new FastMap<>();
		for (Monster m : monster)
		{
			monsterNpcs.put(m, new HashSet<>(m.getNpcIds()));
		}
		final MentorMonsterHunt template = new MentorMonsterHunt(id, startNpcIds, endNpcIds, monsterNpcs, minMenteLevel, maxMenteLevel);
		questEngine.addQuestHandler(template);
	}
}