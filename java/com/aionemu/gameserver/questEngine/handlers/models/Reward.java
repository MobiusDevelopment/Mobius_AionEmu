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

import java.math.BigInteger;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "")
@XmlRootElement(name = "reward")
public class Reward
{
	@XmlAttribute(required = true)
	protected BigInteger count;
	
	@XmlAttribute(name = "item_id", required = true)
	protected BigInteger itemId;
	
	@XmlAttribute(required = true)
	protected BigInteger no;
	
	@XmlAttribute(required = true)
	protected BigInteger rank;
	
	public BigInteger getCount()
	{
		return count;
	}
	
	public void setCount(BigInteger value)
	{
		count = value;
	}
	
	public BigInteger getItemId()
	{
		return itemId;
	}
	
	public void setItemId(BigInteger value)
	{
		itemId = value;
	}
	
	public BigInteger getNo()
	{
		return no;
	}
	
	public void setNo(BigInteger value)
	{
		no = value;
	}
	
	public BigInteger getRank()
	{
		return rank;
	}
	
	public void setRank(BigInteger value)
	{
		rank = value;
	}
}