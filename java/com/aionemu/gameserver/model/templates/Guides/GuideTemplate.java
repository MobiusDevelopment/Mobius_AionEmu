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
package com.aionemu.gameserver.model.templates.Guides;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.apache.commons.lang.StringUtils;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;

/**
 * @author xTz
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "GuideTemplate")
public class GuideTemplate
{
	@XmlAttribute(name = "level")
	private int level;
	@XmlAttribute(name = "classType")
	private PlayerClass classType;
	@XmlAttribute(name = "title")
	private String title;
	@XmlAttribute(name = "race")
	private Race race;
	@XmlElement(name = "reward_info")
	protected String rewardInfo = StringUtils.EMPTY;
	@XmlElement(name = "message")
	protected String message = StringUtils.EMPTY;
	@XmlElement(name = "select")
	protected String select = StringUtils.EMPTY;
	@XmlElement(name = "survey")
	private List<SurveyTemplate> surveys;
	@XmlAttribute(name = "rewardCount")
	private int rewardCount;
	@XmlTransient
	private boolean isActivated = true;
	
	/**
	 * @return the level
	 */
	public int getLevel()
	{
		return level;
	}
	
	/**
	 * @return the classId
	 */
	public PlayerClass getPlayerClass()
	{
		return classType;
	}
	
	/**
	 * @return the title
	 */
	public String getTitle()
	{
		return title;
	}
	
	/**
	 * @return the race
	 */
	public Race getRace()
	{
		return race;
	}
	
	/**
	 * @return the surveys
	 */
	public List<SurveyTemplate> getSurveys()
	{
		return surveys;
	}
	
	/**
	 * @return the message
	 */
	public String getMessage()
	{
		return message;
	}
	
	/**
	 * @return the select
	 */
	public String getSelect()
	{
		return select;
	}
	
	/**
	 * @return the select
	 */
	public String getRewardInfo()
	{
		return rewardInfo;
	}
	
	public int getRewardCount()
	{
		return rewardCount;
	}
	
	/**
	 * @return the isActivated
	 */
	public boolean isActivated()
	{
		return isActivated;
	}
	
	/**
	 * @param isActivated the isActivated to set
	 */
	public void setActivated(boolean isActivated)
	{
		this.isActivated = isActivated;
	}
}
