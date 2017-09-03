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
package system.handlers.instance;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.commons.lang.mutable.MutableInt;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.ai2.manager.WalkManager;
import com.aionemu.gameserver.configs.main.GroupConfig;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.actions.CreatureActions;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.gameobjects.player.RewardType;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.KamarBattlefieldReward;
import com.aionemu.gameserver.model.instance.playerreward.InstancePlayerReward;
import com.aionemu.gameserver.model.instance.playerreward.KamarBattlefieldPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_ATTACK_STATUS;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.questEngine.QuestEngine;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.services.AutoGroupService;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.skillengine.model.DispelCategoryType;
import com.aionemu.gameserver.skillengine.model.Effect;
import com.aionemu.gameserver.utils.MathUtil;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;
import com.aionemu.gameserver.world.knownlist.Visitor;

import javolution.util.FastList;

/**
 * Author Rinzler (Encom) 激戦地カマルは12 vs 12の戦場！ 12 vs 12の合計24人が参加した戦場で、激戦地ドレドギオンの12人バージョンともいえる場所となる。 相手陣営を撃退、ネームドの暗殺、スコアオブジェクト取得などを通じてポイントを獲得することができ、最終的なスコアが高い陣営が勝利となる。 - 12 VS 12、合計24人が参加できる戦場。 - 相手陣営を撃退、ネームド暗殺、スコアオブジェクト取得などを通じてポイントを獲得することができる。 - 制限時間30分経過後、最終的なスコアが高い陣営が勝利する。 -
 * 日、月、水、土曜日の午後8時～10時に各曜日1回 - レベル61～65で種族ごとに12名ずつ入場可能 背景ストーリー 数々の逆境を乗り越えた煉族の輝かしい歴史を代弁してきた花の都カマルは、カルンが失踪した後に状況が急激に変化した。 束縛の強さはゆるくなり、天族と魔族はカマルに一時的に指揮官を残して少しでも隙が見えたら、お互いを除去し、カマロを得ようとする野心を見せた。 煉族、特にカルノンは、千魔族の緊張状態をおさえるしようとしたが、それらを拘束する力が弱くなったので、もはや通用しなかった。 一方、このようなカマルの事情を看破したベリトラは、部隊をカマルに投入するよう指示した。
 * その部隊はカマルで最もよく知られているドレドギオン軍団長であった。このような龍族の陰謀と来るべきリスクを知らずに魔族と天族は、それぞれの立場に真っ向から対立した。 その中、襲撃を開始した竜族は、突撃隊司令官ヴァルガの指揮の下、ついにカマル入りに成功する。 侵入されたカマルは、これまでの平和が無情にも燃え上がり凄惨な戦場に変わってしまう。 入場方法 - 定められた時間の間だけ入場することができる。 - 決まった時間になると、ドレドギオンと同様に、画面の右下に入場ボタンが有効になる。 - パーティーやフォースを事前に設定して入場申請をすることも可能であり、個人的での参加ももちろん可能である。
 * - 入場をすると、全サーバーを対象にランダムにマッチングされる。
 **/
@InstanceID(301120000)
public class KamarBattlefieldInstance extends GeneralInstanceHandler
{
	private long instanceTime;
	private Race RaceKilledVarga = null;
	private Map<Integer, StaticDoor> doors;
	protected KamarBattlefieldReward kamarBattlefieldReward;
	private float loosingGroupMultiplier = 1;
	private boolean isInstanceDestroyed = false;
	protected AtomicBoolean isInstanceStarted = new AtomicBoolean(false);
	private final FastList<Future<?>> kamarTask = FastList.newInstance();
	
	protected KamarBattlefieldPlayerReward getPlayerReward(Player player)
	{
		kamarBattlefieldReward.regPlayerReward(player);
		return kamarBattlefieldReward.getPlayerReward(player.getObjectId());
	}
	
	private boolean containPlayer(Integer object)
	{
		return kamarBattlefieldReward.containPlayer(object);
	}
	
	protected void startInstanceTask()
	{
		instanceTime = System.currentTimeMillis();
		kamarBattlefieldReward.setInstanceStartTime();
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!kamarBattlefieldReward.isRewarded())
				{
					openFirstDoors();
					// The member recruitment window has passed. You cannot recruit any more members.
					sendMsgByRace(1401181, Race.PC_ALL, 5000);
					kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
					startInstancePacket();
					kamarBattlefieldReward.sendPacket(4, null);
				}
			}
		}, 90000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(false);
				kamarBattlefieldReward.sendPacket(4, null);
				// A Cannon has arrived in Peace Square.
				sendMsgByRace(1401841, Race.PC_ALL, 0);
				// Kamar Cannon.
				sp(701806, 1364.5979f, 1467.5867f, 599.7256f, (byte) 104, 0);
				sp(701902, 1262.3992f, 1609.1414f, 585.90643f, (byte) 53, 0);
				// Kamar Cannon Flag.
				sp(801960, 1364.5979f, 1467.5867f, 599.7256f, (byte) 104, 0);
				sp(801961, 1262.3992f, 1609.1414f, 585.90643f, (byte) 53, 0);
			}
		}, 110000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(false);
				kamarBattlefieldReward.sendPacket(4, null);
				// Reian Tribe supplies have been deposited in Peace Square.
				sendMsgByRace(1401840, Race.PC_ALL, 0);
				sp(701906, 1371.4758f, 1549.8353f, 595.35071f, (byte) 0, 65); // Reian Supply Items.
				sp(701907, 1356.1837f, 1479.2998f, 593.80170f, (byte) 0, 66); // Reian Supply Items.
				sp(701908, 1353.0874f, 1413.4635f, 598.66101f, (byte) 0, 68); // Reian Supply Items.
			}
		}, 220000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(false);
				kamarBattlefieldReward.sendPacket(4, null);
				// Teleport Statues have appeared at the entrance to Kamar and the boarding site.
				sendMsgByRace(1401913, Race.PC_ALL, 0);
				// Kamena Development Zone Teleport Statue.
				sp(801774, 1559.2257f, 1409.8746f, 596.60065f, (byte) 0, 215);
				// Kahrun Guard Headquarters Teleport Statue.
				sp(801775, 1172.0404f, 1640.7632f, 599.26404f, (byte) 0, 216);
				// Siel's Spear Headquarters Teleport Statue.
				sp(801776, 1308.8353f, 1704.7883f, 599.26404f, (byte) 0, 213);
				// Kamar Entrance Teleport Statue.
				sp(802016, 1440.3145f, 1227.4073f, 585.78650f, (byte) 0, 223);
				sp(802017, 1109.5887f, 1532.7554f, 585.05902f, (byte) 0, 221);
				// Griffoen Boarding Site Teleport Statue.
				sp(802018, 1213.4902f, 1363.4617f, 612.36188f, (byte) 0, 225);
				// Habrok Boarding Site Teleport Statue.
				sp(802019, 1527.2150f, 1561.5153f, 611.90063f, (byte) 0, 224);
			}
		}, 300000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(false);
				kamarBattlefieldReward.sendPacket(4, null);
				// Reinforcements for the Elyos and Asmodians have arrived.
				sendMsgByRace(1401847, Race.PC_ALL, 0);
				switch (Rnd.get(1, 2))
				{
					case 1:
						sp(233327, 1237.7656f, 1660.9849f, 585.3441f, (byte) 42, 0); // Acting Commander Crispin.
						sp(801957, 1237.7656f, 1660.9849f, 585.3441f, (byte) 42, 0); // Acting Commander Crispin Flag.
						break;
					case 2:
						sp(233329, 1364.8312f, 1425.156f, 598.875f, (byte) 10, 0); // Acting Commander Crispin.
						sp(801957, 1364.8312f, 1425.156f, 598.875f, (byte) 10, 0); // Acting Commander Crispin Flag.
						break;
				}
				switch (Rnd.get(1, 2))
				{
					case 1:
						sp(233328, 1382.1552f, 1443.0217f, 599.3814f, (byte) 2, 0); // Acting Commander Tepes.
						sp(801958, 1382.1552f, 1443.0217f, 599.3814f, (byte) 2, 0); // Acting Commander Tepes Flag.
						break;
					case 2:
						sp(233330, 1235.9894f, 1662.9991f, 585.3441f, (byte) 107, 0); // Acting Commander Tepes.
						sp(801958, 1235.9894f, 1662.9991f, 585.3441f, (byte) 107, 0); // Acting Commander Tepes Flag.
						break;
				}
			}
		}, 600000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				sendPacket(false);
				kamarBattlefieldReward.sendPacket(4, null);
				// The Dredgion has appeared.
				sendMsgByRace(1401842, Race.PC_ALL, 0);
				// The Dredgion is disgorging a massive number of troops.
				sendMsgByRace(1401843, Race.PC_ALL, 5000);
				// Commander Varga and his Deputy have arrived at the battle.
				sendMsgByRace(1401844, Race.PC_ALL, 10000);
				switch (Rnd.get(1, 3))
				{
					case 1:
						sp(233321, 1453.465f, 1347.8022f, 606.12854f, (byte) 42, 0); // General Varga.
						sp(801956, 1453.465f, 1347.8022f, 606.12854f, (byte) 42, 0); // General Varga Flag.
						sp(233324, 1437.8099f, 1368.8099f, 600.8967f, (byte) 41, 0); // Varga Assault Commander.
						break;
					case 2:
						sp(233322, 1432.4172f, 1620.6938f, 599.9493f, (byte) 73, 0); // General Varga.
						sp(801956, 1432.4172f, 1620.6938f, 599.9493f, (byte) 73, 0); // General Varga Flag.
						sp(233325, 1418.108f, 1610.5255f, 599.9493f, (byte) 71, 0); // Varga Assault Commander.
						break;
					case 3:
						sp(233323, 1181.3792f, 1428.9828f, 586.5563f, (byte) 40, 0); // General Varga.
						sp(801956, 1181.3792f, 1428.9828f, 586.5563f, (byte) 40, 0); // General Varga Flag.
						sp(233326, 1169.3397f, 1459.3201f, 586.5563f, (byte) 42, 0); // Varga Assault Commander.
						break;
				}
			}
		}, 900000));
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!kamarBattlefieldReward.isRewarded())
				{
					final Race winnerRace = kamarBattlefieldReward.getWinnerRaceByScore();
					stopInstance(winnerRace);
				}
			}
		}, 1800000));
	}
	
	protected void stopInstance(Race race)
	{
		stopInstanceTask();
		kamarBattlefieldReward.setWinnerRace(race);
		kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		reward();
		kamarBattlefieldReward.sendPacket(5, null);
	}
	
	@Override
	public void onEnterInstance(final Player player)
	{
		if (!containPlayer(player.getObjectId()))
		{
			kamarBattlefieldReward.regPlayerReward(player);
		}
		sendEnterPacket(player);
	}
	
	private void sendEnterPacket(final Player player)
	{
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player opponent)
			{
				if (player.getRace() != opponent.getRace())
				{
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), player.getObjectId()));
				}
				else
				{
					PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), opponent.getObjectId()));
					if (player.getObjectId() != opponent.getObjectId())
					{
						PacketSendUtility.sendPacket(opponent, new SM_INSTANCE_SCORE(3, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
					}
				}
			}
		});
		sendPacket(true);
		sendPacket(false);
		PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(4, getTime(), getInstanceReward(), player.getObjectId(), 20, 0));
	}
	
	private void startInstancePacket()
	{
		instance.doOnAllPlayers(new Visitor<Player>()
		{
			@Override
			public void visit(Player player)
			{
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside(), true));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(3, getTime(), kamarBattlefieldReward, player.getObjectId(), 0, 0));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside(), true));
				PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(11, getTime(), getInstanceReward(), player.getObjectId()));
			}
		});
	}
	
	private void sendPacket(boolean isObjects)
	{
		if (isObjects)
		{
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(6, getTime(), kamarBattlefieldReward, instance.getPlayersInside(), true));
				}
			});
		}
		else
		{
			instance.doOnAllPlayers(new Visitor<Player>()
			{
				@Override
				public void visit(Player player)
				{
					PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(7, getTime(), kamarBattlefieldReward, instance.getPlayersInside(), true));
				}
			});
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		kamarBattlefieldReward = new KamarBattlefieldReward(mapId, instanceId, instance);
		kamarBattlefieldReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
		switch (Rnd.get(1, 7))
		{
			case 1:
				spawn(801903, 1184.5172f, 1408.1493f, 586.6199f, (byte) 5);
				break;
			case 2:
				spawn(801903, 1539.1635f, 1404.6685f, 596.6355f, (byte) 41);
				break;
			case 3:
				spawn(801903, 1138.9729f, 1619.5848f, 598.43506f, (byte) 53);
				break;
			case 4:
				spawn(801903, 1270.4344f, 1455.3026f, 595.29205f, (byte) 21);
				break;
			case 5:
				spawn(801903, 1347.4727f, 1717.1498f, 598.43396f, (byte) 21);
				break;
			case 6:
				spawn(801903, 1393.9829f, 1358.7335f, 598.6061f, (byte) 117);
				break;
			case 7:
				spawn(801903, 1324.5284f, 1554.0479f, 595.5f, (byte) 95);
				break;
		}
	}
	
	protected void reward()
	{
		final int ElyosPvPKills = getPvpKillsByRace(Race.ELYOS).intValue();
		final int ElyosPoints = getPointsByRace(Race.ELYOS).intValue();
		final int AsmoPvPKills = getPvpKillsByRace(Race.ASMODIANS).intValue();
		final int AsmoPoints = getPointsByRace(Race.ASMODIANS).intValue();
		for (final Player player : instance.getPlayersInside())
		{
			if (CreatureActions.isAlreadyDead(player))
			{
				PlayerReviveService.duelRevive(player);
			}
			final KamarBattlefieldPlayerReward playerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
			int abyssPoint = 3163;
			int gloryPoint = 150;
			int expPoint = 10000;
			playerReward.setRewardAp(abyssPoint);
			playerReward.setRewardGp(gloryPoint);
			playerReward.setRewardExp(expPoint);
			if (player.getRace().equals(kamarBattlefieldReward.getWinnerRace()))
			{
				abyssPoint += kamarBattlefieldReward.AbyssReward(true, isVargaKilled(player.getRace()));
				gloryPoint += kamarBattlefieldReward.GloryReward(true, isVargaKilled(player.getRace()));
				expPoint += kamarBattlefieldReward.ExpReward(true, isVargaKilled(player.getRace()));
				playerReward.setBonusAp(kamarBattlefieldReward.AbyssReward(true, isVargaKilled(player.getRace())));
				playerReward.setBonusGp(kamarBattlefieldReward.GloryReward(true, isVargaKilled(player.getRace())));
				playerReward.setBonusExp(kamarBattlefieldReward.ExpReward(true, isVargaKilled(player.getRace())));
				playerReward.setBloodMark(186000236);
				playerReward.setBonusReward(186000243);
				playerReward.setKamarRewardBox(188052660);
			}
			else
			{
				abyssPoint += kamarBattlefieldReward.AbyssReward(false, isVargaKilled(player.getRace()));
				gloryPoint += kamarBattlefieldReward.GloryReward(false, isVargaKilled(player.getRace()));
				expPoint += kamarBattlefieldReward.ExpReward(false, isVargaKilled(player.getRace()));
				playerReward.setRewardAp(kamarBattlefieldReward.AbyssReward(false, isVargaKilled(player.getRace())));
				playerReward.setRewardGp(kamarBattlefieldReward.GloryReward(false, isVargaKilled(player.getRace())));
				playerReward.setRewardExp(kamarBattlefieldReward.ExpReward(false, isVargaKilled(player.getRace())));
				playerReward.setBloodMark(186000236);
				playerReward.setBonusReward(186000243);
			}
			if (RaceKilledVarga == player.getRace())
			{
				playerReward.setAdditionalReward(188052670);
				ItemService.addItem(player, 188052670, 1);
			}
			ItemService.addItem(player, 188052660, 1);
			ItemService.addItem(player, 186000236, 1);
			ItemService.addItem(player, 186000243, 1);
			AbyssPointsService.addAp(player, abyssPoint);
			AbyssPointsService.addGp(player, gloryPoint);
			player.getCommonData().addExp(expPoint, RewardType.HUNTING);
			final QuestEnv env = new QuestEnv(null, player, 0, 0);
			QuestEngine.getInstance().onKamarReward(env);
		}
		for (final Npc npc : instance.getNpcs())
		{
			npc.getController().onDelete();
		}
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!isInstanceDestroyed)
				{
					for (final Player player : instance.getPlayersInside())
					{
						onExitInstance(player);
					}
					AutoGroupService.getInstance().unRegisterInstance(instanceId);
				}
			}
		}, 60000);
	}
	
	private int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 90000)
		{
			return (int) (90000 - result);
		}
		else if (result < 1800000)
		{ // 30-Mins
			return (int) (1800000 - (result - 90000));
		}
		return 0;
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PlayerReviveService.revive(player, 100, 100, false, 0);
		player.getGameStats().updateStatsAndSpeedVisually();
		kamarBattlefieldReward.portToPosition(player);
		return true;
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		final KamarBattlefieldPlayerReward ownerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
		ownerReward.endBoostMoraleEffect(player);
		ownerReward.applyBoostMoraleEffect(player);
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), false, 0, 8));
		int points = 60;
		if (lastAttacker instanceof Player)
		{
			if (lastAttacker.getRace() != player.getRace())
			{
				final InstancePlayerReward playerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
				if (getPointsByRace(lastAttacker.getRace()).compareTo(getPointsByRace(player.getRace())) < 0)
				{
					points *= loosingGroupMultiplier;
				}
				else if ((loosingGroupMultiplier == 10) || (playerReward.getPoints() == 0))
				{
					points = 0;
				}
				updateScore((Player) lastAttacker, player, points, true);
			}
		}
		updateScore(player, player, -points, false);
		return true;
	}
	
	private boolean isVargaKilled(Race PlayerRace)
	{
		if (PlayerRace == RaceKilledVarga)
		{
			return true;
		}
		return false;
	}
	
	private MutableInt getPvpKillsByRace(Race race)
	{
		return kamarBattlefieldReward.getPvpKillsByRace(race);
	}
	
	private MutableInt getPointsByRace(Race race)
	{
		return kamarBattlefieldReward.getPointsByRace(race);
	}
	
	private void addPointsByRace(Race race, int points)
	{
		kamarBattlefieldReward.addPointsByRace(race, points);
	}
	
	private void addPvpKillsByRace(Race race, int points)
	{
		kamarBattlefieldReward.addPvpKillsByRace(race, points);
	}
	
	private void addPointToPlayer(Player player, int points)
	{
		kamarBattlefieldReward.getPlayerReward(player.getObjectId()).addPoints(points);
	}
	
	private void addPvPKillToPlayer(Player player)
	{
		kamarBattlefieldReward.getPlayerReward(player.getObjectId()).addPvPKillToPlayer();
	}
	
	protected void updateScore(Player player, Creature target, int points, boolean pvpKill)
	{
		if (points == 0)
		{
			return;
		}
		addPointsByRace(player.getRace(), points);
		final List<Player> playersToGainScore = new ArrayList<>();
		if ((target != null) && player.isInGroup2())
		{
			for (final Player member : player.getPlayerGroup2().getOnlineMembers())
			{
				if (member.getLifeStats().isAlreadyDead())
				{
					continue;
				}
				if (MathUtil.isIn3dRange(member, target, GroupConfig.GROUP_MAX_DISTANCE))
				{
					playersToGainScore.add(member);
				}
			}
		}
		else
		{
			playersToGainScore.add(player);
		}
		for (final Player playerToGainScore : playersToGainScore)
		{
			addPointToPlayer(playerToGainScore, points / playersToGainScore.size());
			if (target instanceof Npc)
			{
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId((((Npc) target).getObjectTemplate().getNameId() * 2) + 1), points));
			}
			else if (target instanceof Player)
			{
				PacketSendUtility.sendPacket(playerToGainScore, new SM_SYSTEM_MESSAGE(1400237, target.getName(), points));
			}
		}
		int pointDifference = getPointsByRace(Race.ASMODIANS).intValue() - (getPointsByRace(Race.ELYOS)).intValue();
		if (pointDifference < 0)
		{
			pointDifference *= -1;
		}
		if (pointDifference >= 3000)
		{
			loosingGroupMultiplier = 10;
		}
		else if (pointDifference >= 1000)
		{
			loosingGroupMultiplier = 1.5f;
		}
		else
		{
			loosingGroupMultiplier = 1;
		}
		if (pvpKill && (points > 0))
		{
			addPvpKillsByRace(player.getRace(), 1);
			addPvPKillToPlayer(player);
		}
		kamarBattlefieldReward.sendPacket(11, player.getObjectId());
		if (kamarBattlefieldReward.hasCapPoints())
		{
			stopInstance(kamarBattlefieldReward.getWinnerRaceByScore());
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int point = 0;
		final Player mostPlayerDamage = npc.getAggroList().getMostPlayerDamage();
		if (mostPlayerDamage == null)
		{
			return;
		}
		final Race race = mostPlayerDamage.getRace();
		switch (npc.getNpcId())
		{
			case 232841: // Varga Raider Combatant.
			case 232842: // Varga Raider Rampager.
			case 232843: // Varga Raider Gunner.
			case 232844: // Varga Raider Drummer.
			case 232845: // Varga Raider Assaulter.
			case 232846: // Varga Raider Trooper.
			case 233260: // Varga Raider Ambusher.
			case 233261: // Varga Siege Ambusher.
				point = 50;
				despawnNpc(npc);
				break;
			case 801771: // Beritra Iron Fence.
				point = 75;
				despawnNpc(npc);
				break;
			case 232847: // Varga Siege Combatant.
			case 232848: // Varga Siege Rampager.
			case 232849: // Varga Siege Gunner.
			case 232850: // Varga Siege Drummer.
			case 232851: // Varga Siege Assaulter.
				point = 140;
				despawnNpc(npc);
				break;
			case 701909: // Elyos Cannon.
			case 701912: // Elyos Cannon.
				point = 225;
				despawnNpc(npc);
				break;
			case 701910: // Asmodian Cannon.
			case 701911: // Asmodian Cannon.
				point = 225;
				despawnNpc(npc);
				break;
			case 232859: // Hushblade Legion Soldier.
			case 232860: // Fatebound Legion Soldier.
				point = 500;
				despawnNpc(npc);
				break;
			case 232852: // Varga Raider Captain.
			case 232855: // Hushblade Legion Centurion.
			case 232856: // Fatebound Legion Centurion.
				point = 1250;
				despawnNpc(npc);
				break;
			case 233324: // Varga Assault Commander [Starting Point 1]
			case 233325: // Varga Assault Commander [Starting Point 2]
			case 233326: // Varga Assault Commander [Starting Point 3]
				point = 2100;
				despawnNpc(npc);
				break;
			case 233321: // General Varga [Starting Point 1]
			case 233322: // General Varga [Starting Point 2]
			case 233323: // General Varga [Starting Point 3]
				point = 3500;
				despawnNpc(npc);
				// Commander Varga has died.
				sendMsgByRace(1401846, Race.PC_ALL, 0);
				RaceKilledVarga = mostPlayerDamage.getRace();
				ThreadPoolManager.getInstance().schedule(new Runnable()
				{
					@Override
					public void run()
					{
						if (!kamarBattlefieldReward.isRewarded())
						{
							final Race winnerRace = kamarBattlefieldReward.getWinnerRaceByScore();
							stopInstance(winnerRace);
						}
					}
				}, 30000);
				break;
			case 233327: // Acting Commander Cripsin [Starting Point 1]
			case 233329: // Acting Commander Cripsin [Starting Point 2]
				point = 4500;
				despawnNpc(npc);
				// Acting Commander Crispin has died.
				sendMsgByRace(1401849, Race.PC_ALL, 0);
				break;
			case 233328: // Acting Commander Tepes [Starting Point 1]
			case 233330: // Acting Commander Tepes [Starting Point 2]
				point = 4500;
				despawnNpc(npc);
				// Acting Commander Tepes has died.
				sendMsgByRace(1401851, Race.PC_ALL, 0);
				break;
		}
		updateScore(mostPlayerDamage, npc, point, false);
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		int point = 0;
		switch (npc.getNpcId())
		{
			case 801773: // Fuel Barrel.
				point = 75;
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				ItemService.addItem(player, 164000262, 1); // Siege's Power.
				break;
			case 730878: // Reian Guardian Statue.
			case 730879: // Reian Guardian Statue.
			case 730880: // Reian Guardian Statue.
				point = 200;
				break;
			case 801766: // Reian Prisoner.
			case 801767: // Reian Prisoner.
			case 801768: // Reian Prisoner.
			case 801818: // Reian Prisoner.
			case 801819: // Reian Prisoner.
			case 801820: // Reian Prisoner.
			case 801821: // Reian Prisoner.
				point = 225;
				break;
			case 801903: // Garnon.
				point = 1500;
				break;
			case 701906: // Reian Supply Items.
			case 701907: // Reian Supply Items.
			case 701908: // Reian Supply Items.
				point = 525;
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				switch (Rnd.get(1, 4))
				{
					case 1:
						ItemService.addItem(player, 164000261, 1); // Handmade Explosive.
						break;
					case 2:
						ItemService.addItem(player, 162000133, 5); // Kamar Herbal Remedy.
						break;
					case 3:
						ItemService.addItem(player, 186000258, 1); // Kamar Siege Cannonball.
						break;
					case 4:
						ItemService.addItem(player, 164000262, 1); // Siege's Power.
						break;
				}
				break;
			case 801777: // Rack.
				despawnNpc(npc);
				if (player.getInventory().isFull())
				{
					sendMsgByRace(1390149, Race.PC_ALL, 0);
				}
				switch (Rnd.get(1, 3))
				{
					case 1:
						ItemService.addItem(player, 162000135, 5); // Kamar Concoction.
						break;
					case 2:
						ItemService.addItem(player, 162000133, 5); // Kamar Herbal Remedy.
						break;
					case 3:
						ItemService.addItem(player, 162000134, 5); // The Kamar Special.
						break;
				}
				break;
			case 801778: // Wine Glass.
				despawnNpc(npc);
				player.getLifeStats().increaseHp(SM_ATTACK_STATUS.TYPE.HP, 10000);
				player.getLifeStats().increaseMp(SM_ATTACK_STATUS.TYPE.MP, 10000);
				break;
			case 701909: // Kamar Tank Elyos.
			case 701912: // Kamar Tank Elyos.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21403, 1, player).useNoAnimationSkill();
				break;
			case 701910: // Kamar Tank Asmodians.
			case 701911: // Kamar Tank Asmodians.
				despawnNpc(npc);
				SkillEngine.getInstance().getSkill(npc, 21404, 1, player).useNoAnimationSkill();
				break;
			case 701806: // Kamar Cannon.
			case 701902: // Kamar Cannon.
				despawnNpc(npc);
				break;
		}
		updateScore(player, npc, point, false);
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(21403);
		effectController.removeEffect(21404);
		effectController.removeEffect(21731);
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		clearKamarDebuffs(player);
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
		final KamarBattlefieldPlayerReward playerReward = kamarBattlefieldReward.getPlayerReward(player.getObjectId());
		playerReward.endBoostMoraleEffect(player);
		removeEffects(player);
	}
	
	private void clearKamarDebuffs(Player player)
	{
		for (final Effect ef : player.getEffectController().getAbnormalEffects())
		{
			final DispelCategoryType category = ef.getSkillTemplate().getDispelCategory();
			if ((category == DispelCategoryType.DEBUFF) || (category == DispelCategoryType.DEBUFF_MENTAL) || (category == DispelCategoryType.DEBUFF_PHYSICAL) || (category == DispelCategoryType.ALL))
			{
				ef.endEffect();
				player.getEffectController().clearEffect(ef);
			}
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeEffects(player);
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	@Override
	public void onInstanceDestroy()
	{
		kamarBattlefieldReward.clear();
		isInstanceDestroyed = true;
		stopInstanceTask();
		doors.clear();
	}
	
	protected void openFirstDoors()
	{
		openDoor(4);
		openDoor(5);
		openDoor(8);
		openDoor(10);
		openDoor(11);
		openDoor(144);
	}
	
	protected void openDoor(int doorId)
	{
		final StaticDoor door = doors.get(doorId);
		if (door != null)
		{
			door.setOpen(true);
		}
	}
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time)
	{
		sp(npcId, x, y, z, h, 0, time, 0, null);
	}
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final int msg, final Race race)
	{
		sp(npcId, x, y, z, h, 0, time, msg, race);
	}
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int entityId, final int time, final int msg, final Race race)
	{
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!isInstanceDestroyed)
				{
					spawn(npcId, x, y, z, h, entityId);
					if (msg > 0)
					{
						sendMsgByRace(msg, race, 0);
					}
				}
			}
		}, time));
	}
	
	protected void sp(final int npcId, final float x, final float y, final float z, final byte h, final int time, final String walkerId)
	{
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				if (!isInstanceDestroyed)
				{
					final Npc npc = (Npc) spawn(npcId, x, y, z, h);
					npc.getSpawn().setWalkerId(walkerId);
					WalkManager.startWalking((NpcAI2) npc.getAi2());
				}
			}
		}, time));
	}
	
	protected void sendMsgByRace(final int msg, final Race race, int time)
	{
		kamarTask.add(ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				instance.doOnAllPlayers(new Visitor<Player>()
				{
					@Override
					public void visit(Player player)
					{
						if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
						{
							PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
						}
					}
				});
			}
		}, time));
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = kamarTask.head(), end = kamarTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
		}
	}
	
	@Override
	public InstanceReward<?> getInstanceReward()
	{
		return kamarBattlefieldReward;
	}
	
	@Override
	public void onExitInstance(Player player)
	{
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onPlayerLogin(Player player)
	{
		kamarBattlefieldReward.sendPacket(10, player.getObjectId());
	}
}