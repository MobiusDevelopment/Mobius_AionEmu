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
package com.aionemu.gameserver.services.events;

import com.aionemu.commons.database.dao.DAOManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.dao.PlayerUpgradeArcadeDAO;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.PlayerUpgradeArcade;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_UPGRADE_ARCADE;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Ghostfur
 */
public class UpgradeArcadeService
{
	PlayerUpgradeArcadeDAO playerDAO = DAOManager.getDAO(PlayerUpgradeArcadeDAO.class);
	
	public void onEnterWorld(Player player)
	{
		if (playerDAO.getFrenzyMeterByObjId(player.getObjectId()) == 0)
		{
			DAOManager.getDAO(PlayerUpgradeArcadeDAO.class).addUpgradeArcade(player.getObjectId(), 1, 1);
		}
		PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(0, true));
	}
	
	public void updateTypeOne(Player player)
	{
		int getCurrentFrenzyMeterFromDB = playerDAO.getFrenzyMeterByObjId(player.getObjectId());
		Item token = player.getInventory().getFirstItemByItemId(186000389);
		int wtfPoint = 0;
		if (token != null)
		{
			wtfPoint = +70787;
		}
		PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(1, wtfPoint, getCurrentFrenzyMeterFromDB));
	}
	
	public void upgradeTypeTwo(Player player)
	{
		PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(2));
	}
	
	public void upgradeTypeThree(final Player player)
	{
		int getCurrentFrenzyMeterFromDB = playerDAO.getFrenzyMeterByObjId(player.getObjectId());
		final int getCurrentUpgradeLvlFromDB = playerDAO.getUpgradeLvlByObjId(player.getObjectId());
		Item token = player.getInventory().getFirstItemByItemId(186000389);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_POPUP_GACHA_FEVER_TIME_CHECK, 0, 0));
		boolean result = false;
		float random = Rnd.get(1, 1000) / 10f;
		if (random >= 10)
		{
			result = true;
		}
		final PlayerUpgradeArcade lr = new PlayerUpgradeArcade(getCurrentFrenzyMeterFromDB, getCurrentUpgradeLvlFromDB);
		if (result)
		{
			player.getPlayerUpgradeArcade().setFrenzyMeter(lr.getFrenzyMeter() + 5);
			player.getPlayerUpgradeArcade().setFrenzyMeterByObjId(player.getObjectId());
			player.getInventory().decreaseItemCount(token, 1);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(3, 1, getCurrentFrenzyMeterFromDB));
			ThreadPoolManager.getInstance().schedule(() ->
			{
				if (lr.getFrenzyMeter() == 100)
				{
					player.getPlayerUpgradeArcade().setFrenzyMeter(0);
					player.getPlayerUpgradeArcade().setFrenzyMeterByObjId(player.getObjectId());
					player.getPlayerUpgradeArcade().setUpgradeLvl(lr.getUpgradeLvl() + 1);
					player.getPlayerUpgradeArcade().setUpgradeLvlByObjId(player.getObjectId());
				}
				PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(4, player.getPlayerUpgradeArcade().getUpgradeLvl()));
			}, 3000);
			lr.setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		else
		{
			player.getPlayerUpgradeArcade().setFrenzyMeter(lr.getFrenzyMeter() - 5);
			player.getPlayerUpgradeArcade().setFrenzyMeterByObjId(player.getObjectId());
			player.getInventory().decreaseItemCount(token, 1);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(3, 0, getCurrentFrenzyMeterFromDB));
			ThreadPoolManager.getInstance().schedule(() -> PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(5, player.getPlayerUpgradeArcade().getUpgradeLvl(), true, 3)), 3000);
			lr.setPersistentState(PersistentState.UPDATE_REQUIRED);
		}
		player.setPlayerUpgradeArcade(lr);
	}
	
	public void upgradeTypeSix(Player player)
	{
		int getCurrentUpgradeLvlFromDB = playerDAO.getUpgradeLvlByObjId(player.getObjectId());
		int itemIdTier1 = getRewardTier1();
		int countTier1 = 0;
		/** Reward Tier 1 **/
		if (itemIdTier1 == 167010180)
		{ // [Event] Manastone: Attack +6
			countTier1 = 1;
		}
		else if (itemIdTier1 == 188053620)
		{ // Manastone Bundle
			countTier1 = 1;
		}
		else if (itemIdTier1 == 186000237)
		{ // Ancien Coin
			countTier1 = 20;
		}
		else if (itemIdTier1 == 186000247)
		{ // Major Danuar Relic
			countTier1 = 1;
		}
		else if (itemIdTier1 == 188051853)
		{ // [Event] Form Candy Bundle
			countTier1 = 1;
		}
		else if (itemIdTier1 == 188053616)
		{ // Tidal Idian Bundle
			countTier1 = 1;
		}
		else if (itemIdTier1 == 188052822)
		{ // Noble Ancient Manastone Chest
			countTier1 = 20;
		}
		else if (itemIdTier1 == 188052674)
		{ // [Event] Heroic Godstone Bundle I
			countTier1 = 1;
		}
		
		/** Reward Tier 2 **/
		int itemIdTier2 = getRewardTier2();
		int countTier2 = 0;
		if (itemIdTier2 == 110900830)
		{ // Astronaut's Costume
			countTier2 = 1;
		}
		else if (itemIdTier2 == 188052674)
		{ // [Event] Heroic Godstone Bundle I
			countTier2 = 1;
		}
		else if (itemIdTier2 == 167010180)
		{ // [Event] Manastone: Attack +6
			countTier2 = 1;
		}
		else if (itemIdTier2 == 188053624)
		{ // Unified Return Scroll Bundle
			countTier2 = 20;
		}
		else if (itemIdTier2 == 188052654)
		{ // Noble Ancient Manastone Bundle
			countTier2 = 1;
		}
		else if (itemIdTier2 == 166100008)
		{ // Greater Supplements (Eternal)
			countTier2 = 100;
		}
		else if (itemIdTier2 == 188053628)
		{ // Pallasite Bundle
			countTier2 = 1;
		}
		else if (itemIdTier2 == 188053213)
		{ // [Event] Remodeled Danuar Weapon Chest
			countTier2 = 1;
		}
		
		/** Reward Tier 3 **/
		int itemIdTier3 = getRewardTier3();
		int countTier3 = 0;
		if (itemIdTier3 == 125045493)
		{ // Astronaut's Helmet
			countTier3 = 1;
		}
		else if (itemIdTier3 == 188053645)
		{ // Shugo's Special Reward Chest
			countTier3 = 1;
		}
		else if (itemIdTier3 == 188053617)
		{ // Noble Tidal Idian Bundle
			countTier3 = 1;
		}
		else if (itemIdTier3 == 188053626)
		{ // Strong Wyvern Form Candy Bundle
			countTier3 = 1;
		}
		else if (itemIdTier3 == 188053002)
		{ // [Event] Noble Composite Manastone Bundle
			countTier3 = 1;
		}
		else if (itemIdTier3 == 166100011)
		{ // Greater Supplements (Mythic)
			countTier3 = 200;
		}
		else if (itemIdTier3 == 190020214)
		{ // Royal Kitter Egg
			countTier3 = 1;
		}
		else if (itemIdTier3 == 188053618)
		{ // Honorable Elim's Idian Bundle
			countTier3 = 1;
		}
		
		/** Reward Tier 4 **/
		int itemIdTier4 = getRewardTier4();
		int countTier4 = 0;
		if (itemIdTier4 == 188053627)
		{ // Sleek Hovercycle Chest
			countTier4 = 1;
		}
		else if (itemIdTier4 == 188053628)
		{ // Pallasite Bundle
			countTier4 = 1;
		}
		else if (itemIdTier4 == 166150019)
		{ // Assured Greater Felicitous Socketing (Mythic)
			countTier4 = 1;
		}
		else if (itemIdTier4 == 188053002)
		{ // [Event] Noble Composite Manastone Bundle
			countTier4 = 1;
		}
		else if (itemIdTier4 == 188053614)
		{ // Illusion Godstone Bundle
			countTier4 = 1;
		}
		else if (itemIdTier4 == 188052675)
		{ // [Event] Heroic Godstone Bundle II
			countTier4 = 1;
		}
		else if (itemIdTier4 == 188053003)
		{ // [Event] Weapon Shard Box
			countTier4 = 1;
		}
		else if (itemIdTier4 == 187060161)
		{ // Blitzbolt Wings
			countTier4 = 1;
		}
		if ((getCurrentUpgradeLvlFromDB >= 1) && (getCurrentUpgradeLvlFromDB <= 3))
		{
			ItemService.addItem(player, itemIdTier1, countTier1);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(6, itemIdTier1, countTier1, 0));
		}
		else if ((getCurrentUpgradeLvlFromDB >= 4) && (getCurrentUpgradeLvlFromDB <= 5))
		{
			ItemService.addItem(player, itemIdTier2, countTier2);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(6, itemIdTier2, countTier2, 0));
		}
		else if ((getCurrentUpgradeLvlFromDB >= 6) && (getCurrentUpgradeLvlFromDB <= 7))
		{
			ItemService.addItem(player, itemIdTier3, countTier3);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(6, itemIdTier3, countTier3, 0));
		}
		else if (getCurrentUpgradeLvlFromDB == 8)
		{
			ItemService.addItem(player, itemIdTier4, countTier4);
			PacketSendUtility.sendPacket(player, new SM_UPGRADE_ARCADE(6, itemIdTier4, countTier4, 0));
		}
		player.getPlayerUpgradeArcade().setFrenzyMeter(1);
		player.getPlayerUpgradeArcade().setFrenzyMeterByObjId(player.getObjectId());
		player.getPlayerUpgradeArcade().setUpgradeLvl(1);
		player.getPlayerUpgradeArcade().setUpgradeLvlByObjId(player.getObjectId());
	}
	
	private int getRewardTier1()
	{
		return Rnd.get(tier1);
	}
	
	private int getRewardTier2()
	{
		return Rnd.get(tier2);
	}
	
	private int getRewardTier3()
	{
		return Rnd.get(tier3);
	}
	
	private int getRewardTier4()
	{
		return Rnd.get(tier4);
	}
	
	private final int[] tier1 =
	{
		167010180, // [Event] Manastone: Attack +6
		188053620, // Manastone Bundle
		186000237, // Ancient Coin
		186000247, // Major Danuar Relic
		188051853, // [Event] Form Candy Bundle
		188053616, // Tidal Idian Bundle
		188052822, // Noble Ancient Manastone Chest
		188052674, // [Event] Heroic Godstone Bundle I
	};
	
	private final int[] tier2 =
	{
		110900830, // Astronaut's Costume
		188052674, // [Event] Heroic Godstone Bundle I
		167010180, // [Event] Manastone: Attack +6
		188053624, // Unified Return Scroll Bundle
		188052654, // Noble Ancient Manastone Bundle
		166100008, // Greater Supplements (Eternal)
		188053628, // Pallasite Bundle
		188053213, // [Event] Remodeled Danuar Weapon Chest
	};
	
	private final int[] tier3 =
	{
		125045493, // Astronaut's Helmet
		188053645, // Shugo's Special Reward Chest
		188053617, // Noble Tidal Idian Bundle
		188053626, // Strong Wyvern Form Candy Bundle
		188053002, // [Event] Noble Composite Manastone Bundle
		166100011, // Greater Supplements (Mythic)
		190020214, // Royal Kitter Egg
		188053618, // Honorable Elim's Idian Bundle
	};
	
	private final int[] tier4 =
	{
		188053627, // Sleek Hovercycle Chest
		188053628, // Pallasite Bundle
		166150019, // Assured Greater Felicitous Socketing (Mythic)
		188053002, // [Event] Noble Composite Manastone Bundle
		188053614, // Illusion Godstone Bundle
		188052675, // [Event] Heroic Godstone Bundle II
		188053003, // [Event] Weapon Shard Box
		187060161, // Blitzbolt Wings
	};
	
	private static class SingletonHolder
	{
		protected static final UpgradeArcadeService instance = new UpgradeArcadeService();
	}
	
	public static final UpgradeArcadeService getInstance()
	{
		return SingletonHolder.instance;
	}
}