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
package system.handlers.ai.instance.dragonLordRefuge;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.skillengine.SkillEngine;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("tiamat_woman_form")
public class Tiamat_Woman_FormAI2 extends AggressiveNpcAI2
{
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			startLifeTask();
			// Are you... threatening me? Is this what passes for a joke among you people?.
			sendMsg(1500613, getObjectId(), false, 3000);
			// Don't worry. The tragedy will be all yours.
			sendMsg(1500614, getObjectId(), false, 9000);
			// You will feel despair such as you have never felt!
			sendMsg(1500615, getObjectId(), false, 15000);
			// Before I let Calindi destroy you, I will show you a glimpse of your people's ruin.
			sendMsg(1500616, getObjectId(), false, 21000);
			// I... yes. We are grateful. I don't know what we'd have done without your help...
			sendMsg(1500617, getObjectId(), false, 27000);
			SkillEngine.getInstance().getSkill(getOwner(), 20917, 1, getOwner()).useNoAnimationSkill(); // Charge Siel's Relics.
		}, 1000);
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			spawn(219359, 470.5909f, 515.02856f, 417.40436f, (byte) 119); // Calindi.
			spawn(283174, 457.7215f, 514.4464f, 417.53998f, (byte) 0);
			AI2Actions.deleteOwner(Tiamat_Woman_FormAI2.this);
		}, 35000);
	}
	
	void sendMsg(int msg, int Obj, boolean isShout, int time)
	{
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
}