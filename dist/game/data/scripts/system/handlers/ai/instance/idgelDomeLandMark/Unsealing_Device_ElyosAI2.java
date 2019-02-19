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
package system.handlers.ai.instance.idgelDomeLandMark;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;

import system.handlers.ai.ActionItemNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("Unsealing_Device_Elyos")
public class Unsealing_Device_ElyosAI2 extends ActionItemNpcAI2
{
	private boolean isRewarded;
	
	@Override
	protected void handleDialogStart(Player player)
	{
		final InstanceReward<?> instance = getPosition().getWorldMapInstance().getInstanceHandler().getInstanceReward();
		if ((instance != null) && !instance.isStartProgress())
		{
			return;
		}
		super.handleDialogStart(player);
	}
	
	@Override
	protected void handleUseItemFinish(Player player)
	{
		if (!isRewarded)
		{
			isRewarded = true;
			AI2Actions.deleteOwner(this);
			AI2Actions.handleUseItemFinish(this, player);
			switch (getNpcId())
			{
				case 806343: // Unsealing Device.
				{
					spawn(806327, 264.12469f, 273.46167f, 85.796768f, (byte) 0, 77);
					spawn(806279, 264.74783f, 259.22983f, 88.351212f, (byte) 0, 200);
					break;
				}
				case 806344: // Unsealing Device.
				{
					spawn(806279, 264.74783f, 259.22983f, 89.924179f, (byte) 0, 201);
					spawn(806328, 265.26395f, 245.08080f, 85.796768f, (byte) 0, 101);
					break;
				}
				case 806345: // Unsealing Device.
				{
					spawn(806279, 264.74783f, 259.22983f, 91.488808f, (byte) 0, 202);
					spawn(806329, 250.50882f, 258.67883f, 85.796768f, (byte) 0, 78);
					break;
				}
				case 806346: // Unsealing Device.
				{
					spawn(806330, 278.85132f, 259.72498f, 85.796768f, (byte) 0, 103);
					break;
				}
			}
		}
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}