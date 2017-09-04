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
package com.aionemu.gameserver.model.gameobjects.player;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.GameServer;
import com.aionemu.gameserver.configs.main.AdvCustomConfig;
import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.Gender;
import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.team.legion.LegionJoinRequestState;
import com.aionemu.gameserver.model.templates.BoundRadius;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.event.AtreianPassport;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DP_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_DP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATUPDATE_EXP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.player.CreativityPanel.CreativityEssenceService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.XPLossEnum;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * This class is holding base information about player, that may be used even when player itself is not online.
 * @author Luno
 * @modified cura
 */
public class PlayerCommonData extends VisibleObjectTemplate
{
	static Logger log = LoggerFactory.getLogger(PlayerCommonData.class);
	private final int playerObjId;
	private Race race;
	private String name;
	private PlayerClass playerClass;
	private int level = 0;
	private long exp = 0;
	private long expRecoverable = 0;
	private Gender gender;
	private Timestamp lastOnline = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private Timestamp lastStamp = new Timestamp(Calendar.getInstance().getTime().getTime() - 20);
	private boolean online;
	private String note;
	private WorldPosition position;
	private int questExpands = 0;
	private int npcExpands = AdvCustomConfig.CUBE_SIZE;
	private int warehouseSize = 0;
	private int advencedStigmaSlotSize = 0;
	private int titleId = -1;
	private int bonusTitleId = -1;
	private final int cubeExpands = 0;
	private int dp = 0;
	private int mailboxLetters;
	private int soulSickness = 0;
	private boolean noExp = false;
	private long reposteCurrent;
	private long reposteMax;
	private long salvationPoint;
	private int mentorFlagTime;
	private int worldOwnerId;
	private BoundRadius boundRadius;
	private long lastTransferTime;
	private int stamps = 0;
	private int passportReward = 0;
	public Map<Integer, AtreianPassport> playerPassports = new HashMap<>(1);
	private PlayerPassports completedPassports;
	private boolean isArchDaeva = false;
	private int creativityPoint;
	private int cp_step = 0;
	private int joinRequestLegionId = 0;
	private LegionJoinRequestState joinRequestState = LegionJoinRequestState.NONE;
	private int lunaConsumePoint;
	private int muni_keys;
	private int consumeCount = 0;
	private int wardrobeSlot;
	private long goldenStarEnergy;
	private final long goldenStarEnergyMax = 625000000;
	private long growthEnergy;
	private long growthEnergyMax;
	private boolean GoldenStarBoost = false;
	
	public PlayerCommonData(int objId)
	{
		playerObjId = objId;
	}
	
	public int getPlayerObjId()
	{
		return playerObjId;
	}
	
	public long getExp()
	{
		return exp;
	}
	
	public int getQuestExpands()
	{
		return questExpands;
	}
	
	public void setQuestExpands(int questExpands)
	{
		this.questExpands = questExpands;
	}
	
	public void setNpcExpands(int npcExpands)
	{
		this.npcExpands = npcExpands;
	}
	
	public int getNpcExpands()
	{
		return npcExpands;
	}
	
	/**
	 * @return the advencedStigmaSlotSize
	 */
	public int getAdvencedStigmaSlotSize()
	{
		return advencedStigmaSlotSize;
	}
	
	/**
	 * @param advencedStigmaSlotSize the advencedStigmaSlotSize to set
	 */
	public void setAdvencedStigmaSlotSize(int advencedStigmaSlotSize)
	{
		this.advencedStigmaSlotSize = advencedStigmaSlotSize;
	}
	
	public long getExpShown()
	{
		return exp - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level);
	}
	
	public long getExpNeed()
	{
		if (level == DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel())
		{
			return 0;
		}
		return DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level + 1) - DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level);
	}
	
	/**
	 * calculate the lost experience must be called before setexp
	 * @author Jangan
	 */
	public void calculateExpLoss()
	{
		final long expLost = XPLossEnum.getExpLoss(level, getExpNeed());
		
		final int unrecoverable = (int) (expLost * 0.33333333);
		final int recoverable = (int) expLost - unrecoverable;
		final long allExpLost = recoverable + expRecoverable;
		
		if (getExpShown() > unrecoverable)
		{
			exp = exp - unrecoverable;
		}
		else
		{
			exp = exp - getExpShown();
		}
		if (getExpShown() > recoverable)
		{
			expRecoverable = allExpLost;
			exp = exp - recoverable;
		}
		else
		{
			expRecoverable = expRecoverable + getExpShown();
			exp = exp - getExpShown();
		}
		
		if (expRecoverable > (getExpNeed() * 0.25D))
		{
			expRecoverable = Math.round(getExpNeed() * 0.25D);
		}
		
		if (getPlayer() != null)
		{
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), getCurrentReposteEnergy(), getMaxReposteEnergy()));
		}
	}
	
	public void setRecoverableExp(long expRecoverable)
	{
		this.expRecoverable = expRecoverable;
	}
	
	public void resetRecoverableExp()
	{
		final long el = expRecoverable;
		expRecoverable = 0;
		setExp(exp + el, false);
	}
	
	public long getExpRecoverable()
	{
		return expRecoverable;
	}
	
	/**
	 * @param value
	 * @param npcNameId
	 */
	public void addExp(long value, int npcNameId)
	{
		addExp(value, null, npcNameId, "");
	}
	
	public void addExp(long value, RewardType rewardType)
	{
		addExp(value, rewardType, 0, "");
	}
	
	public void addExp(long value, RewardType rewardType, int npcNameId)
	{
		addExp(value, rewardType, npcNameId, "");
	}
	
	public void addExp(long value, RewardType rewardType, String name)
	{
		addExp(value, rewardType, 0, name);
	}
	
	public void addExp(long value, RewardType rewardType, int npcNameId, String name)
	{
		if (noExp)
		{
			return;
		}
		long reward = value;
		if ((getPlayer() != null) && (rewardType != null))
		{
			reward = rewardType.calcReward(getPlayer(), value);
		}
		long repose = 0;
		if ((isReadyForReposteEnergy()) && (getCurrentReposteEnergy() > 0))
		{
			repose = (long) ((reward / 100.0) * 40.0);
			addReposteEnergy(-repose);
		}
		long salvation = 0;
		if ((isReadyForSalvationPoints()) && (getCurrentSalvationPercent() > 0))
		{
			salvation = (long) ((reward / 100.0) * getCurrentSalvationPercent());
		}
		long goldenstar = 0;
		long goldenstarboost = 0;
		if ((isReadyForGoldenStarEnergy()) && (getGoldenStarEnergy() > 0))
		{
			goldenstar = reward;
			addGoldenStarEnergy(3150000); // 0.5%
			if (GoldenStarBoost)
			{
				goldenstarboost = (long) ((reward / 100.0) * 50.0);
			}
		}
		long growth = 0;
		if ((isReadyForGrowthEnergy()) && (getGrowthEnergy() > 0))
		{
			growth = (long) ((reward / 100.0) * 60.0);
			addGrowthEnergy(-growth);
		}
		if ((getPlayer() != null) && (rewardType != null))
		{
			if ((rewardType == RewardType.HUNTING) || (rewardType == RewardType.GROUP_HUNTING) || (rewardType == RewardType.CRAFTING) || (rewardType == RewardType.GATHERING))
			{
				reward += repose + goldenstar + goldenstarboost + growth;
			}
			else
			{
				reward += repose;
			}
		}
		setExp(exp + reward, false);
		if ((getPlayer() != null) && (rewardType != null))
		{
			switch (rewardType)
			{
				case HUNTING:
				case GROUP_HUNTING:
				case CRAFTING:
				case GATHERING:
				{
					if (npcNameId == 0)
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
					}
					else
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId((npcNameId * 2) + 1), reward));
						if (repose > 0)
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2805577), repose));
						}
						if (growth > 0)
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806377), growth));
						}
						if (goldenstar > 0)
						{
							PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId(2806671), goldenstar));
						}
					}
					break;
				}
				case QUEST:
				{
					if (npcNameId == 0)
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP2(reward));
					}
					else if ((repose > 0) && (salvation > 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS_DESC(new DescriptionId((npcNameId * 2) + 1), reward, repose, salvation));
					}
					else if ((repose > 0) && (salvation == 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS_DESC(new DescriptionId((npcNameId * 2) + 1), reward, repose));
					}
					else if ((repose == 0) && (salvation > 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS_DESC(new DescriptionId((npcNameId * 2) + 1), reward, salvation));
					}
					else
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_DESC(new DescriptionId((npcNameId * 2) + 1), reward));
					}
					break;
				}
				case PVP_KILL:
				{
					if ((repose > 0) && (salvation > 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_MAKEUP_BONUS(name, reward, repose, salvation));
					}
					else if ((repose > 0) && (salvation == 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_VITAL_BONUS(name, reward, repose));
					}
					else if ((repose == 0) && (salvation > 0))
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP_MAKEUP_BONUS(name, reward, salvation));
					}
					else
					{
						PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_GET_EXP(name, reward));
					}
					break;
				}
				default:
				{
					break;
				}
			}
			if (isArchDaeva())
			{
				CreativityEssenceService.getInstance().pointPerExp(getPlayer());
			}
		}
	}
	
	public boolean isReadyForSalvationPoints()
	{
		return (level >= 15) && (level < (GSConfig.PLAYER_MAX_LEVEL + 1));
	}
	
	public boolean isReadyForReposteEnergy()
	{
		return CustomConfig.ENERGY_OF_REPOSE_ENABLE && (level >= 10);
	}
	
	public void addReposteEnergy(long add)
	{
		if (!isReadyForReposteEnergy())
		{
			return;
		}
		reposteCurrent += add;
		if (reposteCurrent < 0)
		{
			reposteCurrent = 0;
		}
		else if (reposteCurrent > getMaxReposteEnergy())
		{
			reposteCurrent = getMaxReposteEnergy();
		}
	}
	
	public void updateMaxReposte()
	{
		if (!isReadyForReposteEnergy())
		{
			reposteCurrent = 0;
			reposteMax = 0;
		}
		else
		{
			reposteMax = (long) (getExpNeed() * 0.25f); // Retail 99%
		}
	}
	
	public void setCurrentReposteEnergy(long value)
	{
		reposteCurrent = value;
	}
	
	public long getCurrentReposteEnergy()
	{
		return isReadyForReposteEnergy() ? reposteCurrent : 0;
	}
	
	public long getMaxReposteEnergy()
	{
		return isReadyForReposteEnergy() ? reposteMax : 0;
	}
	
	public void setExp(long exp, boolean ArchDaeva)
	{
		int maxLevel = DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel();
		long maxExp = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(maxLevel);
		if ((getPlayerClass() != null) && getPlayerClass().isStartingClass())
		{
			maxLevel = GSConfig.STARTING_LEVEL > GSConfig.STARTCLASS_MAXLEVEL ? GSConfig.STARTING_LEVEL : GSConfig.STARTCLASS_MAXLEVEL;
			if ((getLevel() == 9) && (getExp() >= 74059))
			{
				// You can advance to level 10 only after you have completed the class change quest.
				PacketSendUtility.sendPacket(getPlayer(), SM_SYSTEM_MESSAGE.STR_LEVEL_LIMIT_QUEST_NOT_FINISHED1);
			}
		}
		else if ((getLevel() == 65) && !isArchDaeva())
		{
			boolean isCompleteQuest = false;
			if (getPlayer().getRace() == Race.ELYOS)
			{
				isCompleteQuest = getPlayer().isCompleteQuest(10520); // Covert Communiques.
			}
			else
			{
				isCompleteQuest = getPlayer().isCompleteQuest(20520); // Lost Destiny.
			}
			if (!isCompleteQuest)
			{
				maxExp = 273566576;
				if (getExp() >= 273566576)
				{
					// You can advance to level 66 only after you have completed the Transcendence quest.
					PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403187, "66"));
				}
			}
		}
		if (exp > maxExp)
		{
			exp = maxExp;
		}
		final int oldLvl = level;
		this.exp = exp;
		boolean up = false;
		while ((((level + 1) < maxLevel) && (up = exp >= DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level + 1))) || (((level - 1) >= 0) && (exp < DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level))))
		{
			if (up)
			{
				level++;
			}
			else
			{
				level--;
			}
			upgradePlayerData();
		}
		if (getPlayer() != null)
		{
			if (up && GSConfig.ENABLE_RATIO_LIMITATION)
			{
				if ((level >= GSConfig.RATIO_MIN_REQUIRED_LEVEL) && (getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1))
				{
					GameServer.updateRatio(getRace(), 1);
				}
				if ((level >= GSConfig.RATIO_MIN_REQUIRED_LEVEL) && (getPlayer().getPlayerAccount().getNumberOf(getRace()) == 1))
				{
					GameServer.updateRatio(getRace(), -1);
				}
			}
			if (oldLvl != level)
			{
				updateMaxReposte();
				updateMaxGrowthEnergy();
			}
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_EXP(getExpShown(), getExpRecoverable(), getExpNeed(), getCurrentReposteEnergy(), getMaxReposteEnergy()));
		}
	}
	
	private void upgradePlayerData()
	{
		final Player player = getPlayer();
		if (player != null)
		{
			player.getController().upgradePlayer();
			resetSalvationPoints();
		}
	}
	
	public void setNoExp(boolean value)
	{
		noExp = value;
	}
	
	public boolean getNoExp()
	{
		return noExp;
	}
	
	/**
	 * @return Race as from template
	 */
	public final Race getRace()
	{
		return race;
	}
	
	public Race getOppositeRace()
	{
		return race == Race.ELYOS ? Race.ASMODIANS : Race.ELYOS;
	}
	
	/**
	 * @return the mentorFlagTime
	 */
	public int getMentorFlagTime()
	{
		return mentorFlagTime;
	}
	
	public boolean isHaveMentorFlag()
	{
		return mentorFlagTime > (System.currentTimeMillis() / 1000);
	}
	
	/**
	 * @param mentorFlagTime the mentorFlagTime to set
	 */
	public void setMentorFlagTime(int mentorFlagTime)
	{
		this.mentorFlagTime = mentorFlagTime;
	}
	
	public void setRace(Race race)
	{
		this.race = race;
	}
	
	@Override
	public String getName()
	{
		return name;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public PlayerClass getPlayerClass()
	{
		return playerClass;
	}
	
	public void setPlayerClass(PlayerClass playerClass)
	{
		this.playerClass = playerClass;
	}
	
	public boolean isOnline()
	{
		return online;
	}
	
	public void setOnline(boolean online)
	{
		this.online = online;
	}
	
	public Gender getGender()
	{
		return gender;
	}
	
	public void setGender(Gender gender)
	{
		this.gender = gender;
	}
	
	public WorldPosition getPosition()
	{
		return position;
	}
	
	public Timestamp getLastOnline()
	{
		return lastOnline;
	}
	
	public void setLastOnline(Timestamp timestamp)
	{
		lastOnline = timestamp;
	}
	
	public int getLevel()
	{
		return level;
	}
	
	public void setLevel(int level)
	{
		if (level <= DataManager.PLAYER_EXPERIENCE_TABLE.getMaxLevel())
		{
			setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(level), false);
		}
	}
	
	// ArchDaeva Update
	public void setArchDaeva()
	{
		setArchDaeva(true);
		if (getLevel() < 66)
		{
			setExp(DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(66), true);
			addGrowthEnergy(1060000 * 10);
		}
		else if (getLevel() >= 66)
		{
			return;
		}
	}
	
	public String getNote()
	{
		return note;
	}
	
	public void setNote(String note)
	{
		this.note = note;
	}
	
	public int getTitleId()
	{
		return titleId;
	}
	
	public void setTitleId(int titleId)
	{
		this.titleId = titleId;
	}
	
	public int getBonusTitleId()
	{
		return bonusTitleId;
	}
	
	public void setBonusTitleId(int bonusTitleId)
	{
		this.bonusTitleId = bonusTitleId;
	}
	
	/**
	 * This method should be called exactly once after creating object of this class
	 * @param position
	 */
	public void setPosition(WorldPosition position)
	{
		if (this.position != null)
		{
			throw new IllegalStateException("position already set");
		}
		this.position = position;
	}
	
	/**
	 * Gets the cooresponding Player for this common data. Returns null if the player is not online
	 * @return Player or null
	 */
	public Player getPlayer()
	{
		if (online && (getPosition() != null))
		{
			return World.getInstance().findPlayer(playerObjId);
		}
		return null;
	}
	
	public void addDp(int dp)
	{
		setDp(this.dp + dp);
	}
	
	/**
	 * //TODO move to lifestats -> db save?
	 * @param dp
	 */
	public void setDp(int dp)
	{
		if (getPlayer() != null)
		{
			if (playerClass.isStartingClass())
			{
				return;
			}
			
			final int maxDp = getPlayer().getGameStats().getMaxDp().getCurrent();
			this.dp = dp > maxDp ? maxDp : dp;
			
			PacketSendUtility.broadcastPacket(getPlayer(), new SM_DP_INFO(playerObjId, this.dp), true);
			getPlayer().getGameStats().updateStatsAndSpeedVisually();
			PacketSendUtility.sendPacket(getPlayer(), new SM_STATUPDATE_DP(this.dp));
		}
		else
		{
			log.debug("CHECKPOINT : getPlayer in PCD return null for setDP " + isOnline() + " " + getPosition());
		}
	}
	
	public int getDp()
	{
		return dp;
	}
	
	@Override
	public int getTemplateId()
	{
		return 100000 + (race.getRaceId() * 2) + gender.getGenderId();
	}
	
	@Override
	public int getNameId()
	{
		return 0;
	}
	
	/**
	 * @param warehouseSize the warehouseSize to set
	 */
	public void setWarehouseSize(int warehouseSize)
	{
		this.warehouseSize = warehouseSize;
	}
	
	/**
	 * @return the warehouseSize
	 */
	public int getWarehouseSize()
	{
		return warehouseSize;
	}
	
	public void setMailboxLetters(int count)
	{
		mailboxLetters = count;
	}
	
	public int getMailboxLetters()
	{
		return mailboxLetters;
	}
	
	/**
	 * @param boundRadius
	 */
	public void setBoundingRadius(BoundRadius boundRadius)
	{
		this.boundRadius = boundRadius;
	}
	
	@Override
	public BoundRadius getBoundRadius()
	{
		return boundRadius;
	}
	
	public void setDeathCount(int count)
	{
		soulSickness = count;
	}
	
	public int getDeathCount()
	{
		return soulSickness;
	}
	
	/**
	 * Value returned here means % of exp bonus.
	 * @return
	 */
	public byte getCurrentSalvationPercent()
	{
		if (salvationPoint <= 0)
		{
			return 0;
		}
		final long per = salvationPoint / 1000;
		if (per > 30)
		{
			return 30;
		}
		return (byte) per;
	}
	
	public void addSalvationPoints(long points)
	{
		salvationPoint += points;
	}
	
	public void resetSalvationPoints()
	{
		salvationPoint = 0;
	}
	
	public void setLastTransferTime(long value)
	{
		lastTransferTime = value;
	}
	
	public long getLastTransferTime()
	{
		return lastTransferTime;
	}
	
	public int getWorldOwnerId()
	{
		return worldOwnerId;
	}
	
	public void setWorldOwnerId(int worldOwnerId)
	{
		this.worldOwnerId = worldOwnerId;
	}
	
	public Timestamp getLastStamp()
	{
		return lastStamp;
	}
	
	public void setLastStamp(Timestamp setTime)
	{
		lastStamp = setTime;
	}
	
	public int getPassportStamps()
	{
		return stamps;
	}
	
	public void setPassportStamps(int stamps)
	{
		this.stamps = stamps;
	}
	
	public Map<Integer, AtreianPassport> getPlayerPassports()
	{
		return playerPassports;
	}
	
	public PlayerPassports getCompletedPassports()
	{
		return completedPassports;
	}
	
	public void addToCompletedPassports(AtreianPassport atreianPassport)
	{
		completedPassports.addPassport(atreianPassport.getId(), atreianPassport);
	}
	
	public void setCompletedPassports(PlayerPassports playerPassports)
	{
		completedPassports = playerPassports;
	}
	
	public int getPassportReward()
	{
		return passportReward;
	}
	
	public void setPassportReward(int passportReward)
	{
		this.passportReward = passportReward;
	}
	
	public void setArchDaeva(boolean isArchDaeva)
	{
		this.isArchDaeva = isArchDaeva;
	}
	
	public boolean isArchDaeva()
	{
		return isArchDaeva;
	}
	
	public int getCreativityPoint()
	{
		return creativityPoint;
	}
	
	public void setCreativityPoint(int point)
	{
		creativityPoint = point;
	}
	
	public int getCPStep()
	{
		return cp_step;
	}
	
	public void setCPStep(int step)
	{
		cp_step = step;
	}
	
	public int getJoinRequestLegionId()
	{
		return joinRequestLegionId;
	}
	
	public void setJoinRequestLegionId(int joinRequestLegionId)
	{
		this.joinRequestLegionId = joinRequestLegionId;
	}
	
	public LegionJoinRequestState getJoinRequestState()
	{
		return joinRequestState;
	}
	
	public void setJoinRequestState(LegionJoinRequestState joinRequestState)
	{
		this.joinRequestState = joinRequestState;
	}
	
	public void setLunaConsumePoint(int point)
	{
		lunaConsumePoint = point;
	}
	
	public int getLunaConsumePoint()
	{
		return lunaConsumePoint;
	}
	
	public void setMuniKeys(int keys)
	{
		muni_keys = keys;
	}
	
	public int getMuniKeys()
	{
		return muni_keys;
	}
	
	public void setLunaConsumeCount(int count)
	{
		consumeCount = count;
	}
	
	public int getLunaConsumeCount()
	{
		return consumeCount;
	}
	
	public void setWardrobeSlot(int slot)
	{
		wardrobeSlot = slot;
	}
	
	public int getWardrobeSlot()
	{
		return wardrobeSlot;
	}
	
	public boolean isReadyForGrowthEnergy()
	{
		return (level >= 66) && (level < (GSConfig.PLAYER_MAX_LEVEL + 1));
	}
	
	public void addGrowthEnergy(long add)
	{
		if (!isReadyForGrowthEnergy())
		{
			return;
		}
		growthEnergy += add;
		if (growthEnergy < 0)
		{
			growthEnergy = 0;
		}
		else if (growthEnergy > getMaxGrowthEnergy())
		{
			growthEnergy = getMaxGrowthEnergy();
		}
	}
	
	public void updateMaxGrowthEnergy()
	{
		if (!isReadyForGrowthEnergy())
		{
			growthEnergy = 0;
			growthEnergyMax = 0;
		}
		else if (level < 70)
		{
			growthEnergyMax = (77000000 + (7000000 * (level - 66)));
		}
		else if (level == 70)
		{
			growthEnergyMax = 106000000;
		}
		else if (level == 71)
		{
			growthEnergyMax = 127000000;
		}
		else if (level < 83)
		{
			growthEnergyMax = (127000000 + (11000000 * (level - 71)));
		}
		else
		{
			growthEnergyMax = 175000000;
		}
	}
	
	public void setGrowthEnergy(long value)
	{
		growthEnergy = value;
	}
	
	public long getGrowthEnergy()
	{
		return isReadyForGrowthEnergy() ? growthEnergy : 0;
	}
	
	public long getMaxGrowthEnergy()
	{
		return isReadyForGrowthEnergy() ? growthEnergyMax : 0;
	}
	
	public boolean isReadyForGoldenStarEnergy()
	{
		return level >= 10;
	}
	
	public void addGoldenStarEnergy(long add)
	{
		if (!isReadyForGoldenStarEnergy())
		{
			return;
		}
		goldenStarEnergy += add;
		if (goldenStarEnergy < 0)
		{
			goldenStarEnergy = 0;
		}
		else if (goldenStarEnergy > getMaxGoldenStarEnergy())
		{
			goldenStarEnergy = getMaxGoldenStarEnergy();
		}
		checkGoldenStarPercent();
	}
	
	public void setGoldenStarEnergy(long value)
	{
		goldenStarEnergy = value;
		checkGoldenStarPercent();
	}
	
	public long getGoldenStarEnergy()
	{
		return isReadyForGoldenStarEnergy() ? goldenStarEnergy : 0;
	}
	
	public long getMaxGoldenStarEnergy()
	{
		return isReadyForGoldenStarEnergy() ? goldenStarEnergyMax : 0;
	}
	
	public void checkGoldenStarPercent()
	{
		if ((getPlayer() != null) && (isReadyForGoldenStarEnergy()))
		{
			final int percent = (int) ((goldenStarEnergy * 100.0) / getMaxGoldenStarEnergy());
			if ((!GoldenStarBoost) && (percent > 50))
			{
				GoldenStarBoost = true;
				PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403399, new Object[]
				{
					Integer.valueOf(50)
				}));
			}
			else if ((GoldenStarBoost) && (percent < 50))
			{
				GoldenStarBoost = false;
				PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403400, new Object[]
				{
					Integer.valueOf(50)
				}));
			}
			else if (goldenStarEnergy <= 0)
			{
				PacketSendUtility.sendPacket(getPlayer(), new SM_SYSTEM_MESSAGE(1403401, new Object[0]));
			}
		}
	}
	
	public int getCubeExpands()
	{
		return cubeExpands;
	}
}