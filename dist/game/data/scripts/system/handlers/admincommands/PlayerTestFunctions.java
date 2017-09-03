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

import org.apache.commons.lang.math.NumberUtils;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.gameserver.dao.PlayerWardrobeDAO;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.dorinerk_wardrobe.PlayerWardrobeEntry;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_PLAYER_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_STATS_INFO;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

public class PlayerTestFunctions extends AdminCommand
{
	private static final String COMMAND_PROTECTOR = "pro";
	private static final String COMMAND_CONQUEROR = "con";
	private static final String COMMAND_GOLDEN_STAR = "star";
	private static final String COMMAND_UNK_1 = "unk1";
	private static final String CP = "cp";
	private static final String GA = "ga";
	private static final String XP = "xp";
	private static final String STATS = "stats";
	private static final String LUNA_RECIPE = "lrecipe";
	private static final String LUNA_BUY = "lbuy";
	private static final String LUNA_Consume = "lcon";
	
	public PlayerTestFunctions()
	{
		super("test");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		if (params.length == 0)
		{
			showHelp(player);
			return;
		}
		if (COMMAND_PROTECTOR.equalsIgnoreCase(params[0]) || COMMAND_CONQUEROR.equalsIgnoreCase(params[0]) || COMMAND_GOLDEN_STAR.equalsIgnoreCase(params[0]) || COMMAND_UNK_1.equalsIgnoreCase(params[0]))
		{
			handleUpdate(player, params);
			player.clearKnownlist();
			PacketSendUtility.sendPacket(player, new SM_PLAYER_INFO(player, false));
			player.updateKnownlist();
		}
		else if (CP.equalsIgnoreCase(params[0]) || GA.equalsIgnoreCase(params[0]) || XP.equalsIgnoreCase(params[0]) || STATS.equalsIgnoreCase(params[0]) || LUNA_RECIPE.equalsIgnoreCase(params[0]) || LUNA_BUY.equalsIgnoreCase(params[0]) || LUNA_Consume.equalsIgnoreCase(params[0]))
		{
			handleUpdate(player, params);
		}
	}
	
	protected void handleUpdate(Player player, String... params)
	{
		// if (params.length != 2 || !NumberUtils.isDigits(params[1])) {
		// showHelp(player);
		// return;
		// }
		final VisibleObject target = player.getTarget();
		if (target == null)
		{
			PacketSendUtility.sendMessage(player, "No target selected");
			return;
		}
		if (!(target instanceof Creature))
		{
			PacketSendUtility.sendMessage(player, "Target has to be Creature!");
			return;
		}
		final Creature creature = (Creature) target;
		final Player targetPlayer = (Player) creature;
		final int values1 = NumberUtils.toInt(params[1]);
		if (COMMAND_PROTECTOR.equalsIgnoreCase(params[0]))
		{
			player.getProtectorInfo().setRank(values1);
		}
		else if (COMMAND_CONQUEROR.equalsIgnoreCase(params[0]))
		{
			player.getConquerorInfo().setRank(values1);
		}
		else if (COMMAND_GOLDEN_STAR.equalsIgnoreCase(params[0]))
		{
			player.setGoldenStarOfLodi(values1);
		}
		else if (COMMAND_UNK_1.equalsIgnoreCase(params[0]))
		{
			player.setUnkPoint1(values1);
		}
		else if (GA.equalsIgnoreCase(params[0]))
		{
			if (values1 == 0)
			{
				targetPlayer.getCommonData().addGrowthEnergy(values1);
			}
			else
			{
				targetPlayer.getCommonData().addGrowthEnergy(values1);
			}
			PacketSendUtility.sendPacket(targetPlayer, new SM_STATS_INFO(player));
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Growth Aura:;0 1 0] " + ((targetPlayer.getCommonData().getGrowthEnergy() * 100) / 97000000)));
		}
		else if (XP.equalsIgnoreCase(params[0]))
		{
			final float a = DataManager.PLAYER_EXPERIENCE_TABLE.getStartExpForLevel(player.getLevel());
			final float current = values1 - a;
			final float total = player.getCommonData().getExpNeed();
			final float percent = ((current * 100) / total);
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1300564, "[color:Info:;0 1 0][color:XP Percent:;0 1 0] " + percent));
		}
		else if (STATS.equalsIgnoreCase(params[0]))
		{
			player.setCPSlot1(values1);
			PacketSendUtility.sendMessage(player, "Set slot" + values1);
			PacketSendUtility.sendPacket(player, new SM_STATS_INFO(player));
			/*
			 * for (PlayerCPEntry a : player.getCP().getAllCP()) { if (a.getSlot() == 1) { PacketSendUtility.sendMessage(player, "STATS 1 "+ a.getPoint()); } if (a.getSlot() == 2) { PacketSendUtility.sendMessage(player, "STATS 2 "+ a.getPoint()); } if (a.getSlot() == 3) {
			 * PacketSendUtility.sendMessage(player, "STATS 3 "+ a.getPoint()); } }
			 */
		}
		else if (LUNA_RECIPE.equalsIgnoreCase(params[0]))
		{
			// player.getWardrobe().removeItem(, 113301845);
			final int size = DAOManager.getDAO(PlayerWardrobeDAO.class).getItemSize(player.getObjectId());
			final int size2 = player.getWardrobe().size();
			for (int i = 0; i < size2; i++)
			{
				for (PlayerWardrobeEntry ce : player.getWardrobe().getAllWardrobe())
				{
					PacketSendUtility.sendMessage(player, "wardrobe size " + size + " id " + ce.getItemId() + " slot " + ce.getSlot());
				}
			}
			
		}
		else if (LUNA_BUY.equalsIgnoreCase(params[0]))
		{
			final int lunaPrice = 15;
			final long count = 570 / lunaPrice;
			PacketSendUtility.sendMessage(player, "count " + count);
		}
		else if (LUNA_Consume.equalsIgnoreCase(params[0]))
		{
			targetPlayer.getPlayerAccount().setLuna(values1);
			PacketSendUtility.sendMessage(player, "luna account " + targetPlayer.getPlayerAccount().getLuna());
			PacketSendUtility.sendMessage(player, "luna account " + targetPlayer.getPlayerAccount().getToll());
		}
	}
	
	protected void showHelp(Player player)
	{
		PacketSendUtility.sendMessage(player, "//test pro|con|star|unk1 <int values>");
	}
}