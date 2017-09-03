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
package system.handlers.ai.worlds.inggison;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.VisibleObject;

import system.handlers.ai.AggressiveNpcAI2;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("omega_clone")
public class Clone_Of_BarrierAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleDied()
	{
		for (VisibleObject object : getKnownList().getKnownObjects().values())
		{
			if (object instanceof Npc)
			{
				final Npc npc = (Npc) object;
				if (npc.getNpcId() == 216516)
				{ // Omega.
					npc.getEffectController().removeEffect(18671); // Magic Ward.
					break;
				}
			}
		}
		super.handleDied();
	}
}