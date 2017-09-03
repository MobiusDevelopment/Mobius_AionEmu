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
package com.aionemu.gameserver.spawnengine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.configs.main.CustomConfig;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.controllers.GatherableController;
import com.aionemu.gameserver.controllers.NpcController;
import com.aionemu.gameserver.controllers.PetController;
import com.aionemu.gameserver.controllers.SiegeWeaponController;
import com.aionemu.gameserver.controllers.SummonController;
import com.aionemu.gameserver.controllers.effect.EffectController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.dataholders.NpcData;
import com.aionemu.gameserver.geoEngine.collision.CollisionIntention;
import com.aionemu.gameserver.geoEngine.math.Vector3f;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.agent.AgentLocation;
import com.aionemu.gameserver.model.anoha.AnohaLocation;
import com.aionemu.gameserver.model.base.BaseLocation;
import com.aionemu.gameserver.model.beritra.BeritraLocation;
import com.aionemu.gameserver.model.conquest.ConquestLocation;
import com.aionemu.gameserver.model.dynamicrift.DynamicRiftLocation;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Gatherable;
import com.aionemu.gameserver.model.gameobjects.GroupGate;
import com.aionemu.gameserver.model.gameobjects.Homing;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.NpcObjectType;
import com.aionemu.gameserver.model.gameobjects.Pet;
import com.aionemu.gameserver.model.gameobjects.Servant;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.SummonedHouseNpc;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.base.BaseNpc;
import com.aionemu.gameserver.model.gameobjects.player.PetCommonData;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.siege.SiegeNpc;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.gameobjects.state.CreatureVisualState;
import com.aionemu.gameserver.model.house.House;
import com.aionemu.gameserver.model.idiandepths.IdianDepthsLocation;
import com.aionemu.gameserver.model.instancerift.InstanceRiftLocation;
import com.aionemu.gameserver.model.iu.IuLocation;
import com.aionemu.gameserver.model.landing.LandingLocation;
import com.aionemu.gameserver.model.landing_special.LandingSpecialLocation;
import com.aionemu.gameserver.model.moltenus.MoltenusLocation;
import com.aionemu.gameserver.model.nightmarecircus.NightmareCircusLocation;
import com.aionemu.gameserver.model.rift.RiftLocation;
import com.aionemu.gameserver.model.rvr.RvrLocation;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.skill.NpcSkillEntry;
import com.aionemu.gameserver.model.svs.SvsLocation;
import com.aionemu.gameserver.model.templates.VisibleObjectTemplate;
import com.aionemu.gameserver.model.templates.npc.NpcTemplate;
import com.aionemu.gameserver.model.templates.pet.PetTemplate;
import com.aionemu.gameserver.model.templates.spawns.SpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.agentspawns.AgentSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.anohaspawns.AnohaSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.basespawns.BaseSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.beritraspawns.BeritraSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.conquestspawns.ConquestSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.dynamicriftspawns.DynamicRiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.idiandepthsspawns.IdianDepthsSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.instanceriftspawns.InstanceRiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.iuspawns.IuSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.landingspawns.LandingSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.landingspecialspawns.LandingSpecialSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.moltenusspawns.MoltenusSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.nightmarecircusspawns.NightmareCircusSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.riftspawns.RiftSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.rvrspawns.RvrSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.siegespawns.SiegeSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.svsspawns.SvsSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.vortexspawns.VortexSpawnTemplate;
import com.aionemu.gameserver.model.templates.spawns.zorshivdredgionspawns.ZorshivDredgionSpawnTemplate;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.model.zorshivdredgion.ZorshivDredgionLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_STATE;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;
import com.aionemu.gameserver.services.AgentService;
import com.aionemu.gameserver.services.AnohaService;
import com.aionemu.gameserver.services.BaseService;
import com.aionemu.gameserver.services.BeritraService;
import com.aionemu.gameserver.services.ConquestService;
import com.aionemu.gameserver.services.DynamicRiftService;
import com.aionemu.gameserver.services.IdianDepthsService;
import com.aionemu.gameserver.services.InstanceRiftService;
import com.aionemu.gameserver.services.IuService;
import com.aionemu.gameserver.services.MoltenusService;
import com.aionemu.gameserver.services.NightmareCircusService;
import com.aionemu.gameserver.services.RiftService;
import com.aionemu.gameserver.services.RvrService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.services.SvsService;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.ZorshivDredgionService;
import com.aionemu.gameserver.skillengine.effect.SummonOwner;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.idfactory.IDFactory;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.geo.GeoService;
import com.aionemu.gameserver.world.knownlist.CreatureAwareKnownList;
import com.aionemu.gameserver.world.knownlist.NpcKnownList;
import com.aionemu.gameserver.world.knownlist.PlayerAwareKnownList;

public class VisibleObjectSpawner
{
	private static final Logger log = LoggerFactory.getLogger(VisibleObjectSpawner.class);
	
	protected static VisibleObject spawnNpc(SpawnTemplate spawn, int instanceIndex)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			log.error("<No Template For NPC> " + String.valueOf(objectId));
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		final Npc npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
		npc.setCreatorId(spawn.getCreatorId());
		npc.setMasterName(spawn.getMasterName());
		npc.setKnownlist(new NpcKnownList(npc));
		npc.setEffectController(new EffectController(npc));
		if (WalkerFormator.processClusteredNpc(npc, spawn.getWorldId(), instanceIndex))
		{
			return npc;
		}
		try
		{
			SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		}
		catch (final Exception ex)
		{
			log.error("Error during spawn of npc {}, world {}, x-y {}-{}", new Object[]
			{
				npcTemplate.getTemplateId(),
				spawn.getWorldId(),
				spawn.getX(),
				spawn.getY()
			});
			log.error("Npc {} will be despawned", npcTemplate.getTemplateId(), ex);
			World.getInstance().despawn(npc);
		}
		return npc;
	}
	
	public static SummonedHouseNpc spawnHouseNpc(SpawnTemplate spawn, int instanceIndex, House creator, String masterName)
	{
		final int npcId = spawn.getNpcId();
		final NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(npcId);
		final SummonedHouseNpc npc = new SummonedHouseNpc(IDFactory.getInstance().nextId(), new NpcController(), spawn, template, creator, masterName);
		npc.setKnownlist(new PlayerAwareKnownList(npc));
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnBaseNpc(BaseSpawnTemplate spawn, int instanceIndex)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final int spawnId = spawn.getId();
		final boolean isActive = BaseService.getInstance().isActive(spawnId);
		final BaseLocation base = BaseService.getInstance().getBaseLocation(spawnId);
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		if (!isActive && (spawn.getBaseRace() != base.getRace()))
		{
			return null;
		}
		if (isActive && (spawn.getBaseRace() == base.getRace()))
		{
			npc = new BaseNpc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnRiftNpc(RiftSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.RIFT_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final RiftLocation loc = RiftService.getInstance().getRiftLocation(spawnId);
		if (loc.isOpened() && (spawnId == loc.getId()))
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnSiegeNpc(SiegeSpawnTemplate spawn, int instanceIndex)
	{
		if (!SiegeConfig.SIEGE_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc = null;
		final int spawnSiegeId = spawn.getSiegeId();
		final SiegeLocation loc = SiegeService.getInstance().getSiegeLocation(spawnSiegeId);
		if ((spawn.isPeace() || loc.isVulnerable()) && (spawnSiegeId == loc.getLocationId()) && (spawn.getSiegeRace() == loc.getRace()))
		{
			npc = new SiegeNpc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (spawn.isAssault() && loc.isVulnerable() && spawn.getSiegeRace().equals(SiegeRace.BALAUR))
		{
			npc = new SiegeNpc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnInvasionNpc(VortexSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.VORTEX_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final VortexLocation loc = VortexService.getInstance().getVortexLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isInvasion())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnBeritraNpc(BeritraSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.BERITRA_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final BeritraLocation loc = BeritraService.getInstance().getBeritraLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isBeritraInvasion())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isBeritraPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnAgentNpc(AgentSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.AGENT_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final AgentLocation loc = AgentService.getInstance().getAgentLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isAgentFight())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isAgentPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnAnohaNpc(AnohaSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.ANOHA_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final AnohaLocation loc = AnohaService.getInstance().getAnohaLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isAnohaFight())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isAnohaPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnConquestNpc(ConquestSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.CONQUEST_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final ConquestLocation loc = ConquestService.getInstance().getConquestLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isConquest())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isConquestPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnSvsNpc(SvsSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.SVS_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final SvsLocation loc = SvsService.getInstance().getSvsLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isSvs())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isSvsPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnRvrNpc(RvrSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.RVR_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final RvrLocation loc = RvrService.getInstance().getRvrLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isRvr())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isRvrPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnIuNpc(IuSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.IU_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final IuLocation loc = IuService.getInstance().getIuLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnMoltenusNpc(MoltenusSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.MOLTENUS_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final MoltenusLocation loc = MoltenusService.getInstance().getMoltenusLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isFight())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnDynamicRiftNpc(DynamicRiftSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.DYNAMIC_RIFT_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final DynamicRiftLocation loc = DynamicRiftService.getInstance().getDynamicRiftLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isDynamicRiftOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isDynamicRiftClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnInstanceRiftNpc(InstanceRiftSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.INSTANCE_RIFT_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final InstanceRiftLocation loc = InstanceRiftService.getInstance().getInstanceRiftLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isInstanceRiftOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isInstanceRiftClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnNightmareCircusNpc(NightmareCircusSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.NIGHTMARE_CIRCUS_ENABLE)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final NightmareCircusLocation loc = NightmareCircusService.getInstance().getNightmareCircusLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isCircusOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isCircusClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnIdianDepthsNpc(IdianDepthsSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.IDIAN_DEPTHS_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final IdianDepthsLocation loc = IdianDepthsService.getInstance().getIdianDepthsLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isIdianDepthsOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isIdianDepthsClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnZorshivDredgionNpc(ZorshivDredgionSpawnTemplate spawn, int instanceIndex)
	{
		if (!CustomConfig.ZORSHIV_DREDGION_ENABLED)
		{
			return null;
		}
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final ZorshivDredgionLocation loc = ZorshivDredgionService.getInstance().getZorshivDredgionLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isLanding())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isPeace())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnGatherable(SpawnTemplate spawn, int instanceIndex)
	{
		final int objectId = spawn.getNpcId();
		final VisibleObjectTemplate template = DataManager.GATHERABLE_DATA.getGatherableTemplate(objectId);
		final Gatherable gatherable = new Gatherable(spawn, template, IDFactory.getInstance().nextId(), new GatherableController());
		gatherable.setKnownlist(new PlayerAwareKnownList(gatherable));
		SpawnEngine.bringIntoWorld(gatherable, spawn, instanceIndex);
		return gatherable;
	}
	
	public static Trap spawnTrap(SpawnTemplate spawn, int instanceIndex, Creature creator)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		final Trap trap = new Trap(IDFactory.getInstance().nextId(), new NpcController(), spawn, npcTemplate);
		trap.setKnownlist(new NpcKnownList(trap));
		trap.setEffectController(new EffectController(trap));
		trap.setCreator(creator);
		trap.setVisualState(CreatureVisualState.HIDE1);
		SpawnEngine.bringIntoWorld(trap, spawn, instanceIndex);
		PacketSendUtility.broadcastPacket(trap, new SM_PLAYER_STATE(trap));
		return trap;
	}
	
	public static GroupGate spawnGroupGate(SpawnTemplate spawn, int instanceIndex, Creature creator)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		final GroupGate groupgate = new GroupGate(IDFactory.getInstance().nextId(), new NpcController(), spawn, npcTemplate);
		groupgate.setKnownlist(new PlayerAwareKnownList(groupgate));
		groupgate.setEffectController(new EffectController(groupgate));
		groupgate.setCreator(creator);
		SpawnEngine.bringIntoWorld(groupgate, spawn, instanceIndex);
		return groupgate;
	}
	
	public static Kisk spawnKisk(SpawnTemplate spawn, int instanceIndex, Player creator)
	{
		final int npcId = spawn.getNpcId();
		final NpcTemplate template = DataManager.NPC_DATA.getNpcTemplate(npcId);
		final Kisk kisk = new Kisk(IDFactory.getInstance().nextId(), new NpcController(), spawn, template, creator);
		kisk.setKnownlist(new PlayerAwareKnownList(kisk));
		kisk.setCreator(creator);
		kisk.setEffectController(new EffectController(kisk));
		SpawnEngine.bringIntoWorld(kisk, spawn, instanceIndex);
		return kisk;
	}
	
	public static Npc spawnPostman(final Player owner)
	{
		final int npcId = owner.getRace() == Race.ELYOS ? 798100 : 798101;
		final NpcData npcData = DataManager.NPC_DATA;
		final NpcTemplate template = npcData.getNpcTemplate(npcId);
		final IDFactory iDFactory = IDFactory.getInstance();
		final int worldId = owner.getWorldId();
		final int instanceId = owner.getInstanceId();
		final double radian = Math.toRadians(MathUtil.convertHeadingToDegree(owner.getHeading()));
		final Vector3f pos = GeoService.getInstance().getClosestCollision(owner, owner.getX() + (float) (Math.cos(radian) * 5), owner.getY() + (float) (Math.sin(radian) * 5), owner.getZ(), false, CollisionIntention.PHYSICAL.getId());
		final SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, pos.getX(), pos.getY(), pos.getZ(), (byte) 0);
		final Npc postman = new Npc(iDFactory.nextId(), new NpcController(), spawn, template);
		postman.setKnownlist(new PlayerAwareKnownList(postman));
		postman.setEffectController(new EffectController(postman));
		postman.getAi2().onCustomEvent(1, owner);
		SpawnEngine.bringIntoWorld(postman, spawn, instanceId);
		owner.setPostman(postman);
		return postman;
	}
	
	public static Npc spawnFunctionalNpc(final Player owner, int npcId, SummonOwner summonOwner)
	{
		final NpcData npcData = DataManager.NPC_DATA;
		final NpcTemplate template = npcData.getNpcTemplate(npcId);
		final IDFactory iDFactory = IDFactory.getInstance();
		final int worldId = owner.getWorldId();
		final int instanceId = owner.getInstanceId();
		final double radian = Math.toRadians(MathUtil.convertHeadingToDegree(owner.getHeading()));
		final Vector3f pos = GeoService.getInstance().getClosestCollision(owner, owner.getX() + (float) (Math.cos(radian) * 5), owner.getY() + (float) (Math.sin(radian) * 5), owner.getZ(), false, CollisionIntention.PHYSICAL.getId());
		final SpawnTemplate spawn = SpawnEngine.addNewSingleTimeSpawn(worldId, npcId, pos.getX(), pos.getY(), pos.getZ(), (byte) 0);
		final Npc functionalNpc = new Npc(iDFactory.nextId(), new NpcController(), spawn, template);
		functionalNpc.setKnownlist(new PlayerAwareKnownList(functionalNpc));
		functionalNpc.setEffectController(new EffectController(functionalNpc));
		functionalNpc.getAi2().onCustomEvent(1, owner);
		SpawnEngine.bringIntoWorld(functionalNpc, spawn, instanceId);
		return functionalNpc;
	}
	
	public static Servant spawnServant(SpawnTemplate spawn, int instanceIndex, Creature creator, int skillId, int level, NpcObjectType objectType)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		final int creatureLevel = creator.getLevel();
		level = SkillLearnService.getSkillLearnLevel(skillId, creatureLevel, level);
		final byte servantLevel = (byte) SkillLearnService.getSkillMinLevel(skillId, creatureLevel, level);
		final Servant servant = new Servant(IDFactory.getInstance().nextId(), new NpcController(), spawn, npcTemplate, servantLevel);
		servant.setKnownlist(new NpcKnownList(servant));
		servant.setEffectController(new EffectController(servant));
		servant.setCreator(creator);
		servant.setNpcObjectType(objectType);
		servant.getSkillList().addSkill(servant, skillId, 1);
		SpawnEngine.bringIntoWorld(servant, spawn, instanceIndex);
		final SkillTemplate st = DataManager.SKILL_DATA.getSkillTemplate(skillId);
		if ((st.getStartconditions() != null) && (st.getHpCondition() != null))
		{
			final int hp = (st.getHpCondition().getHpValue() * 3);
			servant.getLifeStats().setCurrentHp(hp);
		}
		return servant;
	}
	
	public static Servant spawnEnemyServant(SpawnTemplate spawn, int instanceIndex, Creature creator, byte servantLvl)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		final Servant servant = new Servant(IDFactory.getInstance().nextId(), new NpcController(), spawn, npcTemplate, servantLvl);
		servant.setKnownlist(new NpcKnownList(servant));
		servant.setEffectController(new EffectController(servant));
		servant.setCreator(creator);
		servant.setNpcObjectType(NpcObjectType.SERVANT);
		SpawnEngine.bringIntoWorld(servant, spawn, instanceIndex);
		return servant;
	}
	
	public static Homing spawnHoming(SpawnTemplate spawn, int instanceIndex, Creature creator, int attackCount, int skillId, int level)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		final int creatureLevel = creator.getLevel();
		level = SkillLearnService.getSkillLearnLevel(skillId, creatureLevel, level);
		final byte homingLevel = (byte) SkillLearnService.getSkillMinLevel(skillId, creatureLevel, level);
		final Homing homing = new Homing(IDFactory.getInstance().nextId(), new NpcController(), spawn, npcTemplate, homingLevel, skillId);
		homing.setState(CreatureState.WEAPON_EQUIPPED);
		homing.setKnownlist(new NpcKnownList(homing));
		homing.setEffectController(new EffectController(homing));
		homing.setCreator(creator);
		int homingSkillId = 0;
		if (homing.getSkillList() != null)
		{
			final NpcSkillEntry hmSkill = homing.getSkillList().getRandomSkill();
			if (hmSkill != null)
			{
				homingSkillId = hmSkill.getSkillId();
			}
		}
		if (homingSkillId != 0)
		{
			homing.getSkillList().addSkill(homing, homingSkillId, 1);
		}
		homing.setActiveSkillId(homingSkillId);
		homing.setAttackCount(attackCount);
		SpawnEngine.bringIntoWorld(homing, spawn, instanceIndex);
		return homing;
	}
	
	public static Summon spawnSummon(Player creator, int npcId, int skillId, int skillLevel, int time)
	{
		final float x = creator.getX();
		final float y = creator.getY();
		final float z = creator.getZ();
		final byte heading = creator.getHeading();
		final int worldId = creator.getWorldId();
		final int instanceId = creator.getInstanceId();
		final SpawnTemplate spawn = SpawnEngine.createSpawnTemplate(worldId, npcId, x, y, z, heading);
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(npcId);
		skillLevel = SkillLearnService.getSkillLearnLevel(skillId, creator.getCommonData().getLevel(), skillLevel);
		final byte level = (byte) SkillLearnService.getSkillMinLevel(skillId, creator.getCommonData().getLevel(), skillLevel);
		final boolean isSiegeWeapon = npcTemplate.getAi().equals("siege_weapon");
		final Summon summon = new Summon(IDFactory.getInstance().nextId(), isSiegeWeapon ? new SiegeWeaponController(npcId) : new SummonController(), spawn, npcTemplate, isSiegeWeapon ? npcTemplate.getLevel() : level, time);
		summon.setKnownlist(new CreatureAwareKnownList(summon));
		summon.setEffectController(new EffectController(summon));
		summon.setMaster(creator);
		summon.getLifeStats().synchronizeWithMaxStats();
		SpawnEngine.bringIntoWorld(summon, spawn, instanceId);
		return summon;
	}
	
	public static Pet spawnPet(Player player, int petId)
	{
		final PetCommonData petCommonData = player.getPetList().getPet(petId);
		if (petCommonData == null)
		{
			return null;
		}
		final PetTemplate petTemplate = DataManager.PET_DATA.getPetTemplate(petId);
		if (petTemplate == null)
		{
			return null;
		}
		final PetController controller = new PetController();
		final Pet pet = new Pet(petTemplate, controller, petCommonData, player);
		pet.setKnownlist(new PlayerAwareKnownList(pet));
		player.setToyPet(pet);
		final float x = player.getX();
		final float y = player.getY();
		final float z = player.getZ();
		final byte heading = player.getHeading();
		final int worldId = player.getWorldId();
		final int instanceId = player.getInstanceId();
		final SpawnTemplate spawn = SpawnEngine.createSpawnTemplate(worldId, petId, x, y, z, heading);
		SpawnEngine.bringIntoWorld(pet, spawn, instanceId);
		return pet;
	}
	
	protected static VisibleObject spawnLandingNpc(LandingSpawnTemplate spawn, int instanceIndex)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final LandingLocation loc = AbyssLandingService.getInstance().getLandingLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isLandingOpen())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isLandingClosed())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
	
	protected static VisibleObject spawnLandingSpecialNpc(LandingSpecialSpawnTemplate spawn, int instanceIndex)
	{
		final int objectId = spawn.getNpcId();
		final NpcTemplate npcTemplate = DataManager.NPC_DATA.getNpcTemplate(objectId);
		if (npcTemplate == null)
		{
			return null;
		}
		final IDFactory iDFactory = IDFactory.getInstance();
		Npc npc;
		final int spawnId = spawn.getId();
		final LandingSpecialLocation loc = AbyssLandingSpecialService.getInstance().getLandingSpecialLocation(spawnId);
		if (loc.isActive() && (spawnId == loc.getId()) && spawn.isSpecialLandingSpawn())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else if (!loc.isActive() && (spawnId == loc.getId()) && spawn.isSpecialLandingDespawn())
		{
			npc = new Npc(iDFactory.nextId(), new NpcController(), spawn, npcTemplate);
			npc.setKnownlist(new NpcKnownList(npc));
		}
		else
		{
			return null;
		}
		npc.setEffectController(new EffectController(npc));
		SpawnEngine.bringIntoWorld(npc, spawn, instanceIndex);
		return npc;
	}
}