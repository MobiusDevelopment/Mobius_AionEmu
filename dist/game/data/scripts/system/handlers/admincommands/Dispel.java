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

import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * @author Hilgert
 */
public class Dispel extends AdminCommand
{
	
	public Dispel()
	{
		super("dispel");
	}
	
	@Override
	public void execute(Player admin, String... params)
	{
		Player target = null;
		final VisibleObject creature = admin.getTarget();
		
		if (creature == null)
		{
			PacketSendUtility.sendMessage(admin, "You should select a target first!");
			return;
		}
		
		if (creature instanceof Player)
		{
			target = (Player) creature;
			target.getEffectController().removeAllEffects();
			PacketSendUtility.sendMessage(admin, creature.getName() + " had all buff effects dispelled !");
		}
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		// TODO Auto-generated method stub
	}
}
