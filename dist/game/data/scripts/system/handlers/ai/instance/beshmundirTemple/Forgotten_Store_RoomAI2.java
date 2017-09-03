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
package system.handlers.ai.instance.beshmundirTemple;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.ActionItemNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("forgottenstoreroom")
public class Forgotten_Store_RoomAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		final QuestState qsneedelyos = player.getQuestStateList().getQuestState(30211); // [Group] The Rod And The Orb.
		final QuestState qsneedasmo = player.getQuestStateList().getQuestState(30311); // [Group] A Quartz Is A Quartz.
		if (player.getRace() == Race.ELYOS)
		{
			if ((qsneedelyos != null) && (qsneedelyos.getStatus() != QuestStatus.NONE))
			{
				super.handleUseItemStart(player);
			}
			else
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			}
		}
		else
		{
			if ((qsneedasmo != null) && (qsneedasmo.getStatus() != QuestStatus.NONE))
			{
				super.handleUseItemStart(player);
			}
			else
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			}
		}
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		AI2Actions.deleteOwner(this);
	}
}