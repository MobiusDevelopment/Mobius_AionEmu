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
 * @author antness
 */
public class PlayerAP implements Comparable<PlayerAP>
{
	
	private final Player player;
	private final Race race;
	private int ap;
	
	public PlayerAP(Player player)
	{
		this.player = player;
		race = player.getRace();
		ap = 0;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Race getRace()
	{
		return race;
	}
	
	public int getAP()
	{
		return ap;
	}
	
	public void increaseAP(int ap)
	{
		this.ap += ap;
	}
	
	@Override
	public int compareTo(PlayerAP pl)
	{
		return ap - pl.ap;
	}
}
