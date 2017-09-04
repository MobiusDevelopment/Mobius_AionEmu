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
package com.aionemu.gameserver.network.aion.serverpackets;

import java.util.List;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerCommonData;
import com.aionemu.gameserver.model.stats.container.PlayerLifeStats;
import com.aionemu.gameserver.model.team2.common.legacy.GroupEvent;
import com.aionemu.gameserver.model.team2.group.PlayerGroup;
import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.world.WorldPosition;

public class SM_GROUP_MEMBER_INFO extends AionServerPacket
{
	private final int groupId;
	private final Player player;
	private GroupEvent event;
	
	public SM_GROUP_MEMBER_INFO(PlayerGroup group, Player player, GroupEvent event)
	{
		groupId = group.getTeamId();
		this.player = player;
		this.event = event;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		final PlayerLifeStats pls = player.getLifeStats();
		final PlayerCommonData pcd = player.getCommonData();
		final WorldPosition wp = pcd.getPosition();
		if ((event == GroupEvent.ENTER) && !player.isOnline())
		{
			event = GroupEvent.ENTER_OFFLINE;
		}
		writeD(groupId);
		writeD(player.getObjectId());
		if (player.isOnline())
		{
			writeD(pls.getMaxHp());
			writeD(pls.getCurrentHp());
			writeD(pls.getMaxMp());
			writeD(pls.getCurrentMp());
			writeD(pls.getMaxFp());
			writeD(pls.getCurrentFp());
		}
		else
		{
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
			writeD(0);
		}
		writeD(0);
		writeD(wp.getMapId());
		writeD(wp.getMapId());
		writeF(wp.getX());
		writeF(wp.getY());
		writeF(wp.getZ());
		writeC(pcd.getPlayerClass().getClassId());
		writeC(pcd.getGender().getGenderId());
		writeC(pcd.getLevel());
		writeC(event.getId());
		writeH(player.isOnline() ? 1 : 0);
		writeC(player.isMentor() ? 0x01 : 0x00);
		writeC(0x00);// unk 5.1
		switch (event)
		{
			case MOVEMENT:
			case DISCONNECTED:
			{
				break;
			}
			case LEAVE:
			{
				writeH(0x00);
				writeC(0x00);
				break;
			}
			case ENTER_OFFLINE:
			case JOIN:
			{
				writeS(pcd.getName());
				break;
			}
			default:
			{
				writeS(pcd.getName());
				writeD(0x00);
				writeD(0x00);
				writeC(0x7F);
				final List<Effect> abnormalEffects = player.getEffectController().getAbnormalEffects();
				writeH(abnormalEffects.size());
				for (Effect effect : abnormalEffects)
				{
					writeD(effect.getEffectorId());
					writeH(effect.getSkillId());
					writeC(effect.getSkillLevel());
					writeC(effect.getTargetSlot());
					writeD(effect.getRemainingTime());
				}
				writeB(new byte[32]);
				break;
			}
			case UPDATE:
			{
				writeS(pcd.getName());
				writeD(0x00);
				writeD(0x00);
				writeC(0x7F);
				final List<Effect> abnormalEffects1 = player.getEffectController().getAbnormalEffects();
				writeH(abnormalEffects1.size());
				for (Effect effect : abnormalEffects1)
				{
					writeD(effect.getEffectorId());
					writeH(effect.getSkillId());
					writeC(effect.getSkillLevel());
					writeC(effect.getTargetSlot());
					writeD(effect.getRemainingTime());
				}
				writeB(new byte[32]);
				break;
			}
		}
	}
}