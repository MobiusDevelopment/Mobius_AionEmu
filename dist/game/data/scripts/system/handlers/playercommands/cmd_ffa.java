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
package system.handlers.playercommands;

import com.aionemu.gameserver.configs.main.PvPModConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.events.FFAService;
import com.aionemu.gameserver.services.events.LadderService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/**
 * Created by wanke on 12/02/2017.
 */
public class cmd_ffa extends PlayerCommand
{
	public cmd_ffa()
	{
		super("ffa");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (!PvPModConfig.FFA_ENABLED)
		{
			PacketSendUtility.sendSys3Message(player, "\uE005", "The unique <FFA> mod is disabled!!!");
			return;
		}
		if (player.getLevel() < 10)
		{
			PacketSendUtility.sendSys3Message(player, "\uE005", "You must reached lvl 10 for use unique <FFA> mod!!!");
			return;
		}
		if (player.isInInstance() && !FFAService.getInstance().isInArena(player) && !player.isFFA())
		{
			PacketSendUtility.sendSys3Message(player, "\uE005", "You can't use unique <FFA> mod in instance!!!");
			return;
		}
		if ((player.getBattleground() != null) || LadderService.getInstance().isInQueue(player) || player.isSpectating() || player.getLifeStats().isAlreadyDead())
		{
			PacketSendUtility.sendSys3Message(player, "\uE005", "You cannot enter unique <FFA> while in a battleground, in the queue, while spectating or being dead !!!");
			return;
		}
		if (FFAService.getInstance().isInArena(player))
		{
			PacketSendUtility.sendSys3Message(player, "\uE005", "You will be leaving unique <FFA> in 10 seconds!");
			FFAService.getInstance().leaveArena(player);
		}
		else
		{
			if (player.getController().isInCombat())
			{
				PacketSendUtility.sendSys3Message(player, "\uE005", "You cannot enter unique <FFA> while in combat.");
				return;
			}
			PacketSendUtility.sendSys3Message(player, "\uE005", "You will be entering unique <FFA> mod in 10 seconds. To leave <FFA> mod, write .ffa!!!");
			FFAService.getInstance().enterArena(player, false);
		}
	}
}