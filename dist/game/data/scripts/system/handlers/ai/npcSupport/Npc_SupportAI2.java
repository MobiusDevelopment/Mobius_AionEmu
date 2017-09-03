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
package system.handlers.ai.npcSupport;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.GeneralNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("npc_support")
public class Npc_SupportAI2 extends GeneralNpcAI2
{
	@Override
	protected void handleDialogStart(Player player)
	{
		switch (getNpcId())
		{
			// Elyos.
			case 831024: // Ryoenniya.
			case 831025: // Luella.
			case 831030: // Netalion.
			case 831031: // Nebrith.
				// Asmodians.
			case 831026: // Rikanellie.
			case 831027: // Karzanke.
			case 831028: // Erdat.
			case 831029: // Edandos.
			{
				super.handleDialogStart(player);
				break;
			}
			default:
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
				break;
			}
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		final QuestEnv env = new QuestEnv(getOwner(), player, questId, dialogId);
		env.setExtendedRewardIndex(extendedRewardIndex);
		if (QuestEngine.getInstance().onDialog(env))
		{
			return true;
		}
		if (dialogId == 10000)
		{
			// int chance = Rnd.get(1, 2);
			// 1684 : Blessing of Guardianship 4.8 (Cleric Buff - Blessing of Health)
			// 20950 : Blessing of Growth : 4.0
			// SkillEngine.getInstance().getSkill(getOwner(), 20950, 1, player).useWithoutPropSkill();
			SkillEngine.getInstance().getSkill(getOwner(), 1684, 1, player).useWithoutPropSkill();
			player.getLifeStats().setCurrentHpPercent(100);
			player.getLifeStats().setCurrentMpPercent(100);
			player.getLifeStats().updateCurrentStats();
		}
		else if ((dialogId == 26) && (questId != 80487))
		{
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), dialogId, questId));
		}
		return true;
	}
}
