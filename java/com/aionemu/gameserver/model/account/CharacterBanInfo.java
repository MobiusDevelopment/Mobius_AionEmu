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
package com.aionemu.gameserver.model.account;

/**
 * @author nrg
 */
public class CharacterBanInfo
{
	
	private final int playerId;
	private final long start;
	private final long end;
	private final String reason;
	
	public CharacterBanInfo(int playerId, long start, long duration, String reason)
	{
		this.playerId = playerId;
		this.start = start;
		end = duration + start;
		this.reason = (reason.equals("") ? "You are suspected to have violated the server's rules" : reason);
	}
	
	/**
	 * @return the playerId
	 */
	public int getPlayerId()
	{
		return playerId;
	}
	
	/**
	 * @return the start
	 */
	public long getStart()
	{
		return start;
	}
	
	/**
	 * @return the end
	 */
	public long getEnd()
	{
		return end;
	}
	
	/**
	 * @return the reason
	 */
	public String getReason()
	{
		return reason;
	}
}
