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
package system.handlers.ai.instance.luckyDanuarReliquary;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.poll.AIAnswer;
import com.aionemu.gameserver.ai2.poll.AIAnswers;
import com.aionemu.gameserver.ai2.poll.AIQuestion;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("damage_circle")
public class Damage_CircleAI2 extends NpcAI2
{
	@Override
	protected void handleCreatureSee(Creature creature)
	{
		checkDistance(this, creature);
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
		checkDistance(this, creature);
	}
	
	private void checkDistance(NpcAI2 ai, Creature creature)
	{
		if (creature instanceof Player)
		{
			if ((getOwner().getNpcId() == 284447) && MathUtil.isIn3dRangeLimited(getOwner(), creature, 16, 50))
			{
				SkillEngine.getInstance().getSkill(getOwner(), 21263, 60, getOwner()).useNoAnimationSkill();
			}
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