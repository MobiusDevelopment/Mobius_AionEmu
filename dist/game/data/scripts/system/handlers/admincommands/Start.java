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
package system.handlers.admincommands;

import com.aionemu.gameserver.model.PlayerClass;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class Start extends AdminCommand
{
	
	public Start()
	{
		super("start");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (player.getRace() == Race.ELYOS)
		{
			if (player.getPlayerClass() == PlayerClass.ASSASSIN)
			{
				ItemService.addItem(player, 110300872, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113300847, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 114300883, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112300776, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111300825, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000802, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 125001715, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200663, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200954, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001080, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001081, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.CHANTER)
			{
				ItemService.addItem(player, 114500829, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110500842, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111500808, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113500818, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112500764, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001716, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101500481, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.CLERIC)
			{
				ItemService.addItem(player, 114500829, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110500842, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111500808, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113500818, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112500764, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001716, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101500481, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.GLADIATOR)
			{
				ItemService.addItem(player, 114600786, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111600800, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600791, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110600827, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112600775, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001717, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000966, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000893, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101300655, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900684, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900475, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101300464, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.RANGER)
			{
				ItemService.addItem(player, 110300872, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113300847, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 114300883, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112300776, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111300825, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000802, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 125001715, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200663, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200954, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001080, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001081, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.SORCERER)
			{
				ItemService.addItem(player, 114100857, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111100821, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113100829, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112100779, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110100917, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001714, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100600755, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100600512, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100500479, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.SPIRIT_MASTER)
			{
				ItemService.addItem(player, 114100857, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111100821, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113100829, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112100779, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110100917, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001714, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100600755, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100600512, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100500479, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.TEMPLAR)
			{
				ItemService.addItem(player, 114600786, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111600800, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600791, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110600827, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112600775, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001717, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000966, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000893, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900684, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900475, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
			}
		}
		else
		{
			if (player.getPlayerClass() == PlayerClass.ASSASSIN)
			{
				ItemService.addItem(player, 114300884, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111300826, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113300848, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110300873, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112300777, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000802, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 125001715, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200663, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200954, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001080, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001081, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.CHANTER)
			{
				ItemService.addItem(player, 114500830, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110500843, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111500809, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113500819, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112500765, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001716, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101500481, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.CLERIC)
			{
				ItemService.addItem(player, 114500830, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110500843, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111500809, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113500819, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112500765, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001716, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101500481, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.GLADIATOR)
			{
				ItemService.addItem(player, 114600787, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111600801, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600792, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600792, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112600776, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001717, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000966, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000893, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101300655, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900684, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900475, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101300464, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.RANGER)
			{
				ItemService.addItem(player, 114300884, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111300826, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113300848, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110300873, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112300777, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000802, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 125001715, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200663, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200807, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200954, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001080, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100001081, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700722, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 101700494, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100200589, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.SORCERER)
			{
				ItemService.addItem(player, 114100858, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111100822, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113100830, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112100780, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110100918, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001714, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100600755, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100600512, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100500479, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.SPIRIT_MASTER)
			{
				ItemService.addItem(player, 114100858, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111100822, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113100830, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112100780, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 110100918, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001714, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000852, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 120000803, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000732, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000845, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100500698, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100600755, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 100600512, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100500479, 1); // player, [Item-id], [Count]
			}
			else if (player.getPlayerClass() == PlayerClass.TEMPLAR)
			{
				ItemService.addItem(player, 114600787, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 111600801, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600792, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 113600792, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 112600776, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 125001717, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 123000844, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 122000851, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 121000731, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 115000967, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 115000966, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000893, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900684, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100676, 1); // player, [Item-id], [Count]
				
				ItemService.addItem(player, 115000765, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100000618, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100900475, 1); // player, [Item-id], [Count]
				ItemService.addItem(player, 100100482, 1); // player, [Item-id], [Count]
			}
		}
		PacketSendUtility.sendMessage(player, "Fin !");
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax: //start ");
	}
}