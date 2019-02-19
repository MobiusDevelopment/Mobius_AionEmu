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
package system.handlers.ai.event;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.GeneralNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("nightmare_circus")
public class Otherworldly_PucasAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		switch (getNpcId())
		{
			case 831541:
			case 831542:
			case 831543:
			case 831544:
			case 831545:
			case 831546:
			case 831547:
			case 831548:
			{
				super.handleDialogStart(player);
				break;
			}
			default:
			{
				if (player.getLevel() >= 30)
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
				}
				else
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
					PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_CANT_INSTANCE_ENTER_LEVEL);
				}
				break;
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env) && (dialogId != 1011))
		{
			return true;
		}
		if (dialogId == 105)
		{
			switch (getNpcId())
			{
				case 831541:
				case 831542:
				case 831543:
				case 831544:
				case 831545:
				case 831546:
				case 831547:
				case 831548:
				{
					final AutoGroupType agt = AutoGroupType.getAutoGroup(player.getLevel(), getNpcId());
					if (agt != null)
					{
						PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(0x1A, agt.getInstanceMapId()));
					}
					break;
				}
			}
		}
		else if ((dialogId == 1011) && (questId != 0))
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}