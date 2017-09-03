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
package com.aionemu.gameserver.model.instance;

public enum InstanceType
{
	LF1,
	SCORE,
	ARENA,
	NORMAL,
	DREADGION,
	ARENA_PVP,
	ARENA_TEAM,
	BATTLEFIELD;
	
	public boolean isDarkPoetaInstance()
	{
		return equals(InstanceType.LF1);
	}
	
	public boolean isScoreInstance()
	{
		return equals(InstanceType.SCORE);
	}
	
	public boolean isArenaInstance()
	{
		return equals(InstanceType.ARENA);
	}
	
	public boolean isNormalInstance()
	{
		return equals(InstanceType.NORMAL);
	}
	
	public boolean isDreadgionInstance()
	{
		return equals(InstanceType.DREADGION);
	}
	
	public boolean isArenaPvPInstance()
	{
		return equals(InstanceType.ARENA_PVP);
	}
	
	public boolean isArenaTeamInstance()
	{
		return equals(InstanceType.ARENA_TEAM);
	}
	
	public boolean isBattlefieldInstance()
	{
		return equals(InstanceType.BATTLEFIELD);
	}
}