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
package com.aionemu.gameserver.services.abyss;

import com.aionemu.gameserver.model.gameobjects.player.AbyssRank;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.utils.stats.AbyssRankEnum;

/**
 * @author ATracer
 */
public class AbyssSkillService
{
	
	/**
	 * @param player
	 */
	public static void updateSkills(Player player)
	{
		final AbyssRank abyssRank = player.getAbyssRank();
		if (abyssRank == null)
		{
			return;
		}
		final AbyssRankEnum rankEnum = abyssRank.getRank();
		// remove all abyss skills first
		for (final AbyssSkills abyssSkill : AbyssSkills.values())
		{
			if (abyssSkill.getRace() == player.getRace())
			{
				for (final int skillId : abyssSkill.getSkills())
				{
					player.getSkillList().removeSkill(skillId);
				}
			}
		}
		// add new skills
		if (abyssRank.getRank().getId() >= AbyssRankEnum.STAR5_OFFICER.getId())
		{
			for (final int skillId : AbyssSkills.getSkills(player.getRace(), rankEnum))
			{
				player.getSkillList().addAbyssSkill(player, skillId, 1);
			}
		}
	}
	
	/**
	 * @param player
	 */
	public static void onEnterWorld(Player player)
	{
		updateSkills(player);
	}
}