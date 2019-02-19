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
package com.aionemu.gameserver.configs.shedule;

import java.io.File;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.FileUtils;

import com.aionemu.commons.utils.xml.JAXBUtil;

/**
 * @author Rinzler (Encom)
 */
@XmlRootElement(name = "dredgion_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class DredgionSchedule
{
	@XmlElement(name = "dredgion", required = true)
	private List<Dredgion> dredgionsList;
	
	public List<Dredgion> getDredgionsList()
	{
		return dredgionsList;
	}
	
	public void setZorshivsList(List<Dredgion> dredgionList)
	{
		dredgionsList = dredgionList;
	}
	
	@SuppressWarnings("deprecation")
	public static DredgionSchedule load()
	{
		DredgionSchedule ds;
		try
		{
			final String xml = FileUtils.readFileToString(new File("./config/shedule/dredgion_schedule.xml"));
			ds = JAXBUtil.deserialize(xml, DredgionSchedule.class);
		}
		catch (Exception e)
		{
			throw new RuntimeException("Failed to initialize dredgion", e);
		}
		return ds;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "dredgion")
	public static class Dredgion
	{
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "zorshivTime", required = true)
		private List<String> zorshivTimes;
		
		public int getId()
		{
			return id;
		}
		
		public void setId(int id)
		{
			this.id = id;
		}
		
		public List<String> getZorshivTimes()
		{
			return zorshivTimes;
		}
		
		public void setZorshivTimes(List<String> zorshivTimes)
		{
			this.zorshivTimes = zorshivTimes;
		}
	}
}