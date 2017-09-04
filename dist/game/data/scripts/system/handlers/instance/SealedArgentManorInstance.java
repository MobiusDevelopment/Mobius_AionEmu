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

import java.util.Map;
import java.util.Set;
import java.util.concurrent.Future;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.controllers.effect.PlayerEffectController;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.DescriptionId;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.Race;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.StaticDoor;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.instance.InstanceScoreType;
import com.aionemu.gameserver.model.instance.instancereward.InstanceReward;
import com.aionemu.gameserver.model.instance.instancereward.SealedArgentManorReward;
import com.aionemu.gameserver.model.instance.playerreward.SealedArgentManorPlayerReward;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_INSTANCE_SCORE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.abyss.AbyssPointsService;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.item.ItemService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.skillengine.SkillEngine;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.utils.ThreadPoolManager;
import com.aionemu.gameserver.world.WorldMapInstance;

import javolution.util.FastList;

/**
 * Author Rinzler (Encom) Source: https://www.youtube.com/watch?v=F3EmhC6Qtns Background Story: Due to the Invasion, Sarpan has been destroyed. Drakan Zadra Spellweaver sensing the incoming danger moved "Sealed Argent Manor" somewhere between time and space. However, the Mansion was too big for
 * Zadra and he started to lose his mind. When Zadra woke up he didn't know who he was and all his memories were gone. Now, Elyos and Asmodians are trying to sneak into the "Sealed Argent Manor" and steal the research on the experiment. Entrance: Entrances to the "Sealed Argent Manor" are located in
 * each Territory in "Cygnea and Enshar" After clicking on the passage entrance, choose the 2nd passage and "1 Aetheric Field Piece" will be consumed. "Aetheric Field Piece" can be obtained from various quests or dropped from filed monsters in Kaldor, Levinshor, Cygnea, Enshar. Treasure Box
 * Compensation: Inside the instance players can find "4 Argent Manor Treasure Box" However, since chests are protected by additional monsters it might be hard to collect all of them and still achieve a decent rank.
 **/

@InstanceID(301510000)
public class SealedArgentManorInstance extends GeneralInstanceHandler
{
	private int rank;
	private int hardHetgolem;
	private long instanceTime;
	private boolean isInstanceDestroyed;
	Map<Integer, StaticDoor> doors;
	SealedArgentManorReward instanceReward;
	private final FastList<Future<?>> sealedTask = FastList.newInstance();
	
	protected SealedArgentManorPlayerReward getPlayerReward(Integer object)
	{
		return (SealedArgentManorPlayerReward) instanceReward.getPlayerReward(object);
	}
	
	protected void addPlayerReward(Player player)
	{
		instanceReward.addPlayerReward(new SealedArgentManorPlayerReward(player.getObjectId()));
	}
	
	@SuppressWarnings("unused")
	private boolean containPlayer(Integer object)
	{
		return instanceReward.containPlayer(object);
	}
	
	@Override
	public InstanceReward<?> getInstanceReward()
	{
		return instanceReward;
	}
	
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		switch (npcId)
		{
			case 237190: // Manor Usher.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000242, 1)); // Rechargeable Electric Fuel.
				break;
			/**
			 * Apart from the rank rewards there are many additional items awaiting in the "Argent Manor Treasure Box"
			 */
			case 702816: // Argent Manor Treasure Box.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054117, 1)); // Argent Manor Composite Manastone Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 188054118, 1)); // Argent Manor Ancient Coin Bundle.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166100008, 5)); // Greater Supplements (Eternal).
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 166100011, 5)); // Greater Supplements (Mythic).
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000119, 2)); // Superior Life Potion.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000122, 2)); // Superior Life Serum.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000120, 2)); // Superior Mana Potion.
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 162000123, 2)); // Superior Mana Serum.
				break;
		}
	}
	
	@Override
	public void handleUseItemFinish(Player player, Npc npc)
	{
		switch (npc.getNpcId())
		{
			case 701001: // Transformation Bonfire.
				SkillEngine.getInstance().getSkill(npc, 19316, 60, player).useNoAnimationSkill();
				break;
			case 701002: // Spirit's Bucket.
				SkillEngine.getInstance().getSkill(npc, 19317, 60, player).useNoAnimationSkill();
				break;
			case 701003: // Magic Pinwheel.
				SkillEngine.getInstance().getSkill(npc, 19318, 60, player).useNoAnimationSkill();
				break;
			case 701004: // Magical Soil Mound.
				SkillEngine.getInstance().getSkill(npc, 19319, 60, player).useNoAnimationSkill();
				break;
			case 856547: // Drained Hetgolem.
				final float x = npc.getX();
				final float y = npc.getY();
				final float z = npc.getZ();
				final byte h = npc.getHeading();
				if (player.getInventory().decreaseByItemId(185000242, 1))
				{ // Rechargeable Electric Fuel.
					// Electric fuel used.
					sendMsgByRace(1402978, Race.PC_ALL, 0);
					// Hetgolem activated.
					sendMsgByRace(1402977, Race.PC_ALL, 2000);
					if (!isInstanceDestroyed)
					{
						if ((x > 0) && (y > 0) && (z > 0))
						{
							despawnNpc(npc);
							spawn(237196, x, y, z, h); // Perfectly Restored Hetgolem.
						}
					}
				}
				else
				{
					// Electrical fuel required.
					PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1402976));
				}
				break;
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		int points = 0;
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 282208: // Eldritch Surkana.
				despawnNpc(npc);
				break;
			case 237195: // Elemental Iron Prison.
				despawnNpc(npc);
				deleteNpc(701000); // Nameless Wall.
				break;
			case 237180: // Sturdy Hetgolem.
			case 237181: // Agile Hetgolem.
			case 237182: // Mystic Hetgolem.
				points = 300;
				break;
			case 237200: // Hard Hetgolem.
				hardHetgolem++;
				if (hardHetgolem == 4)
				{
					doors.get(210).setOpen(true);
				}
				points = 400;
				break;
			case 237196: // Perfectly Restored Hetgolem.
				points = 1000;
				break;
			case 237193: // Forgotten Zadra.
			case 237194: // Lost Zadra.
				ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 3000);
				points = 1500;
				despawnNpc(npc);
				break;
		}
		if (instanceReward.getInstanceScoreType().isStartProgress())
		{
			instanceReward.addNpcKill();
			instanceReward.addPoints(points);
			sendPacket(npc.getObjectTemplate().getNameId(), points);
		}
	}
	
	/**
	 * You have up to 15min to finish the instance
	 * @return
	 */
	int getTime()
	{
		final long result = System.currentTimeMillis() - instanceTime;
		if (result < 60000)
		{
			return (int) (60000 - result);
		}
		else if (result < 900000)
		{ // 15 Minutes.
			return (int) (900000 - (result - 60000));
		}
		return 0;
	}
	
	void sendPacket(int nameId, int point)
	{
		instance.doOnAllPlayers(player ->
		{
			if (nameId != 0)
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400237, new DescriptionId((nameId * 2) + 1), point));
			}
			PacketSendUtility.sendPacket(player, new SM_INSTANCE_SCORE(getTime(), instanceReward, null));
		});
	}
	
	private int checkRank(int totalPoints)
	{
		if (totalPoints > 16000)
		{ // Rank S.
			rank = 1;
		}
		else if (totalPoints > 13000)
		{ // Rank A.
			rank = 2;
		}
		else if (totalPoints > 11500)
		{ // Rank B.
			rank = 3;
		}
		else if (totalPoints > 10100)
		{ // Rank C.
			rank = 4;
		}
		else if (totalPoints > 8100)
		{ // Rank D.
			rank = 5;
		}
		else if (totalPoints >= 0)
		{ // Rank F.
			rank = 8;
		}
		else
		{
			rank = 8;
		}
		return rank;
	}
	
	protected void startInstanceTask()
	{
		instanceTime = System.currentTimeMillis();
		sealedTask.add(ThreadPoolManager.getInstance().schedule(() ->
		{
			doors.get(14).setOpen(true);
			instanceReward.setInstanceScoreType(InstanceScoreType.START_PROGRESS);
			sendPacket(0, 0);
		}, 60000));
		sealedTask.add(ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player -> stopInstance(player)), 900000));
	}
	
	@Override
	public void onEnterInstance(Player player)
	{
		if (!instanceReward.containPlayer(player.getObjectId()))
		{
			addPlayerReward(player);
		}
		final SealedArgentManorPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (playerReward.isRewarded())
		{
			doReward(player);
		}
		switch (player.getPlayerClass())
		{
			case RANGER:
			case CLERIC:
			case TEMPLAR:
			case CHANTER:
			case ASSASSIN:
			case GLADIATOR:
				spawn(237193, 819.55664f, 1420.614f, 194.97882f, (byte) 30); // Forgotten Zadra.
				break;
			case SORCERER:
			case GUNSLINGER:
			case SONGWEAVER:
			case AETHERTECH:
			case SPIRIT_MASTER:
				spawn(237194, 819.55664f, 1420.614f, 194.97882f, (byte) 30); // Lost Zadra.
				break;
		}
		sendPacket(0, 0);
	}
	
	protected void stopInstance(Player player)
	{
		stopInstanceTask();
		instanceReward.setRank(6);
		instanceReward.setRank(checkRank(instanceReward.getPoints()));
		instanceReward.setInstanceScoreType(InstanceScoreType.END_PROGRESS);
		doReward(player);
		sendMsg("[Congratulation]: you finish <Sealed Argent Manor 4.9.1>");
		sendPacket(0, 0);
	}
	
	@Override
	public void doReward(Player player)
	{
		final SealedArgentManorPlayerReward playerReward = getPlayerReward(player.getObjectId());
		if (!playerReward.isRewarded())
		{
			playerReward.setRewarded();
			final int manorRank = instanceReward.getRank();
			switch (manorRank)
			{
				case 1: // Rank S
					playerReward.setScoreAP(14000);
					playerReward.setGreaterArgentManorBox(1);
					ItemService.addItem(player, 188054114, 1); // Greater Argent Manor Box.
					break;
				case 2: // Rank A
					playerReward.setScoreAP(12000);
					playerReward.setArgentManorBox(1);
					ItemService.addItem(player, 188054115, 1); // Argent Manor Box.
					break;
				case 3: // Rank B
					playerReward.setScoreAP(10000);
					playerReward.setLesserArgentManorBox(1);
					ItemService.addItem(player, 188054116, 1); // Lesser Argent Manor Box.
					break;
				case 4: // Rank C
					playerReward.setScoreAP(5000);
					playerReward.setLesserArgentManorBox(1);
					ItemService.addItem(player, 188054116, 1); // Lesser Argent Manor Box.
					break;
				case 5: // Rank D
					playerReward.setScoreAP(2500);
					playerReward.setLesserArgentManorBox(1);
					ItemService.addItem(player, 188054116, 1); // Lesser Argent Manor Box.
					break;
				case 6: // Rank F
					break;
			}
			AbyssPointsService.addAp(player, playerReward.getScoreAP());
		}
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	protected void sendMsgByRace(int msg, Race race, int time)
	{
		ThreadPoolManager.getInstance().schedule(() -> instance.doOnAllPlayers(player ->
		{
			if (player.getRace().equals(race) || race.equals(Race.PC_ALL))
			{
				PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(msg));
			}
		}), time);
	}
	
	@Override
	public void onInstanceDestroy()
	{
		stopInstanceTask();
		isInstanceDestroyed = true;
		instanceReward.clear();
		doors.clear();
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		instanceReward = new SealedArgentManorReward(mapId, instanceId);
		instanceReward.setInstanceScoreType(InstanceScoreType.PREPARING);
		doors = instance.getDoors();
		startInstanceTask();
		final Npc npc = instance.getNpc(237195); // Elemental Iron Prison.
		if (npc != null)
		{
			switch (Rnd.get(1, 4))
			{
				case 1: // Resistance: Water.
					SkillEngine.getInstance().getSkill(npc, 19312, 60, npc).useNoAnimationSkill();
					break;
				case 2: // Resistance: Fire.
					SkillEngine.getInstance().getSkill(npc, 19313, 60, npc).useNoAnimationSkill();
					break;
				case 3: // Resistance: Earth.
					SkillEngine.getInstance().getSkill(npc, 19314, 60, npc).useNoAnimationSkill();
					break;
				case 4: // Resistance: Wind.
					SkillEngine.getInstance().getSkill(npc, 19315, 60, npc).useNoAnimationSkill();
					break;
			}
		}
	}
	
	private void stopInstanceTask()
	{
		for (FastList.Node<Future<?>> n = sealedTask.head(), end = sealedTask.tail(); (n = n.getNext()) != end;)
		{
			if (n.getValue() != null)
			{
				n.getValue().cancel(true);
			}
		}
	}
	
	private void despawnNpc(Npc npc)
	{
		if (npc != null)
		{
			npc.getController().onDelete();
		}
	}
	
	private void deleteNpc(int npcId)
	{
		if (getNpc(npcId) != null)
		{
			getNpc(npcId).getController().onDelete();
		}
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeEffects(player);
		TeleportService2.moveToInstanceExit(player, mapId, player.getRace());
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeEffects(player);
		// "Player Name" has left the battle.
		PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1400255, player.getName()));
	}
	
	private void removeEffects(Player player)
	{
		final PlayerEffectController effectController = player.getEffectController();
		effectController.removeEffect(19316);
		effectController.removeEffect(19317);
		effectController.removeEffect(19318);
		effectController.removeEffect(19319);
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 994.6607f, 1183.8448f, 65.64002f, (byte) 90);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}