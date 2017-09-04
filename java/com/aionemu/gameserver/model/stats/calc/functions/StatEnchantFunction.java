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
package com.aionemu.gameserver.model.stats.calc.functions;

import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.items.ItemSlot;
import com.aionemu.gameserver.model.stats.calc.Stat2;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.model.templates.item.ArmorType;
import com.aionemu.gameserver.model.templates.item.ItemCategory;

public class StatEnchantFunction extends StatAddFunction
{
	private final Item item;
	private final int point;
	
	public StatEnchantFunction(Item owner, StatEnum stat, int point)
	{
		this.stat = stat;
		item = owner;
		this.point = point;
	}
	
	@Override
	public final int getPriority()
	{
		return 30;
	}
	
	@Override
	public void apply(Stat2 stat)
	{
		if (!item.isEquipped())
		{
			return;
		}
		int enchantLvl = item.getEnchantLevel();
		if (item.getItemTemplate().isAccessory() || (item.getItemTemplate().getCategory() == ItemCategory.HELMET))
		{
			enchantLvl = item.getAuthorize();
		}
		if (enchantLvl == 0)
		{
			return;
		}
		if (((item.getEquipmentSlot() & ItemSlot.MAIN_OFF_HAND.getSlotIdMask()) != 0) || ((item.getEquipmentSlot() & ItemSlot.SUB_OFF_HAND.getSlotIdMask()) != 0))
		{
			return;
		}
		if ((item.getItemTemplate().getArmorType() == ArmorType.PLUME) || (item.getItemTemplate().getArmorType() == ArmorType.BRACELET))
		{
			stat.addToBonus(getEnchantAdditionModifier(enchantLvl, stat));
		}
		
		stat.addToBase(getEnchantAdditionModifier(enchantLvl, stat));
		
	}
	
	private int getEnchantAdditionModifier(int enchantLvl, Stat2 stat)
	{
		if (item.getItemTemplate().isWeapon())
		{
			return getWeaponModifiers(enchantLvl);
		}
		
		if (item.getItemTemplate().isAccessory() && item.getItemTemplate().isPlume() && item.getItemTemplate().isBracelet())
		{
			if (point == 0)
			{
				return getAccessoryModifiers(enchantLvl);
			}
			return point;
		}
		
		if (item.getItemTemplate().isArmor() || item.getItemTemplate().isPlume() || item.getItemTemplate().isBracelet())
		{
			return getArmorModifiers(enchantLvl, stat);
		}
		return 0;
	}
	
	private int getWeaponModifiers(int enchantLvl)
	{
		switch (stat)
		{
			case MAIN_HAND_POWER:
			case OFF_HAND_POWER:
			case PHYSICAL_ATTACK:
			{
				switch (item.getItemTemplate().getWeaponType())
				{
					case GUN_1H:
					case SWORD_1H:
					case DAGGER_1H:
					{
						return 2 * enchantLvl;
					}
					case BOW:
					case SWORD_2H:
					case POLEARM_2H:
					{
						return 3 * enchantLvl;
					}
					case MACE_1H:
					case STAFF_2H:
					{
						return 3 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			case BOOST_MAGICAL_SKILL:
			{
				switch (item.getItemTemplate().getWeaponType())
				{
					case ORB_2H:
					case GUN_1H:
					case HARP_2H:
					case BOOK_2H:
					case MACE_1H:
					case STAFF_2H:
					case CANNON_2H:
					case KEYBLADE_2H:
					{
						return 20 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			case MAGICAL_ATTACK:
			{
				switch (item.getItemTemplate().getWeaponType())
				{
					case GUN_1H:
					{
						return 2 * enchantLvl;
					}
					case ORB_2H:
					case BOOK_2H:
					case HARP_2H:
					{
						return 3 * enchantLvl;
					}
					case CANNON_2H:
					case KEYBLADE_2H:
					{
						return 3 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			default:
			{
				return 0;
			}
		}
	}
	
	private int getAccessoryModifiers(int autorizeLvl)
	{
		switch (stat)
		{
			case PVP_ATTACK_RATIO:
			case PVP_ATTACK_RATIO_PHYSICAL:
			case PVP_ATTACK_RATIO_MAGICAL:
			{
				switch (item.getItemTemplate().getCategory())
				{
					case HELMET:
					case EARRINGS:
					case NECKLACE:
					{
						return 5 * autorizeLvl;
					}
					default:
					{
						break;
					}
				}
			}
			case PVP_DEFEND_RATIO:
			case PVP_DEFEND_RATIO_PHYSICAL:
			case PVP_DEFEND_RATIO_MAGICAL:
			{
				switch (item.getItemTemplate().getCategory())
				{
					case RINGS:
					case BELT:
					{
						return 7 * autorizeLvl;
					}
					default:
					{
						break;
					}
				}
			}
			default:
			{
				break;
			}
		}
		return 0;
	}
	
	private int getArmorModifiers(int enchantLvl, Stat2 applyStat)
	{
		final ArmorType armorType = item.getItemTemplate().getArmorType();
		if (armorType == null)
		{
			return 0;
		}
		// long slot = item.getEquipmentSlot();
		final int equipmentSlot = (int) (item.getEquipmentSlot() & 0xFFFFFFFF);
		switch (item.getItemTemplate().getArmorType())
		{
			/**
			 * 4.9 Enchant Stats
			 */
			case ROBE:
			{
				switch (equipmentSlot)
				{
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return enchantLvl;
							}
							case MAXHP:
							{
								return 20 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 2 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 12:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 2 * enchantLvl;
							}
							case MAXHP:
							{
								return 22 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 3 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 3:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 3 * enchantLvl;
							}
							case MAXHP:
							{
								return 24 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 4 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 3 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
				}
				return 0;
			}
			case LEATHER:
			{
				switch (equipmentSlot)
				{
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 2 * enchantLvl;
							}
							case MAXHP:
							{
								return 18 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 2 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 12:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 3 * enchantLvl;
							}
							case MAXHP:
							{
								return 20 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 3 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 3:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 4 * enchantLvl;
							}
							case MAXHP:
							{
								return 22 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 4 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 3 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
				}
				return 0;
			}
			case CHAIN:
			{
				switch (equipmentSlot)
				{
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 3 * enchantLvl;
							}
							case MAXHP:
							{
								return 16 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 2 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 12:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 4 * enchantLvl;
							}
							case MAXHP:
							{
								return 18 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 3 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 3:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 5 * enchantLvl;
							}
							case MAXHP:
							{
								return 20 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 4 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 3 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
				}
				return 0;
			}
			case PLATE:
			{
				switch (equipmentSlot)
				{
					case 1 << 5:
					case 1 << 11:
					case 1 << 4:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 4 * enchantLvl;
							}
							case MAXHP:
							{
								return 14 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 2 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 12:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 5 * enchantLvl;
							}
							case MAXHP:
							{
								return 16 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 3 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 2 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
					case 1 << 3:
					{
						switch (stat)
						{
							case PHYSICAL_ATTACK:
							{
								return enchantLvl;
							}
							case BOOST_MAGICAL_SKILL:
							{
								return 2 * enchantLvl;
							}
							case PHYSICAL_DEFENSE:
							{
								return 6 * enchantLvl;
							}
							case MAXHP:
							{
								return 18 * enchantLvl;
							}
							case PHYSICAL_CRITICAL_RESIST:
							{
								return 4 * enchantLvl;
							}
							case MAGICAL_DEFEND:
							{
								return 3 * enchantLvl;
							}
							default:
							{
								break;
							}
						}
						return 0;
					}
				}
				return 0;
			}
			case SHIELD:
			{
				switch (stat)
				{
					case DAMAGE_REDUCE:
					{
						final float reduceRate = enchantLvl > 10 ? 0.2f : enchantLvl * 0.02f;
						return Math.round(reduceRate * applyStat.getBase());
					}
					case BLOCK:
					{
						if (enchantLvl > 10)
						{
							return 30 * (enchantLvl - 10);
						}
						return 0;
					}
					default:
					{
						break;
					}
				}
			}
			case PLUME:
			{
				switch (stat)
				{
					case MAXHP:
					{
						return 150 * enchantLvl;
					}
					case PHYSICAL_ATTACK:
					{
						return 4 * enchantLvl;
					}
					case BOOST_MAGICAL_SKILL:
					{
						return 20 * enchantLvl;
					}
					case PHYSICAL_CRITICAL:
					{
						return 12 * enchantLvl;
					}
					case PHYSICAL_ACCURACY:
					{
						return 16 * enchantLvl;
					}
					case MAGICAL_ACCURACY:
					{
						return 8 * enchantLvl;
					}
					case MAGICAL_CRITICAL:
					{
						return 8 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			/**
			 * 5.0 Wings Enchant
			 */
			case WING:
			{
				switch (stat)
				{
					case PHYSICAL_ATTACK:
					{
						return 1 * enchantLvl;
					}
					case BOOST_MAGICAL_SKILL:
					{
						return 4 * enchantLvl;
					}
					case MAXHP:
					{
						return 20 * enchantLvl;
					}
					case PHYSICAL_CRITICAL_RESIST:
					{
						return 2 * enchantLvl;
					}
					case FLY_TIME:
					{
						return 10 * enchantLvl;
					}
					case MAGICAL_CRITICAL_RESIST:
					{
						return 1 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			/**
			 * 5.1 Bracelet Enchant
			 */
			case BRACELET:
			{
				switch (stat)
				{
					case PVP_DEFEND_RATIO_PHYSICAL:
					{
						return 3 * enchantLvl;
					}
					case PVP_DEFEND_RATIO_MAGICAL:
					{
						return 3 * enchantLvl;
					}
					default:
					{
						break;
					}
				}
				return 0;
			}
			default:
			{
				break;
			}
		}
		return 0;
	}
}