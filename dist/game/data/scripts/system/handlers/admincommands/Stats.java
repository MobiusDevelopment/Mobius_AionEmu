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

import java.util.ArrayList;
import java.util.List;

import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Item;
import com.aionemu.gameserver.model.gameobjects.PersistentState;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.stats.calc.StatOwner;
import com.aionemu.gameserver.model.stats.calc.functions.IStatFunction;
import com.aionemu.gameserver.model.stats.calc.functions.StatFunction;
import com.aionemu.gameserver.model.stats.container.StatEnum;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.services.item.ItemPacketService;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;

/**
 * Created by Kill3r
 */
public class Stats extends AdminCommand implements StatOwner
{
	
	public Stats()
	{
		super("stats");
	}
	
	@Override
	public void execute(Player player, String... params)
	{
		int value = 1;
		try
		{
			value = Integer.parseInt(params[1]);
		}
		catch (NumberFormatException e)
		{
			PacketSendUtility.sendMessage(player, "Wrong Param");
			return;
		}
		
		if (params[0].equals("hp"))
		{
			if (value < 9999999)
			{
				final List<IStatFunction> functions = new ArrayList<>();
				functions.add(new StatChangeFunction(StatEnum.MAXHP, value));
				player.getGameStats().addEffect(this, functions);
				PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.START_EMOTE2, 0, 0), true);
				PacketSendUtility.sendMessage(player, "You've changed you're MAX HP to " + value);
			}
			else
			{
				PacketSendUtility.sendMessage(player, "You've exceeded the max amount! Use a lower value!");
			}
		}
		else if (params[0].equals("dev"))
		{
			if (player.getAccessLevel() < 5)
			{
				PacketSendUtility.sendMessage(player, "Need Access!");
				return;
			}
			final Item plume = player.getEquipment().getEquipedPlume();
			final int lvl = Integer.parseInt(params[1]);
			plume.setAuthorize(lvl);
			player.getGameStats().updateStatsVisually();
			ItemPacketService.updateItemAfterInfoChange(player, plume);
			plume.setPersistentState(PersistentState.UPDATE_REQUIRED);
			PacketSendUtility.sendMessage(player, "Enchanted to : " + lvl);
		}
	}
	
	class StatChangeFunction extends StatFunction
	{
		static final int speed = 6000;
		static final int flyspeed = 9000;
		static final int maxDp = 4000;
		int modifier = 1;
		
		StatChangeFunction(StatEnum stat, int modifier)
		{
			this.stat = stat;
			this.modifier = modifier;
		}
		
		@Override
		public void apply(com.aionemu.gameserver.model.stats.calc.Stat2 stat)
		{
			switch (this.stat)
			{
				case SPEED:
					stat.setBase(speed + ((speed * modifier) / 100));
					break;
				case FLY_SPEED:
					stat.setBase(flyspeed + ((flyspeed * modifier) / 100));
					break;
				case POWER:
					final short modifierPower = (short) modifier;
					stat.setBase(Math.round(modifierPower));
					break;
				case MAXHP:
					final float modifierHp = modifier;
					stat.setBase(Math.round(modifierHp));
					break;
				case MAXMP:
					final float modifierMp = modifier;
					stat.setBase(Math.round(modifierMp));
					break;
				case REGEN_HP:
					int baseHp = stat.getOwner().getLevel() + 3;
					stat.setBase(baseHp *= modifier / 100f);
					break;
				case REGEN_MP:
					int baseMp = stat.getOwner().getLevel() + 8;
					stat.setBase(baseMp *= modifier / 100f);
					break;
				case MAXDP:
					stat.setBase(maxDp + ((maxDp * modifier) / 100));
					break;
				case ALLRESIST:
					final short modifierALLR = (short) modifier;
					stat.setBase(Math.round(modifierALLR));
					break;
				case ABNORMAL_RESISTANCE_ALL:
					final short modifierAbnormalALLR = (short) modifier;
					stat.setBase(Math.round(modifierAbnormalALLR));
					break;
				case STRVIT:
					final short modifierStrvit = (short) modifier;
					stat.setBase(Math.round(modifierStrvit));
					break;
				case KNOWIL:
					final short modifierKnowil = (short) modifier;
					stat.setBase(Math.round(modifierKnowil));
					break;
				case AGIDEX:
					final short modifierAgidex = (short) modifier;
					stat.setBase(Math.round(modifierAgidex));
					break;
				case HEALTH:
					final short modifierHealth = (short) modifier;
					stat.setBase(Math.round(modifierHealth));
					break;
				case ACCURACY:
					final short modifierAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierAccuracy));
					break;
				case AGILITY:
					final short modifierAgility = (short) modifier;
					stat.setBase(Math.round(modifierAgility));
					break;
				case KNOWLEDGE:
					final short modifierKnow = (short) modifier;
					stat.setBase(Math.round(modifierKnow));
					break;
				case WILL:
					final short modifierWill = (short) modifier;
					stat.setBase(Math.round(modifierWill));
					break;
				case WATER_RESISTANCE:
					final short modifierWaterRes = (short) modifier;
					stat.setBase(Math.round(modifierWaterRes));
					break;
				case WIND_RESISTANCE:
					final short modifierWindRes = (short) modifier;
					stat.setBase(Math.round(modifierWindRes));
					break;
				case EARTH_RESISTANCE:
					final short modifierEarthRes = (short) modifier;
					stat.setBase(Math.round(modifierEarthRes));
					break;
				case FIRE_RESISTANCE:
					final short modifierFireRes = (short) modifier;
					stat.setBase(Math.round(modifierFireRes));
					break;
				case REGEN_FP:
					stat.setBase(Math.round(modifier));
					break;
				case PHYSICAL_DEFENSE:
					stat.setBase(Math.round(modifier));
					break;
				case MAGICAL_ATTACK:
					final short modifierMAttack = (short) modifier;
					stat.setBase(Math.round(modifierMAttack));
					break;
				case MAGICAL_RESIST:
					final short modifierMResist = (short) modifier;
					stat.setBase(Math.round(modifierMResist));
					break;
				case ATTACK_SPEED:
					final short modifierASpeed = (short) modifier;
					stat.setBase(Math.round(modifierASpeed / 2));
					break;
				case EVASION:
					final short modifierEvasion = (short) modifier;
					stat.setBase(Math.round(modifierEvasion));
					break;
				case PARRY:
					final short modifierParry = (short) modifier;
					stat.setBase(Math.round(modifierParry));
					break;
				case BLOCK:
					final short modifierBlock = (short) modifier;
					stat.setBase(Math.round(modifierBlock));
					break;
				case PHYSICAL_CRITICAL:
					final short modifierPCrit = (short) modifier;
					stat.setBase(Math.round(modifierPCrit));
					break;
				case HIT_COUNT:
					final short modifierHCount = (short) modifier;
					stat.setBase(Math.round(modifierHCount));
					break;
				case ATTACK_RANGE:
					final float modifierARange = modifier;
					stat.setBase(Math.round(modifierARange));
					break;
				case MAGICAL_CRITICAL:
					final short modifierMCrit = (short) modifier;
					stat.setBase(Math.round(modifierMCrit));
					break;
				case CONCENTRATION:
					final short modifierConcentration = (short) modifier;
					stat.setBase(Math.round(modifierConcentration));
					break;
				case POISON_RESISTANCE:
					final short modifierPResist = (short) modifier;
					stat.setBase(Math.round(modifierPResist));
					break;
				case BLEED_RESISTANCE:
					final short modifierBResist = (short) modifier;
					stat.setBase(Math.round(modifierBResist));
					break;
				case PARALYZE_RESISTANCE:
					final short modifierPAResist = (short) modifier;
					stat.setBase(Math.round(modifierPAResist));
					break;
				case SLEEP_RESISTANCE:
					final short modifierSResist = (short) modifier;
					stat.setBase(Math.round(modifierSResist));
					break;
				case ROOT_RESISTANCE:
					final short modifierRResist = (short) modifier;
					stat.setBase(Math.round(modifierRResist));
					break;
				case BLIND_RESISTANCE:
					final short modifierBLResist = (short) modifier;
					stat.setBase(Math.round(modifierBLResist));
					break;
				case CHARM_RESISTANCE:
					final short modifierCResist = (short) modifier;
					stat.setBase(Math.round(modifierCResist));
					break;
				case DISEASE_RESISTANCE:
					final short modifierDResist = (short) modifier;
					stat.setBase(Math.round(modifierDResist));
					break;
				case SILENCE_RESISTANCE:
					final short modifierSIResist = (short) modifier;
					stat.setBase(Math.round(modifierSIResist));
					break;
				case FEAR_RESISTANCE:
					final short modifierFResist = (short) modifier;
					stat.setBase(Math.round(modifierFResist));
					break;
				case CURSE_RESISTANCE:
					final short modifierCUResist = (short) modifier;
					stat.setBase(Math.round(modifierCUResist));
					break;
				case CONFUSE_RESISTANCE:
					final short modifierCOResist = (short) modifier;
					stat.setBase(Math.round(modifierCOResist));
					break;
				case STUN_RESISTANCE:
					final short modifierSTResist = (short) modifier;
					stat.setBase(Math.round(modifierSTResist));
					break;
				case PERIFICATION_RESISTANCE:
					final short modifierPEResist = (short) modifier;
					stat.setBase(Math.round(modifierPEResist));
					break;
				case STUMBLE_RESISTANCE:
					final short modifierSTUResist = (short) modifier;
					stat.setBase(Math.round(modifierSTUResist));
					break;
				case STAGGER_RESISTANCE:
					final short modifierSTAResist = (short) modifier;
					stat.setBase(Math.round(modifierSTAResist));
					break;
				case OPENAREIAL_RESISTANCE:
					final short modifierOResist = (short) modifier;
					stat.setBase(Math.round(modifierOResist));
					break;
				case SNARE_RESISTANCE:
					final short modifierSNResist = (short) modifier;
					stat.setBase(Math.round(modifierSNResist));
					break;
				case SLOW_RESISTANCE:
					final short modifierSLResist = (short) modifier;
					stat.setBase(Math.round(modifierSLResist));
					break;
				case SPIN_RESISTANCE:
					final short modifierSPResist = (short) modifier;
					stat.setBase(Math.round(modifierSPResist));
					break;
				case POISON_RESISTANCE_PENETRATION:
					final short modifierPRP = (short) modifier;
					stat.setBase(Math.round(modifierPRP));
					break;
				case BLEED_RESISTANCE_PENETRATION:
					final short modifierBRP = (short) modifier;
					stat.setBase(Math.round(modifierBRP));
					break;
				case PARALYZE_RESISTANCE_PENETRATION:
					final short modifierPARP = (short) modifier;
					stat.setBase(Math.round(modifierPARP));
					break;
				case SLEEP_RESISTANCE_PENETRATION:
					final short modifierSRP = (short) modifier;
					stat.setBase(Math.round(modifierSRP));
					break;
				case ROOT_RESISTANCE_PENETRATION:
					final short modifierRRP = (short) modifier;
					stat.setBase(Math.round(modifierRRP));
					break;
				case BLIND_RESISTANCE_PENETRATION:
					final short modifierBLRP = (short) modifier;
					stat.setBase(Math.round(modifierBLRP));
					break;
				case CHARM_RESISTANCE_PENETRATION:
					final short modifierCRP = (short) modifier;
					stat.setBase(Math.round(modifierCRP));
					break;
				case DISEASE_RESISTANCE_PENETRATION:
					final short modifierDRP = (short) modifier;
					stat.setBase(Math.round(modifierDRP));
					break;
				case SILENCE_RESISTANCE_PENETRATION:
					final short modifierSIRP = (short) modifier;
					stat.setBase(Math.round(modifierSIRP));
					break;
				case FEAR_RESISTANCE_PENETRATION:
					final short modifierFRP = (short) modifier;
					stat.setBase(Math.round(modifierFRP));
					break;
				case CURSE_RESISTANCE_PENETRATION:
					final short modifierCURP = (short) modifier;
					stat.setBase(Math.round(modifierCURP));
					break;
				case CONFUSE_RESISTANCE_PENETRATION:
					final short modifierCORP = (short) modifier;
					stat.setBase(Math.round(modifierCORP));
					break;
				case STUN_RESISTANCE_PENETRATION:
					final short modifierSTRP = (short) modifier;
					stat.setBase(Math.round(modifierSTRP));
					break;
				case PERIFICATION_RESISTANCE_PENETRATION:
					final short modifierPERP = (short) modifier;
					stat.setBase(Math.round(modifierPERP));
					break;
				case STUMBLE_RESISTANCE_PENETRATION:
					final short modifierSTURP = (short) modifier;
					stat.setBase(Math.round(modifierSTURP));
					break;
				case STAGGER_RESISTANCE_PENETRATION:
					final short modifierSTARP = (short) modifier;
					stat.setBase(Math.round(modifierSTARP));
					break;
				case OPENAREIAL_RESISTANCE_PENETRATION:
					final short modifierORP = (short) modifier;
					stat.setBase(Math.round(modifierORP));
					break;
				case SNARE_RESISTANCE_PENETRATION:
					final short modifierSNRP = (short) modifier;
					stat.setBase(Math.round(modifierSNRP));
					break;
				case SLOW_RESISTANCE_PENETRATION:
					final short modifierSLRP = (short) modifier;
					stat.setBase(Math.round(modifierSLRP));
					break;
				case SPIN_RESISTANCE_PENETRATION:
					final short modifierSPRP = (short) modifier;
					stat.setBase(Math.round(modifierSPRP));
					break;
				case BOOST_MAGICAL_SKILL:
					final short modifierBMSkill = (short) modifier;
					stat.setBase(Math.round(modifierBMSkill));
					break;
				case MAGICAL_ACCURACY:
					final short modifierMAccuracy = (short) modifier;
					stat.setBase(Math.round(modifierMAccuracy));
					break;
				case BOOST_CASTING_TIME:
					final short modifierBCTime = (short) modifier;
					stat.setBase(Math.round(modifierBCTime));
					break;
				case HEAL_BOOST:
					final short modifierHBoost = (short) modifier;
					stat.setBase(Math.round(modifierHBoost));
					break;
				case PHYSICAL_CRITICAL_RESIST:
					final short modifierPCResist = (short) modifier;
					stat.setBase(Math.round(modifierPCResist));
					break;
				case MAGICAL_CRITICAL_RESIST:
					final short modifierMCResist = (short) modifier;
					stat.setBase(Math.round(modifierMCResist));
					break;
				case PHYSICAL_CRITICAL_DAMAGE_REDUCE:
					final short modifierPCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierPCDReduce));
					break;
				case MAGICAL_CRITICAL_DAMAGE_REDUCE:
					final short modifierMCDReduce = (short) modifier;
					stat.setBase(Math.round(modifierMCDReduce));
					break;
				case MAGICAL_DEFEND:
					stat.setBase(Math.round(modifier));
					break;
				case MAGIC_SKILL_BOOST_RESIST:
					final short modifierMSBResist = (short) modifier;
					stat.setBase(Math.round(modifierMSBResist));
					break;
				default:
					break;
			}
		}
		
		@Override
		public int getPriority()
		{
			return 60;
		}
	}
	
	@Override
	public void onFail(Player player, String msg)
	{
		PacketSendUtility.sendMessage(player, "Synax : //stat hp <value>");
		
	}
}
