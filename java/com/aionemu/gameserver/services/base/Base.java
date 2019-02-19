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
package com.aionemu.gameserver.services.base;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.base.BaseNpc;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplateType;
import com.aionemu.gameserver.model.templates.spawns.SpawnGroup2;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.spawnengine.SpawnEngine;
import com.aionemu.gameserver.spawnengine.SpawnHandlerType;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.World;

/**
 * @author Rinzler
 * @param <BL>
 */
public class Base<BL extends BaseLocation>
{
	private Npc boss, flag;
	private boolean started;
	private final BL baseLocation;
	private Future<?> startAssault, stopAssault;
	private final List<Race> list = new ArrayList<>();
	private final List<Npc> spawned = new ArrayList<>();
	private final List<Npc> attackers = new ArrayList<>();
	private final AtomicBoolean finished = new AtomicBoolean();
	private final BaseBossDeathListener baseBossDeathListener = new BaseBossDeathListener(this);
	
	public Base(BL baseLocation)
	{
		list.add(Race.ASMODIANS);
		list.add(Race.ELYOS);
		list.add(Race.NPC);
		this.baseLocation = baseLocation;
	}
	
	public final void start()
	{
		boolean doubleStart = false;
		synchronized (this)
		{
			if (started)
			{
				doubleStart = true;
			}
			else
			{
				started = true;
			}
		}
		if (doubleStart)
		{
			return;
		}
		spawn();
	}
	
	public final void stop()
	{
		if (finished.compareAndSet(false, true))
		{
			if (getBoss() != null)
			{
				// rmvBaseBossListener(); // FIXME? init listeners - EnhancedObject cast
			}
			despawn(getId());
		}
	}
	
	private List<SpawnGroup2> getBaseSpawns()
	{
		final List<SpawnGroup2> spawns = DataManager.SPAWNS_DATA2.getBaseSpawnsByLocId(getId());
		if (spawns == null)
		{
		}
		return spawns;
	}
	
	protected void spawn()
	{
		for (SpawnGroup2 group : getBaseSpawns())
		{
			for (SpawnTemplate spawn : group.getSpawnTemplates())
			{
				final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
				if (template.getBaseRace().equals(getBaseLocation().getRace()))
				{
					if (template.getHandlerType() == null)
					{
						final Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
						final NpcTemplate npcTemplate = npc.getObjectTemplate();
						if (npcTemplate.getNpcTemplateType().equals(NpcTemplateType.FLAG))
						{
							setFlag(npc);
						}
						getSpawned().add(npc);
					}
				}
			}
		}
		delayedAssault();
		delayedSpawn(getRace());
	}
	
	private void delayedAssault()
	{
		startAssault = ThreadPoolManager.getInstance().schedule(() ->
		{
			chooseAttackersRace();
			sendLDF4AdvanceMsgKiller(getId());
		}, Rnd.get(120, 180) * 60000);
	}
	
	public boolean sendLDF4AdvanceMsgKiller(int id)
	{
		switch (id)
		{
			case 90:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v13));
				return true;
			}
			case 91:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v04));
				return true;
			}
			case 92:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v12));
				return true;
			}
			case 93:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v03));
				return true;
			}
			case 94:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v06));
				return true;
			}
			case 95:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v05));
				return true;
			}
			case 96:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v01));
				return true;
			}
			case 97:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v09));
				return true;
			}
			case 98:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v11));
				return true;
			}
			case 99:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v10));
				return true;
			}
			case 100:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v07));
				return true;
			}
			case 101:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v02));
				return true;
			}
			case 102:
			{
				World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Advance_killer_v08));
				return true;
			}
			default:
			{
				return false;
			}
		}
	}
	
	private void delayedSpawn(Race race)
	{
		ThreadPoolManager.getInstance().schedule(() ->
		{
			if (getRace().equals(race) && (getBoss() == null))
			{
				spawnBoss();
			}
		}, Rnd.get(5, 10) * 60000);
	}
	
	protected void spawnBoss()
	{
		for (SpawnGroup2 group : getBaseSpawns())
		{
			for (SpawnTemplate spawn : group.getSpawnTemplates())
			{
				final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
				if (template.getBaseRace().equals(getBaseLocation().getRace()))
				{
					if ((template.getHandlerType() != null) && template.getHandlerType().equals(SpawnHandlerType.CHIEF))
					{
						final Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
						setBoss(npc);
						// addBaseBossListeners(); // FIXME? init listeners - EnhancedObject cast
						getSpawned().add(npc);
					}
				}
			}
		}
	}
	
	protected void chooseAttackersRace()
	{
		final AtomicBoolean next = new AtomicBoolean(Math.random() < 0.5);
		for (Race race : list)
		{
			if (!race.equals(getRace()))
			{
				if (next.compareAndSet(true, false))
				{
					continue;
				}
				spawnAttackers(race);
				if (baseLocation.getWorldId() == 400010000)
				{
					// Rattlefrost Outpost.
					if (getBaseLocation().getId() == 53)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Sliversleet Outpost.
					if (getBaseLocation().getId() == 54)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Coldforge Outpost.
					if (getBaseLocation().getId() == 55)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Shimmerfrost Outpost.
					if (getBaseLocation().getId() == 56)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Icehowl Outpost.
					if (getBaseLocation().getId() == 57)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Chillhaunt Outpost.
					if (getBaseLocation().getId() == 58)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Sootguzzle Outpost.
					if (getBaseLocation().getId() == 59)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Flameruin Outpost.
					if (getBaseLocation().getId() == 60)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Stokebellow Outpost.
					if (getBaseLocation().getId() == 61)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Blazerack Outpost.
					if (getBaseLocation().getId() == 62)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Smoldergeist Outpost.
					if (getBaseLocation().getId() == 63)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
					// Moltenspike Outpost.
					if (getBaseLocation().getId() == 64)
					{
						if (race == Race.ASMODIANS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, false);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, true);
						}
						else if (race == Race.ELYOS)
						{
							AbyssLandingService.getInstance().updateRedemptionLanding(6000, LandingPointsEnum.BASE, true);
							AbyssLandingService.getInstance().updateHarbingerLanding(6000, LandingPointsEnum.BASE, false);
						}
					}
				}
				break;
			}
		}
	}
	
	public void spawnAttackers(Race race)
	{
		if (getFlag() == null)
		{
		}
		else if (!getFlag().getPosition().getMapRegion().isMapRegionActive())
		{
			if (Math.random() < 0.5)
			{
				BaseService.getInstance().capture(getId(), race);
			}
			else
			{
				delayedAssault();
			}
			return;
		}
		if (!isAttacked())
		{
			despawnAttackers();
			for (SpawnGroup2 group : getBaseSpawns())
			{
				for (SpawnTemplate spawn : group.getSpawnTemplates())
				{
					final BaseSpawnTemplate template = (BaseSpawnTemplate) spawn;
					if (template.getBaseRace().equals(race))
					{
						if ((template.getHandlerType() != null) && template.getHandlerType().equals(SpawnHandlerType.SLAYER))
						{
							final Npc npc = (Npc) SpawnEngine.spawnObject(template, 1);
							getAttackers().add(npc);
						}
					}
				}
			}
			if (getAttackers().isEmpty())
			{
			}
			else
			{
				stopAssault = ThreadPoolManager.getInstance().schedule(() ->
				{
					despawnAttackers();
					delayedAssault();
				}, 5 * 60000);
			}
		}
	}
	
	public boolean isAttacked()
	{
		for (Npc attacker : getAttackers())
		{
			if (!attacker.getLifeStats().isAlreadyDead())
			{
				return true;
			}
		}
		return false;
	}
	
	protected void despawn(int baseLocationId)
	{
		setFlag(null);
		final Collection<BaseNpc> baseNpcs = World.getInstance().getLocalBaseNpcs(baseLocationId);
		for (BaseNpc npc : baseNpcs)
		{
			npc.getController().onDelete();
		}
		if (startAssault != null)
		{
			startAssault.cancel(true);
		}
		if (stopAssault != null)
		{
			stopAssault.cancel(true);
			despawnAttackers();
		}
	}
	
	protected void despawnAttackers()
	{
		for (Npc attacker : getAttackers())
		{
			attacker.getController().onDelete();
		}
		getAttackers().clear();
	}
	
	// FIXME? init listeners - EnhancedObject cast
	// protected void addBaseBossListeners()
	// {
	// final AbstractAI ai = (AbstractAI) getBoss().getAi2();
	// final EnhancedObject eo = (EnhancedObject) ai;
	// eo.addCallback(getBaseBossDeathListener());
	// }
	
	// FIXME? init listeners - EnhancedObject cast
	// protected void rmvBaseBossListener()
	// {
	// final AbstractAI ai = (AbstractAI) getBoss().getAi2();
	// final EnhancedObject eo = (EnhancedObject) ai;
	// eo.removeCallback(getBaseBossDeathListener());
	// }
	
	public Npc getFlag()
	{
		return flag;
	}
	
	public void setFlag(Npc flag)
	{
		this.flag = flag;
	}
	
	public Npc getBoss()
	{
		return boss;
	}
	
	public void setBoss(Npc boss)
	{
		this.boss = boss;
	}
	
	public BaseBossDeathListener getBaseBossDeathListener()
	{
		return baseBossDeathListener;
	}
	
	public boolean isFinished()
	{
		return finished.get();
	}
	
	public BL getBaseLocation()
	{
		return baseLocation;
	}
	
	public int getId()
	{
		return baseLocation.getId();
	}
	
	public Race getRace()
	{
		return baseLocation.getRace();
	}
	
	public void setRace(Race race)
	{
		baseLocation.setRace(race);
	}
	
	public List<Npc> getAttackers()
	{
		return attackers;
	}
	
	public List<Npc> getSpawned()
	{
		return spawned;
	}
}