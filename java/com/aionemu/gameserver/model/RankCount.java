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

/**
 * @author zdead
 */
public class RankCount
{
	private final int playerId;
	private final int ap;
	private final int gp;
	private final Race race;
	
	public RankCount(int playerId, int ap, int gp, Race race)
	{
		this.playerId = playerId;
		this.ap = ap;
		this.gp = gp;
		this.race = race;
	}
	
	public int getPlayerId()
	{
		return playerId;
	}
	
	public int getPlayerAP()
	{
		return ap;
	}
	
	public int getPlayerGP()
	{
		return gp;
	}
	
	public Race getPlayerRace()
	{
		return race;
	}
}