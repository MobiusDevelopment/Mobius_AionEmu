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
package com.aionemu.gameserver.model.guide;

/**
 * @author xTz
 */
public class Guide
{
	
	private final int guide_id;
	private final int player_id;
	private final String title;
	
	public Guide(int guide_id, int player_id, String title)
	{
		this.guide_id = guide_id;
		this.player_id = player_id;
		this.title = title;
	}
	
	public int getGuideId()
	{
		return guide_id;
	}
	
	public int getPlayerId()
	{
		return player_id;
	}
	
	public String getTitle()
	{
		return title;
	}
}
