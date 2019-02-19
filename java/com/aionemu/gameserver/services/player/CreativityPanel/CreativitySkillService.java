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
package com.aionemu.gameserver.services.player.CreativityPanel;

import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.templates.panel_cp.PanelCp;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_ESSENCE_CONTROL;
import com.aionemu.gameserver.services.SkillLearnService;
import com.aionemu.gameserver.utils.PacketSendUtility;

/**
 * @author Ranastic (Encom)
 */
public class CreativitySkillService
{
	public void onSkillApply(Player player, int type, int size, int id, int point)
	{
		if ((id >= 15) && (id <= 372))
		{
			enchantSkill(player, id, point);
		}
		if (((id >= 409) && (id <= 456)) || ((id >= 373) && (id <= 400)))
		{
			enchantDaevaSkill(player, id, point);
		}
		PacketSendUtility.sendPacket(player, new SM_PLAYER_ESSENCE_CONTROL(type, size, id, point));
	}
	
	public void enchantSkill(Player player, int id, int point)
	{
		final PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point >= 1)
		{
			player.getSkillList().addSkill(player, pcp.getSkillId(), point + 1);
			player.getCP().addPoint(player, id, 5);
		}
		else if (point == 0)
		{
			player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
			player.getCP().removePoint(player, id);
		}
	}
	
	public void enchantDaevaSkill(Player player, int id, int point)
	{
		final PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if ((id >= 409) && (id <= 456))
		{
			if (point >= 1)
			{
				player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
				player.getCP().addPoint(player, id, point);
			}
			else if (point == 0)
			{
				SkillLearnService.removeSkill(player, pcp.getSkillId());
				player.getCP().removePoint(player, id);
			}
		}
		else if ((id >= 373) && (id <= 400))
		{
			if (point >= 1)
			{
				player.getSkillList().addSkill(player, pcp.getSkillId(), 6);
				player.getCP().addPoint(player, id, point);
			}
			else if (point == 0)
			{
				player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
				player.getCP().removePoint(player, id);
			}
		}
	}
	
	public void loginDaevaSkill(Player player, int id, int point)
	{
		final PanelCp pcp = DataManager.PANEL_CP_DATA.getPanelCpId(id);
		if (point >= 1)
		{
			player.getSkillList().addSkill(player, pcp.getSkillId(), point + 1);
			player.getCP().addPoint(player, id, point);
		}
		else if (point == 0)
		{
			player.getSkillList().addSkill(player, pcp.getSkillId(), 1);
			player.getCP().removePoint(player, id);
		}
	}
	
	public static CreativitySkillService getInstance()
	{
		return NewSingletonHolder.INSTANCE;
	}
	
	private static class NewSingletonHolder
	{
		static final CreativitySkillService INSTANCE = new CreativitySkillService();
	}
}