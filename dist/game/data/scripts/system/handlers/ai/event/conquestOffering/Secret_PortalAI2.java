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
package system.handlers.ai.event.conquestOffering;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.TeleportAnimation;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.services.teleport.TeleportService2;

import system.handlers.ai.ActionItemNpcAI2;

/**
 * @author Author Rinzler (Encom)
 */
@AIName("secret_portal")
public class Secret_PortalAI2 extends ActionItemNpcAI2
{
	@Override
	protected void handleUseItemFinish(Player player)
	{
		switch (getNpcId())
		{
			case 833018: // Secret Portal.
			{
				switch (player.getWorldId())
				{
					case 210050000: // Inggison.
					{
						switch (Rnd.get(1, 13))
						{
							case 1:
							{
								TeleportService2.teleportTo(player, 210050000, 1814.2759f, 242.06152f, 521.8501f, (byte) 10, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 2:
							{
								TeleportService2.teleportTo(player, 210050000, 2331.7358f, 487.32224f, 431.90802f, (byte) 14, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 3:
							{
								TeleportService2.teleportTo(player, 210050000, 2070.4924f, 205.9982f, 490.71933f, (byte) 31, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 4:
							{
								TeleportService2.teleportTo(player, 210050000, 2605.1113f, 1318.8029f, 330.1698f, (byte) 74, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 5:
							{
								TeleportService2.teleportTo(player, 210050000, 2240.141f, 2092.4824f, 58.125f, (byte) 65, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 6:
							{
								TeleportService2.teleportTo(player, 210050000, 191.14435f, 474.06177f, 577.7558f, (byte) 15, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 7:
							{
								TeleportService2.teleportTo(player, 210050000, 762.1146f, 232.81108f, 541.2699f, (byte) 66, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 8:
							{
								TeleportService2.teleportTo(player, 210050000, 146.30334f, 136.93912f, 558.5093f, (byte) 98, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 9:
							{
								TeleportService2.teleportTo(player, 210050000, 321.7804f, 82.18708f, 499.76416f, (byte) 115, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 10:
							{
								TeleportService2.teleportTo(player, 210050000, 1429.834f, 1745.4474f, 162.54492f, (byte) 62, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 11:
							{
								TeleportService2.teleportTo(player, 210050000, 822.2454f, 1047.0043f, 213.04636f, (byte) 13, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 12:
							{
								TeleportService2.teleportTo(player, 210050000, 1602.6942f, 1583.8927f, 168.625f, (byte) 37, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 13:
							{
								TeleportService2.teleportTo(player, 210050000, 1738.6703f, 1154.055f, 393.07278f, (byte) 20, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
						}
						break;
					}
				}
				break;
			}
			case 833021: // Secret Portal.
			{
				switch (player.getWorldId())
				{
					case 220070000: // Gelkmaros.
					{
						switch (Rnd.get(1, 13))
						{
							case 1:
							{
								TeleportService2.teleportTo(player, 220070000, 519.70667f, 1843.6685f, 362.52618f, (byte) 86, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 2:
							{
								TeleportService2.teleportTo(player, 220070000, 304.01727f, 1757.0371f, 353.4341f, (byte) 33, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 3:
							{
								TeleportService2.teleportTo(player, 220070000, 804.1618f, 1981.9644f, 326.0f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 4:
							{
								TeleportService2.teleportTo(player, 220070000, 907.40564f, 1383.379f, 51.78881f, (byte) 3, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 5:
							{
								TeleportService2.teleportTo(player, 220070000, 740.72034f, 1366.3945f, 277.81967f, (byte) 90, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 6:
							{
								TeleportService2.teleportTo(player, 220070000, 451.3702f, 1453.679f, 283.41513f, (byte) 101, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 7:
							{
								TeleportService2.teleportTo(player, 220070000, 1103.892f, 1272.3827f, 280.87134f, (byte) 108, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 8:
							{
								TeleportService2.teleportTo(player, 220070000, 571.23206f, 1541.0194f, 277.125f, (byte) 82, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 9:
							{
								TeleportService2.teleportTo(player, 220070000, 2351.7795f, 673.98047f, 142.015f, (byte) 115, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 10:
							{
								TeleportService2.teleportTo(player, 220070000, 2656.8284f, 666.3557f, 141.40376f, (byte) 62, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 11:
							{
								TeleportService2.teleportTo(player, 220070000, 2179.4883f, 1163.6583f, 206.09595f, (byte) 13, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 12:
							{
								TeleportService2.teleportTo(player, 220070000, 2118.3875f, 1099.9666f, 300.2773f, (byte) 37, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
							case 13:
							{
								TeleportService2.teleportTo(player, 220070000, 391.5f, 1988.5851f, 2281.3125f, (byte) 20, TeleportAnimation.BEAM_ANIMATION);
								break;
							}
						}
						break;
					}
				}
				break;
			}
		}
	}
}