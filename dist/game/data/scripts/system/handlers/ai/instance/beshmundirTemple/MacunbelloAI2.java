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
package system.handlers.ai.instance.beshmundirTemple;

import java.util.concurrent.atomic.AtomicBoolean;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.utils.PacketSendUtility;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("macunbello")
public class MacunbelloAI2 extends AggressiveNpcAI2
{
	private final boolean canThink = true;
	private final AtomicBoolean isAggred = new AtomicBoolean(false);
	private final AtomicBoolean isStartedEvent = new AtomicBoolean(false);
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		if (isAggred.compareAndSet(false, true))
		{
			getPosition().getWorldMapInstance().getDoors().get(467).setOpen(false);
		}
		checkPercentage(getLifeStats().getHpPercentage());
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if (hpPercentage <= 95)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startMacunbelloRightHandEvent();
			}
		}
		if (hpPercentage <= 75)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startMacunbelloRightHandEvent();
			}
		}
		if (hpPercentage <= 55)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startMacunbelloRightHandEvent();
			}
		}
		if (hpPercentage <= 35)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startMacunbelloRightHandEvent();
			}
		}
		if (hpPercentage <= 15)
		{
			if (isStartedEvent.compareAndSet(false, true))
			{
				startMacunbelloRightHandEvent();
			}
		}
	}
	
	private void rushMacunbello(Npc npc, float x, float y, float z, boolean despawn)
	{
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	private void startMacunbelloRightHandEvent()
	{
		rushMacunbello((Npc) spawn(281698, 1007.0455f, 109.74506f, 242.70596f, (byte) 30), 982.9454f, 148.17749f, 241.96664f, false);
		rushMacunbello((Npc) spawn(281698, 952.0972f, 109.84658f, 242.70837f, (byte) 30), 980.1799f, 148.76031f, 241.76593f, false);
	}
	
	@Override
	public boolean canThink()
	{
		return canThink;
	}
	
	@Override
	protected void handleBackHome()
	{
		isAggred.set(false);
		isStartedEvent.set(false);
		super.handleBackHome();
	}
	
	@Override
	protected void handleDespawned()
	{
		super.handleDespawned();
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
	}
}