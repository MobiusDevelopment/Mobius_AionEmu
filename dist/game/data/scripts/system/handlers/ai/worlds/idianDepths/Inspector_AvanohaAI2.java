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
package system.handlers.ai.worlds.idianDepths;

import java.util.ArrayList;
import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("inspector_avanoha")
public class Inspector_AvanohaAI2 extends AggressiveNpcAI2
{
	private int stage = 0;
	private boolean isStart = false;
	
	@Override
	protected void handleCreatureAggro(Creature creature)
	{
		super.handleCreatureAggro(creature);
		wakeUp();
	}
	
	@Override
	protected void handleAttack(Creature creature)
	{
		super.handleAttack(creature);
		checkPercentage(getLifeStats().getHpPercentage());
		wakeUp();
	}
	
	private void wakeUp()
	{
		isStart = true;
	}
	
	private void checkPercentage(int hpPercentage)
	{
		if ((hpPercentage <= 90) && (stage < 1))
		{
			stage1();
			stage = 1;
		}
		if ((hpPercentage <= 50) && (stage < 2))
		{
			stage2();
			stage = 2;
		}
		if ((hpPercentage <= 20) && (stage < 3))
		{
			stage3();
			stage = 3;
		}
	}
	
	private void stage1()
	{
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		SkillEngine.getInstance().getSkill(getOwner(), 21135, 60, getOwner()).useNoAnimationSkill(); // Beritra's Favor.
	}
	
	private void stage2()
	{
		final int delay = 35000;
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		skill();
		scheduleDelayStage2(delay);
	}
	
	private void skill()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 18158, 100, getOwner()).useNoAnimationSkill(); // Wrathful Venom Burst.
		ThreadPoolManager.getInstance().schedule((Runnable) () -> SkillEngine.getInstance().getSkill(getOwner(), 18160, 100, getOwner()).useNoAnimationSkill(), 4000);
	}
	
	private void scheduleDelayStage2(int delay)
	{
		if (!isStart && !isAlreadyDead())
		{
			return;
		}
		ThreadPoolManager.getInstance().schedule((Runnable) () -> stage2(), delay);
	}
	
	private void stage3()
	{
		final int delay = 15000;
		if (isAlreadyDead() || !isStart)
		{
			return;
		}
		scheduleDelayStage3(delay);
	}
	
	private void scheduleDelayStage3(int delay)
	{
		if (!isStart && !isAlreadyDead())
		{
			return;
		}
		ThreadPoolManager.getInstance().schedule((Runnable) () ->
		{
			getRandomTarget();
			stage3();
		}, delay);
	}
	
	void getRandomTarget()
	{
		final List<Player> players = new ArrayList<>();
		for (Player player : getKnownList().getKnownPlayers().values())
		{
			if (!CreatureActions.isAlreadyDead(player) && MathUtil.isIn3dRange(player, getOwner(), 16))
			{
				players.add(player);
			}
		}
		if (players.isEmpty())
		{
			return;
		}
		getAggroList().clear();
		getAggroList().startHate(players.get(Rnd.get(0, players.size() - 1)));
	}
	
	@Override
	protected void handleBackHome()
	{
		super.handleBackHome();
		isStart = false;
		stage = 0;
	}
	
	@Override
	protected void handleDied()
	{
		super.handleDied();
		isStart = false;
		stage = 0;
	}
}