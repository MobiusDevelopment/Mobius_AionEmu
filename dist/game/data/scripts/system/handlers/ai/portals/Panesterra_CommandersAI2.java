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
package system.handlers.ai.portals;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.GeneralNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /** Helper themoose (Encom) /
 ****/

@AIName("panesterra_commanders")
public class Panesterra_CommandersAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		switch (getNpcId())
		{
			case 730940: // Advance Corridor For Commanders
			case 730941:
			{ // Advance Corridor For Commanders
				super.handleDialogStart(player);
				break;
			}
			default:
			{
				if (player.getLevel() >= 65)
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 10));
				}
				else
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
					// Only the commander-in-chief can enter.
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Telepoter_GAb1_User05);
				}
				break;
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		if (dialogId == 105)
		{
			switch (getNpcId())
			{
				case 730940: // Advance Corridor For Commanders
				case 730941: // Advance Corridor For Commanders
					final AutoGroupType agt = AutoGroupType.getAutoGroup(player.getLevel(), getNpcId());
					if (agt != null)
					{
						PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(0x1A, agt.getInstanceMapId()));
					}
					break;
			}
		}
		else if (dialogId == 10)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		}
		return true;
	}
}