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
package system.handlers.ai.instance.tiamatStronghold;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.services.NpcShoutsService;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("tiamateye")
public class TiamatEyeAI2 extends NpcAI2
{
	@Override
	public void think()
	{
	}
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				startLifeTask();
				// Insolent Daevas! Destroying my lieutenants!
				sendMsg(1500679, getObjectId(), false, 3000);
				// Laksyaka was useful to me. You'll see what happens to those who take away my tools.
				sendMsg(1500680, getObjectId(), false, 9000);
				// Whatever agony my lieutenants felt as they died, you will feel tenfold!
				sendMsg(1500681, getObjectId(), false, 15000);
				// Dear Dragon Lord, please rest in peace. Let me avenge you!
				sendMsg(1500682, getObjectId(), false, 21000);
			}
		}, 1000);
	}
	
	private void startLifeTask()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				spawn(730622, 1029.7988f, 267.03915f, 408.98651f, (byte) 0, 83); // Central Passage Teleport.
				AI2Actions.deleteOwner(TiamatEyeAI2.this);
			}
		}, 30000);
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
				return AIAnswers.NEGATIVE;
			case SHOULD_RESPAWN:
				return AIAnswers.NEGATIVE;
			case SHOULD_REWARD:
				return AIAnswers.NEGATIVE;
			default:
				return null;
		}
	}
}