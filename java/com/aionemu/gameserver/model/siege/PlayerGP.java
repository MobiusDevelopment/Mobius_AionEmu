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
package com.aionemu.gameserver.model.siege;

import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author Ever
 */
public class PlayerGP implements Comparable<PlayerGP>
{
	private final Player player;
	private final Race race;
	private int gp;
	
	public PlayerGP(Player player)
	{
		this.player = player;
		race = player.getRace();
		gp = 0;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public int getGP()
	{
		return gp;
	}
	
	public void increaseGP(int gp)
	{
		this.gp += gp;
	}
	
	@Override
	public int compareTo(PlayerGP pl)
	{
		return gp - pl.gp;
	}
}