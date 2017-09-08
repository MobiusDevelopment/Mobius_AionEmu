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
package system.handlers.ai.instance.anguishedDragonLordRefuge;

import java.util.concurrent.Future;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.ThreadPoolManager;

/**
 * @author Rinzler (Encom)
 */
@AIName("kalrindy_surkana")
public class KalrindySurkanaAI2 extends NpcAI2
{
	private Future<?> skillTask;
	Npc kalrindy;
	
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		kalrindy = getPosition().getWorldMapInstance().getNpc(236274);
		reflect();
	}
	
	private void reflect()
	{
		skillTask = ThreadPoolManager.getInstance().scheduleAtFixedRate(new Runnable()
		{
			@Override
			public void run()
			{
				SkillEngine.getInstance().applyEffectDirectly(20891, getOwner(), kalrindy, 0);
			}
		}, 3000, 10000);
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
		if ((skillTask != null) && !skillTask.isCancelled())
		{
			skillTask.cancel(true);
		}
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