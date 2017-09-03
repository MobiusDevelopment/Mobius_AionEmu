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
package system.handlers.playercommands;

import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.PlayerCommand;

/***************************/
/* Created by ElApotsainer */
/***************************/
public class cmd_uniquepack extends PlayerCommand
{
	
	public cmd_uniquepack()
	{
		super("uniquepack");
	}
	
	@Override
	public void execute(final Player player, String... param)
	{
		if (param.length < 1)
		{
			PacketSendUtility.sendMessage(player, "Syntax:' .uniquepack info '");
			return;
		}
		
		if (param[0].equals("scrolls"))
		{
			ItemService.addItem(player, 164002111, 500); // <Greater Running Scroll (1 hour)>
			ItemService.addItem(player, 164002113, 500); // <Greater Awakening Scroll (1 hour)>
			ItemService.addItem(player, 164002112, 500); // <Greater Courage Scroll (1 hour)>
			ItemService.addItem(player, 164002115, 500); // <Major Crit Spell Scroll (1 hour)>
			ItemService.addItem(player, 164002114, 500); // <Major Crit Strike Scroll (1 hour)>
			ItemService.addItem(player, 164000259, 500); // <Premium Anti-Shock Scroll>
			ItemService.addItem(player, 164000260, 500); // <Premium Magic Suppression Scroll>
			ItemService.addItem(player, 164000021, 500); // <Boost Elemental Defense>
			ItemService.addItem(player, 164000114, 500); // <Fine Fireproof Scroll>
			ItemService.addItem(player, 164000115, 500); // <Fine Earthproof Scroll>
			ItemService.addItem(player, 164000116, 500); // <Fine Waterproof Scroll>
			ItemService.addItem(player, 164000117, 500); // <Fine Windproof Scroll>
			ItemService.addItem(player, 164000257, 500); // <Fine Strike Resist Scroll>
			ItemService.addItem(player, 162000083, 500); // <Leader's Recovery Scroll>
			PacketSendUtility.sendMessage(player, "\uE022 Scrolls has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("potions"))
		{
			ItemService.addItem(player, 162000178, 500); // <Refined Recovery Potion>
			ItemService.addItem(player, 162000181, 500); // <Vital Recovery Serum>
			ItemService.addItem(player, 162000197, 500); // <Upgraded Divine Life Serum>
			ItemService.addItem(player, 162000141, 500); // <Sublime Wind Serum>
			ItemService.addItem(player, 162000107, 500); // <Saam King's Herbs>
			PacketSendUtility.sendMessage(player, "\uE022 Potions has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("goodies"))
		{
			ItemService.addItem(player, 160009017, 1); // <Vinna Juice>
			ItemService.addItem(player, 160010337, 1); // <Event Heartfelt Chocolate>
			ItemService.addItem(player, 160010333, 1); // <Event Scintillating Chocolate>
			ItemService.addItem(player, 160010334, 1); // <Event Constitutional Chocolate>
			ItemService.addItem(player, 164000266, 50); // <Enhanced Seed of Detection>
			ItemService.addItem(player, 160003587, 50); // <Pepento Jelly>
			ItemService.addItem(player, 169300007, 10000); // <Master Odella Powder>
			ItemService.addItem(player, 160005052, 500); // <Spicy Banquet Meal for Victory Wishes>
			ItemService.addItem(player, 160002433, 500); // <Spicy Plumbo with Cheese>
			ItemService.addItem(player, 166050129, 10); // <Blazing Warlord's Idian: Physical/Magical Attack>
			ItemService.addItem(player, 169610146, 1); // <Glorious Number One (30 days)>
			PacketSendUtility.sendMessage(player, "\uE022 Goodies has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("gladiator"))
		{
			ItemService.addItem(player, 110601845, 1); // <[Parry] Boundless Plate Breast Protector of Hope>
			ItemService.addItem(player, 113601792, 1); // <[Parry] Boundless Plate Leg Guards of Hope>
			ItemService.addItem(player, 114601798, 1); // <[Parry] Boundless Plate Foot Protectors of Hope>
			ItemService.addItem(player, 111601815, 1); // <[Parry] Boundless Plate Hand Protectors of Hope>
			ItemService.addItem(player, 112601796, 1); // <[Parry] Boundless Plate Shoulder Pad of Hope>
			ItemService.addItem(player, 125004820, 1); // <[Parry] Boundless Plate Protective Helmet of Hope>
			ItemService.addItem(player, 123001543, 1); // <Boundless Leather Waist Band of Hope>
			ItemService.addItem(player, 120001629, 2); // <Boundless Opal Earrings of Hope>
			ItemService.addItem(player, 121001508, 1); // <Boundless Opal Necklace of Hope>
			ItemService.addItem(player, 122001790, 2); // <Boundless Opal Ring of Hope>
			ItemService.addItem(player, 101301414, 2); // <Boundless Polearm of Glory>
			ItemService.addItem(player, 101301416, 1); // <Polearm of Provenance>
			ItemService.addItem(player, 167010259, 50); // <Manastone: Attack +6 / Crit Strike +9>
			ItemService.addItem(player, 167010264, 50); // <Manastone: Attack +6 / Accuracy +14>
			ItemService.addItem(player, 167010283, 25); // <Manastone: Precision +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060242, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Gladiator Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("templar"))
		{
			ItemService.addItem(player, 110601844, 1); // <[Block] Boundless Plate Breastplate of Hope>
			ItemService.addItem(player, 113601791, 1); // <[Block] Boundless Plate Gaiters of Hope>
			ItemService.addItem(player, 114601797, 1); // <[Block] Boundless Plate Boots of Hope>
			ItemService.addItem(player, 111601814, 1); // <[Block] Boundless Plate Gloves of Hope>
			ItemService.addItem(player, 112601795, 1); // <[Block] Boundless Plate Pauldrons of Hope>
			ItemService.addItem(player, 125004819, 1); // <[Block] Boundless Plate Helmet of Hope>
			ItemService.addItem(player, 123001543, 1); // <Boundless Leather Waist Band of Hope>
			ItemService.addItem(player, 120001629, 2); // <Boundless Opal Earrings of Hope>
			ItemService.addItem(player, 121001508, 1); // <Boundless Opal Necklace of Hope>
			ItemService.addItem(player, 122001790, 2); // <Boundless Opal Ring of Hope>
			ItemService.addItem(player, 100901530, 2); // <Boundless Great Sword of Glory>
			ItemService.addItem(player, 100901532, 1); // <Provenance Greatsword>
			ItemService.addItem(player, 100002013, 1); // <Boundless Long Sword of Glory>
			ItemService.addItem(player, 100002015, 1); // <Provenance Sword>
			ItemService.addItem(player, 115001971, 1); // <Shield of Eternal Glory>
			ItemService.addItem(player, 167010259, 50); // <Manastone: Attack +6 / Crit Strike +9>
			ItemService.addItem(player, 167010261, 50); // <Manastone: Attack +6 / Magical Accuracy +8>
			ItemService.addItem(player, 167010283, 25); // <Manastone: Precision +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060242, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Templar Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("cleric"))
		{
			ItemService.addItem(player, 110551423, 1); // <[MB] Boundless Chain Breastplate of Hope>
			ItemService.addItem(player, 113502002, 1); // <[MB] Boundless Chain Gaiters of Hope>
			ItemService.addItem(player, 114502010, 1); // <[MB] Boundless Chain Combat Boots of Hope>
			ItemService.addItem(player, 111501992, 1); // <[MB] Boundless Chain Combat Gloves of Hope>
			ItemService.addItem(player, 112501928, 1); // <[MB] Boundless Chain Spaulders of Hope>
			ItemService.addItem(player, 125004823, 1); // <[MB] Boundless Chain Combat Helmet of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 101501516, 2); // <Boundless Staff of Glory>
			ItemService.addItem(player, 100101495, 1); // <Boundless Mace of Glory>
			ItemService.addItem(player, 115001972, 1); // <Boundless Scale Shield of Glory>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 167010281, 25); // <Manastone: HP +5>
			ItemService.addItem(player, 167010282, 25); // <Manastone: Agility +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Cleric Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("chanter"))
		{
			ItemService.addItem(player, 110551421, 1); // <[Parry] Boundless Chain Armor of Hope>
			ItemService.addItem(player, 113502000, 1); // <[Parry] Boundless Chain Leg Armor of Hope>
			ItemService.addItem(player, 114502008, 1); // <[Parry] Boundless Chain Boots of Hope>
			ItemService.addItem(player, 111501990, 1); // <[Parry] Boundless Chain Gloves of Hope>
			ItemService.addItem(player, 112501926, 1); // <[Parry] Boundless Chain Pauldrons of Hope>
			ItemService.addItem(player, 125004821, 1); // <[Parry] Boundless Chain Helmet of Hope>
			ItemService.addItem(player, 123001543, 1); // <Boundless Leather Waist Band of Hope>
			ItemService.addItem(player, 120001629, 2); // <Boundless Opal Earrings of Hope>
			ItemService.addItem(player, 121001508, 1); // <Boundless Opal Necklace of Hope>
			ItemService.addItem(player, 122001790, 2); // <Boundless Opal Ring of Hope>
			ItemService.addItem(player, 101501516, 2); // <Boundless Staff of Glory>
			ItemService.addItem(player, 101501518, 1); // <Provenance Staff>
			ItemService.addItem(player, 100101495, 1); // <Boundless Mace of Glory>
			ItemService.addItem(player, 115001971, 1); // <Shield of Eternal Glory>
			ItemService.addItem(player, 167010259, 50); // <Manastone: Attack +6 / Crit Strike +9>
			ItemService.addItem(player, 167010261, 50); // <Manastone: Attack +6 / Magical Accuracy +8>
			ItemService.addItem(player, 167010283, 25); // <Manastone: Precision +5>
			ItemService.addItem(player, 167010281, 25); // <Manastone: HP +5>
			ItemService.addItem(player, 167010285, 25); // <Manastone: Will +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060242, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Chanter Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("assassin"))
		{
			ItemService.addItem(player, 110302056, 1); // <Boundless Leather Vest of Hope>
			ItemService.addItem(player, 113302026, 1); // <Boundless Leather Leg Protectors of Hope>
			ItemService.addItem(player, 114302063, 1); // <Boundless Leather Shoes of Hope>
			ItemService.addItem(player, 111302001, 1); // <Boundless Leather Gloves of Hope>
			ItemService.addItem(player, 112301938, 1); // <Boundless Leather Shoulder Guard of Hope>
			ItemService.addItem(player, 125004824, 1); // <Boundless Leather Hat of Hope>
			ItemService.addItem(player, 123001543, 1); // <Boundless Leather Waist Band of Hope>
			ItemService.addItem(player, 120001629, 2); // <Boundless Opal Earrings of Hope>
			ItemService.addItem(player, 121001508, 1); // <Boundless Opal Necklace of Hope>
			ItemService.addItem(player, 122001790, 2); // <Boundless Opal Ring of Hope>
			ItemService.addItem(player, 100002013, 2); // <Boundless Long Sword of Glory>
			ItemService.addItem(player, 100201676, 1); // <Boundless Dagger of Glory>
			ItemService.addItem(player, 101701511, 2); // <Boundless Bow of Glory>
			ItemService.addItem(player, 167010259, 50); // <Manastone: Attack +6 / Crit Strike +9>
			ItemService.addItem(player, 167010261, 50); // <Manastone: Attack +6 / Magical Accuracy +8>
			ItemService.addItem(player, 167010280, 25); // <Manastone: Power +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060242, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Assassin Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("ranger"))
		{
			ItemService.addItem(player, 110302056, 1); // <Boundless Leather Vest of Hope>
			ItemService.addItem(player, 113302026, 1); // <Boundless Leather Leg Protectors of Hope>
			ItemService.addItem(player, 114302063, 1); // <Boundless Leather Shoes of Hope>
			ItemService.addItem(player, 111302001, 1); // <Boundless Leather Gloves of Hope>
			ItemService.addItem(player, 112301938, 1); // <Boundless Leather Shoulder Guard of Hope>
			ItemService.addItem(player, 125004824, 1); // <Boundless Leather Hat of Hope>
			ItemService.addItem(player, 123001543, 1); // <Boundless Leather Waist Band of Hope>
			ItemService.addItem(player, 120001629, 2); // <Boundless Opal Earrings of Hope>
			ItemService.addItem(player, 121001508, 1); // <Boundless Opal Necklace of Hope>
			ItemService.addItem(player, 122001790, 2); // <Boundless Opal Ring of Hope>
			ItemService.addItem(player, 100002013, 2); // <Boundless Long Sword of Glory>
			ItemService.addItem(player, 100201676, 1); // <Boundless Dagger of Glory>
			ItemService.addItem(player, 101701511, 2); // <Boundless Bow of Glory>
			ItemService.addItem(player, 167010259, 50); // <Manastone: Attack +6 / Crit Strike +9>
			ItemService.addItem(player, 167010261, 50); // <Manastone: Attack +6 / Magical Accuracy +8>
			ItemService.addItem(player, 167010280, 25); // <Manastone: Power +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060242, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Ranger Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("sorcerer"))
		{
			ItemService.addItem(player, 110102080, 1); // <[MR] Boundless Cloth Jacket of Hope>
			ItemService.addItem(player, 113101885, 1); // <[MR] Boundless Cloth Pants of Hope>
			ItemService.addItem(player, 114101919, 1); // <[MR] Boundless Cloth Shoes of Hope>
			ItemService.addItem(player, 111101878, 1); // <[MR] Boundless Cloth Gloves of Hope>
			ItemService.addItem(player, 112101822, 1); // <[MR] Boundless Cloth Shoulder Guard of Hope>
			ItemService.addItem(player, 125004826, 1); // <[MR] Boundless Cloth Bandana of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 100601571, 1); // <Boundless Spellbook of Glory>
			ItemService.addItem(player, 100601573, 1); // <Spellbook of Bygone Atreia>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010250, 50); // <Manastone: Magic Boost: +29 / HP +50>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Sorcerer Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("spiritmaster"))
		{
			ItemService.addItem(player, 110102081, 1); // <[MS] Boundless Cloth Breast Protector of Hope>
			ItemService.addItem(player, 113101886, 1); // <[MS] Boundless Cloth Leg Guards of Hope>
			ItemService.addItem(player, 114101920, 1); // <[MS] Boundless Cloth Foot Protectors of Hope>
			ItemService.addItem(player, 111101879, 1); // <[MS] Boundless Cloth Hand Protectors of Hope>
			ItemService.addItem(player, 112101823, 1); // <[MS] Boundless Cloth Shoulder Pad of Hope>
			ItemService.addItem(player, 125004827, 1); // <[MS] Boundless Cloth Protective Bandana of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 100601571, 1); // <Boundless Spellbook of Glory>
			ItemService.addItem(player, 100601573, 1); // <Spellbook of Bygone Atreia>
			ItemService.addItem(player, 100501453, 1); // <Boundless Orb of Glory>
			ItemService.addItem(player, 100501455, 1); // <Orb of Bygone Atreia>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010250, 50); // <Manastone: Magic Boost: +29 / HP +50>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Spiritmaster Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("gunner"))
		{
			ItemService.addItem(player, 110302057, 1); // <Boundless Leather Breast Protector of Hope>
			ItemService.addItem(player, 113302027, 1); // <Boundless Leather Leg Guards of Hope>
			ItemService.addItem(player, 114302064, 1); // <Boundless Leather Foot Protectors of Hope>
			ItemService.addItem(player, 111302002, 1); // <Boundless Leather Hand Protectors of Hope>
			ItemService.addItem(player, 112301939, 1); // <Boundless Leather Shoulder Pad of Hope>
			ItemService.addItem(player, 125004825, 1); // <Boundless Leather Protective Hat of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 101801346, 2); // <Boundless Magic Gun of Glory>
			ItemService.addItem(player, 101901251, 2); // <Boundless Magic Cannon of Glory>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010250, 50); // <Manastone: Magic Boost: +29 / HP +50>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Gunner Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("aethertech"))
		{
			ItemService.addItem(player, 110551423, 1); // <[MB] Boundless Chain Breastplate of Hope>
			ItemService.addItem(player, 113502002, 1); // <[MB] Boundless Chain Gaiters of Hope>
			ItemService.addItem(player, 114502010, 1); // <[MB] Boundless Chain Combat Boots of Hope>
			ItemService.addItem(player, 111501992, 1); // <[MB] Boundless Chain Combat Gloves of Hope>
			ItemService.addItem(player, 112501928, 1); // <[MB] Boundless Chain Spaulders of Hope>
			ItemService.addItem(player, 125004823, 1); // <[MB] Boundless Chain Combat Helmet of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 102101189, 2); // <Boundless Keyblade of Glory>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010250, 50); // <Manastone: Magic Boost: +29 / HP +50>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Aethertech Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("songweaver"))
		{
			ItemService.addItem(player, 110102081, 1); // <[MS] Boundless Cloth Breast Protector of Hope>
			ItemService.addItem(player, 113101886, 1); // <[MS] Boundless Cloth Leg Guards of Hope>
			ItemService.addItem(player, 114101920, 1); // <[MS] Boundless Cloth Foot Protectors of Hope>
			ItemService.addItem(player, 111101879, 1); // <[MS] Boundless Cloth Hand Protectors of Hope>
			ItemService.addItem(player, 112101823, 1); // <[MS] Boundless Cloth Shoulder Pad of Hope>
			ItemService.addItem(player, 125004827, 1); // <[MS] Boundless Cloth Protective Bandana of Hope>
			ItemService.addItem(player, 123001544, 1); // <Boundless Cloth Waist Band of Hope>
			ItemService.addItem(player, 120001630, 2); // <Boundless Dareet Earrings of Hope>
			ItemService.addItem(player, 121001509, 1); // <Boundless Dareet Necklace of Hope>
			ItemService.addItem(player, 122001791, 2); // <Boundless Dareet Ring of Hope>
			ItemService.addItem(player, 102001374, 1); // <Boundless String Instrument of Glory>
			ItemService.addItem(player, 102001376, 1); // <Stringed Instrument of Bygone Atreia>
			ItemService.addItem(player, 167010258, 50); // <Manastone: Magic Boost +29 / Magical Accuracy +8>
			ItemService.addItem(player, 167010250, 50); // <Manastone: Magic Boost: +29 / HP +50>
			ItemService.addItem(player, 167010284, 25); // <Manastone: Knowledge +5>
			ItemService.addItem(player, 190100225, 1); // <Tamed Lion (1 day)>
			ItemService.addItem(player, 187060248, 1); // <Ancient Archdaeva�s Wings>
			PacketSendUtility.sendMessage(player, "\uE022 Songweaver Gears has been added to your Inventory. \uE022");
		}
		
		if (param[0].equals("info"))
		{
			PacketSendUtility.sendMessage(player, "----- \uE079 Unique's Pack Information \uE079 -----\n" + "STUFF:\n" + "For Scrolls Type: ' .uniquepack scrolls '\n" + "For Potions Type: ' .uniquepack potions '\n" + "For 'goodies' Shards , Titles , Idians , ETC Type: ' .uniquepack goodies '\n" + "--------------------------------------------------------------------------\n" + "GEAR:\n" + "For \uE079 GLADIATOR \uE079 Type: ' .uniquepack gladiator '\n" + "For \uE079 TEMPLAR \uE079 Type: ' .uniquepack templar '\n" + "For \uE079 CLERIC \uE079 Type: ' .uniquepack cleric '\n" + "For \uE079 CHANTER \uE079 Type: ' .uniquepack chanter '\n" + "For \uE079 ASSASSIN \uE079 Type: ' .uniquepack assassin '\n" + "For \uE079 RANGER \uE079 Type: ' .uniquepack ranger '\n" + "For \uE079 SORCERER \uE079 Type: ' .uniquepack sorcerer '\n" + "For \uE079 SPIRITMASTER \uE079 Type: ' .uniquepack spiritmaster '\n" + "For \uE079 GUNNER \uE079 Type: ' .uniquepack gunner '\n" + "For \uE079 AETHERTECH \uE079 Type: ' .uniquepack aethertech '\n" + "For \uE079 SONGWEAVER \uE079 Type: ' .uniquepack songweaver '\n" + "\uE020 Have fun kickin ass! \uE020");
		}
		
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Syntax:' .uniquepack info '");
	}
}