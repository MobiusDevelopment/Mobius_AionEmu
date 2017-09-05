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
package com.aionemu.gameserver.services.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.Future;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.EventType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.QuestTemplate;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.model.templates.quest.XMLStartCondition;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

import gnu.trove.map.hash.TIntObjectHashMap;

/**
 * @author Rolandas
 */
public class EventsService
{
	Logger log = LoggerFactory.getLogger(EventsService.class);
	
	private final int CHECK_TIME_PERIOD = 1000 * 60 * 5;
	
	private boolean isStarted = false;
	
	private Future<?> checkTask = null;
	
	private final List<EventTemplate> activeEvents;
	
	TIntObjectHashMap<List<EventTemplate>> eventsForStartQuest = new TIntObjectHashMap<>();
	
	TIntObjectHashMap<List<EventTemplate>> eventsForMaintainQuest = new TIntObjectHashMap<>();
	
	private static class SingletonHolder
	{
		
		protected static final EventsService instance = new EventsService();
	}
	
	public static EventsService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	EventsService()
	{
		activeEvents = Collections.synchronizedList(DataManager.EVENT_DATA.getActiveEvents());
		updateQuestMap();
	}
	
	/**
	 * This method is called just after player logged in to the game.<br>
	 * <br>
	 * <b><font color='red'>NOTICE: </font>This method must not be called from anywhere else.</b>
	 * @param player
	 */
	public void onPlayerLogin(Player player)
	{
		final List<Integer> activeStartQuests = new ArrayList<>();
		final List<Integer> activeMaintainQuests = new ArrayList<>();
		TIntObjectHashMap<List<EventTemplate>> map1 = null;
		TIntObjectHashMap<List<EventTemplate>> map2 = null;
		
		synchronized (activeEvents)
		{
			for (EventTemplate et : activeEvents)
			{
				if (et.isActive())
				{
					activeStartQuests.addAll(et.getStartableQuests());
					activeMaintainQuests.addAll(et.getMaintainableQuests());
				}
			}
			map1 = new TIntObjectHashMap<>(eventsForStartQuest);
			map2 = new TIntObjectHashMap<>(eventsForMaintainQuest);
		}
		
		StartOrMaintainQuests(player, activeStartQuests.listIterator(), map1, true);
		StartOrMaintainQuests(player, activeMaintainQuests.listIterator(), map2, false);
		
		activeStartQuests.clear();
		activeMaintainQuests.clear();
		map1.clear();
		map2.clear();
	}
	
	void StartOrMaintainQuests(Player player, ListIterator<Integer> questList, TIntObjectHashMap<List<EventTemplate>> templateMap, boolean start)
	{
		while (questList.hasNext())
		{
			final int questId = questList.next();
			final QuestState qs = player.getQuestStateList().getQuestState(questId);
			final QuestEnv cookie = new QuestEnv(null, player, questId, 0);
			QuestStatus status = qs == null ? QuestStatus.START : qs.getStatus();
			
			if (QuestService.checkLevelRequirement(questId, player.getCommonData().getLevel()))
			{
				final QuestTemplate template = DataManager.QUEST_DATA.getQuestById(questId);
				if (template.getRacePermitted() != null)
				{
					if (template.getRacePermitted().ordinal() != player.getCommonData().getRace().ordinal())
					{
						continue;
					}
				}
				
				if (template.getClassPermitted().size() != 0)
				{
					if (!template.getClassPermitted().contains(player.getCommonData().getPlayerClass()))
					{
						continue;
					}
				}
				
				if (template.getGenderPermitted() != null)
				{
					if (template.getGenderPermitted().ordinal() != player.getGender().ordinal())
					{
						continue;
					}
				}
				
				final int amountOfStartConditions = template.getXMLStartConditions().size();
				int fulfilledStartConditions = 0;
				if (amountOfStartConditions != 0)
				{
					for (XMLStartCondition startCondition : template.getXMLStartConditions())
					{
						if (startCondition.check(player, false))
						{
							fulfilledStartConditions++;
						}
					}
					if (fulfilledStartConditions < 1)
					{
						continue;
					}
				}
				
				if (qs != null)
				{
					if ((qs.getCompleteTime() != null) || (status == QuestStatus.COMPLETE))
					{
						DateTime completed = null;
						if (qs.getCompleteTime() == null)
						{
							completed = new DateTime(0);
						}
						else
						{
							completed = new DateTime(qs.getCompleteTime().getTime());
						}
						
						if (templateMap.containsKey(questId))
						{
							for (EventTemplate et : templateMap.get(questId))
							{
								// recurring event, reset it
								if (et.getStartDate().isAfter(completed))
								{
									if (start)
									{
										status = QuestStatus.START;
										qs.setQuestVar(0);
										qs.setCompleteCount(0);
										qs.setStatus(status);
									}
									break;
								}
							}
						}
					}
					// re-register quests
					if (status == QuestStatus.COMPLETE)
					{
						PacketSendUtility.sendPacket(player, new SM_QUEST_ACTION(questId, status, qs.getQuestVars().getQuestVars()));
					}
					else
					{
						QuestService.startEventQuest(cookie, status);
					}
				}
				else if (start)
				{
					QuestService.startEventQuest(cookie, status);
				}
			}
		}
	}
	
	public boolean isStarted()
	{
		return isStarted;
	}
	
	public void start()
	{
		if (isStarted)
		{
			checkTask.cancel(false);
		}
		isStarted = true;
		checkTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(() -> checkEvents(), 0, CHECK_TIME_PERIOD);
	}
	
	public void stop()
	{
		if (isStarted)
		{
			checkTask.cancel(false);
		}
		checkTask = null;
		isStarted = false;
	}
	
	void checkEvents()
	{
		final List<EventTemplate> newEvents = new ArrayList<>();
		final List<EventTemplate> allEvents = DataManager.EVENT_DATA.getAllEvents();
		
		for (EventTemplate et : allEvents)
		{
			if (et.isActive())
			{
				newEvents.add(et);
				et.Start();
			}
		}
		
		synchronized (activeEvents)
		{
			for (EventTemplate et : activeEvents)
			{
				if (et.isExpired() || !DataManager.EVENT_DATA.Contains(et.getName()))
				{
					et.Stop();
				}
			}
			
			activeEvents.clear();
			eventsForStartQuest.clear();
			eventsForMaintainQuest.clear();
			activeEvents.addAll(newEvents);
			updateQuestMap();
		}
		
		newEvents.clear();
		allEvents.clear();
	}
	
	private void updateQuestMap()
	{
		for (EventTemplate et : activeEvents)
		{
			for (int qId : et.getStartableQuests())
			{
				if (!eventsForStartQuest.containsKey(qId))
				{
					eventsForStartQuest.put(qId, new ArrayList<EventTemplate>());
				}
				eventsForStartQuest.get(qId).add(et);
			}
			for (int qId : et.getMaintainableQuests())
			{
				if (!eventsForMaintainQuest.containsKey(qId))
				{
					eventsForMaintainQuest.put(qId, new ArrayList<EventTemplate>());
				}
				eventsForMaintainQuest.get(qId).add(et);
			}
		}
	}
	
	public boolean checkQuestIsActive(int questId)
	{
		synchronized (activeEvents)
		{
			if (eventsForStartQuest.containsKey(questId) || eventsForMaintainQuest.containsKey(questId))
			{
				return true;
			}
		}
		return false;
	}
	
	public EventType getEventType()
	{
		for (EventTemplate et : activeEvents)
		{
			final String theme = et.getTheme();
			if (theme != null)
			{
				final EventType type = EventType.getEventType(theme);
				if (et.isActive() && !type.equals(EventType.NONE))
				{
					return type;
				}
			}
		}
		return EventType.NONE;
	}
	
	public List<EventTemplate> getActiveEvents()
	{
		return activeEvents;
	}
}