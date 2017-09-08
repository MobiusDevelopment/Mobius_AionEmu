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
package com.aionemu.gameserver.services.siegeservice;

import java.util.List;
import java.util.Map;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.configs.main.SiegeConfig;
import com.aionemu.gameserver.dao.PlayerDAO;
import com.aionemu.gameserver.dao.SiegeDAO;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.landing.LandingPointsEnum;
import com.aionemu.gameserver.model.siege.ArtifactLocation;
import com.aionemu.gameserver.model.siege.FortressLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeLegionReward;
import com.aionemu.gameserver.model.templates.siegelocation.SiegeReward;
import com.aionemu.gameserver.model.templates.zone.ZoneType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AbyssLandingService;
import com.aionemu.gameserver.services.AbyssLandingSpecialService;
import com.aionemu.gameserver.services.LegionService;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.mail.AbyssSiegeLevel;
import com.aionemu.gameserver.services.mail.MailFormatter;
import com.aionemu.gameserver.services.mail.SiegeResult;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.google.common.collect.Lists;

/**
 * Object that controls siege of certain fortress. Siege object is not reusable. New siege = new instance.
 * <p/>
 * @author SoulKeeper
 */
public class FortressSiege extends Siege<FortressLocation>
{
	// private static final Logger log = LoggerFactory.getLogger("SIEGE_LOG");
	private final AbyssPointsListener addAPListener = new AbyssPointsListener(this);
	private final GloryPointsListener addGPListener = new GloryPointsListener(this);
	
	public FortressSiege(FortressLocation fortress)
	{
		super(fortress);
	}
	
	@Override
	public void onSiegeStart()
	{
		getSiegeLocation().setVulnerable(true);
		getSiegeLocation().setUnderShield(true);
		broadcastState(getSiegeLocation());
		getSiegeLocation().clearLocation();
		GlobalCallbackHelper.addCallback(addAPListener);
		GlobalCallbackHelper.addCallback(addGPListener);
		deSpawnNpcs(getSiegeLocationId());
		spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.SIEGE);
		initSiegeBoss();
		if (getSiegeLocation().getLocationId() == 10111)
		{
			switch (getSiegeLocation().getLocationId())
			{
				case 10111:
				{
					templeGateMsg();
					break;
				}
			}
		}
	}
	
	@Override
	public void onSiegeFinish()
	{
		GlobalCallbackHelper.removeCallback(addAPListener);
		GlobalCallbackHelper.removeCallback(addGPListener);
		// unregisterSiegeBossListeners(); // FIXME? init listeners - EnhancedObject cast
		SiegeService.getInstance().deSpawnNpcs(getSiegeLocationId());
		getSiegeLocation().setVulnerable(false);
		getSiegeLocation().setUnderShield(false);
		if (isBossKilled())
		{
			onCapture();
			applyBuff();
			broadcastUpdate(getSiegeLocation());
		}
		else
		{
			broadcastState(getSiegeLocation());
		}
		SiegeService.getInstance().spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.PEACE);
		if (SiegeRace.BALAUR != getSiegeLocation().getRace())
		{
			if (getSiegeLocation().getLegionId() > 0)
			{
				giveRewardsToLegion();
			}
			giveRewardsToPlayers(getSiegeCounter().getRaceCounter(getSiegeLocation().getRace()));
		}
		updateOutpostStatusByFortress(getSiegeLocation());
		DAOManager.getDAO(SiegeDAO.class).updateSiegeLocation(getSiegeLocation());
		getSiegeLocation().doOnAllPlayers(player ->
		{
			player.unsetInsideZoneType(ZoneType.SIEGE);
			if (isBossKilled() && (SiegeRace.getByRace(player.getRace()) == getSiegeLocation().getRace()))
			{
				QuestEngine.getInstance().onKill(new QuestEnv(getBoss(), player, 0, 0));
			}
		});
	}
	
	public void onCapture()
	{
		final SiegeRaceCounter winner = getSiegeCounter().getWinnerRaceCounter();
		final SiegeRace looser = getSiegeLocation().getRace();
		getSiegeLocation().setRace(winner.getSiegeRace());
		getArtifact().setRace(winner.getSiegeRace());
		if (SiegeRace.BALAUR == winner.getSiegeRace())
		{
			getSiegeLocation().setLegionId(0);
			getArtifact().setLegionId(0);
		}
		else
		{
			final Integer topLegionId = winner.getWinnerLegionId();
			getSiegeLocation().setLegionId(topLegionId != null ? topLegionId : 0);
			getArtifact().setLegionId(topLegionId != null ? topLegionId : 0);
		}
		// Abyss Landing 4.9.1
		if ((getSiegeLocation().getLocationId() == 1221) || (getSiegeLocation().getLocationId() == 1231) || (getSiegeLocation().getLocationId() == 1241))
		{
			final Player player = null;
			if (SiegeRace.BALAUR != getSiegeLocation().getRace())
			{
				switch (getSiegeLocation().getLocationId())
				{
					// Krotan Refuge.
					case 1221:
					{
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(16);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, true);
						}
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(4);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, true);
						}
						break;
					}
					// Kysis Fortress.
					case 1231:
					{
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(18);
							AbyssLandingService.getInstance().updateHarbingerLanding(40000, LandingPointsEnum.SIEGE, true);
						}
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(6);
							AbyssLandingService.getInstance().updateRedemptionLanding(40000, LandingPointsEnum.SIEGE, true);
						}
						break;
					}
					// Miren Fortress.
					case 1241:
					{
						if (getSiegeLocation().getRace() == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(17);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, true);
						}
						if (getSiegeLocation().getRace() == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().startLanding(5);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, true);
						}
						break;
					}
				}
				AbyssLandingService.getInstance().AnnounceToPoints(player, getSiegeLocation().getRace().getDescriptionId(), getSiegeLocation().getNameAsDescriptionId(), 0, LandingPointsEnum.SIEGE);
			}
			if ((SiegeRace.BALAUR == getSiegeLocation().getRace()) || (winner.getSiegeRace() != looser))
			{
				switch (getSiegeLocation().getLocationId())
				{
					// Krotan Refuge.
					case 1221:
					{
						if (looser == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(16);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, false);
						}
						if (looser == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(4);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, false);
						}
						break;
					}
					// Kysis Fortress.
					case 1231:
					{
						if (looser == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(18);
							AbyssLandingService.getInstance().updateHarbingerLanding(40000, LandingPointsEnum.SIEGE, false);
						}
						if (looser == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(6);
							AbyssLandingService.getInstance().updateRedemptionLanding(40000, LandingPointsEnum.SIEGE, false);
						}
						break;
					}
					// Miren Fortress.
					case 1241:
					{
						if (looser == SiegeRace.ASMODIANS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(17);
							AbyssLandingService.getInstance().updateHarbingerLanding(35000, LandingPointsEnum.SIEGE, false);
						}
						if (looser == SiegeRace.ELYOS)
						{
							AbyssLandingSpecialService.getInstance().stopLanding(5);
							AbyssLandingService.getInstance().updateRedemptionLanding(35000, LandingPointsEnum.SIEGE, false);
						}
						break;
					}
				}
			}
		}
	}
	
	public void applyBuff()
	{
		final SiegeRaceCounter winner = getSiegeCounter().getWinnerRaceCounter();
		getSiegeLocation().setRace(winner.getSiegeRace());
		getArtifact().setRace(winner.getSiegeRace());
		if (SiegeRace.BALAUR == winner.getSiegeRace())
		{
			getSiegeLocation().setLegionId(0);
			getArtifact().setLegionId(0);
		}
		else
		{
			final Integer topLegionId = winner.getWinnerLegionId();
			getSiegeLocation().setLegionId(topLegionId != null ? topLegionId : 0);
			getArtifact().setLegionId(topLegionId != null ? topLegionId : 0);
		}
		World.getInstance().doOnAllPlayers(player ->
		{
			// Buff for Both Race.
			if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffId()))
			{
				player.getEffectController().removeEffect(getSiegeLocation().getBuffId());
			}
			else
			{
				SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffId(), player, player, 0);
			}
			// Buff for Asmodians or Elyos.
			if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffIdA()))
			{
				player.getEffectController().removeEffect(getSiegeLocation().getBuffIdA());
			}
			if (player.getEffectController().hasAbnormalEffect(getSiegeLocation().getBuffIdE()))
			{
				player.getEffectController().removeEffect(getSiegeLocation().getBuffIdE());
			}
			if (player.getCommonData().getRace() == Race.ASMODIANS)
			{
				SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffIdA(), player, player, 0);
			}
			if (player.getCommonData().getRace() == Race.ELYOS)
			{
				SkillEngine.getInstance().applyEffectDirectly(getSiegeLocation().getBuffIdE(), player, player, 0);
			}
		});
	}
	
	@Override
	public boolean isEndless()
	{
		return false;
	}
	
	@Override
	public void addAbyssPoints(Player player, int abysPoints)
	{
		getSiegeCounter().addAbyssPoints(player, abysPoints);
	}
	
	@Override
	public void addGloryPoints(Player player, int gloryPoints)
	{
		getSiegeCounter().addGloryPoints(player, gloryPoints);
	}
	
	protected void giveRewardsToLegion()
	{
		if (isBossKilled())
		{
			return;
		}
		if (getSiegeLocation().getLegionId() == 0)
		{
			return;
		}
		final List<SiegeLegionReward> legionRewards = getSiegeLocation().getLegionReward();
		final SiegeResult resultLegion = isBossKilled() ? SiegeResult.OCCUPY : SiegeResult.DEFENDER;
		final int legionBGeneral = LegionService.getInstance().getLegionBGeneral(getSiegeLocation().getLegionId());
		if (legionBGeneral != 0)
		{
			final PlayerCommonData BGeneral = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(legionBGeneral);
			if (legionRewards != null)
			{
				for (SiegeLegionReward medalsType : legionRewards)
				{
					MailFormatter.sendAbyssRewardMail(getSiegeLocation(), BGeneral, AbyssSiegeLevel.VETERAN_SOLDIER, resultLegion, System.currentTimeMillis(), medalsType.getItemId(), medalsType.getCount() * SiegeConfig.SIEGE_MEDAL_RATE, 0);
				}
			}
		}
	}
	
	protected void giveRewardsToPlayers(SiegeRaceCounter winnerDamage)
	{
		final Map<Integer, Long> playerAbyssPoints = winnerDamage.getPlayerAbyssPoints();
		final List<Integer> topPlayersIds = Lists.newArrayList(playerAbyssPoints.keySet());
		final Map<Integer, String> playerNames = PlayerService.getPlayerNames(playerAbyssPoints.keySet());
		final SiegeResult resultPlayers = isBossKilled() ? SiegeResult.OCCUPY : SiegeResult.DEFENDER;
		int i = 0;
		final List<SiegeReward> playerRewards = getSiegeLocation().getReward();
		for (SiegeReward topGrade : playerRewards)
		{
			for (int rewardedPC = 0; (i < topPlayersIds.size()) && (rewardedPC < topGrade.getTop()); ++i)
			{
				final Integer playerId = topPlayersIds.get(i);
				final PlayerCommonData pcd = DAOManager.getDAO(PlayerDAO.class).loadPlayerCommonData(playerId);
				++rewardedPC;
				MailFormatter.sendAbyssRewardMail(getSiegeLocation(), pcd, AbyssSiegeLevel.VETERAN_SOLDIER, resultPlayers, System.currentTimeMillis(), topGrade.getItemId(), topGrade.getCount() * SiegeConfig.SIEGE_MEDAL_RATE, 0);
			}
		}
	}
	
	/**
	 * The Temple Gate Opened.
	 */
	public void templeGateMsg()
	{
		World.getInstance().doOnAllPlayers(player ->
		{
			// The Temple Gate will open in 5 minutes.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START01, 0);
			// The Temple Gate will open in 1 minute.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START02, 240000);
			// The Temple Gate will open in 30 seconds.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START03, 270000);
			// The Temple Gate will open in 10 seconds.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START04, 290000);
			// The Temple Gate has opened.
			PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_Gab1_START05, 300000);
		});
	}
	
	protected ArtifactLocation getArtifact()
	{
		return SiegeService.getInstance().getFortressArtifacts().get(getSiegeLocationId());
	}
	
	protected boolean hasArtifact()
	{
		return getArtifact() != null;
	}
}