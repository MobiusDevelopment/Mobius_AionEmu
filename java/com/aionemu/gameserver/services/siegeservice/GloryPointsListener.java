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

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.siege.SiegeLocation;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;

/**
 * @author Ever
 */
public class GloryPointsListener extends AbyssPointsService.AddGPGlobalCallback
{
	private final Siege<?> siege;
	
	public GloryPointsListener(Siege<?> siege)
	{
		this.siege = siege;
	}
	
	@Override
	public void onGloryPointsAdded(Player player, int gloryPoints)
	{
		final SiegeLocation fortress = siege.getSiegeLocation();
		if (fortress.isInsideLocation(player))
		{
			siege.addGloryPoints(player, gloryPoints);
		}
	}
}