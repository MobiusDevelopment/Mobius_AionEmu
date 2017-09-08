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
package com.aionemu.gameserver.model.templates;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlList;
import javax.xml.bind.annotation.XmlType;

import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.templates.quest.CollectItems;
import com.aionemu.gameserver.model.templates.quest.InventoryItems;
import com.aionemu.gameserver.model.templates.quest.QuestBonuses;
import com.aionemu.gameserver.model.templates.quest.QuestCategory;
import com.aionemu.gameserver.model.templates.quest.QuestDrop;
import com.aionemu.gameserver.model.templates.quest.QuestItems;
import com.aionemu.gameserver.model.templates.quest.QuestKill;
import com.aionemu.gameserver.model.templates.quest.QuestMentorType;
import com.aionemu.gameserver.model.templates.quest.QuestRepeatCycle;
import com.aionemu.gameserver.model.templates.quest.QuestTargetType;
import com.aionemu.gameserver.model.templates.quest.QuestWorkItems;
import com.aionemu.gameserver.model.templates.quest.Rewards;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;

/**
 * @author MrPoke
 * @modified vlog
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "Quest")

public class QuestTemplate
{
	@XmlElement(name = "collect_items")
	protected CollectItems collectItems;
	@XmlElement(name = "inventory_items")
	protected InventoryItems inventoryItems;
	@XmlElement(name = "rewards")
	protected List<Rewards> rewards;
	@XmlElement(name = "bonus")
	protected List<QuestBonuses> bonus;
	@XmlElement(name = "extended_rewards")
	protected List<Rewards> extendedRewards;
	@XmlElement(name = "quest_drop")
	protected List<QuestDrop> questDrop;
	@XmlElement(name = "quest_kill")
	protected List<QuestKill> questKill;
	@XmlElement(name = "start_conditions")
	protected List<XMLStartCondition> startConds;
	@XmlList
	@XmlElement(name = "class_permitted")
	protected List<PlayerClass> classPermitted;
	@XmlElement(name = "gender_permitted")
	protected Gender genderPermitted;
	@XmlElement(name = "quest_work_items")
	protected QuestWorkItems questWorkItems;
	@XmlElement(name = "fighter_selectable_reward")
	protected List<QuestItems> fighterSelectableReward;
	@XmlElement(name = "knight_selectable_reward")
	protected List<QuestItems> knightSelectableReward;
	@XmlElement(name = "ranger_selectable_reward")
	protected List<QuestItems> rangerSelectableReward;
	@XmlElement(name = "assassin_selectable_reward")
	protected List<QuestItems> assassinSelectableReward;
	@XmlElement(name = "wizard_selectable_reward")
	protected List<QuestItems> wizardSelectableReward;
	@XmlElement(name = "elementalist_selectable_reward")
	protected List<QuestItems> elementalistSelectableReward;
	@XmlElement(name = "priest_selectable_reward")
	protected List<QuestItems> priestSelectableReward;
	@XmlElement(name = "chanter_selectable_reward")
	protected List<QuestItems> chanterSelectableReward;
	@XmlElement(name = "gunslinger_selectable_reward")
	protected List<QuestItems> gunslingerSelectableReward;
	@XmlElement(name = "songweaver_selectable_reward")
	protected List<QuestItems> songweaverSelectableReward;
	@XmlElement(name = "aethertech_selectable_reward")
	protected List<QuestItems> aethertechSelectableReward;
	
	@XmlAttribute(name = "id", required = true)
	protected int id;
	@XmlAttribute(name = "name")
	protected String name;
	@XmlAttribute(name = "nameId")
	protected Integer nameId;
	@XmlAttribute(name = "minlevel_permitted")
	protected Integer minlevelPermitted;
	@XmlAttribute(name = "maxlevel_permitted")
	protected int maxlevelPermitted;
	@XmlAttribute(name = "max_repeat_count")
	protected Integer maxRepeatCount;
	@XmlAttribute(name = "quest_cooltime")
	protected int questCooltime;
	@XmlAttribute(name = "rank")
	private int rank;
	@XmlAttribute(name = "max_count_limited_quest")
	protected Integer maxCountLimitedQuest;
	@XmlAttribute(name = "count_recover_limited_quest")
	protected Integer countRecoverLimitedQuest;
	@XmlAttribute(name = "cannot_share")
	protected Boolean cannotShare;
	@XmlAttribute(name = "cannot_giveup")
	protected Boolean cannotGiveup;
	@XmlAttribute(name = "bounty_reward")
	protected Boolean bountyReward;
	@XmlAttribute(name = "use_class_reward")
	protected Integer useClassReward;
	@XmlAttribute(name = "race_permitted")
	protected Race racePermitted;
	@XmlAttribute(name = "combineskill")
	protected Integer combineskill;
	@XmlAttribute(name = "combine_skillpoint")
	protected Integer combineSkillpoint;
	@XmlAttribute(name = "timer")
	protected Boolean timer;
	@XmlAttribute(name = "category")
	protected QuestCategory category;
	@XmlAttribute(name = "repeat_cycle")
	protected List<QuestRepeatCycle> repeatCycle;
	@XmlAttribute(name = "npcfaction_id")
	protected int npcFactionId;
	@XmlAttribute(name = "mentor_type")
	protected QuestMentorType mentorType = QuestMentorType.NONE;
	@XmlAttribute(name = "target_type")
	private final QuestTargetType targetType = QuestTargetType.NONE;
	@XmlAttribute(name = "titleId")
	protected int titleId;
	
	/**
	 * Gets the value of the collectItems property.
	 * @return possible object is {@link CollectItems }
	 */
	public CollectItems getCollectItems()
	{
		return collectItems;
	}
	
	public InventoryItems getInventoryItems()
	{
		return inventoryItems;
	}
	
	/**
	 * Gets the value of the rewards property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the rewards property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRewards().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link Rewards }
	 */
	public List<Rewards> getRewards()
	{
		if (rewards == null)
		{
			rewards = new ArrayList<>();
		}
		return rewards;
	}
	
	public List<Rewards> getExtendedRewards()
	{
		if (extendedRewards == null)
		{
			extendedRewards = new ArrayList<>();
		}
		return extendedRewards;
	}
	
	public List<QuestBonuses> getBonus()
	{
		if (bonus == null)
		{
			bonus = new ArrayList<>();
		}
		return bonus;
	}
	
	/**
	 * Gets the value of the questDrop property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the questDrop property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getQuestDrop().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestDrop }
	 */
	public List<QuestDrop> getQuestDrop()
	{
		if (questDrop == null)
		{
			questDrop = new ArrayList<>();
		}
		return questDrop;
	}
	
	public List<QuestKill> getQuestKill()
	{
		if (questKill == null)
		{
			questKill = new ArrayList<>();
		}
		return questKill;
	}
	
	public List<XMLStartCondition> getXMLStartConditions()
	{
		if (startConds == null)
		{
			startConds = new ArrayList<>();
		}
		return startConds;
	}
	
	/**
	 * Gets the value of the classPermitted property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the classPermitted property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getClassPermitted().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link PlayerClass }
	 */
	public List<PlayerClass> getClassPermitted()
	{
		if (classPermitted == null)
		{
			classPermitted = new ArrayList<>();
		}
		return classPermitted;
	}
	
	/**
	 * Gets the value of the genderPermitted property.
	 * @return possible object is {@link Gender }
	 */
	public Gender getGenderPermitted()
	{
		return genderPermitted;
	}
	
	/**
	 * Gets the value of the questWorkItems property.
	 * @return possible object is {@link QuestWorkItems }
	 */
	public QuestWorkItems getQuestWorkItems()
	{
		return questWorkItems;
	}
	
	/**
	 * Gets the value of the fighterSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the fighterSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getFighterSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getFighterSelectableReward()
	{
		if (fighterSelectableReward == null)
		{
			fighterSelectableReward = new ArrayList<>();
		}
		return fighterSelectableReward;
	}
	
	/**
	 * Gets the value of the knightSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the knightSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getKnightSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getKnightSelectableReward()
	{
		if (knightSelectableReward == null)
		{
			knightSelectableReward = new ArrayList<>();
		}
		return knightSelectableReward;
	}
	
	/**
	 * Gets the value of the rangerSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the rangerSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getRangerSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getRangerSelectableReward()
	{
		if (rangerSelectableReward == null)
		{
			rangerSelectableReward = new ArrayList<>();
		}
		return rangerSelectableReward;
	}
	
	/**
	 * Gets the value of the assassinSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the assassinSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAssassinSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getAssassinSelectableReward()
	{
		if (assassinSelectableReward == null)
		{
			assassinSelectableReward = new ArrayList<>();
		}
		return assassinSelectableReward;
	}
	
	/**
	 * Gets the value of the wizardSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the wizardSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getWizardSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getWizardSelectableReward()
	{
		if (wizardSelectableReward == null)
		{
			wizardSelectableReward = new ArrayList<>();
		}
		return wizardSelectableReward;
	}
	
	/**
	 * Gets the value of the elementalistSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the elementalistSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getElementalistSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getElementalistSelectableReward()
	{
		if (elementalistSelectableReward == null)
		{
			elementalistSelectableReward = new ArrayList<>();
		}
		return elementalistSelectableReward;
	}
	
	/**
	 * Gets the value of the priestSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the priestSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getPriestSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getPriestSelectableReward()
	{
		if (priestSelectableReward == null)
		{
			priestSelectableReward = new ArrayList<>();
		}
		return priestSelectableReward;
	}
	
	/**
	 * Gets the value of the chanterSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the chanterSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getChanterSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getChanterSelectableReward()
	{
		if (chanterSelectableReward == null)
		{
			chanterSelectableReward = new ArrayList<>();
		}
		return chanterSelectableReward;
	}
	
	/**
	 * Gets the value of the gunslingerSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the GunslingerSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getGunslingerSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getGunslingerSelectableReward()
	{
		if (gunslingerSelectableReward == null)
		{
			gunslingerSelectableReward = new ArrayList<>();
		}
		return gunslingerSelectableReward;
	}
	
	/**
	 * Gets the value of the songweaverSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the songweaverSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getSongweaverSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getSongweaverSelectableReward()
	{
		if (songweaverSelectableReward == null)
		{
			songweaverSelectableReward = new ArrayList<>();
		}
		return songweaverSelectableReward;
	}
	
	/**
	 * Gets the value of the aethertechSelectableReward property.
	 * <p>
	 * This accessor method returns a reference to the live list, not a snapshot. Therefore any modification you make to the returned list will be present inside the JAXB object. This is why there is not a <CODE>set</CODE> method for the aethertechSelectableReward property.
	 * <p>
	 * For example, to add a new item, do as follows:
	 * 
	 * <pre>
	 * getAethertechSelectableReward().add(newItem);
	 * </pre>
	 * <p>
	 * Objects of the following type(s) are allowed in the list {@link QuestItems }
	 */
	public List<QuestItems> getAethertechSelectableReward()
	{
		if (aethertechSelectableReward == null)
		{
			aethertechSelectableReward = new ArrayList<>();
		}
		return aethertechSelectableReward;
	}
	
	/**
	 * Gets the value of the id property.
	 */
	public int getId()
	{
		return id;
	}
	
	/**
	 * Gets the value of the name property.
	 * @return possible object is {@link String }
	 */
	public String getName()
	{
		return name;
	}
	
	/**
	 * Gets the value of the nameId property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getNameId()
	{
		return nameId;
	}
	
	/**
	 * Gets the value of the minlevelPermitted property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getMinlevelPermitted()
	{
		return minlevelPermitted;
	}
	
	public int getMaxlevelPermitted()
	{
		return maxlevelPermitted;
	}
	
	public int getRequiredRank()
	{
		return rank;
	}
	
	/**
	 * Gets the value of the maxRepeatCount property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getMaxRepeatCount()
	{
		if ((maxRepeatCount == null) || !(maxRepeatCount > 1))
		{
			return 1;
		}
		return maxRepeatCount;
	}
	
	/**
	 * Gets the value of the maxCountLimitedQuest property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getMaxCountLimitedQuest()
	{
		if ((maxCountLimitedQuest == null) || !(maxCountLimitedQuest > 1))
		{
			return 1;
		}
		return maxCountLimitedQuest;
	}
	
	/**
	 * Gets the value of the countRecoverLimitedQuest property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getCountRecoverLimitedQuest()
	{
		if ((countRecoverLimitedQuest == null) || !(countRecoverLimitedQuest > 1))
		{
			return 1;
		}
		return countRecoverLimitedQuest;
	}
	
	/**
	 * Gets the value of the cannotShare property.
	 * @return possible object is {@link Boolean }
	 */
	public boolean isCannotShare()
	{
		if (cannotShare == null)
		{
			return false;
		}
		else
		{
			return cannotShare;
		}
	}
	
	/**
	 * Gets the value of the cannotGiveup property.
	 * @return possible object is {@link Boolean }
	 */
	public boolean isCannotGiveup()
	{
		if (cannotGiveup == null)
		{
			return false;
		}
		else
		{
			return cannotGiveup;
		}
	}
	
	public boolean isBountyReward()
	{
		if (bountyReward == null)
		{
			return false;
		}
		else
		{
			return bountyReward;
		}
	}
	
	public boolean isUseSingleClassReward()
	{
		if (useClassReward == null)
		{
			return false;
		}
		else
		{
			return useClassReward == 1;
		}
	}
	
	public boolean isUseRepeatedClassReward()
	{
		if (useClassReward == null)
		{
			return false;
		}
		else
		{
			return useClassReward == 2;
		}
	}
	
	public boolean isRepeatable()
	{
		return getMaxRepeatCount() > 1;
	}
	
	/**
	 * Gets the value of the racePermitted property.
	 * @return possible object is {@link Race }
	 */
	public Race getRacePermitted()
	{
		return racePermitted;
	}
	
	/**
	 * Gets the value of the combineskill property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getCombineSkill()
	{
		return combineskill;
	}
	
	/**
	 * Gets the value of the combineSkillpoint property.
	 * @return possible object is {@link Integer }
	 */
	public Integer getCombineSkillPoint()
	{
		return combineSkillpoint;
	}
	
	/**
	 * Gets the value of the timer property.
	 * @return possible object is {@link Integer }
	 */
	
	public boolean isTimer()
	{
		if (timer == null)
		{
			return false;
		}
		else
		{
			return timer;
		}
	}
	
	public QuestCategory getCategory()
	{
		if (category == null)
		{
			category = QuestCategory.QUEST;
		}
		return category;
	}
	
	/**
	 * @return the mentor
	 */
	public boolean isMentor()
	{
		return mentorType != QuestMentorType.NONE;
	}
	
	/**
	 * @return the mentor
	 */
	public QuestMentorType getMentorType()
	{
		return mentorType;
	}
	
	public QuestTargetType getTargetType()
	{
		return targetType;
	}
	
	public List<QuestRepeatCycle> getRepeatCycle()
	{
		return repeatCycle;
	}
	
	public int getTitleId()
	{
		return titleId;
	}
	
	public int getNpcFactionId()
	{
		return npcFactionId;
	}
	
	public boolean isTimeBased()
	{
		return repeatCycle != null;
	}
	
	public int getQuestCoolTime()
	{
		return questCooltime;
	}
	
	public boolean isDaily()
	{
		return isTimeBased() && (repeatCycle.size() == 1) && (repeatCycle.get(0) == QuestRepeatCycle.ALL);
	}
	
	public boolean isWeekly()
	{
		return isTimeBased() && !isDaily();
	}
	
	public boolean isMaster()
	{
		return (getCombineSkillPoint() != null) && (getCombineSkillPoint() == 499);
	}
	
	public boolean isExpert()
	{
		return (getCombineSkillPoint() != null) && (getCombineSkillPoint() == 399);
	}
	
	public boolean isNoCount()
	{
		return category.equals(QuestCategory.NON_COUNT) || category.equals(QuestCategory.EVENT);
	}
}