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
package system.handlers.ai.worlds.inggison;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.services.ZorshivDredgionService;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("dredgion_commander")
public class Dredgion_CommanderAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied()
	{
		final WorldPosition p = getPosition();
		if (p != null)
		{
			sendDredgionGuide();
		}
		announceKilledBattleship();
		ZorshivDredgionService.getInstance().stopZorshivDredgion(3);
	}
	
	private void announceKilledBattleship()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				final AionObject winner = getAggroList().getMostDamage();
				if (winner instanceof Creature)
				{
					final Creature kill = (Creature) winner;
					// "Player Name" of the "Race" has destroyed the Balaur Battleship Dredgion.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1390196, kill.getRace().getRaceDescriptionId(), kill.getName()));
				}
			}
		});
	}
	
	private void sendDredgionGuide()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				if (MathUtil.isIn3dRange(player, getOwner(), 15))
				{
					HTMLService.sendGuideHtml(player, "Dredgion_Guide");
				}
			}
		});
	}
}