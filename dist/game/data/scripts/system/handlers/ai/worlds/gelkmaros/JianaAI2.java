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
package system.handlers.ai.worlds.gelkmaros;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("jiana")
public class JianaAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		if (player.getRace() == Race.ASMODIANS)
		{
			final QuestState qs = player.getQuestStateList().getQuestState(21060); // [League] Eliminate Padmarashka.
			if ((qs == null) || (qs.getStatus() != QuestStatus.START))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(21060));
				// You may have been given a [Shining Scroll] as a means of receiving special instructions from one of the Empyrean Lords.
				// If you have lost it, and are concerned that you will be unable to complete your special quest, I can give you another.
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
			}
			else
			{
				// Marchutan protect you, [%username]. I'm afraid there is nothing I can help you with.
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1352));
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		if (dialogId == 10000)
		{ // Yes please! I lost mine.
			switch (getNpcId())
			{
				case 799445: // Jiana.
					ItemService.addItem(player, 182207849, 1); // Shining Scroll.
					break;
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}