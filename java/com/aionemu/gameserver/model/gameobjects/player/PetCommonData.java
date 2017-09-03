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

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerPetsDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.IExpirable;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.pet.PetDopingBag;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.model.templates.pet.PetTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.toypet.PetAdoptionService;
import com.aionemu.gameserver.services.toypet.PetFeedProgress;
import com.aionemu.gameserver.services.toypet.PetHungryLevel;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.idfactory.IDFactory;

public class PetCommonData extends VisibleObjectTemplate implements IExpirable
{
	private int decoration;
	private String name;
	private final int petId;
	private Timestamp birthday;
	PetFeedProgress feedProgress = null;
	PetDopingBag dopingBag = null;
	private volatile boolean cancelFeed = false;
	boolean feedingTime = true;
	long curentTime;
	private final int petObjectId;
	private final int masterObjectId;
	private long startMoodTime;
	private int shuggleCounter;
	private int lastSentPoints;
	private long moodCdStarted;
	private long giftCdStarted;
	private final int expireTime;
	private Timestamp despawnTime;
	private boolean isLooting = false;
	private boolean isBuffing = false;
	private boolean isSelling = false;
	
	public PetCommonData(int petId, int masterObjectId, int expireTime)
	{
		petObjectId = IDFactory.getInstance().nextId();
		this.petId = petId;
		this.masterObjectId = masterObjectId;
		this.expireTime = expireTime;
		final PetTemplate template = DataManager.PET_DATA.getPetTemplate(petId);
		if (template.ContainsFunction(PetFunctionType.FOOD))
		{
			final int flavourId = template.getPetFunction(PetFunctionType.FOOD).getId();
			final int lovedLimit = DataManager.PET_FEED_DATA.getFlavourById(flavourId).getLovedFoodLimit();
			feedProgress = new PetFeedProgress((byte) (lovedLimit & 0xFF));
		}
		if (template.ContainsFunction(PetFunctionType.DOPING))
		{
			dopingBag = new PetDopingBag();
		}
	}
	
	public final int getDecoration()
	{
		return decoration;
	}
	
	public final void setDecoration(int decoration)
	{
		this.decoration = decoration;
	}
	
	@Override
	public final String getName()
	{
		return name;
	}
	
	public final void setName(String name)
	{
		this.name = name;
	}
	
	public final int getPetId()
	{
		return petId;
	}
	
	public int getBirthday()
	{
		if (birthday == null)
		{
			return 0;
		}
		return (int) (birthday.getTime() / 1000);
	}
	
	public Timestamp getBirthdayTimestamp()
	{
		return birthday;
	}
	
	public void setBirthday(Timestamp birthday)
	{
		this.birthday = birthday;
	}
	
	public long getCurentTime()
	{
		return curentTime;
	}
	
	public void setCurentTime(long curentTime)
	{
		this.curentTime = curentTime;
	}
	
	public void setIsFeedingTime(boolean food)
	{
		feedingTime = food;
	}
	
	public boolean isFeedingTime()
	{
		return feedingTime;
	}
	
	public boolean getCancelFeed()
	{
		return cancelFeed;
	}
	
	public void setCancelFeed(boolean cancelFeed)
	{
		this.cancelFeed = cancelFeed;
	}
	
	public void setFeedingTime(boolean feedingTime)
	{
		this.feedingTime = feedingTime;
	}
	
	public void setReFoodTime(final long reFoodTime)
	{
		setFeedingTime(false);
		ThreadPoolManager.getInstance().schedule(() ->
		{
			feedingTime = true;
			curentTime = 0;
			feedProgress.setHungryLevel(PetHungryLevel.HUNGRY);
		}, reFoodTime);
	}
	
	public long getTime()
	{
		long time = System.currentTimeMillis() - curentTime;
		if ((time < 0) || (time > 600000))
		{
			curentTime = 0;
			time = 0;
		}
		return (600000 - time) == 600000 ? 0 : 600000 - time;
	}
	
	public int getObjectId()
	{
		return petObjectId;
	}
	
	public int getMasterObjectId()
	{
		return masterObjectId;
	}
	
	@Override
	public int getTemplateId()
	{
		return petId;
	}
	
	@Override
	public int getNameId()
	{
		return 0;
	}
	
	public final long getMoodStartTime()
	{
		return startMoodTime;
	}
	
	public final int getShuggleCounter()
	{
		return shuggleCounter;
	}
	
	public final void setShuggleCounter(int shuggleCounter)
	{
		this.shuggleCounter = shuggleCounter;
	}
	
	public final int getMoodPoints(boolean forPacket)
	{
		if (startMoodTime == 0)
		{
			startMoodTime = System.currentTimeMillis();
		}
		final int points = Math.round((System.currentTimeMillis() - startMoodTime) / 1000f) + (shuggleCounter * 1000);
		if (forPacket && (points > 9000))
		{
			return 9000;
		}
		return points;
	}
	
	public final int getLastSentPoints()
	{
		return lastSentPoints;
	}
	
	public final void setLastSentPoints(int points)
	{
		lastSentPoints = points;
	}
	
	public final boolean increaseShuggleCounter()
	{
		if (getMoodRemainingTime() > 0)
		{
			return false;
		}
		moodCdStarted = System.currentTimeMillis();
		shuggleCounter++;
		return true;
	}
	
	public final void clearMoodStatistics()
	{
		startMoodTime = 0;
		shuggleCounter = 0;
	}
	
	public final void setStartMoodTime(long startMoodTime)
	{
		this.startMoodTime = startMoodTime;
	}
	
	public long getMoodCdStarted()
	{
		return moodCdStarted;
	}
	
	public void setMoodCdStarted(long moodCdStarted)
	{
		this.moodCdStarted = moodCdStarted;
	}
	
	public int getMoodRemainingTime()
	{
		final long stop = moodCdStarted + 600000;
		final long remains = stop - System.currentTimeMillis();
		if (remains <= 0)
		{
			setMoodCdStarted(0);
			return 0;
		}
		return (int) (remains / 1000);
	}
	
	public long getGiftCdStarted()
	{
		return giftCdStarted;
	}
	
	public void setGiftCdStarted(long giftCdStarted)
	{
		this.giftCdStarted = giftCdStarted;
	}
	
	public int getGiftRemainingTime()
	{
		final long stop = giftCdStarted + (3600 * 1000);
		final long remains = stop - System.currentTimeMillis();
		if (remains <= 0)
		{
			setGiftCdStarted(0);
			return 0;
		}
		return (int) (remains / 1000);
	}
	
	public Timestamp getDespawnTime()
	{
		return despawnTime;
	}
	
	public void setDespawnTime(Timestamp despawnTime)
	{
		this.despawnTime = despawnTime;
	}
	
	public void savePetMoodData()
	{
		DAOManager.getDAO(PlayerPetsDAO.class).savePetMoodData(this);
	}
	
	public PetFeedProgress getFeedProgress()
	{
		return feedProgress;
	}
	
	public void setIsLooting(boolean isLooting)
	{
		this.isLooting = isLooting;
	}
	
	public boolean isLooting()
	{
		return isLooting;
	}
	
	public PetDopingBag getDopingBag()
	{
		return dopingBag;
	}
	
	public void setIsBuffing(boolean isBuffing)
	{
		this.isBuffing = isBuffing;
	}
	
	public boolean isBuffing()
	{
		return isBuffing;
	}
	
	public void setIsSelling(boolean isSelling)
	{
		this.isSelling = isSelling;
	}
	
	public boolean isSelling()
	{
		return isSelling;
	}
	
	@Override
	public int getExpireTime()
	{
		return expireTime;
	}
	
	@Override
	public void expireEnd(Player player)
	{
		if (player == null)
		{
			return;
		}
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_PET_ABANDON_EXPIRE_TIME_COMPLETE(name));
		PetAdoptionService.surrenderPet(player, petId);
	}
	
	@Override
	public boolean canExpireNow()
	{
		return true;
	}
	
	@Override
	public void expireMessage(Player player, int time)
	{
	}
}