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
package com.aionemu.gameserver.model.ingameshop;

/**
 * @author xTz
 */
public class InGameShop
{
	private byte subCategory;
	private byte category = 2;
	
	public byte getSubCategory()
	{
		return subCategory;
	}
	
	public void setSubCategory(byte subCategory)
	{
		this.subCategory = subCategory;
	}
	
	public byte getCategory()
	{
		return category;
	}
	
	public void setCategory(byte category)
	{
		this.category = category;
	}
}
