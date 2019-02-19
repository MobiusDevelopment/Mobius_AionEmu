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
package system.handlers.ai.event.exceedKey;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.ChatType;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_MESSAGE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Rinzler (Encom)
 */
@AIName("exceed_tool_buyer")
public class ExceedToolBuyerAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		// 손상된 돌파석을 가지고 계시다면 저에게 가져 오십시오.
		// 상당한 가격으로 매입하겠습니다.
		if (player.getInventory().getFirstItemByItemId(165030000) != null)
		{ // Amplification Tool.
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
		}
		else
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
			PacketSendUtility.broadcastPacket(player, new SM_MESSAGE(player, "You do not have <Amplification Tool> to exchange", ChatType.BRIGHT_YELLOW_CENTER), true);
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		if ((dialogId == 10000) && player.getInventory().decreaseByItemId(165030000, 1))
		{ // Amplification Tool.
			switch (getNpcId())
			{
				case 805718: // Leipos.
				case 805719: // Menanor.
				{
					// 소유하고 있는 손상된 돌파석이 있다면 저에게 가져오십시오.
					// 섭섭치 않은 가격에 매입하겠습니다.
					switch (Rnd.get(1, 2))
					{
						case 1:
						{
							ItemService.addItem(player, 166020000, 1); // Omega Enchantment Stone.
							break;
						}
						case 2:
						{
							ItemService.addItem(player, 166022002, 1); // [Event] Shining Empyrean Lord's Enchantment Stone.
							break;
						}
					}
					break;
				}
			}
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		return true;
	}
}