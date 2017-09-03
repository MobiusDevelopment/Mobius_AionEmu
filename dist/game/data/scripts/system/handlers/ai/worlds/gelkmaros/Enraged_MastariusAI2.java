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

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.AionObject;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.knownlist.Visitor;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("enraged_mastarius")
public class Enraged_MastariusAI2 extends AggressiveNpcAI2
{
	private int mastariusPhase = 0;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			switch (getNpcId())
			{
				case 258220: // Enraged Mastarius.
					announceAgentUnderAttack();
					break;
			}
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if ((hpPercentage == 50) && (mastariusPhase < 1))
		{
			mastariusPhase = 1;
			announceJusinOdSpawn();
			announceEmpyreanLordAgentHP50();
		}
		if ((hpPercentage == 10) && (mastariusPhase < 2))
		{
			mastariusPhase = 2;
			announceEmpyreanLordAgentHP10();
		}
	}
	
	@Override
	protected void handleSpawned()
	{
		announceEnragedMastarius();
		super.handleSpawned();
	}
	
	@Override
	protected void handleDied()
	{
		applyVeilleEnergy();
		announceKilledMastarius();
		super.handleDied();
	}
	
	private void announceKilledMastarius()
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
					// "Player Name" of the "Race" has killed Marchutan's Agent Mastarius.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400323, kill.getRace().getRaceDescriptionId(), kill.getName()));
				}
			}
		});
	}
	
	private void announceAgentUnderAttack()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// Marchutan's Agent Mastarius is under attack!
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_FIELDABYSS_DARKBOSS_ATTACKED);
			}
		});
	}
	
	private void announceJusinOdSpawn()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// The Empyrean Lord Agent summoned the Aether Concentrator.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_OdSpawn, 0);
				// The Empyrean Lord Agent has enabled the Aether Concentrator.
				PacketSendUtility.playerSendPacketTime(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_OdStart, 20000);
			}
		});
	}
	
	private void announceEmpyreanLordAgentHP50()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// The Empyrean Lord Agent's HP has dropped below 50%
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_Hp50);
			}
		});
	}
	
	private void announceEmpyreanLordAgentHP10()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// The Empyrean Lord Agent's HP has dropped below 10%
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_LDF4_Jusin_Hp10);
			}
		});
	}
	
	private void announceEnragedMastarius()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				// Marchutan's Agent Mastarius has engaged in battle to defend Gelkmaros.
				PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_MSG_DF4_GODELITE_START_4);
			}
		});
	}
	
	public void applyVeilleEnergy()
	{
		World.getInstance().doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				if (player.getCommonData().getRace() == Race.ELYOS)
				{
					SkillEngine.getInstance().applyEffectDirectly(12119, player, player, 0); // Veille's Energy.
					SkillEngine.getInstance().applyEffectDirectly(20410, player, player, 0); // Victory Salute.
				}
			}
		});
	}
}