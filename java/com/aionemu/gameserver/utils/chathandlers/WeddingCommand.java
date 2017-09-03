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
package com.aionemu.gameserver.utils.chathandlers;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author synchro2
 */
public abstract class WeddingCommand extends ChatCommand
{
	
	public WeddingCommand(String alias)
	{
		super(alias);
	}
	
	@Override
	public boolean checkLevel(Player player)
	{
		return player.havePermission(getLevel());
	}
	
	@Override
	boolean process(Player player, String text)
	{
		if (!player.isMarried())
		{
			return false;
		}
		final String alias = getAlias();
		
		if (!checkLevel(player))
		{
			PacketSendUtility.sendMessage(player, "You not have permission for use this command.");
			return true;
		}
		
		boolean success = false;
		if (text.length() == alias.length())
		{
			success = run(player, EMPTY_PARAMS);
		}
		else
		{
			success = run(player, text.substring(alias.length() + 1).split(" "));
		}
		
		return success;
	}
}
