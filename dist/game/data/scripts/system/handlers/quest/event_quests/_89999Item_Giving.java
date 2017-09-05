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
package system.handlers.quest.event_quests;

import com.aionemu.gameserver.configs.main.EventsConfig;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.model.items.storage.Storage;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.questEngine.handlers.QuestHandler;
import com.aionemu.gameserver.questEngine.model.QuestEnv;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/
public class _89999Item_Giving extends QuestHandler
{
	private static final int questId = 89999;
	
	public _89999Item_Giving()
	{
		super(questId);
	}
	
	@Override
	public void register()
	{
		qe.registerQuestNpc(799702).addOnTalkEvent(questId); // Laylin.
		qe.registerQuestNpc(799703).addOnTalkEvent(questId); // Ronya.
		qe.registerQuestNpc(798414).addOnTalkEvent(questId); // Brios.
		qe.registerQuestNpc(798416).addOnTalkEvent(questId); // Bothen.
	}
	
	@Override
	public boolean onDialogEvent(QuestEnv env)
	{
		int itemId = 0;
		final int targetId = env.getVisibleObject().getObjectId();
		final Player player = env.getPlayer();
		if ((env.getTargetId() == 799703) || (env.getTargetId() == 799702))
		{
			itemId = EventsConfig.EVENT_GIVE_JUICE;
		}
		else if ((env.getTargetId() == 798416) || (env.getTargetId() == 798414))
		{
			itemId = EventsConfig.EVENT_GIVE_CAKE;
		}
		if (itemId == 0)
		{
			return false;
		}
		switch (env.getDialog())
		{
			case USE_OBJECT:
			{
				PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1011, 0));
				return true;
			}
			case SELECT_ACTION_1012:
			{
				final Storage inventory = player.getInventory();
				if (inventory.getItemCountByItemId(itemId) > 0)
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1097, 0));
					return true;
				}
				if (giveQuestItem(env, itemId, 1))
				{
					PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(targetId, 1012, 0));
				}
				return true;
			}
		}
		return false;
	}
}