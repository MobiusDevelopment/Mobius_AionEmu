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

import java.util.Map;

import com.aionemu.commons.callbacks.util.GlobalCallbackHelper;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.OutpostLocation;
import com.aionemu.gameserver.model.siege.SiegeModType;
import com.aionemu.gameserver.model.siege.SiegeRace;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.SiegeService;
import com.aionemu.gameserver.services.player.PlayerService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;

/**
 * @author SoulKeeper
 */
public class OutpostSiege extends Siege<OutpostLocation>
{
	private final AbyssPointsListener addAPListener = new AbyssPointsListener(this);
	private final GloryPointsListener addGPListener = new GloryPointsListener(this);
	
	public OutpostSiege(OutpostLocation siegeLocation)
	{
		super(siegeLocation);
	}
	
	@Override
	protected void onSiegeStart()
	{
		getSiegeLocation().setVulnerable(true);
		GlobalCallbackHelper.addCallback(addAPListener);
		GlobalCallbackHelper.addCallback(addGPListener);
		SiegeService.getInstance().deSpawnNpcs(getSiegeLocationId());
		SiegeService.getInstance().spawnNpcs(getSiegeLocationId(), getSiegeLocation().getRace(), SiegeModType.SIEGE);
		initSiegeBoss();
		World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(getSiegeLocationId() == 2111 ? 1400317 : 1400318)));
		broadcastUpdate(getSiegeLocation());
	}
	
	@Override
	protected void onSiegeFinish()
	{
		// unregisterSiegeBossListeners(); // FIXME? init listeners - EnhancedObject cast
		getSiegeLocation().setVulnerable(false);
		GlobalCallbackHelper.removeCallback(addAPListener);
		GlobalCallbackHelper.removeCallback(addGPListener);
		if (isBossKilled())
		{
			onCapture();
		}
		else
		{
			World.getInstance().doOnAllPlayers(player -> PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(getSiegeLocationId() == 2111 ? 1400319 : 1400320)));
		}
		broadcastUpdate(getSiegeLocation());
	}
	
	private void onCapture()
	{
		final SiegeRaceCounter winnerCounter = getSiegeCounter().getWinnerRaceCounter();
		final Map<Integer, Long> topPlayerDamages = winnerCounter.getPlayerDamageCounter();
		if (!topPlayerDamages.isEmpty())
		{
			final Integer topPlayer = topPlayerDamages.keySet().iterator().next();
			final String topPlayerName = PlayerService.getPlayerName(topPlayer);
			final int messageId = getSiegeLocationId() == 2111 ? 1400324 : 1400323;
			final Race race = winnerCounter.getSiegeRace() == SiegeRace.ELYOS ? Race.ELYOS : Race.ASMODIANS;
			final AionServerPacket asp = new SM_SYSTEM_MESSAGE(messageId, race, topPlayerName);
			World.getInstance().doOnAllPlayers(player ->
			{
				PacketSendUtility.sendPacket(player, asp);
				if (player.getRace().equals(race))
				{
					SkillEngine.getInstance().applyEffectDirectly(race == Race.ELYOS ? 12120 : 12119, player, player, 0);
				}
			});
		}
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
}