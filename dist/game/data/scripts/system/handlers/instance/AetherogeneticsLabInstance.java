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

import java.util.Set;

import com.aionemu.commons.utils.Rnd;
import com.aionemu.gameserver.instance.handlers.GeneralInstanceHandler;
import com.aionemu.gameserver.instance.handlers.InstanceID;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.drop.DropItem;
import com.aionemu.gameserver.model.gameobjects.Creature;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIE;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_QUESTION_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.services.drop.DropRegistrationService;
import com.aionemu.gameserver.services.player.PlayerReviveService;
import com.aionemu.gameserver.services.teleport.TeleportService2;
import com.aionemu.gameserver.utils.PacketSendUtility;
import com.aionemu.gameserver.world.WorldMapInstance;

/**
 * @author Rinzler (Encom)
 */
@InstanceID(310050000)
public class AetherogeneticsLabInstance extends GeneralInstanceHandler
{
	@Override
	public void onDropRegistered(Npc npc)
	{
		final Set<DropItem> dropItems = DropRegistrationService.getInstance().getCurrentDropMap().get(npc.getObjectId());
		final int npcId = npc.getNpcId();
		int index = dropItems.size() + 1;
		switch (npcId)
		{
			case 212341: // The Keykeeper.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000001, 1)); // Lepharist Research Center Key 1.
				break;
			}
			case 212175: // Expert Lab Scholar.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000002, 1)); // Lepharist Research Center Key 2.
				break;
			}
			case 212193: // Pretor Key Keeper.
			{
				switch (Rnd.get(1, 2))
				{
					case 1:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000003, 1)); // Lepharist Research Center Key 3.
						break;
					}
					case 2:
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000004, 1)); // Lepharist Research Center Key 4.
						break;
					}
				}
				break;
			}
			case 212202: // Gatekeeper.
			case 212342: // Key Eater.
			{
				dropItems.add(DropRegistrationService.getInstance().regDropItem(1, 0, npcId, 185000005, 1)); // Lepharist Research Center Key 5.
				break;
			}
			case 212211: // RM-78C.
			{
				for (Player player : instance.getPlayersInside())
				{
					if (player.isOnline())
					{
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188053787, 1)); // Stigma Support Bundle.
						dropItems.add(DropRegistrationService.getInstance().regDropItem(index++, player.getObjectId(), npcId, 188052951, 1)); // [Event] Prestige Supplies.
					}
				}
				break;
			}
		}
	}
	
	@Override
	public void onInstanceCreate(WorldMapInstance instance)
	{
		super.onInstanceCreate(instance);
		switch (Rnd.get(1, 2))
		{
			case 1:
			{
				spawn(212193, 206.10997f, 245.62978f, 132.8339f, (byte) 0); // Pretor Key Keeper.
				break;
			}
			case 2:
			{
				spawn(212193, 205.3984f, 215.02821f, 132.83458f, (byte) 0); // Pretor Key Keeper.
				break;
			}
		}
	}
	
	@Override
	public void onDie(Npc npc)
	{
		switch (npc.getObjectTemplate().getTemplateId())
		{
			case 212211: // RM-78C.
			{
				sendMsg("Congratulations: you finish <Aetherogenetics Lab>");
				break;
			}
		}
	}
	
	public void removeItems(Player player)
	{
		final Storage storage = player.getInventory();
		storage.decreaseByItemId(185000001, storage.getItemCountByItemId(185000001)); // Lepharist Research Center Key 1.
		storage.decreaseByItemId(185000002, storage.getItemCountByItemId(185000002)); // Lepharist Research Center Key 2.
		storage.decreaseByItemId(185000003, storage.getItemCountByItemId(185000003)); // Lepharist Research Center Key 3.
		storage.decreaseByItemId(185000004, storage.getItemCountByItemId(185000004)); // Lepharist Research Center Key 4.
		storage.decreaseByItemId(185000005, storage.getItemCountByItemId(185000005)); // Lepharist Research Center Key 5.
	}
	
	@Override
	public void onPlayerLogOut(Player player)
	{
		removeItems(player);
	}
	
	@Override
	public void onLeaveInstance(Player player)
	{
		removeItems(player);
	}
	
	private void sendMsg(String str)
	{
		instance.doOnAllPlayers(player -> PacketSendUtility.sendMessage(player, str));
	}
	
	@Override
	public boolean onReviveEvent(Player player)
	{
		player.getGameStats().updateStatsAndSpeedVisually();
		PlayerReviveService.revive(player, 100, 100, false, 0);
		PacketSendUtility.sendPacket(player, SM_SYSTEM_MESSAGE.STR_REBIRTH_MASSAGE_ME);
		PacketSendUtility.sendPacket(player, new SM_QUESTION_WINDOW(SM_QUESTION_WINDOW.STR_INSTANT_DUNGEON_RESURRECT, 0, 0));
		return TeleportService2.teleportTo(player, mapId, instanceId, 357.83603f, 230.08176f, 147.59962f, (byte) 60);
	}
	
	@Override
	public boolean onDie(Player player, Creature lastAttacker)
	{
		PacketSendUtility.broadcastPacket(player, new SM_EMOTION(player, EmotionType.DIE, 0, player.equals(lastAttacker) ? 0 : lastAttacker.getObjectId()), true);
		PacketSendUtility.sendPacket(player, new SM_DIE(player.haveSelfRezEffect(), player.haveSelfRezItem(), 0, 8));
		return true;
	}
}