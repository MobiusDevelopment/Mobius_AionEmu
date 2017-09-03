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
package system.handlers.ai.instance.contaminedUnderpath;

import com.aionemu.commons.network.util.ThreadPoolManager;
import com.aionemu.gameserver.ai2.AI2Actions;
import com.aionemu.gameserver.ai2.AIName;
import com.aionemu.gameserver.ai2.AIState;
import com.aionemu.gameserver.ai2.AbstractAI;
import com.aionemu.gameserver.ai2.NpcAI2;
import com.aionemu.gameserver.model.EmotionType;
import com.aionemu.gameserver.model.gameobjects.Npc;
import com.aionemu.gameserver.model.gameobjects.player.Player;
import com.aionemu.gameserver.network.aion.serverpackets.SM_DIALOG_WINDOW;
import com.aionemu.gameserver.network.aion.serverpackets.SM_EMOTION;
import com.aionemu.gameserver.network.aion.serverpackets.SM_SYSTEM_MESSAGE;
import com.aionemu.gameserver.utils.PacketSendUtility;

/****/
/**
 * Author Rinzler (Encom) /
 ****/

@AIName("underground_path_commander")
public class Underground_Path_CommanderAI2 extends NpcAI2
{
	private boolean isInstanceDestroyed;
	
	@Override
	protected void handleDialogStart(Player player)
	{
		if (player.getLevel() >= 10)
		{
			// Come on in, [%username].
			// This is the last bastion. Until a short while ago, dozens of infected Daevas have attacked this place.
			// Please keep defending it so that the epidemic doesn’t spread to Atreia.
			// Are you ready ?
			PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 1011));
		}
	}
	
	@Override
	public boolean onDialogSelect(Player player, int dialogId, int questId, int extendedRewardIndex)
	{
		// Say you are ready.
		if (dialogId == 10000)
		{
			startInfectedDaevas1();
			startInfectedDaevas2();
			// The Zombies are coming. You have to save Atreia.
			PacketSendUtility.sendPacket(player, new SM_SYSTEM_MESSAGE(1403628));
		}
		PacketSendUtility.sendPacket(player, new SM_DIALOG_WINDOW(getObjectId(), 0));
		AI2Actions.deleteOwner(this);
		return true;
	}
	
	private void startInfectedDaevas1()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				attackUnderground((Npc) spawn(245547, 222.75296f, 282.81735f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 224.62003f, 282.99942f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 226.25397f, 283.0834f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 227.87476f, 283.16495f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 229.36754f, 283.24008f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 231.03639f, 283.3239f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 232.78214f, 283.41165f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 234.50366f, 283.59793f, 160.3114f, (byte) 90), 229.63113f, 225.24828f, 160.28148f, false);
			}
		}, 1000);
	}
	
	private void startInfectedDaevas2()
	{
		ThreadPoolManager.getInstance().schedule(new Runnable()
		{
			@Override
			public void run()
			{
				attackUnderground((Npc) spawn(245547, 222.73087f, 285.1328f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 224.51028f, 285.3419f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 226.20146f, 285.4962f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 227.90823f, 285.6521f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 229.46863f, 285.79477f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 231.11894f, 285.9454f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 232.91904f, 286.11008f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
				attackUnderground((Npc) spawn(245547, 234.55264f, 286.2593f, 160.3114f, (byte) 91), 229.63113f, 225.24828f, 160.28148f, false);
			}
		}, 15000);
	}
	
	private void attackUnderground(Npc npc, float x, float y, float z, boolean despawn)
	{
		((AbstractAI) npc.getAi2()).setStateIfNot(AIState.WALKING);
		npc.setState(1);
		npc.getMoveController().moveToPoint(x, y, z);
		PacketSendUtility.broadcastPacket(npc, new SM_EMOTION(npc, EmotionType.START_EMOTE2, 0, npc.getObjectId()));
	}
	
	public void onInstanceDestroy()
	{
		isInstanceDestroyed = true;
	}
}