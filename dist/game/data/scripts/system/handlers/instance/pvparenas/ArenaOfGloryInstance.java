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
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.PvPArenaPlayerReward;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author xTz
 */
@InstanceID(300550000)
public class ArenaOfGloryInstance extends PvPArenaInstance
{
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		killBonus = 1000;
		deathFine = -200;
		super.onInstanceCreate(instance);
	}
	
	@Override
	protected void reward()
	{
		final int totalPoints = instanceReward.getTotalPoints();
		final int size = instanceReward.getInstanceRewards().size();
		final float totalScoreAP = (1.0f * size) * 100;
		final float totalScoreGP = (1.0f * size) * 100;
		float rankingRate = 0;
		if (size > 1)
		{
			rankingRate = (0.077f * (4 - size));
		}
		final float totalRankingAP = 30800 - (30800 * rankingRate);
		final float totalRankingGP = 800 - (800 * rankingRate);
		for (InstancePlayerReward playerReward : instanceReward.getInstanceRewards())
		{
			final PvPArenaPlayerReward reward = (PvPArenaPlayerReward) playerReward;
			if (!reward.isRewarded())
			{
				float playerRate = 1;
				final Player player = instance.getPlayer(playerReward.getOwner());
				if (player != null)
				{
					playerRate = player.getRates().getGloryRewardRate();
				}
				final int score = reward.getScorePoints();
				final float scoreRate = ((float) score / (float) totalPoints);
				final int rank = instanceReward.getRank(score);
				final float percent = reward.getParticipation();
				final float generalRate = 0.167f + (rank * 0.227f);
				int basicAP = 100;
				int basicGP = 100;
				float rankingAP = totalRankingAP;
				float rankingGP = totalRankingGP;
				if (rank > 0)
				{
					rankingAP = rankingAP - (rankingAP * generalRate);
					rankingGP = rankingGP - (rankingGP * generalRate);
				}
				final int scoreAP = (int) (totalScoreAP * scoreRate);
				final int scoreGP = (int) (totalScoreGP * scoreRate);
				// <Abyss Points>
				basicAP *= percent;
				rankingAP *= percent;
				rankingAP *= playerRate;
				reward.setBasicAP(basicAP);
				reward.setRankingAP((int) rankingAP);
				reward.setScoreAP(scoreAP);
				// <Glory Points>
				basicGP *= percent;
				rankingGP *= percent;
				rankingGP *= playerRate;
				/*
				 * reward.setBasicGP(basicGP); reward.setRankingGP((int) rankingGP); reward.setScoreGP(scoreGP);
				 */
				reward.setBasicGP((int) (basicGP * 0.1));
				reward.setRankingGP((int) (rankingGP * 0.1));
				reward.setScoreGP((int) (scoreGP * 0.1));
				
				switch (rank)
				{
					case 0:
					{
						reward.setGloriousInsignia(1);
						reward.setMithrilMedal(5);
						break;
					}
					case 1:
					{
						reward.setGloriousInsignia(1);
						reward.setplatinumMedal(3);
						break;
					}
					case 2:
					{
						reward.setplatinumMedal(3);
						break;
					}
					case 3:
					{
						reward.setLifeSerum(1);
						break;
					}
				}
			}
		}
		super.reward();
	}
}