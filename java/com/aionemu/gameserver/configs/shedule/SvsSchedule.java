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

@XmlRootElement(name = "svs_schedule")
@XmlAccessorType(XmlAccessType.FIELD)
public class SvsSchedule
{
	@XmlElement(name = "svs", required = true)
	private List<Svs> svssList;
	
	public List<Svs> getSvssList()
	{
		return svssList;
	}
	
	public void setSvssList(List<Svs> svsList)
	{
		svssList = svsList;
	}
	
	public static SvsSchedule load()
	{
		SvsSchedule ss;
		try
		{
			final String xml = FileUtils.readFileToString(new File("./config/shedule/svs_schedule.xml"));
			ss = JAXBUtil.deserialize(xml, SvsSchedule.class);
		}
		catch (final Exception e)
		{
			throw new RuntimeException("Failed to initialize svs", e);
		}
		return ss;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlRootElement(name = "svs")
	public static class Svs
	{
		@XmlAttribute(required = true)
		private int id;
		
		@XmlElement(name = "svsTime", required = true)
		private List<String> svsTimes;
		
		public int getId()
		{
			return id;
		}
		
		public void setId(int id)
		{
			this.id = id;
		}
		
		public List<String> getSvsTimes()
		{
			return svsTimes;
		}
		
		public void setSvsTimes(List<String> svsTimes)
		{
			this.svsTimes = svsTimes;
		}
	}
}