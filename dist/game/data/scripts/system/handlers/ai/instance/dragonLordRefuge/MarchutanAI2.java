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

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.services.NpcShoutsService;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("marchutan")
public class MarchutanAI2 extends NpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				// Finally, Tiamat is dead.
				sendMsg(1500698, getObjectId(), false, 3000);
				// I saw its despair, before the end.
				sendMsg(1500699, getObjectId(), false, 6000);
				// You can thank me by giving me those relics, for safekeeping.
				sendMsg(1500633, getObjectId(), false, 9000);
				// That time is over, Kahrun.
				sendMsg(1500634, getObjectId(), false, 12000);
				// You didn't have the power to fight Tiamat, so what makes you think you can stop me ?
				sendMsg(1500635, getObjectId(), false, 15000);
				// How far do you think you'll get with those ?
				sendMsg(1500638, getObjectId(), false, 18000);
				// We must unite to stop him--but don't think that this argument is over!
				sendMsg(1500639, getObjectId(), false, 21000);
				// Well well. What's this ? Bickering ? And so soon after your glorious victory.
				sendMsg(1500640, getObjectId(), false, 24000);
			}
		}, 1000);
	}
	
	private void sendMsg(int msg, int Obj, boolean isShout, int time)
	{
		NpcShoutsService.getInstance().sendMsg(getPosition().getWorldMapInstance(), msg, Obj, isShout, 0, time);
	}
	
	@Override
	public boolean isMoveSupported()
	{
		return false;
	}
	
	@Override
	protected AIAnswer pollInstance(AIQuestion question)
	{
		switch (question)
		{
			case SHOULD_DECAY:
			{
				return AIAnswers.NEGATIVE;
			}
			case SHOULD_RESPAWN:
			{
				return AIAnswers.NEGATIVE;
			}
			case SHOULD_REWARD:
			{
				return AIAnswers.NEGATIVE;
			}
			default:
			{
				return null;
			}
		}
	}
}