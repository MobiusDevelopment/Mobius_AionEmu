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
package system.handlers.ai.instance.shugoEmperorVault;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
@AIName("opened_vault_door")
public class OpenedVaultDoorAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final int instanceId = getPosition().getInstanceId();
		if (dialogId == 104)
		{
			switch (getNpcId())
			{
				case 832924: // Opened Vault Door.
				{
					switch (player.getWorldId())
					{
						case 301400000: // The Shugo Emperor's Vault.
						{
							TeleportService2.teleportTo(player, 301400000, instanceId, 426.50177f, 694.3207f, 398.42203f, (byte) 44);
							break;
						}
						case 301590000: // Emperor Trillirunerk's Safe.
						{
							TeleportService2.teleportTo(player, 301590000, instanceId, 426.50177f, 694.3207f, 398.42203f, (byte) 44);
							break;
						}
					}
					break;
				}
			}
		}
		return true;
	}
}