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
package system.handlers.ai.siege;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Summon;
import com.aionemu.gameserver.model.gameobjects.Trap;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RequestResponseHandler;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.knownlist.Visitor;

/**
 * @author Rinzler (Encom)
 */
@AIName("siege_gate_repair")
public class GateGuardianStoneAI2 extends NpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		final RequestResponseHandler gaterepair = new RequestResponseHandler(player)
		{
			@Override
			public void acceptRequest(Creature requester, Player responder)
			{
				final RequestResponseHandler repairstone = new RequestResponseHandler(player)
				{
					@Override
					public void acceptRequest(Creature requester, Player responder)
					{
						onActivate(player);
					}
					
					@Override
					public void denyRequest(Creature requester, Player responder)
					{
					}
				};
				if (player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_DOOR_REPAIR_DO_YOU_ACCEPT_REPAIR, repairstone))
				{
					PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_DOOR_REPAIR_DO_YOU_ACCEPT_REPAIR, player.getObjectId(), 5, new DescriptionId((2 * 716568) + 1)));
				}
			}
			
			@Override
			public void denyRequest(Creature requester, Player responder)
			{
			}
		};
		if (player.getResponseRequester().putRequest(SM_QUESTION_WINDOW.STR_ASK_DOOR_REPAIR_POPUPDIALOG, gaterepair))
		{
			PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_ASK_DOOR_REPAIR_POPUPDIALOG, player.getObjectId(), 5));
		}
	}
	
	@Override
	protected void handleDied()
	{
		final Player destroyer;
		final Creature lastAttacker = (Creature) getOwner().getAggroList().getMostDamage();
		if (lastAttacker instanceof Player)
		{
			destroyer = (Player) lastAttacker;
		}
		else if (lastAttacker instanceof Trap)
		{
			destroyer = (Player) ((Trap) lastAttacker).getMaster();
		}
		else if (lastAttacker instanceof Summon)
		{
			destroyer = ((Summon) lastAttacker).getMaster();
		}
		else
		{
			destroyer = null;
		}
		if (destroyer != null)
		{
			getOwner().getKnownList().doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player object)
				{
					// "Player Name" of the "Race" destroyed the Gate Guardian Stone.
					PacketSendUtility.sendPacket(object, new SM_SYSTEM_MESSAGE(1301054, destroyer.getRace().getRaceDescriptionId(), destroyer.getName()));
				}
			});
		}
		super.handleDied();
	}
	
	@Override
	protected void handleDialogFinish(Player player)
	{
	}
	
	public void onActivate(Player player)
	{
	}
}