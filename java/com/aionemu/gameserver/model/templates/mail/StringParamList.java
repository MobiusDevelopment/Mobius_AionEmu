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
package com.aionemu.gameserver.model.templates.mail;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlSeeAlso;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "StringParamList", propOrder =
{
	"param"
})
@XmlSeeAlso(
{
	MailPart.class
})
public class StringParamList
{
	
	protected List<Param> param;
	
	public List<Param> getParam()
	{
		if (param == null)
		{
			param = new ArrayList<>();
		}
		return param;
	}
	
	@XmlAccessorType(XmlAccessType.FIELD)
	@XmlType(name = "")
	public static class Param
	{
		
		@XmlAttribute(name = "id", required = true)
		protected String id;
		
		public String getId()
		{
			return id;
		}
		
	}
}
