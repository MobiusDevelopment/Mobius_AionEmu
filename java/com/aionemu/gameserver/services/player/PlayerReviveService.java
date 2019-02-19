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
package com.aionemu.gameserver.services.player;

import com.aionemu.gameserver.configs.administration.AdminConfig;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.Kisk;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.state.CreatureState;
import com.aionemu.gameserver.model.team2.alliance.PlayerAllianceService;
import com.aionemu.gameserver.model.team2.common.legacy.GroupEvent;
import com.aionemu.gameserver.model.team2.common.legacy.PlayerAllianceEvent;
import com.aionemu.gameserver.model.team2.group.PlayerGroupService;
import com.aionemu.gameserver.model.templates.item.ItemUseLimits;
import com.aionemu.gameserver.model.vortex.VortexLocation;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ITEM_USAGE_ANIMATION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_TARGET_SELECTED;
import com.aionemu.gameserver.services.VortexService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.audit.AuditLogger;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldMap;
import com.aionemu.gameserver.world.WorldPosition;

/**
 * @author Jego, xTz
 */
public class PlayerReviveService
{
	public static void duelRevive(Player player)
	{
		revive(player, 30, 30, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		player.unsetResPosState();
	}
	
	public static void skillRevive(Player player)
	{
		revive(player, 10, 10, true, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		if (player.isInResPostState())
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void rebirthRevive(Player player)
	{
		if (!player.canUseRebirthRevive())
		{
			return;
		}
		if (player.getRebirthResurrectPercent() <= 0)
		{
			player.setRebirthResurrectPercent(5);
		}
		boolean soulSickness = true;
		int rebirthResurrectPercent = player.getRebirthResurrectPercent();
		if (player.getAccessLevel() >= AdminConfig.ADMIN_AUTO_RES)
		{
			rebirthResurrectPercent = 100;
			soulSickness = false;
		}
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		revive(player, rebirthResurrectPercent, rebirthResurrectPercent, soulSickness, player.getRebirthSkill());
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void bindRevive(Player player)
	{
		bindRevive(player, 0);
	}
	
	public static void bindRevive(Player player, int skillId)
	{
		revive(player, 25, 25, true, skillId);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		else
		{
			boolean isInvadeActiveVortex = false;
			for (VortexLocation loc : VortexService.getInstance().getVortexLocations().values())
			{
				isInvadeActiveVortex = loc.isInsideActiveVortex(player) && player.getRace().equals(loc.getInvadersRace());
				if (isInvadeActiveVortex)
				{
					TeleportService2.teleportTo(player, loc.getResurrectionPoint());
				}
			}
			if (!isInvadeActiveVortex)
			{
				if (player.isGM() && player.isInvul()) // happens when you fall into the void
				{
					TeleportService2.moveToBindLocation(player, false); // fix admin characters not been able to resurrect
				}
				else
				{
					TeleportService2.moveToBindLocation(player, true);
				}
			}
		}
		player.unsetResPosState();
	}
	
	public static void kiskRevive(Player player)
	{
		kiskRevive(player, 0);
	}
	
	public static void kiskRevive(Player player, int skillId)
	{
		final Kisk kisk = player.getKisk();
		if (kisk == null)
		{
			bindRevive(player);
			return;
		}
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		else if (kisk.isActive())
		{
			final WorldPosition bind = kisk.getPosition();
			kisk.resurrectionUsed();
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
			revive(player, 25, 25, false, skillId);
			player.getController().startProtectionActiveTask();
			player.setPortAnimation(4);
			if (player.getIsFlyingBeforeDeath())
			{
				player.getFlyController().startFly();
			}
			player.getGameStats().updateStatsAndSpeedVisually();
			player.unsetResPosState();
			TeleportService2.moveToKiskLocation(player, bind);
		}
	}
	
	public static void instanceRevive(Player player)
	{
		instanceRevive(player, 0);
	}
	
	public static void instanceRevive(Player player, int skillId)
	{
		if (player.getPosition().getWorldMapInstance().getInstanceHandler().onReviveEvent(player))
		{
			return;
		}
		final WorldMap map = World.getInstance().getWorldMap(player.getWorldId());
		if (map == null)
		{
			bindRevive(player);
			return;
		}
		revive(player, 25, 25, true, skillId);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (map.isInstanceType() && ((player.getInstanceStartPosX() != 0) && (player.getInstanceStartPosY() != 0) && (player.getInstanceStartPosZ() != 0)))
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceStartPosX(), player.getInstanceStartPosY(), player.getInstanceStartPosZ());
		}
		else
		{
			bindRevive(player);
		}
		player.unsetResPosState();
	}
	
	public static void revive(Player player, int hpPercent, int mpPercent, boolean setSoulsickness, int resurrectionSkill)
	{
		player.getKnownList().doOnAllPlayers(visitor ->
		{
			final VisibleObject target = visitor.getTarget();
			if ((target != null) && (target.getObjectId() == player.getObjectId()) && (visitor.getRace() != player.getRace()))
			{
				visitor.setTarget(null);
				PacketSendUtility.sendPacket(visitor, new SM_TARGET_SELECTED(null));
			}
		});
		final boolean isNoResurrectPenalty = player.getController().isNoResurrectPenaltyInEffect();
		player.getMoveController().stopFalling();
		player.setPlayerResActivate(false);
		player.getLifeStats().setCurrentHpPercent(isNoResurrectPenalty ? 100 : hpPercent);
		player.getLifeStats().setCurrentMpPercent(isNoResurrectPenalty ? 100 : mpPercent);
		if ((player.getCommonData().getDp() > 0) && !isNoResurrectPenalty)
		{
			player.getCommonData().setDp(0);
		}
		player.getLifeStats().triggerRestoreOnRevive();
		if (!isNoResurrectPenalty && setSoulsickness)
		{
			player.getController().updateSoulSickness(resurrectionSkill);
		}
		if (player.getResurrectionSkill() > 0)
		{
			player.setResurrectionSkill(0);
		}
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		player.getAggroList().clear();
		player.getController().onBeforeSpawn(false);
		if (player.isInGroup2())
		{
			PlayerGroupService.updateGroup(player, GroupEvent.MOVEMENT);
		}
		if (player.isInAlliance2())
		{
			PlayerAllianceService.updateAlliance(player, PlayerAllianceEvent.MOVEMENT);
		}
	}
	
	public static void itemSelfRevive(Player player)
	{
		final Item item = player.getSelfRezStone();
		if (item == null)
		{
			cancelRes(player);
			return;
		}
		final ItemUseLimits useLimits = item.getItemTemplate().getUseLimits();
		final int useDelay = useLimits.getDelayTime();
		player.addItemCoolDown(useLimits.getDelayId(), System.currentTimeMillis() + useDelay, useDelay / 1000);
		player.getController().cancelUseItem();
		PacketSendUtility.broadcastPacket(player, new SM_ITEM_USAGE_ANIMATION(player.getObjectId(), item.getObjectId(), item.getItemTemplate().getTemplateId()), true);
		if (!player.getInventory().decreaseByObjectId(item.getObjectId(), 1))
		{
			cancelRes(player);
			return;
		}
		revive(player, 15, 15, true, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void banditRevive(Player player)
	{
		revive(player, 100, 1000, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInResPostState())
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void ffaRevive(Player player)
	{
		revive(player, 100, 1000, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInResPostState())
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void bgRevive(Player player)
	{
		revive(player, 100, 1000, false, player.getResurrectionSkill());
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		if (player.isInResPostState())
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.setIsFlyingBeforeDeath(false);
	}
	
	public static void eventRevive(Player player)
	{
		revive(player, 100, 100, false, 0);
		player.getController().startProtectionActiveTask();
		player.setPortAnimation(4);
		if (player.getIsFlyingBeforeDeath())
		{
			player.setState(CreatureState.FLYING);
		}
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.RESURRECT), true);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		player.getGameStats().updateStatsAndSpeedVisually();
		if (player.getIsFlyingBeforeDeath())
		{
			player.getFlyController().startFly();
		}
		if (player.isInPrison())
		{
			TeleportService2.teleportToPrison(player);
		}
		if (player.isInResPostState())
		{
			TeleportService2.teleportTo(player, player.getWorldId(), player.getInstanceId(), player.getResPosX(), player.getResPosY(), player.getResPosZ());
		}
		player.unsetResPosState();
		player.getGameStats().updateStatsAndSpeedVisually();
		PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, true));
		PacketSendUtility.sendPacket(player, new SM_MOTION(player.getObjectId(), player.getMotions().getActiveMotions()));
		player.setIsFlyingBeforeDeath(false);
	}
	
	private static void cancelRes(Player player)
	{
		AuditLogger.info(player, "Possible selfres hack.");
		player.getController().sendDie();
	}
}