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
package com.aionemu.gameserver.dataholders;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.aionemu.gameserver.model.templates.mail.Mails;

/**
 * An instance of this class is the result of data loading.
 * @author Luno, orz Modified by Wakizashi
 */
@XmlRootElement(name = "ae_static_data")
@XmlAccessorType(XmlAccessType.NONE)
public class StaticData
{
	@XmlElement(name = "world_maps")
	public WorldMapsData worldMapsData;
	@XmlElement(name = "npc_trade_list")
	public TradeListData tradeListData;
	@XmlElement(name = "npc_teleporter")
	public TeleporterData teleporterData;
	@XmlElement(name = "teleport_location")
	public TeleLocationData teleLocationData;
	@XmlElement(name = "bind_points")
	public BindPointData bindPointData;
	@XmlElement(name = "quests")
	public QuestsData questData;
	@XmlElement(name = "quest_scripts")
	public XMLQuests questsScriptData;
	@XmlElement(name = "player_experience_table")
	public PlayerExperienceTable playerExperienceTable;
	@XmlElement(name = "player_stats_templates")
	public PlayerStatsData playerStatsData;
	@XmlElement(name = "summon_stats_templates")
	public SummonStatsData summonStatsData;
	@XmlElement(name = "item_templates")
	public ItemData itemData;
	@XmlElement(name = "random_bonuses")
	public ItemRandomBonusData itemRandomBonuses;
	@XmlElement(name = "npc_templates")
	public NpcData npcData;
	@XmlElement(name = "npc_shouts")
	public NpcShoutData npcShoutData;
	@XmlElement(name = "player_initial_data")
	public PlayerInitialData playerInitialData;
	@XmlElement(name = "skill_data")
	public SkillData skillData;
	@XmlElement(name = "motion_times")
	public MotionData motionData;
	@XmlElement(name = "skill_tree")
	public SkillTreeData skillTreeData;
	@XmlElement(name = "cube_expander")
	public CubeExpandData cubeExpandData;
	@XmlElement(name = "warehouse_expander")
	public WarehouseExpandData warehouseExpandData;
	@XmlElement(name = "player_titles")
	public TitleData titleData;
	@XmlElement(name = "gatherable_templates")
	public GatherableData gatherableData;
	@XmlElement(name = "npc_walker")
	public WalkerData walkerData;
	@XmlElement(name = "zones")
	public ZoneData zoneData;
	@XmlElement(name = "goodslists")
	public GoodsListData goodsListData;
	@XmlElement(name = "tribe_relations")
	public TribeRelationsData tribeRelationsData;
	@XmlElement(name = "recipe_templates")
	public RecipeData recipeData;
	@XmlElement(name = "luna_templates")
	public LunaData lunaData;
	@XmlElement(name = "chest_templates")
	public ChestData chestData;
	@XmlElement(name = "staticdoor_templates")
	public StaticDoorData staticDoorData;
	@XmlElement(name = "item_sets")
	public ItemSetData itemSetData;
	@XmlElement(name = "npc_factions")
	public NpcFactionsData npcFactionsData;
	@XmlElement(name = "npc_skill_templates")
	public NpcSkillData npcSkillData;
	@XmlElement(name = "pet_skill_templates")
	public PetSkillData petSkillData;
	@XmlElement(name = "siege_locations")
	public SiegeLocationData siegeLocationData;
	@XmlElement(name = "fly_rings")
	public FlyRingData flyRingData;
	@XmlElement(name = "shields")
	public ShieldData shieldData;
	@XmlElement(name = "pets")
	public PetData petData;
	@XmlElement(name = "pet_feed")
	public PetFeedData petFeedData;
	@XmlElement(name = "dopings")
	public PetDopingData petDopingData;
	@XmlElement(name = "merchands")
	public PetMerchandData petMerchandData;
	@XmlElement(name = "guides")
	public GuideHtmlData guideData;
	@XmlElement(name = "roads")
	public RoadData roadData;
	@XmlElement(name = "instance_cooltimes")
	public InstanceCooltimeData instanceCooltimeData;
	@XmlElement(name = "decomposable_items")
	public DecomposableItemsData decomposableItemsData;
	@XmlElement(name = "ai_templates")
	public AIData aiData;
	@XmlElement(name = "flypath_template")
	public FlyPathData flyPath;
	@XmlElement(name = "windstreams")
	public WindstreamData windstreamsData;
	@XmlElement(name = "item_restriction_cleanups")
	public ItemRestrictionCleanupData itemCleanup;
	@XmlElement(name = "assembled_npcs")
	public AssembledNpcsData assembledNpcData;
	@XmlElement(name = "cosmetic_items")
	public CosmeticItemsData cosmeticItemsData;
	@XmlElement(name = "npc_drops")
	public NpcDropData npcDropData;
	@XmlElement(name = "auto_groups")
	public AutoGroupData autoGroupData;
	@XmlElement(name = "events_config")
	public EventData eventData;
	@XmlElement(name = "spawns")
	public SpawnsData2 spawnsData2;
	@XmlElement(name = "item_groups")
	public ItemGroupsData itemGroupsData;
	@XmlElement(name = "polymorph_panels")
	public PanelSkillsData panelSkillsData;
	@XmlElement(name = "instance_bonusattrs")
	public InstanceBuffData instanceBuffData;
	@XmlElement(name = "housing_objects")
	public HousingObjectData housingObjectData;
	@XmlElement(name = "rides")
	public RideData rideData;
	@XmlElement(name = "instance_exits")
	public InstanceExitData instanceExitData;
	@XmlElement(name = "portal_locs")
	PortalLocData portalLocData;
	@XmlElement(name = "portal_templates2")
	Portal2Data portalTemplate2;
	@XmlElement(name = "house_lands")
	public HouseData houseData;
	@XmlElement(name = "buildings")
	public HouseBuildingData houseBuildingData;
	@XmlElement(name = "house_parts")
	public HousePartsData housePartsData;
	@XmlElement(name = "curing_objects")
	public CuringObjectsData curingObjectsData;
	@XmlElement(name = "house_npcs")
	public HouseNpcsData houseNpcsData;
	@XmlElement(name = "assembly_items")
	public AssemblyItemsData assemblyItemData;
	@XmlElement(name = "multi_returns")
	public MultiReturnItemData multiReturnItemData;
	@XmlElement(name = "lboxes")
	public HouseScriptData houseScriptData;
	@XmlElement(name = "mails")
	public Mails systemMailTemplates;
	@XmlElement(name = "challenge_tasks")
	public ChallengeData challengeData;
	@XmlElement(name = "town_spawns_data")
	public TownSpawnsData townSpawnsData;
	@XmlElement(name = "charge_skills")
	public ChargeSkillData chargeSkillData;
	@XmlElement(name = "spring_objects")
	public SpringObjectsData springObjectsData;
	@XmlElement(name = "robots")
	public RobotData robotData;
	@XmlElement(name = "abyss_bonusattrs")
	public AbyssBuffData abyssBuffData;
	@XmlElement(name = "abyss_groupattrs")
	public AbyssGroupData abyssGroupData;
	@XmlElement(name = "absolute_stats")
	public AbsoluteStatsData absoluteStatsData;
	@XmlElement(name = "base_locations")
	public BaseData baseData;
	@XmlElement(name = "material_templates")
	public MaterialData materiaData;
	@XmlElement(name = "weather")
	public MapWeatherData mapWeatherData;
	@XmlElement(name = "dimensional_vortex")
	public VortexData vortexData;
	@XmlElement(name = "beritra_invasion")
	public BeritraData beritraData;
	@XmlElement(name = "agent_fight")
	public AgentData agentData;
	@XmlElement(name = "svs")
	public SvsData svsData;
	@XmlElement(name = "rvr")
	public RvrData rvrData;
	@XmlElement(name = "moltenus")
	public MoltenusData moltenusData;
	@XmlElement(name = "dynamic_rift")
	public DynamicRiftData dynamicRiftData;
	@XmlElement(name = "instance_rift")
	public InstanceRiftData instanceRiftData;
	@XmlElement(name = "nightmare_circus")
	public NightmareCircusData nightmareCircusData;
	@XmlElement(name = "zorshiv_dredgion")
	public ZorshivDredgionData zorshivDredgionData;
	@XmlElement(name = "dominion_locations")
	public LegionDominionData legionDominionData;
	@XmlElement(name = "idian_depths")
	public IdianDepthsData idianDepthsData;
	@XmlElement(name = "anoha")
	public AnohaData anohaData;
	@XmlElement(name = "iu")
	public IuData iuData;
	@XmlElement(name = "conquest")
	public ConquestData conquestData;
	@XmlElement(name = "serial_guards")
	public SerialGuardData serialGuardData;
	@XmlElement(name = "serial_killers")
	public SerialKillerData serialKillerData;
	@XmlElement(name = "rift_locations")
	public RiftData riftData;
	@XmlElement(name = "service_bonusattrs")
	public ServiceBuffData serviceBuffData;
	@XmlElement(name = "players_service_bonusattrs")
	public PlayersBonusData playersBonusData;
	@XmlElement(name = "enchant_templates")
	public ItemEnchantData itemEnchantData;
	@XmlElement(name = "hotspot_location")
	public HotspotLocationData hotspotLocationData;
	@XmlElement(name = "item_upgrades")
	public ItemUpgradeData itemUpgradeData;
	@XmlElement(name = "login_events")
	public AreianPassportData areianPassportData;
	@XmlElement(name = "game_experience_items")
	public GameExperienceData gameExperienceData;
	@XmlElement(name = "abyss_ops")
	public AbyssOpData abyssOpData;
	@XmlElement(name = "panel_cps")
	public PanelCpData panelCpData;
	@XmlElement(name = "decomposable_select_items")
	public DecomposableSelectItemsData decomposableSelectItemsData;
	@XmlElement(name = "pet_bonusattrs")
	public PetBuffData petBuffData;
	@XmlElement(name = "landing")
	public LandingData landingLocationData;
	@XmlElement(name = "landing_special")
	public LandingSpecialData landingSpecialLocationData;
	@XmlElement(name = "luna_consume_rewards")
	public LunaConsumeRewardsData lunaConsumeRewardsData;
	@XmlElement(name = "item_custom_sets")
	public ItemCustomSetData itemCustomSet;
	@XmlElement(name = "f2p_bonus")
	public F2PBonusData f2pBonus;
	
	@SuppressWarnings("unused")
	private void afterUnmarshal(Unmarshaller unmarshaller, Object parent)
	{
		DataManager.log.info("Loaded " + worldMapsData.size() + " MAP");
		DataManager.log.info("Loaded " + playerExperienceTable.getMaxLevel() + " LEVEL");
		DataManager.log.info("Loaded " + playerStatsData.size() + " PLAYER STATS TEMPLATE");
		DataManager.log.info("Loaded " + summonStatsData.size() + " SUMMON STATS TEMPLATE");
		DataManager.log.info("Loaded " + itemCleanup.size() + " ITEM CLEANUP");
		DataManager.log.info("Loaded " + itemData.size() + " ITEM TEMPLATE");
		DataManager.log.info("Loaded " + itemRandomBonuses.size() + " RANDOM BONUS TEMPLATE");
		DataManager.log.info("Loaded " + itemGroupsData.bonusSize() + " BONUS ITEM GROUP TEMPLATE");
		DataManager.log.info("Loaded " + itemGroupsData.petFoodSize() + " PET FOOD ITEM");
		DataManager.log.info("Loaded " + npcData.size() + " NPC TEMPLATES");
		DataManager.log.info("Loaded " + systemMailTemplates.size() + " SYSTEM MAIL TEMPLATE");
		DataManager.log.info("Loaded " + npcShoutData.size() + " NPC SHOUT TEMPLATE");
		DataManager.log.info("Loaded " + petData.size() + " PET TEMPLATE");
		DataManager.log.info("Loaded " + petFeedData.size() + " FOOD FLAVOUR");
		DataManager.log.info("Loaded " + petDopingData.size() + " PET DOPING TEMPLATE");
		DataManager.log.info("Loaded " + petMerchandData.size() + " PET MERCHAND TEMPLATE");
		DataManager.log.info("Loaded " + playerInitialData.size() + " INITIAL PLAYER TEMPLATE");
		DataManager.log.info("Loaded " + goodsListData.size() + " GOODLIST");
		DataManager.log.info("Loaded " + tradeListData.size() + " NPC TRADE LIST");
		DataManager.log.info("Loaded " + teleporterData.size() + " NPC TELEPORTER TEMPLATE");
		DataManager.log.info("Loaded " + teleLocationData.size() + " TELEPORT LOCATION");
		DataManager.log.info("Loaded " + hotspotLocationData.size() + " HOTSPOT LOCATION");
		DataManager.log.info("Loaded " + skillData.size() + " SKILL TEMPLATES");
		DataManager.log.info("Loaded " + motionData.size() + " MOTION TIMES");
		DataManager.log.info("Loaded " + chargeSkillData.size() + " CHARGE SKILL");
		DataManager.log.info("Loaded " + skillTreeData.size() + " SKILL LEARN");
		DataManager.log.info("Loaded " + cubeExpandData.size() + " CUBE EXPANDER");
		DataManager.log.info("Loaded " + warehouseExpandData.size() + " WAREHOUSE EXPANDER");
		DataManager.log.info("Loaded " + bindPointData.size() + " BIND POINT");
		DataManager.log.info("Loaded " + questData.size() + " QUEST DATA");
		DataManager.log.info("Loaded " + gatherableData.size() + " GATHERABLE");
		DataManager.log.info("Loaded " + titleData.size() + " TITLE");
		DataManager.log.info("Loaded " + walkerData.size() + " WALKER ROAD");
		DataManager.log.info("Loaded " + zoneData.size() + " ZONE");
		DataManager.log.info("Loaded " + tribeRelationsData.size() + " TRIBE RELATION");
		DataManager.log.info("Loaded " + recipeData.size() + " RECIPE TEMPLATE");
		DataManager.log.info("Loaded " + lunaData.size() + " LUNA TEMPLATE");
		DataManager.log.info("Loaded " + chestData.size() + " CHEST");
		DataManager.log.info("Loaded " + staticDoorData.size() + " STATIC DOOR");
		DataManager.log.info("Loaded " + itemSetData.size() + " ITEM SET");
		DataManager.log.info("Loaded " + npcFactionsData.size() + " NPC FACTION");
		DataManager.log.info("Loaded " + npcSkillData.size() + " NPC SKILL LIST");
		DataManager.log.info("Loaded " + petSkillData.size() + " PET SKILL LIST");
		DataManager.log.info("Loaded " + siegeLocationData.size() + " SIEGE LOCATION");
		DataManager.log.info("Loaded " + flyRingData.size() + " FLY RING");
		DataManager.log.info("Loaded " + shieldData.size() + " SHIELD");
		DataManager.log.info("Loaded " + petData.size() + " PET");
		DataManager.log.info("Loaded " + guideData.size() + " GUIDE");
		DataManager.log.info("Loaded " + roadData.size() + " ROAD");
		DataManager.log.info("Loaded " + instanceCooltimeData.size() + " INSTANCE COOLTIME");
		DataManager.log.info("Loaded " + decomposableItemsData.size() + " DECOMPOSABLE ITEM");
		DataManager.log.info("Loaded " + decomposableSelectItemsData.size() + " DECOMPOSABLE SELECT ITEM");
		DataManager.log.info("Loaded " + aiData.size() + " AI2 TEMPLATE");
		DataManager.log.info("Loaded " + flyPath.size() + " FLYPATH TEMPLATE");
		DataManager.log.info("Loaded " + windstreamsData.size() + " WINDSTREAM");
		DataManager.log.info("Loaded " + assembledNpcData.size() + " ASSEMBLED NPC");
		DataManager.log.info("Loaded " + cosmeticItemsData.size() + " COSMETICS ITEM");
		DataManager.log.info("Loaded " + npcDropData.size() + " NPC DROP");
		DataManager.log.info("Loaded " + autoGroupData.size() + " AUTO GROUP");
		DataManager.log.info("Loaded " + spawnsData2.size() + " SPAWN MAP");
		DataManager.log.info("Loaded " + eventData.size() + " EVENT");
		DataManager.log.info("Loaded " + panelSkillsData.size() + " POLYMORPH PANEL");
		DataManager.log.info("Loaded " + instanceBuffData.size() + " INSTANCE BONUS");
		DataManager.log.info("Loaded " + housingObjectData.size() + " HOUSING OBJECT");
		DataManager.log.info("Loaded " + rideData.size() + " RIDE");
		DataManager.log.info("Loaded " + robotData.size() + " AETHERTECH ARMOR");
		DataManager.log.info("Loaded " + instanceExitData.size() + " INSTANCE EXIT");
		DataManager.log.info("Loaded " + portalLocData.size() + " PORTAL LOCATION");
		DataManager.log.info("Loaded " + portalTemplate2.size() + " PORTAL TEMPLATE");
		DataManager.log.info("Loaded " + houseData.size() + " HOUSING LAND");
		DataManager.log.info("Loaded " + houseBuildingData.size() + " HOUSING BUILDING STYLE");
		DataManager.log.info("Loaded " + housePartsData.size() + " HOUSE PART");
		DataManager.log.info("Loaded " + houseNpcsData.size() + " HOUSE SPAWN");
		DataManager.log.info("Loaded " + houseScriptData.size() + " HOUSE SCRIPTS");
		DataManager.log.info("Loaded " + curingObjectsData.size() + " CURING OBJECT");
		DataManager.log.info("Loaded " + springObjectsData.size() + " SPRING OBJECT");
		DataManager.log.info("Loaded " + assemblyItemData.size() + " ASSEMBLY ITEM");
		DataManager.log.info("Loaded " + challengeData.size() + " CHALLENGE TASK");
		DataManager.log.info("Loaded " + townSpawnsData.getSpawnsCount() + " TOWN LOCATION");
		DataManager.log.info("Loaded " + abyssBuffData.size() + " ABYSS BONUS");
		DataManager.log.info("Loaded " + abyssGroupData.size() + " ABYSS GROUP");
		DataManager.log.info("Loaded " + absoluteStatsData.size() + " ABSOLUTE STATS");
		DataManager.log.info("Loaded " + baseData.size() + " BASES LOCATION");
		DataManager.log.info("Loaded " + agentData.size() + " AGENT FIGHT");
		DataManager.log.info("Loaded " + beritraData.size() + " ERESUKIGAL/BERITRA INVASION");
		DataManager.log.info("Loaded " + svsData.size() + " S.v.S LOCATION");
		DataManager.log.info("Loaded " + rvrData.size() + " R.v.R LOCATION");
		DataManager.log.info("Loaded " + moltenusData.size() + " MOLTENUS LOCATION");
		DataManager.log.info("Loaded " + dynamicRiftData.size() + " DYNAMIC RIFT LOCATION");
		DataManager.log.info("Loaded " + instanceRiftData.size() + " INSTANCE RIFT LOCATION");
		DataManager.log.info("Loaded " + nightmareCircusData.size() + " NIGHTMARE CIRCUS LOCATION");
		DataManager.log.info("Loaded " + zorshivDredgionData.size() + " ZORSHIV DREDGION LOCATION");
		DataManager.log.info("Loaded " + legionDominionData.size() + " LEGION DOMINION LOCATION");
		DataManager.log.info("Loaded " + anohaData.size() + " ANOHA LOCATION");
		DataManager.log.info("Loaded " + iuData.size() + " CONCERT LOCATION");
		DataManager.log.info("Loaded " + conquestData.size() + " CONQUEST/OFFERING LOCATION");
		DataManager.log.info("Loaded " + idianDepthsData.size() + " IDIAN DEPTHS LOCATION");
		DataManager.log.info("Loaded " + materiaData.size() + " MATERIALS");
		DataManager.log.info("Loaded " + mapWeatherData.size() + " WEATHER MAP");
		DataManager.log.info("Loaded " + vortexData.size() + " VORTEX");
		DataManager.log.info("Loaded " + serialGuardData.size() + " SERIAL GUARD");
		DataManager.log.info("Loaded " + serialKillerData.size() + " SERIAL KILLER");
		DataManager.log.info("Loaded " + riftData.size() + " RIFT/VOLATILE/CHAOS RIFT");
		DataManager.log.info("Loaded " + serviceBuffData.size() + " SERVICE BONUS");
		DataManager.log.info("Loaded " + playersBonusData.size() + " PLAYER BONUS");
		DataManager.log.info("Loaded " + f2pBonus.size() + " F2P BONUS PACK");
		DataManager.log.info("Loaded " + itemEnchantData.size() + " ITEM ENCHANT TABLE");
		DataManager.log.info("Loaded " + itemUpgradeData.size() + " ITEM UPGRADE");
		DataManager.log.info("Loaded " + areianPassportData.size() + " ATREIAN PASSPORT");
		DataManager.log.info("Loaded " + gameExperienceData.size() + " GAME EXPERIENCE ITEM");
		DataManager.log.info("Loaded " + abyssOpData.size() + " ABYSS LANDING TABLE");
		DataManager.log.info("Loaded " + panelCpData.size() + " PANEL CP");
		DataManager.log.info("Loaded " + petBuffData.size() + " PET BUFF");
		DataManager.log.info("Loaded " + multiReturnItemData.size() + " MULTI RETURN ITEM");
		DataManager.log.info("Loaded " + landingLocationData.size() + " ABYSS LANDING LOCATION");
		DataManager.log.info("Loaded " + landingSpecialLocationData.size() + " MONUMENT LOCATION");
		DataManager.log.info("Loaded " + lunaConsumeRewardsData.size() + " LUNA CONSUME REWARD");
		DataManager.log.info("Loaded " + itemCustomSet.size() + " ITEM CUSTOM SET");
	}
}