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
package system.handlers.instance.pvparenas;

import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.instance.playerreward.HarmonyGroupReward;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300450000)
public class ArenaOfHarmonyInstance extends HarmonyArenaInstance
{
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		killBonus = 1000;
		deathFine = -150;
		super.onInstanceCreate(instance);
	}
	
	@Override
	protected void reward()
	{
		final float totalScoreAP = (1.0f * 3) * 100;
		final float totalScoreGP = (1.0f * 3) * 100;
		final float totalScoreCourage = (1.0f * 3) * 100;
		final float totalScoreInfinity = (1.0f * 3) * 100;
		final int totalPoints = instanceReward.getTotalPoints();
		for (HarmonyGroupReward group : instanceReward.getGroups())
		{
			final int score = group.getPoints();
			final int rank = instanceReward.getRank(score);
			final float percent = group.getParticipation();
			final float scoreRate = ((float) score / (float) totalPoints);
			int basicAP = 100;
			int rankingAP = 0;
			basicAP *= percent;
			int basicGP = 100;
			int rankingGP = 0;
			basicGP *= percent;
			int basicCoI = 0;
			int rankingCoI = 0;
			basicCoI *= percent;
			int basicCiI = 0;
			int rankingCiI = 0;
			basicCiI *= percent;
			final int scoreAP = (int) (totalScoreAP * scoreRate);
			final int scoreGP = (int) (totalScoreGP * scoreRate);
			switch (rank)
			{
				case 0:
				{
					rankingAP = 681;
					rankingGP = 481;
					rankingCoI = 49;
					rankingCiI = 49;
					group.setGloryTicket(1);
					break;
				}
				case 1:
				{
					rankingAP = 487;
					rankingGP = 287;
					rankingCoI = 20;
					rankingCiI = 20;
					break;
				}
				case 2:
				{
					rankingAP = 251;
					rankingGP = 151;
					rankingCoI = 1;
					rankingCiI = 1;
					break;
				}
			}
			rankingAP *= percent;
			rankingGP *= percent;
			rankingCoI *= percent;
			rankingCiI *= percent;
			final int scoreCoI = (int) (totalScoreCourage * scoreRate);
			final int scoreCiI = (int) (totalScoreInfinity * scoreRate);
			group.setBasicAP(basicAP);
			group.setRankingAP(rankingAP);
			group.setScoreAP(scoreAP);
			/*
			 * group.setBasicGP(basicGP); group.setRankingGP(rankingGP); group.setScoreGP(scoreGP);
			 */
			group.setBasicGP((int) (basicGP * 0.1));
			group.setRankingGP((int) (rankingGP * 0.1));
			group.setScoreGP((int) (scoreGP * 0.1));
			
			group.setBasicCourage(basicCoI);
			group.setRankingCourage(rankingCoI);
			group.setScoreCourage(scoreCoI);
			group.setBasicInfinity(basicCiI);
			group.setRankingInfinity(rankingCiI);
			group.setScoreInfinity(scoreCiI);
		}
		super.reward();
	}
}