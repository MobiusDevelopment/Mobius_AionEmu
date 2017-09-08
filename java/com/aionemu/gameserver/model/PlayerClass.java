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
package com.aionemu.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * This enum represent class that a player may belong to.
 * @author Luno
 */
@XmlEnum
public enum PlayerClass
{
	WARRIOR(0, true),
	GLADIATOR(1),
	TEMPLAR(2),
	SCOUT(3, true),
	ASSASSIN(4),
	RANGER(5),
	MAGE(6, true),
	SORCERER(7),
	SPIRIT_MASTER(8),
	PRIEST(9, true),
	CLERIC(10),
	CHANTER(11),
	
	// News Class 4.3/4.5
	TECHNIST(12, true),
	AETHERTECH(13),
	GUNSLINGER(14),
	MUSE(15, true),
	SONGWEAVER(16),
	ALL(17);
	
	private byte classId;
	private int idMask;
	private boolean startingClass;
	
	private PlayerClass(int classId)
	{
		this(classId, false);
	}
	
	private PlayerClass(int classId, boolean startingClass)
	{
		this.classId = (byte) classId;
		this.startingClass = startingClass;
		idMask = (int) Math.pow(2, classId);
	}
	
	public byte getClassId()
	{
		return classId;
	}
	
	public static PlayerClass getPlayerClassById(byte classId)
	{
		for (PlayerClass pc : values())
		{
			if (pc.getClassId() == classId)
			{
				return pc;
			}
		}
		throw new IllegalArgumentException("There is no player class with id " + classId);
	}
	
	public boolean isStartingClass()
	{
		return startingClass;
	}
	
	public static PlayerClass getStartingClassFor(PlayerClass pc)
	{
		switch (pc)
		{
			case ASSASSIN:
			case RANGER:
			{
				return SCOUT;
			}
			case GLADIATOR:
			case TEMPLAR:
			{
				return WARRIOR;
			}
			case CHANTER:
			case CLERIC:
			{
				return PRIEST;
			}
			case SORCERER:
			case SPIRIT_MASTER:
			{
				return MAGE;
			}
			// News Class 4.3/4.5
			case SONGWEAVER:
			{
				return MUSE;
			}
			case AETHERTECH:
			case GUNSLINGER:
			{
				return TECHNIST;
			}
			case SCOUT:
			case WARRIOR:
			case PRIEST:
			case MAGE:
			case MUSE:
			case TECHNIST:
			{
				return pc;
			}
			default:
			{
				throw new IllegalArgumentException("Given player class is starting class: " + pc);
			}
		}
	}
	
	public static PlayerClass getPlayerClassByString(String fieldName)
	{
		for (PlayerClass pc : values())
		{
			if (pc.toString().equals(fieldName))
			{
				return pc;
			}
		}
		return null;
	}
	
	public int getMask()
	{
		return idMask;
	}
}