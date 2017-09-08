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
package com.aionemu.gameserver.model;

import javax.xml.bind.annotation.XmlEnum;

/**
 * @rework Blackfire
 */
@XmlEnum
public enum TribeClass
{
	AB1_AGGRESSIVESINGLEMONSTER,
	AB1_AGGRESSIVESUPPORTMONSTER,
	AB1_BOSS,
	AB1_DOOR_DA,
	AB1_DOOR_LI,
	AB1_DOOR_KILLER,
	AB1_U_DRAKAN,
	AB1_U_SHUGO,
	ABDARK_AABDRAGON,
	ABDRAGON_AABDARK,
	ABYSSDRAKANGATE,
	AGGRESSIVE1_AAGGRESSIVE2,
	AGGRESSIVE2_AAGGRESSIVE1,
	AGGRESSIVEESCORT,
	AGRRESSIVEFRIENDLYVRITRA,
	AGRRESSIVEFRIENDLYVRITRA2,
	AGGRESSIVEMONSTER,
	AGGRESSIVESINGLEMONSTER,
	AGGRESSIVESUPPORTMONSTER,
	AGGRESSIVESUPPORTMONSTER2,
	AGGRESSIVETOIDELIM,
	AGGRESSIVETOPCPET,
	AGGRESSIVETOSHULACK,
	AGGRESSIVETOSHULACK2,
	AGRRESSIVEVRITRAANDPC,
	AGGRESSIVE_ALL,
	AGGRESSIVE_DARK,
	AGGRESSIVE_DARK_HSPECTRE,
	AGGRESSIVE_DRAGON,
	AGGRESSIVE_LIGHT,
	AGGRESSIVE_LIGHT_HSPECTRE,
	AGRESSIVETOMONSTER,
	AHELLHOUND,
	AIREL1,
	AIREL2,
	AIREL3,
	AIRELBOSS,
	ANTI_CRYSTAL,
	APRETOR,
	ARCHERYBASFELT2_ATARGETBASFELT2_DF1,
	ARCHERYBASFELT2_ATARGETBASFELT2_LF1,
	ARCHERYBASFELT_ATARGETBASFELT_DF1,
	ARCHERYBASFELT_ATARGETBASFELT_LF1,
	ASIST_D(true),
	ATAURIC,
	ATKDRAKAN,
	BAT_FAMILY_ELITE,
	BMDGUARDIAN,
	BMLGUARDIAN,
	BOMB_LIZARDMAN,
	BRAX,
	BROHUM,
	BROWNIE,
	BROWNIECOWARD,
	BROWNIEFELLER_HZAIF_LF1,
	BROWNIEGUARD,
	CALYDON,
	CALYDON_POLYMORPH,
	CHERUBIM2ND,
	CHILDMONSTER,
	CONSIADE,
	CONSIADE_SUM,
	CRESTLICH,
	CRYSTAL,
	CRYSTAL_NMDD,
	CRYSTAL_SUM,
	CYCLOPSBOSS,
	D1_HKERUBIM_LF1,
	DARK_LICH,
	DARK_MOB,
	DARK_NPC,
	DARK_SUR_MOB,
	DARU,
	DARU_HZAIF,
	DC_HERO,
	DC_VILLAIN,
	DECOY,
	DECOY_HUNGER,
	DF5_DUMMY1,
	DF5_DUMMY2,
	DF5_DUMMY1_DGUARD(Race.ASMODIANS),
	DF5_DUMMY2_DGUARD(Race.ASMODIANS),
	DF5_FRILLFAIMAM,
	DF5_FRILLFAIMAM_BABY,
	DF5_FUNGY,
	DF5_GUARD_01_DARK(Race.ASMODIANS),
	DF5_GUARD_02_DARK(Race.ASMODIANS),
	DF5_GUARD_03_DARK(Race.ASMODIANS),
	DF5_GUARD_04_DARK(Race.ASMODIANS),
	DF5_GUARD_SCENE,
	DF5_GUARD_TRAP,
	DF5_MERMAN,
	DF5_MERMAN_MURATUN,
	DF5_VRITRA,
	DF5_VRITRA_SCENE,
	DRAGGMOB_ADRGUARD1,
	DRAGON,
	DRAGON_CTRL,
	DRAGON_SLAVE,
	DRAKANDF3BOSS,
	DRAKANDF3SLAVE,
	DRAKANDOOR,
	DRAKANPOLYMORPH,
	DRAKAN_DGUARD,
	DRAKAN_LGUARD,
	DRAKEPURPLE_MASTER,
	DRAKEPURPLE_SLAVE,
	DRAKY_BOMB_EX,
	DRAKY_BOMB_MASTER,
	DRAMATA,
	DRAMATATIMERA,
	DRAMATATIMERB,
	DRAMA_EVE_NONPC_A,
	DRAMA_EVE_NONPC_B,
	DRAMA_EVE_NONPC_DARKA,
	DRAMA_EVE_NONPC_DARKB,
	DRAMA_KIMEIA_DARKNPC,
	DRAMA_KIMEIA_MOB,
	DRAMA_KIMEIA_NPC,
	DREADGION_MODULE,
	DREADGION_MODULE_WEAPON,
	DREADGION_SUB_NPC,
	DREADGION_TANK,
	DUMMY,
	DUMMY2,
	DUMMY2_DGUARD(Race.ASMODIANS),
	DUMMY2_LGUARD(Race.ELYOS),
	DUMMY_DGUARD(Race.ASMODIANS),
	DUMMY_LGUARD(Race.ELYOS),
	ELEMENTAL_AIR,
	ELEMENTAL_EARTH,
	ELEMENTAL_FIRE,
	ELEMENTAL_LIGHT,
	ELEMENTAL_WATER,
	ENEMY_AGUARD_DARK,
	ERESUKIGAL,
	ERESUKIGAL_BOMB,
	ERESUKIGAL_DREAD,
	ERESUKIGAL_ENEMY,
	ERESUKIGAL_ENEMY_POITION,
	ERESUKIGAL_ENEMY_WEAPON,
	ERESUKIGAL_ENEMY_WEAPON_02,
	ERESUKIGAL_KILLER,
	ESCORT,
	ESCORT_IDF6_LF1,
	ETTIN,
	EVENT_SOLO_MONSTER,
	EVENT_SOLO_POLYMORPH,
	EVENT_SOLO_PORGUSS,
	EVENT_SOLO_TOG,
	F4GUARD_DARK(Race.ASMODIANS),
	F4GUARD_DRAGON(Race.DRAGON),
	F4GUARD_LIGHT(Race.ELYOS),
	F4RAID,
	F6_CALYDON,
	F6_EVENT_ATTACKER_DA(Race.ASMODIANS),
	F6_EVENT_ATTACKER_LI(Race.ELYOS),
	F6_EVENT_CANNON_DA(Race.ASMODIANS),
	F6_EVENT_CANNON_LI(Race.ELYOS),
	F6_EVENT_DA(Race.ASMODIANS),
	F6_EVENT_LI(Race.ELYOS),
	F6_INVASION_DA(Race.ASMODIANS),
	F6_INVASION_LI(Race.ELYOS),
	F6_KRALL,
	F6_LYCAN,
	F6_OWLLAU,
	FANATIC,
	FARMER_HKERUBIM_LF1,
	FETHLOT,
	FIELD_OBJECT_ALL,
	FIELD_OBJECT_ALL_HOSTILEMONSTER,
	FIELD_OBJECT_ALL_MONSTER,
	FIELD_OBJECT_DARK(true),
	FIELD_OBJECT_LIGHT(true),
	FIREEL1,
	FIREEL2,
	FIREEL3,
	FIREELBOSS,
	FIREFUNGY,
	FIRETEMPLE_MOB,
	FRIENDLYTOIDELIM,
	FRILLFAIMAMBABY,
	FRILLFAIMAMCOUPLE,
	FRILLFAIMAMMOM,
	FUNGUS,
	FUNGY,
	GAB1_KILLER,
	GAB1_MONSTER,
	GAB1_MONSTER_NONAGGRE,
	GAB1_PEACE,
	GAB1_SHUGO,
	GAB1_TURRET,
	GAB1_SUB_ATTACKABLE_FOBJ,
	GAB1_SUB_DEST_69,
	GAB1_SUB_DEST_70,
	GAB1_SUB_DEST_71,
	GAB1_SUB_DEST_72,
	GAB1_SUB_DEST_69_AGGRESSIVE,
	GAB1_SUB_DEST_70_AGGRESSIVE,
	GAB1_SUB_DEST_71_AGGRESSIVE,
	GAB1_SUB_DEST_72_AGGRESSIVE,
	GAB1_SUB_DRAKAN,
	GAB1_SUB_KILLER,
	GAB1_SUB_NONAGGRESSIVE_DRAKAN,
	GAB1_01_POINT_01,
	GAB1_01_POINT_02,
	GAB1_01_POINT_03,
	GAB1_01_POINT_04,
	GAB1_01_POINT_05,
	GAB1_01_REMNANT,
	GAB1_02_POINT_01,
	GAB1_02_POINT_02,
	GAB1_02_POINT_03,
	GAB1_02_POINT_04,
	GAB1_02_POINT_05,
	GAB1_02_REMNANT,
	GAB1_03_POINT_01,
	GAB1_03_POINT_02,
	GAB1_03_POINT_03,
	GAB1_03_POINT_04,
	GAB1_03_POINT_05,
	GAB1_03_REMNANT,
	GAB1_04_POINT_01,
	GAB1_04_POINT_02,
	GAB1_04_POINT_03,
	GAB1_04_POINT_04,
	GAB1_04_POINT_05,
	GAB1_04_REMNANT,
	GARGOYLE,
	GARGOYLE_ELITE,
	GENERAL(true),
	GENERALDA_ALIDR,
	GENERALDR_ALIDA,
	GENERALDR_ALIDA_SUPPORT,
	GENERAL_ADADR,
	GENERAL_DARK(true),
	GENERAL_DARK_IDLDF4A_INTRO,
	GENERAL_DARK_LYCAN,
	GENERAL_DRAGON(true),
	GENERAL_IDLDF4A_INTRO,
	GENERAL_KRALL,
	GENERAL_LDF4A_PUBLIC_YUN,
	GHOSTDARK,
	GHOSTLIGHT,
	GHTIMER,
	GMASTER,
	GOBLIN,
	GOD_KAISINEL,
	GOD_MARCHUTAN,
	GOLEM_SWITCH,
	GRAND_RVR(Race.ELYOS),
	GRAND_RVR_DARK(Race.ASMODIANS),
	GRIFFO,
	GRIFFON,
	GSLAVE,
	GUARD(true),
	GUARDDARK_ALEHPAR(Race.ASMODIANS),
	GUARDIAN,
	GUARD_D1NOATTACK,
	GUARD_DARK(true),
	GUARD_DARKAENEMY(Race.ASMODIANS),
	GUARD_DARKMA(Race.ASMODIANS),
	GUARD_DARK_ALYCANARATMAN_DF1(Race.ASMODIANS),
	GUARD_DRAGON,
	GUARD_DRAGONMA,
	GUARD_FTARGETBASFELT_DF1,
	GUARD_FTARGETBASFELT_LF1,
	GUARD_LIGHTMA(Race.ELYOS),
	GUARD_LIGHT_AKERUBIM_LF1(Race.ELYOS),
	GURURU_D1,
	GURURU_DECO,
	HIPPOLIZARD,
	HOLYSERVANT,
	HOLYSERVANT_DEBUFFER,
	HOLYSERVANT_DESPAWN,
	HOSTILEONLYMONSTER,
	HOSTILE_ALL,
	IDASTERIA_IU_ATK,
	IDASTERIA_IU_MONSTER,
	IDASTERIA_IU_MONSTER2,
	IDASTERIA_IU_NPC,
	IDASTERIA_IU_POLYMORPH_D,
	IDASTERIA_IU_POLYMORPH_L,
	IDASTERIA_IU_WORLD_MONSTER,
	IDASTERIA_IU_WORLD_MONSTER2,
	IDASTERIA_IU_WORLD_NPC,
	IDASTERIA_IU_WORLD_POLYMORPH_D,
	IDASTERIA_IU_WORLD_POLYMORPH_L,
	IDCATACOMBS_DRAKE,
	IDCATACOMBS_DRAKE_SUM,
	IDCATACOMBS_TAROS,
	IDDF3_DRAGON_T_BOSS,
	IDELEMENTAL_2_E_BOSS,
	IDELEMENTAL_2_E_NPC,
	IDELEMENTAL_2_HEAL_SUM,
	IDELIM,
	IDELIM_FRIEND,
	IDETERNITY_01_ARTIFACT,
	IDETERNITY_01_ARTIFACTROOMCHECK,
	IDETERNITY_01_NAMED,
	IDETERNITY_01_PCSUPPORT,
	IDETERNITY_01_SINGLEMONSTER,
	IDETERNITY_01_SUMMON,
	IDETERNITY_01_SUPPORTMONSTER,
	IDETERNITY_02_BOSS,
	IDETERNITY_02_BOSS_AREA,
	IDETERNITY_02_BOSS_SUB_01,
	IDETERNITY_02_BOSS_SUB_02,
	IDETERNITY_02_BOSS_SUBF,
	IDETERNITY_02_BOSS_SUM,
	IDETERNITY_02_BOSSF,
	IDETERNITY_02_POLYMORPH,
	IDETERNITY_02_POLYMORPH_DISPEL,
	IDETERNITY_02_POLYMORPH_ENEMY,
	IDEVENT01_MC,
	IDEVENT01_POLYMORPH_D,
	IDEVENT01_POLYMORPH_L,
	IDEVENT01_SUMMON,
	IDEVENT01_TOWER,
	IDF5_R2_CANNON,
	IDF5_R2_CANNON_ATTACK,
	IDF5_R2_SYNC1,
	IDF5_R2_SYNC1_ATTACK,
	IDF5_R2_SYNC2,
	IDF5_R2_SYNC2_ATTACK,
	IDF5_R2_SYNC3,
	IDF5_R2_SYNC3_ATTACK,
	IDF5_SIEGEWEAPON,
	IDF5_SIEGEWEAPON_ATTACK,
	IDF5_SIEGEWEAPON_PC_DARK,
	IDF5_TELEPORT_MONSTER,
	IDF5_TELEPORT_NPC,
	IDF5_TD_ASSULT,
	IDF5_TD_BOMBER,
	IDF5_TD_COMMANDER_DA,
	IDF5_TD_COMMANDER_LI,
	IDF5_TD_DOOR,
	IDF5_TD_GUARD_DARK,
	IDF5_TD_GUARD_LIGHT,
	IDF5_TD_SIEGE,
	IDF5_TD_WAR_DARK,
	IDF5_TD_WAR_LIGHT,
	IDF5_TD_WEAPON_PC,
	IDF5_TD_WEAPON_PC_DARK,
	IDF5U1_AGGRESSIVETANK,
	IDF5U1_PCFLAG,
	IDF5U1_TANK,
	IDF5U1_VRITRA,
	IDF5U1_VRITRAFLAG,
	IDF5U1_VRITRATRAP,
	IDF5U1_VRITRAWEAPON,
	IDF5U1_WAR_01_GUARD,
	IDF5U1_WAR_01_GUARD_DARK,
	IDF5U1_WAR_01_REWARD,
	IDF5U2_ARROWTRAP,
	IDF5U2_FOBJ,
	IDF5U2_GUARD,
	IDF5U2_GUARD_DARK,
	IDF5U2_SHULACK,
	IDF5U2_SHULACK_ESCORT,
	IDF5U2_SHULACK_SLAVE,
	IDF5U2_VRITRA,
	IDF5U2_VRITRATRAP,
	IDF5U3_CANNON,
	IDF5U3_OBJ,
	IDF5U3_VRITRA,
	IDF5U3_VRITRA_01,
	IDF5U3_VRITRA_02,
	IDF5U3_VRITRA_HIDE,
	IDF5U3_VRITRA_BOSS_SUM,
	IDF5U3_VRITRA_SUM,
	IDF6_ADMA_BAT,
	IDF6_ADMA_ZOMBIE,
	IDF6_DRAGON_ARTIFACT,
	IDF6_LAP_SHELUK,
	IDF6_LAP_SHELUK_BABY,
	IDF6_LAP_STATUE,
	IDF6_LAP_TESINON,
	IDF6_LF1_PROTECT,
	IDF6_LF1_PROTECT_D,
	IDFORTRESS_SWITCH_DARK,
	IDFORTRESS_SWITCH_LIGHT,
	IDFORTRESS_VRITRA,
	IDKAMAR_CANNON,
	IDKAMAR_PROTECTGUARD_DARK,
	IDKAMAR_PROTECTGUARD_LIGHT,
	IDKAMAR_SIEGEWEAPON_ATTACK,
	IDKAMAR_SIEGEWEAPON_DARK,
	IDKAMAR_SIEGEWEAPON_LIGHT,
	IDKAMAR_VRITRA,
	IDLDF4A_DECOY,
	IDLDF4_RE_01,
	IDLDF4_RE_01_DOOR,
	IDLDF4_RE_01_FRIEND,
	IDLDF4_RE_01_GUARD,
	IDLDF4_RE_01_GUARD_DARK,
	IDLDF5_UNDER_RUNE,
	IDLDF5RE_SOLO_Q,
	IDLDF5RE_SOLO_CRISTAL_01,
	IDLDF5RE_SOLO_VRITRA_01,
	IDLF1_MONSTER,
	IDLUNA_DEF_BOMB,
	IDLUNA_DEF_DEAD_NPC,
	IDLUNA_DEF_MONSTER,
	IDLUNA_DEF_MONSTER2,
	IDLUNA_DEF_MONSTER3,
	IDLUNA_DEF_MONSTER4,
	IDLUNA_DEF_MONSTER5,
	IDLUNA_DEF_MONSTER6,
	IDLUNA_DEF_NPC,
	IDLUNA_DEF_NPC2,
	IDLUNA_DEF_NPC3,
	IDLUNA_DEF_NPC4,
	IDLUNA_DEF_NPC5,
	IDLUNA_DEF_NPC6,
	IDLUNA_DEF_NPC7,
	IDLUNA_DEF_POLYMORPH_D,
	IDLUNA_DEF_POLYMORPH_L,
	IDLUNA_SIEGE_BOMB,
	IDLUNA_SIEGE_BOMB_02,
	IDLUNA_SIEGE_CANNON,
	IDLUNA_SIEGE_ELECTRO,
	IDLUNA_SIEGE_ETC_01,
	IDLUNA_SIEGE_GATE,
	IDLUNA_SIEGE_MONSTER,
	IDLUNA_SIEGE_NPC,
	IDLUNA_SIEGE_POLYMORPH_D,
	IDLUNA_SIEGE_POLYMORPH_L,
	IDLUNA_SIEGE_REWARD,
	IDLUNA_SIEGE_TOWER,
	IDRAKSHA_DRAGONTOOTH,
	IDRAKSHA_DRAKAN,
	IDRAKSHA_NORMAL,
	IDRAKSHA_RAKSHA,
	IDRUNEWP_AGGRESSIVEANCIENTARM,
	IDRUNEWP_ANCIENTARM,
	IDRUNEWP_ESCORT,
	IDRUNEWP_RUNEDEVICE,
	IDRUNEWP_RUNEFRIEND,
	IDRUNEWP_VRITRA,
	IDRUNEWP_VRITRADEVICE,
	IDSEAL_BOSS,
	IDSEAL_BOSS_OBEY,
	IDSEAL_BOSS_SCENE,
	IDSEAL_BOSS_SKILL_01,
	IDSEAL_BOSS_SKILL_02,
	IDSEAL_BOSS_SKILL_03,
	IDSEAL_BOSS_SKILL_04,
	IDSEAL_BOSS_SKILL_05,
	IDSEAL_BOSS_SUMMON,
	IDSEAL_GROUP_LEADER_1,
	IDSEAL_GROUP_LEADER_2,
	IDSEAL_GROUP_LEADER_3,
	IDSEAL_GROUP_MONSTER_1,
	IDSEAL_GROUP_MONSTER_2,
	IDSEAL_GROUP_MONSTER_3,
	IDSEAL_MONSTER,
	IDSEAL_PCGUARD,
	IDSEAL_PCGUARD_SCENE,
	IDSEAL_Q_BOSS,
	IDSEAL_Q_GUARD,
	IDSEAL_Q_GUARD_PRIEST,
	IDSEAL_Q_KEYNAMED,
	IDSEAL_Q_MOB,
	IDSEAL_Q_MOB_A,
	IDSEAL_Q_NPC,
	IDSEAL_Q_SKILL_01,
	IDSEAL_Q_SKILL_02,
	IDSEAL_Q_SKILL_03,
	IDSEAL_Q_SKILL_04,
	IDSEAL_Q_WAVE,
	IDSEAL_SEALGUARD,
	IDSEAL_SEAL_GUARD_SKILL,
	IDSEAL_WAVE_DEALER,
	IDSEAL_WAVE_HEALER,
	IDSEAL_WAVE_LEADER,
	IDSEAL_WAVE_TANKER,
	IDSEAL_WAVE_TARGET,
	IDSTATION_BOOM,
	IDSTATION_GUIDE,
	IDSTATION_MONSTER,
	IDSTATION_POLYMORP_H,
	IDSWEEP_BONUS,
	IDSWEEP_MONSTER,
	IDSWEEP_MONSTER_2,
	IDSWEEP_MONSTER_3,
	IDSWEEP_MINE,
	IDSWEEP_NPC,
	IDSWEEP_POLYMORPH_D,
	IDSWEEP_POLYMORPH_L,
	IDSWEEP_POLYMORPHLD,
	IDTEMPLE_BUGS,
	IDTEMPLE_STONE,
	IDTIAMAT_AREAHIDE,
	IDTIAMAT_ANCIENT,
	IDTIAMAT_SPAWNHEAL,
	IDTIAMAT_XDRAKAN,
	IDTRANSFORM_DEVA,
	IDTRANSFORM_DRAGON_CINEMA,
	IDTRANSFORM_PC,
	IDTRANSFORM_MONSTER,
	IDTRANSFORM_SHADOW,
	IDTRANSFORM_TRANS_NPC,
	IDVRITRA_BASE_REBIRTH,
	IDVRITRA_BASE_GI_BEACON,
	IDVRITRA_BASE_GI_CANNON,
	IDYUN_ANCIENT,
	IDYUN_ANTIBOMBER,
	IDYUN_BOMBER,
	IDYUN_D1,
	IDYUN_DOOR,
	IDYUN_FIST,
	IDYUN_HDRAKAN,
	IDYUN_MEROPS,
	IDYUN_OBJECTS,
	IDYUN_ODRAKAN,
	IDYUN_POLYMORPH,
	IDYUN_RDRAKAN,
	IDYUN_SIEGEWEAPON,
	IDYUN_TARGET,
	IDYUN_VASARTI,
	IDYUN_XDRAKAN,
	KALNIF_AMINX,
	KALNIF_ATOG,
	KAHRUN,
	KERUBIM_AD1_LF1,
	KERUBIM_AFARMER_LF1,
	KRALL,
	KRALLMASTER,
	KRALLWIZARDCY,
	KRALL_PC,
	KRALL_TRAINING,
	LASBERG,
	LDF4_ADVANCE_AGGRESSIVESINGLE,
	LDF4_ADVANCE_CHAPIRE_SINGLE,
	LDF4_ADVANCE_CHAPIRE_SUPPORT,
	LDF4_ADVANCE_DACHAPIRE,
	LDF4_ADVANCE_DGUARD,
	LDF4_ADVANCE_DRGUARD,
	LDF4_ADVANCE_LGUARD,
	LDF4_ADVANCE_FNAMED_FOBJ,
	LDF4_ADVANCE_GODELITEDM,
	LDF4_ADVANCE_GODELITELF,
	LDF4_ADVANCE_LIPOPOKU,
	LDF4_ADVANCE_MANDURI,
	LDF4_ADVANCE_MUDTHORN,
	LDF4_ADVANCE_NEUTH,
	LDF4_ADVANCE_POPOKU,
	LDF4_ADVANCE_POTCRAB,
	LDF4_ADVANCE_SAAM,
	LDF4_ADVANCE_SINGLE,
	LDF4_ADVANCE_TIAMAT,
	LDF4_ADVANCE_WORKER,
	LDF4_ADVANCE_XIPRTO,
	LDF4A_CALYDON,
	LDF4A_LG_SKILL,
	LDF4A_LG_SKILL_RECEIVE,
	LDF4A_NEPILIM,
	LDF4A_NEPILIM_SUMMON,
	LDF4A_OWLLAU,
	LDF4A_POLY_SHULACK,
	LDF4A_PUBLIC_MONSTER,
	LDF4A_SANDWARM,
	LDF4A_YUN_GUARD,
	LDF4B_AGGRESSIVEYUNSOLDIER,
	LDF4B_ATTACKWAGON,
	LDF4B_FANATIC,
	LDF4B_MINE,
	LDF4B_SPARRING_DGUARD,
	LDF4B_SPARRING_DGUARD2,
	LDF4B_SPARRING_GUARD,
	LDF4B_SPARRING_GUARD2,
	LDF4B_SPARRING_Y,
	LDF4B_SPARRING_Y2,
	LDF4B_WAGON,
	LDF_V_CHIEF_D,
	LDF_V_CHIEF_L,
	LDF_V_GUARD_DARK,
	LDF_V_GUARD_LIGHT,
	LDF_V_KILLER_LEHPAR,
	LDF_V_KILLER_KRALL,
	LDF_V_KILLER_LYCAN,
	LDF5_BABARIAN,
	LDF5_BRAX,
	LDF5_CALYDON,
	LDF5_DARU,
	LDF5_DEBRIE,
	LDF5_DUMMY1,
	LDF5_DUMMY2,
	LDF5_DUMMY1_DGUARD(Race.ASMODIANS),
	LDF5_DUMMY1_LGUARD(Race.ELYOS),
	LDF5_DUMMY2_DGUARD(Race.ASMODIANS),
	LDF5_DUMMY2_LGUARD(Race.ELYOS),
	LDF5_FORTRESS_DARK(Race.ASMODIANS),
	LDF5_FORTRESS_LIGHT(Race.ELYOS),
	LDF5_FORTRESS_VRITRA(Race.DRAGON),
	LDF5_FORTRESS_TANK_DARK,
	LDF5_FORTRESS_TANK_LIGHT,
	LDF5_FUNGY,
	LDF5_GURURU,
	LDF5_LUPYLLINI,
	LDF5_MUTA,
	LDF5_NATIVE_DIRECT,
	LDF5_NEUT,
	LDF5_OWLLAU,
	LDF5_SHULACK_DIRECT,
	LDF5_SHULACK_KEEPER,
	LDF5_SPAKLE,
	LDF5_SPARRING1_DGUARD,
	LDF5_SPARRING1_LGUARD,
	LDF5_SPARRING2_DGUARD,
	LDF5_SPARRING2_LGUARD,
	LDF5_V_CHIEF_D,
	LDF5_V_CHIEF_DR,
	LDF5_V_CHIEF_L,
	LDF5_V_KILLER_D,
	LDF5_V_KILLER_DR,
	LDF5_V_KILLER_L,
	LDF5_VESPA,
	LDF5_WORKER,
	LDF5B_DOOR_DA,
	LDF5B_DOOR_DR,
	LDF5B_DOOR_LI,
	LDF5B_FOBJ_HOSTILEPC,
	LDF5B_KILLER,
	LDF5B_OUT_DOOR_KILLER_DA,
	LDF5B_OUT_DOOR_KILLER_LI,
	LEHPAR,
	LEHPAR_AGUARDDARK,
	LEHPAR_APRETOR,
	LF5_DUMMY1,
	LF5_DUMMY2,
	LF5_DUMMY1_LGUARD(Race.ELYOS),
	LF5_DUMMY2_LGUARD(Race.ELYOS),
	LF5_GUARD_01_LIGHT(Race.ELYOS),
	LF5_GUARD_02_LIGHT(Race.ELYOS),
	LF5_GUARD_03_LIGHT(Race.ELYOS),
	LF5_GUARD_04_LIGHT(Race.ELYOS),
	LF5_GUARD_TRAP,
	LF5_ITEM,
	LF5_ITEM_SUM,
	LICH_SOULEDSTONE,
	LIGHT_DENLABIS,
	LIGHT_LICH,
	LIGHT_LICH_DF2ADIRECTPORTAL,
	LIGHT_MOB,
	LIGHT_NPC,
	LIGHT_SUR_MOB,
	LIGHT_SUR_MOB_DF2ADIRECTPORTAL,
	LIZARDMAN,
	LIZARDMAN_BOMB,
	LIZARDMAN_KB,
	LUPYLLINI,
	LYCAN,
	LYCANDF2MASTER,
	LYCANDF2SLAVE1,
	LYCANDF2SLAVE2,
	LYCANMASTER,
	LYCAN_AGUARD_DARK_DF1,
	LYCAN_HUNTER,
	LYCAN_MAGE,
	LYCAN_PC,
	LYCAN_PET,
	LYCAN_PET_TRAINING,
	LYCAN_SUM,
	LYCAN_TRAINING,
	L_DRGUARD_ADRAGGMOB1,
	MAIDENGOLEM_ELITE,
	MANDURITWEAK,
	MERDION,
	MINX,
	MINX_HKALNIF,
	MINX_HZAIF,
	MONSTER(true),
	MONSTER_FRIENDLY_LDFCHIEF,
	MONSTER_LDF4A_PUBLIC_LIZARDMAN,
	MOSBEARBABY,
	MOSBEARFATHER,
	MUTA,
	MUTA_HOCTASIDE,
	NEPILIM_EARTH,
	NEPILIM_EVENT,
	NEPILIM_NAMED,
	NEPILIM_NORMAL,
	NEPILIM_SUMMON,
	NEPILIM_SUN,
	NEPILIM_SUN_BATTLE,
	NEUT,
	NEUTBUG,
	NEUTQUEEN,
	NEUTRAL_GUARD,
	NEUTRAL_GUARD_ON_ATTACK,
	NEUTRAL_GUARD_ON_ATTACK01,
	NEUTRAL_DGUARD,
	NEUTRAL_LGUARD,
	NLIZARDMAN,
	NLIZARDMAN2,
	NLIZARDPET,
	NLIZARDPRIEST,
	NLIZARDRAISER,
	NNAGA,
	NNAGA_BOSS_SERVANT,
	NNAGA_ELEMENTAL,
	NNAGA_ELEMENTALIST,
	NNAGA_PRIEST,
	NNAGA_PRIESTBOSS,
	NNAGA_SERVANT,
	NOFIGHT,
	NONAGRRESSIVEFRIENDLYVRITRA,
	NONE(true),
	NPC(true),
	OCTASIDEBABY,
	OCTASIDE_AMUTA,
	ORC,
	PARENTSMONSTER,
	PC(true),
	PC_DARK(true),
	PC_DRAGON(true),
	PET,
	PET_DARK,
	POLYMORPHFUNGY,
	POLYMORPHPARROT,
	PREDATOR,
	PRETOR_ALEHPAR,
	PREY,
	PROTECTGUARD_DARK(Race.ASMODIANS),
	PROTECTGUARD_DARK_F2A(Race.ASMODIANS),
	PROTECTGUARD_DARK_HERO(Race.ASMODIANS),
	PROTECTGUARD_DARK_SIEGEWEAPON(Race.ASMODIANS),
	PROTECTGUARD_LIGHT(Race.ELYOS),
	PROTECTGUARD_LIGHT_F2A(Race.ELYOS),
	PROTECTGUARD_LIGHT_HERO(Race.ELYOS),
	PROTECTGUARD_LIGHT_SIEGEWEAPON(Race.ELYOS),
	QUESTGUARD_DARK(Race.ASMODIANS),
	QUESTGUARD_LIGHT(Race.ELYOS),
	RANMARK,
	RATMAN,
	RATMANDFWORKER,
	RATMANWORKER,
	RATMAN_AGUARD_DARK_DF1,
	ROBBERALDER_ASPRIGG_DF1,
	RVR_DGUARD_01(Race.ASMODIANS),
	RVR_DGUARD_02(Race.ASMODIANS),
	RVR_DGUARD_03(Race.ASMODIANS),
	RVR_DGUARD_04(Race.ASMODIANS),
	RVR_DGUARD_05(Race.ASMODIANS),
	RVR_DGUARD_06(Race.ASMODIANS),
	RVR_DGUARD_07(Race.ASMODIANS),
	RVR_DGUARD_08(Race.ASMODIANS),
	RVR_DGUARD_09(Race.ASMODIANS),
	RVR_LGUARD_01(Race.ELYOS),
	RVR_LGUARD_02(Race.ELYOS),
	RVR_LGUARD_03(Race.ELYOS),
	RVR_LGUARD_04(Race.ELYOS),
	RVR_LGUARD_05(Race.ELYOS),
	RVR_LGUARD_06(Race.ELYOS),
	RVR_LGUARD_07(Race.ELYOS),
	RVR_LGUARD_08(Race.ELYOS),
	RVR_LGUARD_09(Race.ELYOS),
	RVR_LIZARD_GUARD_01(Race.DRAGON),
	RVR_LIZARD_GUARD_02(Race.DRAGON),
	RVR_LIZARD_GUARD_03(Race.DRAGON),
	RVR_LIZARD_GUARD_04(Race.DRAGON),
	RVR_LIZARD_GUARD_05(Race.DRAGON),
	RVR_LIZARD_GUARD_06(Race.DRAGON),
	RVR_LIZARD_GUARD_07(Race.DRAGON),
	RVR_LIZARD_GUARD_08(Race.DRAGON),
	RVR_LIZARD_GUARD_09(Race.DRAGON),
	SAMM,
	SAM_ELITE,
	SEIREN,
	SEIREN_MASTER,
	SEIREN_SNAKE,
	SHELLIZARDBABY,
	SHELLIZARDMOM,
	SHUGOBOB_BTGROUND_D,
	SHUGOBOB_BTGROUND_L,
	SHUGOBOB_D,
	SHUGOBOB_HI_D,
	SHUGOBOB_HI_L,
	SHUGOBOB_L,
	SHUGOBOB_MONSTER,
	SHULACK,
	SHULACK_ATTACKED,
	SHULACK_ATTACKING,
	SHULACK_DECK,
	SHULACK_DECK_KILLER,
	SHULACK_SLAVE,
	SHULACK_SLAVE_NOTAGGRESSIVE,
	SHULACK_SUPPORT,
	SIEGE_WEAPON_PC_DARK,
	SIEGE_WEAPON_PC,
	SOULEDSTONE,
	SOULEDSTONE_MINI,
	SPAKY,
	SPALLER,
	SPALLERCTRL,
	SPECTRE_AALIGHTDARK,
	SPRIGGREFUSE_DF1,
	SPRIGG_HROBBERALDER_DF1,
	SUCCUBUS_ELITE,
	SWELLFISH,
	TARGETBASFELT2_DF1,
	TARGETBASFELT_DF1,
	TAURIC,
	TDOWN_DRAKAN,
	TELEPORTOR_DA,
	TELEPORTOR_LI,
	TESTBATTLE_NPC,
	TEST_ATTACKTOPC,
	TEST_ATTACKTOPC_DARK,
	TEST_ATTACKTONPC,
	TEST_DARK_ADRAGON,
	TEST_DARK_AETC,
	TEST_DARK_ALIGHT,
	TEST_DRAGON_ADARK,
	TEST_DRAGON_AETC,
	TEST_DRAGON_ALIGHT,
	TEST_DRAKAN,
	TEST_ETC_ADARK,
	TEST_ETC_ADRAGON,
	TEST_ETC_ALIGHT,
	TEST_LIGHT_ADARK,
	TEST_LIGHT_ADRAGON,
	TEST_LIGHT_AETC,
	TEST_SUPPORTNPC,
	TIAMAT,
	TIAMATREMNANT_DRAKAN,
	TIAMATREMNANT_LIZARD,
	TIAMATREMNANT_LIZARD_INJURY,
	TIGRAN,
	TOG,
	TOG_AKALNIF,
	TOG_AZAIF,
	TOWERMAN,
	TRICO,
	TRICON,
	UNDEADGRADIATOR_DF1,
	UNDER_01_WAR_VRITRA,
	USEALL(true),
	USEALL_HOSTILEPC,
	USEALL_LDF5_TOWER_DA,
	USEALL_LDF5_TOWER_LI,
	USEALL_TELEPORTER_DA,
	USEALL_TELEPORTER_LI,
	USEALLNONETOMONSTER,
	VRITRA,
	VRITRASUPPORT,
	VRITRATANK,
	WAVE_SWARM1,
	WAVE_SWARM2,
	WAVE_TREE,
	WEAPON_FRIEND,
	WORLDRAID_EVENT,
	WORLDRAID_EVENT_AGGRESSIVE,
	WORLDRAID_MONSTER,
	WORLDRAID_MONSTER_SANDWORMSUM,
	WORLDRAID_NPC,
	XDRAKAN,
	XDRAKAN_ANU,
	XDRAKAN_DGUARD,
	XDRAKAN_ELEMENTALIST,
	XDRAKAN_LGUARD,
	XDRAKAN_PET,
	XDRAKAN_PRIEST,
	XDRAKAN_SERVANT,
	XDRAKAN_UNATTACK,
	XIPETO,
	XIPETOBABY,
	YDUMMY,
	YDUMMY2,
	YDUMMY2_DGUARD,
	YDUMMY2_GUARD,
	YDUMMY2_LGUARD,
	YDUMMY_DGUARD,
	YDUMMY_GUARD,
	YDUMMY_LGUARD,
	YUN_GUARD,
	ZAIF,
	ZAIF_ABROWNIEFELLER_LF1,
	ZAIF_ADARU,
	ZAIF_AMINX,
	ZAIF_ATOG;
	
	private Race guardRace;
	private boolean isBasic;
	
	private TribeClass()
	{
	}
	
	private TribeClass(Race guardRace)
	{
		this.guardRace = guardRace;
	}
	
	private TribeClass(Race guardRace, boolean isBasic)
	{
		this.guardRace = guardRace;
		this.isBasic = isBasic;
	}
	
	private TribeClass(boolean isBasic)
	{
		this.isBasic = isBasic;
	}
	
	public boolean isGuard()
	{
		return guardRace != null;
	}
	
	public boolean isBasicClass()
	{
		return isBasic;
	}
	
	public boolean isLightGuard()
	{
		return guardRace == Race.ELYOS;
	}
	
	public boolean isDarkGuard()
	{
		return guardRace == Race.ASMODIANS;
	}
	
	public boolean isDrakanGuard()
	{
		return guardRace == Race.DRAGON;
	}
}