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
package system.handlers.ai.siege;

import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AISummon;
import com.aionemu.gameserver.controllers.SiegeWeaponController;
import com.aionemu.gameserver.dataholders.DataManager;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.summons.SummonMode;
import com.aionemu.gameserver.model.templates.npcskill.NpcSkillTemplate;
import com.aionemu.gameserver.model.templates.npcskill.NpcSkillTemplates;
import com.aionemu.gameserver.services.summons.SummonsService;

/**
 * @author xTz
 */
@AIName("siege_weapon")
public class SiegeWeaponAI2 extends AISummon
{
	private long lastAttackTime;
	private int skill;
	private int skillLvl;
	private int duration;
	
	@Override
	protected void handleSpawned()
	{
		setStateIfNot(AIState.IDLE);
		SummonsService.doMode(SummonMode.GUARD, getOwner());
		final NpcSkillTemplate skillTemplate = getNpcSkillTemplates().getNpcSkills().get(0);
		skill = skillTemplate.getSkillid();
		skillLvl = skillTemplate.getSkillLevel();
		duration = DataManager.SKILL_DATA.getSkillTemplate(skill).getDuration();
	}
	
	@Override
	protected void handleFollowMe(Creature creature)
	{
		setStateIfNot(AIState.FOLLOWING);
	}
	
	@Override
	protected void handleCreatureMoved(Creature creature)
	{
	}
	
	@Override
	protected void handleStopFollowMe(Creature creature)
	{
		setStateIfNot(AIState.IDLE);
		getOwner().getMoveController().abortMove();
	}
	
	@Override
	protected void handleTargetTooFar()
	{
		getOwner().getMoveController().moveToDestination();
	}
	
	@Override
	protected void handleMoveArrived()
	{
		getOwner().getController().onMove();
		getOwner().getMoveController().abortMove();
	}
	
	@Override
	protected void handleMoveValidate()
	{
		getOwner().getController().onMove();
		getMoveController().moveToTargetObject();
	}
	
	@Override
	protected SiegeWeaponController getController()
	{
		return (SiegeWeaponController) super.getController();
	}
	
	private NpcSkillTemplates getNpcSkillTemplates()
	{
		return getController().getNpcSkillTemplates();
	}
	
	@Override
	protected void handleAttack(Creature creature)
	{
		if (creature == null)
		{
			return;
		}
		final Race race = creature.getRace();
		final Player master = getOwner().getMaster();
		if (master == null)
		{
			return;
		}
		final Race masterRace = master.getRace();
		if (masterRace.equals(Race.ASMODIANS) && !race.equals(Race.PC_LIGHT_CASTLE_DOOR) && !race.equals(Race.DRAGON_CASTLE_DOOR))
		{
			return;
		}
		else if (masterRace.equals(Race.ELYOS) && !race.equals(Race.PC_DARK_CASTLE_DOOR) && !race.equals(Race.DRAGON_CASTLE_DOOR))
		{
			return;
		}
		if (!getOwner().getMode().equals(SummonMode.ATTACK))
		{
			return;
		}
		if ((System.currentTimeMillis() - lastAttackTime) > (duration + 2000))
		{
			lastAttackTime = System.currentTimeMillis();
			getOwner().getController().useSkill(skill, skillLvl);
		}
	}
}