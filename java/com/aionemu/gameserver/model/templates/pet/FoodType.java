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
package com.aionemu.gameserver.model.templates.pet;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

/**
 * @author Rolandas
 */
@XmlType(name = "FoodType")
@XmlEnum
public enum FoodType
{
	AETHER_CRYSTAL_BISCUIT,
	AETHER_GEM_BISCUIT,
	AETHER_POWDER_BISCUIT,
	ARMOR,
	BALAUR_SCALES,
	BONES,
	EXCLUDES,
	FLUIDS,
	HEALTHY_FOOD_ALL,
	HEALTHY_FOOD_SPICY,
	INFERNAL_DIABOL_AP,
	INNOCENT_MEREK_XP,
	MISCELLANEOUS,
	NEW_YEAR_PET_FOOD,
	POPPY_SNACK,
	POPPY_SNACK_TASTY,
	POPPY_SNACK_NUTRITIOUS,
	SHUGO_COIN,
	SOULS,
	STINKY,
	THORNS;
	
	public String value()
	{
		return name();
	}
	
	public static FoodType fromValue(String value)
	{
		return valueOf(value);
	}
}