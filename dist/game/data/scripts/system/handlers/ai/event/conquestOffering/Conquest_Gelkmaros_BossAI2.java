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
package system.handlers.ai.event.conquestOffering;

import java.util.List;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.services.HTMLService;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.world.World;
import com.aionemu.gameserver.world.WorldPosition;

import system.handlers.ai.AggressiveNpcAI2;

/**
 * @author Rinzler (Encom)
 */
@AIName("conquest_gelkmaros")
public class Conquest_Gelkmaros_BossAI2 extends AggressiveNpcAI2
{
	@Override
	protected void handleSpawned()
	{
		super.handleSpawned();
		boostDefense();
	}
	
	@Override
	protected void handleDied()
	{
		final WorldPosition p = getPosition();
		if (p != null)
		{
			sendGuide();
		}
		switch (Rnd.get(1, 2))
		{
			case 1:
			{
				spawnSecretPortal();
				break;
			}
			case 2:
			{
				break;
			}
		}
		super.handleDied();
	}
	
	private void spawnSecretPortal()
	{
		final WorldPosition p = getPosition();
		if (p != null)
		{
			ThreadPoolManager.getInstance().schedule((Runnable) () -> spawn(833021, p.getX(), p.getY(), p.getZ(), (byte) 0), 15000);
			ThreadPoolManager.getInstance().schedule((Runnable) () -> despawnNpc(833021), 300000); // 5 Minutes.
		}
	}
	
	private void boostDefense()
	{
		SkillEngine.getInstance().getSkill(getOwner(), 21923, 1, getOwner()).useNoAnimationSkill(); // Boost Defense.
	}
	
	private void sendGuide()
	{
		World.getInstance().doOnAllPlayers(player ->
		{
			if (MathUtil.isIn3dRange(player, getOwner(), 15))
			{
				HTMLService.sendGuideHtml(player, "Conquest_Offering");
			}
		});
	}
	
	void despawnNpc(int npcId)
	{
		if (getPosition().getWorldMapInstance().getNpcs(npcId) != null)
		{
			final List<Npc> npcs = getPosition().getWorldMapInstance().getNpcs(npcId);
			for (Npc npc : npcs)
			{
				npc.getController().onDelete();
			}
		}
	}
}