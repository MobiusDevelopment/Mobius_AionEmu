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

import com.aionemu.gameserver.model.gameobjects.player.Player;

/**
 * @author synchro2
 */
public class Wedding
{
	private final Player player;
	private final Player partner;
	private final Player priest;
	private boolean accepted;
	
	public Wedding(Player player, Player partner, Player priest)
	{
		super();
		this.player = player;
		this.partner = partner;
		this.priest = priest;
	}
	
	public void setAccept()
	{
		accepted = true;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public Player getPartner()
	{
		return partner;
	}
	
	public Player getPriest()
	{
		return priest;
	}
	
	public boolean isAccepted()
	{
		return accepted;
	}
	
}