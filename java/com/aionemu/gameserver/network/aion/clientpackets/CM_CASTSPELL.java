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
package com.aionemu.gameserver.network.aion.clientpackets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.AionClientPacket;
import com.aionemu.gameserver.network.aion.AionConnection.State;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.skillengine.model.SkillTemplate;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author alexa026
 * @author rhys2002
 */
public class CM_CASTSPELL extends AionClientPacket
{
	private final Logger log = LoggerFactory.getLogger(CM_CASTSPELL.class);
	private int spellid;
	private int targetType;
	private float x, y, z;
	@SuppressWarnings("unused")
	private int targetObjectId;
	private int hitTime;
	private int level;
	private int unk;
	
	public CM_CASTSPELL(int opcode, State state, State... restStates)
	{
		super(opcode, state, restStates);
	}
	
	@Override
	protected void readImpl()
	{
		spellid = readH();
		level = readC();
		targetType = readC();
		switch (targetType)
		{
			case 0:
			case 3:
			case 4:
			case 87:
			{
				targetObjectId = readD();
				break;
			}
			case 1:
			{
				x = readF();
				y = readF();
				z = readF();
				break;
			}
			case 2:
			{
				x = readF();
				y = readF();
				z = readF();
				readF();
				readF();
				readF();
				readF();
				readF();
				readF();
				readF();
				readF();
				break;
			}
			default:
			{
				break;
			}
		}
		hitTime = readH();
		unk = readD();
		log.debug("[CM_CASTSPELL] Unk value: " + unk);
	}
	
	@Override
	protected void runImpl()
	{
		final Player player = getConnection().getActivePlayer();
		final SkillTemplate template = DataManager.SKILL_DATA.getSkillTemplate(spellid);
		
		/*
		 * KorLightNing if iscasting 'ESC' Key Send cancel casting
		 */
		if ((spellid == 0) && player.isCasting())
		{
			player.getController().cancelCurrentSkill();
		}
		
		if ((template == null) || template.isPassive())
		{
			return;
		}
		
		if (player.isProtectionActive())
		{
			player.getController().stopProtectionActiveTask();
		}
		
		final long currentTime = System.currentTimeMillis();
		if (player.getNextSkillUse() > currentTime)
		{
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300021));
			return;
		}
		
		if (!player.getLifeStats().isAlreadyDead())
		{
			player.getController().useSkill(template, targetType, x, y, z, hitTime, level);
		}
	}
}