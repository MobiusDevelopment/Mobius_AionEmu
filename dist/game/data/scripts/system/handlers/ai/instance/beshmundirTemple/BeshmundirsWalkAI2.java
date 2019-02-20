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
import com.aionemu.gameserver.ai2.AI2Request;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.autogroup.AutoGroupType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.portal.PortalPath;
import com.aionemu.gameserver.model.templates.portal.PortalUse;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_FIND_GROUP;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.teleport.PortalService;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.ActionItemNpcAI2;

/**
 * @author Gigi
 * @author vlog
 */
@AIName("beshmundirswalk")
public class BeshmundirsWalkAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 10));
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final AI2Request request = new AI2Request()
		{
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				moveToInstance(responder);
			}
		};
		switch (dialogId)
		{
			case 105:
			{
				final AutoGroupType agt = AutoGroupType.getAutoGroup(player.getLevel(), getNpcId());
				if (agt != null)
				{
					PacketSendUtility.sendPacket(player, new SM_FIND_GROUP(0x1A, agt.getInstanceMapId()));
				}
				break;
			}
			case 65:
			{
				if (!player.isInGroup2())
				{
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1390256));
					return true;
				}
				if (player.getPlayerGroup2().isLeader(player))
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 4762));
				}
				else
				{
					if (!isAGroupMemberInInstance(player))
					{
						PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400361));
						return true;
					}
					moveToInstance(player);
				}
				break;
			}
			case 4763:
			{
				AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_INSTANCE_DUNGEON_WITH_DIFFICULTY_ENTER_CONFIRM, getObjectId(), request, new DescriptionId(1804103));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 4762));
				break;
			}
			case 4848:
			{
				AI2Actions.addRequest(this, player, SM_QUESTION_WINDOW.STR_INSTANCE_DUNGEON_WITH_DIFFICULTY_ENTER_CONFIRM, getObjectId(), request, new DescriptionId(1804105));
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 4762));
				break;
			}
		}
		return true;
	}
	
	private boolean isAGroupMemberInInstance(Player player)
	{
		if (player.isInGroup2())
		{
			for (Player member : player.getPlayerGroup2().getMembers())
			{
				if (member.getWorldId() == 300170000) // Beshmundir Temple.
				{
					return true;
				}
			}
		}
		return false;
	}
	
	void moveToInstance(Player player)
	{
		final PortalUse portalUse = DataManager.PORTAL2_DATA.getPortalUse(getNpcId());
		if (portalUse != null)
		{
			final PortalPath portalPath = portalUse.getPortalPath(player.getRace());
			if (portalPath != null)
			{
				PortalService.port(portalPath, player, getObjectId());
			}
		}
	}
}