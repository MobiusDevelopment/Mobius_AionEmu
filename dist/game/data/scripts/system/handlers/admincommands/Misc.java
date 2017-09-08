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

import java.util.Iterator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUEST_ACTION;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.effect.AbnormalState;
import com.aionemu.gameserver.skillengine.model.Skill;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.utils.chathandlers.AdminCommand;
import com.aionemu.gameserver.world.World;

/**
 * Created by Kill3r
 */
public class Misc extends AdminCommand
{
	
	public Misc()
	{
		super("misc");
	}
	
	private static final Logger log = LoggerFactory.getLogger("GM_MONITOR_LOG");
	
	public int knockdown;
	
	@Override
	public void execute(Player admin, String... params)
	{
		if (params.length == 0)
		{
			onFail(admin, "");
			return;
		}
		if (params[0].equalsIgnoreCase("worldchat"))
		{
			final Configure config = new Configure();
			final String set = "set", property = "custom", variable = "FACTION_CMD_CHANNEL";
			String button = "idle";
			if (params[1].equalsIgnoreCase("enable"))
			{
				button = "true";
				PacketSendUtility.sendMessage(admin, "You've successfully Enabled the World Chat :)");
			}
			if (params[1].equalsIgnoreCase("disable"))
			{
				button = "false";
				PacketSendUtility.sendMessage(admin, "You've successfully Disabled the World Chat :)");
			}
			if (!button.equals("idle"))
			{
				config.execute(admin, set, property, variable, button);
			}
			log.info("[misc-worldchat] GM : " + admin.getName() + " has changed the worldchat config to [" + button + "]");
			return;
		}
		
		if (params[0].equalsIgnoreCase("timer"))
		{
			if (params[1].equalsIgnoreCase("stop"))
			{
				PacketSendUtility.sendPacket(admin, new SM_QUEST_ACTION(0, 0));
				final Iterator<Player> ita = World.getInstance().getPlayersIterator();
				while (ita.hasNext())
				{
					final Player p1 = ita.next();
					if (MathUtil.isInRange(admin, p1, 100))
					{
						PacketSendUtility.sendPacket(p1, new SM_QUEST_ACTION(0, 0));
					}
				}
				return;
			}
			final int time = Integer.parseInt(params[1]);
			PacketSendUtility.sendPacket(admin, new SM_QUEST_ACTION(0, time));
			final Iterator<Player> ita = World.getInstance().getPlayersIterator();
			while (ita.hasNext())
			{
				final Player p1 = ita.next();
				if (MathUtil.isInRange(admin, p1, 100))
				{
					PacketSendUtility.sendPacket(p1, new SM_QUEST_ACTION(0, time));
				}
			}
			log.info("[misc-timer] GM : " + admin.getName() + " has used the timer parameter in mapId '" + admin.getWorldId() + "'");
		}
		
		if (params[0].equals("slap"))
		{
			final Player player = World.getInstance().findPlayer(params[1]);
			
			if (knockdown == 10)
			{
				player.getEffectController().setAbnormal(AbnormalState.PARALYZE.getId());
				// SkillEngine.getInstance().applyEffectDirectly(8634, admin, player, 5000);
				final Skill skill = SkillEngine.getInstance().getSkill(admin, 8634, 1, player);
				SkillEngine.getInstance().applyEffectDirectly(18147, admin, player, 1);
				skill.useNoAnimationSkill();
				player.getEffectController().updatePlayerEffectIcons();
				player.getEffectController().broadCastEffects();
				PacketSendUtility.sendMessage(player, "You got slapped soo hard, that you got knocked down!!");
				startTask(player, AbnormalState.PARALYZE, 5000);
				knockdown = 0;
			}
			else
			{
				player.getEffectController().setAbnormal(AbnormalState.KNOCKBACK.getId());
				startTask(player, AbnormalState.KNOCKBACK, 1000);
				SkillEngine.getInstance().applyEffectDirectly(18147, admin, player, 1);
				player.getEffectController().updatePlayerEffectIcons();
				player.getEffectController().broadCastEffects();
				knockdown += 1;
			}
			PacketSendUtility.sendBrightYellowMessageOnCenter(player, getRandomMsg("slap"));
			PacketSendUtility.sendMessage(player, "GM " + admin.getName() + " just slapped you!");
			PacketSendUtility.sendMessage(admin, "You have just slapped the player!");
			return;
		}
		
		if (params[0].equalsIgnoreCase("kick"))
		{
			final Player player = World.getInstance().findPlayer(params[1]);
			
			PacketSendUtility.sendBrightYellowMessageOnCenter(player, getRandomMsg("kick"));
			PacketSendUtility.sendMessage(player, "GM " + admin.getName() + " has just kicked you on the face!!");
			PacketSendUtility.sendMessage(admin, "You just kicked on the player!!");
			
			SkillEngine.getInstance().applyEffectDirectly(18147, admin, player, 1);
			return;
		}
		
	}
	
	public String getRandomMsg(String state)
	{
		String msg = "";
		final int rng = Rnd.get(1, 5);
		if (state.equalsIgnoreCase("kick"))
		{
			switch (rng)
			{
				case 1:
				{
					msg = "You've been kicked on the face by someone!!";
					break;
				}
				case 2:
				{
					msg = "Knock Down Bish!! :D";
					break;
				}
				case 3:
				{
					msg = "Stay down, will ya!!!";
					break;
				}
				case 4:
				{
					msg = "You better stay like that *.*";
					break;
				}
				case 5:
				{
					msg = "Thats much better!!";
					break;
				}
			}
		}
		else if (state.equalsIgnoreCase("slap"))
		{
			switch (rng)
			{
				case 1:
				{
					msg = "How dare you!! :@";
					break;
				}
				case 2:
				{
					msg = "In your face!!";
					break;
				}
				case 3:
				{
					msg = "Slaaaaaaaaaap!!";
					break;
				}
				case 4:
				{
					msg = "Slap slap slap slooopp!!";
					break;
				}
				case 5:
				{
					msg = "Monster Slaaapp!!!";
					break;
				}
			}
		}
		return msg;
	}
	
	public void startTask(Player player, AbnormalState state, int delay)
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				player.getEffectController().unsetAbnormal(state.getId());
				player.getEffectController().updatePlayerEffectIcons();
				player.getEffectController().broadCastEffects();
			}
		}, delay);
	}
	
	@Override
	public void onFail(Player player, String message)
	{
		PacketSendUtility.sendMessage(player, "Synax : //misc worldchat <enable | disable>\n" + "Synax : //misc timer <stop | <second>>\n" + "Synax : //misc slap <playerName> - Slaps the player and he loses couple of HP's.\n" + "Synax : //misc kick <playerName> - Kicks on the player so hard, he falls down.");
	}
}
