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
package system.handlers.admincommands;

import com.aionemu.gameserver.configs.main.GSConfig;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Shift
 */
public class LevelUp extends AdminCommand
{
	
	/**
	 * @param alias
	 */
	public LevelUp()
	{
		super("levelup");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		Player target = null;
		final VisibleObject creature = player.getTarget();
		
		if (player.getTarget() instanceof Player)
		{
			target = (Player) creature;
		}
		
		if (target == null)
		{
			PacketSendUtility.sendMessage(player, "You should select a target first!");
			return;
		}
		
		if (params.length < 1)
		{
			PacketSendUtility.sendMessage(player, "You should enter second params!");
			return;
		}
		
		int level;
		try
		{
			level = Integer.parseInt(params[0]);
		}
		catch (final NumberFormatException e)
		{
			PacketSendUtility.sendMessage(player, "You should enter valid second params!");
			return;
		}
		
		final Player playerT = target;
		
		if ((playerT.getCommonData().getLevel() + level) <= GSConfig.PLAYER_MAX_LEVEL)
		{
			final int newLevel = playerT.getCommonData().getLevel() + level;
			playerT.getCommonData().setLevel(newLevel);
		}
		else
		{
			PacketSendUtility.sendMessage(player, "The value of <level> will plus calculated to the current player level!");
		}
		
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "syntax //levelup <target> <level>");
		PacketSendUtility.sendMessage(player, "The value of <level> will plus calculated to the current player level!");
	}
	
}
