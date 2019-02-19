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
package system.handlers.ai.instance.empyreanCrucible;

import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.GeneralNpcAI2;

/**
 * @author xTz
 */
@AIName("arminos_draky")
public class ArminosDrakyAI2 extends GeneralNpcAI2
{
	private final String walkerId = "300300001";
	private boolean isStart = true;
	
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		getSpawnTemplate().setWalkerId(walkerId);
		WalkManager.startWalking(this);
		getOwner().setState(1);
		PacketSendUtility.broadcastPacket(getOwner(), new SM_EMOTION(getOwner(), EmotionType.START_EMOTE2, 0, getObjectId()));
	}
	
	@Override
	protected void handleMoveArrived()
	{
		final int point = getOwner().getMoveController().getCurrentPoint();
		super.handleMoveArrived();
		if (point == 15)
		{
			if (!isStart)
			{
				getSpawnTemplate().setWalkerId(null);
				WalkManager.stopWalking(this);
				AI2Actions.deleteOwner(this);
			}
			else
			{
				isStart = false;
			}
		}
	}
}