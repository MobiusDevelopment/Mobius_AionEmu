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

/**
 * @author Rinzler (Encom)
 */
@AIName("kaisinel")
public class KaisinelAI2 extends NpcAI2
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
				// It is finally over.
				sendMsg(1500695, getObjectId(), false, 3000);
				// Even a Dragon Lord can be driven to madness.
				sendMsg(1500696, getObjectId(), false, 6000);
				// No need to thank me. Now, give me the relics, and I will be gone.
				sendMsg(1500625, getObjectId(), false, 9000);
				// Yes ? And such a fine job you've done.
				sendMsg(1500626, getObjectId(), false, 12000);
				// How dare you raise your voice in this place ? Be silent.
				sendMsg(1500627, getObjectId(), false, 15000);
				// What do you think you're going to do with those ?
				sendMsg(1500630, getObjectId(), false, 18000);
				// The traitor was right about one thing. We must put our argument aside and go after him.
				sendMsg(1500631, getObjectId(), false, 21000);
				// Ah, Kahrun. Getting a little ambitious, don't you think ?
				sendMsg(1500632, getObjectId(), false, 24000);
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