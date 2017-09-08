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
package system.handlers.ai.instance.transidiumAnnex;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.NpcType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rinzler (Encom)
 */
@AIName("bulwark_shield")
public class Bulwark_ShieldAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		startShieldVulnerable();
	}
	
	/**
	 * Walls around "Ahserion" become vulnerable You can start to destroy walls between you and "Ahserion". You can say that after 30min the real battle begins.
	 */
	private void startShieldVulnerable()
	{
		final Npc GAb1SubNamedBarricadeDa65Ah = getPosition().getWorldMapInstance().getNpc(277231); // Bulwark Shield.
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				GAb1SubNamedBarricadeDa65Ah.setTarget(getOwner());
				GAb1SubNamedBarricadeDa65Ah.setNpcType(NpcType.ATTACKABLE);
				final WorldMapInstance instance = getPosition().getWorldMapInstance();
				for (Player player : instance.getPlayersInside())
				{
					if (MathUtil.isIn3dRange(player, GAb1SubNamedBarricadeDa65Ah, 5))
					{
						player.clearKnownlist();
						player.updateKnownlist();
					}
				}
				// The effect of the Transidium Annex has weakened the Bulwark Shield.
				PacketSendUtility.npcSendPacketTime(getOwner(), SM_SYSTEM_MESSAGE.STR_MSG_GAB1_SUB_ALARM_12, 0);
			}
		}, 1800000); // ...30 Minutes.
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}