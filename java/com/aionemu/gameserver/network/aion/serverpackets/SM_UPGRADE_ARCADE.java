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
package com.aionemu.gameserver.network.aion.serverpackets;

import com.aionemu.gameserver.network.aion.AionConnection;
import com.aionemu.gameserver.network.aion.AionServerPacket;

/**
 * @author Ranastic
 */

public class SM_UPGRADE_ARCADE extends AionServerPacket
{
	private final int type;
	private int chargePoints;
	private boolean display, success, canResume;
	private int frenzymeter;
	private int value;
	private int itemIdReward;
	private int rewardItemCount;
	private int tokenRequire;
	
	public SM_UPGRADE_ARCADE(int type)
	{
		this.type = type;
	}
	
	public SM_UPGRADE_ARCADE(int type, boolean display)
	{
		this.type = type;
		this.display = display;
	}
	
	public SM_UPGRADE_ARCADE(int type, boolean success, int frenzymeter)
	{
		this.type = type;
		this.success = success;
		this.frenzymeter = frenzymeter;
	}
	
	public SM_UPGRADE_ARCADE(int type, int value)
	{
		this.type = type;
		this.value = value;
	}
	
	public SM_UPGRADE_ARCADE(int type, int chargePoints, int frenzymeter)
	{
		this.type = type;
		this.chargePoints = chargePoints;
		this.frenzymeter = frenzymeter;
	}
	
	public SM_UPGRADE_ARCADE(int type, int itemIdReward, int rewardItemCount, int unk)
	{
		this.type = type;
		this.itemIdReward = itemIdReward;
		this.rewardItemCount = rewardItemCount;
	}
	
	public SM_UPGRADE_ARCADE(int type, int value, boolean canResume, int tokenRequire)
	{
		this.type = type;
		this.value = value;
		this.canResume = canResume;
		this.tokenRequire = tokenRequire;
	}
	
	@Override
	protected void writeImpl(AionConnection con)
	{
		writeC(type);
		switch (type)
		{
			case 0:
			{
				writeD(display ? 1 : 0);
				break;
			}
			case 1:
			{
				writeD(chargePoints);
				writeD(frenzymeter);
				writeD(1);
				writeD(4);
				writeD(6);
				writeD(8);
				writeD(8);
				writeH(272);
				writeB("73007500630063006500730073005F0077006500610070006F006E00300031000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300031000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300031000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300032000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300032000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300033000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300033000000");
				writeB("73007500630063006500730073005F0077006500610070006F006E00300034000000");
				break;
			}
			case 2:
			{
				writeC(1);
				break;
			}
			case 3:
			{
				writeC(success ? 1 : 0);
				writeD(frenzymeter);
				break;
			}
			case 4:
			{
				writeD(value);
				break;
			}
			case 5:
			{
				writeD(value);
				writeC(canResume ? 1 : 0);
				writeD(tokenRequire);
				break;
			}
			case 6:
			{
				writeD(itemIdReward);
				writeD(rewardItemCount);
				writeD(0);
				break;
			}
			case 10:
			{
				writeC(8);
				writeC(8);
				writeC(8);
				writeC(8);
				writeB("845FF409010000000000000001000000000000007478350B010000000000000001000000000000006D23160B0A0000000000000014000000000000007723160B010000000000000001000000000000008D71350B010000000000000001000000000000007078350B010000000000000001000000000000005675350B00000000000000001400000000000000C274350B000000000000000001000000000000005F369C0601000000000000000100000000000000C274350B01000000000000000100000000000000845FF409010000000000000001000000000000007878350B0A000000000000001400000000000000AE74350B01000000000000000100000000000000287CE609320000000000000064000000000000007C78350B00000000000000000100000000000000DD76350B00000000000000000100000000000000F60A7407010000000000000001000000000000008D78350B010000000000000001000000000000007178350B010000000000000001000000000000007A78350B010000000000000001000000000000000A76350B010000000000000001000000000000002B7CE6096400000000000000C800000000000000767A530B000000000000000001000000000000007278350B000000000000000001000000000000007B78350B010000000000000001000000000000007C78350B01000000000000000100000000000000833FE709010000000000000001000000000000000A76350B010000000000000001000000000000006E78350B01000000000000000100000000000000C374350B010000000000000001000000000000000B76350B00000000000000000100000000000000C14F260B000000000000000001000000000000000000000000");
				break;
			}
		}
	}
}