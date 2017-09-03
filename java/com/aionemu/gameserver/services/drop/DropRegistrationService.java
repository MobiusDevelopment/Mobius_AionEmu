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
package com.aionemu.gameserver.services.drop;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.event.AIEventType;
import com.aionemu.gameserver.configs.main.DropConfig;
import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.NpcDropData;
import com.aionemu.gameserver.model.drop.Drop;
import com.aionemu.gameserver.model.drop.DropGroup;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.drop.NpcDrop;
import com.aionemu.gameserver.model.gameobjects.DropNpc;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.team2.common.legacy.LootGroupRules;
import com.aionemu.gameserver.model.templates.event.EventDrop;
import com.aionemu.gameserver.model.templates.event.EventTemplate;
import com.aionemu.gameserver.model.templates.housing.HouseType;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.pet.PetFunctionType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_LOOT_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PET;
import com.aionemu.gameserver.services.EventService;
import com.aionemu.gameserver.services.QuestService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.stats.DropRewardEnum;

import javolution.util.FastList;
import javolution.util.FastMap;

/**
 * @author xTz
 */
public class DropRegistrationService
{
	
	private final Map<Integer, Set<DropItem>> currentDropMap = new FastMap<Integer, Set<DropItem>>().shared();
	private final Map<Integer, DropNpc> dropRegistrationMap = new FastMap<Integer, DropNpc>().shared();
	private final FastList<Integer> noReductionMaps;
	
	public void registerDrop(Npc npc, Player player, Collection<Player> groupMembers)
	{
		registerDrop(npc, player, player.getLevel(), groupMembers);
	}
	
	private DropRegistrationService()
	{
		init();
		noReductionMaps = new FastList<>();
		for (final String zone : DropConfig.DISABLE_DROP_REDUCTION_IN_ZONES.split(","))
		{
			noReductionMaps.add(Integer.parseInt(zone));
		}
	}
	
	public final void init()
	{
		final NpcDropData npcDrop = DataManager.NPC_DROP_DATA;
		for (final NpcDrop drop : npcDrop.getNpcDrop())
		{
			final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(drop.getNpcId());
			if (npcTemplate == null)
			{
				continue;
			}
			if (npcTemplate.getNpcDrop() != null)
			{
				final NpcDrop currentDrop = npcTemplate.getNpcDrop();
				for (final DropGroup dg : currentDrop.getDropGroup())
				{
					final Iterator<Drop> iter = dg.getDrop().iterator();
					while (iter.hasNext())
					{
						final Drop d = iter.next();
						for (final DropGroup dg2 : drop.getDropGroup())
						{
							for (final Drop d2 : dg2.getDrop())
							{
								if (d.getItemId() == d2.getItemId())
								{
									iter.remove();
								}
							}
						}
					}
				}
				final List<DropGroup> list = new ArrayList<>();
				for (final DropGroup dg : drop.getDropGroup())
				{
					boolean added = false;
					for (final DropGroup dg2 : currentDrop.getDropGroup())
					{
						if (dg2.getGroupName().equals(dg.getGroupName()))
						{
							dg2.getDrop().addAll(dg.getDrop());
							added = true;
						}
					}
					if (!added)
					{
						list.add(dg);
					}
				}
				if (!list.isEmpty())
				{
					currentDrop.getDropGroup().addAll(list);
				}
			}
			else
			{
				npcTemplate.setNpcDrop(drop);
			}
		}
	}
	
	/**
	 * After NPC dies, it can register arbitrary drop
	 */
	public void registerDrop(Npc npc, Player player, int heighestLevel, Collection<Player> groupMembers)
	{
		
		if (player == null)
		{
			return;
		}
		final int npcObjId = npc.getObjectId();
		
		// Getting all possible drops for this Npc
		final NpcDrop npcDrop = npc.getNpcDrop();
		final Set<DropItem> droppedItems = new HashSet<>();
		int index = 1;
		int dropChance = 100;
		final int npcLevel = npc.getLevel();
		final boolean isChest = npc.getAi2().getName().equals("chest");
		if (!DropConfig.DISABLE_DROP_REDUCTION && (((isChest && (npcLevel != 1)) || !isChest)) && !noReductionMaps.contains(npc.getWorldId()))
		{
			dropChance = DropRewardEnum.dropRewardFrom(npcLevel - heighestLevel); // reduce chance depending on level
		}
		
		Player genesis = player;
		Integer winnerObj = 0;
		
		// Distributing drops to players
		final Collection<Player> dropPlayers = new ArrayList<>();
		Collection<Player> winningPlayers = new ArrayList<>();
		if (player.isInGroup2() || player.isInAlliance2())
		{
			final List<Integer> dropMembers = new ArrayList<>();
			final LootGroupRules lootGrouRules = player.getLootGroupRules();
			
			switch (lootGrouRules.getLootRule())
			{
				case ROUNDROBIN:
					final int size = groupMembers.size();
					if (size > lootGrouRules.getNrRoundRobin())
					{
						lootGrouRules.setNrRoundRobin(lootGrouRules.getNrRoundRobin() + 1);
					}
					else
					{
						lootGrouRules.setNrRoundRobin(1);
					}
					
					int i = 0;
					for (final Player p : groupMembers)
					{
						i++;
						if (i == lootGrouRules.getNrRoundRobin())
						{
							winningPlayers.add(p);
							winnerObj = p.getObjectId();
							setItemsToWinner(droppedItems, winnerObj);
							genesis = p;
							break;
						}
					}
					break;
				case FREEFORALL:
					winningPlayers = groupMembers;
					break;
				case LEADER:
					final Player leader = player.isInGroup2() ? player.getPlayerGroup2().getLeaderObject() : player.getPlayerAlliance2().getLeaderObject();
					winningPlayers.add(leader);
					winnerObj = leader.getObjectId();
					setItemsToWinner(droppedItems, winnerObj);
					genesis = leader;
					break;
			}
			
			for (final Player member : winningPlayers)
			{
				dropMembers.add(member.getObjectId());
				dropPlayers.add(member);
			}
			final DropNpc dropNpc = new DropNpc(npcObjId);
			dropRegistrationMap.put(npcObjId, dropNpc);
			dropNpc.setPlayersObjectId(dropMembers);
			dropNpc.setInRangePlayers(groupMembers);
			dropNpc.setGroupSize(groupMembers.size());
		}
		else
		{
			final List<Integer> singlePlayer = new ArrayList<>();
			singlePlayer.add(player.getObjectId());
			dropPlayers.add(player);
			dropRegistrationMap.put(npcObjId, new DropNpc(npcObjId));
			dropRegistrationMap.get(npcObjId).setPlayersObjectId(singlePlayer);
		}
		float boostDropRate = npc.getGameStats().getStat(StatEnum.BOOST_DROP_RATE, 100).getCurrent() / 100f;
		boostDropRate += genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f;
		boostDropRate += genesis.getCommonData().getCurrentReposteEnergy() > 0 ? 0.05f : 0;
		boostDropRate += genesis.getCommonData().getCurrentSalvationPercent() > 0 ? 0.05f : 0;
		boostDropRate += genesis.getActiveHouse() != null ? genesis.getActiveHouse().getHouseType().equals(HouseType.PALACE) ? 0.05f : 0 : 0;
		boostDropRate += (genesis.getGameStats().getStat(StatEnum.BOOST_DROP_RATE, 100).getCurrent() / 100f) - 1;
		boostDropRate += (genesis.getGameStats().getStat(StatEnum.DR_BOOST, 100).getCurrent() / 100f) - 1;
		final float dropRate = (genesis.getRates().getDropRate() * boostDropRate * dropChance) / 100f;
		if (npcDrop != null)
		{
			index = npcDrop.dropCalculator(droppedItems, index, dropRate, genesis.getRace(), groupMembers);
		}
		currentDropMap.put(npcObjId, droppedItems);
		index = QuestService.getQuestDrop(droppedItems, index, npc, groupMembers, genesis);
		if (EventsConfig.ENABLE_EVENT_SERVICE)
		{
			final List<EventTemplate> activeEvents = EventService.getInstance().getActiveEvents();
			for (final EventTemplate eventTemplate : activeEvents)
			{
				if (eventTemplate.EventDrop() == null)
				{
					continue;
				}
				final List<EventDrop> eventDrops = eventTemplate.EventDrop().getEventDrops();
				for (final EventDrop eventDrop : eventDrops)
				{
					final int diff = npc.getLevel() - eventDrop.getItemTemplate().getLevel();
					final int minDiff = eventDrop.getMinDiff();
					final int maxDiff = eventDrop.getMaxDiff();
					if (minDiff != 0)
					{
						if (diff < eventDrop.getMinDiff())
						{
							continue;
						}
					}
					if (maxDiff != 0)
					{
						if (diff > eventDrop.getMaxDiff())
						{
							continue;
						}
					}
					float percent = eventDrop.getChance();
					percent *= dropRate;
					if ((Rnd.get() * 100) > percent)
					{
						continue;
					}
					droppedItems.add(regDropItem(index++, winnerObj, npcObjId, eventDrop.getItemId(), eventDrop.getCount()));
				}
			}
		}
		if (npc.getPosition().isInstanceMap())
		{
			npc.getPosition().getWorldMapInstance().getInstanceHandler().onDropRegistered(npc);
		}
		npc.getAi2().onGeneralEvent(AIEventType.DROP_REGISTERED);
		for (final Player p : dropPlayers)
		{
			PacketSendUtility.sendPacket(p, new SM_LOOT_STATUS(npcObjId, 0));
		}
		if ((player.getPet() != null) && (player.getPet().getPetTemplate().getPetFunction(PetFunctionType.LOOT) != null) && player.getPet().getCommonData().isLooting())
		{
			PacketSendUtility.sendPacket(player, new SM_PET(true, npcObjId));
			final Set<DropItem> drops = getCurrentDropMap().get(npcObjId);
			if ((drops == null) || (drops.size() == 0))
			{
				npc.getController().onDelete();
			}
			else
			{
				final DropItem[] dropItems = drops.toArray(new DropItem[0]);
				for (final DropItem dropItem : dropItems)
				{
					DropService.getInstance().requestDropItem(player, npcObjId, dropItem.getIndex(), true);
				}
			}
			PacketSendUtility.sendPacket(player, new SM_PET(false, npcObjId));
			if ((drops == null) || (drops.size() == 0))
			{
				return;
			}
		}
		DropService.getInstance().scheduleFreeForAll(npcObjId);
	}
	
	public void setItemsToWinner(Set<DropItem> droppedItems, Integer obj)
	{
		for (final DropItem dropItem : droppedItems)
		{
			if (!dropItem.getDropTemplate().isEachMember())
			{
				dropItem.setPlayerObjId(obj);
			}
		}
	}
	
	public DropItem regDropItem(int index, int playerObjId, int objId, int itemId, long count)
	{
		final DropItem item = new DropItem(new Drop(itemId, 1, 1, 100, false, false));
		item.setPlayerObjId(playerObjId);
		item.setNpcObj(objId);
		item.setCount(count);
		item.setIndex(index);
		return item;
	}
	
	/**
	 * @return dropRegistrationMap
	 */
	public Map<Integer, DropNpc> getDropRegistrationMap()
	{
		return dropRegistrationMap;
	}
	
	/**
	 * @return currentDropMap
	 */
	public Map<Integer, Set<DropItem>> getCurrentDropMap()
	{
		return currentDropMap;
	}
	
	public static DropRegistrationService getInstance()
	{
		return SingletonHolder.instance;
	}
	
	@SuppressWarnings("synthetic-access")
	private static class SingletonHolder
	{
		
		protected static final DropRegistrationService instance = new DropRegistrationService();
	}
}