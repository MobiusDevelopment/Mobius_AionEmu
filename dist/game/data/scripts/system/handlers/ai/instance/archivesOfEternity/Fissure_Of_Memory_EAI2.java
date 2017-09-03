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
package system.handlers.ai.instance.archivesOfEternity;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.model.QuestState;
import com.aionemu.gameserver.questEngine.model.QuestStatus;
import com.aionemu.gameserver.services.instance.InstanceService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("Fissure_Of_Memory_E")
public class Fissure_Of_Memory_EAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		if (player.getLevel() >= 66)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getOwner().getObjectId(), 1011));
		}
		else
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			// Only players level 65 or over can enter.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Fortress_Entrance_In01);
		}
	}
	
	@Override
	public boolean onDialogSelect(final Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final PlayerGroup group = player.getPlayerGroup2();
		if (player.getPlayerGroup2() == null)
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 27));
			// Unavailable to use when you're alone.
			PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_Ab1_Fortress_Entrance_In02);
			return true;
		}
		if (dialogId == 10000)
		{
			final QuestState qs = player.getQuestStateList().getQuestState(16804); // Archives of Eternity Protector.
			if ((qs == null) || (qs.getStatus() != QuestStatus.COMPLETE))
			{
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_HOUSING_CANT_OWN_NOT_COMPLETE_QUEST(16804));
				return true;
			}
			else
			{
				WorldMapInstance cradleOfEternity = InstanceService.getRegisteredInstance(301550000, group.getTeamId());
				if (cradleOfEternity == null)
				{
					cradleOfEternity = InstanceService.getNextAvailableInstance(301550000);
					InstanceService.registerGroupWithInstance(cradleOfEternity, group);
				}
				TeleportService2.teleportTo(player, 301550000, cradleOfEternity.getInstanceId(), 1477.0000f, 774.0000f, 1035.0000f, (byte) 0, TeleportAnimation.BEAM_ANIMATION);
			}
		}
		return true;
	}
}